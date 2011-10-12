package com.dthielke.herochat;

import java.util.List;

public interface Channel {

    public String getName();

    public String getNick();

    public int getDistance();

    public boolean isMember(Chatter chatter);

    public boolean isBanned(Chatter chatter);

    public boolean isMuted(Chatter chatter);

    public void setBanned(Chatter chatter, boolean banned);

    public void setMuted(Chatter chatter, boolean muted);

    public List<Chatter> getMembers();

}
