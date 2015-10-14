package loto

import spray.json.DefaultJsonProtocol

package object loto {

	type Numbers = IndexedSeq[Int]

	type DateTime = org.joda.time.DateTime

	trait BetProtocols extends DefaultJsonProtocol {
		implicit val ipInfoFormat = jsonFormat2(Bet.apply)
		implicit val ipPairSummaryRequestFormat = jsonFormat2(Result.apply)
		implicit val ipPairSummaryFormat = jsonFormat2(BetHit.apply)
	}

}
