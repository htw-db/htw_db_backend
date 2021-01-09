


version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala).settings(
  name := """htw_db_backend""",
  organization := "com.htw",
  scalaVersion := "2.13.3",
  libraryDependencies ++= Seq(
    guice,
    "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test,
    "com.typesafe.play" %% "play-slick" % "5.0.0",
    "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0",
    "org.postgresql" % "postgresql" % "42.2.14",
    "org.apache.directory.api" % "api-all" % "1.0.0-RC2"
  ),
  libraryDependencies += evolutions
)

// Adds additional packages into Twirl
// TwirlKeys.templateImports += "com.htw.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.htw.binders._"
