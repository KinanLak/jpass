package com.jpass;

import com.jpass.ui.MainWindow;
import com.jpass.ui.dialogs.CreateVaultDialog;
import com.jpass.ui.dialogs.UnlockDialog;
import com.jpass.service.EntryService;
import com.jpass.service.SecurityService;
import com.jpass.service.VaultManager;
import javax.swing.*;
import java.awt.*;
//import java.nio.file.Files;
//import java.nio.file.Paths;
import java.util.List;

/**
 * Point d'entrée principal de l'application de gestion de mots de passe.
 */
public class JPassApp {
    /**
     * Méthode principale qui initialise l'application et lance l'interface
     * graphique.
     * 
     * @param args Arguments de la ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                SecurityService securityService = new SecurityService();
                EntryService entryService = new EntryService(securityService);
                
                JFrame tempFrame = new JFrame();
                tempFrame.setUndecorated(true);
                tempFrame.setLocationRelativeTo(null);
                tempFrame.setVisible(true);

                boolean continueLoop = true;
                while (continueLoop) {
                    VaultManager vaultManager = new VaultManager(securityService);
                    List<String> vaults = vaultManager.listVaults();
                    
                    String selectedVault = null;
                    String masterPassword = null;

                    if (vaults.isEmpty()) {
                        CreateVaultDialog createDialog = new CreateVaultDialog(tempFrame);
                        createDialog.setVisible(true);
                        selectedVault = createDialog.getVaultName();
                        masterPassword = createDialog.getMasterPassword();
                        if (masterPassword == null || selectedVault == null) {
                            System.exit(0);
                        }
                        continueLoop = false;
                    } else {
                        UnlockDialog unlockDialog = new UnlockDialog(tempFrame, vaults);
                        unlockDialog.setVisible(true);

                        if (unlockDialog.isCreateNewVaultRequested()) {
                            CreateVaultDialog createDialog = new CreateVaultDialog(tempFrame);
                            createDialog.setVisible(true);
                            selectedVault = createDialog.getVaultName();
                            masterPassword = createDialog.getMasterPassword();
                            if (masterPassword == null || selectedVault == null) {
                                continue; // Retour au début de la boucle
                            }
                            continueLoop = false;
                        } else {
                            selectedVault = unlockDialog.getSelectedVault();
                            masterPassword = unlockDialog.getPassword();
                            if (masterPassword == null || selectedVault == null) {
                                System.exit(0);
                            }
                            continueLoop = false;
                        }
                    }

                    if (!continueLoop) {
                        try {
                            entryService.unlock(selectedVault, masterPassword);
                            MainWindow mainWindow = new MainWindow(entryService);
                            mainWindow.setVisible(true);
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(tempFrame,
                                "Failed to unlock vault: " + e.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                            continueLoop = true; // Retour au début en cas d'erreur
                        }
                    }
                }

                tempFrame.dispose();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        });
    }

    private static String showCreateVaultDialog(Frame owner) {
        CreateVaultDialog createDialog = new CreateVaultDialog(owner);
        createDialog.setVisible(true);
        return createDialog.getVaultName();
    }
}