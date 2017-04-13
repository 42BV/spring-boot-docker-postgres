package nl._42.boot.docker.postgres.logs;

public class DockerLogsRepeater {

    private int lastLineRead = 0;
    private int totalTimesStartupVerificationTextEncountered = 0;

    private final String startupVerificationText;
    private final Integer timesExpectedVerificationText;

    public DockerLogsRepeater(String startupVerificationText, Integer timesExpectedVerificationText) {
        this.startupVerificationText = startupVerificationText;
        this.timesExpectedVerificationText = timesExpectedVerificationText;
    }

    public boolean hasStarted(String[] lines) {
        DockerLogs dockerLogs = new DockerLogs(
                startupVerificationText,
                timesExpectedVerificationText,
                lastLineRead,
                totalTimesStartupVerificationTextEncountered,
                lines);
        boolean hasStarted = dockerLogs.hasStarted();
        this.lastLineRead = dockerLogs.getLastLineRead();
        this.totalTimesStartupVerificationTextEncountered =
                dockerLogs.getTotalTimesStartupVerificationTextEncountered();
        return hasStarted;
    }

}
