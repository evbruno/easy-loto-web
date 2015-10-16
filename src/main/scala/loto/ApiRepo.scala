package loto

import akka.actor.ActorSystem
import akka.http.scaladsl.coding.Gzip
import akka.http.scaladsl.model.TransferEncodings.gzip
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.{MiscDirectives, RespondWithDirectives}
import com.mongodb.casbah.Imports._

import scala.concurrent.Future

trait ApiRepo extends BetProtocols with MiscDirectives with RespondWithDirectives {

	import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
	import spray.json._

	//import scala.concurrent.ExecutionContext.Implicits.global

	implicit val system: ActorSystem
	import ApiRepo._
	import system.dispatcher

	def betsF = Future { bets }
	def resultsF = Future { results }
	def hitsF = Future { hits }

	val apiRoute = logRequestResult("easy-loto-api") {
		pathPrefix("api") {
			(get & path("ping")) {
				complete {
					"pong"
				}
			} ~
			(get & path("lotofacil") & encodeResponseWith(Gzip)) {
				//complete { bets	}
//				onSuccess(resultsF) { r => complete(r.toJson) }
				onSuccess(resultsF) { r => complete(r.take(20).toJson) }
			} ~
			(get & path("lotofacil" / "bets")) {
				onSuccess(betsF) { b => complete(b.toJson) }
			} ~
			(get & path("lotofacil" / "hits")) {
				onSuccess(hitsF) { h => complete(h.toJson) }
			}
		}
	}
}

object ApiRepo {

	// eg:  mongodb://<dbuser>:<dbpassword>@mongolob.url:AAA/DATABASE
	val uri = scala.util.Properties.envOrElse("MONGOLAB_URI", "mongodb://localhost:27017/lotofacil")
	val mongoClientURI = MongoClientURI(uri)

	val mongoClient = MongoClient(mongoClientURI)
	val db = mongoClient(mongoClientURI.database.get)

	val resultados = db("resultados")

	def close() = mongoClient.close()

	def results : List[Result] = {
		val cursor = resultados.find().sort(MongoDBObject("concurso" -> -1))
		(for (doc <- cursor) yield
				Result(draw = doc.as[Int]("concurso"), numbers = doc.as[List[Int]]("aposta"))).toList
	}


//	lazy val results = (1 to 10).map(c => Result(c, c to 14+c)).toList

	lazy val bets: List[Bet] = (7 to 10).map(c => Bet(c to 14+c)).toList
	lazy val hits : List[BetHit] = bets.flatMap(b => results.map(r => BetHit(b, r)))

}

//object ApiRepoApp extends App {
//
//	val r = ApiRepo.results
//	ApiRepo.close
//
//	println(s"Last: ${r.head}")
//	println(s"First: ${r.last}")
//}
