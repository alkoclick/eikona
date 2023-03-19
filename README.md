# Eikona
Hobby project. Eikona will be a private photo hosting application that doesn't monetize your data

## Current goals
- Allow sharing with public audiences (set up share route and some UUID corresponding to public users )
- Make a user home page
- Actually make some pages with nice visuals
- Readd list functionality based on permissions?

## Tooling

- Kotlin
- Ktor for the API server
- Arrow for functional types
- Jackson for JSON parsing
- OkHTTP for HTTP client work
- Prometheus simpleclient for metrics
- Junit + Kotest for testing
- SQL DB for relational data
- S3 compatible block storage (S3 for prod and Minio for testing)
- docker-compose for local testing

## Running this locally

`gradle run` then check http://0.0.0.0:8080

#### Pending Thoughts & Decisions
- What toolset to use for permissions? Themis looks interesting
- OpenAPI for the API specs? Ktor compatibility is poor
