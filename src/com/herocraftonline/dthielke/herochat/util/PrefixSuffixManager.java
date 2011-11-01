package com.herocraftonline.dthielke.herochat.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.herocraftonline.dthielke.herochat.HeroChat;

public class PrefixSuffixManager {

    private List<FixObj> prefixes;
    private List<FixObj> suffixes;

    private HeroChat plugin;

    public PrefixSuffixManager(HeroChat p) {
        plugin = p;
        prefixes = new ArrayList<FixObj>();
        suffixes = new ArrayList<FixObj>();
    }

    public void setPrefixList(List<FixObj> prefixes) {
        this.prefixes = prefixes;
    }
    
    public List<FixObj> getPrefixList() {
        return this.prefixes;
    }

    public void setSuffixList(List<FixObj> suffixes) {
        this.suffixes = suffixes;
    }
    
    public List<FixObj> getSuffixList() {
        return this.suffixes;
    }

    public String getPrefix(Player p) {
        return searchList(p, prefixes);
    }

    public String getSuffix(Player p) {
        return searchList(p, suffixes);
    }

    private String searchList(Player p, List<FixObj> list) {
        int priority = -1;
        FixObj theObj = null;
        for (FixObj o : list) {
            boolean use = o.getAllPermsRequired() ? plugin.getPermissionManager().hasAll(p, o.getPermissions()) : plugin.getPermissionManager().hasAny(p, o.getPermissions());
            if (use && o.getPriority() > priority) {
                theObj = o;
                priority = theObj.getPriority();
            }
        }
        return theObj != null ? theObj.toString() : "";
    }

    public static class FixObj {
        private String name;
        private String data;
        private int priority;
        private List<String> permNodes;
        private boolean requiresAll;

        public FixObj(String name, String data, int priority, List<String> perms, boolean requiresAll) {
            this.name = name;
            this.data = data;
            this.priority = priority;
            this.permNodes = perms;
            this.requiresAll = requiresAll;
        }

        public int getPriority() {
            return priority;
        }

        public List<String> getPermissions() {
            return permNodes;
        }

        public String getName() {
            return name;
        }
        
        public String toString() {
            return data;
        }

        public boolean getAllPermsRequired() {
            return requiresAll;
        }
    }

}
