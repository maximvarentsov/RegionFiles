package ru.gtncraft.regionfiles;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.HashSet;

public class Cuboid {
    private final static int CHUNK_SIZE = 16;

    public static Multimap<Integer, Integer> getChunks(int x1, int z1, int x2, int z2) {
        Multimap<Integer, Integer> result = ArrayListMultimap.create();
        for (int x = x1; x <= x2; x += CHUNK_SIZE) {
            for (int z = z1; z <= z2; z += CHUNK_SIZE) {
                int chunkX = x >> 4;
                int chunkZ = z >> 4;
                result.put(chunkX, chunkZ);
            }
        }
        return result;
    }

    public static Collection<Region> getRegions(int x1, int z1, int x2, int z2) {
        Collection<Region> result = new HashSet<Region>();
        for (int x = x1; x <= x2; x += CHUNK_SIZE) {
            for (int z = z1; z <= z2; z += CHUNK_SIZE) {
                int regionX = (x >> 4) >> 5;
                int regionZ = (z >> 4) >> 5;
                Region region = new Region(regionX, regionZ);
                result.add(region);
            }
        }
        return result;
    }
}
