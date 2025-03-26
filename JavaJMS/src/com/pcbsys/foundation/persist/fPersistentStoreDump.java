/*
 *
 *   Copyright (c) 1999 - 2011 my-Channels Ltd
 *   Copyright (c) 2012 - 2017 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 *   Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 *
 */

package com.pcbsys.foundation.persist;

import com.pcbsys.foundation.base.fEventDictionary;
import com.pcbsys.foundation.fConstants;
import com.pcbsys.foundation.io.fBaseEvent;
import com.pcbsys.foundation.io.fBaseEventFactory;
import com.pcbsys.foundation.persist.event.fPersistentEventHolder;


public class fPersistentStoreDump {

  //------------------------------------------------------------------------------------------------------------------
  public static void main(String[] args) {
    if (args.length < 2) {
      System.out.println("Usage :-");
      System.out.println("  fPersistentStoreDump fileName factoryClassName [compress=Y]");
      System.exit(0);
    }

    try {
      String fileName = args[0];
      fBaseEventFactory factory = null;
      if (args.length >= 2) {
        factory = (fBaseEventFactory) Class.forName(args[1]).newInstance();
      }
      boolean recover = false;
      if (args.length == 3) {
        if (args[2].equalsIgnoreCase("Recover")) {
          recover = true;
        }
      }

      fPersistantEventManager pem = new fPersistantEventManager(fileName, factory, recover);
      System.out.println("Size    = " + pem.myStorageSpace);
      System.out.println("Free    = " + pem.myFreeSpace);
      System.out.println("%Free   = " + pem.getPercentFree());
      System.out.println("Minimum = " + pem.getFirstStoredKey());
      System.out.println("Maximum = " + pem.getLastStoredKey());
      System.out.println("Events  = " + pem.getNoEvents());
      if (recover) {
        System.out.println("Restored= " + pem.restoredEvents);
      }
      if (args.length == 3) {
        if (recover) {
          System.out.println("Committing changes to store");
        } else {
          System.out.println("Removing free space");
        }

        pem.performMaintenance();
        dumpStore(pem);
      }
    } catch (Exception ex) {
      fConstants.logger.error(ex);
    }
  }

  //------------------------------------------------------------------------------------------------------------------
  protected static void dumpStore(fPersistantEventManager pem) {
    for (long x = pem.getFirstStoredKey(); x < pem.getLastStoredKey(); x++) {
      fPersistentEventHolder s = (fPersistentEventHolder) pem.inmem.get(x);
      if (s != null) {
        java.util.Date dt = new java.util.Date(s.getTime());
        System.out.println("Event ID = " + x);
        System.out.println("  Pos:" + s.getPos());
        System.out.println("  Size:" + s.getSize());
        System.out.println("  Date:" + dt.toString());
        fBaseEvent evt = pem.getEvent(x);
        fEventDictionary dic = evt.getDictionary();
        if (dic != null) {
          System.out.println("  **Dictionary**");
          java.util.Enumeration keys = dic.getKeysAsStrings();
          while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            Object val = dic.get(key);
            System.out.println("   " + key + " = " + val.toString());
          }
        }
        System.out.println("   Evt:" + evt.toString());
      }
    }
  }
}
