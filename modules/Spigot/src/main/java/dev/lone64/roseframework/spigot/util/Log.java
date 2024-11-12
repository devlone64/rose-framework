package dev.lone64.roseframework.spigot.util;

import dev.lone64.roseframework.spigot.SpigotInitializer;

import java.util.logging.Level;

public class Log {

    public static void info(String message) {
        SpigotInitializer.get().getLogger().info(message);
    }

    public static void warning(String message) {
        SpigotInitializer.get().getLogger().warning(message);
    }

    public static void severe(String message) {
        SpigotInitializer.get().getLogger().severe(message);
    }

    public static void log(Level level, String message) {
        SpigotInitializer.get().getLogger().log(level, message);
    }

    public static void log(Level level, String message, Throwable throwable) {
        SpigotInitializer.get().getLogger().log(level, message, throwable);
    }

}