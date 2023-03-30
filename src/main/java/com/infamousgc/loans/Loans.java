package com.infamousgc.loans;

import com.infamousgc.loans.Commands.SlashLoans;
import com.infamousgc.loans.Commands.TabLoans;
import com.infamousgc.loans.Utilities.Error;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class Loans extends JavaPlugin {

    private static Economy economy = null;
    public static HeadDatabaseAPI hdb;

    @Override
    public void onEnable() {
        // Plugin startup logic
        if (!checkDependency("Vault")) return;
        if (!checkDependency("RankTree")) return;
        if (!checkDependency("HeadDatabase")) return;

        getCommand("Loans").setExecutor(new SlashLoans(this));
        getCommand("Loans").setTabCompleter(new TabLoans());

        hdb = new HeadDatabaseAPI();

        if ((!setupEconomy())) {
            Error.noDependency("Vault");
            this.getPluginLoader().disablePlugin(this);
            return;
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }

    private boolean checkDependency(String plugin) {
        if (getServer().getPluginManager().getPlugin(plugin) == null) {
            Error.noDependency(plugin);
            this.getPluginLoader().disablePlugin(this);
            return false;
        }

        if (!getServer().getPluginManager().getPlugin(plugin).isEnabled()) {
            Error.noDependency(plugin);
            this.getPluginLoader().disablePlugin(this);
            return false;
        }
        return true;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
