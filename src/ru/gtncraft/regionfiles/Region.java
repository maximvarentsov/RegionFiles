package ru.gtncraft.regionfiles;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.io.File;

public class Region {
    public final int x;
    public final int z;

    public Region(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public int getMinChunkX() {
        return x << 5;
    }

    public int getMinChunkZ() {
        return z << 5;
    }

    public int getMaxChunkX() {
        return (x + 1 << 5) - 1;
    }

    public int getMaxChunkZ() {
        return (z + 1 << 5) - 1;
    }

    public Multimap<Integer, Integer> getChunks() {
        Multimap<Integer, Integer> result = ArrayListMultimap.create();
        for (int x = getMinChunkX(); x <= getMaxChunkX(); x++) {
            for (int z = getMinChunkZ(); z <= getMaxChunkZ(); z++) {
                result.put(x, z);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "r." + x + "." + z + ".mca";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Region) {
            Region region = (Region) obj;
            return region.x == x && region.z == z;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return x * 31 + z;
    }
}
