package nl._42.boot.docker.postgres.shared;

public class DockerHeaderMismatch extends Exception {

    private static final String MISMATCH = "The headers from the Docker Container listing do not match the expected headers";

    public DockerHeaderMismatch() {
        super(MISMATCH);
    }

}
