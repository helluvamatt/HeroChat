/**
 * Copyright (C) 2011 DThielke <dave.thielke@gmail.com>
 * 
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to
 * Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 **/

package com.herocraftonline.dthielke.herochat.util;

import java.util.List;

import org.bukkit.entity.Player;

public class PermissionManager {

    public boolean isAdmin(Player p) {
        return p.hasPermission("herochat.admin");
    }

    public boolean isAllowedColor(Player p) {
        return p.hasPermission("herochat.color");
    }

    public boolean canCreate(Player p) {
        boolean admin = p.hasPermission("herochat.admin");
        boolean create = p.hasPermission("herochat.create");
        return admin || create;
    }

    public boolean hasAny(Player p, List<String> perms) {
        for (String s : perms) {
            if (p.hasPermission(s)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAll(Player p, List<String> perms) {
        for (String s : perms) {
            if (!p.hasPermission(s)) {
                return false;
            }
        }
        return true;
    }

}
