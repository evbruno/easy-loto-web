package jobs

import scala.util.Random

object SomeJob extends App {

	println("Job started...")

	val dooh = Random.shuffle((1 to 10000000).map(_ * 10).toList.reverse).sorted.sum + System.currentTimeMillis

	println(s"Result: $dooh")

	
}
