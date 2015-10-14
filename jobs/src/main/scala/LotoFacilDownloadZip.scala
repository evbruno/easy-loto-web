
import java.io.{File, FileOutputStream}
import java.net.{CookieHandler, CookieManager, CookiePolicy, URL}
import java.util.zip.ZipFile

import com.typesafe.config.ConfigFactory
import org.apache.commons.io.IOUtils

import scala.language.postfixOps
import scala.sys.process._

class LotoFacilDownloadZip extends loto.LotoLogger {

	CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL))

	val config = ConfigFactory.load()

	val url = config.getString("lotofacil.url")
	val htmlFileName = config.getString("lotofacil.htmlFileName")

	def download: String = {
		val outputZip = File.createTempFile("lotofacil_", ".zip")
		val outputHtml = File.createTempFile("lotofacil_", ".html")

		info(s"Downloading from $url")

		new URL(url) #> outputZip !!

		info(s"Download file $outputZip //  ${outputZip.exists} // ${outputZip.length}")

		val zipFile = new ZipFile(outputZip)
		val in = zipFile.getInputStream(zipFile.getEntry(htmlFileName))

		val out = new FileOutputStream(outputHtml)
		IOUtils.copy(in, out)

		IOUtils.closeQuietly(in)
		IOUtils.closeQuietly(out)

		info(s"HTML output file $outputHtml // ${outputHtml.exists} // ${outputHtml.length}")

		outputHtml.getAbsolutePath
	}

}
