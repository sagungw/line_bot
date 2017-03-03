name := """line-bot"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  javaJpa,
  cache,
  javaWs,
  "dom4j" % "dom4j" % "1.6.1",
  "org.postgresql" % "postgresql" % "9.4.1212",
  "org.hibernate" % "hibernate-core" % "5.2.6.Final",
  "org.projectlombok" % "lombok" % "1.16.12",
  "com.codeborne" % "phantomjsdriver" % "1.3.0",
  "io.github.bonigarcia" % "webdrivermanager" % "1.6.0",
  "com.linecorp.bot" % "line-bot-servlet" % "1.6.0"
)