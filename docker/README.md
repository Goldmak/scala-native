# Docker Containerization for Scala Native Application

This folder contains Docker configurations to containerize the Scala Native application. We provide multiple approaches for different use cases.

## Dockerfile Approaches

### 1. Multi-stage with Git Clone (`Dockerfile`)
This is the recommended approach for Docker Hub publishing. It clones the repository and builds everything within Docker.

```bash
docker build -t scala-native-hello .
```

**Pros:**
- Fully self-contained, only requires Docker
- Perfect for Docker Hub automated builds
- Always builds from the latest source code in the repository
- Reproducible builds anywhere
- No dependency on local build artifacts

**Cons:**
- Longer build time (needs to clone repo and build)
- Requires internet access during build
- Larger image size due to build dependencies

### 2. Pre-built Executable (`Dockerfile.simple`)
This approach uses a pre-built executable that you compile locally.

First, build the Scala Native executable locally:
```bash
sbt nativeLink
```

Then build the Docker image:
```bash
docker build -t scala-native-hello -f Dockerfile.simple .
```

**Pros:**
- Fastest build time
- Smallest image size
- Works without internet during Docker build

**Cons:**
- Requires local build step first
- Not suitable for Docker Hub automated builds
- Not reproducible without the local environment
- Dependent on local build artifacts

## Running the Docker Container

After building any of the images, you can run the application in a container:

```bash
docker run --rm scala-native-hello
```

Or with docker-compose:

```bash
docker-compose -f docker-compose.yml run --rm scala-native-app
```

## Docker Hub Publishing Recommendation

For publishing to Docker Hub, we recommend using the `Dockerfile` approach (multi-stage with git clone) because:

1. **Docker Hub Compatibility**: Docker Hub automated builds work by cloning the repository and running Dockerfile instructions
2. **Reproducibility**: Anyone can build the exact same image by running the same Dockerfile
3. **Self-contained**: The build process is entirely contained within the Dockerfile
4. **Version Control**: The Docker image is always built from the source code in the repository

## Image Sizes

- Multi-stage with Git Clone: ~104MB
- Pre-built Executable: ~94MB

The slight increase in image size for the multi-stage approach is a reasonable trade-off for the benefits of having a fully automated, reproducible build process that works with Docker Hub's infrastructure.