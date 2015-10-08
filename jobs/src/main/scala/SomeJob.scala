import com.typesafe.config.ConfigFactory

import scala.util.Random

object SomeJob extends App {

	val config = ConfigFactory.load()
	val st = config.getInt("jobs.sleepTime")

	val title = s"# Job started. Sleeping for ${st} ms #"
	val width = title.length

	def line = println("#" * width)

	line
	println(title)
	line

	Thread.sleep(st)

	val x = loto.Config.dooh
	val dooh = Random.shuffle((1 to 1000000).map(_ * 10).toList.reverse).sorted.sum + x

	line
	val res = s"# FakeJob result: $dooh "
	print(res)
	print(" " * (width - res.length - 1))
	println("#")
	line

}
