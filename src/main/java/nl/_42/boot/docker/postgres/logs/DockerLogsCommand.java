package nl._42.boot.docker.postgres.logs;

import nl._42.boot.docker.postgres.DockerPostgresProperties;
import nl._42.boot.docker.utils.DockerFiniteProcessRunner;
import nl._42.boot.docker.utils.DockerOutputResult;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class DockerLogsCommand extends DockerFiniteProcessRunner {

    private final static String COMMAND = "docker logs ${containerName}";

    private final DockerLogsRepeater repeater;

    public DockerLogsCommand(DockerPostgresProperties properties, DockerLogsRepeater repeater) {
        super(
                COMMAND,
                properties.getDockerLogsStdOutFilename(),
                properties.getDockerLogsStdErrFilename(),
                properties);
        this.repeater = repeater;
    }

    public boolean hasStarted() {
        try {
            final DockerOutputResult result = execute();
            return repeater.hasStarted(StringUtils.split(result.getStdOut(), "\r\n"));
        } catch (IOException err) {
            return false;
        }
    }

}
