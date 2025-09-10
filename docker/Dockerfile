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

# Clone the repository
RUN git clone https://github.com/Goldmak/scala-native.git /app

# Change to the application directory
WORKDIR /app

# Run native executable build
# Use 'sbt nativeLink' to create the native binary
# 'sbt clean' for cleaning, 'sbt update' for updating dependencies
RUN sbt clean update nativeLink

# --- Stage 2: Create the final minimal image ---
# Use a minimal base image since the native binary doesn't require JVM
FROM ubuntu:22.04

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
# Use the correct path to the executable (named "app" in Docker)
COPY --from=builder /app/target/scala-3.4.2/app .

# Rename the file for convenience
RUN mv /app/app /app/scala-native-hello

# Specify the command to run the application
ENTRYPOINT ["/app/scala-native-hello"]