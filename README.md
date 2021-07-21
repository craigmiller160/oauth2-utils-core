# OAuth2 Utils Core

A generic library with utilities to help integrate apps with the sso-oauth2-server project.

## Documentation

### 1. [Properties](./docs/PROPERTIES.md)
### 2. [More Configuration](./docs/CONFIGURATION.md)
### 3. [How to Use Authentication](./docs/USING.md)

## SQL

Do not forget to execute the provided SQL in the database for the consuming application.

## Tomcat Filters

This project uses the Apache Tomcat `RestCsrfPreventionFilter`, copied from their git repo. All credit goes to Apache Tomcat for this piece of it.