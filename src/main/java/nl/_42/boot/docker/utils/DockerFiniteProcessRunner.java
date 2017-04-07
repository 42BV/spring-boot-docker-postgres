package nl._42.boot.docker.utils;

import nl._42.boot.docker.postgres.DockerPostgresProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class DockerFiniteProcessRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerFiniteProcessRunner.class);

    private final ProcessRunner processRunner;

    private final String command;
    private final String stdOutFilename;
    private final String stdErrFilename;

    public DockerFiniteProcessRunner(  String command,
                                       DockerPostgresProperties properties) {
        super();

        this.command = command;
        this.stdOutFilename = properties.getStdOutFilename();
        this.stdErrFilename = properties.getStdErrFilename();

        processRunner = new ProcessRunner(command, properties);
    }

    public DockerOutputResult execute() throws IOException {
        try {
            int exitValue = processRunner.execute();

            DockerOutputResult result = new DockerOutputResult(
                    readFile(stdOutFilename, Charset.defaultCharset()),
                    readFile(stdErrFilename, Charset.defaultCharset()),
                    exitValue);

            if (result.getExitCode() != 0) {
                result.logErrLines();
                LOGGER.error("| Docker command: " + command + " failed to execute");
                throw new ExceptionInInitializerError("Docker command: " + command + " failed to execute");
            }

            return result;
        } finally {
            processRunner.removeFiles();
        }
    }

    static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

}
