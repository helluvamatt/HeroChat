package com.herocraftonline.dthielke.herochat.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event.Type;

import com.herocraftonline.dthielke.herochat.channels.Channel;

public class PlayerLeaveChannelEvent extends PlayerChannelEvent {

    public PlayerLeaveChannelEvent(Type type, Player who, Channel where) {
        super(type, who, where);
        // TODO Auto-generated constructor stub
    }

}
