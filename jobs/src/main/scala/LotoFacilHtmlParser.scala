
import loto.Result
import net.ruippeixotog.scalascraper.browser.Browser
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import net.ruippeixotog.scalascraper.dsl.DSL._

import scala.collection.mutable.ArrayBuffer

class LotoFacilHtmlParser(fileName: String) extends loto.LotoLogger {

	line
	info(s"Arquivo para parser: $fileName")

	lazy val browser = new Browser
	lazy val doc = browser.parseFile(fileName)
	
	type Linha = Seq[String]
	type BufferDeLinhas = scala.collection.mutable.ArrayBuffer[Linha]

	private def parseLinhas: BufferDeLinhas = {

		val trs = doc >> extractor("tr", asIs)
		info(s"Total de 'trs': ${trs.size}")

		var linhas = new BufferDeLinhas()
		
		trs.foreach { tr =>
			val tds: Linha = tr >> "td"
			if (tds.size > 30)
				linhas += tds
		}
		
		linhas
	}
	
	def parse: ArrayBuffer[Result] = {
		parseLinhas.map { linhas =>
			val aposta = linhas.slice(2, 17).map(_.toInt).sorted.toIndexedSeq
			val concurso = linhas(0).toInt
			Result(concurso, aposta)
		}
	}

}
	
	
