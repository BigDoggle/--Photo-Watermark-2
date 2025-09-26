package com.bigdoggy.ui;

import com.bigdoggy.model.ImageItem;
import com.bigdoggy.ui.WatermarkPreviewPanel.WatermarkPosition;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.Point;
import javax.imageio.ImageIO;

public class ImageWatermarkDialog extends JDialog {
    private boolean confirmed = false;
    private File watermarkFile = null;
    private BufferedImage watermarkImage = null;
    private ImageItem previewImageItem;
    
    // 图片水印设置
    private JLabel imagePreviewLabel;
    private JButton selectImageButton;
    private JSpinner scaleSpinner;
    private JSlider opacitySlider;
    private JLabel opacityLabel;
    
    // 高级设置控件
    private JSlider scaleSlider;
    private JSlider rotationSlider;
    private JComboBox<String> positionComboBox;
    private WatermarkPreviewPanel previewPanel;
    
    // 默认值
    private double scale = 100.0; // 百分比
    private int watermarkOpacity = 100; // 0-100%
    
    // 高级设置默认值
    private double rotation = 0.0; // 旋转角度
    private WatermarkPosition position = WatermarkPosition.BOTTOM_RIGHT;

    public ImageWatermarkDialog(Frame parent, ImageItem previewImage) {
        super(parent, "图片水印设置", true);
        this.previewImageItem = previewImage;
        initializeComponents();
        layoutComponents();
        setupEventHandlers();
        
        // 设置预览图片
        if (previewImageItem != null) {
            previewPanel.setImageItem(previewImageItem);
        }
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setResizable(true);
        setLocationRelativeTo(parent);
    }

    private void initializeComponents() {
        // 图片预览标签
        imagePreviewLabel = new JLabel("无图片", SwingConstants.CENTER);
        imagePreviewLabel.setPreferredSize(new Dimension(200, 150));
        imagePreviewLabel.setBorder(BorderFactory.createEtchedBorder());
        
        // 选择图片按钮
        selectImageButton = new JButton("选择图片");
        
        // 缩放比例选择（用于图片水印本身）
        scaleSpinner = new JSpinner(new SpinnerNumberModel(scale, 10, 500, 5));
        
        // 透明度滑块
        opacitySlider = new JSlider(0, 100, watermarkOpacity);
        opacitySlider.setMajorTickSpacing(20);
        opacitySlider.setMinorTickSpacing(5);
        opacitySlider.setPaintTicks(true);
        opacitySlider.setPaintLabels(true);
        
        opacityLabel = new JLabel("透明度: " + watermarkOpacity + "%");
        opacityLabel.setPreferredSize(new Dimension(100, 20));
        
        // 高级设置组件
        previewPanel = new WatermarkPreviewPanel();
        previewPanel.setPreferredSize(new Dimension(400, 300));
        previewPanel.setBorder(BorderFactory.createTitledBorder("预览"));
        
        // 缩放滑块（用于预览中的缩放）
        scaleSlider = new JSlider(10, 500, (int)scale);
        scaleSlider.setMajorTickSpacing(50);  // 减少标签密度，每50个单位一个主刻度
        scaleSlider.setMinorTickSpacing(10);
        scaleSlider.setPaintTicks(true);
        scaleSlider.setPaintLabels(true);
        scaleSlider.setPreferredSize(new Dimension(250, 50)); // 增加尺寸以提供更多空间
        
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
        mainPanel.add(selectImageButton, gbc);
        
        // 缩放设置（用于图片水印本身）
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
        
        // 高级设置 - 缩放（用于预览中的缩放）
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        mainPanel.add(new JLabel("预览缩放:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        mainPanel.add(scaleSlider, gbc);
        
        // 高级设置 - 旋转
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1;
        mainPanel.add(new JLabel("旋转:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        mainPanel.add(rotationSlider, gbc);
        
        // 高级设置 - 位置
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1;
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
                    
                    // 高级设置值
                    rotation = rotationSlider.getValue();
                    position = WatermarkPosition.values()[positionComboBox.getSelectedIndex()];
                    
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
                opacityLabel.setText(String.format("透明度: %3d%%", watermarkOpacity));
                updatePreview();
            }
        });
    }

    private void setupEventHandlers() {
        // 缩放比例变化事件（用于图片水印本身）
        scaleSpinner.addChangeListener(e -> updatePreview());
        
        // 预览缩放滑块事件处理
        scaleSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // 确保即使在调整过程中也更新预览
                updatePreview();
            }
        });
        
        // 旋转滑块事件处理
        rotationSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // 确保即使在调整过程中也更新预览
                rotation = rotationSlider.getValue();
                updatePreview();
            }
        });
        
        // 位置选择事件处理
        positionComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                position = WatermarkPosition.values()[positionComboBox.getSelectedIndex()];
                updatePreview();
            }
        });
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
                updatePreview();
                
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
    
    // 更新预览
    private void updatePreview() {
        if (watermarkImage != null) {
            double scaleValue = (Double) scaleSpinner.getValue();
            double previewScale = scaleSlider.getValue();
            previewPanel.setWatermarkImage(watermarkImage);
            previewPanel.setOpacity(watermarkOpacity);
            previewPanel.setScale(previewScale / 100.0);
            previewPanel.setRotation(rotation);
            previewPanel.setPresetPosition(position);
            previewPanel.setUsePresetPosition(true); // 使用预设位置
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