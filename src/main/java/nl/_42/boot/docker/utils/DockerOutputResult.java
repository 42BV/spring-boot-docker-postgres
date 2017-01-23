package nl._42.boot.docker.utils;

public class DockerOutputResult {

    private String stdOut;
    private String stdErr;
    private Integer exitCode;

    public DockerOutputResult(String stdOut, String stdErr, Integer exitCode) {
        this.stdOut = stdOut;
        this.stdErr = stdErr;
        this.exitCode = exitCode;
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

}
