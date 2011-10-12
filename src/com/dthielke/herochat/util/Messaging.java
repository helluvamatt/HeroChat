package com.dthielke.herochat.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class Messaging {

    public static void broadcast(String msg, Object... params) {
        Bukkit.getServer().broadcastMessage(parameterizeMessage(msg, params));
    }
    
    public static void send(CommandSender player, String msg, Object... params) {
        player.sendMessage(parameterizeMessage(msg, params));
    }

    private static String parameterizeMessage(String msg, Object... params) {
        msg = ChatColor.RED + "HeroChat: " + ChatColor.GRAY + msg;
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                msg = msg.replace("$" + (i + 1), ChatColor.WHITE + params[i].toString() + ChatColor.GRAY);
            }
        }
        return msg;
    }
}
