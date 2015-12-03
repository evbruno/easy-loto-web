import loto.ApiRepo

object LotoFacilJob extends App with loto.LotoLogger {
	
	val downloader = new LotoFacilDownloadZip
	val parser = new LotoFacilHtmlParser(downloader.download)
	val resultados = parser.parse
	

	line
	info(s"Resultados.size: ${resultados.size}")
	info(s"Resultados.head: ${resultados.head}")
	info(s"Resultados.last: ${resultados.last}")
	line

	ApiRepo.updateJobExecution(resultados.size)
	
	val last = ApiRepo.lastDraw
	
	if (last == resultados.last.draw)
		info("Records are on date. Nothing else to do...")
	else {
		if (last == 0) info("Save'em all [init] !")
		else info(s"Save concursos > ${last}")

		resultados filter (_.draw > last) foreach (ApiRepo.save _)
	}


	ApiRepo.close

	line
	info(s"The End.")
	line


}
