package com.herocraftonline.dthielke.herochat.event;

import org.bukkit.event.Event;

import com.herocraftonline.dthielke.herochat.channels.Channel;

@SuppressWarnings("serial")
public class ChannelEvent extends Event {
    protected Channel channel;
    protected boolean cancelled;

    public ChannelEvent(final String type, final Channel channel) {
        super(type);
        this.channel = channel;
        this.cancelled = false;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

}
