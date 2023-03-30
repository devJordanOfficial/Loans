package com.infamousgc.loans.Utilities;

import com.infamousgc.loans.Loans;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Logger {

    public static void log(String msg) {
        msg = ChatColor.translateAlternateColorCodes('&', "[" +
                Loans.getPlugin(Loans.class).getName() + "] " + msg);

        Bukkit.getConsoleSender().sendMessage(msg);
    }

    public static void severe(String msg) {
        log("&c------------------------ &4SEVERE &c------------------------");
        log(msg);
        log("&c------------------------ &4SEVERE &c------------------------");
    }

    public static void warning(String msg) {
        log("&e------------------------ &6WARNING &e------------------------");
        log(msg);
        log("&e------------------------ &6WARNING &e------------------------");
    }

}
