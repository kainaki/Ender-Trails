package net.kainaki.endertrails.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.kainaki.endertrails.EnderTrailsMod;
import net.minecraft.text.Text;

public class EnderTrailsCommand {
    
    public static int showInfo(CommandContext<FabricClientCommandSource> context) {
        context.getSource().sendFeedback(Text.literal("§5Ender Trails v1.0.0 by Kainaki"));
        return Command.SINGLE_SUCCESS;
    }
    
    public static int toggle(CommandContext<FabricClientCommandSource> context) {
        boolean currentState = EnderTrailsMod.isTrailsEnabled();
        EnderTrailsMod.setTrailsEnabled(!currentState);
        
        String message = EnderTrailsMod.isTrailsEnabled() ? 
            "§aEnder trails: ENABLED" : "§cEnder trails: DISABLED";
        context.getSource().sendFeedback(Text.literal(message));
        
        return Command.SINGLE_SUCCESS;
    }
}