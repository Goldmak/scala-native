# Project: HTTP Client in Scala Native
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Goldmak_scala-native&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Goldmak_scala-native) [![CI](https://github.com/Goldmak/scala-native/actions/workflows/scala.yml/badge.svg)](https://github.com/Goldmak/scala-native/actions/workflows/scala.yml) [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

This project is a simple example of a console application in Scala, compiled into a native executable using Scala Native. The application sends an HTTP GET request, receives, and parses a JSON response.

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
(Content of this section remains the same)

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
1.  **Generate Site:** Runs `sbt -f docs/build.sbt laikaSite` to generate HTML files using the Laika library.
2.  **Publish:** Automatically deploys the generated files to GitHub Pages.
