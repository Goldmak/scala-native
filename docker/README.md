# Docker Containerization for Scala Native Application

This folder contains the Docker configuration to containerize the Scala Native application.

## Building the Docker Image

There are two approaches for building the Docker image:

### Approach 1: Multi-stage build (recommended)
This approach builds the Scala Native executable inside the Docker container:

```bash
docker build -t scala-native-hello -f docker/Dockerfile .
```

### Approach 2: Pre-built executable
First, you need to build the Scala Native executable locally:

```bash
sbt nativeLink
```

Then build the Docker image using the simple Dockerfile:

```bash
docker build -t scala-native-hello -f docker/Dockerfile.simple .
```

Alternatively, you can use docker-compose:

```bash
docker-compose -f docker/docker-compose.yml build
```

## Running the Docker Container

After building the image, you can run the application in a container:

```bash
docker run --rm scala-native-hello
```

Or with docker-compose:

```bash
docker-compose -f docker/docker-compose.yml run --rm scala-native-app
```

## How it Works

### Multi-stage approach:
1. **Builder Stage**: Uses a JDK image with sbt and Scala Native dependencies to compile the application
2. **Runtime Stage**: Copies the native executable to a minimal Ubuntu image and installs only the required system libraries

### Pre-built executable approach:
1. **Runtime Stage**: Uses a minimal Ubuntu image and copies the pre-built native executable

Both approaches result in a small, efficient container that doesn't require a JVM to run.