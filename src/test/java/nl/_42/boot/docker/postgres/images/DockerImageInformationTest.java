package nl._42.boot.docker.postgres.images;

import nl._42.boot.docker.postgres.shared.DockerHeaderMismatch;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DockerImageInformationTest {

    @Test
    public void properHeaders() throws DockerHeaderMismatch {
        final String[] headers = {
                "REPOSITORY      TAG       IMAGE ID            CREATED             SIZE",
                "postgres        9.6       4e18b2c30f8d        5 weeks ago         266 MB"
        };
        DockerImageInformation containerInformation = new DockerImageInformation(headers);
        assertEquals("postgres", containerInformation.getList().get(0).getRepository());
        assertEquals("9.6", containerInformation.getList().get(0).getTag());
    }

    @Test(expected = DockerHeaderMismatch.class)
    public void missingTagHeader() throws DockerHeaderMismatch {
        final String[] headers = {
                "REPOSITORY      XXX      IMAGE ID            CREATED             SIZE",
                "postgres        9.6       4e18b2c30f8d        5 weeks ago         266 MB"
        };
        new DockerImageInformation(headers);
    }

    @Test
    public void mixupOfFields() throws DockerHeaderMismatch {
        final String[] headers = {
                "SIZE       IMAGE ID            REPOSITORY      TAG       CREATED",
                "266 MB     4e18b2c30f8d        postgres        9.6       5 weeks ago"
        };
        DockerImageInformation containerInformation = new DockerImageInformation(headers);
        assertEquals("postgres", containerInformation.getList().get(0).getRepository());
        assertEquals("9.6", containerInformation.getList().get(0).getTag());
    }

    @Test
    public void LessFieldsButCrucialsAreAllThere() throws DockerHeaderMismatch {
        final String[] headers = {
                "REPOSITORY      TAG       IMAGE ID            CREATED             SIZE",
                "postgres        9.6       4e18b2c30f8d        5 weeks ago         266 MB"
        };
        DockerImageInformation containerInformation = new DockerImageInformation(headers);
        assertEquals("postgres", containerInformation.getList().get(0).getRepository());
        assertEquals("9.6", containerInformation.getList().get(0).getTag());
        assertEquals("4e18b2c30f8d", containerInformation.getList().get(0).getImageId());
        assertEquals("5 weeks ago", containerInformation.getList().get(0).getCreated());
        assertEquals("266 MB", containerInformation.getList().get(0).getSize());
    }
    
}
