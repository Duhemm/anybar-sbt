lazy val root = (project in file(".")).
  settings(
    name := "sbt-anybar",
    description := "sbt plugin to display tests results using AnyBar",
    version := "0.1.0-SNAPSHOT",
    sbtPlugin := true,
    organization := "org.duhemm",
    libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.1" % "test"
  ) settings (
    Formatting.sbtFilesSettings: _*
  )
