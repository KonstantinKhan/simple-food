# Testing guide

Use code-tester to write and run tests.

## Testing

### All tests

`./gradlew test`

### Specific test

`./gradlew :simple-food-transport-mappers:test --tests "com.khan366kos.mapper.ProductMapperTest"`

### Module tests

`./gradlew :simple-food-repo-memory:test`

### Postgresql
- Use TestContainer