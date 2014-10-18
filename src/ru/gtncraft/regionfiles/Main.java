package ru.gtncraft.regionfiles;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mojang.RegionFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.*;

public class Main {
    private final static String usage = "Usage: -world=/path/to/world -cube=x1,z1,x2,z2 [-out=out]";

    public static void main(String args[]) throws IOException {
        int x1 = 0, x2 = 0, z1 = 0, z2 = 0;
        String path = null;
        File out = new File("out");

        try {
            path = args[0].substring(7);
            String[] xzxz = args[1].substring(6).split(",");
            x1 = Integer.parseInt(xzxz[0]);
            z1 = Integer.parseInt(xzxz[1]);
            x2 = Integer.parseInt(xzxz[2]);
            z2 = Integer.parseInt(xzxz[3]);
            if (args[2].startsWith("-out=")) {
                out = new File(args[2].substring(5));
            }
        } catch (Throwable ex) {
            System.out.println(usage);
            System.exit(1);
        }

        if (out.exists()) {
            out.delete();
            out.mkdir();
        }

        int lowerX = Math.min(x1, x2);
        int lowerZ = Math.min(z1, z2);
        int upperX = Math.max(x1, x2);
        int upperZ = Math.max(z1, z2);

        Collection<Region> regions = Cuboid.getRegions(lowerX, lowerZ, upperX, upperZ);
        Multimap<Integer, Integer> chunks = Cuboid.getChunks(lowerX, lowerZ, upperX, upperZ);

        Multimap<Integer, Integer> chunksDelete = ArrayListMultimap.create();
        for (Region region : regions) {
            for (Map.Entry<Integer, Integer> chunk : region.getChunks().entries()) {
                int chunkX = chunk.getKey();
                int chunkZ = chunk.getValue();
                if (!chunks.containsEntry(chunkX, chunkZ)) {
                    chunksDelete.put(chunkX, chunkZ);
                }
            }
        }

        for (Region region : regions) {
            File src = new File(path, region.toString());
            copyFile(src, out);
            File dst = new File(out, region.toString());

            RegionFile regionFile = new RegionFile(dst);
            for (Map.Entry<Integer, Integer> chunk : chunksDelete.entries()) {
                int x = chunk.getKey();
                int z = chunk.getValue();
                if (regionFile.hasChunk(x, z)) {
                    regionFile.deleteChunck(x, z);
                    chunksDelete.remove(x, z);
                }

            }
            regionFile.close();
        }
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }
}
