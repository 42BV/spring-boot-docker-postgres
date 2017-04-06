package nl._42.boot.docker.postgres.images;

import nl._42.boot.docker.postgres.shared.DockerHeaderMismatch;
import nl._42.boot.docker.postgres.shared.DockerInformation;
import nl._42.boot.docker.postgres.shared.DockerListHeaders;

import java.util.Map;

public class DockerImageInformation extends DockerInformation<DockerImage> {

    public DockerImageInformation(String[] lines) throws DockerHeaderMismatch {
        super(lines);
    }

    @Override
    protected DockerListHeaders checkHeaders(String[] headers) {
        return new DockerImageListHeaders(headers);
    }

    @Override
    protected DockerImage createLine(Map<Integer, Integer> actualToIntendedMapping) {
        return new DockerImage(actualToIntendedMapping);
    }
}
