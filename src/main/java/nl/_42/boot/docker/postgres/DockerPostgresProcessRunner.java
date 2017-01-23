package nl._42.boot.docker.postgres;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class DockerPostgresProcessRunner extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerPostgresProcessRunner.class);

    private final static String COMMAND =
            "docker run --rm -e POSTGRES_PASSWORD=${password} -p ${port}:5432 --name ${containerName} ${imageName}:${imageVersion}";

    private final DockerPostgresProperties properties;

    private final DockerPostgresTailer tailer;

    public DockerPostgresProcessRunner(DockerPostgresProperties properties) {
        super();

        LOGGER.info(">>> Configuring Docker Postgres");
        LOGGER.info("| Docker Postgres Properties");
        LOGGER.info("| * Image name: " + properties.getImageName());
        LOGGER.info("| * Image version: " + properties.getImageVersion());
        LOGGER.info("| * Timeout: " + properties.getTimeout());
        LOGGER.info("| * Container name: " + properties.getContainerName());
        LOGGER.info("| * Port: " + properties.getPort());
        LOGGER.info("| * Password: " + properties.getPassword());
        LOGGER.info("| * Startup Verification Text: [" + properties.getStartupVerificationText() + "]");
        LOGGER.info("| * Std out: " + properties.getStdOutFilename());
        LOGGER.info("| * Std err: " + properties.getStdErrFilename());

        try {
            cleanupFile(properties.getStdErrFilename());
            cleanupFile(properties.getStdOutFilename());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        this.properties = properties;
        this.tailer = new DockerPostgresTailer(
                this,
                properties.getStdOutFilename(),
                properties.getStdErrFilename(),
                properties.getStartupVerificationText(),
                properties.getTimeout());
    }

    public boolean verify() throws IOException {
        return tailer.verify();
    }

    private static String[] replacePlaceholders(Map<String, String> properties, String commandLine) {
        StrSubstitutor sub = new StrSubstitutor(properties);
        String[] oldCmd = commandLine.split(" ");
        String[] newCmd = new String[oldCmd.length];
        int counter = 0;
        for (String cmd : oldCmd) {
            newCmd[counter++] = sub.replace(cmd);
        }
        return newCmd;
    }

    private static void cleanupFile(String standardOutFilename) throws IOException {
        File output = new File(standardOutFilename);
        output.delete();
        output.createNewFile();
    }

    public void run() {

        File output = new File(properties.getStdOutFilename());
        File errors = new File(properties.getStdErrFilename());

        final Process process;
        try {
            ProcessBuilder pb = new ProcessBuilder();
            pb.redirectOutput(output);
            pb.redirectError(errors);

            LOGGER.debug("| Process redirectInput(): " + pb.redirectInput());
            LOGGER.debug("| Process redirectOutput(): " + pb.redirectOutput());
            LOGGER.debug("| Process redirectError(): " + pb.redirectError());

            pb.command(replacePlaceholders(properties.getProperties(), COMMAND));
            process = pb.start();

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            while (process.isAlive()) {
                Thread.sleep(100);
            }
        } catch (InterruptedException err) {
            LOGGER.info("| Interruption signal received, proceeding to destroy process");
            process.destroy();
            LOGGER.info("| Process destroyed");
        }
    }

}
