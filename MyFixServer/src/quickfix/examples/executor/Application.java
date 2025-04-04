/*******************************************************************************
 * Copyright (c) quickfixengine.org  All rights reserved. 
 * 
 * This file is part of the QuickFIX FIX Engine 
 * 
 * This file may be distributed under the terms of the quickfixengine.org 
 * license as defined by quickfixengine.org and appearing in the file 
 * LICENSE included in the packaging of this file. 
 * 
 * This file is provided AS IS with NO WARRANTY OF ANY KIND, INCLUDING 
 * THE WARRANTY OF DESIGN, MERCHANTABILITY AND FITNESS FOR A 
 * PARTICULAR PURPOSE. 
 * 
 * See http://www.quickfixengine.org/LICENSE for licensing information. 
 * 
 * Contact ask@quickfixengine.org if any conditions of this licensing 
 * are not clear to you.
 ******************************************************************************/

package quickfix.examples.executor;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quickfix.ConfigError;
import quickfix.DataDictionaryProvider;
import quickfix.DoNotSend;
import quickfix.FieldConvertError;
import quickfix.FieldNotFound;
import quickfix.FixVersions;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.InvalidMessage;
import quickfix.LogUtil;
import quickfix.Message;
import quickfix.MessageUtils;
import quickfix.RejectLogon;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.SessionSettings;
import quickfix.UnsupportedMessageType;
import quickfix.field.ApplVerID;
import quickfix.field.AvgPx;
import quickfix.field.ClOrdID;
import quickfix.field.CumQty;
import quickfix.field.ExecID;
import quickfix.field.ExecTransType;
import quickfix.field.ExecType;
import quickfix.field.LastPx;
import quickfix.field.LastQty;
import quickfix.field.LastShares;
import quickfix.field.LeavesQty;
import quickfix.field.OrdStatus;
import quickfix.field.OrdType;
import quickfix.field.OrderID;
import quickfix.field.OrderQty;
import quickfix.field.OrigClOrdID;
import quickfix.field.Price;
import quickfix.field.Side;
import quickfix.field.Symbol;

