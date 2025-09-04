# Project: HTTP Client in Scala Native
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Goldmak_scala-native&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Goldmak_scala-native) [![CI](https://github.com/Goldmak/scala-native/actions/workflows/scala.yml/badge.svg)](https://github.com/Goldmak/scala-native/actions/workflows/scala.yml) [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

This project is a simple example of a console application in Scala, compiled into a native executable using Scala Native. The application sends an HTTP GET request, receives, and parses a JSON response.

## Project Structure
The project is organized as an `sbt` multi-project and consists of two main parts:
*   **Main Application:** Located in the root directory and contains the Scala Native application source code.
*   **Documentation Subproject (`docs`):** Located in the `docs/` directory and is used to generate a static website using Laika.

## 1. Prerequisites
To build and run this project, the following tools are required.

### Prerequisite Check Script: prereq.sh
This script will check for all necessary components. If you are on a Debian/Ubuntu-based system, it will...
How to use: make it executable (`chmod +x prereq.sh`) and run it (`./prereq.sh`).

## 2. Build and Run
All commands are executed from the project's root folder.
```bash
sbt nativeLink
./target/scala-3.4.2/scala-native-hello-native
```

## 3. Debugging Common Errors
### Problem 1: Dependency Resolution Error (ResolveException: Not Found)
**Symptom:** sbt cannot download a library.

**Cause:** Version incompatibility of the "Golden Trio": Scala Version + Scala Native Version + Library Version.

**Solution:**

1.  Go to Scaladex.
2.  Find each library and check which of its versions are compatible with your Scala version (3.x) and Scala Native (0.5).
3.  Select a working combination of versions in build.sbt and project/plugins.sbt.
4.  Ensure that triple percent signs (%%%) are used for Scala Native dependencies.

### Problem 2: Native Compilation/Linking Errors
**Symptom:** sbt downloaded dependencies, but the build fails with an error from clang or ld.

**Cause A:** `fatal error: 'some/file.h' file not found`. This means a dev package of a system library is missing.

**Solution:** Find the corresponding dev package by file name (curl.h -> libcurl) (e.g., libcurl4-openssl-dev) and install it via the system package manager (sudo apt-get install ...).

**Cause B:** `/usr/bin/ld: cannot find -l....` This means the linker cannot find the system library itself. Often this is a "dependency of a dependency".

**Solution:** By name (-lidn2 -> libidn2) find the corresponding dev package (libidn2-dev) and install it.

### Problem 3: Binary Incompatibility Error (binary-incompatible version of NIR)
**Symptom:** Deep AssertionError from the Scala Native compiler itself.

**Cause:** Version conflict between the sbt-scala-native plugin and Scala Native system libraries (nativelib, javalib) brought in by one of the dependencies.

**Solution:**

1.  Look in the error log for which version of the system library it is trying to use (e.g., nativelib version 0.5.8).
2.  Update the plugin version in project/plugins.sbt to the same version (0.5.8).
3.  If this does not help, perform a full cache cleanup: `rm -rf ~/.cache/coursier/`.

## 4. CI/CD - Continuous Integration
This project uses GitHub Actions to automatically build, test, and analyze code on every change. The process consists of two independent workflows:

### Main Workflow (`.github/workflows/scala.yml`)
This workflow is responsible for the main application:
1.  **Setup Dependencies:** Configures Java 17, sbt, and system libraries required for Scala Native (clang, libcurl).
2.  **Run Tests:** Executes `sbt test` to verify code correctness.
3.  **Code Quality Analysis:** Runs the SonarCloud scanner to find bugs, vulnerabilities, and code smells. Analysis results are available on [SonarCloud](https://sonarcloud.io/summary/new_code?id=Goldmak_scala-native) and directly in Pull Requests.
4.  **Dependency Graph:** Submits project dependency information to GitHub to track vulnerabilities.

### Website Workflow (`.github/workflows/pages.yml`)
This workflow is responsible for building and publishing the GitHub Pages site:
1.  **Generate Site:** Runs `cd docs && sbt laikaSite` to generate HTML files using the Laika library.
2.  **Publish:** Automatically publishes the generated files to GitHub Pages.