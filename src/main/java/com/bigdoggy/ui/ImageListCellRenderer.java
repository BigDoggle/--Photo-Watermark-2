package com.bigdoggy.ui;

import com.bigdoggy.model.ImageItem;

import javax.swing.*;
import java.awt.*;

public class ImageListCellRenderer extends JPanel implements ListCellRenderer<ImageItem> {
    private JLabel imageLabel;
    private JLabel nameLabel;

    public ImageListCellRenderer() {
        setLayout(new BorderLayout());
        imageLabel = new JLabel();
        nameLabel = new JLabel();
        nameLabel.setHorizontalAlignment(JLabel.CENTER);//居中

        add(imageLabel, BorderLayout.CENTER);
        add(nameLabel, BorderLayout.SOUTH);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends ImageItem> list,
                                                  ImageItem value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {

        if (value != null) {
            nameLabel.setText(value.getName());

            // 调整图片大小作为缩略图
            ImageIcon originalIcon = value.getIcon();
            if (originalIcon != null) {
                Image img = originalIcon.getImage();
                Image scaledImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImg));
            } else {
                imageLabel.setIcon(null);
            }
        }

        // 设置选中状态的背景色
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            nameLabel.setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            nameLabel.setForeground(list.getForeground());
        }

        return this;
    }
}