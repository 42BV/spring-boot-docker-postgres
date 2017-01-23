package nl._42.boot.docker.postgres;

import nl._42.boot.docker.utils.DockerInfiniteProcessRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DockerPostgresContainer extends DockerInfiniteProcessRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerPostgresContainer.class);

    private final static String COMMAND =
            "docker run --rm -e POSTGRES_PASSWORD=${password} -p ${port}:5432 --name ${containerName} ${imageName}:${imageVersion}";

    public DockerPostgresContainer(DockerPostgresProperties properties) {
        super(COMMAND, properties);

        LOGGER.info("| Docker Postgres Properties");
        LOGGER.info("| * Image name: " + properties.getImageName());
        LOGGER.info("| * Image version: " + properties.getImageVersion());
        LOGGER.info("| * Timeout: " + properties.getTimeout());
        LOGGER.info("| * Container name: " + properties.getContainerName());
        LOGGER.info("| * Port: " + properties.getPort());
        LOGGER.info("| * Password: " + properties.getPassword());
        LOGGER.info("| * Startup Verification Text: [" + properties.getStartupVerificationText() + "]");
        LOGGER.info("| * Std out: " + properties.getStdOutFilename());
        LOGGER.info("| * Std err: " + properties.getStdErrFilename());
    }

}
