package dev.lone64.roseframework.spigot.command.root;

import dev.lone64.roseframework.spigot.SpigotInitializer;
import dev.lone64.roseframework.spigot.command.annotation.Route;
import dev.lone64.roseframework.spigot.command.annotation.command.HelpCommand;
import dev.lone64.roseframework.spigot.command.annotation.command.MainCommand;
import dev.lone64.roseframework.spigot.command.annotation.command.SubCommand;
import dev.lone64.roseframework.spigot.command.annotation.command.TabCommand;
import dev.lone64.roseframework.spigot.command.extension.PageExt;
import dev.lone64.roseframework.spigot.command.manager.CommandManager;
import dev.lone64.roseframework.spigot.command.type.Path;
import dev.lone64.roseframework.spigot.command.util.CommandUtil;
import dev.lone64.roseframework.spigot.util.math.MathUtil;
import dev.lone64.roseframework.spigot.util.text.Text;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

@Getter
public class RootCommand extends PageExt {

    private final JavaPlugin plugin;

    private final Object command;
    private final Class<?> commandClass;
    private final MainCommand mainCommand;
    private final CommandManager commandManager;

    private final Map<String, SubCommand> userSubCommands = new HashMap<>();
    private final Map<String, SubCommand> adminSubCommands = new HashMap<>();

    private final Map<Path, String> commandRoutes = new HashMap<>();
    private final Map<Class<?>, Method> commandMethods = new HashMap<>();

    public RootCommand(JavaPlugin plugin, Object command, CommandManager commandManager) {
        this.plugin = plugin;

        this.command = command;
        this.commandClass = command.getClass();
        this.mainCommand = this.commandClass.getAnnotation(MainCommand.class);
        this.commandManager = commandManager;

        initMethods();
        initRoutes();
        for (var type : Path.values()) {
            if (!this.commandRoutes.containsKey(type)) {
                this.commandRoutes.put(type, type.getMessage());
            }
        }
    }

    public void setupPluginCommand(PluginCommand command) {
        command.setUsage(this.mainCommand.usage());
        command.setDescription(this.mainCommand.description());
        command.setAliases(Arrays.asList(this.mainCommand.aliases()));
        command.setPermission(this.mainCommand.permission());
        command.setPermissionMessage(this.commandRoutes.get(Path.PERMISSION));
        command.setExecutor(this::performCommand);
        command.setTabCompleter(this::performTabCompleter);
    }

