package com.bigdoggy.ui;

import com.bigdoggy.model.ImageItem;
import com.bigdoggy.ui.WatermarkPreviewPanel.WatermarkPosition;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
    
    // 高级设置控件
    private JSlider rotationSlider;
    private JComboBox<String> positionComboBox;
    private WatermarkPreviewPanel previewPanel;
    
    // 默认值
    private String watermarkText = "水印文本";
    private String fontName = "宋体";
    private int fontSize = 80;
    private boolean isBold = false;
    private boolean isItalic = false;
    private Color textColor = Color.BLACK;
    private int watermarkOpacity = 100; // 0-100%
    private boolean hasShadow = false;
    private boolean hasOutline = false;
    
    // 高级设置默认值
    private double rotation = 0.0; // 旋转角度
    private WatermarkPosition position = WatermarkPosition.BOTTOM_RIGHT;

    public TextWatermarkDialog(Frame parent, ImageItem previewImage) {
        super(parent, "文本水印设置", true);
        initializeComponents();
        layoutComponents();
        setupEventHandlers();
        
        // 设置预览图片
        if (previewImage != null) {
            previewPanel.setImageItem(previewImage);
            updatePreview();
        }
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setResizable(true);
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
        fontSizeSpinner = new JSpinner(new SpinnerNumberModel(fontSize, 8, 120, 1));
        
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
        
        // 高级设置组件
        previewPanel = new WatermarkPreviewPanel();
        previewPanel.setPreferredSize(new Dimension(400, 300));
        previewPanel.setBorder(BorderFactory.createTitledBorder("预览"));
        
        // 旋转滑块
        rotationSlider = new JSlider(-180, 180, (int)rotation);
        rotationSlider.setMajorTickSpacing(90);
        rotationSlider.setMinorTickSpacing(15);
        rotationSlider.setPaintTicks(true);
        rotationSlider.setPaintLabels(true);
        rotationSlider.setPreferredSize(new Dimension(250, 50)); // 增加尺寸以提供更多空间
        
        // 位置选择下拉框
        String[] positions = {
            "左上角", "顶部居中", "右上角",
            "左侧居中", "正中央", "右侧居中",
            "左下角", "底部居中", "右下角"
        };
        positionComboBox = new JComboBox<>(positions);
        positionComboBox.setSelectedIndex(8); // 默认右下角
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
        
        // 高级设置 - 旋转
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 1;
        mainPanel.add(new JLabel("旋转:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        mainPanel.add(rotationSlider, gbc);
        
        // 高级设置 - 位置
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 1;
        mainPanel.add(new JLabel("位置:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        mainPanel.add(positionComboBox, gbc);
        
        // 创建包含主设置和预览的中间面板
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(mainPanel, BorderLayout.WEST);
        centerPanel.add(previewPanel, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
        
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
                
                // 高级设置值
                rotation = rotationSlider.getValue();
                position = WatermarkPosition.values()[positionComboBox.getSelectedIndex()];
                
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
                    updatePreview();
                }
            }
        });
        
        // 添加所有控件的事件监听器以更新预览
        textWatermarkField.addActionListener(e -> updatePreview());
        fontComboBox.addActionListener(e -> updatePreview());
        fontSizeSpinner.addChangeListener(e -> updatePreview());
        boldCheckBox.addActionListener(e -> updatePreview());
        italicCheckBox.addActionListener(e -> updatePreview());
        opacitySlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                watermarkOpacity = opacitySlider.getValue();
                updatePreview();
            }
        });
        shadowCheckBox.addActionListener(e -> updatePreview());
        outlineCheckBox.addActionListener(e -> updatePreview());
        
        // 高级设置控件事件监听器
        rotationSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // 确保即使在调整过程中也更新预览
                rotation = rotationSlider.getValue();
                updatePreview();
            }
        });
        
        positionComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                position = WatermarkPosition.values()[positionComboBox.getSelectedIndex()];
                updatePreview();
            }
        });
    }
    
    // 更新预览
    private void updatePreview() {
        // 获取当前文本水印设置
        String text = textWatermarkField.getText();
        String fontName = (String) fontComboBox.getSelectedItem();
        int fontSize = (Integer) fontSizeSpinner.getValue();
        boolean isBold = boldCheckBox.isSelected();
        boolean isItalic = italicCheckBox.isSelected();
        
        // 创建字体
        int fontStyle = Font.PLAIN;
        if (isBold && isItalic) {
            fontStyle = Font.BOLD | Font.ITALIC;
        } else if (isBold) {
            fontStyle = Font.BOLD;
        } else if (isItalic) {
            fontStyle = Font.ITALIC;
        }
        
        Font font = new Font(fontName, fontStyle, fontSize);
        
        // 更新预览面板
        previewPanel.setTextWatermark(text, font, textColor);
        previewPanel.setOpacity(watermarkOpacity);
        previewPanel.setScale(1.0); // 文本水印不支持缩放，使用默认值1.0
        previewPanel.setRotation(rotation);
        previewPanel.setPresetPosition(position);
        previewPanel.setUsePresetPosition(true); // 使用预设位置
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
    
    // 高级设置相关getter方法
    public double getRotation() {
        return rotation;
    }

    public WatermarkPosition getPosition() {
        return position;
    }
    
    // 获取水印位置
    public Point getWatermarkPosition() {
        // 如果使用预设位置，返回null，否则返回自定义位置
        if (previewPanel.isUsePresetPosition()) {
            return null;
        } else {
            return previewPanel.getWatermarkPosition();
        }
    }
}