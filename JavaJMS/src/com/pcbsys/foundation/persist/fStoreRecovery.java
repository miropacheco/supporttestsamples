/*
 *
 *   Copyright (c) 1999 - 2011 my-Channels Ltd
 *   Copyright (c) 2012 - 2017 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 *   Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 *
 */

package com.pcbsys.foundation.persist;

import com.pcbsys.foundation.fConstants;
import com.pcbsys.foundation.io.fBaseEventFactory;
import com.pcbsys.foundation.logger.fLogLevel;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class fStoreRecovery {


  private static final byte[] PersistentMarkers = {(byte) 0x45, (byte) 0x46, (byte) 0x47, (byte) 0x48};
  private static final byte[] MixedMarkers0 = {(byte) 0xf0, (byte) 0x0d, (byte) 0xf0, (byte) 0x0d};
  private static final byte[] MixedMarkers1 = {(byte) 0xba, (byte) 0xad, (byte) 0xf0, (byte) 0x0d};
  private static final byte[] MixedMarkers2 = {(byte) 0x0f, (byte) 0xd0, (byte) 0x0f, (byte) 0xd0};

  public fStoreRecovery(String[] args) {
    if (args.length < 2) {
      System.out.println("Usage :-");
      System.out.println("  fPersistantEventManager.main fileName factoryClassName [compress=Y]");
      System.exit(0);
    }

    try {
      fConstants.logger.setLogLevel(fLogLevel.valueFrom(9));
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
      process(fileName, factory, recover, args.length == 3);
    } catch (Exception ex) {
      fConstants.logger.error(ex);
    }

  }

  //------------------------------------------------------------------------------------------------------------------

  protected void process(String fileName, fBaseEventFactory factory, boolean recover, boolean compress)
      throws IOException {
    File file = new File(fileName);
    if (file.exists() && file.isDirectory()) {
      File[] files = file.listFiles();
      for (int x = 0; x < files.length; x++) {
        process(files[x].getAbsolutePath(), factory, recover, compress);
      }
    } else {
      processFile(fileName, factory, recover, compress);
    }
  }

  protected void processFile(String fileName, fBaseEventFactory factory, boolean recover, boolean compress)
      throws IOException {
    System.err.println("\n\n\nTesting " + fileName);
    if (fileName.endsWith(".mem")) {
      fileName = fileName.substring(0, fileName.length() - 4);
    } else {
      System.err.println("Not a native Nirvana store");
      return;
    }
    System.err.println("Detecting store type....");
    File file = new File(fileName + ".mem");
    if (!file.exists()) {
      System.err.println("Unable to locate file");
    } else if (file.length() == 0) {
      System.err.println("Unknown store type, length is 0");
    } else if (file.length() == 8) {
      System.err.println("Store is reliable");
      fReliableEventManager rem = new fReliableEventManager(fileName, true);
      System.err.println("Last EID : " + rem.getLastKey());
      rem.close();
    } else {
      System.err.println("Detecting if Mixed or Persistent store");
      boolean isMixed;
      boolean isPersistent;
      FileInputStream fis = null;
      try {
        fis = new FileInputStream(file);
        isMixed = false;
        isPersistent = false;

        byte[] buffer = new byte[80000];
        long len = file.length();
        long pos = 0;

        while (pos < len && !isMixed && !isPersistent) {
          int read = fis.read(buffer);
          System.err.print("Position : " + pos + " of " + len + "\r");
          System.err.flush();
          pos += read;
          for (int x = 0; x < read - 4; x++) {

            isMixed = true;
            for (int y = 0; y < MixedMarkers0.length; y++) {
              if (buffer[x + y] != MixedMarkers0[y]) {
                isMixed = false;
              }
            }
            if (!isMixed) {
              isMixed = true;
              for (int y = 0; y < MixedMarkers1.length; y++) {
                if (buffer[x + y] != MixedMarkers1[y]) {
                  isMixed = false;
                }
              }
            }
            if (!isMixed) {
              isMixed = true;
              for (int y = 0; y < MixedMarkers2.length; y++) {
                if (buffer[x + y] != MixedMarkers2[y]) {
                  isMixed = false;
                }
              }
            }

            isPersistent = true;
            for (int y = 0; y < PersistentMarkers.length; y++) {
              if (buffer[x + y] != PersistentMarkers[y]) {
                isPersistent = false;
              }
            }
            if (isPersistent || isMixed) {
              break;
            }
          }
        }
      } finally {
        if (fis != null) {
          fis.close();
        }
      }

      // Scan for markers

      if (!isMixed) {
        System.err.println("Defaulting to persistent store type");
        isPersistent = true;
      }
      if (isMixed || true) {
        System.err.println("Detected Mixed store...");
        fMixedEventManager pem = new fMixedEventManager(fileName, factory);
        System.err.println("Size    = " + pem.myStorageSpace);
        System.err.println("Free    = " + pem.myFreeSpace);
        System.err.println("%Free   = " + pem.getPercentFree());
        System.err.println("Minimum = " + pem.getFirstStoredKey());
        System.err.println("Maximum = " + pem.getLastStoredKey());
        System.err.println("Events  = " + pem.getNoEvents());
        if (pem.isCorrupted()) {
          System.err.println("****         WARNING         ****");
          System.err.println("**                             **");
          System.err.println("** Underlying file is corrupt  **");
          System.err.println("**                             **");
          System.err.println("****         WARNING         ****");
        }

      for(int x=0;x<pem.inmem.size();x++){
    	  System.out.println("Removing free space");

      }

        if (compress) {
          System.out.println("Removing free space");
          pem.performMaintenance();
        }

      } else if (isPersistent) {
        System.err.println("Detected Persistent store...");
        fPersistantEventManager pem = new fPersistantEventManager(fileName, factory, recover);
        System.err.println("Size    = " + pem.myStorageSpace);
        System.err.println("Free    = " + pem.myFreeSpace);
        System.err.println("%Free   = " + pem.getPercentFree());
        System.err.println("Minimum = " + pem.getFirstStoredKey());
        System.err.println("Maximum = " + pem.getLastStoredKey());
        System.err.println("Events  = " + pem.getNoEvents());
        if (pem.isCorrupted()) {
          System.err.println("****         WARNING         ****");
          System.err.println("**                             **");
          System.err.println("** Underlying file is corrupt  **");
          System.err.println("**                             **");
          System.err.println("****         WARNING         ****");
        }

        if (recover) {
          System.err.println("Restored= " + pem.restoredEvents);
        }
//      dumpStore(pem);
        if (compress) {
          pem.performMaintenance();
//        dumpStore(pem);
        }
        pem.close();
      } else {
        System.err.println("Unable to automatically determine type of store");
      }
    }
  }

  public static void main(String[] args) {
    new fStoreRecovery(args);
  }

}
