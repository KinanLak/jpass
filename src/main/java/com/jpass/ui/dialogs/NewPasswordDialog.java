package com.jpass.ui.dialogs;

import com.jpass.model.PasswordEntry;
import javax.swing.*;
import java.awt.*;

public class NewPasswordDialog extends JDialog {
    private final JTextField titleField;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JTextField urlField;
    private PasswordEntry result;

    public NewPasswordDialog(Frame owner) {
        super(owner, "New Password Entry", true);
        setBackground(new Color(28, 28, 30));

        // Create main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(28, 28, 30));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Fields
        titleField = createField("Title");
        usernameField = createField("Username");
        passwordField = createPasswordField();
        urlField = createField("Website URL");

        // Add fields to panel
        mainPanel.add(createFieldGroup("Title", titleField));
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(createFieldGroup("Username", usernameField));
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(createFieldGroup("Password", passwordField));
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(createFieldGroup("Website", urlField));
        mainPanel.add(Box.createVerticalStrut(20));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(28, 28, 30));

        JButton cancelButton = new JButton("Cancel");
        styleButton(cancelButton, false);
        cancelButton.addActionListener(e -> dispose());

        JButton saveButton = new JButton("Save");
        styleButton(saveButton, true);
        saveButton.addActionListener(e -> save());

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        mainPanel.add(buttonPanel);

        // Add to dialog
        add(mainPanel);
        pack();
        setMinimumSize(new Dimension(400, getHeight()));
        setLocationRelativeTo(owner);
    }

    private JTextField createField(String placeholder) {
        JTextField field = new JTextField(20);
        field.putClientProperty("JTextField.placeholderText", placeholder);
        styleField(field);
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField(20);
        field.putClientProperty("JTextField.placeholderText", "Password");
        styleField(field);
        return field;
    }

    private void styleField(JComponent field) {
        field.setBackground(new Color(44, 44, 46));
        field.setForeground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(61, 61, 61)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
    }

    private JPanel createFieldGroup(String label, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBackground(new Color(28, 28, 30));

        JLabel titleLabel = new JLabel(label);
        titleLabel.setForeground(new Color(153, 153, 153));
        titleLabel.setFont(titleLabel.getFont().deriveFont(12f));
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        
        return panel;
    }

    private void styleButton(JButton button, boolean isPrimary) {
        button.setBackground(isPrimary ? new Color(10, 132, 255) : new Color(61, 61, 61));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setFocusPainted(false);
    }

    private void save() {
        if (titleField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Title is required",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        result = new PasswordEntry(
            titleField.getText().trim(),
            usernameField.getText().trim(),
            new String(passwordField.getPassword()),
            urlField.getText().trim()
        );
        
        dispose();
    }

    public PasswordEntry getResult() {
        return result;
    }
}
