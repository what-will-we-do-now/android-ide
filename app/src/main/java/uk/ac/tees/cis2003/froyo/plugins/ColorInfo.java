package uk.ac.tees.cis2003.froyo.plugins;

public class ColorInfo {

    private final int offset;
    private final int length;
    private final String color;
    /**
     * Things with lower priority will overtake anything with higher priority
     */
    private final int priority;

    public ColorInfo(int offset, int length, String color) {
        this.offset = offset;
        this.length = length;
        this.color = color;
        this.priority = 0;
    }

    public ColorInfo(int offset, int length, String color, int priority) {
        this.offset = offset;
        this.length = length;
        this.color = color;
        this.priority = priority;
    }

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }

    public String getColor() {
        return color;
    }

    public int getPriority() {
        return priority;
    }
}