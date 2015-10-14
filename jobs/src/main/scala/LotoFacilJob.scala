

object LotoFacilJob extends App with loto.LotoLogger {
	
	val downloader = new LotoFacilDownloadZip
	val parser = new LotoFacilHtmlParser(downloader.download)
	val resultados = parser.parse
	

	line
	info(s"Resultados.size: ${resultados.size}")
	info(s"Resultados.head: ${resultados.head}")
	info(s"Resultados.last: ${resultados.last}")
	line

	LotoFacilRepo.updateJobExecution(resultados.size)
	
	val last = LotoFacilRepo.lastConcurso
	
	if (last == resultados.last.draw)
		info("Records are on date. Nothing else to do...")
	else {
		if (last == 0) info("Save'em all [init] !")
		else info(s"Save concursos > ${last}")

		resultados filter (_.draw > last) foreach (LotoFacilRepo.save _)
	}


	LotoFacilRepo.close

	line
	info(s"The End.")
	line


}
