import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import loto.ApiRoute

import scala.concurrent.ExecutionContextExecutor

object AkkaHttpMicroservice extends App with ApiRoute {

	implicit val system = ActorSystem()
	implicit val executor: ExecutionContextExecutor = system.dispatcher
	implicit val materializer = ActorMaterializer()

	val config = ConfigFactory.load()
	val logger = Logging(system, getClass)

	val allRoutes = apiRoute ~ staticFilesRoute

	Http().bindAndHandle(allRoutes, config.getString("http.interface"), config.getInt("http.port"))

}
