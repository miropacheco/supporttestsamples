$Copyright(c) 2006-2011 Progress Software Corporation. All Rights Reserved.$

Progress Apama QuickFix (FIX) IAF Adapter
==========================================

Version: 4.3.0-48


DESCRIPTION

	The FIX adapter uses the Apama Integration Adapter Framework (IAF) and
	the QuickFIX library to connect to FIX compliant systems.  The adapter
	is fully documented in the the file "FIX_Adapter.pdf" that ships with
	this release.  This readme contains any information that is currently
	missing from that document as well as legal notices and acknowledgements.


*** IMPORTANT NOTES ON UPGRADING AND CONFIGURING THE ADAPTER ***

	Users upgrading from a version of the FIX adapter prior to 4.0.0-27
	should note that the event and repeating group definitions used by the
	adapter changed significantly in version 4.0.0-27.  In addition, some
	old service monitors have been removed and new service monitors added.
	
	Configuration files from previous versions will NOT work correctly with
	4.0.0-27 or any later version of the adapter.  Users should ensure that
	they follow the recommended procedure for upgrading their configuration
	files after installing the adapter (users configuring a new FIX adapter
	installation should start from step 3 below):
	
	1. A number of service monitors have been made obsolete by changes to
	   the configuration files. If any of the following files are present
	   in the APAMA_HOME/adapters "monitors" directory they should be removed
	   to avoid confusion:
	     FIX_eSpeed_Events.mon
	     FIX_ICE_Events1.mon
	     FIX_ICE_Events2.mon
	     FIX_iLink_Events1.mon
	     FIX_iLink_Events2.mon
	     FIX_Lava_Events.mon
	     FIX_Lava_Support.mon
	
	2. Make backup copies of all configuration files used with previous
	   versions of the adapter.
	   
	3. Make copies of the supplied template configuration files needed for
	   your local environment:
	     FIX.xml.dist - General FIX client configuration
	     FIX-server.xml.dist - General FIX server configuration
	     FIX-CNX.xml.dist - Client configuration for Currenex and FXCMPro
	     FIX-eSpeed.xml.dist - Client configuration for eSpeed/BGC FX
	     FIX-ICE.xml.dist - Client configuration for ICE
	     FIX-iLink.xml.dist - Client configuration for CME iLink
	     FIX-Lava.xml.dist - Client configuration for Lava
	   The copied files should have a ".xml" extension.  These files will
	   not be overwritten on subsequent upgrades, but the ".dist" files
	   may be; therefore these should NOT be used for local configuration.
	   
	   Note that all of the standard FIX repeating group definitions,
	   codec configuration and FIX event type definitions are contained in
	   the various *-static.xml configuration files distributed with the
	   adapter.  These are included into the template configuration files
	   using standard XML "XInclude" syntax and this pattern should be
	   followed in all user-generated configuration files.  The contents
	   of the *-static.xml files should not be modified without explicit
	   instructions from Apama customer support.
	
	4. Edit the copied configuration files as appropriate for the local
	   environment:
	   - Entries of the form "@NAME@" identify configuration properties
	     that should be filled in by the user (although note that not all
	     of these values are required).  All configurable properties are
	     documented in this file, the service-specific README files and
	     the FIX_Adapter.pdf file in the adapters/doc directory.
	   - The correlator host, port and channel properties in the <apama>
	     section at the end of each configuration file must be set.
	
	5. Review the "SERVICE MONITOR INJECTION ORDER" section below in this
	   file, and if necessary the README files for specific services, to
	   ensure that you are injecting the correct monitors in the right
	   order and using appropriate session configuration parameters.
	   Service-specific README files are provided for these services:
	     BARX
	     BrokerBox
	     Currenex (also applies to FXCMPro)
	     eSpeed FX (also applies to BGC FX)
	     FXall
	     ICE
	     CME iLink
	     Lava
	     Nexa
	
	These steps should be followed after every upgrade to the adapter, as
	any upgrade may change the repeating group or event definitions.
	

