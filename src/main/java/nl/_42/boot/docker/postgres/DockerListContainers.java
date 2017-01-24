package nl._42.boot.docker.postgres;

import nl._42.boot.docker.utils.DockerFiniteProcessRunner;
import nl._42.boot.docker.utils.DockerOutputResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DockerListContainers extends DockerFiniteProcessRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerPostgresBootSequence.class);

    private final static String COMMAND = "docker image ls";

    public DockerListContainers(DockerPostgresProperties properties) {
        super(COMMAND, properties);
    }

    public boolean hasImage(String imageName, String imageVersion) throws IOException {
        DockerOutputResult result = execute();
        String[] lines = StringUtils.split(result.getStdOut(), "\r\n");
        boolean first = true;
        for (String line : lines) {
            if (first) { // Ignore header line
                first = false;
                continue;
            }
            if (line != null && line.length() > 0) {
                String[] fields = line.split(" +");
                if (fields.length > 2) {
                    String currentImageName = fields[0];
                    String currentImageVersion = fields[1];
                    if (imageName.equals(currentImageName) && imageVersion.equals(currentImageVersion)) {
                        LOGGER.info("| Image [" + imageName + ":" + imageVersion + "] already downloaded");
                        return true;
                    }
                }
            }
        }
        LOGGER.info("| Image [" + imageName + ":" + imageVersion + "] not yet downloaded");
        return false;
    }

}
