package uk.ac.tees.v8036651.mode.plugins;

public class ColorInfo {
    private int offset;
    private int length;
    private String color;

    public ColorInfo(int offset, int length, String color) {
        this.offset = offset;
        this.length = length;
        this.color = color;
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
}