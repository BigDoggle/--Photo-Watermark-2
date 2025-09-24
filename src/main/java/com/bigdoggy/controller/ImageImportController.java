package com.bigdoggy.controller;

import com.bigdoggy.model.ImageItem;
import com.bigdoggy.ui.MainWindow;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;

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
                "图片文件", "jpg", "jpeg", "png", "gif", "bmp"));

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
                "图片文件", "jpg", "jpeg", "png", "gif", "bmp"));

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
                    name.toLowerCase().matches(".*\\.(jpg|jpeg|png|gif|bmp)$"));

            if (files != null) {
                for (File file : files) {
                    addImageToList(file);
                }
            }
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
                name.endsWith(".png") || name.endsWith(".gif") ||
                name.endsWith(".bmp");
    }

    private void importFolderImages(File folder) {
        File[] files = folder.listFiles((dir, name) ->
                name.toLowerCase().matches(".*\\.(jpg|jpeg|png|gif|bmp)$"));

        if (files != null) {
            for (File file : files) {
                addImageToList(file);
            }
        }
    }
}