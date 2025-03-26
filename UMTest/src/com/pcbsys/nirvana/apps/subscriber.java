/*
    Copyright 2012 Software AG, Darmstadt, Germany and/or Software AG USA
  Inc., Reston, United States of America, and/or their licensors.
  In the event that you should download or otherwise use this software
  you hereby acknowledge and agree to the terms at
  http://um.terracotta.org/company/terms.html#legalnotices
 */
package com.pcbsys.nirvana.apps;
import java.io.*;
import java.util.Enumeration;

import com.pcbsys.nirvana.client.nChannel;
import com.pcbsys.nirvana.client.nChannelAttributes;
import com.pcbsys.nirvana.client.nEventListener;

import java.util.Date;
/**
 * Subscribes to a nirvana channel
 */
public class subscriber  implements nEventListener {
	static long startEid;
	static String selector = null;
	private long lastEID = 0;
	private long startTime = 0;
	private long byteCount = 0;
	private int logLevel = 0;
	private int count = -1;
	private int totalMsgs = 0;
	private int reportCount = 10000;
	private nChannel myChannel;
	private static subscriber mySelf = null;
	/**
	 * This method demonstrates the Universal Messaging API calls necessary to subscribe to
	 * a channel. It is called after all command line arguments have been
	 * received and validated
	 * 
	 * @param realmDetails
	 *            a String[] containing the possible RNAME values
	 * @param achannelName
	 *            the channel name to create
	 * @param selector
	 *            the subscription selector filter
	 * @param startEid
	 *            the eid to start subscribing from
	 * @param loglvl
	 *            the specified log level
	 * @param repCount
	 *            the specified report count
	 */
	private void doit(String[] realmDetails, String achannelName,
			String selector, long startEid, int loglvl, int repCount) {
		logLevel = loglvl;
		reportCount = repCount;
		// Subscribes to the specified channel
		try {
			// Create a channel attributes object
			nChannelAttributes nca = new nChannelAttributes();
			nca.setName(achannelName);
			// Obtain the channel reference
			myChannel = mySession.findChannel(nca);
			// if the latest event has been implied (by specifying -1)
			if (startEid == -1) {
				// Get the last eid on the channel and reset the start eid with
				// that value
				startEid = myChannel.getLastEID();
			}
			// Add this object as a subscribe to the channel with the specified
			// message selector
			// and start eid
			myChannel.addSubscriber(this, selector, startEid);
			// Stay subscribed until the user presses any key
			System.out.println("Press any key to quit !");
			BufferedInputStream bis = new BufferedInputStream(System.in);
			try {
				bis.read();
			} catch (Exception read) {
			} // Ignore this
			System.out.println("Finished. Consumed total of " + totalMsgs);
			// Remove this subscriber
			myChannel.removeSubscriber(this);
		}
		// Handle errors
		catch (nChannelNotFoundException cnfe) {
			System.out.println("The channel specified could not be found.");
			System.out.println("Please ensure that the channel exists in the REALM you connect to.");
			cnfe.printStackTrace();
			System.exit(1);
		} catch (nSecurityException se) {
			System.out.println("Insufficient permissions for the requested operation.");
			System.out.println("Please check the ACL settings on the server.");
			se.printStackTrace();
			System.exit(1);
		} catch (nSessionNotConnectedException snce) {
			System.out.println("The session object used is not physically connected to the Universal Messaging Realm.");
			System.out.println("Please ensure the realm is running and check your RNAME value.");
			snce.printStackTrace();
			System.exit(1);
		} catch (nUnexpectedResponseException ure) {
			System.out.println("The Universal Messaging REALM has returned an unexpected response.");
			System.out.println("Please ensure the Universal Messaging REALM and client API used are compatible.");
			ure.printStackTrace();
			System.exit(1);
		} catch (nUnknownRemoteRealmException urre) {
			System.out.println("The channel specified resided in a remote realm which could not be found.");
			System.out.println("Please ensure the channel name specified is correct.");
			urre.printStackTrace();
			System.exit(1);
		} catch (nRequestTimedOutException rtoe) {
			System.out.println("The requested operation has timed out waiting for a response from the REALM.");
			System.out.println("If this is a very busy REALM ask your administrator to increase the client timeout values.");
			rtoe.printStackTrace();
			System.exit(1);
		} catch (nChannelAlreadySubscribedException chase) {
			System.out.println("You are already subscribed to this channel.");
			chase.printStackTrace();
			System.exit(1);
		} catch (nSelectorParserException spe) {
			System.out.println("An error occured while parsing the selector filter specified.");
			System.out.println("Please check the JMS documentation on how to write a valid selector.");
			spe.printStackTrace();
			System.exit(1);
		} catch (nBaseClientException nbce) {
			System.out.println("An error occured while creating the Channel Attributes object.");
			nbce.printStackTrace();
			System.exit(1);
		}
		// Close the session we opened
		try {
			nSessionFactory.close(mySession);
		} catch (Exception ex) {
		}
		// Close any other sessions within this JVM so that we can exit
		nSessionFactory.shutdown();
	}
	protected void processArgs(String[] args) {
		switch (args.length) {
		case 5:
			System.getProperties().put("SELECTOR", args[4]);
		case 4:
			System.getProperties().put("COUNT", args[3]);
		case 3:
			System.getProperties().put("DEBUG", args[2]);
		case 2:
			System.getProperties().put("START", args[1]);
		case 1:
			if (args[0].equals("-?")) {
				Usage();
				UsageEnv();
			}
			System.getProperties().put("CHANNAME", args[0]);
			break;
		}
	}
	public static void main(String[] args) {
		// Create an instance for this class
		mySelf = new subscriber();
		// Process command line arguments
		mySelf.processArgs(args);
		nSampleApp.processEnvironmentVariables();
		// Check the channel name specified
		String channelName = null;
		if (System.getProperty("CHANNAME") != null)
			channelName = System.getProperty("CHANNAME");
		else {
			Usage();
			System.exit(1);
		}
		startEid = -1; // Default value (points to last event in the channel +
						// 1)
		// Check to see if a start EID value has been specified
		if (System.getProperty("START") != null) {
			try {
				startEid = Integer.parseInt(System.getProperty("START"));
			} catch (Exception num) {
			} // Ignore and use the defaults
		}
		int loglvl = 1; // Default value
		// Check to see if a LOGLEVEL value has been specified.
		if (System.getProperty("DEBUG") != null) {
			try {
				loglvl = Integer.parseInt(System.getProperty("DEBUG"));
			} catch (Exception num) {
			} // Ignore and use the default
		}
		int reportCount = 10000; // Default value
		// Check to see if a value for report count has been specified. Every
		// time
		// N events get received where N = report count, a subscription rate
		// report will
		// be printed in System.out
		if (System.getProperty("COUNT") != null) {
			try {
				reportCount = Integer.parseInt(System.getProperty("COUNT"));
			} catch (Exception num) {
			} // Ignore and use the default
		}
		// Check for a selector message filter value
		selector = System.getProperty("SELECTOR");
		// Check the local realm details
		int idx = 0;
		String RNAME = null;
		if (System.getProperty("RNAME") != null)
			RNAME = System.getProperty("RNAME");
		else {
			Usage();
			System.exit(1);
		}
		// Process the local REALM RNAME details
		String[] rproperties = new String[4];
		rproperties = parseRealmProperties(RNAME);
		// Subscribe to the channel specified
		mySelf.doit(rproperties, channelName, selector, startEid, loglvl,
				reportCount);
	}
	/**
	 * A callback is received by the API to this method each time an event is
	 * received from the nirvana channel. Be carefull not to spend too much time
	 * processing the message inside this method, as until it exits the next
	 * message can not be pushed.
	 * 
	 * @param evt
	 *            An nConsumeEvent object containing the message received from
	 *            the channel
	 */
	public void go(nConsumeEvent evt) {
		// If this is the first message we receive
		if (count == -1) {
			// Get a timestamp to be used for message rate calculations
			startTime = System.currentTimeMillis();
			count = 0;
		}
		// Increment he counter
		count++;
		totalMsgs++;
		// Have we reached the point where we need to report the rates?
		if (count == reportCount) {
			// Reset the counter
			count = 0;
			// Get a timestampt to calculate the rates
			long end = System.currentTimeMillis();
			// Does the specified log level permits us to print on the screen?
			if (logLevel >= 1) {
				// Dump the rates on the screen
				if (end != startTime) {
					System.out.println("Received " + reportCount + " in "
							+ (end - startTime) + " Evt/Sec = "
							+ ((reportCount * 1000) / (end - startTime))
							+ " Bytes/sec="
							+ ((byteCount * 1000) / (end - startTime)));
					System.out.println("Bandwidth data : Bytes Tx ["
							+ mySession.getOutputByteCount() + "] Bytes Rx ["
							+ mySession.getInputByteCount() + "]");
				} else {
					System.out.println("Received " + reportCount
							+ " faster than the system millisecond counter");
				}
			}
			// Set the startTime for the next report equal to the end timestamp
			// for the previous one
			startTime = end;
			// Reset the byte counter
			byteCount = 0;
		}
		// If the last EID counter is not equal to the current event ID
		if (lastEID != evt.getEventID()) {
			// If yes, maybe we have missed an event, so print a message on the
			// screen.
			// This message could be printed for a number of other reasons.
			// One of them would be someone purging a range creating an 'eid
			// gap'.
			// As eids are never reused within a channel you could have a
			// situation
			// where this gets printed but nothing is missed.
			System.out.println("Expired event range " + (lastEID) + " - "
					+ (evt.getEventID() - 1));
			// Reset the last eid counter
			lastEID = evt.getEventID() + 1;
		} else {
			// Increment the last eid counter
			lastEID++;
		}
		// Get the data of the message
		byte[] buffer = evt.getEventData();
		if (buffer != null) {
			// Add its length to the byte counter
			byteCount += buffer.length;
		}
		// If the loglevel permits printing on the screen
		if (logLevel >= 2) {
			// Print the eid
			System.out.println("Event id : " + evt.getEventID());
			if (evt.isEndOfChannel()) {
				System.out.println("End of channel reached");
			}
			// If the loglevel permits printing on the screen
			if (logLevel >= 3) {
				// Print the message tag
				System.out.println("Event tag : " + evt.getEventTag());
				// Print the message data
				System.out.println("Event data : "
						+ new String(evt.getEventData()));
				if (evt.hasAttributes()) {
					displayEventAttributes(evt.getAttributes());
				}
				nEventProperties prop = evt.getProperties();
				if (prop != null) {
					displayEventProperties(prop);
				}
			}
		}
	}
	/**
	 * Prints the usage message for this class
	 */
	private static void Usage() {
		System.out.println("Usage ...\n");
		System.out.println("nsubchan <channel name> [start eid] [debug] [count] [selector] \n");
		System.out.println("<Required Arguments> \n");
		System.out.println("<channel name> - Channel name parameter for the channel to subscribe to");
		System.out.println("\n[Optional Arguments] \n");
		System.out.println("[start eid] - The Event ID to start subscribing from");
		System.out.println("[debug] - The level of output from each event, 0 - none, 1 - summary, 2 - EIDs, 3 - All");
		System.out.println("[count] - The number of events to wait before printing out summary information");
		System.out.println("[selector] - The event filter string to use");
		System.out.println("\n\nNote: -? provides help on environment variables \n");
	}
}