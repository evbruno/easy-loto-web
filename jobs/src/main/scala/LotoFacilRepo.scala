import com.mongodb.casbah.Imports._
import loto._

object LotoFacilRepo extends LotoLogger {

	// eg:  mongodb://<dbuser>:<dbpassword>@mongolob.url:AAA/DATABASE
	val uri = scala.util.Properties.envOrElse("MONGOLAB_URI", "mongodb://localhost:27017/lotofacil")
	val mongoClientURI = MongoClientURI(uri)

	//val mongoClient = MongoClient("localhost", 27017)
	//val db = mongoClient("lotofacil")

	info(s"MongoDB URI: ${uri}")
	info(s"MongoDB Client URI: ${mongoClientURI}")
	info(s"MongoDB Client URI.database: ${mongoClientURI.database}")
	line

	val mongoClient = MongoClient(mongoClientURI)
	val db = mongoClient(mongoClientURI.database.get)

	val atualizacoes = db("atualizacoes")
	val resultados = db("resultados")

	def save(obj: Result) {
		val doc = MongoDBObject("concurso" -> obj.draw, "aposta" -> obj.numbers)
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

	def lastConcurso: Int = {
		val query = MongoDBObject() // All documents
		val fields = MongoDBObject("concurso" -> 1)
		val orderBy = MongoDBObject("concurso" -> -1)

		val x = resultados.findOne(query, fields, orderBy)

		x match {
			case Some(k) => k.getAs[Int]("concurso").get
			case _ => 0
		}
	}

	def close() = mongoClient.close()

}
