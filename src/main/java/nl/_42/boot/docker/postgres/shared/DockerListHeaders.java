package nl._42.boot.docker.postgres.shared;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public abstract class DockerListHeaders {

    private final Logger LOGGER = LoggerFactory.getLogger(DockerListHeaders.class);

    private final String[] actualHeaders;

    private Map<Integer, Integer> actualToIntendedMapping = new HashMap<>();

    public DockerListHeaders(String[] actualHeaders) {
        this.actualHeaders = actualHeaders;
    }

    public void verify() throws DockerHeaderMismatch {
        if (!verify(getExpectedHeaders(), actualHeaders)) {
            logFatal(getExpectedHeaders(), actualHeaders);
            throw new DockerHeaderMismatch();
        }
    }

    public Map<Integer, Integer> getActualToIntendedMapping() {
        return actualToIntendedMapping;
    }

    private boolean verify(HeaderImportance[] expectedHeaders, String[] actualHeaders)
            throws DockerHeaderMismatch {
        boolean valid = true;

        Integer expectedColumn = 0;
        for (HeaderImportance expectedHeader : expectedHeaders) {
            Integer actualColumn = getActualColumn(expectedHeader.getName(), actualHeaders);
            if (actualColumn == null && expectedHeader.isCrucial()) {
                valid = false;
            }
            if (actualColumn != null) {
                actualToIntendedMapping.put(actualColumn, expectedColumn);
            }
            if (!expectedColumn.equals(actualColumn)) {
                LOGGER.debug("| " + expectedColumn + ": Header [" + expectedHeader.getName() + "] found in column " + actualColumn);
            }
            expectedColumn++;
        }

        return valid;
    }

    private Integer getActualColumn(String expectedHeader, String[] actualHeaders) {
        Integer actualColumn = 0;
        for (String actualHeader : actualHeaders) {
            if (expectedHeader.equals(actualHeader)) {
                return actualColumn;
            }
            actualColumn++;
        }
        return null;
    }

    private void logFatal(HeaderImportance[] expectedHeaders, String[] actualHeaders) throws DockerHeaderMismatch {
        LOGGER.error("| The headers from the Docker Container listing do not match the expected headers");

        // Verify if the number of headers match
        if (expectedHeaders.length != actualHeaders.length) {
            LOGGER.error("| The number of columns from the container list is " +
                    actualHeaders.length + ", expected " + expectedHeaders.length);
        }

        // Check the names of the headers with the expected positions
        int numberOfHeaders = expectedHeaders.length > actualHeaders.length ?
                expectedHeaders.length : actualHeaders.length;
        for (Integer columnPos = 0; columnPos < numberOfHeaders; columnPos++) {
            HeaderImportance expectedHeader = columnPos >= expectedHeaders.length ? null : expectedHeaders[columnPos];
            String actualHeader = columnPos >= actualHeaders.length ? null : actualHeaders[columnPos];
            if (    expectedHeader != null &&
                    actualToIntendedMapping.get(columnPos) == null &&
                    expectedHeader.isCrucial()) {
                LOGGER.error(constructHeaderAssertionResult(columnPos, expectedHeader, actualHeader));
            } else {
                LOGGER.info(constructHeaderAssertionResult(columnPos, expectedHeader, actualHeader));
            }
        }
    }

    private String constructHeaderAssertionResult(Integer columnPos,
                                                  HeaderImportance expectedHeader,
                                                  String actualHeader) {
        return "| " + columnPos + ": expected [" +
                (expectedHeader == null ? null : expectedHeader.getName()) + "], actual [" +
                actualHeader + "]" +
                constructFoundAt(columnPos, (expectedHeader == null ? null : expectedHeader.getName()));
    }

    private String constructFoundAt(Integer columnPos, String expectedHeader) {
        if (expectedHeader == null) {
            return "";
        }

        for (Integer actualPos : actualToIntendedMapping.keySet()) {
            Integer expectedPos = actualToIntendedMapping.get(actualPos);
            if (columnPos.equals(expectedPos)) {
                return ", found at column " + actualPos;
            }
        }
        return "";
    }

    protected abstract HeaderImportance[] getExpectedHeaders();
}
