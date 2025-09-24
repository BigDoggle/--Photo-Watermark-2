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

        toolbar.add(importSingleButton);
        toolbar.add(importMultipleButton);
        toolbar.add(importFolderButton);

        return toolbar;
    }

    public JFrame getFrame() {
        return frame;
    }

    public DefaultListModel<ImageItem> getImageListModel() {
        return imageListModel;
    }
}