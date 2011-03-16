/**
 * Copyright (C) 2011 DThielke <dave.thielke@gmail.com>
 * 
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to
 * Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 **/

package com.herocraftonline.dthielke.herochat.util;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.entity.Player;

import com.herocraftonline.dthielke.herochat.HeroChat;
import com.herocraftonline.dthielke.herochat.HeroChat.ChatColor;
import com.herocraftonline.dthielke.herochat.channels.Channel;

public class Messaging {
    private static final String[] HEALTH_COLORS = { "§0", "§4", "§6", "§e", "§2" };
    private static char[] alternates = { '!', '@', '$', '|', '0', '1', '4', '3' };
    private static char[] actuals = { 'i', 'a', 's', 'l', 'o', 'l', 'a', 'e' };

    public static String format(HeroChat plugin, Channel channel, String format, String name, String msg, boolean sentByPlayer) {
        msg = msg.replaceAll("§[0-9a-f]", "");
        List<String> censors = plugin.getCensors();
        for (String censor : censors) {
            String[] split = censor.split(";", 2);
            if (split.length == 1) {
                msg = censorMsg(msg, censor, false, "");
            } else {
                msg = censorMsg(msg, split[0], true, split[1]);
            }
        }
        String leader = createLeader(plugin, channel, format, name, msg, sentByPlayer);
        return leader + msg;
    }

    private static String censorMsg(String msg, String censor, boolean customReplacement, String replacement) {
        Pattern pattern = Pattern.compile(censor, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(msg);
        while (matcher.find()) {
            String match = matcher.group();
            if (!customReplacement) {
                char[] replaceChars = new char[match.length()];
                Arrays.fill(replaceChars, '*');
                replacement = new String(replaceChars);
            }
            msg = msg.substring(0, matcher.start()) + replacement + msg.substring(matcher.end());
            matcher = pattern.matcher(msg);
        }

        String mod = msg;
        for (int i = 0; i < alternates.length; i++) {
            mod = mod.replace(alternates[i], actuals[i]);
        }
        matcher = pattern.matcher(mod);
        while (matcher.find()) {
            String match = matcher.group();
            if (!customReplacement) {
                char[] replaceChars = new char[match.length()];
                Arrays.fill(replaceChars, '*');
                replacement = new String(replaceChars);
            }
            msg = msg.substring(0, matcher.start()) + replacement + msg.substring(matcher.end());
            matcher = pattern.matcher(msg);
        }
        return msg;
    }

    private static String createLeader(HeroChat plugin, Channel channel, String format, String name, String msg, boolean sentByPlayer) {
        String prefix = "";
        String suffix = "";
        String world = "";
        String healthBar = "";
        if (sentByPlayer) {
            try {
                Player sender = plugin.getServer().getPlayer(name);
                if (sender != null) {
                    prefix = plugin.getPermissions().getPrefix(sender);
                    suffix = plugin.getPermissions().getSuffix(sender);
                    world = sender.getWorld().getName();
                    name = sender.getDisplayName();
                    healthBar = createHealthBar(sender);
                }
            } catch (Exception e) {
                e.printStackTrace();
                plugin.log(Level.WARNING, "Error encountered while fetching prefixes/suffixes from Permissions. Is Permissions properly configured and up to date?");
            }
        }

        String leader = format;
        leader = leader.replaceAll("\\{default\\}", plugin.getChannelManager().getDefaultMsgFormat());
        leader = leader.replaceAll("&([0-9a-f])", "§$1");
        leader = leader.replaceAll("\\{prefix\\}", prefix);
        leader = leader.replaceAll("\\{suffix\\}", suffix);
        leader = leader.replaceAll("\\{nick\\}", channel.getNick());
        leader = leader.replaceAll("\\{name\\}", channel.getName());
        leader = leader.replaceAll("\\{player\\}", name);
        leader = leader.replaceAll("\\{healthbar\\}", healthBar);
        leader = leader.replaceAll("\\{color\\}", channel.getColor().str);
        leader = leader.replaceAll("\\{color.CHANNEL\\}", channel.getColor().str);
        leader = leader.replaceAll("\\{world\\}", world);
        Matcher matcher = Pattern.compile("\\{color.[a-zA-Z_]+\\}").matcher(leader);
        while (matcher.find()) {
            String match = matcher.group();
            String colorString = match.substring(7, match.length() - 1);
            leader = leader.replaceAll("\\Q" + match + "\\E", ChatColor.valueOf(colorString).str);
        }

        return leader;
    }

    private static String createHealthBar(Player player) {
        int health = player.getHealth();
        if (health < 0) {
            health = 0;
        }
        int fullBars = health / 4;
        int remainder = health % 4;
        String healthBar = "";
        for (int i = 0; i < fullBars; i++) {
            healthBar += HEALTH_COLORS[4] + "|";
        }
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
}
