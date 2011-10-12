package com.dthielke.herochat;

import java.util.List;

import org.bukkit.entity.Player;

public interface Chatter {

    public List<Channel> getChannels();

    public Player getPlayer();

    public String getName();
    
    boolean hasChannel(Channel channel);

    public boolean addChannel(Channel channel);

    public boolean removeChannel(Channel channel);

    public Channel getActiveChannel();

    public boolean setActiveChannel(Channel channel);

    public Result canJoin(Channel channel);

    public Result canLeave(Channel channel);

    public Result canSpeak(Channel channel);

    public Result canKick(Channel channel);

    public Result canBan(Channel channel);

    public Result canMute(Channel channel);

    public enum Result {
        NO_PERMISSION,
        REDUNDANT,
        ALLOWED;
    }

}
