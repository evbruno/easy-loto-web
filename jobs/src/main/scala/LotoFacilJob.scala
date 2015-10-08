
object LotoFacilJob extends App with loto.LotoLogger {
	
	val downloader = new LotoFacilDownloadZip
	val parser = new LotoFacilHtmlParser(downloader.download)
	val resultados = parser.parse

	line
	info(s"Resultados.size: ${resultados.size}")
	info(s"Resultados.head: ${resultados.head}")
	info(s"Resultados.last: ${resultados.last}")
	line

}
