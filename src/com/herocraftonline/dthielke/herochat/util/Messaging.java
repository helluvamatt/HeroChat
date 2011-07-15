package com.herocraftonline.dthielke.herochat.util;

import java.util.Iterator;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.herocraftonline.dthielke.herochat.chatters.Chatter;
import com.herocraftonline.dthielke.herochat.messages.Message;
import com.herocraftonline.dthielke.herochat.messages.PlayerMessage;

public final class Messaging {
    private static final String[] HEALTH_COLORS = { "§0", "§4", "§6", "§e", "§2" };

    public static String stripColors(String input) {
        return input.replaceAll("[§&][0-9a-f]", "");
    }

    public static String format(Message msg) {
        // start with the format
        String formatted = msg.getFormat();

        // replace tags with data
        formatted = formatted.replace("{name}", msg.getChannel().getName());
        formatted = formatted.replace("{nick}", msg.getChannel().getNick());
        formatted = formatted.replace("{color}", msg.getChannel().getColor().toString());
        formatted = formatted.replace("{message}", msg.getMessage());
        
        Iterator<Chatter> iter = msg.getRecipients().iterator();
        if (iter.hasNext()) {
            formatted = formatted.replace("{receiver}", iter.next().getPlayer().getDisplayName());
        }

        // replace tags with player specific data
        if (msg instanceof PlayerMessage) {
            PlayerMessage pMsg = (PlayerMessage) msg;
            Player sender = pMsg.getSender().getPlayer();
            formatted = formatted.replace("{sender}", sender.getDisplayName());
            formatted = formatted.replace("{world}", sender.getWorld().getName());
            formatted = formatted.replace("{prefix}", pMsg.getPrefix());
            formatted = formatted.replace("{suffix}", pMsg.getSuffix());
            formatted = formatted.replace("{group}", pMsg.getGroup());
            formatted = formatted.replace("{groupPrefix}", pMsg.getGroupPrefix());
            formatted = formatted.replace("{groupSuffix}", pMsg.getGroupSuffix());
            formatted = formatted.replace("{health}", createHealthBar(sender.getHealth()));
        }

        // convert ampersand color codes
        formatted = formatted.replaceAll("&([0-9a-f])", "§$1");

        return formatted;
    }

    public static String createHealthBar(int health) {
        // make sure 0 <= health <= 20
        if (health < 0) {
            health = 0;
        }
        if (health > 20) {
            health = 20;
        }
        int fullBars = health / 4;
        int remainder = health % 4;
        String healthBar = "";

        // append full health bars
        for (int i = 0; i < fullBars; i++) {
            healthBar += HEALTH_COLORS[4] + "|";
        }

        // add remainder health bar with appropriate color
        int barsLeft = 5 - fullBars;
        if (barsLeft > 0) {
            healthBar += HEALTH_COLORS[remainder] + "|";
            barsLeft--;
            for (int i = 0; i < barsLeft; i++) {
                healthBar += HEALTH_COLORS[0] + "|";
            }
        }
        return healthBar;
    }

    public static void send(CommandSender player, String msg, Object... params) {
        player.sendMessage(parameterizeMessage(msg, params));
    }

    public static void broadcast(Server server, String msg, Object... params) {
        server.broadcastMessage(parameterizeMessage(msg, params));
    }

    private static String parameterizeMessage(String msg, Object... params) {
        msg = "§9HeroChat:§c " + msg;
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                msg = msg.replace("$" + (i + 1), "§f" + params[i].toString() + "§c");
            }
        }
        return msg;
    }

}
