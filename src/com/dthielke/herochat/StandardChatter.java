package com.dthielke.herochat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.dthielke.herochat.StandardChannel.ChannelPermission;

public class StandardChatter implements Chatter {

    private final Player player;
    private List<Channel> channels = new ArrayList<Channel>();

    public StandardChatter(Player player) {
        this.player = player;
    }

    @Override
    public List<Channel> getChannels() {
        return channels;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public Result canJoin(Channel channel) {
        if (channel.isMember(this))
            return Result.REDUNDANT;

        if (!player.hasPermission(ChannelPermission.JOIN.form(channel)))
            return Result.NO_PERMISSION;

        if (channel.isBanned(this))
            return Result.NO_PERMISSION;

        return Result.ALLOWED;
    }

    @Override
    public Result canLeave(Channel channel) {
        if (!channel.isMember(this))
            return Result.REDUNDANT;

        if (!player.hasPermission(ChannelPermission.LEAVE.form(channel)))
            return Result.NO_PERMISSION;

        return Result.ALLOWED;
    }

    @Override
    public Result canSpeak(Channel channel) {
        if (!channel.isMember(this))
            return Result.NO_PERMISSION;

        if (!player.hasPermission(ChannelPermission.SPEAK.form(channel)))
            return Result.NO_PERMISSION;

        if (channel.isMuted(this))
            return Result.NO_PERMISSION;

        return Result.ALLOWED;
    }

    @Override
    public Result canKick(Channel channel) {
        if (!player.hasPermission(ChannelPermission.KICK.form(channel)))
            return Result.NO_PERMISSION;

        return Result.ALLOWED;
    }

    @Override
    public Result canBan(Channel channel) {
        if (!player.hasPermission(ChannelPermission.BAN.form(channel)))
            return Result.NO_PERMISSION;

        return Result.ALLOWED;
    }

    @Override
    public Result canMute(Channel channel) {
        if (!player.hasPermission(ChannelPermission.MUTE.form(channel)))
            return Result.NO_PERMISSION;

        return Result.ALLOWED;
    }
    
    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        
        if (other == null)
            return false;
        
        if (!(other instanceof Chatter))
            return false;
        
        return player.equals(((Chatter) other).getPlayer());
    }
    
    @Override
    public int hashCode() {
        return player.hashCode();
    }

}
