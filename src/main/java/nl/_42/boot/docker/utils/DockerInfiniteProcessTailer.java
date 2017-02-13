package nl._42.boot.docker.utils;

import nl._42.boot.docker.postgres.DockerPostgresProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class DockerInfiniteProcessTailer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerInfiniteProcessTailer.class);

    private final Thread dockerThread;
    private final String dockerStandardOutFilename;
    private final String dockerStandardErrorFilename;
    private final String startupVerificationText;
    private final Integer timesExpectedVerificationText;
    private final Integer timeout;

    private Integer sleepTime = 0;
    private Integer verificationTextEncountered = 0;

    public DockerInfiniteProcessTailer( Thread dockerThread,
                                        DockerPostgresProperties properties,
                                        boolean imageDownloaded) {
        this.dockerThread = dockerThread;
        this.dockerStandardOutFilename = properties.getStdOutFilename();
        this.dockerStandardErrorFilename = properties.getStdErrFilename();
        this.startupVerificationText = properties.getStartupVerificationText();
        this.timesExpectedVerificationText = properties.getTimesExpectedVerificationText();
        this.timeout = imageDownloaded ? properties.getTimeout() : -1;
        LOGGER.info("| Applied timeout: (-1 means no timeout): " + this.timeout);
    }

    public boolean verify() throws IOException {

        final BufferedInputStream reader;
        try {
            reader = new BufferedInputStream(new FileInputStream( dockerStandardOutFilename) );
        } catch (FileNotFoundException e) {
            throw e;
        }

        try {
            StringBuilder line = new StringBuilder();
            while(true) {
                if( reader.available() > 0 ) {
                    char readChar = (char)reader.read();
                    if (readChar == '\n') {
                        LOGGER.info("| > " + line.toString());
                        if (line.toString().contains(startupVerificationText)) {
                            verificationTextEncountered++;
                            LOGGER.info("| = Verification text encountered " + verificationTextEncountered + "x");
                            if (verificationTextEncountered == timesExpectedVerificationText) {
                                LOGGER.info("| > " + line.toString());
                                LOGGER.info("| = Docker startup verification text found");
                                logErrorLinesAsWarning();
                                return true;
                            }
                        }
                        line = new StringBuilder();
                    } else {
                        line.append(readChar);
                    }
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
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            reader.close();
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
