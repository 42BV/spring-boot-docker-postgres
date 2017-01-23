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

    public boolean hasImage() throws IOException {
        DockerOutputResult result = execute();
        String[] lines = StringUtils.split(result.getStdOut(), "\r\n");
        boolean first = true;
        for (String line : lines) {
            if (first) {
                first = false;
                continue;
            }
            if (line != null && line.length() > 0) {
                String[] fields = line.split(" +");
                System.out.println(fields[0]);
                System.out.println(fields[1]);
            }
        }
        return true;
    }

}