SERVICE MONITOR INJECTION ORDER

	The following monitors need to be injected into the correlator (in the
	following order) to support the adapter.  All paths are relative to the
	Apama 4.3 installation directory:

	monitors/LoggingManager.mon
	monitors/StatusSupport.mon
	monitors/finance/TickManagerSupport.mon
	monitors/finance/OrderManagerSupport.mon
	monitors/ScenarioService.mon
	monitors/finance/ScenarioOrderService.mon
	monitors/finance/MultiLegOrderManagerSupport.mon
	adapters/monitors/IAFStatusManager.mon
	adapters/monitors/FIX_Events.mon
	adapters/monitors/FIX_SessionManager.mon
	adapters/monitors/FIX_DataManager.mon
	adapters/monitors/FIX_SubscriptionManager.mon
	adapters/monitors/FIX_OrderManager.mon
	adapters/monitors/FIX_OrderServer.mon
	adapters/monitors/FIX_StatusManager.mon
	adapters/monitors/MarketDataServerSupport.mon
	adapters/monitors/FIX_MarketDataServer.mon


	See the README files for specific FIX services for details of any
	additional service monitors required to use the FIX adapter with
	those services.

TRANSPORT PROPERTIES CONFIGURATIONS
	QuickFIX.FileStorePath
	 -This parameter controls logging of persisted FIX messages 
	  (used for replay and gap recovery )
	 -This property must be specified and does not point to invalid path. Takes 
	  relative as well as absolute paths. 
	 For example:
	 Windows(Absolute):- "D:\XYZ\FIX_Log => directory FIX_Log will be created
	                                        if not exists if path D:\XYZ exists
	 Windows(Relative):- "Fix_Log"       => directory FIX_Log will be created
                                              in current directory from where the
                                              iaf instance started if directory
                                              does not exists

	QuickFIX.FileLogPath
	 -This parameter controls logging of all incoming and outgoing FIX messages
	 -If this property is not mentioned the logging is disabled. it accepts 
	  relative as well as absolute paths.

	QuickFIX.SendBufferSize
	QuickFIX.ReceiveBufferSize
	 - These properties can be used to set the QuickFIX TCP socket send/receive
	   buffer sizes to the number of bytes specified.
	   It defaults to OS specific.
	For example:
	<property name="QuickFIX.SendBufferSize" value="1024" />
	<property name="QuickFIX.ReceiveBufferSize" value="1024" />

	"ConvertNativeStrings" (boolean, default false).
	 - If true, any STRING-type fields in FIX messages should be converted from the native character encoding
	 -> UTF-8 for downstream messages and from UTF-8 -> the native encoding for 
		upstream messages.  If false, all strings are assumed to be UTF-8.
   
  	"NativeEncoding" (string, default empty).
  	 - If non-empty, this is the name of the native encoding to use for conversions to/from UTF-8.  If not 
	   specified, the system encoding should be used - this parameter is to 
  	   override it per-transport.

	For example,
	 <property name="ConvertNativeStrings" value="true" />
	 <property name="NativeEncoding" value="ISO8859-1" />

NEW TRANSPORT PROPERTIES

	ValidateLogonRequests (boolean)
	 - Enable it to authenticate clients in server scenarios.
	   (valid only if QuickFIX.ConnectionType = acceptor).
	   Defaults to false.

	LogonValidationTimeout (in milliseconds)
	 - Configure a timeout for client connection validation
	   Considered only if ValidateLogonRequests is enabled.
	   Defaults to 2000.

	   
	StopAcceptorOnWatchdogTimeout
	 prevents Acceptor connection from stopping if connection is lost between correlator and IAF. Default is "true"
	 eg.
	 <property name="StopAcceptorOnWatchdogTimeout" value="false" />	 

