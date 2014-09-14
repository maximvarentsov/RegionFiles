package ru.gtncraft.regionfiles;

import java.util.*;

public class Main {

    private final static String usage = "Usage: -p1=x,z -p2=x,z ...";

    public static void main(String args[]) {
        if (args.length == 0 || (args.length % 2) != 0) {
            System.out.println(usage);
            System.exit(1);
        }
        int i = 0;
        do {
            String p1 = args[i];
            String p2 = args[i + 1];
            try {
                if (p1.startsWith("-p1=") && p2.startsWith("-p2")) {
                    String[] xz1 = p1.substring(4).split(",");
                    String[] xz2 = p2.substring(4).split(",");
                    int lowerX = Integer.parseInt(xz1[0]);
                    int lowerZ = Integer.parseInt(xz1[1]);
                    int upperX = Integer.parseInt(xz2[0]);
                    int upperZ = Integer.parseInt(xz2[1]);
                    // Get chunks
                    int x1 = lowerX & ~0xf;
                    int x2 = upperX & ~0xf;
                    int z1 = lowerZ & ~0xf;
                    int z2 = upperZ & ~0xf;
                    System.out.println("Region files for p1[" + lowerX + ", " + lowerZ + "] " + "p2[" + upperX + ", " + upperZ + "]:");
                    Collection<String> regions = new HashSet<String>();
                    for (int x = x1; x <= x2; x += 16) {
                        for (int z = z1; z <= z2; z += 16) {
                            int chunkX = x >> 4;
                            int chunkZ = z >> 4;
                            int regionX = chunkX >> 5;
                            int regionZ = chunkZ >> 5;
                            String value = regionX + "." + regionZ;
                            regions.add(value);
                        }
                    }
                    for (String value : regions) {
                        System.out.print("r." + value + ".mca");
                        System.out.print(" ");
                    }
                    System.out.println();
                }
            } catch (Throwable ex) {
                System.out.println(usage);
                System.exit(1);
            }
            i+=2;
        } while (args.length > i);
    }
}
