package dev.lone64.roseframework.spigot.util.file;

import dev.lone64.roseframework.spigot.util.Log;
import org.apache.commons.io.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class FileUtil {

    public static boolean createFile(JavaPlugin plugin, String name) {
        try {
            File folder = plugin.getDataFolder();
            if (!folder.exists()) {
                if (!folder.mkdir()) {
                    Log.severe("Cloud not create to '%s'".formatted(folder.getPath()));
                }
            }

            File file = new File(plugin.getDataFolder(), name.replace(plugin.getDataFolder().getPath() + "\\", ""));
            if (file.exists()) return false;
            if (plugin.getResource("default/" + name.replace(plugin.getDataFolder().getPath() + "\\", "")) != null) {
                saveResource(plugin, "default/" + name.replace(plugin.getDataFolder().getPath() + "\\", ""), false);
                return true;
            } else {
                return file.createNewFile();
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean createFolder(JavaPlugin plugin, String name) {
        File file = new File(plugin.getDataFolder(), name.replace(plugin.getDataFolder().getPath() + "\\", ""));
        if (file.exists()) return false;
        return file.mkdir();
    }

    public static boolean deleteFile(JavaPlugin plugin, String name) {
        File file = new File(plugin.getDataFolder(), name.replace(plugin.getDataFolder().getPath() + "\\", ""));
        if (!file.exists()) return false;
        return file.delete();
    }

    public static boolean deleteFolder(JavaPlugin plugin, String name) {
        try {
            File folder = new File(plugin.getDataFolder(), name.replace(plugin.getDataFolder().getPath() + "\\", ""));
            if (!folder.exists()) return false;
            FileUtils.deleteDirectory(folder);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean exists(JavaPlugin plugin, String name) {
        File file = new File(plugin.getDataFolder(), name);
        return file.exists();
    }

    public static boolean isDirectory(JavaPlugin plugin, String name) {
        File directory = new File(plugin.getDataFolder(), name);
        if (!directory.exists()) return false;
        return Files.isDirectory(directory.toPath());
    }

    public static List<String> getFiles(String name) {
        String[] strings = new File(name).list();
        if (strings != null) return Arrays.stream(strings).toList();
        return new ArrayList<>();
    }

    public static void saveResource(JavaPlugin plugin, String resourcePath, boolean replace) {
        if (resourcePath != null && !resourcePath.isEmpty()) {
            resourcePath = resourcePath.replace('\\', '/');
            InputStream in = plugin.getResource(resourcePath);
            if (in == null) {
                throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found.");
            } else {
                File outFile = new File(plugin.getDataFolder(), resourcePath.replace("default/", ""));
                int lastIndex = resourcePath.lastIndexOf(47);
                if (!resourcePath.contains("default/")) {
                    File outDir = new File(plugin.getDataFolder(), resourcePath.substring(0, Math.max(lastIndex, 0)).replace("default/", ""));
                    if (!outDir.exists()) {
                        if (!outDir.mkdirs()) {
                            Log.severe("Cloud not create " + outDir.getName());
                        }
                    }
                }

                try {
                    if (outFile.exists() && !replace) {
                        Log.warning("Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
                    } else {
                        OutputStream out = new FileOutputStream(outFile);
                        byte[] buf = new byte[1024];

                        int len;
                        while((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }

                        out.close();
                        in.close();
                    }
                } catch (IOException e) {
                    Log.severe("Could not save " + outFile.getName() + " to " + outFile);
                }
            }
        } else {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }
    }

}