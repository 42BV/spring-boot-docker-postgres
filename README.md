# Docker Postgres

> If you develop for a particular database, your development process should reflect this.

# Introduction

If you subscribe to the above quote, this might be the library for you. Previously, when we developed applications for Postgres, we had a development pipeline based on H2, an in-memory database. The advantages of H2 are appealing; it runs at your behest and it is torn down with ease. 

However, this approach also has substantial downsides. The most important one is that you are developing to another database. This becomes most apparent at the time when the application must be deployed to its first environment and queries break down. Another disadvantages is that it is not possible to make use of the powerful features of Postgres in H2; mocking becomes a regrettable requirement. 

This library is a plug-and-play Docker Postgres enabler that hooks you into the Spring Boot lifecycle. It starts up your Postgres when you need it, and it tears it down when you are done.

# System Requirements

You must have Docker installed on your system.

## Mac OS X

If you are on Mac OS X, following these instructions: https://docs.docker.com/docker-for-mac/

*Docker for Mac* is an small wrapper which runs at low overhead on your system. It makes sure you have full Docker functionality. For those who have known boot2docker; this is the version of it that actually works.

## Linux

Follow the link for your Linux distro: https://docs.docker.com/engine/installation/linux/


# Usage

## Spring Boot POM

Add the following two dependencies to your POM. This is enough to make it all work.

```xml
<dependency>
    <groupId>nl.42</groupId>
    <artifactId>spring-boot-starter-docker</artifactId>
    <version>0.2.0</version>
    <scope>provided</scope>
</dependency>

<dependency>
    <groupId>nl.42</groupId>
    <artifactId>spring-boot-docker-postgres</artifactId>
    <version>0.2.0</version>
    <scope>provided</scope>
</dependency>
```

Note that the ```provided``` scope ascertains the Docker Postgres container is available for spring-boot:run and unit tests, but will not be assembled as a lib in your WAR file.

## Arguments

You can tweak the configuration to use for your Docker run.

| argument                  | description   |
| ------------------------- | ------------- |
| container-name            | the Docker container on which the image has to run. Default: postgression |
| enabled                   | determines if the Postgres container must be started. Default: true |
| force-clean               | determines if the container must be removed if it already exists. Default: false|
| image-name                | the image name to be used for deploying the container. Default: postgres |
| image-version             | the version of the image to be used for deplying the container. Advice is to specify wherever possible. Default: latest |
| password                  | the password used to apply for the database on the container. Default: postgres |
| port                      | the port that you can access the database on. Will be mapped to the container's own 5432 port. Default: 5432 |
| startup-verification-text | the text that will be searched within the Docker log. If found, the Postgres container is available. Default: "PostgreSQL init process complete; ready for start up." |
| std-out-filename          | the file to write the Docker output to. Default: "docker-std-out.log" |
| std-err-filename          | the file to write the Docker errors to. Default: docker-std-err.log |
| timeout                   | the timeout to apply for booting the container. Note that the value will not be used if the image has to be downloaded. Default: 300000 (5 minutes) |

## Best practices

# Troubleshooting

