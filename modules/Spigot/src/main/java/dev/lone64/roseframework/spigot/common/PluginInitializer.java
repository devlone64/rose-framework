package dev.lone64.roseframework.spigot.common;

public interface PluginInitializer {
    default void load() { }
    default void enable() { }
    default void disable() { }
}