SERVICE MONITOR CONFIGURATION PARAMETERS

    The following configuration parameters have been added since the previous
    release. for a full list of available parameters and details on their
    use, please see the FIX documentation.

    PARAMETER                     TYPE           APPLIES TO              DESCRIPTION
    ---------------------------------------------------------------------------------------------------------------------------------------------

    SubscriptionManager.          boolean       SubscriptionManager      Adds support for latency measurement on marketdata messages, stringified 
	LogLatency                                                           timestamp set dictionary is added to com.apama.marketdata events. (default = false)
	
	
    OrderManager.LogLatency        boolean        OrderManager           Enriches upstream order management messages with timestamp data. (default = false)

    ReturnZeroPricesOnError        boolean        SubscriptionManager    Send Zero Depths/Ticks in the event of Error occurs (default = true)

    SubscriptionManager.           boolean        SubscriptionManager    Ignores any trade message received after unsubscription
    IgnoreSnapshotOnUnsubscription 

    SubscriptionManager.           boolean        SubscriptionManager    Overwrites old quantity, if the new quantity is Zero 
    SupportZeroQuantities        

    SubscriptionManager.           boolean        SubscriptionManager    To enable in place update of Market data processing. 
    MDupdateInPlace                                                      Position based updates are applied in place

    OrderManager.                  boolean        OrderManager           Specifies that the status of an order
    IgnoreStatusOnOrderCancelReject                                      will be unchanged by a OrderCancelReject message.
                                                                         (the succeeding Amends/Cancels after the Rejected message
                                                                         will have their origClorderId set to clorderId of the last successful order)

    SubscriptionManager.                                             
    SuppressZeroQuantities        boolean        SubscriptionManager    Suppress zero quantity entries from
                                                                        com.apama.marketdata.Depth event.
    
    SubscriptionManager.
    TagsToForwardFromExchange     boolean        SubscriptionManager    Forwards the values for tags received from exchange
                                                                        in Depth and Tick Updates. Also prevents overwriting
                                                                        their values with the subscription parameters.
                                                                        ex: "SubscriptionManager.TagsToForwardFromExchange":"64 167"

    OrderManager.
    CancelRejectUsesOrigId        boolean        OrderManager           To handle Order Cancel Reject with swapped
                                                                        ClOrdID(11) & OrigClOrdID(41) fields.
                                                                        Defaults to false.



