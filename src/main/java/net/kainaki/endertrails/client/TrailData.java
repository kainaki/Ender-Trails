package net.kainaki.endertrails.client;

import net.minecraft.util.math.Vec3d;

public class TrailData {
    
    private static final int BUFFER_SIZE = 12;
    private final Vec3d[] positionRing = new Vec3d[BUFFER_SIZE];
    private int currentIndex = 0;
    private int validPositions = 0;
    
    public void addPosition(Vec3d position) {
        positionRing[currentIndex] = position;
        currentIndex = (currentIndex + 1) % BUFFER_SIZE;
        if (validPositions < BUFFER_SIZE) {
            validPositions++;
        }
    }
    
    public Vec3d getPosition(int stepsBack) {
        if (stepsBack < 0 || stepsBack >= validPositions) {
            return null;
        }
        int index = (currentIndex - 1 - stepsBack + BUFFER_SIZE) % BUFFER_SIZE;
        return positionRing[index];
    }
    
    public int getValidPositionsCount() {
        return validPositions;
    }
    
    public void clear() {
        for (int i = 0; i < BUFFER_SIZE; i++) {
            positionRing[i] = null;
        }
        validPositions = 0;
        currentIndex = 0;
    }
}