# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## Unreleased

## [0.7.1] - 2018-01-24
### Fixed
- Issue [#29](https://github.com/42BV/spring-boot-docker-postgres/issues/29), **By default use in-memory destination mounts for Docker run**; by default the Docker run will make use of in-memory mounts for the application and data path in the standard Postgres Docker image. If in-memory must be disabled, it can be done in the properties. Custom paths can be passed as a list under the inMemoryMountDestinations list.
