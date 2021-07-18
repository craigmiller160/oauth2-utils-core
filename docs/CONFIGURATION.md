# More Configuration

Additional configuration for the library

## Logging

This library uses SLF4J. Whatever implementation the project uses, make sure this logger is exposed:

```
io.craigmiller160.oauth2
```

## SQL

The file at `sql/schema.sql` must be executed in the database that the application has access to.