package nl._42.boot.docker.postgres.shared;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class DockerInformation<S extends DockerEntity> {

    private List<S> list = new ArrayList<>();

    public DockerInformation(String[] lines) throws DockerHeaderMismatch {
        processLines(lines);
    }

    private void processLines(String[] lines) throws DockerHeaderMismatch {
        boolean first = true;
        DockerListHeaders headers = null;
        for (String line : lines) {

            if (first) { // Ignore header line
                headers = checkHeaders(line.split("\\s{2,}"));
                headers.verify();
                first = false;
                continue;
            }
            if (line != null && line.length() > 0) {
                String[] fields = line.split("\\s{2,}");
                S info = createLine(headers.getActualToIntendedMapping());
                int columnPos = 0;
                for (String field : fields) {
                    info.setField(columnPos++, field);
                }
                list.add(info);
            }
        }
    }

    protected abstract DockerListHeaders checkHeaders(String[] headers);

    protected abstract S createLine(Map<Integer, Integer> actualToIntendedMapping);

    public List<S> getList() {
        return list;
    }

}
