package nl._42.boot.docker.postgres.containers;

import nl._42.boot.docker.postgres.shared.DockerHeaderMismatch;
import nl._42.boot.docker.postgres.shared.DockerInformation;
import nl._42.boot.docker.postgres.shared.DockerListHeaders;

import java.util.Map;

public class DockerContainerInformation extends DockerInformation<DockerContainer> {

    public DockerContainerInformation(String[] lines) throws DockerHeaderMismatch {
        super(lines);
    }

    @Override
    protected DockerListHeaders checkHeaders(String[] headers) {
        return new DockerContainerListHeaders(headers);
    }

    @Override
    protected DockerContainer createLine(Map<Integer, Integer> actualToIntendedMapping) {
        return new DockerContainer(actualToIntendedMapping);
    }

}
