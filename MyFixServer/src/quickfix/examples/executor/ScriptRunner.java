package quickfix.examples.executor;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import quickfix.FieldNotFound;
import quickfix.InvalidMessage;
import quickfix.MessageUtils;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.field.ClOrdID;
import quickfix.field.OrderQty;
import quickfix.field.OrigClOrdID;
import quickfix.field.Price;

import com.apama.EngineException;
import com.apama.event.Event;
import com.apama.services.event.EventServiceFactory;
import com.apama.services.event.IEventService;

class OrderNode {
	String ordId;
	HashMap<String, String> tag11;

	OrderNode() {
		tag11 = new HashMap<String, String>();
	}
}

public class ScriptRunner {
	private BufferedReader buffer;
	private Application application;
	State scriptState;
	private Session session;
	IEventService eventService;
	ArrayList<OrderNode> nodes;

	public ScriptRunner(BufferedReader buffer, Application application,
			SessionID sessionID) {
		this.buffer = buffer;
		this.application = application;
		this.session = Session.lookupSession(sessionID);
		this.nodes = new ArrayList<OrderNode>();

	}

	public void run() {
		String messageString;
		this.application.setScript(true);
		int correlatorPort = 15903;
		String correlatorHost = "localhost";
		if (System.getProperty("correlator.port") != null) {
			correlatorPort = Integer.parseInt(System
					.getProperty("correlator.port"));
		}
		if (System.getProperty("correlator.host") != null) {
			correlatorHost = System.getProperty("correlator.host");
		}

		eventService = EventServiceFactory.createEventService(correlatorHost,
				correlatorPort, "OMSSimulator");

		while (true) {
			try {
				messageString = buffer.readLine();

				if (messageString == null) {
					break;
				}
				this.application.setTag11(null);
				this.application.setTag41(null);
				quickfix.Message msg = null;
				if (session.getSessionID().getBeginString()
						.equalsIgnoreCase("FIX.4.2")) {
					msg = (quickfix.fix42.Message) MessageUtils.parse(session,
							messageString);

				} else if (session.getSessionID().getBeginString()
						.equalsIgnoreCase("FIX.4.4")) {
					msg = (quickfix.fix44.Message) MessageUtils.parse(session,
							messageString);

				}

				if (msg instanceof quickfix.fix42.NewOrderSingle
						|| msg instanceof quickfix.fix44.NewOrderSingle) {

					// create a new message

					String[] ret = newOrderToFIX(msg);

					// send the message to the default correlator
					String old11 = msg.getString(ClOrdID.FIELD);
					String ordId = ret[0];
					synchronized (application.getBlock()) {
						sendEventCorrelator(ret[1]);
					}
					// wait for the event to arrive as a new order
					synchronized (this.application.getBlock()) {
						try {

							while (this.application.getLastTag11() == null) {
								System.out.println("waiting for the order");
								this.application.getBlock().wait();
							}
							String new11 = application.getLastTag11()
									.getValue();
							OrderNode node = new OrderNode();
							node.ordId = ordId;
							node.tag11.put(old11, new11);
							this.nodes.add(node);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				} else if (msg instanceof quickfix.fix42.OrderCancelRequest
						|| msg instanceof quickfix.fix44.OrderCancelRequest) {
					// check the order that contain the old request and send the
					// amend
					for (OrderNode node : nodes) {
						if (node.tag11.containsKey(msg
								.getString(OrigClOrdID.FIELD))) {
							// found the original order, Ask the correlator to
							// cancel it
							String event = canceleOrderToFIX(node.ordId, msg);
							sendEventCorrelator(event);
							// wait for the event to arrive as a new order
							synchronized (this.application.getBlock()) {
								try {

									while (this.application.getLastTag11() == null) {
										this.application.getBlock().wait(3000);
										break;
									}
									if (application.getLastTag11() != null) {
										String new11 = application
												.getLastTag11().getValue();
										node.tag11.put(
												msg.getString(ClOrdID.FIELD),
												new11);
									}

								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							break;

						}

					}
				} else if (msg instanceof quickfix.fix42.OrderCancelReplaceRequest
						|| msg instanceof quickfix.fix44.OrderCancelReplaceRequest) {
					// check the order that contain the old request and send the
					// amend
					for (OrderNode node : nodes) {
						if (node.tag11.containsKey(msg
								.getString(OrigClOrdID.FIELD))) {
							// found the original order, Ask the correlator to
							// ammend it
							String event = updateOrderToFIX(node.ordId, msg);
							sendEventCorrelator(event);
							// wait for the event to arrive as a new order
							synchronized (this.application.getBlock()) {
								try {

									while (this.application.getLastTag11() == null) {
										System.out.println("waiting update");
										this.application.getBlock().wait(2000);
										break;
									}
									String new11 = application.getLastTag11()
											.getValue();
									node.tag11
											.put(msg.getString(ClOrdID.FIELD),
													new11);

								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							break;

						}

					}

				} else if (msg instanceof quickfix.fix44.OrderMassCancelRequest) {

				} else {
					if (msg instanceof quickfix.fix42.ExecutionReport
							|| msg instanceof quickfix.fix44.ExecutionReport) {
						// replace the tags 11 and 41 from the original
						boolean found = false;
						if (msg.isSetField(ClOrdID.FIELD)) {
							for (OrderNode node : nodes) {
								if (node.tag11.containsKey(msg
										.getString(ClOrdID.FIELD))) {
									msg.setString(ClOrdID.FIELD, node.tag11
											.get(msg.getString(ClOrdID.FIELD)));

									if (msg.isSetField(OrigClOrdID.FIELD))
										if (node.tag11.containsKey(msg
												.getString(OrigClOrdID.FIELD))) {
											msg.setString(
													OrigClOrdID.FIELD,
													node.tag11.get(msg
															.getString(OrigClOrdID.FIELD)));
										}
									session.send(msg);
									break;

								}

							}
						}

					}
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidMessage e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FieldNotFound e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		this.application.setScript(false);
		eventService.destroy();
	}

	private void sendEventCorrelator(String event) {

		try {

			// create the simpleEvent object
			Event simpleEvent = new Event(event);

			// send it
			eventService.getEngineClient().sendEvents(
					new Event[] { simpleEvent });

			System.out.println(event + " was sent...");

		} catch (EngineException ee) {
			ee.printStackTrace(System.err);
		} finally {
			// eventService.destroy();
		}

	}

	private String[] newOrderToFIX(quickfix.Message msg) {
		// com.apama.oms.NewOrder("4-1542","EURUSD",11,"BUY","LIMIT",100,"__ObjectionBasedFirewallControllerExternal","","","Transport_AppiaBOV","Transport_AppiaBOV","#u",{"Firewall.TargetService":"FIX","ScenarioDisplayName":"Outright"})
		String newOrderTemplate = "com.apama.oms.NewOrder(\"#ID\",\"#SYMBOL\",#PRICE,\"#SIDE\",\"#TYPE\",#QTY,\"#SERVICEID\",\"\",\"\",\"#FIX_TRANSP\",\"#FIX_TRANSP\",\"#USER\",{\"Firewall.TargetService\":\"#TARGET\",\"ScenarioDisplayName\":\"Outright\" #EXTRA})";
		String retMsg = newOrderTemplate;
		String id = UUID.randomUUID().toString();
		String user = System.getProperty("dashboard.user");
		String target = System.getProperty("firewall.target");
		String extra = System.getProperty("order.extra","");
		String serviceId = System.getProperty("adapter.serviceId",
				"__ObjectionBasedFirewallControllerExternal");
		if (target == null)
			target = "FIX";
		if (user == null)
			user = "cpacheco";

		retMsg = retMsg.replaceAll("#ID", id);
		try {
			retMsg = retMsg.replaceAll("#SYMBOL", msg.getString(55));
			retMsg = retMsg.replaceAll("#SIDE", msg.getString(54)
					.equalsIgnoreCase("1") ? "BUY" : "SELL");
			retMsg = retMsg.replaceAll("#TYPE",
					fixTypeTOString(msg.getString(40)));
			retMsg = retMsg.replaceAll("#SERVICEID", serviceId);
			retMsg = retMsg.replaceAll("#PRICE", msg.getString(Price.FIELD));
			retMsg = retMsg.replaceAll("#FIX_TRANSP", this.getFixTransport());
			retMsg = retMsg.replaceAll("#USER", user);
			retMsg = retMsg.replaceAll("#QTY", msg.getString(OrderQty.FIELD));
			retMsg = retMsg.replaceAll("#TARGET", target);
			retMsg = retMsg.replaceAll("#EXTRA",extra);

			return new String[] { id, retMsg };

		} catch (FieldNotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String[] {};

	}

	private String updateOrderToFIX(String orderId, quickfix.Message msg) {
		//
		String template = "com.apama.oms.AmendOrder(\"#ID\",\"#SERVICEID\",\"#SYMBOL\",#PRICE,\"#SIDE\",\"#TYPE\",#QTY,{\"Exchange\":\"#FIX_TRANSP\",\"Firewall.TargetService\":\"#TARGET\",\"Market\":\"#FIX_TRANSP\",\"SERVICE_NAME\":\"__ObjectionBasedFirewallControllerExternal\",\"ScenarioDisplayName\":\"Outright\" #EXTRA})";
		String retMsg = template;
		String id = orderId;
		String user = System.getProperty("dashboard.user");
		
		String target = System.getProperty("firewall.target");
		String extra = System.getProperty("order.extra","");
		if (target == null)
			target = "FIX";
		String serviceId = System.getProperty("adapter.serviceId",
				"__ObjectionBasedFirewallControllerExternal");
		if (user == null)
			user = "cpacheco";

		retMsg = retMsg.replaceAll("#ID", id);
		try {
			retMsg = retMsg.replaceAll("#SYMBOL", msg.getString(55));
			retMsg = retMsg.replaceAll("#SIDE", msg.getString(54)
					.equalsIgnoreCase("1") ? "BUY" : "SELL");
			retMsg = retMsg.replaceAll("#TYPE",
					fixTypeTOString(msg.getString(40)));
			retMsg = retMsg.replaceAll("#PRICE", msg.getString(Price.FIELD));
			retMsg = retMsg.replaceAll("#FIX_TRANSP", this.getFixTransport());
			retMsg = retMsg.replaceAll("#USER", user);
			retMsg = retMsg.replaceAll("#SERVICEID", serviceId);
			retMsg = retMsg.replaceAll("#QTY", msg.getString(OrderQty.FIELD));
			retMsg = retMsg.replaceAll("#QTY", msg.getString(OrderQty.FIELD));
			retMsg = retMsg.replaceAll("#TARGET", target);
			retMsg = retMsg.replaceAll("#EXTRA", extra); 
			
			return retMsg;

		} catch (FieldNotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	private String canceleOrderToFIX(String orderId, quickfix.Message msg) {
		//

		String template = "com.apama.oms.CancelOrder(\"#ID\",\"#SERVICEID\",{\"Exchange\":\"#FIX_TRANSP\",\"Firewall.TargetService\":\"#TARGET\",\"Market\":\"#FIX_TRANSP\",\"SERVICE_NAME\":\"__ObjectionBasedFirewallControllerExternal\",\"ScenarioDisplayName\":\"Outright\"})";
		;
		String retMsg = template;
		String id = orderId;
		String user = System.getProperty("dashboard.user");
		if (user == null)
			user = "cpacheco";
		String target = System.getProperty("firewall.target");
		String serviceId = System.getProperty("adapter.serviceId",
				"__ObjectionBasedFirewallControllerExternal");
		if (target == null)
			target = "FIX";

		retMsg = retMsg.replaceAll("#ID", id);
		try {
			retMsg = retMsg.replaceAll("#SYMBOL", msg.getString(55));
			retMsg = retMsg.replaceAll("#SERVICEID", serviceId);
			retMsg = retMsg.replaceAll("#FIX_TRANSP", this.getFixTransport());
			retMsg = retMsg.replaceAll("#QTY", msg.getString(OrderQty.FIELD));
			retMsg = retMsg.replaceAll("#TARGET", target);
			return retMsg;

		} catch (FieldNotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	private String getFixTransport() {
		String transport = System.getProperties().getProperty("fix.transport");

		return transport == null ? "FIXT" : transport;
	}

	private String fixTypeTOString(String type) {
		int iType = Integer.parseInt(type);
		switch (iType) {
		case 1:
			return "MARKET";
		case 2:
			return "LIMIT";
		case 3:
			return "STOP MARKET";
		default:
			return "UNDEFINED";
		}

	}

}
