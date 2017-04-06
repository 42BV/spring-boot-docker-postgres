package nl._42.boot.docker.postgres.images;

import nl._42.boot.docker.postgres.DockerPostgresBootSequence;
import nl._42.boot.docker.postgres.DockerPostgresProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DockerImageAvailableCheck {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerPostgresBootSequence.class);

    private final String imageName;
    private final String imageVersion;

    private DockerImageInformation images;

    public DockerImageAvailableCheck(DockerPostgresProperties properties,
                                     DockerImageInformation images) {
        this.imageName = properties.getImageName();
        this.imageVersion = properties.getImageVersion();
        this.images = images;
    }

    public boolean hasImage() {
        for (DockerImage image : images.getList()) {
            if (imageName.equals(image.getRepository()) && imageVersion.equals(image.getTag())) {
                LOGGER.info("| Image [" + imageName + ":" + imageVersion + "] already downloaded");
                return true;
            }
        }
        LOGGER.info("| Image [" + imageName + ":" + imageVersion + "] not yet downloaded");
        return false;
    }

}
