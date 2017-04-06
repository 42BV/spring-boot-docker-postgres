package nl._42.boot.docker.postgres.shared;

public class HeaderImportance {

    private boolean crucial;
    private String name;

    public HeaderImportance(String name, boolean crucial) {
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
