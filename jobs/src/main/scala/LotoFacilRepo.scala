import com.mongodb.casbah.Imports._
import loto.Resultado

object LotoFacilRepo {

	val uri = scala.util.Properties.envOrElse("MONGOLAB_URI", "localhost")
//	val mongoClient = MongoClient("localhost", 27017)
	val mongoClient = MongoClient(uri)
	val db = mongoClient("lotofacil")

	val atualizacoes = db("atualizacoes")
	val resultados = db("resultados")

	def save(obj: Resultado) {
		val doc = MongoDBObject("concurso" -> obj.concurso, "aposta" -> obj.aposta)
		resultados.insert(doc)
	}
	
	def updateJobExecution(totalParsed: Int) {
		atualizacoes.insert(
			MongoDBObject(
				"when" -> new java.util.Date,
				"totalParsed" -> totalParsed,
				"currentSize" -> atualizacoes.size
			)
		)
	}

	def lastConcurso : Int = {
		val query = MongoDBObject() // All documents
		val fields = MongoDBObject("concurso" -> 1)
		val orderBy = MongoDBObject("concurso" -> -1)

		val x = resultados.findOne(query, fields, orderBy)

		x match {
			case Some(k) => k.getAs[Int]("concurso").get
			case _ => 0
		}
	}

	def close = mongoClient.close()

}