NEW SERVICE MONITOR CONFIGURATION PARAMETERS

    PARAMETER                      TYPE        APPLIES TO        DESCRIPTION
    ---------------------------------------------------------------------------------------------------------------------------------------------
    FIX.TagsToSupress              sequence    ALL                Provide a list of tags that needs to be removed from the
                                                                  payload of events emitted from the correlator in the upstream 
                                                                  direction. Defaults to "35 52 34 8 "
    
    OrderManager.                            
    amendUsesNonRejectedClOrderID  boolean    OrderManager        Order amend/cancel uses last ClOrderID if it is not rejected.
                                                                  Defaults to false.
                                                            
    SubscriptionManager.                                          Sequence of MDUpdateAction values separated by space.
    MDUpdateActionOrder            sequence                       SubscriptionManager Specified UpdateAction order will be used to update
                                                                  marketdata from incremental refresh.
                                                                  Defaults to "1 2 0" (i.e. Change, Delete, New)
    SubscriptionManager.
    PublishNonPositivePrices       boolean   SubscriptionManager  To publish zero and -ve along with +ve prices in Depth.
                                                                  Defaults to false.
                                                            
    SubscriptionManager.           string    SubscriptionManager  Specifies that which params or tags should 
    SubscriptionKeyTags                                           be considered for creating the subscription
                                                                  key for Depth/Tick subscriptions. The value 
                                                                  to be given as string with elements seperated by space
                                                                  Ex. "22 207"  or  ""
                                                                  See more about this tag in the below Note section.

    DataManager.SymbolFormationTags    string    DataManager      Tells which tags to be used for symbol normalization.
                                                                  more about this is found under section
                                                                  Symbol Normalization.
 
    DataManager.MappedSecDefListenerKey        Support mons       This parameters may be used in conjunction with the 
                                                                  DataManager.SymbolFormationTags.More about it is found 
                                                                  under section Symbol normalization. 
                                                                  Ex. "" . Empty strings also could be a possible case 
                                                                  where in just symbol will be used for creating subscription 
                                                                  key. The unique ness is based on just the symbol.

    OrderManager.                string        OrderManager       To populate the Currency(Tag 15) from supplied symbol
    UseCurrencyFromSymbol                                         Allowed values are "BASE", "TERM". For more description
                                                                  Ex. "" . Empty strings also could be a possible case where in just symbol 
                                                                  will be used for creating subscription key. The uniqueness is based on just the symbol.
                                                                
    SubscriptionManager.         integer       SubscriptionManager   allows users to put alternative security
    UseAltSecurityId                                            id in the "symbol" field of a market data 
                                                                request and have this mapped to the right 
                                                                tag on the FIX message (and vice-verssa for 
                                                                downstream messages). The value of this 
                                                                should be the number of the FIX tag that the 
                                                                alternative security id will go in.
                                                                More about this in the notes section below.
    OrderManager.                integer       OrderManager     allows users to put alternative security
    UseAltSecurityId                                            id in the "symbol" field of a Order management 
                                                                event and have this mapped to the right 
                                                                tag on the FIX message (and vice-verssa for 
                                                                downstream messages). The value of this 
                                                                should be the number of the FIX tag that the 
                                                                alternative security id will go in.
                                                                More about this in the notes section below.

    SubscriptionManager.        boolean        SubscriptionManager If the exchange publishes same MDEntryID (Tag 278) for bidSide MDEntry
    SupportNonUniqueMDEntryIDs                                  and offerSide MDEntry, then this parameter should be used to maintain different 
                                                                dictionaries for the MDEntries. Defaults to false

    SubscriptionManager.
    UseCurrencyFromSymbol       string         SubscriptionManager    To populate the Currency(Tag 15) from supplied symbol
                                                                      Allowed values are "BASE", "TERM". For more description
                                                                      Ex. "" . Empty strings also could be a possible case where in just symbol 
                                                                      will be used for creating subscription key. The uniqueness is based on just the symbol.
    
    OrderServer.
    UseFirewallServiceID       boolean        OrderServer       To add Firewall service Id in the extraparams while routing NewOrder, AmendOrder and 
                                                                 CancelOrder, when it is set to 'true'.

Note:
	WARNING: The default behaviour of the config parameter "SubscriptionManager.ReturnZeroPricesOnError" will be changing to "false"
	in the future releases.
	WARNING: The default behaviour of the config parameter "OrderManager.CancelRejectUsesOrigId" will be changing to "false"
	in the future releases.
	
	SubscriptionManager.SubscriptionKeyTags (Notes)
	-----------------------------------------------
	The tags mentioned in the above config param are searched in the same order in
	the extraParams field of the com.apama.marketdata.SubscribeDepth(),
	com.apama.marketdata.SubscribeTick() to create the subscription key.
	The reference counting and uniqueness of the subscription is based on
	presence of these tags and values corresponding to these tags.
		
	Ex.
	SubscriptionManager.SubscriptionKeyTags = "22 207"
	com.apama.marketdata.SubscribeDepth("FIX", "Connection", "SYMBOL", {"22":"8", "207":"ABCD", "123":"EFG", "234":"IJK"})
	
	The key prepared is : "SYMBOL:8:ABCD"
	
	For
	com.apama.marketdata.SubscribeDepth("FIX", "Connection", "SYMBOL", {"22":"8", "123":"EFG", "234":"IJK"})
	The key prepared is : "SYMBOL:8:"
	
	The empty value of config param tells that the key is prepared just from the
	symbol, so the uniqueness is based on just the symbol.
	
	WARNING!!! The config parameter "SubscriptionManager.SubscriptionKeyTags" is not supported in session reconfiguration, only the initial provided
	values are used in succeeding re-configurations. Also this parameter has a dependency on config param "SubscriptionManager.DistinctDepthTickRequest".
	If both parameters are used in session configuration event then for succeeding re-configurations the value of param "SubscriptionManager.DistinctDepthTickRequest"
	should not be altered.


	Configuration parameter "OrderManager.UseCurrencyFromSymbol" for currency pair symbols
	--------------------------------------------------------------------------------------
	Example: Symbol => USD/GBP or USDGBP (Symbol expected XXX/YYY or XXXYYYY)
			 BASE currency => USD
			 TERM currency => GBP

	Scenario 1:
	----------
	In default behaviour (not setting the configuration parameter) user need to give the tag 15 in his order requests.
	without which order will not be accepted.
			 
	Scenario 2:
	----------
	If user sets the configuration parameter then user need not give the tag 15 in his/her order requests.
	the tag is populated from the symbol as per the configuration.
	
	Scenaio 3:		  	
	----------
	If user sets the configuration and gives tag 15 in his order requests.
	the supplied tag value will be used.

	SubscriptionManager.UseAltSecurityId, OrderManager.UseAltSecurityId notes:
	--------------------------------------------------------------------------
	You can set these parameters to an alternate FIX tag:
	example: "SubscriptionManager.UseAltSecurityId":"10455" 
			"SubscriptionManager.UseAltSecurityId":"10455"
	then a market data request can be sent as:
		com.apama.marketdata.SubscribeDepth("FIX", "Transport", "AlternateName",{"55":"SYMBOL"})
	a NewOrder can be sent as:
		com.apama.oms.NewOrder("1040","AlternateName",5.031,"SELL","LIMIT",10,"FIX","","","Transport","","",{"55":"SYMBOL"})
	After these are processed by the respective monitors the SYMBOL and AlternateName get exchanged and 
	AlternateName will be assigned to 10455 tag.
	

	
		

