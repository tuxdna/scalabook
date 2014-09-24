name := "scala-session-code"

scalaVersion :="2.10.2"

version :="1.0"

libraryDependencies ++= Seq(
        "com.netflix.rxjava" % "rxjava-scala" % "0.19.6",
        "com.typesafe.akka" %% "akka-actor" % "2.2.3",
        "com.typesafe.akka" %% "akka-testkit" % "2.2.3"
)

libraryDependencies += "org.scala-lang" % "scala-library" % "2.10.2"


libraryDependencies <+= scalaVersion { "org.scala-lang" % "scala-swing" % _ }

