package dev.lone64.roseframework.spigot;

import dev.lone64.roseframework.spigot.armorequip.Types;
import dev.lone64.roseframework.spigot.armorequip.listener.ArmorerListener;
import dev.lone64.roseframework.spigot.armorequip.listener.DispenserArmorListener;
import dev.lone64.roseframework.spigot.command.manager.CommandManager;
import dev.lone64.roseframework.spigot.common.PluginInitializer;
import dev.lone64.roseframework.spigot.listener.SpigotListener;
import dev.lone64.roseframework.spigot.util.Log;
import dev.lone64.roseframework.spigot.util.version.VersionUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class SpigotInitializer extends JavaPlugin implements PluginInitializer {

    private static SpigotInitializer initializer;
    
    @Setter @Getter private static String prefix;
    
    public final CommandManager commandManager = new CommandManager(this);

    @Override
    public void onLoad() {
        initializer = this;
        prefix = "<GRADIENT:FF9633>{PREFIX}</GRADIENT:FFD633>&r";
        load();
    }

    @Override
    public void onEnable() {
        if (VersionUtil.isSupportVersion()) {
            Bukkit.getPluginManager().disablePlugin(this);
            Log.severe("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            Log.severe("%s 플러그인은 %s 버전을 지원하지 않습니다.".formatted(getName(), VersionUtil.getVersion()));
            Log.severe("올바른 버전에서 플러그인을 사용해주세요.");
            Log.severe("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            return;
        }

        Bukkit.getPluginManager().registerEvents(new SpigotListener(), this);
        Bukkit.getPluginManager().registerEvents(new DispenserArmorListener(), this);
        Bukkit.getPluginManager().registerEvents(new ArmorerListener(Types.getTypeList()), this);
        
        enable();
    }

    @Override
    public void onDisable() {
        disable();
    }

    public static SpigotInitializer get() {
        return initializer;
    }

}