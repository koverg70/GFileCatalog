package hu.koverg70
import groovy.io.FileType;
import groovy.transform.CompileStatic;
import groovy.transform.ToString;

import java.security.MessageDigest;

@ToString(includePackage=false)
class FileDesc
{
	final static int DIGEST_BUF = 16384*8;

	String path;
	String name;
	Date modified;
	long length;
	String md5Hash;

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

	static FileDesc read(File f)
	{
		new FileDesc(
				name: f.name,
				path: f.parentFile.absolutePath,
				modified: new Date(f.lastModified()),
				md5Hash: generateMD5(f),
				length: f.length()
			);
	}

	static processFolder(File folder, Closure closure)
	{
		folder.eachFileRecurse(FileType.FILES)
		{
			def d = FileDesc.read(it)
			closure.call(d)
		}
	}

	static void main(String[] args) {
		def descrs = [];
		def folderName = "N:/Filmek";
		int count = 0;
		println "Processing folder " + folderName
		FileDesc.processFolder new File(folderName), {
			descrs << it
			// println it.path + "/" + it.name
			if (++count % 100 == 0) {
				println "Processed: " + count
			}
		}

		// duplicates
		println "Looking for duplicates..."
		def dupl = descrs.groupBy{it.md5Hash}.findAll {it.value.size() > 1}

		println "Duplicates: "
		dupl.each {
			print it.value[0].name + ": " + it.value.size() + " occurrences in ["
			it.value.each {
				print it.path + "; "
			}
			println "]"
		}
	}
}