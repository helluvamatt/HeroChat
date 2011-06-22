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

public class KickCommand extends BaseCommand {

    public KickCommand(HeroChat plugin) {
        super(plugin);
        name = "Kick";
        description = "Removes a player from a channel";
        usage = "§e/ch kick §9<channel> <player>";
        minArgs = 2;
        maxArgs = 2;
        identifiers.add("ch kick");
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

        Player target = plugin.getServer().getPlayer(args[1]);
        if (target == null) {
            Messaging.send(sender, "Player not found.");
            return;
        }

        Chatter targetChatter = plugin.getChatterManager().getChatter(target);
        if (permissions.hasPermission(target, Permission.ADMIN_IMMUNITY) || channel.isModerator(targetChatter)) {
            Messaging.send(sender, "You can't kick this player.");
            return;
        }

        if (!channel.hasChatter(targetChatter)) {
            Messaging.send(sender, "Player not in the channel.");
            return;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            Chatter playerChatter = plugin.getChatterManager().getChatter(player);

            boolean kickPerm = permissions.hasPermission(player, Permission.KICK);
            boolean adminKickPerm = permissions.hasPermission(player, Permission.ADMIN_KICK);
            boolean mod = channel.isModerator(playerChatter);

            if ((!kickPerm && !adminKickPerm) || (kickPerm && !adminKickPerm && !mod)) {
                Messaging.send(player, "Insufficient permission.");
                return;
            }
        }

        channel.removeChatter(targetChatter, true);

        Messaging.send(sender, "Kicked $1 from $2.", target.getName(), channel.getName());
    }
}
