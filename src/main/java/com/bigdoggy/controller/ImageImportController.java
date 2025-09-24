package com.bigdoggy.controller;

import com.bigdoggy.model.ImageItem;
import com.bigdoggy.ui.ExportSettingsDialog;
import com.bigdoggy.ui.MainWindow;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;

public class ImageImportController {
    private MainWindow mainWindow;

    public ImageImportController(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void importSingleImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);//不可多选
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "图片文件 (JPEG, PNG, BMP, TIFF)", "jpg", "jpeg", "png", "bmp", "tiff", "tif"));

        if (fileChooser.showOpenDialog(mainWindow.getFrame()) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            addImageToList(file);
        }
    }

    public void importMultipleImages() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "图片文件 (JPEG, PNG, BMP, TIFF)", "jpg", "jpeg", "png", "bmp", "tiff", "tif"));

        if (fileChooser.showOpenDialog(mainWindow.getFrame()) == JFileChooser.APPROVE_OPTION) {
            File[] files = fileChooser.getSelectedFiles();
            for (File file : files) {
                addImageToList(file);
            }
        }
    }

    public void importFolder() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);

        if (fileChooser.showOpenDialog(mainWindow.getFrame()) == JFileChooser.APPROVE_OPTION) {
            File folder = fileChooser.getSelectedFile();
            File[] files = folder.listFiles((dir, name) ->
                    name.toLowerCase().matches(".*\\.(jpg|jpeg|png|bmp|tiff|tif)$"));

            if (files != null) {
                for (File file : files) {
                    addImageToList(file);
                }
            }
        }
    }

    public void exportImages() {
        if (mainWindow.getImageListModel().getSize() == 0) {
            JOptionPane.showMessageDialog(mainWindow.getFrame(), "没有图片需要导出", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // 选择输出文件夹
        JFileChooser folderChooser = new JFileChooser();
        folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        folderChooser.setDialogTitle("选择导出文件夹");

        if (folderChooser.showOpenDialog(mainWindow.getFrame()) == JFileChooser.APPROVE_OPTION) {
            File outputFolder = folderChooser.getSelectedFile();
            
            // 检查是否与原文件夹相同
            boolean sameFolder = false;
            for (int i = 0; i < mainWindow.getImageListModel().getSize(); i++) {
                ImageItem item = mainWindow.getImageListModel().getElementAt(i);
                if (item.getFile().getParentFile().equals(outputFolder)) {
                    sameFolder = true;
                    break;
                }
            }
            
            if (sameFolder) {
                int result = JOptionPane.showConfirmDialog(
                    mainWindow.getFrame(),
                    "导出文件夹与原文件夹相同，可能会覆盖原文件。是否继续？",
                    "警告",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                
                if (result != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            // 显示导出设置对话框
            ExportSettingsDialog dialog = new ExportSettingsDialog(mainWindow.getFrame(), mainWindow.getOutputFormat());
            dialog.setVisible(true);
            
            if (dialog.isConfirmed()) {
                String namingRule = dialog.getNamingRule();
                String prefix = dialog.getPrefix();
                String suffix = dialog.getSuffix();
                int quality = dialog.getQuality();
                
                // 执行导出
                exportImagesToFolder(outputFolder, namingRule, prefix, suffix, quality);
            }
        }
    }

    private void exportImagesToFolder(File outputFolder, String namingRule, String prefix, String suffix, int quality) {
        try {
            int successCount = 0;
            for (int i = 0; i < mainWindow.getImageListModel().getSize(); i++) {
                ImageItem item = mainWindow.getImageListModel().getElementAt(i);
                File originalFile = item.getFile();
                
                // 根据命名规则生成新文件名
                String newName = generateNewFileName(originalFile.getName(), namingRule, prefix, suffix);
                File outputFile = new File(outputFolder, newName);
                
                // 读取原图
                BufferedImage image = ImageIO.read(originalFile);
                
                // 导出图片
                if (mainWindow.getOutputFormat().equals("JPEG")) {
                    // 对于JPEG格式，确保没有透明度
                    BufferedImage jpegImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
                    jpegImage.createGraphics().drawImage(image, 0, 0, null);
                    ImageIO.write(jpegImage, "jpeg", outputFile);
                } else {
                    // PNG格式支持透明度
                    ImageIO.write(image, "png", outputFile);
                }
                successCount++;
            }
            
            JOptionPane.showMessageDialog(mainWindow.getFrame(), 
                "导出完成！成功导出 " + successCount + " 张图片。", 
                "导出完成", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainWindow.getFrame(), 
                "导出过程中发生错误：" + e.getMessage(), 
                "导出失败", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private String generateNewFileName(String originalName, String namingRule, String prefix, String suffix) {
        // 移除文件扩展名
        int lastDotIndex = originalName.lastIndexOf('.');
        String nameWithoutExtension = lastDotIndex > 0 ? originalName.substring(0, lastDotIndex) : originalName;
        String extension = mainWindow.getOutputFormat().toLowerCase();
        
        switch (namingRule) {
            case "prefix":
                return prefix + originalName;
            case "suffix":
                return nameWithoutExtension + suffix + "." + extension;
            case "original":
            default:
                // 保留原文件名，但可能需要更改扩展名
                return nameWithoutExtension + "." + extension;
        }
    }

    public void addDragAndDropSupport(JComponent component) {
        component.setDropTarget(new DropTarget(component, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    Transferable transferable = dtde.getTransferable();

                    if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);

                        for (File file : files) {

                            if (file.isFile() && isImageFile(file)) {
                                addImageToList(file);
                            } else if (file.isDirectory()) {
                                importFolderImages(file);
                            }
                        }
                    }
                    dtde.dropComplete(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    dtde.dropComplete(false);
                }
            }
        }));
    }

    private void addImageToList(File file) {
        ImageIcon icon = new ImageIcon(file.getAbsolutePath());
        ImageItem imageItem = new ImageItem(file.getName(), icon, file);
        mainWindow.getImageListModel().addElement(imageItem);
    }

    private boolean isImageFile(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".jpg") || name.endsWith(".jpeg") ||
                name.endsWith(".png") || name.endsWith(".bmp") ||
                name.endsWith(".tiff") || name.endsWith(".tif");
    }

    private void importFolderImages(File folder) {
        File[] files = folder.listFiles((dir, name) ->
                name.toLowerCase().matches(".*\\.(jpg|jpeg|png|bmp|tiff|tif)$"));

        if (files != null) {
            for (File file : files) {
                addImageToList(file);
            }
        }
    }
    
    // 获取支持的图片格式列表（用于显示）
    public static String[] getSupportedFormats() {
        return new String[]{"JPEG", "PNG", "BMP", "TIFF"};
    }
    
    // 获取支持的文件扩展名
    public static String[] getSupportedExtensions() {
        return new String[]{"jpg", "jpeg", "png", "bmp", "tiff", "tif"};
    }
}