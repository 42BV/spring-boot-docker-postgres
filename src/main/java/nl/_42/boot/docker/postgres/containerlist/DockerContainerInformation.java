package nl._42.boot.docker.postgres.containerlist;

import java.util.ArrayList;
import java.util.List;

public class DockerContainerInformation {

    private List<DockerContainer> containers = new ArrayList<>();

    public DockerContainerInformation(String[] lines) throws DockerContainerHeadersMismatch {
        processLines(lines);
    }

    private void processLines(String[] lines) throws DockerContainerHeadersMismatch {
        boolean first = true;
        DockerContainerListHeaders headers = null;
        for (String line : lines) {

            if (first) { // Ignore header line
                headers = new DockerContainerListHeaders(line.split("\\s{2,}"));
                headers.verify();
                first = false;
                continue;
            }
            if (line != null && line.length() > 0) {
                String[] fields = line.split("\\s{2,}");
                DockerContainer containerInfo = new DockerContainer(headers.getActualToIntendedMapping());
                int columnPos = 0;
                for (String field : fields) {
                    containerInfo.setField(columnPos++, field);
                }
                containers.add(containerInfo);
            }
        }
    }

    public List<DockerContainer> getContainers() {
        return containers;
    }

}
