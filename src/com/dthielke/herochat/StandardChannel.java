package com.dthielke.herochat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StandardChannel implements Channel {

    public enum ChannelPermission {
        JOIN("join"),
        LEAVE("leave"),
        SPEAK("speak"),
        KICK("kick"),
        BAN("ban"),
        MUTE("mute");

        private static Map<ChannelPermission, String> names;
        private String name;

        static {
            names = new HashMap<ChannelPermission, String>();
            for (ChannelPermission permission : ChannelPermission.values())
                names.put(permission, permission.name);
        }

        private ChannelPermission(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        public String form(Channel channel) {
            return "herochat." + channel.getName().toLowerCase() + "." + name;
        }
    }

    private String name;
    private String nick;
    private int distance;
    private List<Chatter> members = new ArrayList<Chatter>();
    private List<Chatter> bans = new ArrayList<Chatter>();
    private List<Chatter> mutes = new ArrayList<Chatter>();

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getNick() {
        return nick;
    }

    @Override
    public int getDistance() {
        return distance;
    }

    @Override
    public List<Chatter> getMembers() {
        return members;
    }

    @Override
    public boolean isBanned(Chatter chatter) {
        return bans.contains(chatter);
    }

    @Override
    public boolean isMuted(Chatter chatter) {
        return mutes.contains(chatter);
    }

    @Override
    public void setBanned(Chatter chatter, boolean banned) {
        if (banned) {
            if (!bans.contains(chatter))
                bans.add(chatter);
        } else {
            if (bans.contains(chatter))
                bans.remove(chatter);
        }
    }

    @Override
    public void setMuted(Chatter chatter, boolean muted) {
        if (muted) {
            if (!mutes.contains(chatter))
                mutes.add(chatter);
        } else {
            if (mutes.contains(chatter))
                mutes.remove(chatter);
        }
    }

    @Override
    public boolean isMember(Chatter chatter) {
        return members.contains(chatter);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.toLowerCase().hashCode());
        result = prime * result + ((nick == null) ? 0 : nick.toLowerCase().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;

        if (other == null)
            return false;

        if (!(other instanceof Channel))
            return false;

        Channel channel = (Channel) other;
        return name.equalsIgnoreCase(channel.getName()) || name.equalsIgnoreCase(channel.getNick());
    }

}
