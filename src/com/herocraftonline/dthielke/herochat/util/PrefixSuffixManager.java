package com.herocraftonline.dthielke.herochat.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.herocraftonline.dthielke.herochat.HeroChat;

public class PrefixSuffixManager {

    private List<FixObj> prefixes;
    private List<FixObj> suffixes;
    private List<FixObj> groupPrefixes;
    private List<FixObj> groupSuffixes;

    private HeroChat plugin;

    public PrefixSuffixManager(HeroChat p) {
        plugin = p;
        prefixes = new ArrayList<FixObj>();
        suffixes = new ArrayList<FixObj>();
        groupPrefixes = new ArrayList<FixObj>();
        groupSuffixes = new ArrayList<FixObj>();
    }

    public void setPrefixList(List<FixObj> prefixes) {
        this.prefixes = prefixes;
    }

    public void setSuffixList(List<FixObj> suffixes) {
        this.suffixes = suffixes;
    }

    public void setGroupPrefixList(List<FixObj> gPrefixes) {
        this.groupPrefixes = gPrefixes;
    }

    public void setGroupSuffixList(List<FixObj> gSuffixes) {
        this.groupSuffixes = gSuffixes;
    }

    public String getPrefix(Player p) {
        return searchList(p, prefixes);
    }

    public String getSuffix(Player p) {
        return searchList(p, suffixes);
    }

    public String getGroupPrefix(Player p) {
        return searchList(p, groupPrefixes);
    }

    public String getGroupSuffix(Player p) {
        return searchList(p, groupSuffixes);
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

    public class FixObj {
        private String data;
        private int priority;
        private List<String> permNodes;
        private boolean requiresAll;

        public FixObj(String data, int priority, List<String> perms, boolean requiresAll) {
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

        public String toString() {
            return data;
        }

        public boolean getAllPermsRequired() {
            return requiresAll;
        }
    }

}
