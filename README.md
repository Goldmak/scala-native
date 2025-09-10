# Project: HTTP Client on Scala Native
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Goldmak_scala-native&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Goldmak_scala-native) [![CI](https://github.com/Goldmak/scala-native/actions/workflows/scala.yml/badge.svg)](https://github.com/Goldmak/scala-native/actions/workflows/scala.yml) [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) [![Built with Laika](https://img.shields.io/badge/Built%20with-Laika-blue.svg)](https://laika.site/) [![GitHub Pages deploy](https://github.com/Goldmak/scala-native/actions/workflows/pages.yml/badge.svg)](https://github.com/Goldmak/scala-native/actions/workflows/pages.yml) [![Scala Version](https://img.shields.io/badge/Scala-3.4.2-blue.svg)](https://www.scala-lang.org/) [![Docker Image](https://img.shields.io/docker/v/goodmak/scala-native-hello?label=Docker&sort=semver)](https://hub.docker.com/r/goodmak/scala-native-hello)

This project is a simple example of a console application in Scala, compiled into a native executable file using Scala Native. The application sends an HTTP GET request, receives and parses a JSON response.

## Project Structure
The project is organized as an `sbt` multi-project and consists of two main parts:
*   **Main application:** Located in the root directory and contains the Scala Native application source code.
*   **Documentation subproject (`docs`):** Located in the `docs/` directory and is used to generate a static site using Laika.

## 1. Prerequisites (System Requirements)
The following tools are required to build and run the project.

### Bash script to check and install prereq.sh
This script will check for all required components and, if you are using a Debian/Ubuntu-based system,
How to use: make it executable (chmod +x check_prereqs.sh) and run (./check_prereqs.sh).

## 2. Build and Run
All commands are executed from the root project folder.
```bash
sbt nativeLink
./target/scala-3.4.2/scala-native-hello
```

## 3. Typical Error Debugging Methodology
### Issue 1: Dependency Resolution Error (ResolveException: Not Found)
**Symptom:** sbt cannot download the library.

**Cause:** Incompatibility of the "Golden Trio" versions: Scala Version + Scala Native Version + Library Version.

**Solution:**

1.  Go to Scaladex.
2.  Find each library and check which versions are compatible with your Scala version (3.x) and Scala Native (0.5).
3.  Select a working combination of versions in build.sbt and project/plugins.sbt.
4.  Make sure that for Scala Native dependencies, the triple percent sign (%%%) is used.

### Issue 2: Native Compilation/Linking Errors
**Symptom:** sbt downloaded dependencies, but the build fails with an error from clang or ld.

**Cause A:** `fatal error: 'some/file.h' file not found`. This means that the dev package of the system library is missing.

**Solution:** Find the corresponding dev package by file name (curl.h -> libcurl) (libcurl4-openssl-dev) and install it through the system package manager (sudo apt-get install ...).

**Cause B:** `/usr/bin/ld: cannot find -l....` This means that the linker cannot find the system library itself. This is often a "dependency of a dependency".

**Solution:** Find the corresponding dev package by name (-lidn2 -> libidn2) (libidn2-dev) and install it.

### Issue 3: Binary Incompatibility Error (binary-incompatible version of NIR)
**Symptom:** Deep AssertionError error from the Scala Native compiler itself.

**Cause:** Version conflict between the sbt-scala-native plugin and Scala Native system libraries (nativelib, javalib) that were "brought" by one of the dependencies.

**Solution:**

1.  Look in the error log to see which version of the system library it is trying to use (for example, nativelib version 0.5.8).
2.  Update the plugin version in project/plugins.sbt to the same version (0.5.8).
3.  If this doesn't help, perform a complete cache cleanup: `rm -rf ~/.cache/coursier/`.

## 4. CI/CD - Continuous Integration
This project uses GitHub Actions for automatic build, testing and code analysis with each change. The process consists of two independent workflows:

### Main Workflow (`.github/workflows/scala.yml`)
This workflow is responsible for the main application:
1.  **Installing dependencies:** Configures Java 17, sbt and system libraries required for Scala Native (clang, libcurl).
2.  **Running tests:** Executes the `sbt test` command to verify code correctness.
3.  **Code quality analysis:** Runs the SonarCloud scanner to find bugs, vulnerabilities and "code smells". Analysis results are available on [SonarCloud](https://sonarcloud.io/summary/new_code?id=Goldmak_scala-native) and directly in Pull Requests.
4.  **Dependency graph collection:** Sends information about project dependencies to GitHub for vulnerability tracking.

### Site Workflow (`.github/workflows/pages.yml`)
This workflow is responsible for building and publishing the site on GitHub Pages:
1.  **Site generation:** Runs the `cd docs && sbt laikaSite` command to generate HTML files using the Laika library.
2.  **Publishing:** Automatically publishes the generated files to GitHub Pages.