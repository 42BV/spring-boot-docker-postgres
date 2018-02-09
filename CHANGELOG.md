# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## Unreleased
### Fixed
- Issue [#31](https://github.com/42BV/spring-boot-docker-postgres/issues/31), **Docker stop command runs the wrong argument**; when the library notices another Docker container occupies the port that it needs to run on, a ```docker stop``` command is initiated for the port-occupying container. However, this logic was flawed, as it tried to stop a container with the same name as the container that had to be started. Fixed by using the proper container name.

## [0.7.1] - 2018-01-25
### Fixed
- Issue [#29](https://github.com/42BV/spring-boot-docker-postgres/issues/29), **By default use in-memory destination mounts for Docker run**; by default the Docker run will make use of in-memory mounts for the application and data path in the standard Postgres Docker image. If in-memory must be disabled, it can be done in the properties. Custom paths can be passed as a list under the inMemoryMountDestinations list.
