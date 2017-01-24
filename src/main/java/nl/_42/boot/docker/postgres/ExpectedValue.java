package nl._42.boot.docker.postgres;

public class ExpectedValue {
    private final int column;
    private final String expectedValue;

    public ExpectedValue(int column, String expectedValue) {
        this.column = column;
        this.expectedValue = expectedValue;
    }

    public boolean match(String[] fields) {
        if (fields.length <= column) {
            return false;
        }
        return expectedValue.equals(fields[column]);
    }

}
