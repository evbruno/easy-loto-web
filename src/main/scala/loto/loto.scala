
package object loto {

	type Numbers = IndexedSeq[Int]

	type Prize = (Int, String)

	import scala.language.implicitConversions

	implicit def fromListToIndexedSeq(in: List[Int]) : Numbers = in.toIndexedSeq

}
