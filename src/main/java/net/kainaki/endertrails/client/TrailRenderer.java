package net.kainaki.endertrails.client;

import net.kainaki.endertrails.EnderTrailsMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import java.util.Random;

public class TrailRenderer {
    
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final Random random = new Random();
    
    public static void spawnTrailParticles(EnderPearlEntity pearl) {
        if (!EnderTrailsMod.isTrailsEnabled() || client.world == null) {
            return;
        }
        
        if (!pearl.isAlive() || pearl.age < 2) {
            return;
        }
        
        Vec3d pos = pearl.getPos();
        Vec3d velocity = pearl.getVelocity();
        
        if (velocity.lengthSquared() < 0.01) {
            return;
        }
        
        int particleCount = 3;
        
        for (int i = 0; i < particleCount; i++) {
            double offsetX = (random.nextDouble() - 0.5) * 0.2;
            double offsetY = (random.nextDouble() - 0.5) * 0.2;
            double offsetZ = (random.nextDouble() - 0.5) * 0.2;
            
            double velX = -velocity.x * 0.1 + (random.nextDouble() - 0.5) * 0.05;
            double velY = -velocity.y * 0.1 + (random.nextDouble() - 0.5) * 0.05;
            double velZ = -velocity.z * 0.1 + (random.nextDouble() - 0.5) * 0.05;
            
            client.world.addParticle(
                ParticleTypes.PORTAL,
                pos.x + offsetX,
                pos.y + offsetY,
                pos.z + offsetZ,
                velX, velY, velZ
            );
            
            client.world.addParticle(
                ParticleTypes.REVERSE_PORTAL,
                pos.x + offsetX * 0.5,
                pos.y + offsetY * 0.5,
                pos.z + offsetZ * 0.5,
                velX * 0.5, velY * 0.5, velZ * 0.5
            );
        }
        
        if (random.nextFloat() < 0.3f) {
            client.world.addParticle(
                ParticleTypes.WITCH,
                pos.x,
                pos.y,
                pos.z,
                0, 0, 0
            );
        }
    }
}