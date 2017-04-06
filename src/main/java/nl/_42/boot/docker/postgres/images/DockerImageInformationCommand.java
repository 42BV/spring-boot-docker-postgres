package nl._42.boot.docker.postgres.images;

import nl._42.boot.docker.postgres.DockerPostgresProperties;
import nl._42.boot.docker.postgres.shared.DockerHeaderMismatch;
import nl._42.boot.docker.utils.DockerFiniteProcessRunner;
import nl._42.boot.docker.utils.DockerOutputResult;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class DockerImageInformationCommand extends DockerFiniteProcessRunner {

    private final static String COMMAND = "docker images";

    public DockerImageInformationCommand(DockerPostgresProperties properties) {
        super(COMMAND, properties);
    }

    public DockerImageInformation interpretDockerImageListing()
            throws DockerHeaderMismatch, IOException {
        DockerOutputResult result = execute();
        return new DockerImageInformation(StringUtils.split(result.getStdOut(), "\r\n"));
    }

}
