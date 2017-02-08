package nl._42.boot.docker.postgres;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DockerImageAvailableCheck extends AbstractDockerAvailableCheck {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerPostgresBootSequence.class);

    private final static String COMMAND = "docker images";

    private final String imageName;
    private final String imageVersion;

    public DockerImageAvailableCheck(DockerPostgresProperties properties) {
        super(COMMAND, properties);
        this.imageName = properties.getImageName();
        this.imageVersion = properties.getImageVersion();
    }

    public boolean hasImage() throws IOException {
        if (hasValues(new ExpectedValue(0, imageName), new ExpectedValue(1, imageVersion))) {
            LOGGER.info("| Image [" + imageName + ":" + imageVersion + "] already downloaded");
            return true;
        }
        LOGGER.info("| Image [" + imageName + ":" + imageVersion + "] not yet downloaded");
        return false;
    }

}
