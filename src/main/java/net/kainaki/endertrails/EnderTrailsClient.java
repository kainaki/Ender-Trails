package net.kainaki.endertrails;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.kainaki.endertrails.client.RiftSplashHandler;
import net.kainaki.endertrails.client.TrailTracker;
import net.kainaki.endertrails.command.EnderTrailsCommand;

public class EnderTrailsClient implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world != null && EnderTrailsMod.isTrailsEnabled()) {
                TrailTracker.updateAll();
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
}