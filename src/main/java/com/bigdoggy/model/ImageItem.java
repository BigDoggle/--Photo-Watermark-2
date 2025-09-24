package com.bigdoggy.model;

import javax.swing.*;
import java.io.File;

public class ImageItem {
    private String name;
    private ImageIcon icon;
    private File file;

    public ImageItem(String name, ImageIcon icon, File file) {
        this.name = name;
        this.icon = icon;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public File getFile() {
        return file;
    }

    @Override
    public String toString() {
        return name;
    }
}