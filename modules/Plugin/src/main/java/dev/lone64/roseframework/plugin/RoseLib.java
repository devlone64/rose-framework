package dev.lone64.roseframework.plugin;

import dev.lone64.roseframework.plugin.command.RoseCommand;
import dev.lone64.roseframework.spigot.SpigotInitializer;
import lombok.Getter;

public class RoseLib extends SpigotInitializer {

    @Getter private static RoseLib instance;

    @Override
    public void load() {
        instance = this;
        setPrefix(getPrefix().replace("{PREFIX}", "[ʀᴏѕᴇ ʟɪʙʀᴀʀʏ]"));
    }

    @Override
    public void enable() {
        this.commandManager.registerCommand(new RoseCommand());
    }

}