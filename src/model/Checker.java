package model;

public class Checker {
    private final char name;
    private boolean isKing;

    public Checker(char name) {
        this.name = name;
        this.isKing = false;
    }

    public char getName() { return this.name; }

    public boolean isKing() { return this.isKing; }

    public void setKing() { this.isKing = true; }
}
