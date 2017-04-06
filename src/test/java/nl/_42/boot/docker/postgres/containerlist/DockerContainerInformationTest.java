package nl._42.boot.docker.postgres.containerlist;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DockerContainerInformationTest {

    @Test
    public void properHeaders() throws DockerContainerHeadersMismatch {
        final String[] headers = {
                "CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                    NAMES",
                "c4f0dca9b424        postgres:9.6        \"docker-entrypoint...\"   24 hours ago        Up 24 hours         0.0.0.0:5434->5432/tcp   postgression-unit-test"
        };
        DockerContainerInformation containerInformation = new DockerContainerInformation(headers);
        assertEquals("postgression-unit-test", containerInformation.getContainers().get(0).getNames());
    }

    @Test(expected = DockerContainerHeadersMismatch.class)
    public void missingNamesHeader() throws DockerContainerHeadersMismatch {
        final String[] headers = {
                "CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                    XXX",
                "c4f0dca9b424        postgres:9.6        \"docker-entrypoint...\"   24 hours ago        Up 24 hours         0.0.0.0:5434->5432/tcp   postgression-unit-test"
        };
        DockerContainerInformation containerInformation = new DockerContainerInformation(headers);
    }

    @Test
    public void mixupOfFields() throws DockerContainerHeadersMismatch {
        final String[] headers = {
                "IMAGE               COMMAND                  CREATED             STATUS              PORTS                    NAMES             CONTAINER ID",
                "postgres:9.6        \"docker-entrypoint...\"   24 hours ago        Up 24 hours         0.0.0.0:5434->5432/tcp   postgression-unit-test          c4f0dca9b424"
        };
        DockerContainerInformation containerInformation = new DockerContainerInformation(headers);
        assertEquals("postgression-unit-test", containerInformation.getContainers().get(0).getNames());
        assertEquals("c4f0dca9b424", containerInformation.getContainers().get(0).getContainerId());
    }

    @Test
    public void LessFieldsButCrucialsAreAllThere() throws DockerContainerHeadersMismatch {
        final String[] headers = {
                "STATUS              PORTS                    NAMES",
                "Up 24 hours         0.0.0.0:5434->5432/tcp   postgression-unit-test"
        };
        DockerContainerInformation containerInformation = new DockerContainerInformation(headers);
        assertEquals("Up 24 hours", containerInformation.getContainers().get(0).getStatus());
        assertEquals("0.0.0.0:5434->5432/tcp", containerInformation.getContainers().get(0).getPorts());
        assertEquals("postgression-unit-test", containerInformation.getContainers().get(0).getNames());
    }

}
