package com.jpass.service;

import com.jpass.model.Entry;
import java.util.*;

public class EntryService {
    private final VaultManager vaultManager;
    private Map<String, Entry> entries;
    private String masterPassword;
    private String currentVault;

    public EntryService(SecurityService securityService) {
        this.vaultManager = new VaultManager(securityService);
        this.entries = new HashMap<>();
    }

    public void unlock(String vaultName, String masterPassword) throws Exception {
        this.currentVault = vaultName;
        this.masterPassword = masterPassword;
        this.entries = vaultManager.loadVault(vaultName, masterPassword);
    }

    public String addEntry(Entry entry) throws Exception {
        if (isLocked()) {
            throw new IllegalStateException("Vault is locked");
        }

        String id = UUID.randomUUID().toString();
        entry.setId(id);
        
        entries.put(id, entry);
        vaultManager.saveVault(currentVault, entries, masterPassword);
        return id;
    }

    public void updateEntry(Entry entry) throws Exception {
        if (isLocked()) {
            throw new IllegalStateException("Vault is locked");
        }
        
        if (entry.getId() == null || !entries.containsKey(entry.getId())) {
            throw new IllegalArgumentException("Entry does not exist in vault");
        }
        
        entries.put(entry.getId(), entry);
        vaultManager.saveVault(currentVault, entries, masterPassword);
    }

    public List<Entry> searchEntries(String searchTerm, Class<? extends Entry> type) {
        return entries.values().stream()
            .filter(entry -> type == null || type.isInstance(entry))
            .filter(entry -> searchTerm == null || 
                entry.getTitle().toLowerCase().contains(searchTerm.toLowerCase()))
            .toList();
    }

    public void switchVault(String newVaultName, String masterPassword) throws Exception {
        lock();
        unlock(newVaultName, masterPassword);
    }

    public void lock() {
        masterPassword = null;
        currentVault = null;
        entries.clear();
    }

    public boolean isLocked() {
        return masterPassword == null || currentVault == null;
    }

    public String getCurrentVault() {
        return currentVault;
    }

    public List<String> listVaults() {
        return vaultManager.listVaults();
    }
}
