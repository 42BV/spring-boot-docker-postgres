package nl._42.boot.docker.postgres.containers;

import nl._42.boot.docker.postgres.DockerPostgresBootSequence;
import nl._42.boot.docker.postgres.DockerPostgresProperties;
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
        return containers.hasContainer(containerName);
    }

}
