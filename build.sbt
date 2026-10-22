scalaVersion := "3.9.0"

lazy val root = (project in file("."))
  .settings(
    name := "project-monopoly",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.20" % Test
    )
  )