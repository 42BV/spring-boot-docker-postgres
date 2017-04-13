package nl._42.boot.docker.postgres.containers;

import nl._42.boot.docker.postgres.shared.DockerEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DockerContainer implements DockerEntity {

    private String containerId;
    private String image;
    private String command;
    private String created;
    private String status;
    private String ports;
    private String names;

    private final Map<Integer, Integer> actualToIntendedMapping;

    public DockerContainer(Map<Integer, Integer> actualToIntendedMapping) {
        this.actualToIntendedMapping = actualToIntendedMapping;
    }

    public void setField(Integer actualColumn, String value) {
        Integer intendedColumn = actualToIntendedMapping.get(actualColumn);
        // If not mapped, ignore the value, has not been flagged as crucial
        if (intendedColumn == null) {
            return;
        }
        // Store the value in the intended field
        switch(intendedColumn) {
            case 0 :
                containerId = value;
                break;
            case 1 :
                image = value;
                break;
            case 2 :
                command = value;
                break;
            case 3 :
                created = value;
                break;
            case 4 :
                status = value;
                break;
            case 5 :
                ports = value;
                break;
            case 6 :
                names = value;
                break;
        }
    }

    public String getContainerId() {
        return containerId;
    }

    public String getImage() {
        return image;
    }

    public String getCommand() {
        return command;
    }

    public String getCreated() {
        return created;
    }

    public String getStatus() {
        return status;
    }

    public String getPorts() {
        return ports;
    }

    public String getNames() {
        return names;
    }

    private List<Integer> getExposedPorts() {
        if (ports == null) {
            return Collections.emptyList();
        }
        List<Integer> exposedPorts = new ArrayList<>();

        Pattern pattern = Pattern.compile("([0-9]*)->[0-9]*");
        Matcher matcher = pattern.matcher(ports);
        while (matcher.find()) {
            exposedPorts.add(Integer.parseInt(matcher.group(1)));
        }
        return exposedPorts;
    }

    public void repairIfNameInPort() {
        this.names = ports;
        this.ports = null;
    }

    private boolean statusIsUp() {
        return status != null && !status.startsWith("Exited");
    }

    public boolean portActivelyOccupied(Integer port) {
        return getExposedPorts().contains(port) && statusIsUp();
    }

}
