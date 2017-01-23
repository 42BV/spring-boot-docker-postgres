package nl._42.boot.docker.utils;

import nl._42.boot.docker.postgres.DockerPostgresProperties;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class DockerFiniteProcessRunner {

    private final ProcessRunner processRunner;

    private final String stdOutFilename;
    private final String stdErrFilename;

    public DockerFiniteProcessRunner(  String command,
                                       DockerPostgresProperties properties) {
        super();

        this.stdOutFilename = properties.getStdOutFilename();
        this.stdErrFilename = properties.getStdErrFilename();

        processRunner = new ProcessRunner(command, properties);
    }

    public DockerOutputResult execute() throws IOException {
        int exitValue = processRunner.execute();

        return new DockerOutputResult(
                readFile(stdOutFilename, Charset.defaultCharset()),
                readFile(stdErrFilename, Charset.defaultCharset()),
                exitValue);
    }

    static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

}
