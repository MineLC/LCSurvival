package lc.survival.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
public class MessageUtils {

    public static String color(String str){
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static void sendMessage(CommandSender sender, String msg){
        if(sender instanceof Player){
            sender.sendMessage(PlaceholderAPI.setPlaceholders((Player) sender, color(msg)));
        }else{
            sender.sendMessage(PlaceholderAPI.setPlaceholders(null, color(msg)));
        }
    }
    public static void console(String msg){
       sendMessage(Bukkit.getConsoleSender(), msg);
    }
}
