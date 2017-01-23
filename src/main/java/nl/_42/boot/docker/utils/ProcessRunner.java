package nl._42.boot.docker.utils;

import nl._42.boot.docker.postgres.DockerPostgresProperties;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ProcessRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessRunner.class);

    private final String[] command;
    private final String stdOutFilename;
    private final String stdErrFilename;

    public ProcessRunner(String command,
                         DockerPostgresProperties properties) {
        super();

        this.command = replacePlaceholders(properties.getProperties(), command);
        this.stdOutFilename = properties.getStdOutFilename();
        this.stdErrFilename = properties.getStdErrFilename();

        removeFiles();
    }

    private void removeFiles() {
        try {
            cleanupFile(this.stdErrFilename);
            cleanupFile(this.stdOutFilename);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static void cleanupFile(String standardOutFilename) throws IOException {
        File output = new File(standardOutFilename);
        output.delete();
        output.createNewFile();
    }

    private String[] replacePlaceholders(Map<String, String> properties, String commandLine) {
        StrSubstitutor sub = new StrSubstitutor(properties);
        String[] oldCmd = commandLine.split(" ");
        String[] newCmd = new String[oldCmd.length];
        int counter = 0;
        for (String cmd : oldCmd) {
            newCmd[counter++] = sub.replace(cmd);
        }
        return newCmd;
    }

    public int execute() {
        File output = new File(this.stdOutFilename);
        File errors = new File(this.stdErrFilename);

        final Process process;
        try {
            ProcessBuilder pb = new ProcessBuilder();
            pb.redirectOutput(output);
            pb.redirectError(errors);

            LOGGER.debug("| Process redirectInput(): " + pb.redirectInput());
            LOGGER.debug("| Process redirectOutput(): " + pb.redirectOutput());
            LOGGER.debug("| Process redirectError(): " + pb.redirectError());

            pb.command(command);
            process = pb.start();

        } catch (IOException e) {
            e.printStackTrace();
            return 1; // Error
        }

        try {
            while (process.isAlive()) {
                Thread.sleep(100);
            }
        } catch (InterruptedException err) {
            LOGGER.info("| Interruption signal received, proceeding to destroy process");
            process.destroy();
            LOGGER.info("| Process destroyed");
            return 0;
        }

        return process.exitValue();
    }

    public String getStdOutFilename() {
        return stdOutFilename;
    }

    public String getStdErrFilename() {
        return stdErrFilename;
    }

}
