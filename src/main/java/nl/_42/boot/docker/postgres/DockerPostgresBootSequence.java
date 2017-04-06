package nl._42.boot.docker.postgres;

import nl._42.boot.docker.postgres.containers.DockerContainerAvailableCheck;
import nl._42.boot.docker.postgres.containers.DockerContainerInformation;
import nl._42.boot.docker.postgres.containers.DockerContainerInformationCommand;
import nl._42.boot.docker.postgres.images.DockerImageAvailableCheck;
import nl._42.boot.docker.postgres.images.DockerImageInformation;
import nl._42.boot.docker.postgres.images.DockerImageInformationCommand;
import nl._42.boot.docker.postgres.shared.DockerHeaderMismatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import java.io.IOException;

public class DockerPostgresBootSequence {

    private static final Integer DEFAULT_PORT = 5432;
    private static final Logger LOGGER = LoggerFactory.getLogger(DockerPostgresBootSequence.class);

    private final DockerPostgresProperties properties;
    private final DataSourceProperties dataSourceProperties;

    public DockerPostgresBootSequence(DockerPostgresProperties properties, DataSourceProperties dataSourceProperties) {
        this.properties = properties;
        this.dataSourceProperties = dataSourceProperties;
    }

    public DockerPostgresContainer execute() throws IOException, InterruptedException {

        LOGGER.info("| Docker Postgres Properties");
        LOGGER.info("| * Image name: " + properties.getImageName());
        LOGGER.info("| * Image version: " + properties.getImageVersion());
        LOGGER.info("| * Force clean: " + properties.isForceClean());
        LOGGER.info("| * Timeout: " + properties.getTimeout());
        LOGGER.info("| * Container name: " + properties.getContainerName());
        if (properties.getPort() == null) {
            if (dataSourceProperties.getUrl() != null) {
                properties.setPort(determinePort(dataSourceProperties.getUrl()));
                // Scrap the port from the JDBC URL
            } else {
                properties.setPort(DEFAULT_PORT);
            }
        }
        LOGGER.info("| * Port: " + properties.getPort());
        LOGGER.info("| * Password: " + properties.getPassword());
        LOGGER.info("| * Startup Verification Text: [" + properties.getStartupVerificationText() + "]");
        LOGGER.info("| * Times expected verification text: " + properties.getTimesExpectedVerificationText() + "x");
        LOGGER.info("| * After verification wait: " + properties.getAfterVerificationWait() + "ms");
        LOGGER.info("| * Docker command: [" + properties.getDockerCommand() + "]");
        LOGGER.info("| * Custom variables (" + properties.getCustomVariables().size() + ")");
        for (String key : properties.getCustomVariables().keySet()) {
            LOGGER.info("|   - " + key + ": " + properties.getCustomVariables().get(key));
        }
        LOGGER.info("| * Std out: " + properties.getStdOutFilename());
        LOGGER.info("| * Std err: " + properties.getStdErrFilename());

        // Verify if Docker is available on the command-line
        new DockerAvailableCheck(properties).tryDocker();

        // Read the container list
        final DockerContainerInformation containers;
        try {
            containers = new DockerContainerInformationCommand(properties).interpretDockerContainerListing();
        } catch (DockerHeaderMismatch ex) {
            throw new ExceptionInInitializerError(ex.getMessage());
        }

        // Force clean the old container
        if (    properties.isForceClean() &&
                new DockerContainerAvailableCheck(properties, containers).hasContainer()) {
            new DockerForceRemoveContainer(properties).forceRemove();
        }

        // Verify if the image is downloaded (influences the timeout)
        boolean imageDownloaded = false;
        try {
            DockerImageInformation images =
                    new DockerImageInformationCommand(properties).interpretDockerImageListing();
            imageDownloaded = new DockerImageAvailableCheck(properties, images).hasImage();
        } catch (DockerHeaderMismatch ex) {
            // Non-fatal; continue, just assume the image not to have been downloaded
            LOGGER.warn("| The image information was not read, assuming download not to have taken place");
        }

        // Start up the Docker Postgres container (blocking thread)
        DockerPostgresContainer postgresContainer = new DockerPostgresContainer(properties, imageDownloaded);
        postgresContainer.start();
        if (postgresContainer.verify()) {
            applyAfterVerificationWait(properties.getAfterVerificationWait());
            LOGGER.info("| Postgres container successfully started");
        } else {
            LOGGER.error("| Postgres failed to initialize");
            throw new ExceptionInInitializerError("The Docker Container failed to properly initialize.");
        }

        return postgresContainer;
    }

    private Integer determinePort(String url) {
        if (url == null || url.length() == 0) {
            throw new ExceptionInInitializerError("spring.datasource.url is empty. No port could be derived.");
        }
        int lastColonPos = url.lastIndexOf(':');
        int slashAfterPortPos = url.indexOf('/', lastColonPos);
        if (lastColonPos == -1 || slashAfterPortPos == -1 || slashAfterPortPos < lastColonPos + 2) {
            throw new ExceptionInInitializerError("spring.datasource.url does not have port information: [" + url + "]. No port could be derived.");
        }
        return Integer.parseInt(url.substring(lastColonPos + 1, slashAfterPortPos));
    }

    private void applyAfterVerificationWait(Integer afterVerificationWait) throws InterruptedException {
        if (afterVerificationWait > 0) {
            LOGGER.info("| Applying after verification wait of " + afterVerificationWait + "ms");
            Thread.sleep(afterVerificationWait);
        }
    }

}
