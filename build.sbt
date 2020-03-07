name := "RadixProject"

version := "0.1"

scalaVersion := "2.12.0"
scalacOptions ++= Seq("-Ypartial-unification")

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies += "com.slamdata" %% "matryoshka-core" % "0.21.3"
libraryDependencies += "org.http4s" %% "http4s-blaze-server" % "0.20.0-RC1"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.3" % Runtime
libraryDependencies += "org.http4s" %% "http4s-dsl" % "0.20.0"
libraryDependencies += "com.github.xuwei-k" %% "optparse-applicative" % "0.8.2"




