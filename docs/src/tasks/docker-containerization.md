# Containerization with Docker: Writing Dockerfile and Publishing Images

Docker is a platform for developing, delivering, and running applications in containers. Containers allow you to package an application with all its dependencies into a single isolated "package" that can be easily transported and run in any environment.

## Why Docker for Scala Native?

*   **Isolation:** The application runs in an isolated environment, eliminating dependency conflicts with the host system.
*   **Portability:** The same Docker image can be run on any machine with Docker, whether it's a local computer, server, or cloud.
*   **Reproducibility:** Guarantees that the application will always work the same, regardless of the environment.
*   **Simplified deployment:** Deployment is reduced to running a single Docker image.

## Writing Dockerfile for Scala Native Application

Dockerfile is a text file containing instructions for building a Docker image. For a Scala Native application, we'll need a multi-stage build to make the image as compact as possible.

### Dockerfile Example

```dockerfile
# --- Stage 1: Build the native executable ---
# Use a base image with JDK and sbt for building
FROM eclipse-temurin:17-jdk-jammy AS builder

# Install required system dependencies for Scala Native
# (e.g., clang, zlib, libcurl, libidn2, libssl)
RUN apt-get update && apt-get install -y \
    clang \
    zlib1g-dev \
    libcurl4-openssl-dev \
    libidn2-dev \
    libssl-dev \
    pkg-config \
    git \
    && rm -rf /var/lib/apt/lists/*

# Install sbt
ENV SBT_VERSION=1.10.7
RUN curl -L "https://github.com/sbt/sbt/releases/download/v${SBT_VERSION}/sbt-${SBT_VERSION}.tgz" | tar xz -C /usr/local --strip-components=1
ENV PATH="/usr/local/bin:${PATH}"

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Run native executable build
# Use 'sbt nativeLink' to create the native binary
# 'sbt clean' for cleaning, 'sbt update' for updating dependencies
RUN sbt clean update nativeLink

# --- Stage 2: Create the final minimal image ---
# Use a minimal base image since the native binary doesn't require JVM
FROM ubuntu:jammy-slim

# Install only the system libraries needed by the native binary
# (e.g., libcurl, libidn2, libssl)
RUN apt-get update && apt-get install -y \
    libcurl4 \
    libidn2-0 \
    libssl3 \
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy the native executable from the build stage
COPY --from=builder /app/target/scala-3.4.2/scala-native-hello .

# Specify the command to run the application
ENTRYPOINT ["/app/scala-native-hello"]

# Optional: Specify port if the application is a server
# EXPOSE 8080
```

### Dockerfile Explanation:

*   **Stage 1 (builder):** Used for compilation. Includes JDK, sbt and all dev dependencies required for Scala Native. After building, this stage is discarded, and only the result goes into the final image.
*   **Stage 2 (final):** Uses a minimal image (e.g., `ubuntu:jammy-slim`) since the native binary doesn't need a JVM. Only the executable file and the required runtime libraries are copied here.
*   **`scala-native-hello`:** The name of the executable file created by the Scala Native build process.
*   **`target/scala-3.4.2/`:** The path to the executable file may differ depending on the Scala version.

## Building Docker Image

Navigate to your project's root directory (where the Dockerfile is located) and run the command:

```bash
docker build -t your-docker-username/scala-native-app:latest .
```

*   `-t`: Tag for your image (Docker Hub username / image name : version).
*   `.`: Indicates that the Dockerfile is in the current directory.

## Running Docker Image

```bash
docker run -p 8080:8080 your-docker-username/scala-native-app:latest
```

*   `-p`: Port forwarding (if your application listens on a port).

## Publishing Docker Image

To publish an image to Docker Hub or GitHub Container Registry (GHCR):

1.  **Authenticate:**
    ```bash
docker login # For Docker Hub
# or
docker login ghcr.io -u YOUR_GITHUB_USERNAME # For GHCR
    ```
2.  **Push the image:**
    ```bash
docker push your-docker-username/scala-native-app:latest # For Docker Hub
# or
docker push ghcr.io/YOUR_GITHUB_USERNAME/YOUR_REPO_NAME/scala-native-app:latest # For GHCR
    ```

## Integration with `sbt-native-packager` (Alternative/Supplement)

The `sbt-native-packager` plugin (which is already in your `project/plugins.sbt`) can automate Dockerfile creation and image building.

*   **Add settings to `build.sbt`:**
    ```scala
enablePlugins(DockerPlugin)

dockerBaseImage := "ubuntu:jammy-slim"
dockerEntrypoint := Seq("bin/scala-native-hello") // Your binary name
dockerExposedPorts := Seq(8080) // If needed
dockerRepository := Some("your-docker-username") // For Docker Hub
# dockerRepository := Some("ghcr.io/YOUR_GITHUB_USERNAME") // For GHCR
# dockerAlias := DockerAlias(
#   dockerRepository.value,
#   None,
#   Some("scala-native-app"),
#   Some("latest")
# )
    ```
*   **Build image via sbt:**
    ```bash
sbt docker:publishLocal # Build and save locally
sbt docker:publish      # Build and publish (requires authentication)
    ```
This is a more integrated approach, but requires configuration in `build.sbt`.