package nl._42.boot.docker.postgres.containerlist;

public class DockerContainerHeadersMismatch extends Exception {

    private static final String MISMATCH = "The headers from the Docker Container listing do not match the expected headers";

    public DockerContainerHeadersMismatch() {
        super(MISMATCH);
    }

}
