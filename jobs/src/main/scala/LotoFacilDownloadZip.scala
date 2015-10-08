
import java.io.{File, FileOutputStream}
import java.net.{CookieHandler, CookieManager, CookiePolicy, URL}
import java.util.zip.ZipFile

import org.apache.commons.io.IOUtils

import scala.language.postfixOps
import scala.sys.process._

class LotoFacilDownloadZip extends loto.LotoLogger {

	CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL))

	val url = ("http://www1.caixa.gov.br/loterias/_arquivos/loterias/D_lotfac.zip", "D_LOTFAC.HTM")

	def download: String = {
		val outputZip = File.createTempFile("lotofacil_", ".zip")
		val outputHtml = File.createTempFile("lotofacil_", ".html")

		new URL(url._1) #> outputZip !!

		info("Arquivo de download " + outputZip + " // " + outputZip.exists + " // " + outputZip.length)

		val zipFile = new ZipFile(outputZip)
		val in = zipFile.getInputStream(zipFile.getEntry(url._2))

		val out = new FileOutputStream(outputHtml)
		IOUtils.copy(in, out)

		IOUtils.closeQuietly(in)
		IOUtils.closeQuietly(out)

		info("Arquivo de html " + outputHtml + " // " + outputHtml.exists + " // " + outputHtml.length)

		outputHtml.getAbsolutePath
	}

}
