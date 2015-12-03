package loto

import com.mongodb.casbah.Imports
import com.mongodb.casbah.Imports._

import scala.collection.mutable

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
			Result(draw = doc.as[Int]("draw"),
				numbers = doc.as[List[Int]]("numbers"),
				drawDate = doc.as[String]("draw-date"),
				prizes = extractPrizes(doc))
		).toList
	}

	private def extractPrizes(doc: DBObject) : List[Prize] = {
		val res = doc.as[BasicDBList]("prizes").map { p =>
			(p.asInstanceOf[BasicDBList].get(0).asInstanceOf[Int],
				p.asInstanceOf[BasicDBList].get(1).asInstanceOf[String])
		}
		res.toList
	}

	def betsFor(draw: Int) : List[Bet] = {
//		val query = MongoDBObject(
//						"from" -> MongoDBObject("$lte" -> draw),
//						"to" -> MongoDBObject("$gte" -> draw),
//						"source" -> MongoDBObject("$eq" -> LOTO)
//					)

		val query = ("from" $lte draw) ++
					("to" $gte draw) ++
					("source" $eq LOTO)

		(for {
			doc <- dbBets.find(query)
			numbers <- doc.as[List[BasicDBList]]("numbers")
		} yield Bet(extractNumbers(numbers))).toList
	}

	private def extractNumbers(doc: BasicDBList) : Numbers = {
		doc.map(_.asInstanceOf[Int]).toList
	}

	def lastDraw: Int = {
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
		val doc = MongoDBObject("draw" -> obj.draw,
			"numbers" -> obj.numbers,
			"source" -> LOTO,
			"draw-date" -> obj.drawDate,
			"prizes" -> obj.prizes
		)

		dbResults.insert(doc)
	}

}

//object ApiRepoApp extends App {
//
////	val r = ApiRepo.results
////
////	println(s"Last: ${r.head}")
////	println(s"First: ${r.last}")
//	val x = ApiRepo.betsFor(1300)
//
////	val x = ApiRepo.results
//	println(s"x=$x")
//
//	ApiRepo.close
//}