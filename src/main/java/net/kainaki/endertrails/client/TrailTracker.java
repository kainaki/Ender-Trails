package net.kainaki.endertrails.client;

import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.util.math.Vec3d;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

public class TrailTracker {
    
    private static final Map<EnderPearlEntity, TrailData> trails = new WeakHashMap<>();
    
    public static void registerPearl(EnderPearlEntity pearl) {
        trails.put(pearl, new TrailData());
    }
    
    public static void updatePosition(EnderPearlEntity pearl, Vec3d position) {
        TrailData data = trails.get(pearl);
        if (data != null) {
            data.addPosition(position);
        }
    }
    
    public static void removePearl(EnderPearlEntity pearl) {
        trails.remove(pearl);
    }
    
    public static void updateAll() {
        Iterator<Map.Entry<EnderPearlEntity, TrailData>> iterator = trails.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<EnderPearlEntity, TrailData> entry = iterator.next();
            EnderPearlEntity pearl = entry.getKey();
            if (pearl.isRemoved()) {
                iterator.remove();
            }
        }
    }
    
    public static Map<EnderPearlEntity, TrailData> getTrails() {
        return trails;
    }
    
    public static void clear() {
        trails.clear();
    }
}