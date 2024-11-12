package dev.lone64.roseframework.spigot.listener;

import dev.lone64.roseframework.spigot.SpigotInitializer;
import dev.lone64.roseframework.spigot.builder.input.InputBuilder;
import dev.lone64.roseframework.spigot.builder.inventory.impl.BukkitInventory;
import dev.lone64.roseframework.spigot.builder.inventory.impl.CustomInventory;
import dev.lone64.roseframework.spigot.bukkit.BukkitScheduler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SpigotListener implements Listener {

    @EventHandler(priority= EventPriority.HIGH, ignoreCancelled=true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (InputBuilder.is(event.getPlayer())) {
            InputBuilder.remove(event.getPlayer());
        }
    }

    @EventHandler(priority= EventPriority.HIGH, ignoreCancelled=true)
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        var player = event.getPlayer();
        for (var label : SpigotInitializer.get().getCommandManager().getRegisteredCommands().keySet()) {
            var pluginCommand = SpigotInitializer.get().getCommandManager().getRegisteredCommands().get(label);
            if (pluginCommand != null && pluginCommand.getName().toLowerCase().contains(event.getMessage().replace("/", ""))) {
                if (pluginCommand.getPermission() != null && !pluginCommand.getPermission().isEmpty()) {
                    if (!player.hasPermission(pluginCommand.getPermission())) {
                        if (pluginCommand.getPermissionMessage() != null && !pluginCommand.getPermissionMessage().isEmpty()) {
                            event.setCancelled(true);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginCommand.getPermissionMessage()));
                        } else {
                            event.setCancelled(true);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have permission to execute this command!"));
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority= EventPriority.HIGH, ignoreCancelled=true)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        var listener = InputBuilder.get(event.getPlayer());
        if (InputBuilder.is(event.getPlayer())) {
            event.setCancelled(true);
            if (listener.onInit(event.getPlayer(), event.getMessage())) {
                BukkitScheduler.sync(SpigotInitializer.get(), () -> InputBuilder.remove(event.getPlayer()));
            }
        }
    }

    @EventHandler(priority= EventPriority.HIGH, ignoreCancelled=true)
    public void onPlayerMove(PlayerMoveEvent event) {
        var listener = InputBuilder.get(event.getPlayer());
        if (InputBuilder.is(event.getPlayer())) {
            var location = event.getPlayer().getLocation().clone().subtract(0, 1, 0);
            if (location.getBlock().getType() == Material.AIR && listener.onCancel(event.getPlayer())) {
                InputBuilder.remove(event.getPlayer());
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            if (event.getInventory().getHolder() instanceof BukkitInventory inventory) {
                inventory.onClick(event);
                inventory.onClick(event, player);
            }

            if (event.getInventory().getHolder() instanceof CustomInventory inventory) {
                inventory.onClick(event);
                inventory.onClick(event, player);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player player) {
            if (event.getInventory().getHolder() instanceof BukkitInventory inventory) {
                inventory.onClose(event);
                inventory.onClose(event, player);
            }

            if (event.getInventory().getHolder() instanceof CustomInventory inventory) {
                inventory.onClose(event);
                inventory.onClose(event, player);
            }
        }
    }

}