    private boolean performCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!this.commandManager.getRegisteredCommands().containsKey(label)) {
            sender.sendMessage(Text.from(this.commandRoutes.get(Path.NOT_REGISTERED)));
            return true;
        } else if (!this.mainCommand.consoleAvailable() && sender instanceof ConsoleCommandSender) {
            sender.sendMessage(Text.from(this.commandRoutes.get(Path.CONSOLE)));
            return true;
        } else if (args.length == 0) {
            if (this.mainCommand.helpAvailable()) {
                onInit(sender, 1, this.commandManager.getTitle(), page ->
                        sender.sendMessage(Text.from(this.commandRoutes.get(Path.NOT_FOUND_PAGE))));
                return true;
            }
            var method = this.commandMethods.get(HelpCommand.class);
            if (this.commandMethods.containsKey(HelpCommand.class)) {
                try {
                    return CommandUtil.invokeBoolean(method, this.command, sender);
                } catch (Exception e) {
                    e.printStackTrace();
                    sender.sendMessage(Text.from("&c[%s] Command 모듈에서 오류가 발생했습니다.".formatted(SpigotInitializer.get())));
                    return true;
                }
            }
            sender.sendMessage(Text.from(this.commandRoutes.get(Path.NOT_SETUP_HELP)));
            return true;
        } else if (MathUtil.getIntegerOrNull(args[0]) != null && this.mainCommand.helpAvailable()) {
            var current = MathUtil.getIntegerOrElse(args[0], 1);
            onInit(sender, current, this.commandManager.getTitle(), page ->
                    sender.sendMessage(Text.from(this.commandRoutes.get(Path.NOT_FOUND_PAGE))));
            return true;
        }
        for (var method : this.commandClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(SubCommand.class)) {
                var subCommand = method.getAnnotation(SubCommand.class);
                if (args[0].equalsIgnoreCase(subCommand.label())) {
                    if (!subCommand.consoleAvailable() && sender instanceof ConsoleCommandSender) {
                        sender.sendMessage(Text.from(this.commandRoutes.get(Path.CONSOLE)));
                        return true;
                    } else if (!subCommand.permission().isEmpty() && !sender.hasPermission(subCommand.permission())) {
                        sender.sendMessage(Text.from(this.commandRoutes.get(Path.PERMISSION)));
                        return true;
                    }
                    try {
                        var arguments = new String[args.length - 1];
                        System.arraycopy(args, 1, arguments, 0, arguments.length);
                        return CommandUtil.invokeBoolean(method, this.command, sender, arguments);
                    } catch (Exception e) {
                        e.printStackTrace();
                        sender.sendMessage(Text.from("&c[%s] Command 모듈에서 오류가 발생했습니다.".formatted(SpigotInitializer.get())));
                        return true;
                    }
                }
            }
        }
        sender.sendMessage(Text.from(this.commandRoutes.get(Path.NOT_FOUND_CMD)));
        return true;
    }

    private List<String> performTabCompleter(CommandSender sender, Command command, String label, String[] args) {
        var method = this.commandMethods.get(TabCommand.class);
        if (this.commandMethods.containsKey(TabCommand.class)) {
            var tabCommand = method.getAnnotation(TabCommand.class);
            if (!tabCommand.consoleAvailable() && sender instanceof ConsoleCommandSender) {
                return new ArrayList<>();
            } else if (args.length == 1) {
                if (!this.mainCommand.permission().isEmpty() && !sender.hasPermission(this.mainCommand.permission())) {
                    return new ArrayList<>(userSubCommands.keySet());
                }
                var subCommands = new ArrayList<String>();
                if (!this.userSubCommands.isEmpty())
                    subCommands.addAll(new ArrayList<>(userSubCommands.keySet()));
                if (!this.adminSubCommands.isEmpty())
                    subCommands.addAll(new ArrayList<>(adminSubCommands.keySet()));
                return subCommands;
            }
            try {
                var index = args.length - 1;
                return CommandUtil.invokeList(method, this.command, sender, index, args[0], args);
            } catch (Exception e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        } else if (sender instanceof Player) {
            if (args.length == 1) {
                if (!this.mainCommand.permission().isEmpty() && !sender.hasPermission(this.mainCommand.permission())) {
                    return new ArrayList<>(userSubCommands.keySet());
                }
                var subCommands = new ArrayList<String>();
                if (!this.userSubCommands.isEmpty())
                    subCommands.addAll(new ArrayList<>(userSubCommands.keySet()));
                if (!this.adminSubCommands.isEmpty())
                    subCommands.addAll(new ArrayList<>(adminSubCommands.keySet()));
                return subCommands;
            }
        }
        return new ArrayList<>();
    }

    private void initMethods() {
        for (var method : this.commandClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(HelpCommand.class)) {
                this.commandMethods.put(HelpCommand.class, method);
            } else if (method.isAnnotationPresent(SubCommand.class)) {
                var cmd = method.getAnnotation(SubCommand.class);
                this.userSubCommands.put(cmd.label(), cmd);
                if (!cmd.permission().isEmpty()) {
                    this.adminSubCommands.put(cmd.label(), cmd);
                }
            } else if (method.isAnnotationPresent(TabCommand.class)) {
                this.commandMethods.put(TabCommand.class, method);
            }
        }
    }

    private void initRoutes() {
        for (var field : this.commandClass.getDeclaredFields()) {
            if (Modifier.isFinal(field.getModifiers())) {
                if (field.isAnnotationPresent(Route.class)) {
                    var route = field.getAnnotation(Route.class);
                    var message = CommandUtil.invokeString(field, this.command);
                    if (message != null) this.commandRoutes.put(route.path(), message);
                }
            }
        }
    }

    @Override
    protected List<String> getHelps(CommandSender sender) {
        var helps = new ArrayList<String>();
        for (var userCmd : this.userSubCommands.values()) {
            helps.add(this.commandManager.getFormat().replace("%NAME%", this.mainCommand.label())
                    .replace("%SUB_NAME%", userCmd.label()).replace("%DESCRIPTION%", userCmd.description()));
        }
        for (var adminCmd : this.adminSubCommands.values()) {
            helps.add(this.commandManager.getFormat().replace("%NAME%", this.mainCommand.label())
                    .replace("%SUB_NAME%", adminCmd.label()).replace("%DESCRIPTION%", adminCmd.description()));
        }
        return helps;
    }

}