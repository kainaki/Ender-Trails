package net.kainaki.endertrails.mixin;

import net.kainaki.endertrails.client.RiftSplashHandler;
import net.kainaki.endertrails.client.TrailRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderPearlEntity.class)
public abstract class EnderPearlEntityMixin extends Entity {
    
    private boolean hasCollided = false;
    
    public EnderPearlEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (this.getWorld().isClient) {
            EnderPearlEntity self = (EnderPearlEntity) (Object) this;
            
            TrailRenderer.spawnTrailParticles(self);
            
            if (!hasCollided && (this.isOnGround() || this.verticalCollision)) {
                hasCollided = true;
                RiftSplashHandler.createSplash(self);
            }
        }
    }
}