CONNECTING TO SSL-ENABLED FIX SERVERS

	The FIX Adapter does not support SSL natively.  The usual solution
	is to use a tool like stunnel (www.stunnel.org) to wrap the FIX server
	connection inside SSL.  The setup will be something like the following:

	[FIX adapter] <-- stunnel --> [host:port] <-- ssh --> [remote server]

	For example, if the remote server is running on remotehost:443, then in the
	stunnel configuration file on localhost the following configuration is
	needed:

	[fix-adapter]
	accept = 12345 (or any unused port number)
	connect = remotehost:443

	Then in the FIX Adapter configuration file, for the transport property
	QuickFIX.SocketConnectHost the value should be "localhost", and for property
	QuickFIX.SocketConnectPort the value should be "12345" (or any unused port
	number set in the stunnel configuration file).

	Stunnel can be started simply by calling the stunnel command with the
	stunnel configuration file with the above settings.

	Stunnel should be started before the FIX Adapter is started.


LATENCY MEASUREMENT

	The transport supports the Apama 4.x adapter latency measurement
	framework.  See "The IAF Configuration File" section of the
	"Developing Adapters" book in the Apama 4.x documentation set for full details on
	configuring the transport to record high-resolution timestamps on
	downstream events.
	To enable timestamp logging and timestamp propagation to marketdata
	events the following SessionConfiguration parameters should be passed:
		- "SubscriptionManager.LogLatency":"true"
		- "OrderManager.LogLatency":"true"

CODEC

	This adapter uses the Filter Codec.  For details of its usage, see the
	associated readme which is distributed with this release.


FLOATING POINT QUANTITIES

	In order to submit a new order or amend an order with floating point qantity
	values, the following session configuration parameter must be specified:

		"AddQuantityToExtraParams":"true"
		
	NewOrder and AmendOrder Examples :
			
		com.apama.oms.NewOrder("order1", "gbp/usd", 1.9857, "BUY", "LIMIT", 6000000, "TRANSPORT", "", "", "Connection", "", "", {"Quantity":"6000000.45"})
			
		com.apama.oms.AmendOrder("order1","TRANSPORT","gbp/usd",1.9857,"BUY","LIMIT",3000000,{"Quantity":"3000000.34"}) 
			
	For trade report service, additional quantity information can also be found
	in the "com.apama.oms.OrderUpdate" extraParams:

	The keys are as follows:

		"Quantity"
		"QuantityExecuted"
		"QuantityRemaining"
		"LastQuantityExecuted"


