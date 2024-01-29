package lc.survival.commands;

import lc.survival.gui.GUIManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MissionsCommands implements CommandExecutor, TabCompleter {


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(commandSender instanceof Player){
            if(args.length == 0){
                GUIManager.showMainGUI((Player) commandSender);
            }else{
                if(args[0].equalsIgnoreCase("cazeria")) GUIManager.showCazeriaGUI((Player) commandSender);
                if(args[0].equalsIgnoreCase("asesino")) GUIManager.showAsesinoGUI((Player) commandSender);
                if(args[0].equalsIgnoreCase("mineria")) GUIManager.showMineriaGUI((Player) commandSender);

            }
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> l = new ArrayList<>();
        if(args.length == 1){
            l.add("asesino");
            l.add("cazeria");
            l.add("mineria");
        }
        return l;
    }
}
