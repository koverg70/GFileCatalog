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

import java.security.MessageDigest

/**
 *
 *
 * @author Kövér Gábor, Scriptum Inc.
 * @version $Id$
 */
class Utils
{
	final static int DIGEST_BUF = 16384*8;

	static Writable pogoToXml( object )
	{
		new groovy.xml.StreamingMarkupBuilder().bind {
			"${object.getClass().name}" {
				object.getClass().declaredFields.grep { !it.synthetic }.name.each { n ->
					"$n"( object."$n" )
				}
			}
		}
	}

	static String generateMD5(File f)
	{
		MessageDigest digest = MessageDigest.getInstance("MD5")
		RandomAccessFile raf = new RandomAccessFile(f, "r")
		byte[] buffer = new byte[DIGEST_BUF]

		// beginning of file
		int len = (int)([DIGEST_BUF, raf.length()].min())
		raf.read buffer, 0, len
		digest.update buffer, 0, len
		if (raf.length() >= DIGEST_BUF*3)
		{
			// middle of file
			raf.seek((long)((raf.length() - DIGEST_BUF) / 2))
			raf.read buffer, 0, DIGEST_BUF;
			digest.update buffer, 0, DIGEST_BUF
		}
		if (raf.length() >= DIGEST_BUF*2)
		{
			// end of file
			raf.seek raf.length() - DIGEST_BUF
			raf.read buffer, 0, DIGEST_BUF
			digest.update buffer, 0, DIGEST_BUF
		}
		new BigInteger(1, digest.digest()).toString(16).padLeft(32, '0')
	}

	static checkDuplicates(Collection<FileDesc> descrs)
	{
		def df = new java.text.DecimalFormat("#,###,###,##0.00" )

		// duplicates
		println "Looking for duplicates..."
		def duplmap = descrs.groupBy{it.md5Hash}.findAll {it.value.size() > 1}
		def duplarr = []
		
		// copy the values of the duplmap to duplarr
		duplmap.each {
			duplarr << it.value
		}
		
		println "Duplicates: " + duplarr.size
		
		duplarr = duplarr.sort false, {it[0].length}
		
		def size = 0

		println "Duplicates: "
		duplarr.each {
		  println it[0].name + ": " + it.size() + " occurrences in ["
		  it.each {
			println "\t\t" + it.path + "/" + it.name + "; "
			size += it.length
		  }
		  // size -= it.value[0].length
		  println "]"
		}
		

		println df.format(size) + " bytes can be freed by removing duplicates"
	}

}