SYMBOL NORMALIZATION
====================		
This feature enables application user to use unique symbol(service monitor created symbol) for multi-leg securities.
In the absence of this feature user has to give several tag values for each user request (Subscription,NewOrder, AmendOrder, CancelOrder) and
has to write several "if" conditions to uniquely identify the response event.

This new feature reduces the task by providing a MAPPED_SYMBOL so that all the interaction of user application with service monitor can then
be through this mapped_symbol. The service monitors will take care of to and fro translation of security defition and mapped symbol.

Session Configuration parameters for symbol normalization
=========================================================
DataManager.SymbolFormationTags":"167 207 [146 308 309 310 313]"
The configuration to create the mapped symbol. This list tells which tag values to be used for constructing the mapped symbol
the mapped symbol will have the tags values in the same sequence as mentioned in the configuration including the position of 
occurance of the repeating group.
	
DataManager.MappedSecDefListenerKey":"MARKET_DATA"
This parameters tells which MarketId/Transport name to be used to fetching the security information of the mapped symbol.
By default each configured session in the service monitors uses it's own marketId/Connection/TransportName.	

	In dual session MARKET_DATA, TRADE_DATA. MARKET_DATA populate the intermediate cache of the mapped symbol to security definition
	and TRADE_DATA session can use cache created by setting the above config parameter to corresponding MARKET_DATA transport name.
	
	In the absence of this parameter setting, user has to send two InstrumentDataRequest() one with MARKET_DATA transport and other with
	TRADE_DATA transport to populate the mapped_symbol cache for each session.
		
	
	Note:- User has to send the InstrumentDataRequest() with MARKET_DATA transport/connection to populate the cache of mapped symbol
		to security definition before using the cache in other sessions.


Symbol formation (MAPPED_SYMBOL)
================================
The symbol is formed with the normalized values of tags mentioned in config parameter DataManager.SymbolFormationTags"

The tags need to be given space delimited. the repeating group tags can be mentioned in "[]" square brackets.
the first tag in the brackets needs to be the repeating group tag itself. The tags in the "[]" are also space delimited.


From the configuration DataManager.SymbolFormationTags":"167 207 [146 309 310 313]"

The base tags : 167 207
repeating group tags : 309 310 313

example:(tag string) 	
		167:207:309:310:313-309:310:313-309:310:313 
		 
		If you replace the tag numbers with their corresponding values picked from the security definition 
		that will become the mapped symbol.
		
		MLEG:ICE_IPE:217922:FUT:201103-217951:FUT:201108-217923:FUT:201110 (This security has 3 legs)

The mapped symbol contains all the legs of the multi-leg security with configured tag values populated.

It is the user's choice to create the symbol, if user finds minimal tags to distinguish each security then the mapped
symbol looks simpler and cleaner.
		
example:
		DataManager.SymbolFormationTags":"207 [146 10456]"
		207:10456-10456	
		The mapped symbol will be CME:CLF1-CLG1 (This security has only two legs)
		
		
Multi-Leg symbol formation:
	All the above examples are the multi-leg scenarios.	

Single leg symbol formation:
	In single leg securities, though the session config mention the repeating group tags, the security definition recieved from exchange
	will not have the repeating group so that part is omited while constructing the mapped symbol.
	
	For example:
	
		167:207:309:310:313-309:310:313-309:310:313 
		 
		If you replace the tag numbers with their corresponding values that will become the mapped symbol.
		MLEG:ICE_IPE:217922 (repeating group absent in the security defintion for single legged security)
		
	User should choose minimum base tags such that symbol formed will be unique and simple.


The created mapped symbol will appear in the dictionary field "extraParams" of InstrumentDataResponse() as a key value pair.
 "MAPPED_SYMBOL" : "MLEG:ICE_IPE:217922"

	
Requesting the security definition for multi-leg securities
===========================================================

Following are the request/response events for getting the security definition 

package com.apama.fix

