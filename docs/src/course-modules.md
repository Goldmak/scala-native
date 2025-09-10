# Course: "Scala SDET: from Native Applications to Blockchain"

## ✅ Module 1: Scala Basics and Native Compilation
*   \[x\] Scala philosophy and platform (JVM/Native).
*   \[x\] Environment setup (JDK, sbt, clang, system libraries).
*   \[x\] Project building with sbt and dependency management.
*   \[x\] Compilation to native executable file with Scala Native.
*   \[x\] Basic syntax, case class, match, Option and Either handling.
*   \[x\] Using sttp for http work, parsing and serialization through circe-generic.

## ✅ Module 2: Advanced Testing and Code Quality
*   \[x\] Unit testing: Deep dive into ScalaTest (assert, ignore, intercept).
*   \[x\] Mocking: Writing a "manual" mock for test isolation.
*   \[x\] E2E (End-to-End) testing: Our test with realClient is essentially a simple E2E test, as it checks the entire chain from client to real server.
*   \[x\] Static code analysis: Integration with SonarCloud via GitHub Actions.
*   \[ \] Using automated tests to gate a Pull Request.

## ✅ Module 3: CI/CD, Security and Containerization
*   \[x\] CI/CD with GitHub Actions: Automatic build and test execution.
*   \[x\] Security testing: Dependabot.yml and sbt-dependency-submission are configured, which monitor vulnerabilities in dependencies.
*   \[x\] Containerization with Docker: Moving artefacts to Dockerfile for local testing.
*   \[x\] Containerization with Docker: Writing a full Dockerfile and publishing image to Docker Hub.
*   \[x\] Using docker compose for local Dockerfile building.
*   \[x\] Creating indicators based on badges.
*   \[x\] Frontend based MD: Integration with Laika via GitHub Actions.
*   \[x\] Projects separation approach for backend/frontend.

## Module 4: Asynchronous Programming and Data Processing
*   \[ \] Asynchrony with Future: How to perform long operations without blocking the program.
*   \[ \] Working with databases: Connecting to PostgreSQL with Doobie.
*   \[ \] Working with message queues: Apache Kafka basics.

## Module 5: Diving into Blockchain on Scala
*   \[ \] Waves client on Scala.
*   \[ \] Blockchain framework (Scorex).
*   \[ \] EVM client on Scala (Mantis).