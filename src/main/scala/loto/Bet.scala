package loto

import spray.json._

trait BetProtocols extends DefaultJsonProtocol {

	implicit val betFormat = jsonFormat2(Bet.apply)
	implicit val resultFormat: RootJsonFormat[Result] = jsonFormat4(Result.apply)

}

case class Result(draw: Int, drawDate: String, numbers: Numbers, prizes: List[Prize]) {
	assert(draw > 0, s"Invalid draw: ${draw}")
	assert(numbers.size == 15, s"Invalid number combination: ${numbers}")
}

case class Bet(numbers: Numbers, owner: Option[String] = None) {
	assert(numbers.size == 15, s"Invalid number combination: ${numbers}")
}
