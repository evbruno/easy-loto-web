package loto

import spray.json._

trait BetProtocols extends DefaultJsonProtocol {

	implicit val betFormat = jsonFormat2(Bet.apply)
	implicit val resultFormat: RootJsonFormat[Result] = jsonFormat2(Result.apply)

	//	implicit val betHitFormat = jsonFormat3(BetHit.apply)
	implicit object BetsProtocol extends JsonFormat[BetHit] {

		override def read(json: JsValue): BetHit = ???

		override def write(obj: BetHit): JsValue = JsObject(
			("bet" -> obj.bet.toJson),
			("draw" -> obj.result.draw.toJson),
			("hits" -> obj.hits.toJson)
		)

	}

}

// TODO drawDate: Date, prizes: List[Prize]
case class Result(draw: Int, numbers: Numbers) {
//	assert(draw > 0, s"Invalid draw: ${draw}")
//	assert(numbers.size == 15, s"Invalid number combination: ${numbers}")
}

case class Bet(numbers: Numbers, owner: Option[String] = None) {
	assert(numbers.size == 15, s"Invalid number combination: ${numbers}")
}

case class BetHit(bet: Bet, result: Result) {
	lazy val hits = result.numbers.size - (result.numbers diff bet.numbers).size
}
