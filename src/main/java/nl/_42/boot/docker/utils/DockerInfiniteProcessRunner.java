package nl._42.boot.docker.utils;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public abstract class DockerInfiniteProcessRunner extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerInfiniteProcessRunner.class);

    protected final DockerInfiniteProcessTailer tailer;
    private final String[] command;
    private final String stdOutFilename;
    private final String stdErrFilename;

    public DockerInfiniteProcessRunner(String command,
                                       Map<String, String> properties,
                                       String stdErrFilename,
                                       String stdOutFilename,
                                       String startupVerificationText,
                                       Integer timeout) {
        super();

        this.command = replacePlaceholders(properties, command);
        this.stdOutFilename = stdOutFilename;
        this.stdErrFilename = stdErrFilename;

        try {
            cleanupFile(this.stdErrFilename);
            cleanupFile(this.stdOutFilename);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        this.tailer = new DockerInfiniteProcessTailer(
                this,
                stdOutFilename,
                stdErrFilename,
                startupVerificationText,
                timeout);
    }

    public boolean verify() throws IOException {
        return tailer.verify();
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

    private static void cleanupFile(String standardOutFilename) throws IOException {
        File output = new File(standardOutFilename);
        output.delete();
        output.createNewFile();
    }

    public void run() {

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
