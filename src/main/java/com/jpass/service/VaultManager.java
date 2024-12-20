package com.jpass.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.jpass.model.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class VaultManager {
    private static final String VAULT_DIR = "vaults";
    //private static final String VAULT_FILE = "data.jpass";
    private final SecurityService securityService;
    private final ObjectMapper objectMapper;
    private String currentSalt;

    public VaultManager(SecurityService securityService) {
        this.securityService = securityService;
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        // Add these lines to handle Java collections better
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        initializeVault();
    }

    private void initializeVault() {
        try {
            Path vaultDir = Paths.get(VAULT_DIR);
            if (!Files.exists(vaultDir)) {
                Files.createDirectory(vaultDir);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize vault", e);
        }
    }

    public List<String> listVaults() {
        try {
            Path vaultDir = Paths.get(VAULT_DIR);
            if (!Files.exists(vaultDir)) {
                return new ArrayList<>();
            }
            
            return Files.list(vaultDir)
                .filter(path -> path.toString().endsWith(".jpass"))
                .map(path -> path.getFileName().toString().replace(".jpass", ""))
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to list vaults", e);
        }
    }

    public void saveVault(String vaultName, Map<String, Entry> entries, String masterPassword) throws Exception {
        if (currentSalt == null) {
            currentSalt = securityService.generateSalt();
        }

        // Vérifier que toutes les entrées ont un ID
        entries.values().forEach(entry -> {
            if (entry.getId() == null) {
                throw new IllegalStateException("Entry must have an ID before saving: " + entry.getTitle());
            }
        });

        Map<String, Object> vaultData = new HashMap<>();
        vaultData.put("encrypted", true);
        vaultData.put("salt", currentSalt);

        // Grouper et encoder chaque type d'entrée séparément
        Arrays.asList(PasswordEntry.class, SecureNote.class, CreditCardEntry.class)
                .forEach(type -> {
                    try {
                        List<Entry> typeEntries = entries.values().stream()
                                .filter(type::isInstance)
                                .collect(Collectors.toList());

                        if (!typeEntries.isEmpty()) {
                            Map<String, Entry> entriesMap = typeEntries.stream()
                                    .collect(Collectors.toMap(Entry::getId, e -> e));

                            String json = objectMapper.writeValueAsString(entriesMap);
                            String encrypted = securityService.encrypt(json, masterPassword, currentSalt);
                            vaultData.put(type.getSimpleName().toLowerCase() + "_entries", encrypted);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(
                                "Failed to encrypt " + type.getSimpleName() + " entries: " + e.getMessage(), e);
                    }
                });

        String finalJson = objectMapper.writeValueAsString(vaultData);
        Files.write(Paths.get(VAULT_DIR, vaultName + ".jpass"), finalJson.getBytes());
    }

    @SuppressWarnings("unchecked")
    public Map<String, Entry> loadVault(String vaultName, String masterPassword) throws Exception {
        Path vaultPath = Paths.get(VAULT_DIR, vaultName + ".jpass");
        if (!Files.exists(vaultPath)) {
            return new HashMap<>();
        }

        try {
            String vaultContent = Files.readString(vaultPath);

            Map<String, Object> vaultData = objectMapper.readValue(vaultContent, Map.class);

            // Vérifier le format des données
            if (vaultData == null) {
                throw new IllegalStateException("Vault data is null");
            }

            currentSalt = (String) vaultData.get("salt");
            if (currentSalt == null) {
                throw new IllegalStateException("Salt is missing from vault data");
            }

            Map<String, Entry> allEntries = new HashMap<>();

            // Types d'entrées à charger
            List<Class<? extends Entry>> entryTypes = Arrays.asList(
                    PasswordEntry.class,
                    SecureNote.class,
                    CreditCardEntry.class,
                    IdentityEntry.class); // Add this line

            // Charger chaque type d'entrée
            for (Class<? extends Entry> type : entryTypes) {
                String key = type.getSimpleName().toLowerCase() + "_entries";
                String encryptedData = (String) vaultData.get(key);

                if (encryptedData != null) {
                    try {
                        String decryptedJson = securityService.decrypt(encryptedData, masterPassword, currentSalt);

                        Map<String, ? extends Entry> typeEntries = objectMapper.readValue(
                                decryptedJson,
                                new com.fasterxml.jackson.core.type.TypeReference<Map<String, Entry>>() {
                                });

                        allEntries.putAll(typeEntries);
                    } catch (Exception e) {
                        throw new IllegalStateException("Failed to parse " + key + ": " + e.getMessage(), e);
                    }
                }
            }

            return allEntries;

        } catch (Exception e) {
            throw new IllegalStateException("Failed to load vault: " + e.getMessage(), e);
        }
    }
}
