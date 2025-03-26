package quickfix.examples.executor;

import java.math.BigInteger;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import quickfix.DataDictionary;
import quickfix.FieldNotFound;
import quickfix.InvalidMessage;
import quickfix.Message;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.StringField;
import quickfix.field.AvgPx;
import quickfix.field.CumQty;
import quickfix.field.CxlRejResponseTo;
import quickfix.field.EncodedText;
import quickfix.field.ExecID;
import quickfix.field.ExecTransType;
import quickfix.field.ExecType;
import quickfix.field.LastPx;
import quickfix.field.LastShares;
import quickfix.field.LeavesQty;
import quickfix.field.OrdStatus;
import quickfix.field.OrderID;
import quickfix.field.OrderQty;
import quickfix.field.Price;
import quickfix.fix42.ExecutionReport;
import quickfix.fix42.NewOrderSingle;
import quickfix.fix42.OrderCancelReject;
import quickfix.fix42.OrderCancelReplaceRequest;

class MyUserInterface {
	SessionID id;
	private Message msg;
	Application parent;
	private Shell frame;
	private String msgType;
	int orderId = 0;
	private Message msg1;
	private Text answer;
	Text received;
	Text qty;
	private float orderQty;
	private float cumQty;

	public MyUserInterface(String msgType, SessionID sessionID, Message msg,
			Application parent, Shell frame, float orderQty, float cumQty) {
		this.id = sessionID;
		this.msgType = msgType;
		this.msg = msg;
		this.frame = frame;
		this.parent = parent;

	}

