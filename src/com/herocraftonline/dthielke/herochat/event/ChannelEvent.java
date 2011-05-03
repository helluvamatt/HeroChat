package com.herocraftonline.dthielke.herochat.event;

import org.bukkit.event.Event;

import com.herocraftonline.dthielke.herochat.channels.ChannelOld;

@SuppressWarnings("serial")
public class ChannelEvent extends Event {
    protected ChannelOld channel;
    protected boolean cancelled;

    public ChannelEvent(final String type, final ChannelOld channel) {
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

    public ChannelOld getChannel() {
        return channel;
    }

    public void setChannel(ChannelOld channel) {
        this.channel = channel;
    }

}
