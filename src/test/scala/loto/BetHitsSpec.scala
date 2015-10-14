package loto

import org.scalatest._

class BetHitsSpec extends FlatSpec with Matchers {

	"A Bet" should "have size 15" in {
		val bet = Bet(numbers = (1 to 15).toIndexedSeq)
		bet.numbers.size should be (15)
	}

	"A Result" should "have size 15 and positive draw" in {
		val result = Result(draw = 1, numbers = (1 to 15).toIndexedSeq)
		result.numbers.size should be (15)
		result.draw should be > 0
	}

	"A BetHit" should "have hit some numbers" in {
		val result = Result(draw = 1, numbers = (1 to 15).toIndexedSeq)

		BetHit(Bet(1 to 15), result).hits should be (15)
		BetHit(Bet(2 to 16), result).hits should be (14)
		BetHit(Bet(14 to 28), result).hits should be (2)
		BetHit(Bet(15 to 29), result).hits should be (1)
		BetHit(Bet(16 to 30), result).hits should be (0)
	}

}
