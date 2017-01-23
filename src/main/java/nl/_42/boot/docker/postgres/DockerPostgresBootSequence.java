package nl._42.boot.docker.postgres;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DockerPostgresBootSequence {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerPostgresBootSequence.class);

    private final DockerPostgresProperties properties;

    public DockerPostgresBootSequence(DockerPostgresProperties properties) {
        this.properties = properties;
    }

    public DockerPostgresContainer execute() throws IOException {

        new DockerListContainers(properties).hasImage();

        DockerPostgresContainer postgresContainer = new DockerPostgresContainer(properties);
        postgresContainer.start();
        if (postgresContainer.verify()) {
            LOGGER.info("| Postgres container successfully started");
        } else {
            LOGGER.error("| Postgres failed to initialize");
            throw new ExceptionInInitializerError("The Docker Container failed to properly initialize.");
        }

        return postgresContainer;
    }

}
