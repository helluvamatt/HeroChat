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

public class GMuteCommand extends BaseCommand {

    public GMuteCommand(HeroChat plugin) {
        super(plugin);
        name = "Global Mute";
        description = "Prevents a player from speaking in any channel";
        usage = "§e/ch gmute §8[player]";
        minArgs = 1;
        maxArgs = 1;
        identifiers.add("ch gmute");
        identifiers.add("gmute");
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
        if (permissions.hasPermission(target, Permission.ADMIN_IMMUNITY)) {
            Messaging.send(sender, "You can't mute this player.");
            return;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!permissions.hasPermission(player, Permission.ADMIN_GMUTE)) {
                Messaging.send(player, "Insufficient permission.");
                return;
            }
        }

        if (targetChatter.isMuted()) {
            targetChatter.setMuted(false);
            Messaging.send(sender, "Unmuted $1.", target.getName(), channel.getName());
        } else {
            targetChatter.setMuted(true);
            Messaging.send(sender, "Muted $1.", target.getName(), channel.getName());
        }
    }

}
