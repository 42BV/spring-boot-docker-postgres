package nl._42.boot.docker.postgres.images;

import nl._42.boot.docker.postgres.shared.DockerHeaderMismatch;
import nl._42.boot.docker.postgres.shared.DockerInformation;
import nl._42.boot.docker.postgres.shared.DockerListHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class DockerImageInformation extends DockerInformation<DockerImage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerImageInformation.class);

    public DockerImageInformation(String[] lines) throws DockerHeaderMismatch {
        super(lines);
    }

    @Override
    protected DockerListHeaders checkHeaders(String[] headers) {
        return new DockerImageListHeaders(headers);
    }

    @Override
    protected DockerImage createLine(Map<Integer, Integer> actualToIntendedMapping) {
        return new DockerImage(actualToIntendedMapping);
    }

    public boolean hasImage(String imageName, String imageVersion) {
        for (DockerImage image : getList()) {
            if (imageName.equals(image.getRepository()) && imageVersion.equals(image.getTag())) {
                LOGGER.info("| Image [" + imageName + ":" + imageVersion + "] already downloaded");
                return true;
            }
        }
        LOGGER.info("| Image [" + imageName + ":" + imageVersion + "] not yet downloaded");
        return false;
    }

}
