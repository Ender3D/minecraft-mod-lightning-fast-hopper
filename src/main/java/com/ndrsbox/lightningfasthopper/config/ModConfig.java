package com.ndrsbox.lightningfasthopper.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("lightningfasthopper.json")
            .toFile();

    // Configurable speeds (Lower = Faster)
    public int inputCooldownTicks = 2;
    public int outputCooldownTicks = 2;

    // Transfer settings
    public boolean pullEntireStack = true;
    public int outputTransferBatchSize = 1; // Number of items to push into containers at once (e.g. 1, 4, 16, 64)

    public static ModConfig load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                return GSON.fromJson(reader, ModConfig.class);
            } catch (Exception e) {
                System.err.println("[LightningFastHopper] Failed to load config, using defaults.");
            }
        }
        ModConfig config = new ModConfig();
        config.save();
        return config;
    }

    public void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(this, writer);
        } catch (Exception e) {
            System.err.println("[LightningFastHopper] Failed to save config.");
        }
    }
}
