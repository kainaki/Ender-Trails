package net.kainaki.endertrails.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.kainaki.endertrails.EnderTrailsMod;

import java.io.*;
import java.nio.file.Path;

public class ModConfig {
    
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
        .getConfigDir().resolve("endertrails.json");
    
    public boolean enabled = true;
    public int trail_length = 12;
    public double trail_width_start = 0.15;
    public double trail_width_end = 0.05;
    public int splash_particles = 8;
    public boolean splash_enabled = true;
    
    public static ModConfig load() {
        if (!CONFIG_PATH.toFile().exists()) {
            ModConfig defaultConfig = new ModConfig();
            save(defaultConfig);
            return defaultConfig;
        }
        
        try (Reader reader = new FileReader(CONFIG_PATH.toFile())) {
            return GSON.fromJson(reader, ModConfig.class);
        } catch (IOException e) {
            EnderTrailsMod.LOGGER.error("Failed to load config", e);
            return new ModConfig();
        }
    }
    
    public static void save(ModConfig config) {
        try (Writer writer = new FileWriter(CONFIG_PATH.toFile())) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            EnderTrailsMod.LOGGER.error("Failed to save config", e);
        }
    }
}