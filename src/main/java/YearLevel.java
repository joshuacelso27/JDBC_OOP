public enum YearLevel {
    FIRST_YEAR(1, "1st Year"),
    SECOND_YEAR(2, "2nd Year"),
    THIRD_YEAR(3, "3rd Year"),
    FOURTH_YEAR(4, "4th Year");

    private final int value;
    private final String displayText;

    YearLevel(int value, String displayText) {
        this.value = value;
        this.displayText = displayText;
    }

    public int getValue() {
        return value;
    }

    public static YearLevel fromValue(int value) {
        for (YearLevel yearLevel : values()) {
            if (yearLevel.getValue() == value) {
                return yearLevel;
            }
        }
        return null;
    }

    public static YearLevel fromDisplayText(String displayText) {
        for (YearLevel yearLevel : values()) {
            if (yearLevel.toString().equals(displayText)) {
                return yearLevel;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return displayText;
    }
}
