package com.jpass.ui.components;

import com.jpass.model.PasswordEntry;
import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MainContent extends JPanel {
    private JLabel titleLabel;
    private JPanel fieldsPanel;
    private JLabel lastEditedLabel;

    public MainContent() {
        setLayout(new BorderLayout());
        setBackground(new Color(28, 28, 30));
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        // Initialize components
        initializeHeader();
        initializeFields();
        initializeFooter();
    }

    private void initializeHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(28, 28, 30));
        
        titleLabel = new JLabel("");
        titleLabel.setFont(titleLabel.getFont().deriveFont(24f));
        titleLabel.setForeground(Color.WHITE);
        
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttons.setBackground(new Color(28, 28, 30));
        
        buttons.add(createButton("Share", false));
        buttons.add(createButton("Edit", true));
        
        header.add(titleLabel, BorderLayout.WEST);
        header.add(buttons, BorderLayout.EAST);
        
        add(header, BorderLayout.NORTH);
    }

    private void initializeFields() {
        fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBackground(new Color(28, 28, 30));
        add(fieldsPanel, BorderLayout.CENTER);
    }

    private void initializeFooter() {
        lastEditedLabel = new JLabel("");
        lastEditedLabel.setForeground(new Color(102, 102, 102));
        lastEditedLabel.setBorder(BorderFactory.createEmptyBorder(24, 0, 0, 0));
        add(lastEditedLabel, BorderLayout.SOUTH);
    }

    public void displayEntry(PasswordEntry entry) {
        if (entry == null) {
            clearDisplay();
            return;
        }

        // Update title
        titleLabel.setText(entry.getTitle());

        // Update fields
        fieldsPanel.removeAll();
        addField(fieldsPanel, "username", entry.getUsername());
        addField(fieldsPanel, "password", "••••••••••");
        addField(fieldsPanel, "website", entry.getUrl());

        // Update last edited
        String lastEdited = entry.getModificationDates().isEmpty() ? 
            formatDateTime(entry.getCreationDate()) :
            formatDateTime(entry.getModificationDates().get(entry.getModificationDates().size() - 1));
        lastEditedLabel.setText("Last edited " + lastEdited);

        // Refresh UI
        revalidate();
        repaint();
    }

    private void clearDisplay() {
        titleLabel.setText("");
        fieldsPanel.removeAll();
        lastEditedLabel.setText("");
        revalidate();
        repaint();
    }

    private String formatDateTime(java.time.LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern(
            "EEEE, MMMM d, yyyy 'at' h:mm:ss a", Locale.ENGLISH
        ));
    }

    private JButton createButton(String text, boolean isPrimary) {
        JButton button = new JButton(text);
        if (isPrimary) {
            button.setBackground(new Color(10, 132, 255));
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(new Color(61, 61, 61));
            button.setForeground(Color.WHITE);
        }
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setFocusPainted(false);
        return button;
    }

    private void addField(JPanel panel, String label, String value) {
        JPanel fieldGroup = new JPanel(new BorderLayout(0, 4));
        fieldGroup.setBackground(new Color(28, 28, 30));
        fieldGroup.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));

        // Label
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setForeground(new Color(153, 153, 153));
        fieldLabel.setFont(fieldLabel.getFont().deriveFont(12f));
        fieldGroup.add(fieldLabel, BorderLayout.NORTH);

        // Value container
        JPanel valueContainer = new JPanel(new BorderLayout());
        valueContainer.setBackground(new Color(44, 44, 46));
        valueContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(61, 61, 61)),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        // Value
        JLabel fieldValue = new JLabel(value);
        fieldValue.setForeground(Color.WHITE);
        if (label.equals("password")) {
            fieldValue.setFont(new Font(fieldValue.getFont().getFontName(), Font.PLAIN, 14));
        }
        valueContainer.add(fieldValue, BorderLayout.CENTER);

        // Copy button
        JButton copyButton = new JButton("Copy");
        copyButton.setBackground(new Color(61, 61, 61));
        copyButton.setForeground(Color.WHITE);
        copyButton.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        copyButton.setFocusPainted(false);
        valueContainer.add(copyButton, BorderLayout.EAST);

        fieldGroup.add(valueContainer, BorderLayout.CENTER);
        panel.add(fieldGroup);
        panel.add(Box.createVerticalStrut(4));
    }
}
