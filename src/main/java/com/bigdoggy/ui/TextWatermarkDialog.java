package com.bigdoggy.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TextWatermarkDialog extends JDialog {
    private boolean confirmed = false;
    
    // 文本水印设置
    private JTextField textWatermarkField;
    private JComboBox<String> fontComboBox;
    private JSpinner fontSizeSpinner;
    private JCheckBox boldCheckBox;
    private JCheckBox italicCheckBox;
    private JButton colorButton;
    private JSlider opacitySlider;
    private JCheckBox shadowCheckBox;
    private JCheckBox outlineCheckBox;
    
    // 默认值
    private String watermarkText = "水印文本";
    private String fontName = "宋体";
    private int fontSize = 24;
    private boolean isBold = false;
    private boolean isItalic = false;
    private Color textColor = Color.BLACK;
    private int watermarkOpacity = 100; // 0-100%
    private boolean hasShadow = false;
    private boolean hasOutline = false;

    public TextWatermarkDialog(Frame parent) {
        super(parent, "文本水印设置", true);
        initializeComponents();
        layoutComponents();
        setupEventHandlers();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private void initializeComponents() {
        // 文本水印内容
        textWatermarkField = new JTextField(watermarkText, 15);
        
        // 字体选择
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontNames = ge.getAvailableFontFamilyNames();
        fontComboBox = new JComboBox<>(fontNames);
        fontComboBox.setSelectedItem(fontName);
        
        // 字号选择
        fontSizeSpinner = new JSpinner(new SpinnerNumberModel(fontSize, 8, 72, 1));
        
        // 粗体和斜体
        boldCheckBox = new JCheckBox("粗体", isBold);
        italicCheckBox = new JCheckBox("斜体", isItalic);
        
        // 颜色选择按钮
        colorButton = new JButton("选择颜色");
        colorButton.setBackground(textColor);
        colorButton.setForeground(textColor);
        colorButton.setPreferredSize(new Dimension(100, 25));
        
        // 透明度滑块
        opacitySlider = new JSlider(0, 100, watermarkOpacity);
        opacitySlider.setMajorTickSpacing(20);
        opacitySlider.setMinorTickSpacing(5);
        opacitySlider.setPaintTicks(true);
        opacitySlider.setPaintLabels(true);
        
        // 阴影和描边效果
        shadowCheckBox = new JCheckBox("阴影效果", hasShadow);
        outlineCheckBox = new JCheckBox("描边效果", hasOutline);
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        // 主设置面板
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createTitledBorder("文本水印设置"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // 文本内容
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("水印文本:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        mainPanel.add(textWatermarkField, gbc);
        
        // 字体设置
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        mainPanel.add(new JLabel("字体:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 1;
        mainPanel.add(fontComboBox, gbc);
        
        gbc.gridx = 2;
        mainPanel.add(new JLabel("字号:"), gbc);
        gbc.gridx = 3;
        mainPanel.add(fontSizeSpinner, gbc);
        
        // 样式设置
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("样式:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(boldCheckBox, gbc);
        gbc.gridx = 2; gbc.gridwidth = 2;
        mainPanel.add(italicCheckBox, gbc);
        
        // 颜色设置
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        mainPanel.add(new JLabel("颜色:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        mainPanel.add(colorButton, gbc);
        
        // 透明度设置
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1;
        mainPanel.add(new JLabel("透明度:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        mainPanel.add(opacitySlider, gbc);
        
        // 效果设置
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1;
        mainPanel.add(new JLabel("效果:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(shadowCheckBox, gbc);
        gbc.gridx = 2; gbc.gridwidth = 2;
        mainPanel.add(outlineCheckBox, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton okButton = new JButton("确定");
        JButton cancelButton = new JButton("取消");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // 按钮事件处理
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmed = true;
                watermarkText = textWatermarkField.getText();
                fontName = (String) fontComboBox.getSelectedItem();
                fontSize = (Integer) fontSizeSpinner.getValue();
                isBold = boldCheckBox.isSelected();
                isItalic = italicCheckBox.isSelected();
                watermarkOpacity = opacitySlider.getValue();
                hasShadow = shadowCheckBox.isSelected();
                hasOutline = outlineCheckBox.isSelected();
                dispose();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmed = false;
                dispose();
            }
        });
    }

    private void setupEventHandlers() {
        colorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(
                    TextWatermarkDialog.this,
                    "选择水印颜色",
                    textColor
                );
                if (newColor != null) {
                    textColor = newColor;
                    colorButton.setBackground(textColor);
                    colorButton.setForeground(textColor);
                }
            }
        });
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getWatermarkText() {
        return watermarkText;
    }

    public String getFontName() {
        return fontName;
    }

    public int getFontSize() {
        return fontSize;
    }

    public boolean isBold() {
        return isBold;
    }

    public boolean isItalic() {
        return isItalic;
    }

    public Color getTextColor() {
        return textColor;
    }

    public int getWatermarkOpacity() {
        return watermarkOpacity;
    }

    public boolean hasShadow() {
        return hasShadow;
    }

    public boolean hasOutline() {
        return hasOutline;
    }
}