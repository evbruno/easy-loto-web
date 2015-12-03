package loto

import org.scalatest._

class BetHitsSpec extends FlatSpec with Matchers {

	"A Bet" should "have size 15" in {
		val bet = Bet(numbers = (1 to 15).toIndexedSeq)
		bet.numbers.size should be (15)
	}

	"A Result" should "have size 15 and positive draw" in {
		val result = Result(draw = 1, numbers = (1 to 15).toIndexedSeq, drawDate = null, prizes = null)
		result.numbers.size should be (15)
		result.draw should be > 0
	}

}
