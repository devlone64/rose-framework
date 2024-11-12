package dev.lone64.roseframework.spigot.builder.database.impl.connection;

import dev.lone64.roseframework.spigot.builder.database.sql.SQLConnection;
import dev.lone64.roseframework.spigot.bukkit.BukkitPluginManager;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.DriverManager;

public class MySQLConnection extends SQLConnection {

    @SneakyThrows
    public MySQLConnection(JavaPlugin plugin, String host, String port, String name, String username, String password) {
        var logger = plugin.getLogger();
        if (isConnection()) return;

        try {
            setConnection(DriverManager.getConnection("jdbc:mysql://%s:%s/%s".formatted(host, port, name), username, password));
        } catch (Exception exception) {
            BukkitPluginManager.disablePlugin(plugin);
            logger.severe("Failed to connect to MySQL server. are the credentials correct?");
        }
    }

}