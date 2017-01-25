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

        LOGGER.info("| Docker Postgres Properties");
        LOGGER.info("| * Image name: " + properties.getImageName());
        LOGGER.info("| * Image version: " + properties.getImageVersion());
        LOGGER.info("| * Force clean: " + properties.isForceClean());
        LOGGER.info("| * Timeout: " + properties.getTimeout());
        LOGGER.info("| * Container name: " + properties.getContainerName());
        LOGGER.info("| * Port: " + properties.getPort());
        LOGGER.info("| * Password: " + properties.getPassword());
        LOGGER.info("| * Startup Verification Text: [" + properties.getStartupVerificationText() + "]");
        LOGGER.info("| * Std out: " + properties.getStdOutFilename());
        LOGGER.info("| * Std err: " + properties.getStdErrFilename());

        // Verify if Docker is available on the command-line
        new DockerAvailableCheck(properties).tryDocker();

        // Force clean the old container
        if (    properties.isForceClean() &&
                new DockerContainerAvailableCheck(properties).hasContainer()) {
            new DockerForceRemoveContainer(properties).forceRemove();
        }

        // Verify if the image is downloaded (influences the timeout)
        boolean imageDownloaded = new DockerImageAvailableCheck(properties).hasImage();

        // Start up the Docker Postgres container (blocking thread)
        DockerPostgresContainer postgresContainer = new DockerPostgresContainer(properties, imageDownloaded);
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
