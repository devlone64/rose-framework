package dev.lone64.roseframework.spigot.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class BukkitScheduler {

    public static BukkitTask async(JavaPlugin plugin, Runnable runnable) {
        return Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public static BukkitTask sync(JavaPlugin plugin, Runnable runnable) {
        return Bukkit.getScheduler().runTask(plugin, runnable);
    }

    public static BukkitTask asyncLater(JavaPlugin plugin, Runnable runnable, long ticks) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, ticks);
    }

    public static BukkitTask asyncTimer(JavaPlugin plugin, Runnable runnable, long delay, long ticks) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, ticks);
    }

    public static BukkitTask syncLater(JavaPlugin plugin, Runnable runnable, long ticks) {
        return Bukkit.getScheduler().runTaskLater(plugin, runnable, ticks);
    }

    public static BukkitTask syncTimer(JavaPlugin plugin, Runnable runnable, long delay, long ticks) {
        return Bukkit.getScheduler().runTaskTimer(plugin, runnable, delay, ticks);
    }

    public static boolean isQueued(int taskId) {
        return Bukkit.getScheduler().isQueued(taskId);
    }

    public static boolean isCurrentlyRunning(int taskId) {
        return Bukkit.getScheduler().isCurrentlyRunning(taskId);
    }

}