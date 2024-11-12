package dev.lone64.roseframework.spigot.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

public class BukkitPluginManager {

    public static void enablePlugin(Plugin plugin) {
        Bukkit.getPluginManager().enablePlugin(plugin);
    }

    public static void disablePlugin(Plugin plugin) {
        Bukkit.getPluginManager().disablePlugin(plugin);
    }

    public static void register(Permission permission) {
        Bukkit.getPluginManager().addPermission(permission);
    }

}