package nl._42.boot.docker.postgres;

import nl._42.boot.docker.utils.DockerFiniteProcessRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DockerStopContainerCommand extends DockerFiniteProcessRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerStopContainerCommand.class);

    private final static String COMMAND = "docker stop ${containerOccupyingPort}";

    private final String containerName;

    private final Integer port;

    public DockerStopContainerCommand(DockerPostgresProperties properties) {
        super(COMMAND, properties);
        this.containerName = properties.getContainerOccupyingPort();
        this.port = properties.getPort();
    }

    public void stopContainer() throws IOException {
        LOGGER.info("| Stopping container [" + containerName+ "] that occupies port " + port);
        execute().getExitCode();
    }

}
