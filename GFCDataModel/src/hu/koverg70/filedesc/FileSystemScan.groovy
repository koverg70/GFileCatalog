package hu.koverg70.filedesc

class FileSystemScan
{
	def static folder = new File("N:\\Filmek");
	def static folders = 0;
	def static films = 0;
	
	def static filmFolderPattern = /(?i).*(19|20)[0-9][0-9]\.(720p\.)?(HUN\.|ENG\.)?(dvdrip\.|bdrip\.|x264\.)xvid(-hdtv)?.*/
	
	public static void main(String[] args)
	{
		processFolder(folder)
	}
	
	public static void processFolder(File folder)
	{
		//println "Mappa feldolgozása: " + folder
		folder.eachFile {
			if (it.isDirectory())
			{
				if (it.name ==~ filmFolderPattern)
				{
					println "Film: " + it
				}
				else
				{
					processFolder(it)
				}
			}
			else
			{
				if (it.size() > 600 * 1024 * 1024)
				{
					println "Film: " + it
				}
				else
				{
				/*	println "Egyéb: " + it */
				}
			}
		}
	}
}

