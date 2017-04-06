package nl._42.boot.docker.postgres.containers;

import nl._42.boot.docker.postgres.shared.DockerListHeaders;
import nl._42.boot.docker.postgres.shared.HeaderImportance;

public class DockerContainerListHeaders extends DockerListHeaders {

    public static final HeaderImportance[] HEADERS = {
            new HeaderImportance("CONTAINER ID", false),
            new HeaderImportance("IMAGE", false),
            new HeaderImportance("COMMAND", false),
            new HeaderImportance("CREATED", false),
            new HeaderImportance("STATUS", true),
            new HeaderImportance("PORTS", true),
            new HeaderImportance("NAMES", true)
    };

    public DockerContainerListHeaders(String[] actualHeaders) {
        super(actualHeaders);
    }

    @Override
    protected HeaderImportance[] getExpectedHeaders() {
        return HEADERS;
    }

}
