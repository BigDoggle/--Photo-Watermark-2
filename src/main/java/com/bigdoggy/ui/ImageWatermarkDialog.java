package com.bigdoggy.ui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImageWatermarkDialog extends JDialog {
    private boolean confirmed = false;
    private File watermarkFile = null;
    private BufferedImage watermarkImage = null;
    
    // 图片水印设置
    private JLabel imagePreviewLabel;
    private JSpinner scaleSpinner;
    private JSlider opacitySlider;
    private JLabel opacityLabel;
    
    // 默认值
    private double scale = 100.0; // 百分比
    private int watermarkOpacity = 100; // 0-100%

    public ImageWatermarkDialog(Frame parent) {
        super(parent, "图片水印设置", true);
        initializeComponents();
        layoutComponents();
        setupEventHandlers();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private void initializeComponents() {
        // 图片预览标签
        imagePreviewLabel = new JLabel("无图片", SwingConstants.CENTER);
        imagePreviewLabel.setPreferredSize(new Dimension(200, 150));
        imagePreviewLabel.setBorder(BorderFactory.createEtchedBorder());
        
        // 缩放比例选择
        scaleSpinner = new JSpinner(new SpinnerNumberModel(scale, 10, 500, 5));
        
        // 透明度滑块
        opacitySlider = new JSlider(0, 100, watermarkOpacity);
        opacitySlider.setMajorTickSpacing(20);
        opacitySlider.setMinorTickSpacing(5);
        opacitySlider.setPaintTicks(true);
        opacitySlider.setPaintLabels(true);
        
        opacityLabel = new JLabel("透明度: " + watermarkOpacity + "%");
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        // 主设置面板
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createTitledBorder("图片水印设置"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // 图片选择和预览
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        mainPanel.add(new JLabel("水印图片:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        mainPanel.add(imagePreviewLabel, gbc);
        
        // 选择图片按钮
        gbc.gridx = 3; gbc.gridwidth = 1;
        JButton selectImageButton = new JButton("选择图片");
        mainPanel.add(selectImageButton, gbc);
        
        // 缩放设置
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        mainPanel.add(new JLabel("缩放比例:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 1;
        mainPanel.add(scaleSpinner, gbc);
        gbc.gridx = 2; gbc.gridwidth = 2;
        mainPanel.add(new JLabel("%"), gbc);
        
        // 透明度设置
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        mainPanel.add(opacityLabel, gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        mainPanel.add(opacitySlider, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton okButton = new JButton("确定");
        JButton cancelButton = new JButton("取消");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // 按钮事件处理
        selectImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectWatermarkImage();
            }
        });
        
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (watermarkImage != null) {
                    confirmed = true;
                    scale = (Double) scaleSpinner.getValue();
                    watermarkOpacity = opacitySlider.getValue();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(ImageWatermarkDialog.this, 
                        "请先选择一张水印图片", "提示", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmed = false;
                dispose();
            }
        });
        
        // 透明度滑块事件处理
        opacitySlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                watermarkOpacity = opacitySlider.getValue();
                opacityLabel.setText("透明度: " + watermarkOpacity + "%");
            }
        });
    }

    private void setupEventHandlers() {
        // 可以在这里添加其他事件处理
    }
    
    private void selectWatermarkImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "图片文件 (PNG, JPEG, BMP, TIFF)", "png", "jpg", "jpeg", "bmp", "tiff", "tif"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                watermarkFile = fileChooser.getSelectedFile();
                watermarkImage = ImageIO.read(watermarkFile);
                
                // 显示预览
                displayImagePreview();
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "加载图片时发生错误：" + e.getMessage(), 
                    "错误", 
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
    
    private void displayImagePreview() {
        if (watermarkImage != null) {
            // 计算缩略图尺寸，保持宽高比
            int originalWidth = watermarkImage.getWidth();
            int originalHeight = watermarkImage.getHeight();
            int maxWidth = 180;
            int maxHeight = 130;
            
            double scaleWidth = (double) maxWidth / originalWidth;
            double scaleHeight = (double) maxHeight / originalHeight;
            double scale = Math.min(scaleWidth, scaleHeight);
            
            int scaledWidth = (int) (originalWidth * scale);
            int scaledHeight = (int) (originalHeight * scale);
            
            // 创建缩略图
            Image scaledImage = watermarkImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            imagePreviewLabel.setIcon(new ImageIcon(scaledImage));
            imagePreviewLabel.setText("");
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public BufferedImage getWatermarkImage() {
        return watermarkImage;
    }

    public double getScale() {
        return scale;
    }

    public int getWatermarkOpacity() {
        return watermarkOpacity;
    }
}