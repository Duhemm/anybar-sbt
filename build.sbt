lazy val root = (project in file(".")).
  settings(
    name := "sbt-anybar",
    description := "sbt plugin to display tests results using AnyBar",
    version := "0.0.1",
    sbtPlugin := true,
    organization := "com.github.duhemm",
    libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.1" % "test",

    publishMavenStyle := true,
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases"  at nexus + "service/local/staging/deploy/maven2")
    },
    publishArtifact in Test := false,
    pomExtra := (
      <url>https://github.com/Duhemm/anybar-sbt</url>
      <licenses>
        <license>
          <name>MIT</name>
          <url>http://opensource.org/licenses/MIT</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <scm>
        <url>git@github.com:Duhemm/anybar-sbt.git</url>
        <connection>scm:git:git@github.com:Duhemm/anybar-sbt.git</connection>
      </scm>
      <developers>
        <developer>
          <id>Duhemm</id>
          <name>Martin Duhem</name>
        </developer>
      </developers>)
  ) settings (
    Formatting.sbtFilesSettings: _*
  )
