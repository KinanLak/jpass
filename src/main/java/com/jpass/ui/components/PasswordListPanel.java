package com.jpass.ui.components;

import com.jpass.model.Entry;
import com.jpass.model.PasswordEntry;
import com.jpass.service.EntryService;
import com.jpass.ui.dialogs.NewPasswordDialog;
import com.jpass.ui.dialogs.UnlockDialog;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class PasswordListPanel extends JPanel {
    private final JTextField searchField;
    private final JPanel entriesList;
    private final EntryService entryService;
    private PasswordEntry selectedEntry;
    private final Map<JPanel, PasswordEntry> entryPanels;
    private final JButton addButton;

    public PasswordListPanel(EntryService entryService) {
        this.entryService = entryService;
        this.entryPanels = new HashMap<>();
        setPreferredSize(new Dimension(380, -1));
        setBackground(new Color(44, 44, 46));
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(61, 61, 61)));
        setLayout(new BorderLayout());

        // Create top panel for search and add button
        JPanel topPanel = new JPanel(new BorderLayout(12, 0));
        topPanel.setBackground(new Color(44, 44, 46));
        topPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Search field with its own panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(new Color(44, 44, 46));
        searchField = new JTextField();
        searchField.putClientProperty("JTextField.placeholderText", "Search in vault...");
        searchField.setBackground(new Color(28, 28, 30));
        searchField.setForeground(Color.WHITE);
        searchField.setCaretColor(Color.WHITE);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(61, 61, 61)),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        searchPanel.add(searchField);

        // Add button
        addButton = new JButton("+ New");
        addButton.setBackground(new Color(10, 132, 255));
        addButton.setForeground(Color.WHITE);
        addButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        addButton.setFocusPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.addActionListener(e -> createNewPasswordEntry());

        // Add components to top panel
        topPanel.add(searchPanel, BorderLayout.CENTER);
        topPanel.add(addButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Add search listener
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            private void search() {
                updateEntriesList(searchField.getText());
            }

            @Override
            public void insertUpdate(DocumentEvent e) { search(); }
            @Override
            public void removeUpdate(DocumentEvent e) { search(); }
            @Override
            public void changedUpdate(DocumentEvent e) { search(); }
        });

        // Entries list
        entriesList = new JPanel();
        entriesList.setBackground(new Color(44, 44, 46));
        entriesList.setLayout(new BoxLayout(entriesList, BoxLayout.Y_AXIS));

        // Initial load of entries
        updateEntriesList("");

        JScrollPane scrollPane = new JScrollPane(entriesList);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(new Color(44, 44, 46));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void createNewPasswordEntry() {
        if (entryService.isLocked()) {
            unlockVault();
        }
        
        if (!entryService.isLocked()) {
            Window window = SwingUtilities.getWindowAncestor(this);
            NewPasswordDialog dialog = new NewPasswordDialog((Frame) window);
            dialog.setVisible(true);

            PasswordEntry newEntry = dialog.getResult();
            if (newEntry != null) {
                try {
                    String id = entryService.addEntry(newEntry);
                    newEntry.setId(id);
                    updateEntriesList(searchField.getText());
                    selectEntry(newEntry);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                        "Failed to create new entry: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private boolean unlockVault() {
        Window window = SwingUtilities.getWindowAncestor(this);
        // Récupérer la liste des coffres depuis le VaultManager
        List<String> vaults = entryService.listVaults();
        UnlockDialog dialog = new UnlockDialog((Frame) window, vaults);
        dialog.setVisible(true);

        String selectedVault = dialog.getSelectedVault();
        String password = dialog.getPassword();
        
        if (password != null && selectedVault != null) {
            try {
                entryService.unlock(selectedVault, password);
                updateEntriesList(searchField.getText());
                return true;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Failed to unlock vault: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        return false;
    }

    private void updateEntriesList(String searchTerm) {
        entriesList.removeAll();
        entryPanels.clear();

        // Get entries and group by month/year
        List<Entry> entries = entryService.searchEntries(searchTerm, PasswordEntry.class);
        Map<String, List<PasswordEntry>> groupedEntries = entries.stream()
            .map(e -> (PasswordEntry) e)
            .collect(Collectors.groupingBy(this::getMonthYearKey));

        // Sort groups by date (most recent first)
        groupedEntries.keySet().stream()
            .sorted(Comparator.reverseOrder())
            .forEach(monthYear -> {
                // Add month/year header
                addCategoryHeader(monthYear);
                
                // Add entries for this month
                groupedEntries.get(monthYear).forEach(entry -> 
                    addPasswordEntry(entry, entry.equals(selectedEntry))
                );
            });

        entriesList.revalidate();
        entriesList.repaint();
    }

    private String getMonthYearKey(PasswordEntry entry) {
        LocalDateTime date = entry.getModificationDates().isEmpty() ? 
            entry.getCreationDate() : 
            entry.getModificationDates().get(entry.getModificationDates().size() - 1);
            
        return date.format(DateTimeFormatter.ofPattern("MMMM yyyy").withLocale(Locale.ENGLISH));
    }

    private void addCategoryHeader(String text) {
        JLabel header = new JLabel(text.toUpperCase());
        header.setForeground(new Color(102, 102, 102));
        header.setFont(header.getFont().deriveFont(12f));
        header.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        entriesList.add(header);
    }

    private void addPasswordEntry(PasswordEntry entry, boolean isSelected) {
        Color bgColor = isSelected ? new Color(10, 132, 255) : new Color(44, 44, 46);
        
        // Main panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        panel.setBackground(bgColor);

        // Store reference to entry
        entryPanels.put(panel, entry);

        // Text container
        JPanel textContainer = new JPanel(new BorderLayout());
        textContainer.setBackground(bgColor);

        // Title and username panel
        JPanel labelsPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        labelsPanel.setBackground(bgColor);

        // Title
        JLabel titleLabel = new JLabel(entry.getTitle());
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBackground(bgColor);

        // Username
        JLabel usernameLabel = new JLabel(entry.getUsername());
        usernameLabel.setForeground(new Color(153, 153, 153));
        usernameLabel.setFont(usernameLabel.getFont().deriveFont(12f));
        usernameLabel.setBackground(bgColor);

        // Assemble components
        labelsPanel.add(titleLabel);
        labelsPanel.add(usernameLabel);
        textContainer.add(labelsPanel, BorderLayout.CENTER);
        panel.add(textContainer, BorderLayout.CENTER);

        // Add click listener
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectEntry(entry);
            }
        });

        entriesList.add(panel);
    }

    private void selectEntry(PasswordEntry entry) {
        selectedEntry = entry;
        
        // Update UI colors for all components
        entryPanels.forEach((panel, passwordEntry) -> {
            boolean isSelected = passwordEntry.equals(selectedEntry);
            Color bgColor = isSelected ? new Color(10, 132, 255) : new Color(44, 44, 46);
            
            // Update all component colors
            updatePanelColors(panel, bgColor);
        });

        // Notify listeners
        firePropertyChange("selectedEntry", null, selectedEntry);
    }

    private void updatePanelColors(Container container, Color bgColor) {
        container.setBackground(bgColor);
        
        // Update all child components
        for (Component comp : container.getComponents()) {
            comp.setBackground(bgColor);
            if (comp instanceof Container) {
                updatePanelColors((Container) comp, bgColor);
            }
        }
    }

    public PasswordEntry getSelectedEntry() {
        return selectedEntry;
    }
}
