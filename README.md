# CRMS - Clubroom Reservation Management System

![build](https://github.com/averak/CRMS/workflows/build/badge.svg)
![test](https://github.com/averak/CRMS/workflows/test/badge.svg)
![code check](https://github.com/averak/CRMS/workflows/code%20check/badge.svg)
![deploy](https://github.com/averak/CRMS/workflows/deploy/badge.svg)
![Version 1.0](https://img.shields.io/badge/version-1.0-yellow.svg)
[![MIT License](http://img.shields.io/badge/license-MIT-blue.svg?style=flat)](LICENSE)

This app is a clubroom reservation management system.

## Develop

### Requirements

- Angular
- Java OpenJDK 11
- docker-compose

### Usage

If you want to run on Windows, you can use `gradlew.bat` instead of of `gradlew`.

#### How to bulid

```sh
$ ./gradlew build
```

When build successful, you can find .jar file in `app/build/libs`

#### How to run

Default port is `8080`. If you want to change port, run with `-Dserver.port=XXXX`.

```sh
# 1. run .jar file
$ java -jar crms-<version>.jar

# 2. run on dev environment
$ ./gradlew bootRun
```

#### How to test

```sh
# 1 all tests
$ ./gradlew test

# 2. only unit tests
$ ./gradlew unitTest

# 3. only integration tests
$ ./gradlew integrationTest
```

### API docs

This project support Swagger UI.

1. Run application
2. Access to [Swagger UI](http://localhost:8080/swagger-ui/)

## Wiki

You can find more details on [wiki](https://github.com/averak/CRMS/wiki).
