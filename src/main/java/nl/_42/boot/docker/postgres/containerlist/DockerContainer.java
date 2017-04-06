package nl._42.boot.docker.postgres.containerlist;

import java.util.Map;

public class DockerContainer {

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

}
