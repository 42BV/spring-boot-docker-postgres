package nl._42.boot.docker.postgres.containers;

import nl._42.boot.docker.postgres.DockerPostgresProperties;
import nl._42.boot.docker.postgres.shared.DockerHeaderMismatch;
import nl._42.boot.docker.utils.DockerFiniteProcessRunner;
import nl._42.boot.docker.utils.DockerOutputResult;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class DockerContainerInformationCommand extends DockerFiniteProcessRunner {

    private final static String COMMAND = "docker ps";

    public DockerContainerInformationCommand(DockerPostgresProperties properties) {
        super(COMMAND, properties);
    }

    public DockerContainerInformation interpretDockerContainerListing()
            throws DockerHeaderMismatch, IOException {
        DockerOutputResult result = execute();
        return new DockerContainerInformation(StringUtils.split(result.getStdOut(), "\r\n"));
    }

}
