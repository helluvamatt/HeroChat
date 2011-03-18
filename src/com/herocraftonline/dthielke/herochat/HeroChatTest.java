package com.herocraftonline.dthielke.herochat;

import org.bukkit.command.ConsoleCommandSender;

import com.herocraftonline.dthielke.herochat.command.CommandManager;
import com.herocraftonline.dthielke.herochat.command.commands.QuickMsgCommand;

public class HeroChatTest {

    public static void main(String[] args) {
        HeroChat plugin = new HeroChat();
        CommandManager commandManager = new CommandManager();
        commandManager.addCommand(new QuickMsgCommand(plugin));
        commandManager.dispatch(new ConsoleCommandSender(plugin.getServer()), null, "qm", new String[]{"g", "?"});
    }

}
