package dev.lone64.roseframework.plugin.command;

import dev.lone64.roseframework.plugin.RoseLib;
import dev.lone64.roseframework.spigot.command.annotation.command.HelpCommand;
import dev.lone64.roseframework.spigot.command.annotation.command.MainCommand;
import dev.lone64.roseframework.spigot.util.text.Text;
import org.bukkit.entity.Player;

@MainCommand(
        label = "roselib",
        description = "RoseLib 플러그인의 전용 명령어입니다."
)
public class RoseCommand {

    @HelpCommand()
    public boolean onDefault(Player player) {
        player.sendMessage(Text.from("<GRADIENT:ffffff>&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━</GRADIENT:555555>"));
        player.sendMessage(Text.from("%s &f이름 : &b%s".formatted(RoseLib.getPrefix(), RoseLib.get().getName())));
        player.sendMessage(Text.from("%s &f버전 : &b%s".formatted(RoseLib.getPrefix(), RoseLib.get().getDescription().getVersion())));
        player.sendMessage(Text.from("%s &f다운로드 : &b%s".formatted(RoseLib.getPrefix(), "https://github.com/devlone64")));
        player.sendMessage(Text.from("<GRADIENT:ffffff>&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━</GRADIENT:555555>"));
        return true;
    }

}