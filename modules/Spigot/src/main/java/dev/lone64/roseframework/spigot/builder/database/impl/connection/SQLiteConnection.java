package dev.lone64.roseframework.spigot.builder.database.impl.connection;

import dev.lone64.roseframework.spigot.builder.database.sql.SQLConnection;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.DriverManager;

public class SQLiteConnection extends SQLConnection {

    public SQLiteConnection(JavaPlugin plugin, String fileName) {
        this(plugin, null, fileName);
    }

    @SneakyThrows
    public SQLiteConnection(JavaPlugin plugin, String dir, String fileName) {
        var logger = plugin.getLogger();
        if (isConnection()) return;

        var dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            if (!dataFolder.mkdirs()) {
                logger.severe("Cloud not create folder to '%s'.".formatted(dataFolder.getPath().replace("\\", "/")));
            }
        }

        var dirFolder = dataFolder;
        if (dir != null && !dir.isEmpty()) {
            dirFolder = new File(dataFolder, dir);
            if (!dirFolder.exists()) {
                if (!dirFolder.mkdirs()) {
                    logger.severe("Cloud not create folder to '%s'.".formatted(dirFolder.getPath().replace("\\", "/")));
                }
            }
        }

        var dbFile = new File(dirFolder, fileName);
        if (!dbFile.exists()) {
            if (!dbFile.createNewFile()) {
                logger.severe("Cloud not create file to '%s'.".formatted(dbFile.getPath().replace("\\", "/")));
            }
        }

        try {
            Class.forName("org.sqlite.JDBC");
            setConnection(DriverManager.getConnection("jdbc:sqlite:" + dbFile));
        } catch (ClassNotFoundException e) {
            logger.severe("Failed to connect to SQLite server.");
        }
    }

}