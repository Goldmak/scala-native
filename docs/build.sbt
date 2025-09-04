import laika.sbt.LaikaPlugin.autoImport.*
import laika.helium.config.*
import laika.ast.Path.Root
import laika.helium.Helium

ThisBuild / scalaVersion := "3.4.2"

lazy val docs = (project in file("."))
  .enablePlugins(LaikaPlugin)
  .settings(
    name := "docs",
    laikaTheme := laika.helium.Helium.defaults.site.
    topNavigationBar(
        homeLink = IconLink.internal(Root / "index.md",HeliumIcon.home),
      highContrast = true
    ).build
  )