event InstrumentDataRequest {
	string marketId;
	string key;
	string symbol;
	dictionary <string,string> extraParams;
	sequence<string> requiredParams;
}

event InstrumentDataResponse {
	string marketId;
	string key;
	boolean success;
	string errorMessage;
	dictionary <string,string> parameters;
}


Requesting required information
-------------------------------

In the extraParams field of the InstrumentDataRequest user can give the filtering information so that the matching security definitions are recieved as 
InstrumentDataResponse() for each matched security.

example:
	User want to fetch the security definition for the multi-leg instrument whose leg information is as follows
	309 : 217922 (tag 309 is UnderlyingSecurityID)
	309 : 217951

	com.apama.fix.InstrumentDataRequest("MARKET_DATA","40","",{"167":"MLEG","207":"ICE_IPE","146":"[{309:\"217922\"},{309:\"217951\"}]"},["55","48","207"])

	This request will result in unique/multiple InstrumentDataResponse() events. If there are more than one security available with this legs information then
	user gets multiple responses. In case of multiple responses the boolean flag "success" of response event is set to false.
	

Recieving multi-leg information as part of InstrumentDataResponse
-----------------------------------------------------------------
	
The 'parameters' field of the InstrumentDataResponse() is populated based on the tag list mentioned in 'requiredParams' field of the InstrumentDataRequest.
The 'requiredParams' sequence field now accepts repeating group as a string.


example:
	to recieve the repeating field tags 310 311 313 the request has to be
	com.apama.fix.InstrumentDataRequest("MARKET_DATA","40","",{"167":"MLEG","207":"ICE_IPE"},["55","48","207","[146 310 311 313]"])
	
	The response looks like this
	com.apama.fix.InstrumentDataResponse("MARKET_DATA","40",true,"",{"146":"[{\"310\":\"FUT\",\"311\":\"IPE e-Brent\",\"313\":\"201103\"},{\"310\":\"FUT\",\"311\":\"IPE e-Brent\",\"313\":\"201108\"}]","207":"ICE_IPE","48":"219924","55":"IPE e-Brent","MAPPED_SYMBOL":"MLEG:ICE_IPE:_:ICE_IPE:217922:FUT:201103-ICE_IPE:217951:FUT:201108"})


Note:  Always mention the tags in requiredParams field of the InstrumentDataRequest(), The tags mentioned in this parameter are the out going tags in each request.
Infact the mapped symbol cache is formed against these tags as Mapped symbol => tags mentioned in the requiredParams field. 
=====	
	
	
Using Mapped symbol
====================

  Subscription management 
  =======================
    com.apama.marketdata.SubscribeDepth("FIX_SERVICE", "MARKET_DATA", "CME:CFL1-CFG1",{})
    com.apama.marketdata.UnsubscribeDepth("FIX_SERVICE", "MARKET_DATA", "CME:CFL1-CFG1",{})
	com.apama.marketdata.Depth("CME:CFL1-CFG1",[],[],[],[],[],{})

	or
	
	com.apama.marketdata.SubscribeTick("FIX_SERVICE", "MARKET_DATA", "CME:CFL1-CFG1",{})
    com.apama.marketdata.UnsubscribeTick("FIX_SERVICE", "MARKET_DATA", "CME:CFL1-CFG1",{})
	com.apama.marketdata.Tick("CME:CFL1-CFG1", 123.23, 100000,{})

	
  Order management 
  =================	
	
    com.apama.oms.NewOrder("19","CME:CFL1-CFG1",35350,"BUY","LIMIT",1,"FIX_SERVICE","","","TT_TRADING","","",{})
	
	com.apama.oms.AmendOrder("19","FIX_SERVICE","CME:CFL1-CFG1",120450,"BUY","LIMIT",2000,{})

	com.apama.oms.CancelOrder("19", "FIX_SERVICE", {})

	com.apama.oms.OrderUpdate("19", "CME:CFL1-CFG1",35350,"BUY","LIMIT",1000,true,true,true,false,false,false,"",1000,1000,1000,35350,35350,"Fill:Partial Fill",{})
	

