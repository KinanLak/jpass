package com.jpass.ui.dialogs;

import javax.swing.*;
import java.awt.*;
import com.jpass.service.SecurityService;
import com.jpass.service.VaultManager;

public class CreateVaultDialog extends JDialog {
    private final JTextField vaultNameField;
    private final JPasswordField passwordField;
    private final JPasswordField confirmPasswordField;
    private String vaultName;
    private String masterPassword;

    public CreateVaultDialog(Frame owner) {
        super(owner, "Create New Vault", true);
        setBackground(new Color(28, 28, 30));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(28, 28, 30));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome to JPass!");
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(24f));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(welcomeLabel);
        mainPanel.add(Box.createVerticalStrut(10));

        JLabel subtitleLabel = new JLabel("Let's setup your vault");
        subtitleLabel.setForeground(new Color(153, 153, 153));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(subtitleLabel);
        mainPanel.add(Box.createVerticalStrut(30));

        // Vault name field
        vaultNameField = createField("Vault Name");
        mainPanel.add(createFieldGroup("Name your vault", vaultNameField));
        mainPanel.add(Box.createVerticalStrut(20));

        // Password fields
        passwordField = createPasswordField("Master Password");
        mainPanel.add(createFieldGroup("Create master password", passwordField));
        mainPanel.add(Box.createVerticalStrut(10));

        confirmPasswordField = createPasswordField("Confirm Password");
        mainPanel.add(createFieldGroup("Confirm master password", confirmPasswordField));
        mainPanel.add(Box.createVerticalStrut(30));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(28, 28, 30));

        JButton createButton = new JButton("Create Vault");
        styleButton(createButton, true);
        createButton.addActionListener(e -> createVault());

        buttonPanel.add(createButton);
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

    private JPasswordField createPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(20);
        field.putClientProperty("JTextField.placeholderText", placeholder);
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
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(28, 28, 30));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JLabel titleLabel = new JLabel(label);
        titleLabel.setForeground(new Color(153, 153, 153));
        titleLabel.setFont(titleLabel.getFont().deriveFont(12f));
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(8));

        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.add(field);

        return panel;
    }

    private void styleButton(JButton button, boolean isPrimary) {
        button.setBackground(isPrimary ? new Color(10, 132, 255) : new Color(61, 61, 61));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void createVault() {
        vaultName = vaultNameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (vaultName.isEmpty()) {
            showError("Please enter a vault name");
            return;
        }

        // Vérifier si le vault existe déjà
        if (new VaultManager(new SecurityService()).listVaults().contains(vaultName)) {
            showError("A vault with this name already exists");
            return;
        }

        if (password.isEmpty()) {
            showError("Please enter a master password");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            return;
        }

        masterPassword = password;
        dispose();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public String getVaultName() {
        return vaultName;
    }

    public String getMasterPassword() {
        return masterPassword;
    }
}
