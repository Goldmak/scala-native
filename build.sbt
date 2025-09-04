scalaVersion := "3.4.2" // Используем свежую версию Scala 3

enablePlugins(ScalaNativePlugin) // Включаем режим нативной сборки

scalaVersion := "3.4.2"

enablePlugins(ScalaNativePlugin)

libraryDependencies ++= Seq(
  "com.softwaremill.sttp.client4" %%% "core" % "4.0.9",
  "com.softwaremill.sttp.client4" %%% "circe" % "4.0.9",
  "org.scalatest" %%% "scalatest" % "3.2.19" % "test",
  "io.circe" %%% "circe-generic" % "0.14.14"
)
