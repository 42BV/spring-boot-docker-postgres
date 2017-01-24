package nl._42.boot.docker.postgres;

import nl._42.boot.docker.utils.DockerInfiniteProcessRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DockerPostgresContainer extends DockerInfiniteProcessRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerPostgresContainer.class);

    private final static String COMMAND =
            "docker run --rm -e POSTGRES_PASSWORD=${password} -p ${port}:5432 --name ${containerName} ${imageName}:${imageVersion}";

    public DockerPostgresContainer(DockerPostgresProperties properties, boolean imageDownloaded) {
        super(COMMAND, properties, imageDownloaded);

        if (!imageDownloaded) {
            LOGGER.info("| Process will download (no visual feedback, please be patient)...");
        }
    }

}
