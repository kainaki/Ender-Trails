package net.kainaki.endertrails.client;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;

public class SplashParticle extends SpriteBillboardParticle {
    
    private boolean gravityEnabled = false;
    private int groundContactTicks = 0;
    
    protected SplashParticle(ClientWorld world, double x, double y, double z, 
                            double velocityX, double velocityY, double velocityZ) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        
        this.maxAge = 15 + random.nextInt(6);
        this.scale = 0.2f;
        this.red = 180f / 255f;
        this.green = 0f;
        this.blue = 255f / 255f;
        this.alpha = 1.0f;
        this.collidesWithWorld = true;
    }
    
    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        
        if (this.age++ >= this.maxAge) {
            this.markDead();
            return;
        }
        
        if (!gravityEnabled && this.age >= 4) {
            gravityEnabled = true;
        }
        
        if (gravityEnabled) {
            this.velocityY -= 0.04;
        }
        
        if (this.onGround) {
            groundContactTicks++;
            this.velocityX *= 0.7;
            this.velocityZ *= 0.7;
            
            if (groundContactTicks > 3) {
                this.velocityX *= 0.5;
                this.velocityZ *= 0.5;
            }
        }
        
        this.move(this.velocityX, this.velocityY, this.velocityZ);
        
        float lifeProgress = (float) this.age / this.maxAge;
        this.alpha = 1.0f - lifeProgress;
        this.scale = 0.2f * (1.0f - lifeProgress * 0.5f);
    }
    
    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }
}