Client Authentication
=====================

    When FIX adapter configured as a Server(OrderServer/MarketDataServer),
    it allows the Apama application to authenticate client sessions.

    Enable ValidateLogonRequests property at transport and create an application
    to validate client sessions by listening com.apama.fix.Logon request(s)
    and reply com.apama.fix.LogonAuthenticationStatus events.

    When ValidateLogonRequests property is enabled on Server Transport,
    for each logon request from client it waits upto LogonValidationTimeout
    milliseconds to receive LogonAuthenticationStatus event from Apama application.
    On timeout it rejects logon requests with reason "Logon validation timeout".


MARKETDATA SERVER CONFIGURATION
===============================

    The FIX_MarketDataServer translates incoming fix market data request messages into their
    apama equivalents like com.apama.mds.SubscribeDepth and com.apama.mds.SubscribeTick events.

    MarketData Server can be configured and instantiate by sending the following event:
    com.apama.fix.MDServerConfiguration {
        string connection;
        dictionary<string,string> configuration;
    }

    Following session configuration parameters can be used to configure the FIX MarketData Server

    PARAMETER                    TYPE                                     DESCRIPTION
    ------------------------------------------------------------------------------------------------------------------------
    FIX.TagsToSupress          sequence                Provide a list of tags that needs to be removed from the
                                                       payload of events emitted from the correlator in the upstream 
                                                       direction. Defaults to "35 52 34 8 "
    
    RequestKeyParams           sequence                Specifies that which params or tags should be considered for
                                                       creating the subscription key for Depth/Tick subscriptions.
                                                       The value to be given as string with elements seperated by space
                                                           "*" --> Includes all extraParams of MarketDataRequest (Default).
                                                           ""  --> Includes None of the extraParams of MarketDataRequest.
                                                           "336 625"  --> Includes tags 336, 625 and their values only.
                                                           "* 336 625"  --> Includes all except tags 336 and 625.
    FixChannel                 String                  The channel to emit upstream events to.
                                                       Defaults to "FIX".
    TargetService              String                  The ID of the target service.
                                                       Defaults to "MarketSimulator".

    Ex: com.apama.fix.MDServerConfiguration("MarketDataSimulator", {"RequestKeyParams":"* 336 625"})

    Supported Features
    ==================
        1. Client authentication
        2. Tick Subscription and Unsubscription 
        3. Depth Subscription and Unsubscription
            a) Single Snapshot
            b) Snapshot + Updates (Full & Incremental)
        4. MarketData Request Reject by Application
        5. Configurable Subscription key consctuction parameters

    For Authentication related configuration please refer the "Client Authentication" section.

    MarketData Server uses com.apama.fix.* events to interact with Server Transport and
    com.apama.mds.* events from MarketDataServerSupport.mon to interact with Application.

    On receiving a com.apama.fix.MarketDataRequest event from its transport, the server
    creates com.apama.mds.SubscribeDepth/com.apama.mds.SubscribeTick with unique MDReqID
    for specified TargetService.

    For each com.apama.mds.MDServerDepth, com.apama.mds.MDServerTick received from Application
    for known subscriptions it will generate com.apama.fix.MarketDataSnapshotFullRefresh and
    com.apama.fix.MarketDataIncrementalRefresh based on the request type.


TRADEMARKS NOTICE

	All Apama brands and product names are trademarks or registered trademarks of
	Progress Software Corporation or one of its affiliates or subsidiaries. Other
	brands and product names may be trademarks or registered trademarks of their
	respective owners. No license is granted to reproduce or use any such brand,
	product name or trademark except as expressly in the applicable license
	agreement.


PROPRIETARY INFORMATION

	Progress Software Corporation considers information included in this software
	to be Confidential Information.  Your access and use of this Confidential
	Information is subject to the terms and conditions of the software license
	agreement, which has been executed and with which you agree to comply.
	Accordingly, you may not disclose or disseminate any such Confidential
	Information except as expressly permitted under such license agreement.