public class Application extends quickfix.MessageCracker implements
		quickfix.Application {
	private static final String DEFAULT_MARKET_PRICE_KEY = "DefaultMarketPrice";
	private static final String ALWAYS_FILL_LIMIT_KEY = "AlwaysFillLimitOrders";
	private static final String VALID_ORDER_TYPES_KEY = "ValidOrderTypes";

	private final Logger log = LoggerFactory.getLogger(getClass());
	private final boolean alwaysFillLimitOrders;
	private final HashSet<String> validOrderTypes = new HashSet<String>();
	private MarketDataProvider marketDataProvider;
	private Shell frame;
	private float orderQty;
	private Object block = new Object();

	public float getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(float orderQty) {
		this.orderQty = orderQty;
	}

	public float getCumQty() {
		return cumQty;
	}

	public void setCumQty(float cumQty) {
		this.cumQty = cumQty;
	}

	private float cumQty;
	private OrigClOrdID lastOrigOrdId;
	private ClOrdID lastClOrdID;

	public Application(SessionSettings settings) throws ConfigError,
			FieldConvertError {
		initializeValidOrderTypes(settings);
		initializeMarketDataProvider(settings);

		if (settings.isSetting(ALWAYS_FILL_LIMIT_KEY)) {
			alwaysFillLimitOrders = settings.getBool(ALWAYS_FILL_LIMIT_KEY);
		} else {
			alwaysFillLimitOrders = false;
		}

	}

	private void initializeMarketDataProvider(SessionSettings settings)
			throws ConfigError, FieldConvertError {
		if (settings.isSetting(DEFAULT_MARKET_PRICE_KEY)) {
			if (marketDataProvider == null) {
				final double defaultMarketPrice = settings
						.getDouble(DEFAULT_MARKET_PRICE_KEY);
				marketDataProvider = new MarketDataProvider() {
					public double getAsk(String symbol) {
						return defaultMarketPrice;
					}

					public double getBid(String symbol) {
						return defaultMarketPrice;
					}
				};
			} else {
				log.warn("Ignoring " + DEFAULT_MARKET_PRICE_KEY
						+ " since provider is already defined.");
			}
		}
	}

	private void initializeValidOrderTypes(SessionSettings settings)
			throws ConfigError, FieldConvertError {
		if (settings.isSetting(VALID_ORDER_TYPES_KEY)) {
			List<String> orderTypes = Arrays
					.asList(settings.getString(VALID_ORDER_TYPES_KEY).trim()
							.split("\\s*,\\s*"));
			validOrderTypes.addAll(orderTypes);
		} else {
			validOrderTypes.add(OrdType.LIMIT + "");
		}
	}

	public void onCreate(SessionID sessionID) {
		Session.lookupSession(sessionID).getLog()
				.onEvent("Valid order types: " + validOrderTypes);
	}

	public void onLogon(SessionID sessionID) {

		frame.getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				MessageBox msg = new MessageBox(frame);
				msg.setMessage("Client connected");
				msg.open();

			}
		});
		synchronized (this.frame) {
			ConnUserInterface conn = new ConnUserInterface("Ceonnction",
					sessionID, null, this, frame, orderQty, cumQty);
			try {
				conn.createUserInterface();
			} catch (FieldNotFound e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public void onLogout(SessionID sessionID) {
	}

	public void toAdmin(quickfix.Message message, SessionID sessionID) {
	}

	public void toApp(quickfix.Message message, SessionID sessionID)
			throws DoNotSend {
	}

	public void fromAdmin(quickfix.Message message, SessionID sessionID)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
			RejectLogon {
	}

	public void fromApp(quickfix.Message message, SessionID sessionID)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
			UnsupportedMessageType {
		crack(message, sessionID);
	}

	public void onMessage(quickfix.fix42.OrderCancelRequest order,
			SessionID sessionID) throws FieldNotFound, UnsupportedMessageType,
			IncorrectTagValue {
		synchronized (this.frame) {
			if (!script) {
				MyUserInterface interf = new MyUserInterface("Cancel order",
						sessionID, order, this, frame, orderQty, cumQty);
				interf.createUserInterface();
			} else {

				this.setTag41(order.get(new OrigClOrdID()));
				this.setTag11(order.get(new ClOrdID()));
				synchronized (this.getBlock()) {
					this.getBlock().notify();
				}

			}
		}

	}

	public void onMessage(quickfix.fix40.NewOrderSingle order,
			SessionID sessionID) throws FieldNotFound, UnsupportedMessageType,
			IncorrectTagValue {
		try {
			validateOrder(order);

			OrderQty orderQty = order.getOrderQty();

			Price price = getPrice(order);

			quickfix.fix40.ExecutionReport accept = new quickfix.fix40.ExecutionReport(
					genOrderID(), genExecID(), new ExecTransType(
							ExecTransType.NEW), new OrdStatus(OrdStatus.NEW),
					order.getSymbol(), order.getSide(), orderQty,
					new LastShares(0), new LastPx(0), new CumQty(0), new AvgPx(
							0));

			accept.set(order.getClOrdID());

			if (isOrderExecutable(order, price)) {
				quickfix.fix40.ExecutionReport fill = new quickfix.fix40.ExecutionReport(
						genOrderID(), genExecID(), new ExecTransType(
								ExecTransType.NEW), new OrdStatus(
								OrdStatus.FILLED), order.getSymbol(),
						order.getSide(), orderQty, new LastShares(
								orderQty.getValue()), new LastPx(
								price.getValue()), new CumQty(
								orderQty.getValue()), new AvgPx(
								price.getValue()));

				fill.set(order.getClOrdID());

				sendMessage(sessionID, fill);
			}
		} catch (RuntimeException e) {
			LogUtil.logThrowable(sessionID, e.getMessage(), e);
		}
	}

	private boolean isOrderExecutable(Message order, Price price)
			throws FieldNotFound {
		if (order.getChar(OrdType.FIELD) == OrdType.LIMIT) {
			BigDecimal limitPrice = new BigDecimal(order.getString(Price.FIELD));
			char side = order.getChar(Side.FIELD);
			BigDecimal thePrice = new BigDecimal("" + price.getValue());

			return (side == Side.BUY && thePrice.compareTo(limitPrice) <= 0)
					|| ((side == Side.SELL || side == Side.SELL_SHORT) && thePrice
							.compareTo(limitPrice) >= 0);
		}
		return true;
	}

	private Price getPrice(Message message) throws FieldNotFound {
		Price price;
		if (message.getChar(OrdType.FIELD) == OrdType.LIMIT
				&& alwaysFillLimitOrders) {
			price = new Price(message.getDouble(Price.FIELD));
		} else {
			if (marketDataProvider == null) {
				throw new RuntimeException(
						"No market data provider specified for market order");
			}
			char side = message.getChar(Side.FIELD);
			if (side == Side.BUY) {
				price = new Price(marketDataProvider.getAsk(message
						.getString(Symbol.FIELD)));
			} else if (side == Side.SELL || side == Side.SELL_SHORT) {
				price = new Price(marketDataProvider.getBid(message
						.getString(Symbol.FIELD)));
			} else {
				throw new RuntimeException("Invalid order side: " + side);
			}
		}
		return price;
	}

	public void sendMessage(SessionID sessionID, Message message) {
		try {
			Session session = Session.lookupSession(sessionID);
			if (session == null) {
				throw new SessionNotFound(sessionID.toString());
			}

			DataDictionaryProvider dataDictionaryProvider = session
					.getDataDictionaryProvider();
			if (dataDictionaryProvider != null) {
				try {
					dataDictionaryProvider.getApplicationDataDictionary(
							getApplVerID(session, message), null).validate(
							message, true);
				} catch (Exception e) {
					LogUtil.logThrowable(
							sessionID,
							"Outgoing message failed validation: "
									+ e.getMessage(), e);
					return;
				}
			}

			session.send(message);
		} catch (SessionNotFound e) {
			log.error(e.getMessage(), e);
		}
	}

	private ApplVerID getApplVerID(Session session, Message message) {
		String beginString = session.getSessionID().getBeginString();
		if (FixVersions.BEGINSTRING_FIXT11.equals(beginString)) {
			return new ApplVerID(ApplVerID.FIX50);
		} else {
			return MessageUtils.toApplVerID(beginString);
		}
	}

	public void onMessage(quickfix.fix41.NewOrderSingle order,
			SessionID sessionID) throws FieldNotFound, UnsupportedMessageType,
			IncorrectTagValue {
		try {
			validateOrder(order);

			OrderQty orderQty = order.getOrderQty();
			Price price = getPrice(order);

			quickfix.fix41.ExecutionReport accept = new quickfix.fix41.ExecutionReport(
					genOrderID(), genExecID(), new ExecTransType(
							ExecTransType.NEW), new ExecType(ExecType.FILL),
					new OrdStatus(OrdStatus.NEW), order.getSymbol(),
					order.getSide(), orderQty, new LastShares(0),
					new LastPx(0), new LeavesQty(0), new CumQty(0),
					new AvgPx(0));

			accept.set(order.getClOrdID());
			sendMessage(sessionID, accept);

			if (isOrderExecutable(order, price)) {
				quickfix.fix41.ExecutionReport executionReport = new quickfix.fix41.ExecutionReport(
						genOrderID(), genExecID(), new ExecTransType(
								ExecTransType.NEW),
						new ExecType(ExecType.FILL), new OrdStatus(
								OrdStatus.FILLED), order.getSymbol(),
						order.getSide(), orderQty, new LastShares(
								orderQty.getValue()), new LastPx(
								price.getValue()), new LeavesQty(0),
						new CumQty(orderQty.getValue()), new AvgPx(
								price.getValue()));

				executionReport.set(order.getClOrdID());

				sendMessage(sessionID, executionReport);
			}
		} catch (RuntimeException e) {
			LogUtil.logThrowable(sessionID, e.getMessage(), e);
		}
	}

	public void onMessage(quickfix.fix42.OrderCancelReplaceRequest order,
			SessionID sessionID) throws FieldNotFound, UnsupportedMessageType,
			IncorrectTagValue {
		synchronized (this.frame) {
			if (!script) {

				MyUserInterface interf = new MyUserInterface("Cancel/Replace",
						sessionID, order, this, frame, orderQty, cumQty);
				interf.createUserInterface();

			} else {

				this.setTag41(order.get(new OrigClOrdID()));
				this.setTag11(order.get(new ClOrdID()));
				synchronized (this.getBlock()) {
					System.out.println("notifying update");
					this.getBlock().notify();
				}

			}

		}

	}

	public void setTag11(ClOrdID clOrdID) {
		this.lastClOrdID = clOrdID;

	}

	public void setTag41(OrigClOrdID origClOrdID) {
		this.lastOrigOrdId = origClOrdID;

	}

	public OrigClOrdID getLastTag41() {
		return this.lastOrigOrdId;
	}

	public ClOrdID getLastTag11() {
		return this.lastClOrdID;
	}

	public void onMessage(quickfix.fix44.OrderCancelReplaceRequest order,
			SessionID sessionID) throws FieldNotFound, UnsupportedMessageType,
			IncorrectTagValue {
		synchronized (this.frame) {
			if (!script) {

				MyUserInterface interf = new MyUserInterface("Cancel/Replace",
						sessionID, order, this, frame, orderQty, cumQty);
				interf.createUserInterface();

			} else {

				this.setTag41(order.get(new OrigClOrdID()));
				this.setTag11(order.get(new ClOrdID()));
				synchronized (this.getBlock()) {
					System.out.println("notifying update");
					this.getBlock().notify();
				}

			}

		}

	}

	public void onMessage(quickfix.fix44.OrderCancelRequest order,
			SessionID sessionID) throws FieldNotFound, UnsupportedMessageType,
			IncorrectTagValue {
		synchronized (this.frame) {
			if (!script) {
				MyUserInterface interf = new MyUserInterface("Cancel order",
						sessionID, order, this, frame, orderQty, cumQty);
				interf.createUserInterface();
			} else {

				this.setTag41(order.get(new OrigClOrdID()));
				this.setTag11(order.get(new ClOrdID()));
				synchronized (this.getBlock()) {
					this.getBlock().notify();
				}

			}
		}

	}

	public void onMessage(quickfix.fix42.NewOrderSingle order,
			SessionID sessionID) throws FieldNotFound, UnsupportedMessageType,
			IncorrectTagValue {
		try {

			synchronized (this.frame) {
				cumQty = 0;
				orderQty = (float) order.getOrderQty().getValue();
				if (!script) {
					
					MyUserInterface interf = new MyUserInterface("New Order",
							sessionID, order, this, frame, orderQty, cumQty);

					if (System.getProperty("showOrder", "true")
							.equalsIgnoreCase("true")) {
						interf.createUserInterface();

					}
					if (System.getProperty("autoack", "false")
							.equalsIgnoreCase("true")) {
						String ack = interf.generateAck();
						String autoFill = interf.generateFullReply();
						Message msg = new Message(ack, Session.lookupSession(
								sessionID).getDataDictionary());
						this.sendMessage(sessionID, msg);
						msg = new Message(autoFill, Session.lookupSession(
								sessionID).getDataDictionary());
						this.sendMessage(sessionID, msg); 

					}
					

				} else {

					this.setTag41(null);
					this.setTag11(order.get(new ClOrdID()));
					synchronized (this.getBlock()) {
						System.out.println("notifying new order");
						this.getBlock().notify();
					}
				}

			}

			/*
			 * if (isOrderExecutable(order, price)) {
			 * quickfix.fix42.ExecutionReport executionReport = new
			 * quickfix.fix42.ExecutionReport( genOrderID(), genExecID(), new
			 * ExecTransType( ExecTransType.NEW), new ExecType(ExecType.FILL),
			 * new OrdStatus( OrdStatus.FILLED), order.getSymbol(), order
			 * .getSide(), new LeavesQty(0), new CumQty( orderQty.getValue()),
			 * new AvgPx(price .getValue()));
			 * 
			 * executionReport.set(order.getClOrdID());
			 * executionReport.set(orderQty); executionReport.set(new
			 * LastShares(orderQty.getValue())); executionReport.set(new
			 * LastPx(price.getValue()));
			 * 
			 * sendMessage(sessionID, executionReport);
			 * 
			 * }
			 */

		} catch (RuntimeException e) {
			LogUtil.logThrowable(sessionID, e.getMessage(), e);
		} catch (InvalidMessage e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void validateOrder(Message order) throws IncorrectTagValue,
			FieldNotFound {
		OrdType ordType = new OrdType(order.getChar(OrdType.FIELD));
		if (!validOrderTypes.contains(Character.toString(ordType.getValue()))) {
			log.error("Order type not in ValidOrderTypes setting");
			throw new IncorrectTagValue(ordType.getField());
		}
		if (ordType.getValue() == OrdType.MARKET && marketDataProvider == null) {
			log.error("DefaultMarketPrice setting not specified for market order");
			throw new IncorrectTagValue(ordType.getField());
		}
	}

	public void onMessage(quickfix.fix43.NewOrderSingle order,
			SessionID sessionID) throws FieldNotFound, UnsupportedMessageType,
			IncorrectTagValue {
		try {
			validateOrder(order);

			OrderQty orderQty = order.getOrderQty();
			Price price = getPrice(order);

			quickfix.fix43.ExecutionReport accept = new quickfix.fix43.ExecutionReport(
					genOrderID(), genExecID(), new ExecType(ExecType.FILL),
					new OrdStatus(OrdStatus.NEW), order.getSide(),
					new LeavesQty(order.getOrderQty().getValue()),
					new CumQty(0), new AvgPx(0));

			accept.set(order.getClOrdID());
			accept.set(order.getSymbol());
			sendMessage(sessionID, accept);

			if (isOrderExecutable(order, price)) {
				quickfix.fix43.ExecutionReport executionReport = new quickfix.fix43.ExecutionReport(
						genOrderID(), genExecID(), new ExecType(ExecType.FILL),
						new OrdStatus(OrdStatus.FILLED), order.getSide(),
						new LeavesQty(0), new CumQty(orderQty.getValue()),
						new AvgPx(price.getValue()));

				executionReport.set(order.getClOrdID());
				executionReport.set(order.getSymbol());
				executionReport.set(orderQty);
				executionReport.set(new LastQty(orderQty.getValue()));
				executionReport.set(new LastPx(price.getValue()));
				sendMessage(sessionID, executionReport);
			}
		} catch (RuntimeException e) {
			LogUtil.logThrowable(sessionID, e.getMessage(), e);
		}
	}

	public void onMessage(quickfix.fix44.NewOrderSingle order,
			SessionID sessionID) throws FieldNotFound, UnsupportedMessageType,
			IncorrectTagValue {

		try {

			synchronized (this.frame) {
				cumQty = 0;
				orderQty = (float) order.getOrderQty().getValue();
				if (!script) {

					MyUserInterface interf = new MyUserInterface("New Order",
							sessionID, order, this, frame, orderQty, cumQty);
					interf.createUserInterface();

				} else {

					this.setTag41(null);
					this.setTag11(order.get(new ClOrdID()));
					synchronized (this.getBlock()) {
						System.out.println("notifying new order");
						this.getBlock().notify();
					}
				}

			}

			/*
			 * if (isOrderExecutable(order, price)) {
			 * quickfix.fix42.ExecutionReport executionReport = new
			 * quickfix.fix42.ExecutionReport( genOrderID(), genExecID(), new
			 * ExecTransType( ExecTransType.NEW), new ExecType(ExecType.FILL),
			 * new OrdStatus( OrdStatus.FILLED), order.getSymbol(), order
			 * .getSide(), new LeavesQty(0), new CumQty( orderQty.getValue()),
			 * new AvgPx(price .getValue()));
			 * 
			 * executionReport.set(order.getClOrdID());
			 * executionReport.set(orderQty); executionReport.set(new
			 * LastShares(orderQty.getValue())); executionReport.set(new
			 * LastPx(price.getValue()));
			 * 
			 * sendMessage(sessionID, executionReport);
			 * 
			 * }
			 */

		} catch (RuntimeException e) {
			LogUtil.logThrowable(sessionID, e.getMessage(), e);
		} 

	}

	public void onMessage(quickfix.fix50.NewOrderSingle order,
			SessionID sessionID) throws FieldNotFound, UnsupportedMessageType,
			IncorrectTagValue {
		try {
			validateOrder(order);

			OrderQty orderQty = order.getOrderQty();
			Price price = getPrice(order);

			quickfix.fix50.ExecutionReport accept = new quickfix.fix50.ExecutionReport(
					genOrderID(), genExecID(), new ExecType(ExecType.FILL),
					new OrdStatus(OrdStatus.NEW), order.getSide(),
					new LeavesQty(order.getOrderQty().getValue()),
					new CumQty(0));

			accept.set(order.getClOrdID());
			accept.set(order.getSymbol());
			sendMessage(sessionID, accept);

			if (isOrderExecutable(order, price)) {
				quickfix.fix50.ExecutionReport executionReport = new quickfix.fix50.ExecutionReport(
						genOrderID(), genExecID(), new ExecType(ExecType.FILL),
						new OrdStatus(OrdStatus.FILLED), order.getSide(),
						new LeavesQty(0), new CumQty(orderQty.getValue()));

				executionReport.set(order.getClOrdID());
				executionReport.set(order.getSymbol());
				executionReport.set(orderQty);
				executionReport.set(new LastQty(orderQty.getValue()));
				executionReport.set(new LastPx(price.getValue()));
				executionReport.set(new AvgPx(price.getValue()));

				sendMessage(sessionID, executionReport);
			}
		} catch (RuntimeException e) {
			LogUtil.logThrowable(sessionID, e.getMessage(), e);
		}
	}

	public void increaseCumQty(int qty) {

	}

	public OrderID genOrderID() {
		return new OrderID(Integer.valueOf(++m_orderID).toString());
	}

	public ExecID genExecID() {
		return new ExecID(Integer.valueOf(++m_execID).toString());
	}

	/**
	 * Allows a custom market data provider to be specified.
	 * 
	 * @param marketDataProvider
	 */
	public void setMarketDataProvider(MarketDataProvider marketDataProvider) {
		this.marketDataProvider = marketDataProvider;
	}

	private int m_orderID = 0;
	private int m_execID = 0;
	private boolean script;

	public void setFrame(Shell frame) {
		this.frame = frame;

	}

	public void setScript(boolean b) {
		this.script = b;

	}

	public Object getBlock() {
		return block;
	}
}
