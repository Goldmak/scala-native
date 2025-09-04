# Documentation Subproject

This directory contains the source files and build configuration for the project's documentation website, generated using Laika.

The website is automatically built and deployed to GitHub Pages via a dedicated GitHub Actions workflow.

## How to build locally

To build the documentation site locally, navigate to the root of the repository and run:

```bash
sbt laikaSite
```

The generated site will be available in `docs/target/laika/site`.
