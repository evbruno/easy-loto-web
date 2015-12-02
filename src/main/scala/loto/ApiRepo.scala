package loto

import com.mongodb.casbah.Imports._

object ApiRepo {

	private val LOTO = "lotofacil"
	private val MEGA = "mega-sena"

	// eg:  mongodb://<dbuser>:<dbpassword>@mongolob.url:AAA/DATABASE
	val uri = scala.util.Properties.envOrElse("MONGOLAB_URI", "mongodb://localhost:27017/lotofacil")
	private val mongoClientURI = MongoClientURI(uri)

	private val mongoClient = MongoClient(mongoClientURI)
	private val db = mongoClient(mongoClientURI.database.get)

	private val dbResults = db("results")
	private val dbBets = db("bets")
	private val dbJobUpdates = db("jobs_updates")

	def close() = mongoClient.close()

	def results: List[Result] = {
		val cursor = dbResults.find().sort(MongoDBObject("draw" -> -1))
		(for (doc <- cursor) yield
		Result(draw = doc.as[Int]("draw"), numbers = doc.as[List[Int]]("numbers"))).toList
	}

	def betsFor(draw: Int) : List[Bet] = {
		val query = MongoDBObject(
						"from" -> MongoDBObject("$lte" -> draw),
						"to" -> MongoDBObject("$gte" -> draw),
						"source" -> MongoDBObject("$eq" -> LOTO)
					)

		(for {
			doc <- dbBets.find(query)
			numbers <- doc.as[List[BasicDBList]]("numbers")
		} yield Bet(numbers.map(_.toString.toDouble.toInt).toList)).toList
	}

	def lastConcurso: Int = {
		val query = MongoDBObject() // All documents
		val fields = MongoDBObject("draw" -> 1)
		val orderBy = MongoDBObject("draw" -> -1)

		val x = dbResults.findOne(query, fields, orderBy)

		x match {
			case Some(k) => k.getAs[Int]("draw").get
			case _ => 0
		}
	}

	def updateJobExecution(totalParsed: Int) {
		dbJobUpdates.insert(
			MongoDBObject(
				"when" -> new java.util.Date,
				"totalParsed" -> totalParsed,
				"currentSize" -> dbJobUpdates.size
			)
		)
	}

	def save(obj: Result) {
		val doc = MongoDBObject("draw" -> obj.draw, "numbers" -> obj.numbers, "source" -> LOTO)
		dbResults.insert(doc)
	}

	lazy val hits: List[BetHit] = List() // bets.flatMap(b => results.map(r => BetHit(b, r)))

}
