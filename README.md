# eikona
Hobby project. Eikona will be a private photo hosting application that doesn't monetize your data

## Current goals
- Set up persistence

## Tooling

- Kotlin
- Ktor for the API server
- Junit for testing? Or something more Kotlin specific?
- OpenAPI for the API specs? Ktor compatibility is poor
- S3 compatible block storage (S3 for prod and Minio for testing)
- docker-compose for local testing

#### Pending
- What toolset to use for permissions? Themis looks interesting
- Use SQL to store object relationships such as file shares?
