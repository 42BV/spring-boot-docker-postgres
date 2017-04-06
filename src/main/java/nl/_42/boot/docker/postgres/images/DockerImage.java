package nl._42.boot.docker.postgres.images;

import nl._42.boot.docker.postgres.shared.DockerEntity;

import java.util.Map;

public class DockerImage implements DockerEntity {

    private String repository;
    private String tag;
    private String imageId;
    private String created;
    private String size;

    private final Map<Integer, Integer> actualToIntendedMapping;

    public DockerImage(Map<Integer, Integer> actualToIntendedMapping) {
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
                repository = value;
                break;
            case 1 :
                tag = value;
                break;
            case 2 :
                imageId = value;
                break;
            case 3 :
                created = value;
                break;
            case 4 :
                size = value;
                break;
        }
    }

    public String getRepository() {
        return repository;
    }

    public String getTag() {
        return tag;
    }

    public String getImageId() {
        return imageId;
    }

    public String getCreated() {
        return created;
    }

    public String getSize() {
        return size;
    }
}
