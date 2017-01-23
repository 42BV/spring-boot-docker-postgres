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
    private final Integer timeout;

    private Integer sleepTime = 0;

    public DockerInfiniteProcessTailer( Thread dockerThread,
                                        DockerPostgresProperties properties) {
        this.dockerThread = dockerThread;
        this.dockerStandardOutFilename = properties.getStdOutFilename();
        this.dockerStandardErrorFilename = properties.getStdErrFilename();
        this.startupVerificationText = properties.getStartupVerificationText();
        this.timeout = properties.getTimeout();
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
                        if (line.toString().contains(startupVerificationText)) {
                            LOGGER.info("| > " + line.toString());
                            LOGGER.info("| = Docker startup verification text found");
                            logErrorLinesAsWarning();
                            return true;
                        }
                        LOGGER.info("| > " + line.toString());
                        line = new StringBuilder();
                    } else {
                        line.append(readChar);
                    }
                }
                else {
                    try {
                        if (sleepTime > timeout) {
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
