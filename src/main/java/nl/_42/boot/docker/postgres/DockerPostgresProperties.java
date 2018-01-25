package nl._42.boot.docker.postgres;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "docker.postgres", ignoreUnknownFields = false)
public class DockerPostgresProperties {

    private static final String DEFAULT_PORT                        = "5432";
    private static final String POSTGRES_BASE_FOLDER_APPLICATION    = "/app";
    private static final String POSTGRES_BASE_FOLDER_DATA           = "/var/lib/postgresql/data";
    private static final String IN_MEMORY_TEMPLATE                  = "[IN_MEMORY_TEMPLATE]";
    private static final String VAR_IN_MEMORY_MOUNT_DESTINATION     = "inMemoryMountDestination";

    private boolean enabled = true;

    private String stdOutFilename = "docker-std-out.log";

    private String stdErrFilename = "docker-std-err.log";

    private String password = "postgres";

    private Integer port = null;

    private String imageName = "postgres";

    private String imageVersion = "latest";

    private String containerName = "postgression";

    private String startupVerificationText = "database system is ready to accept connections";

    private Integer timesExpectedVerificationText = 2;

    private String dockerCommand = "docker run --rm --tty -e POSTGRES_PASSWORD=${password} -p ${port}:5432 --name ${containerName} " + IN_MEMORY_TEMPLATE + " ${imageName}:${imageVersion}";

    private String useDockerCommand;

    private Integer timeout = 300000; // 5 minutes because of time required for downloading?

    private boolean forceClean = true;

    private boolean forceCleanAfterwards = true;

    private boolean stopPortOccupyingContainer = true;

    private Integer afterVerificationWait = 0;

    private String containerOccupyingPort = null;

    private boolean inMemory = true;

    private String inMemoryMountDestinationCommand =
            "--mount type=tmpfs,destination=${" + VAR_IN_MEMORY_MOUNT_DESTINATION + "_@N}";

    private List<String> inMemoryMountDestinations = new ArrayList<>();

    private Map<String, String> customVariables = new HashMap<>();

    Map<String,String> properties = null;

    public DockerPostgresProperties() {
        inMemoryMountDestinations.add(POSTGRES_BASE_FOLDER_APPLICATION);
        inMemoryMountDestinations.add(POSTGRES_BASE_FOLDER_DATA);
    }

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

    public boolean isForceCleanAfterwards() {
        return forceCleanAfterwards;
    }

    public void setForceCleanAfterwards(boolean forceCleanAfterwards) {
        this.forceCleanAfterwards = forceCleanAfterwards;
    }

    public boolean isInMemory() {
        return inMemory;
    }

    public void setInMemory(boolean inMemory) {
        this.inMemory = inMemory;
    }

    public List<String> getInMemoryMountDestinations() {
        return inMemoryMountDestinations;
    }

    public void setInMemoryMountDestinations(List<String> inMemoryMountDestinations) {
        this.inMemoryMountDestinations = inMemoryMountDestinations;
    }

    public String getInMemoryMountDestinationCommand() {
        return inMemoryMountDestinationCommand;
    }

    public void setInMemoryMountDestinationCommand(String inMemoryMountDestinationCommand) {
        this.inMemoryMountDestinationCommand = inMemoryMountDestinationCommand;
    }

    public String getUseDockerCommand() {
        if (useDockerCommand == null) {
            getProperties();
        }
        return useDockerCommand;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void init(String datasourceUrl) {
        initPort(datasourceUrl);
        properties = new HashMap<>();
        this.useDockerCommand = replaceInMemoryTemplate(properties, getDockerCommand());
        initProperties();
    }

    private void initPort(String datasourceUrl) {
        if (getPort() != null) {
            return;
        }
        // Scrape the port from the JDBC URL
        setPort(determinePort(datasourceUrl != null ? datasourceUrl : DEFAULT_PORT));
    }

    private void initProperties() {
        properties.put("stdOutFilename", getStdOutFilename());
        properties.put("stdErrFilename", getStdErrFilename());
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
        properties.put("useDockerCommand", getUseDockerCommand());
        properties.put("forceClean", Boolean.toString(isForceClean()));
        properties.put("forceCleanAfterwards", Boolean.toString(isForceCleanAfterwards()));
        properties.put("stopPortOccupyingContainer", Boolean.toString(isStopPortOccupyingContainer()));
        properties.put("afterVerificationWait", Boolean.toString(isForceClean()));
        properties.put("containerOccupyingPort", getContainerOccupyingPort());
        properties.put("inMemory", Boolean.toString(isInMemory()));
        properties.putAll(getCustomVariables());
    }

    private String replaceInMemoryTemplate(Map<String, String> properties, String dockerCommand) {
        int templatePosition = dockerCommand.indexOf(IN_MEMORY_TEMPLATE);
        if (templatePosition == -1) {
            return dockerCommand;
        }
        List<String> inMemoryCommands = new ArrayList<>();
        if (inMemory) {
            Integer destinationNumber = 1;
            for (String inMemoryMountDestination : getInMemoryMountDestinations()) {
                inMemoryCommands.add(inMemoryMountDestinationCommand.replace(
                        "@N",
                        destinationNumber.toString()));
                properties.put(
                        VAR_IN_MEMORY_MOUNT_DESTINATION + "_" + destinationNumber,
                        inMemoryMountDestination);
                destinationNumber++;
            }
        }
        return
                dockerCommand.substring(0, templatePosition) +
                StringUtils.join(inMemoryCommands, ' ') +
                dockerCommand.substring(templatePosition + IN_MEMORY_TEMPLATE.length());
    }

    private Integer determinePort(String url) {
        if (url == null || url.length() == 0) {
            throw new ExceptionInInitializerError("spring.datasource.url is empty. No port could be derived.");
        }
        int lastColonPos = url.lastIndexOf(':');
        int slashAfterPortPos = url.indexOf('/', lastColonPos);
        if (lastColonPos == -1 || slashAfterPortPos == -1 || slashAfterPortPos < lastColonPos + 2) {
            throw new ExceptionInInitializerError("spring.datasource.url does not have port information: [" + url + "]. No port could be derived.");
        }
        return Integer.parseInt(url.substring(lastColonPos + 1, slashAfterPortPos));
    }

}
