package loto

import akka.actor.ActorSystem
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.{Credentials, MiscDirectives, RespondWithDirectives}


import scala.concurrent.Future


trait ApiRoute extends BetProtocols with MiscDirectives with RespondWithDirectives with LotoLogger {

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

	val apiRoute = logRequestResult("easy-loto-api") {
		(pathPrefix("api") & respondWithHeader(`Access-Control-Allow-Origin`.`*`)) {  // encodeResponseWith(Gzip)

			(get & path("ping")) {
				complete { "pong" }
			} ~
				(get & path("lotofacil") & encodeResponse) {
					onSuccess(resultsF) { r => complete(r.take(100).toJson)	}
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
		path("") {
			getFromResource("www/main.html")
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
		pathPrefix("img") {
			getFromResourceDirectory("www/img")
		} ~
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
