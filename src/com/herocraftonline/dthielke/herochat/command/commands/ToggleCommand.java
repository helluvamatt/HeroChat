package com.herocraftonline.dthielke.herochat.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.herocraftonline.dthielke.herochat.HeroChat;
import com.herocraftonline.dthielke.herochat.channels.Channel;
import com.herocraftonline.dthielke.herochat.channels.ChannelManager;
import com.herocraftonline.dthielke.herochat.command.BaseCommand;
import com.herocraftonline.dthielke.herochat.util.Messaging;
import com.herocraftonline.dthielke.herochat.util.PermissionManager;
import com.herocraftonline.dthielke.herochat.util.PermissionManager.Permission;

public class ToggleCommand extends BaseCommand {

    public ToggleCommand(HeroChat plugin) {
        super(plugin);
        setName("Toggle");
        setDescription("Temporarily enables or disables a channel");
        setUsage("§e/ch toggle §8[channel]");
        setMinArgs(0);
        setMaxArgs(1);
        getIdentifiers().add("ch toggle");
        getNotes().add("§cNote: §eIf no channel is provided, all channels are toggled");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ChannelManager channelManager = plugin.getChannelManager();

        if (sender instanceof Player) {
            Player player = (Player) sender;
            PermissionManager permissions = plugin.getPermissionManager();

            boolean togglePerm = permissions.hasPermission(player, Permission.ADMIN_TOGGLE);

            if (!togglePerm) {
                Messaging.send(player, "Insufficient permission.");
                return;
            }
        }

        if (args.length == 0) {
            if (channelManager.getDefaultChannel().isEnabled()) {
                channelManager.disableChannels();
                Messaging.send(sender, "Disabled all channels.");
            } else {
                channelManager.enableChannels();
                Messaging.send(sender, "Enabled all channels.");
            }
        } else {
            Channel channel = channelManager.getChannel(args[0]);

            if (channel == null) {
                Messaging.send(sender, "Channel not found.");
                return;
            }

            if (channel.isEnabled()) {
                channel.setEnabled(false);
                Messaging.send(sender, "Disabled $1.", channel.getName());
            } else {
                channel.setEnabled(true);
                Messaging.send(sender, "Enabled $1.", channel.getName());
            }
        }
    }

}
