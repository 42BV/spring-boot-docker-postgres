package nl._42.boot.docker.postgres;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "docker.postgres", ignoreUnknownFields = false)
public class DockerPostgresProperties {

    private boolean enabled = true;

    private String stdOutFilename = "docker-std-out.log";

    private String stdErrFilename = "docker-std-err.log";

    private String password = "postgres";

    private Integer port = 5432;

    private String imageName = "postgres";

    private String imageVersion = "latest";

    private String containerName = "postgression";

    private String startupVerificationText = "PostgreSQL init process complete; ready for start up.";

    private Integer timeout = 300000; // 5 minutes because of time required for downloading?

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getStdOutFilename() {
        return stdOutFilename;
    }

    public void setStdOutFilename(String stdOutFilename) {
        this.stdOutFilename = stdOutFilename;
    }

    public String getStdErrFilename() {
        return stdErrFilename;
    }

    public void setStdErrFilename(String stdErrFilename) {
        this.stdErrFilename = stdErrFilename;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public String getImageVersion() {
        return imageVersion;
    }

    public void setImageVersion(String imageVersion) {
        this.imageVersion = imageVersion;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public String getStartupVerificationText() {
        return startupVerificationText;
    }

    public void setStartupVerificationText(String startupVerificationText) {
        this.startupVerificationText = startupVerificationText;
    }

    public Map<String, String> getProperties() {
        Map<String,String> properties = new HashMap<>();
        properties.put("stdOutFilename", getStdOutFilename());
        properties.put("stdErrFilename", getStdErrFilename());
        properties.put("timeout", getTimeout().toString());
        properties.put("password", getPassword());
        properties.put("port", getPort().toString());
        properties.put("containerName", getContainerName());
        properties.put("imageName", getImageName());
        properties.put("imageVersion", getImageVersion());
        properties.put("startupVerificationText", getStartupVerificationText());
        return properties;
    }

}
