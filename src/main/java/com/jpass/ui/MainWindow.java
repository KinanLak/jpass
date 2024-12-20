package com.jpass.ui;

import com.jpass.ui.components.Sidebar;
import com.jpass.ui.components.PasswordListPanel;
import com.jpass.ui.components.MainContent;
import com.jpass.service.EntryService;
import com.jpass.model.PasswordEntry;

import javax.swing.*;
import java.awt.*;

/**
 * Fenêtre principale de l'application.
 */
public class MainWindow extends JFrame {
    private final EntryService entryService;
    private PasswordListPanel passwordListPanel;
    private MainContent mainContent;

    /**
     * Constructeur initialisant l'interface principale.
     */
    public MainWindow(EntryService entryService) {
        this.entryService = entryService;
        initComponents();
        setupWindow();
    }

    private void initComponents() {
        // Create main container
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(new Color(28, 28, 30));

        // Initialize components with proper service injection
        Sidebar sidebar = new Sidebar(entryService.listVaults(), entryService.getCurrentVault());
        passwordListPanel = new PasswordListPanel(entryService);
        mainContent = new MainContent();

        // Add selection listener
        passwordListPanel.addPropertyChangeListener("selectedEntry", evt -> {
            PasswordEntry selectedEntry = (PasswordEntry) evt.getNewValue();
            mainContent.displayEntry(selectedEntry);
        });

        mainContainer.add(sidebar, BorderLayout.WEST);
        mainContainer.add(passwordListPanel, BorderLayout.CENTER);
        mainContainer.add(mainContent, BorderLayout.EAST);

        setContentPane(mainContainer);
    }

    private void setupWindow() {
        setTitle("JPass - Password Manager");
        setSize(1280, 800);
        setMinimumSize(new Dimension(1024, 600));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(new Color(28, 28, 30));

        // Add window listener for proper shutdown
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                entryService.lock();
                dispose();
                System.exit(0);
            }
        });
    }
}