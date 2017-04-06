package nl._42.boot.docker.postgres.containers;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DockerContainerTest {

    @Test
    public void portInUse() {
        DockerContainer container = setStatusAndPort(
                "Up 33 hours",
                "0.0.0.0:1234->9876/tcp, 0.0.0.0:4321->7890/tcp");
        assertTrue("Port 4321 is in use and the container is up, should have reported port in use",
                container.portActivelyOccupied(4321));
    }

    @Test
    public void portNotInUse() {
        DockerContainer container = setStatusAndPort(
                "Up 33 hours",
                "0.0.0.0:1234->9876/tcp, 0.0.0.0:4321->7890/tcp");
        assertFalse("Port 4321 is in use and the container is up, should have reported port in use", container.portActivelyOccupied(8765));
    }

    @Test
    public void portInUseButContainerDown() {
        DockerContainer container = setStatusAndPort(
                "Exited (0) 3 days ago",
                "0.0.0.0:1234->9876/tcp, 0.0.0.0:4321->7890/tcp");
        assertFalse("Port 4321 is not in use, because the container has exited", container.portActivelyOccupied(4321));
    }

    private DockerContainer setStatusAndPort(String status, String ports) {
        Map<Integer, Integer> mappings = new HashMap<>();
        DockerContainer container = new DockerContainer(mappings);
        setField(container, mappings, 4, status);
        setField(container, mappings, 5, ports);
        return container;
    }

    private void setField(DockerContainer container, Map<Integer, Integer> mappings, int field, String value) {
        mappings.put(field, field);
        container.setField(field, value);
    }

}
