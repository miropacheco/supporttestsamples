/*
Copyright 1999-2011 (c) My-Channels
Copyright (c) 2012-2014 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.

Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
*/

package com.pcbsys.nirvana.nJMSApps;

import com.pcbsys.foundation.utils.fEnvironment;

import java.util.*;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.NamingException;

/**
 * This class demonstrates how to construct a Publisher that can send bytes messages on a topic
 */
public class BytesMessagePublisher extends Publisher {
  // static public String sClassVersion = "$Name: nirvana_9_6 $ - $Revision: 1.20 $";

  public static void main(String[] args) {
	System.out.println("START: BytesMessagePublisher");
    //Check to see if args were specified
    if ((args.length == 1) && args[0].equals("-?")) {
      UsageEnv();
    }

    if (args.length < 2) {
      Usage();
      System.exit(1);
    }

    //Process Environment Variables
    processEnvironmentVariable("RNAME");
    processEnvironmentVariable("PRINCIPAL");
    processEnvironmentVariable("PASSWORD");
    processEnvironmentVariable("CONTEXT_FACTORY");
    processEnvironmentVariable("PROVIDER_URL");
    processEnvironmentVariable("LOGLEVEL");
    processEnvironmentVariable("HPROXY");
    processEnvironmentVariable("HAUTH");
    processEnvironmentVariable("CKEYSTORE");
    processEnvironmentVariable("CKEYSTOREPASSWD");
    processEnvironmentVariable("CAKEYSTORE");
    processEnvironmentVariable("CAKEYSTOREPASSWD");

    // Install any proxy server settings
    fEnvironment.setProxyEnvironments();

    // Install any ssl settings
    fEnvironment.setSSLEnvironments();

    //Create an instance for this class
    BytesMessagePublisher publisher = new BytesMessagePublisher();

    int count = 10;
    boolean transacted = false;

    if (args.length > 2) {
      count = Integer.parseInt(args[2]);
    }

    if (args.length > 3) {
      transacted = Boolean.parseBoolean(args[2]);
    }

    //Publish to the topic specified
    publisher.doIt(args[0], args[1], count, transacted);
  }

  /**
   * This method demonstrates the Nirvana JMS API calls necessary to publish to
   * a topic.
   * It is called after all command line arguments have been received and
   * validated
   *
   * @param factoryName The name of the factory to find
   * @param topicName   the name of the topic
   * @param count       number of message to publish
   * @param transacted  whether the session is transactional
   */
  private void doIt(String factoryName, String topicName, int count, boolean transacted) {
    try {
      System.out.println("******* This program is throttled to produce 100 events a second *******");
      System.out.println("******* remove the sleep in Publisher.doTXPublish or doPublish   *******");
      // get the initial context
      Context ctx = getInitialContext();
      TopicConnectionFactory tcf = (TopicConnectionFactory) ctx.lookup(factoryName);
      System.out.println("Pub: Context="+ctx.getClass().getName()+", ConnectionFactory="+tcf.getClass().getName()+" - transacted="+transacted);

      // Create a Topic Connection from the Topic Connection Factory
      TopicConnection tc = tcf.createTopicConnection();

      // set the exceptionlistener
      tc.setExceptionListener(this);

      // Start the connection
      tc.start();

      // Create a Topic Sesson from the Topic Connection
      s = tc.createTopicSession(transacted, 1);

      // create the topic, and bind if necessary
      Topic t = (Topic) getDestination(ctx, s, topicName);

      //Create a Topic Publisher from the Topic Session
      p = s.createProducer(t);
      System.out.println("Pub: Connection="+tc.getClass().getName()+", Session="+s.getClass().getName()+", Topic="+t.getClass().getName()+", Producer="+p.getClass().getName());
      p.setTimeToLive(20000);

      //Loop for count
      for (int x = 0; x < count; x++) {
        // create a bytes message
        BytesMessage bmsg = s.createBytesMessage();
        bmsg.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
        String str = "Message Number(" + x + ")";
        bmsg.writeBytes(str.getBytes());
        bmsg.setIntProperty("Message Number", x);
        if (transacted) {
          doTXPublish(bmsg);
        } else {
          doPublish(bmsg);
        }
        System.out.println(new Date(System.currentTimeMillis()) + " - Published " + str);
      }

      //Print a message to the console saying we are about to exit
      System.out.println("Closing topic session and topic connection. Published a total of " + publishCount + " messages");
      Thread.sleep(1000);

      //Close the Topic Connection
      tc.close();

      //Close the Topic Session
      s.close();

      //close the context
      ctx.close();
    } catch (NamingException ex) {
      System.out.println("Naming Exception : Please ensure you have created the connection factory " + factoryName);
      System.out.println("Naming Exception : This can be done using the Enterprise Manager JNDI panel or the jmsadmin command line application");
      System.exit(0);
    } catch (Exception ex) {
      ex.printStackTrace();
      System.exit(0);
    }
  }

  /**
   * Prints the usage message for this class
   */
  private static void Usage() {
    System.out.println("Usage ...\n\n");
    System.out.println("jmsbytespub <factoryname> <topicName> <count> <transacted>\n");

    System.out.println("<Required Arguments> \n");
    System.out.println("<factoryname> - JMS Factory (Must exist in target realm)");
    System.out.println("<topicName> - JMS Topic to publish on");
    System.out.println("<count> - Number of events to publish");
    System.out.println("<transacted> - Whether the session is transacted");
    System.out.println("\n\nNote: -? provides help on environment variables \n");
  }
}
// End of BytesMessagePublisher Class
