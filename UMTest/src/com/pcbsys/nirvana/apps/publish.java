/*
    Copyright 2012 Software AG, Darmstadt, Germany and/or Software AG USA
  Inc., Reston, United States of America, and/or their licensors.
  In the event that you should download or otherwise use this software
  you hereby acknowledge and agree to the terms at
  http://um.terracotta.org/company/terms.html#legalnotices
 */
package com.pcbsys.nirvana.apps;
import com.pcbsys.nirvana.client.*;
/**
 * Publishes reliably to a nirvana channel
 */
public class publish extends nSampleApp {
	private boolean isOk = true;
	private nBaseClientException asyncException;
	private static publish mySelf = null;
	/**
	 * This method demonstrates the Universal Messaging API calls necessary to publish to a
	 * channel. It is called after all command line arguments have been received
	 * and validated
	 * 
	 * @param realmDetails
	 *            a String[] containing the possible RNAME values
	 * @param achannelName
	 *            the channel name to publish to
	 * @param count
	 *            the number of messages to publish
	 * @param size
	 *            the size (bytes) of each message to be published
	 */
	private void doit(String[] realmDetails, String achannelName, int count,
			int size) {
		mySelf.constructSession(realmDetails);
		// Publishes to the specified channel
		try {
			// Create a channel attributes object
			nChannelAttributes nca = new nChannelAttributes();
			nca.setName(achannelName);
			// Obtain a reference to the channel
			nChannel myChannel = mySession.findChannel(nca);
			// Create a byte array filled with characters equal to
			// the message size specified. This could be a result
			// of String.getBytes() call in a real world scenario.
			byte[] buffer = new byte[size];
			for (int x = 0; x < size; x++) {
				buffer[x] = (byte) ((x % 90) + 32);
			}
			// Construct a sample nEventProperties object and add 2 sample
			// properties
			// Instantiate the message to be published with the specified
			// nEventPropeties and byte[]
			nConsumeEvent evt1 = new nConsumeEvent("Tag", buffer);
			nEventAttributes eAttr = evt1.getAttributes();
			eAttr.setMessageId("My Message Id".getBytes());
			eAttr.setUserId("My User id".getBytes());
			// Inform the user that publishing is about to start
			System.out.println("Starting publish of " + count
					+ " events with a size of " + size + " bytes each");
			// Obtain the channel's last event ID prior to us publishing
			// anything
			long startEid = myChannel.getLastEID();
			// Get a timestamp to be used to calculate the message publishing
			// rates
			long start = System.currentTimeMillis();
			// Loop as many times as the number of messages we want to publish
			for (int x = 0; x < count; x++) {
				try {
					// Publish the event
					myChannel.publish(evt1);
				} catch (nSessionNotConnectedException ex) {
					while (!mySession.isConnected()) {
						System.out.println("Disconnected from Universal Messaging, Sleeping for 1 second...");
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}
					}
					x--; // We need to repeat the publish for the event publish
							// that caused the exception,
					// so we reduce the counter
				} catch (nBaseClientException ex) {
					System.out.println("Publish.java : Exception : "
							+ ex.getMessage());
					throw ex;
				}
				// Check if an asynchronous exception has been received
				if (!isOk) {
					// If it has, then throw it
					throw asyncException;
				}
			}
			// Calculate the actual number of events published by obtaining
			// the channel's last eid after our publishing and subtracting
			// the channel's last eid before our publishing.
			// This also ensures that all client queues have been flushed.
			long end = System.currentTimeMillis();
			System.out.println("Get Last EID");
			long events = myChannel.getLastEID() - startEid;
			System.out.println("Got Last EID");
			// Check if an asynchronous exception has been received
			if (!isOk) {
				// If it has, then throw it
				throw asyncException;
			}
			// Get a timestamp to calculate the publishing rates
			// Calculate the events / sec rate
			long eventPerSec = (((events) * 1000) / ((end + 1) - start));
			// Calculate the bytes / sec rate
			long bytesPerSec = eventPerSec * size;
			// Inform the user of the resulting rates
			System.out.println("Publish Completed : ");
			System.out.println("[Events Published = " + events
					+ "]  [Events/Second = " + eventPerSec
					+ "]  [Bytes/Second = " + bytesPerSec + "]");
			System.out.println("Bandwidth data : Bytes Tx ["
					+ mySession.getOutputByteCount() + "] Bytes Rx ["
					+ mySession.getInputByteCount() + "]");
		}
		// Handle errors
		catch (nChannelNotFoundException cnfe) {
			System.out.println("The channel specified could not be found.");
			System.out.println("Please ensure that the channel exists in the Universal Messaging Realm you connect to.");
			cnfe.printStackTrace();
			System.exit(1);
		} catch (nSecurityException se) {
			System.out.println("Insufficient permissions for the requested operation.");
			System.out.println("Please check the ACL settings on the server.");
			se.printStackTrace();
			System.exit(1);
		} catch (nSessionNotConnectedException snce) {
			System.out.println("The session object used is not physically connected to the Universal Messaging Realm.");
			System.out.println("Please ensure the Universal Messaging Realm Server is up and check your RNAME value.");
			snce.printStackTrace();
			System.exit(1);
		} catch (nUnexpectedResponseException ure) {
			System.out.println("The Universal Messaging Realm Server has returned an unexpected response.");
			System.out.println("Please ensure the Universal Messaging Realm Server and client API used are compatible.");
			ure.printStackTrace();
			System.exit(1);
		} catch (nUnknownRemoteRealmException urre) {
			System.out.println("The channel specified resided in a remote Universal Messaging Realm which could not be found.");
			System.out.println("Please ensure the channel name specified is correct.");
			urre.printStackTrace();
			System.exit(1);
		} catch (nRequestTimedOutException rtoe) {
			System.out.println("The requested operation has timed out waiting for a response from the Realm,.");
			System.out.println("If this is a very busy Realm ask your administrator to increase the client timeout values.");
			rtoe.printStackTrace();
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
		case 3:
			System.getProperties().put("SIZE", args[2]);
		case 2:
			System.getProperties().put("COUNT", args[1]);
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
		mySelf = new publish();
		// Process command line arguments
		mySelf.processArgs(args);
		// Process Environment Variables
		nSampleApp.processEnvironmentVariables();
		// Check the channel name specified
		String channelName = null;
		if (System.getProperty("CHANNAME") != null)
			channelName = System.getProperty("CHANNAME");
		else {
			Usage();
			System.exit(1);
		}
		int count = 10; // default value
		// Check if the number of messages to be published has been specified
		if (System.getProperty("COUNT") != null) {
			try {
				count = Integer.parseInt(System.getProperty("COUNT"));
			} catch (Exception num) {
			} // Ignore and use the defaults
		}
		int size = 100; // default value
		// Check if the size (bytes) of each message has been specified
		if (System.getProperty("SIZE") != null) {
			try {
				size = Integer.parseInt(System.getProperty("SIZE"));
			} catch (Exception num) {
			} // Ignore and use the default
		}
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
		// Publish to the channel specified
		mySelf.doit(rproperties, channelName, count, size);
	}
	/**
	 * Prints the usage message for this class
	 */
	private static void Usage() {
		System.out.println("Usage ...\n");
		System.out.println("npubchan <channel name> [count] [size] \n");
		System.out.println("<Required Arguments> \n");
		System.out.println("<channel name> - Channel name parameter for the channel to publish to");
		System.out.println("\n[Optional Arguments] \n");
		System.out.println("[count] -The number of events to publish (default: 10)");
		System.out.println("[size] - The size (bytes) of the event to publish (default: 100)");
		System.out.println("\n\nNote: -? provides help on environment variables \n");
	}
} // End of publish Class