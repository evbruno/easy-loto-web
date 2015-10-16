
package object loto {

	type Numbers = IndexedSeq[Int]

	type DateTime = org.joda.time.DateTime

	import scala.language.implicitConversions

	implicit def fromLitToIndexedSeq(in: List[Int]) : Numbers = in.toIndexedSeq

}
