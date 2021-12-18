# OAuth2 Utils Core

A generic library with utilities to help integrate apps with the sso-oauth2-server project.

## Documentation

### 1. [Properties](./docs/PROPERTIES.md)
### 2. [More Configuration](./docs/CONFIGURATION.md)
### 3. [How to Use Authentication](./docs/USING.md)

## SQL

Do not forget to execute the provided SQL in the database for the consuming application.

## Javax vs Jakarta

At the time of writing this, the Java world is transitioning between Java EE and Jakarta EE. One of the applications I have uses the latest Jersey implementation based on the latest Jakarta EE, however my other applications are all Spring Boot applications which still depend on Java EE. This means there is a breaking change in the form of the `javax.*` and `jakarta.*` package names. I cannot at this time commit to one or the other due to the nature of the projects that use this library.

Therefore I am creating two separate master branches. The regular `master` branch will be maintained normally and will use Java EE and the `javax.*` package name. The `jakarta-master` branch will use Jakarta EE and the `jakarta.*` package name. Most importantly, the regular `master` branch will be kept up to date with `develop`, which will remain on Java EE, while `jakarta-master` will only be updated when a special Jakarta EE build is needed for the one application that requires it.