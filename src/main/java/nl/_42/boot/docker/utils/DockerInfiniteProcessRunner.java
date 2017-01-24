package nl._42.boot.docker.utils;

import nl._42.boot.docker.postgres.DockerPostgresProperties;

import java.io.IOException;

public abstract class DockerInfiniteProcessRunner extends Thread {

    protected final DockerInfiniteProcessTailer tailer;

    private final ProcessRunner processRunner;

    public DockerInfiniteProcessRunner(String command,
                                       DockerPostgresProperties properties,
                                       boolean imageDownloaded) {
        super();

        processRunner = new ProcessRunner(command, properties);

        this.tailer = new DockerInfiniteProcessTailer(this, properties, imageDownloaded);
    }

    public boolean verify() throws IOException {
        return tailer.verify();
    }

    public void run() {
        processRunner.execute();
    }

}
