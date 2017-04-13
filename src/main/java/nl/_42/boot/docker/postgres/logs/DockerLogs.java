package nl._42.boot.docker.postgres.logs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DockerLogs {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerLogs.class);

    private final String startupVerificationText;
    private final Integer timesExpectedVerificationText;
    private int lastLineRead;
    private int totalTimesStartupVerificationTextEncountered;
    private final String[] lines;

    public DockerLogs(
            String startupVerificationText,
            Integer timesExpectedVerificationText,
            int lastLineRead,
            int totalTimesStartupVerificationTextEncountered,
            String[] lines) {
        this.startupVerificationText = startupVerificationText;
        this.timesExpectedVerificationText = timesExpectedVerificationText;
        this.lastLineRead = lastLineRead;
        this.totalTimesStartupVerificationTextEncountered = totalTimesStartupVerificationTextEncountered;
        this.lines = lines;
    }

    public boolean hasStarted() {
        int linesRead = 0;
        int timesStartupVerificationTextEncountered = this.totalTimesStartupVerificationTextEncountered;
        boolean found = false;

        for (String line : lines) {
            if (linesRead < lastLineRead) {
                // Ignore all lines already read
                linesRead++;
                continue;
            }
            linesRead++;
            LOGGER.info("| > " + line);

            if (line.contains(startupVerificationText)) {
                timesStartupVerificationTextEncountered++;
                LOGGER.info("| = Verification text encountered " + totalTimesStartupVerificationTextEncountered + "x");
                if (totalTimesStartupVerificationTextEncountered == timesExpectedVerificationText) {
                    LOGGER.info("| = Docker startup verified");
                    found = true;
                    break;
                }
            }
        }

        this.lastLineRead = linesRead;
        this.totalTimesStartupVerificationTextEncountered = timesStartupVerificationTextEncountered;
        return found;
    }

    public int getLastLineRead() {
        return lastLineRead;
    }

    public int getTotalTimesStartupVerificationTextEncountered() {
        return totalTimesStartupVerificationTextEncountered;
    }

}
