# CRS - Clubroom Reservation System

![build](https://github.com/averak/crs/workflows/build/badge.svg)
![test](https://github.com/averak/crs/workflows/test/badge.svg)
![Version 1.0](https://img.shields.io/badge/version-1.0-yellow.svg)
[![MIT License](http://img.shields.io/badge/license-MIT-blue.svg?style=flat)](LICENSE)

This app is a clubroom reservation system.

## Develop

### Requirements

- Java OpenJDK 11
- docker-compose

### How to run

`$ java -jar crs-<version>.jar`

### How to run on dev environment

1. Clone this repository
2. Run `$ cd crs`
3. Run `$ docker-compose up -d`
4. Run application
   - macOS or Linux: `$ ./gradlew bootRun`
   - Windows: `$ ./gradlew.bat bootRun`

### How to build

1. Run `$ cd crs`.
2. Run build
   - macOS or Linux: `$ ./gradlew build`
   - Windows: `$ ./gradlew.bat build`
3. When build successful, you can find .jar file in `app/build/libs`

### How to run test

```sh
# run only unit test
$ ./gradlew unitTest
# run only integration test
$ ./gradlew integrationTest
# run all test
$ ./gradlew test
```

### API docs

This project support Swagger UI.

1. Run application
2. Access to [Swagger UI](http://localhost:8080/swagger-ui/)
