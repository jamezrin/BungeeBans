package me.jaime29010.bungeebans.utils;

import net.md_5.bungee.api.ChatColor;

public class Utils {
    private static final char COLOR_CHAR = '&';
    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes(COLOR_CHAR, string);
    }
}
