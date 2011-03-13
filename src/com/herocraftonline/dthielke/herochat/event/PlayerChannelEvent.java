package com.herocraftonline.dthielke.herochat.event;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;

import com.herocraftonline.dthielke.herochat.channels.Channel;

@SuppressWarnings("serial")
public class PlayerChannelEvent extends PlayerEvent {
    protected Channel channel;
    protected String message;
    protected String leader;
    protected List<Player> recipients;

    public PlayerChannelEvent(final Event.Type type, final Player who, final Channel where) {
        super(type, who);
        channel = where;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public List<Player> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<Player> recipients) {
        this.recipients = recipients;
    }
}