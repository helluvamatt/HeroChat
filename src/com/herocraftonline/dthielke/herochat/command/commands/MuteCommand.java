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

public class MuteCommand extends BaseCommand {

    public MuteCommand(HeroChat plugin) {
        super(plugin);
        name = "Mute";
        description = "Prevents a player from speaking in a channel";
        usage = "§e/ch mute §9<channel> §8[player] §eOR /mute §9<channel> §8[player]";
        minArgs = 1;
        maxArgs = 2;
        identifiers.add("ch mute");
        identifiers.add("mute");
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
            displayMuteList(sender, channel);
            return;
        }

        Player target = plugin.getServer().getPlayer(args[1]);
        if (target == null) {
            Messaging.send(sender, "Player not found.");
            return;
        }

        Chatter targetChatter = plugin.getChatterManager().getChatter(target);
        if (permissions.hasPermission(target, Permission.ADMIN_IMMUNITY) || channel.isModerator(targetChatter)) {
            Messaging.send(sender, "You can't mute this player.");
            return;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            Chatter playerChatter = plugin.getChatterManager().getChatter(player);

            boolean mutePerm = permissions.hasPermission(player, Permission.MUTE);
            boolean adminMutePerm = permissions.hasPermission(player, Permission.ADMIN_MUTE);
            boolean mod = channel.isModerator(playerChatter);

            if ((!mutePerm && !adminMutePerm) || (mutePerm && !adminMutePerm && !mod)) {
                Messaging.send(player, "Insufficient permission.");
                return;
            }
        }

        if (channel.isMuted(targetChatter)) {
            channel.unmuteChatter(targetChatter, true);
            Messaging.send(sender, "$1 can now speak in $2.", target.getName(), channel.getName());
        } else {
            channel.muteChatter(targetChatter, true);
            Messaging.send(sender, "$1 can no longer speak in $2.", target.getName(), channel.getName());
        }
    }

    private void displayMuteList(CommandSender sender, Channel channel) {
        String[] mutes = channel.getMutes();
        if (mutes.length == 0) {
            Messaging.send(sender, "No one is muted in $1.", channel.getName());
        } else {
            String muteListMsg;
            muteListMsg = "Mutes ($1): ";
            for (String s : mutes) {
                muteListMsg += s + ", ";
            }
            muteListMsg = muteListMsg.substring(0, muteListMsg.length() - 2);
            Messaging.send(sender, muteListMsg, channel.getName());
        }
    }
}
