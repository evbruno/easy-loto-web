package loto

import akka.http.scaladsl.server.directives.{RespondWithDirectives, MiscDirectives}
import loto._

trait ApiRoutes extends BetProtocols with MiscDirectives with RespondWithDirectives {

	lazy val api = {
		path("api") {
			
		}
	}
	
}
