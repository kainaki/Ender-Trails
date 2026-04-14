package net.kainaki.endertrails.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kainaki.endertrails.EnderTrailsMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class RiftSplashHandler {
    
    private static final List<RiftRing> activeRings = new ArrayList<>();
    private static final Random random = new Random();
    
    public static void createSplash(EnderPearlEntity pearl) {
        if (!EnderTrailsMod.getConfig().splash_enabled) {
            return;
        }
        
        World world = pearl.getWorld();
        if (world.isClient) {
            Vec3d impactPos = pearl.getPos();
            
            activeRings.add(new RiftRing(impactPos.add(0, 0.1, 0)));
            
            int particleCount = EnderTrailsMod.getConfig().splash_particles;
            for (int i = 0; i < particleCount; i++) {
                spawnEjectaParticle(world, impactPos);
            }
        }
    }
    
    private static void spawnEjectaParticle(World world, Vec3d pos) {
        double offsetX = (random.nextDouble() - 0.5) * 0.3;
        double offsetZ = (random.nextDouble() - 0.5) * 0.3;
        double velX = (random.nextDouble() - 0.5) * 0.15;
        double velY = random.nextDouble() * 0.05 + 0.02;
        double velZ = (random.nextDouble() - 0.5) * 0.15;
        
        MinecraftClient.getInstance().particleManager.addParticle(
            ParticleTypes.PORTAL,
            pos.x + offsetX,
            pos.y + 0.05,
            pos.z + offsetZ,
            velX, velY, velZ);
    }
    
    public static void renderRings(MatrixStack matrices, Camera camera) {
        if (activeRings.isEmpty()) {
            return;
        }
        
        Vec3d cameraPos = camera.getPos();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        
        buffer.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
        Matrix4f positionMatrix = matrices.peek().getPositionMatrix();
        
        for (RiftRing ring : activeRings) {
            renderRing(buffer, positionMatrix, cameraPos, ring);
        }
        
        tessellator.draw();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }
    
    private static void renderRing(BufferBuilder buffer, Matrix4f matrix, Vec3d cameraPos, RiftRing ring) {
        Vec3d center = ring.getCenter();
        float radius = ring.getRadius();
        float alpha = ring.getAlpha();
        
        int segments = 16;
        for (int i = 0; i < segments; i++) {
            double angle1 = (2 * Math.PI * i) / segments;
            double angle2 = (2 * Math.PI * (i + 1)) / segments;
            
            double x1 = center.x + Math.cos(angle1) * radius;
            double z1 = center.z + Math.sin(angle1) * radius;
            double x2 = center.x + Math.cos(angle2) * radius;
            double z2 = center.z + Math.sin(angle2) * radius;
            
            Vec3d v1 = new Vec3d(x1, center.y, z1).subtract(cameraPos);
            Vec3d v2 = new Vec3d(x2, center.y, z2).subtract(cameraPos);
            
            int colorAlpha = (int) (100 * alpha);
            
            buffer.vertex(matrix, (float) v1.x, (float) v1.y, (float) v1.z)
                .color(180, 0, 255, colorAlpha).next();
            buffer.vertex(matrix, (float) v2.x, (float) v2.y, (float) v2.z)
                .color(180, 0, 255, colorAlpha).next();
        }
    }
    
    public static void tickRings() {
        Iterator<RiftRing> iterator = activeRings.iterator();
        while (iterator.hasNext()) {
            RiftRing ring = iterator.next();
            ring.tick();
            if (ring.isExpired()) {
                iterator.remove();
            }
        }
    }
    
    public static List<RiftRing> getActiveRings() {
        return activeRings;
    }
    
    public static void clear() {
        activeRings.clear();
    }
    
    public static class RiftRing {
        private final Vec3d center;
        private int age = 0;
        private final int maxAge = 6;
        private float currentRadius = 0.3f;
        
        public RiftRing(Vec3d center) {
            this.center = center;
        }
        
        public void tick() {
            age++;
            currentRadius = 0.3f + (age / (float) maxAge) * 0.9f;
        }
        
        public boolean isExpired() {
            return age >= maxAge;
        }
        
        public Vec3d getCenter() {
            return center;
        }
        
        public float getRadius() {
            return currentRadius;
        }
        
        public float getAlpha() {
            return 1.0f - (age / (float) maxAge);
        }
    }
}