package lc.survival.commands;

import lc.survival.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class LCReload implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!commandSender.hasPermission("lcsurvival.reload")){
            MessageUtils.sendMessage(commandSender, "&8[&bSurvival&8] &cNo tienes acceso a este comando.");
            return true;
        }
        MessageUtils.sendMessage(commandSender, "&8[&bSurvival&8] &cNo tienes acceso a este comando.");
        return true;
    }
}
