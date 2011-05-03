package com.herocraftonline.dthielke.herochat.event;

import org.bukkit.entity.Player;

import com.herocraftonline.dthielke.herochat.channels.ChannelOld;

@SuppressWarnings("serial")
public class ChannelJoinEvent extends ChannelEvent {

    protected Player joiner;
    
    public ChannelJoinEvent(Type type, ChannelOld channel, Player joiner) {
        super(ChannelJoinEvent.class.getSimpleName(), channel);
        this.joiner = joiner;
    }

    public Player getJoiner() {
        return joiner;
    }

    public void setJoiner(Player joiner) {
        this.joiner = joiner;
    }

}
