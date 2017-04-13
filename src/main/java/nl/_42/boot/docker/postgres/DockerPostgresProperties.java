package nl._42.boot.docker.postgres;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "docker.postgres", ignoreUnknownFields = false)
public class DockerPostgresProperties {

    private boolean enabled = true;

    private String stdOutFilename = "docker-std-out.log";

    private String stdErrFilename = "docker-std-err.log";

    private String dockerLogsStdOutFilename = "docker-logs-std-out.log";

    private String dockerLogsStdErrFilename = "docker-logs-std-err.log";

    private String password = "postgres";

    private Integer port = null;

    private String imageName = "postgres";

    private String imageVersion = "latest";

    private String containerName = "postgression";

    private String startupVerificationText = "database system is ready to accept connections";

    private Integer timesExpectedVerificationText = 2;

    private String dockerCommand = "docker run --rm -e POSTGRES_PASSWORD=${password} -p ${port}:5432 --name ${containerName} ${imageName}:${imageVersion}";

    private Integer timeout = 300000; // 5 minutes because of time required for downloading?

    private boolean forceClean = true;

    private boolean stopPortOccupyingContainer = true;

    private Integer afterVerificationWait = 0;

    private String containerOccupyingPort = null;

    private Map<String, String> customVariables = new HashMap<>();

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

    public String getDockerCommand() {
        return dockerCommand;
    }

    public void setDockerCommand(String dockerCommand) {
        this.dockerCommand = dockerCommand;
    }

    public boolean isForceClean() {
        return forceClean;
    }

    public void setForceClean(boolean forceClean) {
        this.forceClean = forceClean;
    }

    public Map<String, String> getCustomVariables() {
        return customVariables;
    }

    public void setCustomVariables(Map<String, String> customVariables) {
        this.customVariables = customVariables;
    }

    public Integer getAfterVerificationWait() {
        return afterVerificationWait;
    }

    public void setAfterVerificationWait(Integer afterVerificationWait) {
        this.afterVerificationWait = afterVerificationWait;
    }

    public Integer getTimesExpectedVerificationText() {
        return timesExpectedVerificationText;
    }

    public void setTimesExpectedVerificationText(Integer timesExpectedVerificationText) {
        this.timesExpectedVerificationText = timesExpectedVerificationText;
    }

    public boolean isStopPortOccupyingContainer() {
        return stopPortOccupyingContainer;
    }

    public void setStopPortOccupyingContainer(boolean stopPortOccupyingContainer) {
        this.stopPortOccupyingContainer = stopPortOccupyingContainer;
    }

    public String getContainerOccupyingPort() {
        return containerOccupyingPort;
    }

    public void setContainerOccupyingPort(String containerOccupyingPort) {
        this.containerOccupyingPort = containerOccupyingPort;
    }

    public String getDockerLogsStdOutFilename() {
        return dockerLogsStdOutFilename;
    }

    public void setDockerLogsStdOutFilename(String dockerLogsStdOutFilename) {
        this.dockerLogsStdOutFilename = dockerLogsStdOutFilename;
    }

    public String getDockerLogsStdErrFilename() {
        return dockerLogsStdErrFilename;
    }

    public void setDockerLogsStdErrFilename(String dockerLogsStdErrFilename) {
        this.dockerLogsStdErrFilename = dockerLogsStdErrFilename;
    }

    public Map<String, String> getProperties() {
        Map<String,String> properties = new HashMap<>();
        properties.put("stdOutFilename", getStdOutFilename());
        properties.put("stdErrFilename", getStdErrFilename());
        properties.put("dockerLogsStdOutFilename", getDockerLogsStdOutFilename());
        properties.put("dockerLogsStdErrFilename", getDockerLogsStdErrFilename());
        properties.put("timeout", getTimeout().toString());
        properties.put("password", getPassword());
        properties.put("port", getPort().toString());
        properties.put("containerName", getContainerName());
        properties.put("imageName", getImageName());
        properties.put("imageVersion", getImageVersion());
        properties.put("startupVerificationText", getStartupVerificationText());
        properties.put("timesExpectedVerificationText", getTimesExpectedVerificationText().toString());
        properties.put("afterVerificationWait", getAfterVerificationWait().toString());
        properties.put("dockerCommand", getDockerCommand());
        properties.put("forceClean", Boolean.toString(isForceClean()));
        properties.put("stopPortOccupyingContainer", Boolean.toString(isStopPortOccupyingContainer()));
        properties.put("afterVerificationWait", Boolean.toString(isForceClean()));
        properties.put("containerOccupyingPort", getContainerOccupyingPort());
        properties.putAll(getCustomVariables());
        return properties;
    }

}
