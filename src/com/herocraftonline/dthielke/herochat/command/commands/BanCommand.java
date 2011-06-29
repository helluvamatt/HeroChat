/**
 * Copyright (C) 2011 DThielke <dave.thielke@gmail.com>
 * 
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to
 * Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 **/

package com.herocraftonline.dthielke.herochat.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.herocraftonline.dthielke.herochat.HeroChat;
import com.herocraftonline.dthielke.herochat.channels.Channel;
import com.herocraftonline.dthielke.herochat.channels.ChannelManager;
import com.herocraftonline.dthielke.herochat.chatters.Chatter;
import com.herocraftonline.dthielke.herochat.command.BaseCommand;
import com.herocraftonline.dthielke.herochat.util.Messaging;
import com.herocraftonline.dthielke.herochat.util.PermissionManager;
import com.herocraftonline.dthielke.herochat.util.PermissionManager.Permission;

public class BanCommand extends BaseCommand {

    public BanCommand(HeroChat plugin) {
        super(plugin);
        name = "Ban";
        description = "Bans a player from a channel";
        usage = "§e/ch ban §9<channel> <player>";
        minArgs = 1;
        maxArgs = 2;
        identifiers.add("ch ban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        PermissionManager permissions = plugin.getPermissionManager();
        ChannelManager channelManager = plugin.getChannelManager();
        Channel channel = channelManager.getChannel(args[0]);

        if (channel == null) {
            Messaging.send(sender, "Channel not found.");
            return;
        }

        if (args.length == 1) {
            displayBanList(sender, channel);
            return;
        }

        Player target = plugin.getServer().getPlayer(args[1]);
        if (target == null) {
            Messaging.send(sender, "Player not found.");
            return;
        }

        Chatter targetChatter = plugin.getChatterManager().getChatter(target);
        if (permissions.hasPermission(target, Permission.ADMIN_IMMUNITY) || channel.isModerator(targetChatter)) {
            Messaging.send(sender, "You can't ban this player.");
            return;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            Chatter playerChatter = plugin.getChatterManager().getChatter(player);

            boolean banPerm = permissions.hasPermission(player, Permission.BAN);
            boolean adminBanPerm = permissions.hasPermission(player, Permission.ADMIN_BAN);
            boolean mod = channel.isModerator(playerChatter);

            if ((!banPerm && !adminBanPerm) || (banPerm && !adminBanPerm && !mod)) {
                Messaging.send(player, "Insufficient permission.");
                return;
            }
        }

        if (channel.isBanned(targetChatter)) {
            channel.unbanChatter(targetChatter, true);
            Messaging.send(sender, "Unbanned $1 from $2.", target.getName(), channel.getName());
        } else {
            channel.banChatter(targetChatter, true);
            Messaging.send(sender, "Banned $1 from $2.", target.getName(), channel.getName());
        }
    }

    private void displayBanList(CommandSender sender, Channel channel) {
        String[] bans = channel.getBans();
        if (bans.length == 0) {
            Messaging.send(sender, "No one is banned from $1.", channel.getName());
        } else {
            String banListMsg;
            banListMsg = "Bans ($1): ";
            for (String s : bans) {
                banListMsg += s + ", ";
            }
            banListMsg = banListMsg.substring(0, banListMsg.length() - 2);
            Messaging.send(sender, banListMsg, channel.getName());
        }
    }

}
