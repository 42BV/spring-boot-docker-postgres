package nl._42.boot.docker.postgres;

import nl._42.boot.docker.utils.DockerFiniteProcessRunner;
import nl._42.boot.docker.utils.DockerOutputResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public abstract class AbstractDockerAvailableCheck extends DockerFiniteProcessRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerPostgresBootSequence.class);

    public AbstractDockerAvailableCheck(String command, DockerPostgresProperties properties) {
        super(command, properties);
    }

    public boolean hasValues(ExpectedValue... expectedValues) throws IOException {
        DockerOutputResult result = execute();
        String[] lines = StringUtils.split(result.getStdOut(), "\r\n");
        boolean first = true;
        for (String line : lines) {
            if (first) { // Ignore header line
                first = false;
                continue;
            }
            if (line != null && line.length() > 0) {
                String[] fields = line.split("\\s{2,}");
                boolean match = true;
                for (ExpectedValue expectedValue : expectedValues) {
                    if (!expectedValue.match(fields)) {
                        match = false;
                    }
                }
                if (match) {
                    return true;
                }
            }
        }
        return false;
    }

}
