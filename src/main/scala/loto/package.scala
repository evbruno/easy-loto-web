
import akka.actor.{ActorLogging, ActorRef, Actor}

import scala.collection.immutable.IndexedSeq

package object loto {

	type Aposta = IndexedSeq[Int]

	case class Resultado(concurso: Int, aposta: Aposta) {
		assert(aposta.size == 15, s"Aposta inválida: ${aposta}")
	}

	case class Combinacao(aposta: Aposta) {
		val acertos = scala.collection.mutable.Map(11 -> 0, 12 -> 0, 13 -> 0, 14 -> 0, 15 -> 0)
		var totalAcertos = 0

		def acertou(pontos: Int) = {
			totalAcertos += 1
			acertos.update(pontos, acertos(pontos) + 1)
		}

		override def toString =
			if (totalAcertos == 0) s"Aposta ${aposta} num deu nada"
			else s"Aposta ${aposta} acertou: (${totalAcertos}) : ${acertos.toString}"
	}

}
