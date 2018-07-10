package com.example.sharingapp;

/**
 * Dimensions class
 */
@SuppressWarnings("WeakerAccess")
public class Dimensions {

    private String length;
    private String width;
    private String height;

    public Dimensions(String length, String width, String height) {
        this.length = length;
        this.width = width;
        this.height = height;
    }

    public String getLength() {
        return length;
    }

    public String getWidth() {
        return width;
    }

    public String getHeight() {
        return height;
    }
}
