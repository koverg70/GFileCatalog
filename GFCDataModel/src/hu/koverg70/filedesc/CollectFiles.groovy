/*
 * Copyright 2013 by Scriptum, Inc.,
 * Mályva utca 34, H-6771 Szeged, Hungary
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Scriptum, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Scriptum.
 */

package hu.koverg70.filedesc

/**
 *
 *
 * @author Kövér Gábor, Scriptum Inc.
 * @version $Id$
 */
public class CollectFiles
{
  /**
   * 0 - the name of the folder to process
   * 1 - the output file name and path
   * @param args
   */
  static void main(String[] args)
  {
    def descrs = [];
    int count = 0;
    println "Processing folder " + args[0]
    FileDesc.processFolder new File(args[0]), {
      descrs << it
      // println it.path + "/" + it.name
      if (++count % 100 == 0) {
        println "Processed: " + count
      }
    }

	// files
    println "Writing output to " + args[1]
    def out = new File(args[1])
    out.delete()
    out << "<?xml version=\"1.0\" encoding=\"ISO-8859-2\" ?>"
    out << "<FileList>"
    descrs.each {
      out << Utils.pogoToXml(it)
    }
    out << "</FileList>"
	
  }
}
