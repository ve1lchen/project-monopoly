scalaVersion := "3.9.0"

lazy val root = rootProject
  .settings(
    name := "project-monopoly",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.19" % Test,
      "org.scalameta" %% "munit"     % "1.2.3"  % Test
    )
  )
