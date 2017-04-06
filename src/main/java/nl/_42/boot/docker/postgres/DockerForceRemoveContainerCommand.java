package nl._42.boot.docker.postgres;

import nl._42.boot.docker.utils.DockerFiniteProcessRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DockerForceRemoveContainerCommand extends DockerFiniteProcessRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerForceRemoveContainerCommand.class);

    private final static String COMMAND = "docker container rm -fv ${containerName}";
    private final String containerName;

    public DockerForceRemoveContainerCommand(DockerPostgresProperties properties) {
        super(COMMAND, properties);
        this.containerName = properties.getContainerName();
    }

    public void forceRemove() throws IOException {
        LOGGER.info("| Forcibly removing container [" + containerName+ "]");
        execute().getExitCode();
    }

}
