package com.jpass.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class Sidebar extends JPanel {

    private final JComboBox<String> vaultSelector;
    private final JPanel categoriesList;

    public Sidebar(List<String> vaults, String currentVault) {
        setPreferredSize(new Dimension(260, -1));
        setBackground(new Color(44, 44, 46));
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(61, 61, 61)));
        setLayout(new BorderLayout());

        // Vault selector panel
        JPanel selectorPanel = new JPanel(new BorderLayout());
        selectorPanel.setBackground(new Color(44, 44, 46));
        selectorPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        vaultSelector = new JComboBox<>(vaults.toArray(new String[0]));
        vaultSelector.setSelectedItem(currentVault);
        vaultSelector.setBackground(new Color(61, 61, 61));
        vaultSelector.setForeground(Color.WHITE);
        vaultSelector.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        
        selectorPanel.add(vaultSelector, BorderLayout.CENTER);
        
        add(selectorPanel, BorderLayout.NORTH);

        // Categories
        categoriesList = new JPanel();
        categoriesList.setBackground(new Color(44, 44, 46));
        categoriesList.setLayout(new BoxLayout(categoriesList, BoxLayout.Y_AXIS));
        categoriesList.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        addCategory("📑 All Items", true);
        addCategory("⭐ Favorites", false);
        addCategory("🔑 Logins", false);
        addCategory("📝 Secure Notes", false);
        addCategory("💳 Credit Cards", false);
        addCategory("👤 Identities", false);

        JScrollPane scrollPane = new JScrollPane(categoriesList);
        scrollPane.setBorder(null);
        scrollPane.setBackground(new Color(44, 44, 46));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addCategory(String name, boolean selected) {
        JPanel category = new JPanel(new BorderLayout());
        category.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        category.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        if (selected) {
            category.setBackground(new Color(10, 132, 255));
        } else {
            category.setBackground(new Color(44, 44, 46));
        }

        JLabel label = new JLabel(name);
        label.setForeground(Color.WHITE);
        category.add(label);

        categoriesList.add(category);
        categoriesList.add(Box.createVerticalStrut(2));
    }

    public void setVaultChangeListener(ActionListener listener) {
        vaultSelector.addActionListener(listener);
    }

    public String getSelectedVault() {
        return (String) vaultSelector.getSelectedItem();
    }
}
