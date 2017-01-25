package nl._42.boot.docker.postgres;

import nl._42.boot.docker.utils.DockerInfiniteProcessRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DockerPostgresContainer extends DockerInfiniteProcessRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerPostgresContainer.class);

    public DockerPostgresContainer(DockerPostgresProperties properties, boolean imageDownloaded) {
        super(properties.getDockerCommand(), properties, imageDownloaded);

        if (!imageDownloaded) {
            LOGGER.info("| Process will download (no visual feedback, please be patient)...");
        }
    }

}
