package nl._42.boot.docker.postgres.containers;

import nl._42.boot.docker.postgres.DockerPostgresBootSequence;
import nl._42.boot.docker.postgres.DockerPostgresProperties;
import nl._42.boot.docker.postgres.containers.DockerContainer;
import nl._42.boot.docker.postgres.containers.DockerContainerInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DockerContainerAvailableCheck {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerPostgresBootSequence.class);

    private final String containerName;

    private final DockerContainerInformation containers;

    public DockerContainerAvailableCheck(   DockerPostgresProperties properties,
                                            DockerContainerInformation containers) {
        this.containerName = properties.getContainerName();
        this.containers = containers;
    }

    public boolean hasContainer() {
        for (DockerContainer container : containers.getList()) {
            if (containerName.equals(container.getNames())) {
                LOGGER.info("| Container [" + containerName + "] exists");
                return true;
            }
        }
        LOGGER.info("| Container [" + containerName + "] not found; no force remove needed");
        return false;
    }

}
