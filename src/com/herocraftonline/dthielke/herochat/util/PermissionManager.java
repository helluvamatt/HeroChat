/**
 * Copyright (C) 2011 DThielke <dave.thielke@gmail.com>
 * 
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to
 * Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 **/

package com.herocraftonline.dthielke.herochat.util;

import org.bukkit.entity.Player;

import com.herocraftonline.dthielke.herochat.channels.Channel;
import com.nijiko.permissions.PermissionHandler;

public class PermissionManager {

    public enum Permission {
        RELOAD("reload"),
        REMOVE("remove"),
        BAN("ban"),
        KICK("kick"),
        MUTE("mute"),
        CREATE("create"),
        ADMIN_MOD("admin.mod"),
        ADMIN_REMOVE("admin.remove"),
        ADMIN_BAN("admin.ban"),
        ADMIN_KICK("admin.kick"),
        ADMIN_MUTE("admin.mute"),
        ADMIN_GMUTE("admin.gmute"),
        ADMIN_IMMUNITY("admin.immunity"),
        ADMIN_TOGGLE("admin.toggle");
        
        private final String string;
        
        private Permission(String string) {
            this.string = string;
        }
        
        @Override
        public String toString() {
            return string;
        }
    }
    
    public enum ChannelPermission {
        ALLOW("allow."),
        DENY("deny."),
        AUTOJOIN_ONCE("autojoin.once."),
        AUTOJOIN_ALWAYS("autojoin.always."),
        AUTOFOCUS_ONCE("autofocus.once."),
        AUTOFOCUS_ALWAYS("autofocus.always."),
        FORCED("forced."),
        SPEAK("speak."),
        MUTE("mute.");

        private final String string;

        private ChannelPermission(String string) {
            this.string = string;
        }

        @Override
        public String toString() {
            return string;
        }
    }

    private PermissionHandler security;

    public PermissionManager(PermissionHandler security) {
        this.security = security;
    }

    public boolean hasPermission(Player player, Channel channel, ChannelPermission permission) {
        if (security != null) {
            return security.has(player, "herochat." + permission + channel.getName().toLowerCase());
        } else {
            return false;
        }
    }
    
    public boolean hasPermission(Player player, Permission permission) {
        if (security != null) {
            return security.has(player, "herochat." + permission);
        } else {
            return player.isOp();
        }
    }

}
