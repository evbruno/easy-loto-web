package loto

import loto._

// TODO drawDate: Date, prizes: List[Prize]
case class Result(draw: Int, numbers: Numbers) {
	assert(draw > 0, s"Invalid draw: ${draw}")
	assert(numbers.size == 15, s"Invalid number combination: ${numbers}")
}

case class Bet(numbers: Numbers, owner: Option[String] = None) {
	assert(numbers.size == 15, s"Invalid number combination: ${numbers}")
}

case class BetHit(bet: Bet, result: Result) {
	lazy val hits = result.numbers.size - (result.numbers diff bet.numbers).size
}