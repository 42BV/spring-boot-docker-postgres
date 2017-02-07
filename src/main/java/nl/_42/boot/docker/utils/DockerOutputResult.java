package nl._42.boot.docker.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DockerOutputResult {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerOutputResult.class);

    private String stdOut;
    private String stdErr;
    private Integer exitCode;
    private String[] errLines = new String[0];

    public DockerOutputResult(String stdOut, String stdErr, Integer exitCode) {
        this.stdOut = stdOut;
        this.stdErr = stdErr;
        this.exitCode = exitCode;
        if (!StringUtils.isBlank(stdErr)) {
            errLines = stdErr.split("\\r\\n|\\n|\\r");
        }
    }

    public String getStdOut() {
        return stdOut;
    }

    public String getStdErr() {
        return stdErr;
    }

    public Integer getExitCode() {
        return exitCode;
    }

    public String[] getErrLines() {
        return errLines;
    }

    public void logErrLines() {
        for (String line : getErrLines()) {
            LOGGER.error("| > " + line);
        }
    }
}
