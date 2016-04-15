name := "akka-account"
version := "0.1"
scalaVersion := "2.11.7"
lazy val akkaVersion = "2.4.4"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "org.springframework" % "spring-context" % "4.2.5.RELEASE",
  "javax.inject" % "javax.inject" % "1",
  "junit" % "junit" % "4.11" % "test",
  "com.novocode" % "junit-interface" % "0.9" % "test->default"
)

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")
scalacOptions += "-target:jvm-1.8"
testOptions += Tests.Argument(TestFrameworks.JUnit, "-v", "-a")
