package nl._42.boot.docker.postgres;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DockerContainerAvailableCheck extends AbstractDockerAvailableCheck {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerPostgresBootSequence.class);

    private final static String COMMAND = "docker ps";

    private final String containerName;

    public DockerContainerAvailableCheck(DockerPostgresProperties properties) {
        super(COMMAND, properties);
        this.containerName = properties.getContainerName();
    }

    public boolean hasContainer() throws IOException {
        if (hasValues(new ExpectedValue(6, containerName))) {
            LOGGER.info("| Container [" + containerName + "] exists");
            return true;
        }
        LOGGER.info("| Container [" + containerName + "] not found; no force remove needed");
        return false;
    }

}
