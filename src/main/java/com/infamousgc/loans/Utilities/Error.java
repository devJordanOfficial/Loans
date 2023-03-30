package com.infamousgc.loans.Utilities;

public class Error {

    public static void noDependency(String plugin) {
        Logger.severe("&7" + plugin + " was not found! Please add the plugin to the server. " +
                "Loans does not work without it.");
    }

    public static String isConsole() {
        return Formatter.parse("&c&lOOPS! &7This command must be run by a &cplayer&7!");
    }

}
