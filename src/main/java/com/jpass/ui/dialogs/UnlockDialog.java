package com.jpass.ui.dialogs;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.util.List;

public class UnlockDialog extends JDialog {
    private final JPasswordField passwordField;
    private final JComboBox<String> vaultComboBox;
    private String masterPassword;
    private boolean createNewVaultRequested = false;

    public UnlockDialog(Frame owner, List<String> vaults) {
        super(owner, "Unlock Vault", true);
        setBackground(new Color(28, 28, 30));

        // Create main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(28, 28, 30));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add lock icon
        JLabel lockIcon = new JLabel("🔒");
        lockIcon.setFont(lockIcon.getFont().deriveFont(48f));
        lockIcon.setForeground(Color.WHITE);
        lockIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lockIcon);
        mainPanel.add(Box.createVerticalStrut(20));

        // Add title
        JLabel titleLabel = new JLabel("Enter master password to unlock");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Add vault selection combo box
        vaultComboBox = new JComboBox<>(vaults.toArray(new String[0]));
        vaultComboBox.setMaximumSize(new Dimension(300, 40));
        vaultComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        vaultComboBox.setBackground(new Color(44, 44, 46));
        styleField(vaultComboBox);

        // Set preferred row count to show more items
        vaultComboBox.setMaximumRowCount(6);

        mainPanel.add(vaultComboBox);
        mainPanel.add(Box.createVerticalStrut(20));

        // Add "Create New Vault" link
        JButton createNewLink = new JButton("Create New Vault");
        createNewLink.setBorderPainted(false);
        createNewLink.setContentAreaFilled(false);
        createNewLink.setForeground(new Color(10, 132, 255));
        createNewLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createNewLink.setAlignmentX(Component.CENTER_ALIGNMENT);
        createNewLink.addActionListener(e -> {
            createNewVaultRequested = true;
            dispose();
        });

        mainPanel.add(createNewLink);
        mainPanel.add(Box.createVerticalStrut(20));

        // Password field
        passwordField = new JPasswordField(20);
        styleField(passwordField);
        passwordField.setMaximumSize(new Dimension(300, 40));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add enter key listener
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    unlock();
                }
            }
        });

        mainPanel.add(passwordField);
        mainPanel.add(Box.createVerticalStrut(20));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(28, 28, 30));

        JButton cancelButton = new JButton("Cancel");
        styleButton(cancelButton, false);
        cancelButton.addActionListener(e -> {
            masterPassword = null;
            dispose();
        });

        JButton unlockButton = new JButton("Unlock");
        styleButton(unlockButton, true);
        unlockButton.addActionListener(e -> unlock());

        buttonPanel.add(cancelButton);
        buttonPanel.add(unlockButton);
        mainPanel.add(buttonPanel);

        // Set default button
        getRootPane().setDefaultButton(unlockButton);

        // Add to dialog
        add(mainPanel);
        pack();
        setMinimumSize(new Dimension(400, getHeight()));
        setLocationRelativeTo(owner);

        // Focus password field on show
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                passwordField.requestFocusInWindow();
            }
        });
    }

    public String getSelectedVault() {
        return (String) vaultComboBox.getSelectedItem();
    }

    public boolean isCreateNewVaultRequested() {
        return createNewVaultRequested;
    }

    private void styleField(JComponent field) {
        field.setBackground(new Color(44, 44, 46));
        field.setForeground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(61, 61, 61)),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        if (field instanceof JComboBox) {
            JComboBox<?> combo = (JComboBox<?>) field;
            // Augmenter la hauteur minimum du combobox
            combo.setPreferredSize(new Dimension(combo.getPreferredSize().width, 40));
            combo.setMinimumSize(new Dimension(combo.getMinimumSize().width, 40));

            combo.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value,
                        int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    // Style pour les items dans la liste déroulée
                    if (index != -1) {
                        setBackground(isSelected ? new Color(10, 132, 255) : new Color(44, 44, 46));
                        setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                    } else {
                        // Style spécial pour l'item sélectionné affiché
                        setBackground(new Color(44, 44, 46));
                        setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                    }
                    setForeground(Color.WHITE);
                    return this;
                }
            });

            // Personnaliser l'apparence du bouton de la combobox

            combo.setUI(new BasicComboBoxUI() {
                protected JButton arrowButton;

                @Override
                protected JButton createArrowButton() {
                    arrowButton = new JButton("↓");
                    arrowButton.setBackground(new Color(44, 44, 46));
                    arrowButton.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                    arrowButton.setFont(arrowButton.getFont().deriveFont(18f));
                    arrowButton.setForeground(Color.WHITE);
                    return arrowButton;
                }

                @Override
                public void installUI(JComponent c) {
                    super.installUI(c);
                    JComboBox<?> combo = (JComboBox<?>) c;
                    combo.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
                        public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent e) {
                            arrowButton.setText("↑");
                        }

                        public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent e) {
                            arrowButton.setText("↓");
                        }

                        public void popupMenuCanceled(javax.swing.event.PopupMenuEvent e) {
                            arrowButton.setText("↓");
                        }
                    });
                }
            });
        }
    }

    private void styleButton(JButton button, boolean isPrimary) {
        button.setBackground(isPrimary ? new Color(10, 132, 255) : new Color(61, 61, 61));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void unlock() {
        char[] password = passwordField.getPassword();
        if (password.length == 0) {
            JOptionPane.showMessageDialog(this,
                    "Please enter your master password",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        masterPassword = new String(password);
        dispose();
    }

    public String getPassword() {
        return masterPassword;
    }
}
