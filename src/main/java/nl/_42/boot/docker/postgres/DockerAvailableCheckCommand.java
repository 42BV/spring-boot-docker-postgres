package nl._42.boot.docker.postgres;

import nl._42.boot.docker.utils.DockerFiniteProcessRunner;
import nl._42.boot.docker.utils.DockerOutputResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DockerAvailableCheckCommand extends DockerFiniteProcessRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerPostgresBootSequence.class);

    private final static String COMMAND = "docker --version";

    public DockerAvailableCheckCommand(DockerPostgresProperties properties) {
        super(COMMAND, properties);
    }

    public void tryDocker() throws IOException {
        try {
            DockerOutputResult result = execute();
            LOGGER.info("| > " + result.getStdOut().trim());
        }
        catch (ExceptionInInitializerError ex) {
            LOGGER.error("| Docker not available. Make sure to install Docker on your system.");
            throw new ExceptionInInitializerError("Docker not available. Make sure to install Docker on your system.");
        }
    }

}
