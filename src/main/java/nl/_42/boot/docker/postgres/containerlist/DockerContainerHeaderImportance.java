package nl._42.boot.docker.postgres.containerlist;

public class DockerContainerHeaderImportance {

    private boolean crucial;
    private String name;

    DockerContainerHeaderImportance(String name, boolean crucial) {
        this.name = name;
        this.crucial = crucial;
    }

    public boolean isCrucial() {
        return crucial;
    }

    public String getName() {
        return name;
    }

}
