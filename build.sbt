name := "bitten-buffer"

organization := "zd"

version := "0.0.1"

scalaVersion := "2.13.8"

val weaverTest = "0.6.13"
val circe = "0.14.2"
val monix = "3.4.1"

libraryDependencies ++= Seq(
  "com.disneystreaming" %% "weaver-monix" % weaverTest % Test,
  "com.disneystreaming" %% "weaver-scalacheck" % weaverTest % Test,
  "io.circe" %% "circe-generic" % circe % Test,
  "io.circe" %% "circe-parser" % circe % Test,
  "io.circe" %% "circe-literal" % circe % Test,
  "io.monix" %% "monix" % monix  % Test
)

lazy val commonCompilerOptions =
  Seq(
      "-unchecked",
      "-encoding", "UTF-8",
      "-deprecation",
      "-feature"
    )

scalacOptions ++=
  commonCompilerOptions ++
  Seq(
      "-Werror"
  )

testFrameworks := Seq(
  new TestFramework("weaver.framework.Monix")
)

scalacOptions in (Compile, console) := commonCompilerOptions

scalacOptions in (Test, console) := commonCompilerOptions

