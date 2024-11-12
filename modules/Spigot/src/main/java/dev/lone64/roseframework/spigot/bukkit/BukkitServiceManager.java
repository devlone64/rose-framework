package dev.lone64.roseframework.spigot.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class BukkitServiceManager {

    public static <T> RegisteredServiceProvider<T> getRegistration(Class<T> serviceClass) {
        return Bukkit.getServicesManager().getRegistration(serviceClass);
    }

}