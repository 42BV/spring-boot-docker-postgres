package nl._42.boot.docker.postgres;

import nl._42.boot.docker.utils.DockerInfiniteProcessRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DockerStartContainerCommand extends DockerInfiniteProcessRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerStartContainerCommand.class);

    public DockerStartContainerCommand(DockerPostgresProperties properties, boolean imageDownloaded) {
        super(properties.getUseDockerCommand(), properties, imageDownloaded);

        if (!imageDownloaded) {
            LOGGER.info("| Process will download (no visual feedback, please be patient)...");
        }
    }

}
