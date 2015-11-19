object Spikez { //extends App {

	import loto._

	val concurso : Numbers = List(1, 2, 6, 7, 10, 11, 12, 15, 16, 17, 18, 20, 22, 24, 25)

	val bets : Array[Numbers] = Array(
		List(2, 3, 5, 7, 8, 10, 12, 14, 15, 16, 17, 19, 22, 23, 25),
		List(3, 4, 6, 7, 8, 11, 13, 14, 15, 17, 18, 19, 21, 22, 23)
	)

	val res = Result(1, concurso)

	bets.foreach { bet =>
		val bh = BetHit(Bet(bet), res)
		println(bh.hits)
	}

}
