package com.codeinsight.project.util;

import java.io.*;
import java.nio.file.*;
import java.util.zip.*;

public class ZipExtractionUtil {

    public static void unzip(Path zipFilePath, Path destDir) throws IOException {

        try (ZipInputStream zis = new ZipInputStream(
                new FileInputStream(zipFilePath.toFile()))) {

            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {

                Path newPath = destDir.resolve(entry.getName()).normalize();

                if (!newPath.startsWith(destDir)) {
                    throw new IOException("Bad zip entry");
                }

                if (entry.isDirectory()) {
                    Files.createDirectories(newPath);
                } else {
                    Files.createDirectories(newPath.getParent());
                    Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);
                }

                zis.closeEntry();
            }
        }
    }
}