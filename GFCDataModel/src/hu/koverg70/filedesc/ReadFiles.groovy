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

import java.text.SimpleDateFormat

/**
 *
 *
 * @author Kövér Gábor, Scriptum Inc.
 * @version $Id$
 */
class ReadFiles
{
	static void main(String[] args)
	{
		def df = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", new Locale("EN", "US"))
		println "Reading " + args[0]
		def xml = new XmlSlurper().parse(args[0]);

		println "Processing " + args[0]
		println "Size " + xml.FileDesc.size()
		def descrs = [];
		xml.FileDesc.each {
			descrs << new FileDesc(
				path: it.path,
				name: it.name,
				modified: df.parse(it.modified.toString()),
				length: Long.parseLong(it.length.toString()),
				md5Hash: it.md5Hash.toString()
			);
		}
		//println descrs

		Utils.checkDuplicates descrs
	}
}
