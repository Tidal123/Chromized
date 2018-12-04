package chromized.event;

public enum Priority {
    HIGH(-1),
    NORMAL(0),
    LOW(1);

    public final int value;

    private Priority(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