	public void createUserInterface() throws FieldNotFound {
		frame.getDisplay().syncExec(new Runnable() {

			private TabItem tab;
			private Text error;
			private Text tTag41;
			private Text tTag11;

			@Override
			public void run() {
				TabFolder folder = (TabFolder) frame.getChildren()[0];
				tab = new TabItem(folder, SWT.NULL);

				try {
					tab.setText(msgType + ":" + msg.getString(11));
				} catch (FieldNotFound e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				SashForm form = new SashForm(folder, SWT.HORIZONTAL);
				Composite c = new Composite(form, SWT.None);

				c.setLayout(new FormLayout());

				Label lReceived = new Label(c, SWT.BORDER);
				lReceived.setText("Received:");
				FormData data = new FormData();
				data.left = new FormAttachment(5, 0);
				lReceived.setLayoutData(data);

				received = new Text(c, SWT.BORDER);
				received.setText(msg.toString());

				data = new FormData();
				data.left = new FormAttachment(lReceived, +5);
				data.right = new FormAttachment(80, 0);
				received.setLayoutData(data);

				Button details = new Button(c, SWT.BUTTON1);
				details.setText("See details...");
				details.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent arg0) {
						// frame.setEnabled(false);
						new FixDetails(frame, msg, MyUserInterface.this).show();

					}

					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub

					}
				});

				data = new FormData();
				data.left = new FormAttachment(received, +5);
				// data.top= new FormAttachment(0,+5);
				data.right = new FormAttachment(100, -5);
				details.setLayoutData(data);

				Label tag11 = new Label(c, SWT.BORDER);
				tag11.setText("Tag 11");
				data = new FormData();
				data.top = new FormAttachment(lReceived, +10);
				data.left = new FormAttachment(5, 0);
				tag11.setLayoutData(data);

				tTag11 = new Text(c, SWT.BORDER);
				try {
					tTag11.setText(msg.getString(11));
				} catch (FieldNotFound e1) {
					// TODO Auto-generated catch block
					tTag11.setText("");
				}
				data = new FormData();
				data.top = new FormAttachment(lReceived, +10);
				data.left = new FormAttachment(tag11, 5);
				tTag11.setLayoutData(data);

				Label tag41 = new Label(c, SWT.BORDER);
				tag41.setText("Tag 41");
				data = new FormData();
				data.top = new FormAttachment(tag11, +10);
				data.left = new FormAttachment(5, 0);
				tag41.setLayoutData(data);

				tTag41 = new Text(c, SWT.BORDER);
				try {
					tTag41.setText(msg.getString(41));
				} catch (FieldNotFound e1) {
					// TODO Auto-generated catch block
					tTag41.setText("Field 41 not found");
				}
				data = new FormData();
				data.top = new FormAttachment(tag11, +10);
				data.left = new FormAttachment(tag11, 5);
				tTag41.setLayoutData(data);

				Label lanswer = new Label(c, SWT.BORDER);
				lanswer.setText("Answer:");
				data = new FormData();
				data.top = new FormAttachment(tag41, +10);
				data.left = new FormAttachment(5, 0);
				lanswer.setLayoutData(data);

				answer = new Text(c, SWT.BORDER);
				answer.setText("");
				data = new FormData();
				data.left = new FormAttachment(lanswer, +5);
				data.top = new FormAttachment(tag41, +10);
				data.right = new FormAttachment(70, -5);
				answer.setLayoutData(data);
				Button details1 = new Button(c, SWT.BUTTON1);
				details1.setText("See details...");
				details1.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent arg0) {
						// frame.setEnabled(false);
						if (answer.getText() != null
								&& !answer.getText().equalsIgnoreCase("")) {
							try {
								msg1 = new Message(answer.getText());
								new FixDetails(frame, msg1,
										MyUserInterface.this).show();
							} catch (InvalidMessage e) {
								// TODO Auto-generated catch block
								error.setText(e.getMessage());
								e.printStackTrace();
							}
						}

					}

					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub

					}
				});
				data = new FormData();
				data.left = new FormAttachment(answer, 5);

				data.top = new FormAttachment(tag41, +10);
				details1.setLayoutData(data);

				Button replaceTags = new Button(c, SWT.BUTTON1);
				replaceTags.setText("Replace 11 41");
				replaceTags.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent arg0) {
						// frame.setEnabled(false);
						if (answer.getText() != null
								&& !answer.getText().equalsIgnoreCase("")) {
							try {
								Message msg1 = new Message(answer.getText());
								msg1.removeField(11);
								msg1.removeField(41);
								if (tTag11.getText() != null
										&& !tTag11.getText().equalsIgnoreCase(
												"")) {

									StringField strField = new StringField(11,
											tTag11.getText());

									msg1.setField(strField);
								}
								if (tTag41.getText() != null
										&& !tTag41.getText().equalsIgnoreCase(
												"")) {

									StringField strField = new StringField(41,
											tTag41.getText());

									msg1.setField(strField);
								}
								answer.setText(msg1.toString());

							} catch (InvalidMessage e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								error.setText(e.getMessage());
							}
						}

					}

					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub

					}
				});

				data = new FormData();
				data.left = new FormAttachment(details1, 5);

				data.top = new FormAttachment(tag41, +10);
				replaceTags.setLayoutData(data);

				Button generateFull = new Button(c, SWT.PUSH);
				generateFull.setText("reply full fill ");

				data = new FormData();
				data.top = new FormAttachment(lanswer, +10);
				data.left = new FormAttachment(5, 0);
				generateFull.setLayoutData(data);

				generateFull.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent arg0) {
						String s = generateFullReply();
						answer.setText(s);

					}

					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {

					}

				});

				Button generatePartial = new Button(c, SWT.PUSH);
				generatePartial.setText("reply partial fill ");
				data = new FormData();
				data.top = new FormAttachment(lanswer, +10);
				data.left = new FormAttachment(generateFull, +10);
				generatePartial.setLayoutData(data);

				qty = new Text(c, SWT.BORDER);
				data = new FormData();
				data.top = new FormAttachment(lanswer, +10);
				data.left = new FormAttachment(generatePartial, +10);
				qty.setLayoutData(data);
				generatePartial.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent arg0) {
						if (!qty.getText().isEmpty()) {
							String s = generatePartial();
							answer.setText(s);
						} else {
							MessageBox alert = new MessageBox(frame);
							alert.setText("Please inform quantity");
							alert.setMessage("Please inform quantity");
							alert.open();

						}

					}

					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub

					}
				});

				Button generatePending = new Button(c, SWT.PUSH);
				generatePending.setText("reply pending");
				data = new FormData();
				data.top = new FormAttachment(lanswer, +10);
				data.left = new FormAttachment(qty, +10);
				generatePending.setLayoutData(data);

				generatePending.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub
						String s = generatePending();
						answer.setText(s);

					}

					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {

					}

				});

				Button generateAck = new Button(c, SWT.PUSH);

				generateAck.setText("reply acknowledge");
				data = new FormData();
				data.top = new FormAttachment(lanswer, +10);
				data.left = new FormAttachment(generatePending, +10);
				generateAck.setLayoutData(data);

				generateAck.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub
						String s = generateAck();
						answer.setText(s);

					}

					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {

					}

				});

				Button generateReject = new Button(c, SWT.PUSH);

				generateReject.setText("Reject");
				data = new FormData();
				data.top = new FormAttachment(lanswer, +10);
				data.left = new FormAttachment(generateAck, +10);
				generateReject.setLayoutData(data);

				generateReject.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub
						String s = generateReject();
						answer.setText(s);

					}

					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {

					}

				});

				Label lerror = new Label(c, SWT.BORDER);
				lerror.setText("Error:");
				data = new FormData();
				data.top = new FormAttachment(generateFull, +10);
				data.left = new FormAttachment(5, 0);
				lerror.setLayoutData(data);

				error = new Text(c, SWT.BORDER);
				error.setText("");
				data = new FormData();
				data.left = new FormAttachment(lerror, +5);
				data.top = new FormAttachment(generateFull, +10);
				data.right = new FormAttachment(100, -5);
				error.setLayoutData(data);

				Button close = new Button(c, SWT.PUSH);
				close.setText("close");
				close.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent arg0) {
						tab.dispose();

					}

					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub

					}
				});
				data = new FormData();
				data.top = new FormAttachment(lerror, +5);
				data.left = new FormAttachment(lerror, 0);

				close.setLayoutData(data);

				Button sendIt = new Button(c, SWT.PUSH);
				sendIt.setText("Send answer");
				data = new FormData();
				data.top = new FormAttachment(lerror, +5);
				data.left = new FormAttachment(close, 5);
				data.right = new FormAttachment(100, -5);
				sendIt.setLayoutData(data);
				sendIt.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent arg0) {
						try {
							error.setText("");
							

							Message msg = new Message(answer.getText(), Session.lookupSession(id).getDataDictionary());
							parent.sendMessage(id, msg);
							error.setText("Message was sent successfully");
						} catch (InvalidMessage e) {
							// TODO Auto-generated catch block
							error.setText(e.getMessage());
						} catch (quickfix.FieldException e1) {
							error.setText(e1.getMessage());
							e1.printStackTrace();
						}

					}

					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub

					}
				});

				tab.setControl(form);

				frame.redraw();

			}
		});

	}

	public String generateFullReply() {
		try {
			if (msg instanceof NewOrderSingle) {
				 
				NewOrderSingle no = (NewOrderSingle) msg;
				 
				ExecutionReport execReport = new ExecutionReport(new OrderID(
						"order-" + Integer.toString(orderId)), new ExecID(
						new BigInteger(64, new Random()).toString()),
						new ExecTransType(ExecTransType.NEW), new ExecType(
								ExecType.FILL),
						new OrdStatus(OrdStatus.FILLED), no.getSymbol(),
						no.getSide(), new LeavesQty(0.0), new CumQty(no
								.getOrderQty().getValue()), new AvgPx());
				
				
				
				execReport.set(no.getClOrdID());

				if (no.isSet(new OrderQty()))
					execReport.set(no.getOrderQty());
				execReport.set(new LastShares(no.getOrderQty().getValue()));
				Price price = new Price();
				execReport.set(no.getOrdType());
				if (no.isSet(price)) {
					execReport.set(no.getPrice());
					execReport.set(new LastPx(no.getPrice().getValue()));
				} else {
					execReport.set(new Price());
					execReport.set(new LastPx());

				}

				return execReport.toString();

			} else if (msg instanceof OrderCancelReplaceRequest) {
				OrderCancelReplaceRequest no = (OrderCancelReplaceRequest) msg;

				ExecutionReport execReport = new ExecutionReport(new OrderID(
						"order-" + Integer.toString(orderId)), new ExecID(
						new BigInteger(64, new Random()).toString()),
						new ExecTransType(ExecTransType.NEW), new ExecType(
								ExecType.FILL),
						new OrdStatus(OrdStatus.FILLED), no.getSymbol(),
						no.getSide(), new LeavesQty(0.0), new CumQty(no
								.getOrderQty().getValue()), new AvgPx());
				execReport.set(no.getClOrdID());
				execReport.set(new LastShares(no.getOrderQty().getValue()));
				Price price = new Price();
				execReport.set(no.getOrdType());
				if (no.isSet(price)) {
					execReport.set(no.getPrice());
					execReport.set(new LastPx(no.getPrice().getValue()));
				} else {
					execReport.set(new Price());
					execReport.set(new LastPx());
				}

				return execReport.toString();

			}
		} catch (FieldNotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	private String generatePartial() {
		try {
			if (msg instanceof NewOrderSingle) {
				NewOrderSingle no = (NewOrderSingle) msg;
				orderQty = parent.getOrderQty();
				cumQty = parent.getCumQty();

				ExecutionReport execReport = new ExecutionReport(new OrderID(
						"order-" + Integer.toString(orderId)), new ExecID(
						new BigInteger(64, new Random()).toString()),
						new ExecTransType(ExecTransType.NEW), new ExecType(
								ExecType.PARTIAL_FILL), new OrdStatus(
								OrdStatus.PARTIALLY_FILLED), no.getSymbol(),
						no.getSide(), new LeavesQty(orderQty - cumQty
								- Integer.parseInt(qty.getText())), new CumQty(
								cumQty + Integer.parseInt(qty.getText())),
						new AvgPx());
				execReport.set(no.getClOrdID());
				execReport.set(new LastShares(Integer.parseInt(qty.getText())));
				execReport.set(new OrderQty(orderQty));
				execReport.set(no.getOrdType());
				Price price = new Price();
				parent.setCumQty(cumQty + Integer.parseInt(qty.getText()));
				if (no.isSet(price)) {
					execReport.set(no.getPrice());
					execReport.set(new LastPx(no.getPrice().getValue()));
				} else {
					execReport.set(new Price());
					execReport.set(new LastPx());

				}

				return execReport.toString();

			} else if (msg instanceof OrderCancelReplaceRequest) {
				OrderCancelReplaceRequest no = (OrderCancelReplaceRequest) msg;
				orderQty = (float) no.getOrderQty().getValue();
				cumQty = parent.getCumQty();

				ExecutionReport execReport = new ExecutionReport(new OrderID(
						"order-" + Integer.toString(orderId)), new ExecID(
						new BigInteger(64, new Random()).toString()),
						new ExecTransType(ExecTransType.NEW), new ExecType(
								ExecType.PARTIAL_FILL), new OrdStatus(
								OrdStatus.PARTIALLY_FILLED), no.getSymbol(),
						no.getSide(), new LeavesQty(orderQty - cumQty
								+ Integer.parseInt(qty.getText())), new CumQty(
								no.getOrderQty().getValue()), new AvgPx());
				execReport.set(no.getClOrdID());
				execReport.set(new LastShares(Integer.parseInt(qty.getText())));
				Price price = new Price();
				execReport.set(no.getOrdType());
				execReport.set(new OrderQty(orderQty));
				parent.setCumQty(cumQty + Integer.parseInt(qty.getText()));
				if (no.isSet(price)) {
					execReport.set(no.getPrice());
					execReport.set(new LastPx(no.getPrice().getValue()));
				} else {
					execReport.set(new Price());
					execReport.set(new LastPx());

				}

				return execReport.toString();

			}
		}

		catch (FieldNotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	private String generatePending() {

		try {
			if (msg instanceof NewOrderSingle) {
				NewOrderSingle no = (NewOrderSingle) msg;
				ExecutionReport execReport = new ExecutionReport(new OrderID(
						"order-" + Integer.toString(orderId)), new ExecID(
						new BigInteger(64, new Random()).toString()),
						new ExecTransType(ExecTransType.CORRECT), new ExecType(
								ExecType.PENDING_NEW), new OrdStatus(
								OrdStatus.PENDING_NEW), no.getSymbol(),
						no.getSide(),
						new LeavesQty(no.getOrderQty().getValue()), new CumQty(
								0.0), new AvgPx());
				execReport.set(no.getClOrdID());
				execReport.set(no.getOrdType());
				execReport.set(new LastPx(0));
				execReport.set(new LastShares(0));
				execReport.set(no.getOrderQty());
				Price price = new Price();
				if (no.isSet(price))
					execReport.set(no.getPrice());
				return execReport.toString();
			} else if (msg instanceof OrderCancelReplaceRequest) {
				OrderCancelReplaceRequest no = (OrderCancelReplaceRequest) msg;
				ExecutionReport execReport = new ExecutionReport(new OrderID(
						"order-" + Integer.toString(orderId)), new ExecID(
						new BigInteger(64, new Random()).toString()),
						new ExecTransType(ExecTransType.CORRECT), new ExecType(
								ExecType.PENDING_REPLACE), new OrdStatus(
								OrdStatus.PENDING_REPLACE), no.getSymbol(),
						no.getSide(),
						new LeavesQty(no.getOrderQty().getValue()), new CumQty(
								0.0), new AvgPx());
				execReport.set(no.getClOrdID());
				execReport.set(new LastPx(0));
				execReport.set(no.getOrdType());
				execReport.set(no.getOrderQty());
				execReport.set(new LastShares(0));
				Price price = new Price();
				if (no.isSet(price))
					execReport.set(no.getPrice());
				return execReport.toString();
			}

		} catch (FieldNotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public String generateAck() {
		try {
			if (msg instanceof NewOrderSingle) {
				NewOrderSingle no = (NewOrderSingle) msg;

				ExecutionReport execReport = new ExecutionReport(new OrderID(
						"order-" + Integer.toString(orderId)), new ExecID(
						new BigInteger(64, new Random()).toString()),
						new ExecTransType(ExecTransType.CORRECT), new ExecType(
								ExecType.NEW), new OrdStatus(OrdStatus.NEW),
						no.getSymbol(), no.getSide(), new LeavesQty(no
								.getOrderQty().getValue()), new CumQty(0.0),
						new AvgPx());

				execReport.set(no.getClOrdID());
				execReport.set(new LastPx(0));
				execReport.set(new LastShares(0));
				execReport.set(no.getOrdType());
				execReport.set(no.getOrderQty());
				Price price = new Price();
				if (no.isSet(new OrderQty()))
					execReport.set(no.getOrderQty());

				if (no.isSet(price))
					execReport.set(no.getPrice());

				return execReport.toString();

			} else if (msg instanceof OrderCancelReplaceRequest) {
				OrderCancelReplaceRequest no = (OrderCancelReplaceRequest) msg;
				ExecutionReport execReport = new ExecutionReport(new OrderID(
						"order-" + Integer.toString(orderId)), new ExecID(
						new BigInteger(64, new Random()).toString()),
						new ExecTransType(ExecTransType.NEW), new ExecType(
								ExecType.REPLACE), new OrdStatus(
								OrdStatus.REPLACED), no.getSymbol(),
						no.getSide(),
						new LeavesQty(no.getOrderQty().getValue()), new CumQty(
								0.0), new AvgPx());
				execReport.set(no.getClOrdID());
				execReport.set(new LastPx(0));
				execReport.set(new LastShares(0));
				execReport.set(no.getOrdType());
				execReport.set(no.getOrderQty());
				Price price = new Price();
				if (no.isSet(price))
					execReport.set(no.getPrice());

				return execReport.toString();
			}
		} catch (FieldNotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private String generateReject() {
		try {
			if (msg instanceof OrderCancelReplaceRequest) {
				OrderCancelReplaceRequest no = (OrderCancelReplaceRequest) msg;

				OrderCancelReject reject = new OrderCancelReject(
						no.getOrderID(), no.getClOrdID(), no.getOrigClOrdID(),
						new OrdStatus(OrdStatus.REJECTED), 
						new CxlRejResponseTo(
								CxlRejResponseTo.ORDER_CANCEL_REPLACE_REQUEST));
				// no.getOrderID(),
				// no.getClOrdID(), no.getOrigClOrdID(), OrdStatus.REPLACED,aasdfasd
				// CxlRejResponseTo.ORDER_CANCEL_REPLACE_REQUEST);
				reject.set(new quickfix.field.Text("Because I CAN"));

				// OrderCancelReject reject = new OrderCancelReject
				/*
				 * execReport.set(no.getClOrdID()); execReport.set(new
				 * LastPx(0)); execReport.set(new LastShares(0));
				 * execReport.set(no.getOrdType());
				 * execReport.set(no.getOrderQty()); Price price = new Price();
				 * if (no.isSet(price)) execReport.set(no.getPrice());
				 * 
				 * return execReport.toString();
				 */	
				return reject.toString();
			}
			else 
				return "";
		} catch (FieldNotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public void updateMsgs() {
		if (msg1 != null) {
			answer.setText(msg1.toString());

		}
		if (msg != null) {
			received.setText(msg.toString());
		}
	}

}