package com.infamousgc.loans;

import com.infamousgc.loans.Commands.SlashLoans;
import com.infamousgc.loans.Commands.TabLoans;
import com.infamousgc.loans.Data.FileManager;
import com.infamousgc.loans.Data.Players;
import com.infamousgc.loans.Events.ChatListener;
import com.infamousgc.loans.Interface.GUI;
import com.infamousgc.loans.Events.InterfaceListener;
import com.infamousgc.loans.Storage.Database;
import com.infamousgc.loans.Utilities.Error;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.infamousmc.ranktree.RankTreeAPI;

import java.util.*;

public final class Loans extends JavaPlugin {

    private FileManager config;
    private Database database;

    private static Economy economy = null;
    public static HeadDatabaseAPI hdb;
    public RankTreeAPI rankTreeAPI;

    public final Map<UUID, GUI> user = new HashMap<>();
    public final List<UUID> listen = new ArrayList<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        if (!checkDependency("Vault")) return;
        if (!checkDependency("RankTree")) return;
        if (!checkDependency("HeadDatabase")) return;

        getCommand("Loans").setExecutor(new SlashLoans(this));
        getCommand("Loans").setTabCompleter(new TabLoans());

        getServer().getPluginManager().registerEvents(new InterfaceListener(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);

        hdb = new HeadDatabaseAPI();

        if ((!setupEconomy())) {
            Error.noDependency("Vault");
            this.getPluginLoader().disablePlugin(this);
            return;
        }

        rankTreeAPI = new RankTreeAPI();

        config = new FileManager(this, "config.yml");
        database = new Database(this);

        load();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        save();
        database.disconnect();
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

    private void save() {
        for (UUID uuid : Players.getList().keySet()) {
            database.setData(uuid);
        }
    }

    private void load() {
        database.loadData();
    }

    public FileManager config() {
        return config;
    }
}
