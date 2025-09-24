package com.bigdoggy.ui;

import com.bigdoggy.model.ImageItem;
import com.bigdoggy.controller.ImageImportController;

import javax.swing.*;
import java.awt.*;

public class MainWindow {
    private JFrame frame;
    private DefaultListModel<ImageItem> imageListModel;
    private JList<ImageItem> imageList;
    private ImageImportController imageImportController;
    private String outputFormat = "JPEG"; // 默认输出格式

    public MainWindow() {
        imageImportController = new ImageImportController(this);
    }

    public void createAndShow() {
        frame = new JFrame("图片水印工具");
        frame.setLayout(new BorderLayout());

        // 创建顶部工具栏
        JPanel toolbarPanel = createToolbar();
        frame.add(toolbarPanel, BorderLayout.NORTH);

        // 创建图片显示区域
        imageListModel = new DefaultListModel<>();
        imageList = new JList<>(imageListModel);
        imageList.setCellRenderer(new ImageListCellRenderer());
        imageList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        imageList.setVisibleRowCount(-1);

        JScrollPane scrollPane = new JScrollPane(imageList);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        // 添加拖拽支持
        imageImportController.addDragAndDropSupport(scrollPane);

        frame.add(scrollPane, BorderLayout.CENTER);

        // 添加输出格式选择面板
        JPanel bottomPanel = createOutputFormatPanel();
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setBounds(400, 300, 600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private JPanel createToolbar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton importSingleButton = new JButton("导入单张图片");
        importSingleButton.addActionListener(e -> imageImportController.importSingleImage());

        JButton importMultipleButton = new JButton("批量导入图片");
        importMultipleButton.addActionListener(e -> imageImportController.importMultipleImages());

        JButton importFolderButton = new JButton("导入文件夹");
        importFolderButton.addActionListener(e -> imageImportController.importFolder());

        JButton exportButton = new JButton("导出图片");
        exportButton.addActionListener(e -> imageImportController.exportImages());

        toolbar.add(importSingleButton);
        toolbar.add(importMultipleButton);
        toolbar.add(importFolderButton);
        toolbar.add(exportButton);

        return toolbar;
    }

    private JPanel createOutputFormatPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("输出设置"));

        JLabel formatLabel = new JLabel("输出格式:");
        JRadioButton jpegRadio = new JRadioButton("JPEG", true);
        JRadioButton pngRadio = new JRadioButton("PNG", false);

        ButtonGroup group = new ButtonGroup();
        group.add(jpegRadio);
        group.add(pngRadio);

        jpegRadio.addActionListener(e -> outputFormat = "JPEG");
        pngRadio.addActionListener(e -> outputFormat = "PNG");

        panel.add(formatLabel);
        panel.add(jpegRadio);
        panel.add(pngRadio);

        return panel;
    }

    public JFrame getFrame() {
        return frame;
    }

    public DefaultListModel<ImageItem> getImageListModel() {
        return imageListModel;
    }

    public String getOutputFormat() {
        return outputFormat;
    }
}