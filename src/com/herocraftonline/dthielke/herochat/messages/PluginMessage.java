package com.herocraftonline.dthielke.herochat.messages;

import java.util.Set;

import org.bukkit.plugin.java.JavaPlugin;

import com.herocraftonline.dthielke.herochat.channels.Channel;
import com.herocraftonline.dthielke.herochat.chatters.Chatter;

public class PluginMessage extends Message {

    private final JavaPlugin sender;

    public PluginMessage(String message, String format, Channel channel, Set<Chatter> recipients, JavaPlugin sender) {
        super(message, format, channel, recipients);
        this.sender = sender;
    }

    public JavaPlugin getSender() {
        return sender;
    }

}
