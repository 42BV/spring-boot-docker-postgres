package nl._42.boot.docker.postgres.containers;

import nl._42.boot.docker.postgres.shared.DockerHeaderMismatch;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DockerContainerInformationTest {

    @Test
    public void properHeaders() throws DockerHeaderMismatch {
        final String[] headers = {
                "CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                    NAMES",
                "c4f0dca9b424        postgres:9.6        \"docker-entrypoint...\"   24 hours ago        Up 24 hours         0.0.0.0:5434->5432/tcp   postgression-unit-test"
        };
        DockerContainerInformation containerInformation = new DockerContainerInformation(headers);
        assertEquals("postgression-unit-test", containerInformation.getList().get(0).getNames());
    }

    @Test(expected = DockerHeaderMismatch.class)
    public void missingNamesHeader() throws DockerHeaderMismatch {
        final String[] headers = {
                "CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                    XXX",
                "c4f0dca9b424        postgres:9.6        \"docker-entrypoint...\"   24 hours ago        Up 24 hours         0.0.0.0:5434->5432/tcp   postgression-unit-test"
        };
        DockerContainerInformation containerInformation = new DockerContainerInformation(headers);
    }

    @Test
    public void mixupOfFields() throws DockerHeaderMismatch {
        final String[] headers = {
                "IMAGE               COMMAND                  CREATED             STATUS              PORTS                    NAMES             CONTAINER ID",
                "postgres:9.6        \"docker-entrypoint...\"   24 hours ago        Up 24 hours         0.0.0.0:5434->5432/tcp   postgression-unit-test          c4f0dca9b424"
        };
        DockerContainerInformation containerInformation = new DockerContainerInformation(headers);
        assertEquals("postgression-unit-test", containerInformation.getList().get(0).getNames());
        assertEquals("c4f0dca9b424", containerInformation.getList().get(0).getContainerId());
    }

    @Test
    public void LessFieldsButCrucialsAreAllThere() throws DockerHeaderMismatch {
        final String[] headers = {
                "STATUS              PORTS                    NAMES",
                "Up 24 hours         0.0.0.0:5434->5432/tcp   postgression-unit-test"
        };
        DockerContainerInformation containerInformation = new DockerContainerInformation(headers);
        assertEquals("Up 24 hours", containerInformation.getList().get(0).getStatus());
        assertEquals("0.0.0.0:5434->5432/tcp", containerInformation.getList().get(0).getPorts());
        assertEquals("postgression-unit-test", containerInformation.getList().get(0).getNames());
    }

    @Test
    public void containerExists() throws DockerHeaderMismatch {
        final String[] headers = {
                "CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                    NAMES",
                "c4f0dca9b424        postgres:9.6        \"docker-entrypoint...\"   24 hours ago        Up 24 hours         0.0.0.0:5434->5432/tcp   some-container",
                "c4f0dca9b424        postgres:9.6        \"docker-entrypoint...\"   24 hours ago        Up 24 hours         0.0.0.0:5434->5432/tcp   some-other-container",
                "c4f0dca9b424        postgres:9.6        \"docker-entrypoint...\"   24 hours ago        Up 24 hours         0.0.0.0:5434->5432/tcp   postgression-unit-test"
        };
        DockerContainerInformation containerInformation = new DockerContainerInformation(headers);
        assertTrue("Container 'postgression-unit-test' was expected, not found", containerInformation.hasContainer("postgression-unit-test"));
    }


}
