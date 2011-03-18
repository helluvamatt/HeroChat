package com.herocraftonline.dthielke.herochat.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.herocraftonline.dthielke.herochat.HeroChat;
import com.herocraftonline.dthielke.herochat.channels.Channel;
import com.herocraftonline.dthielke.herochat.channels.ChannelManager;
import com.herocraftonline.dthielke.herochat.command.BaseCommand;

public class ToggleCommand extends BaseCommand {

    public ToggleCommand(HeroChat plugin) {
        super(plugin);
        name = "Toggle";
        description = "Temporarily enables or disables a channel";
        usage = "§e/ch toggle §9<channel>";
        minArgs = 1;
        maxArgs = 1;
        identifiers.add("ch toggle");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ChannelManager cm = plugin.getChannelManager();
        Channel channel = cm.getChannel(args[0]);
        if (channel != null) {
            if (sender instanceof Player) {
                Player muter = (Player) sender;
                if (muter == null || plugin.getPermissions().isAdmin(muter) || channel.getModerators().contains(muter.getName())) {
                    if (channel.isEnabled()) {
                        channel.setEnabled(false);
                        sender.sendMessage(plugin.getTag() + "§cDisabled " + channel.getCName());
                    } else {
                        channel.setEnabled(true);
                        sender.sendMessage(plugin.getTag() + "§cEnabled " + channel.getCName());
                    }
                } else {
                    muter.sendMessage(plugin.getTag() + "§cYou do not have sufficient permission");
                }
            } else {
                sender.sendMessage(plugin.getTag() + "§cYou must be a player to use this command");
            }
        } else {
            sender.sendMessage(plugin.getTag() + "§cChannel not found");
        }
    }

}
