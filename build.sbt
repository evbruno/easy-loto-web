import sbt.Keys._

enablePlugins(JavaAppPackaging)

lazy val commonSettings = Seq(
	name := "easy-loto",
	organization := "etc.bruno",
	version := "1.0",
	scalaVersion := "2.11.7"
)

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

//fork in run := true

libraryDependencies ++= {
	val akkaV = "2.3.12"
	val akkaStreamV = "1.0"
	val scalaTestV = "2.2.5"
	Seq(
		"com.typesafe.akka" %% "akka-actor" % akkaV,
		"com.typesafe.akka" %% "akka-stream-experimental" % akkaStreamV,
		"com.typesafe.akka" %% "akka-http-core-experimental" % akkaStreamV,
		"com.typesafe.akka" %% "akka-http-experimental" % akkaStreamV,
		"com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaStreamV,
		"com.typesafe.akka" %% "akka-http-testkit-experimental" % akkaStreamV,
		"org.scalatest" %% "scalatest" % scalaTestV % "test",
		"org.scala-lang.modules" %% "scala-xml" % "1.0.5"
	)
}

Revolver.settings

lazy val root = (project in file(".")).
				settings(commonSettings: _*)

lazy val jobs = project.dependsOn(root).
				settings(commonSettings: _*).
				settings(
					name := "easy-loto-jobz",
					libraryDependencies ++= {
						Seq("commons-io" % "commons-io" % "2.4",
							"net.ruippeixotog" %% "scala-scraper" % "0.1.1"
						)
					}
				)
