# Eikona
Hobby project. Eikona will be a private photo hosting application that doesn't monetize your data

## Current goals
- Allow sharing with public audiences (set up share route and some UUID corresponding to public users )
- Readd list functionality based on permissions?
- Web UI

## Tooling

- Kotlin
- Ktor for the API server
- Junit + Kotest for testing
- SQL DB for relational data
- S3 compatible block storage (S3 for prod and Minio for testing)
- docker-compose for local testing

#### Pending
- What toolset to use for permissions? Themis looks interesting
- OpenAPI for the API specs? Ktor compatibility is poor
