package com.herocraftonline.dthielke.herochat.messages;

import java.util.Set;

import com.herocraftonline.dthielke.herochat.channels.Channel;
import com.herocraftonline.dthielke.herochat.chatters.Chatter;

public class PlayerMessage extends Message {

    private final Chatter sender;
    private final String prefix;
    private final String suffix;
    private final String group;
    private final String groupPrefix;
    private final String groupSuffix;

    public PlayerMessage(String message, String format, Channel channel, Set<Chatter> recipients, Chatter sender, String prefix, String suffix, String group, String groupPrefix, String groupSuffix) {
        super(message, format, channel, recipients);
        this.sender = sender;
        this.prefix = prefix;
        this.suffix = suffix;
        this.group = group;
        this.groupPrefix = groupPrefix;
        this.groupSuffix = groupSuffix;
    }

    public Chatter getSender() {
        return sender;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getGroup() {
        return group;
    }

    public String getGroupPrefix() {
        return groupPrefix;
    }

    public String getGroupSuffix() {
        return groupSuffix;
    }

}
