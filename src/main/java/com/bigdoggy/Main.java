package com.bigdoggy;

import com.bigdoggy.ui.MainWindow;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // 设置系统外观
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                // 增加字体大小以提高可读性
                Font font = new Font("微软雅黑", Font.PLAIN, 12);
                UIManager.put("Button.font", font);
                UIManager.put("Label.font", font);
                UIManager.put("ComboBox.font", font);
                UIManager.put("TextField.font", font);
                UIManager.put("TextArea.font", font);
                UIManager.put("Slider.font", font);
                UIManager.put("RadioButton.font", font);
                UIManager.put("CheckBox.font", font);
                UIManager.put("Table.font", font);
                UIManager.put("MenuItem.font", font);
            } catch (Exception e) {
                e.printStackTrace();
            }
            MainWindow mainWindow = new MainWindow();
            mainWindow.createAndShow();
        });
    }
}