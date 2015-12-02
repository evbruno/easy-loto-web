package loto

import akka.actor.ActorSystem
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.{Credentials, MiscDirectives, RespondWithDirectives}
import com.mongodb.casbah.Imports._

import scala.concurrent.Future


trait ApiRepo extends BetProtocols with MiscDirectives with RespondWithDirectives with LotoLogger {

	import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
	import spray.json._

	//import scala.concurrent.ExecutionContext.Implicits.global

	implicit val system: ActorSystem

	import ApiRepo._
	import system.dispatcher

	def betsF(d: Int) = Future {
		println(s"getting bets for $d")
		betsFor(d)
	}

	def resultsF = Future {
		results
	}

	def hitsF = Future {
		hits
	}

	val apiRoute = logRequestResult("easy-loto-api") {
		(pathPrefix("api") & respondWithHeader(`Access-Control-Allow-Origin`.`*`)) {  // encodeResponseWith(Gzip)

			(get & path("ping")) {
				complete {
					"pong"
				}
			} ~
				(get & path("lotofacil") & encodeResponse) {
				//complete { bets	}
//				onSuccess(resultsF) { r => complete(r.toJson) }
					onSuccess(resultsF) { r => complete(r.take(5).toJson)	}
			} ~
				(get & path("lotofacil" / "hits")) {
					onSuccess(hitsF) { h => complete(h.toJson) }
			} ~
				(get & path("lotofacil" / IntNumber / "bets")) { drawNumber =>
					onSuccess(betsF(drawNumber)) { h => complete(h.toJson) }
			}

		}
	}

	def myUserPassAuthenticator2(credentials: Credentials): Future[Option[String]] =
		credentials match {
			case p@Credentials.Provided(id) => {
				Future {
					if (p.verify("jerry") && id == "tom") Some(id)
					else None
				}
			}
			case _ => Future { None }
		}

	def myUserPassAuthenticator(credentials: Credentials): Option[String] =
		credentials match {
			case p@Credentials.Provided(id) if p.verify("p4ssw0rd") => Some(id)
			case _ => None
		}


	val staticFilesRoute = {
		path("main") {
			getFromResource("www/main.html")
		} ~ path("main2") {
			getFromResource("www/main_old.html")
		} ~
		pathPrefix("css") {
			getFromResourceDirectory("www/css")
		} ~
		pathPrefix("font") {
			getFromResourceDirectory("www/font")
		} ~
		pathPrefix("js") {
			getFromResourceDirectory("www/js")
		} ~
		// 
		pathPrefix("lib") {
			getFromResourceDirectory("www/lib")
		} ~
		pathPrefix("lotofacil") {
			getFromResourceDirectory("www/lotofacil")
		} ~
		path("mainr") {
			getFromResource("www_require/main.html")
		} ~
		path("secure") {
			authenticateBasic("EasyLoto domains", myUserPassAuthenticator) { username =>
				complete(s"Done $username")
			}
		} ~
		path("secure2") {
			authenticateBasicAsync("EasyLoto domains", myUserPassAuthenticator2) { username =>
				complete(s"Well Done $username")
			}
		}

		// % curl http://localhost:9000/secure --user tomcat:p4ssw0rd
		// % curl http://localhost:9000/secure2 --user tom:jerry
	}
}

object ApiRepo {

	// eg:  mongodb://<dbuser>:<dbpassword>@mongolob.url:AAA/DATABASE
	val uri = scala.util.Properties.envOrElse("MONGOLAB_URI", "mongodb://localhost:27017/lotofacil")
	val mongoClientURI = MongoClientURI(uri)

	val mongoClient = MongoClient(mongoClientURI)
	val db = mongoClient(mongoClientURI.database.get)

	val resultados = db("resultados")
	val lotofacilBets = db("lotofacil_bets")

	def close() = mongoClient.close()

	def results: List[Result] = {
		val cursor = resultados.find().sort(MongoDBObject("concurso" -> -1))
		(for (doc <- cursor) yield
		Result(draw = doc.as[Int]("concurso"), numbers = doc.as[List[Int]]("aposta"))).toList
	}

	def betsFor(draw: Int) : List[Bet] = {
		val query = MongoDBObject(
						"from" -> MongoDBObject("$lte" -> draw),
						"to" -> MongoDBObject("$gte" -> draw)
					)

		(for {
					doc <- lotofacilBets.find(query)
					numbers <- doc.as[List[BasicDBList]]("numbers")
				} yield Bet(numbers.map(_.asInstanceOf[Double].toInt).toList)).toList
	}


	lazy val hits: List[BetHit] = List() // bets.flatMap(b => results.map(r => BetHit(b, r)))

}

//object ApiRepoApp extends App {
//
////	val r = ApiRepo.results
////
////	println(s"Last: ${r.head}")
////	println(s"First: ${r.last}")
//
//	val x = ApiRepo.betsFor2(1300)
//
//	println(s"x=$x")
//
//	ApiRepo.close
//}
