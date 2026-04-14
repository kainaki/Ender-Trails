package net.kainaki.endertrails;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.kainaki.endertrails.client.RiftSplashHandler;
import net.kainaki.endertrails.client.TrailTracker;
import net.kainaki.endertrails.command.EnderTrailsCommand;
import net.kainaki.endertrails.config.ModConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnderTrailsMod implements ClientModInitializer {
    
    public static final String MOD_ID = "endertrails";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    private static ModConfig config;
    private static boolean trailsEnabled = true;
    
    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Ender Trails");
        config = ModConfig.load();
        trailsEnabled = config.enabled;
        
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world != null && trailsEnabled) {
                RiftSplashHandler.tickRings();
            }
        });
        
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            TrailTracker.clear();
            RiftSplashHandler.clear();
        });
        
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("endertrails")
                .executes(EnderTrailsCommand::showInfo)
                .then(ClientCommandManager.literal("toggle")
                    .executes(EnderTrailsCommand::toggle)));
        });
    }
    
    public static ModConfig getConfig() {
        return config;
    }
    
    public static boolean isTrailsEnabled() {
        return trailsEnabled;
    }
    
    public static void setTrailsEnabled(boolean enabled) {
        trailsEnabled = enabled;
        config.enabled = enabled;
        ModConfig.save(config);
    }
}