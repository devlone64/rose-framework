package dev.lone64.roseframework.spigot.builder.language.data;

import dev.lone64.roseframework.spigot.builder.config.yaml.YamlConfigBuilder;
import dev.lone64.roseframework.spigot.builder.language.LanguageBuilder;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Language extends YamlConfigBuilder {

    private final boolean enabled;
    private final String name;

    public Language(JavaPlugin plugin, LanguageBuilder lang, String name) {
        this(plugin, lang, true, name);
    }

    public Language(JavaPlugin plugin, LanguageBuilder lang, boolean enabled, String name) {
        super(plugin, lang.getFolderName(), "messages_%s.yml".formatted(name));

        this.enabled = enabled;
        this.name = name;
    }

}