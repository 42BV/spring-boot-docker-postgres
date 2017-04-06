package nl._42.boot.docker.postgres.images;

import nl._42.boot.docker.postgres.shared.DockerListHeaders;
import nl._42.boot.docker.postgres.shared.HeaderImportance;

public class DockerImageListHeaders extends DockerListHeaders {

    public static final HeaderImportance[] HEADERS = {
            new HeaderImportance("REPOSITORY", true),
            new HeaderImportance("TAG", true),
            new HeaderImportance("IMAGE ID", false),
            new HeaderImportance("CREATED", false),
            new HeaderImportance("SIZE", false),
    };

    public DockerImageListHeaders(String[] actualHeaders) {
        super(actualHeaders);
    }

    @Override
    protected HeaderImportance[] getExpectedHeaders() {
        return HEADERS;
    }

}
