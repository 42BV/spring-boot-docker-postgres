package nl._42.boot.docker.utils;

import nl._42.boot.docker.postgres.DockerPostgresProperties;
import nl._42.boot.docker.postgres.logs.DockerLogsCommand;
import nl._42.boot.docker.postgres.logs.DockerLogsRepeater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class DockerInfiniteProcessTailer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerInfiniteProcessTailer.class);

    private final Thread dockerThread;
    private final String dockerStandardErrorFilename;
    private final Integer timeout;
    private final DockerPostgresProperties properties;
    private final DockerLogsRepeater dockerLogsRepeater;

    private Integer sleepTime = 0;

    public DockerInfiniteProcessTailer( Thread dockerThread,
                                        DockerPostgresProperties properties,
                                        boolean imageDownloaded) {
        this.dockerThread = dockerThread;
        this.timeout = imageDownloaded ? properties.getTimeout() : -1;
        this.dockerStandardErrorFilename = properties.getStdErrFilename();
        this.properties = properties;
        dockerLogsRepeater = new DockerLogsRepeater(
                properties.getStartupVerificationText(),
                properties.getTimesExpectedVerificationText());
        LOGGER.info("| Applied timeout: (-1 means no timeout): " + this.timeout);
    }

    public boolean verify() throws IOException {

        try {
            while(true) {
                // Temp measure...
                try { Thread.sleep( 500 ); } catch (InterruptedException e) { e.printStackTrace(); }
                DockerLogsCommand dockerLogsCommand = new DockerLogsCommand(properties, dockerLogsRepeater);

                if (dockerLogsCommand.hasStarted()) {
                    logErrorLinesAsWarning();
                    return true;
                }
                else {
                    try {
                        if (timeout != -1 && sleepTime > timeout) {
                            LOGGER.error("| = Startup could not be verified. Interrupting process. <check your verification string>");
                            dockerThread.interrupt();
                        }

                        sleepTime += 100;
                        Thread.sleep( 100 );

                        if (!dockerThread.isAlive()) {
                            LOGGER.error("| = Docker Postgres container has stopped processing");
                            logErrorLinesAsError();
                            return false;
                        }
                    }
                    catch( InterruptedException ex ) {
                        LOGGER.error("| = Docker Postgres container failed to initialize");
                        logErrorLinesAsError();
                        return false;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        logErrorLinesAsError();
        LOGGER.error("| = Docker Postgres container failed to initialize");
        return false;
    }

    private void logErrorLinesAsError() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(new File(dockerStandardErrorFilename)))) {
            String errLine;
            while ((errLine = br.readLine()) != null) {
                LOGGER.error("| > " + errLine);
            }
        }
    }

    private void logErrorLinesAsWarning() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(new File(dockerStandardErrorFilename)))) {
            String errLine;
            while ((errLine = br.readLine()) != null) {
                LOGGER.warn("| > " + errLine);
            }
        }
    }

}
