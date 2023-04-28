package com.infamousgc.loans;

import com.infamousgc.loans.Commands.SlashLoans;
import com.infamousgc.loans.Commands.TabLoans;
import com.infamousgc.loans.Data.FileManager;
import com.infamousgc.loans.Data.OverdueCalculator;
import com.infamousgc.loans.Data.Players;
import com.infamousgc.loans.Events.ChatListener;
import com.infamousgc.loans.Events.JobPaymentListener;
import com.infamousgc.loans.Events.JoinListener;
import com.infamousgc.loans.Interface.GUI;
import com.infamousgc.loans.Events.InterfaceListener;
import com.infamousgc.loans.Storage.Database;
import com.infamousgc.loans.Utilities.Error;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
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
    public LuckPerms luckperms;

    public final Map<UUID, GUI> user = new HashMap<>();
    public final List<UUID> listen = new ArrayList<>();

    private boolean disabled;

    @Override
    public void onEnable() {
        // Plugin startup logic
        if (!checkDependency("Vault")) return;
        if (!checkDependency("RankTree")) return;
        if (!checkDependency("HeadDatabase")) return;
        if (!checkDependency("Jobs")) return;
        if (!checkDependency("Luckperms")) return;

        getCommand("Loans").setExecutor(new SlashLoans(this));
        getCommand("Loans").setTabCompleter(new TabLoans());

        getServer().getPluginManager().registerEvents(new InterfaceListener(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new JobPaymentListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);

        hdb = new HeadDatabaseAPI();
        rankTreeAPI = new RankTreeAPI();
        luckperms = LuckPermsProvider.get();

        if ((!setupEconomy())) {
            Error.noDependency("Vault");
            disabled = true;
            this.getPluginLoader().disablePlugin(this);
            return;
        }

        config = new FileManager(this, "config.yml");
        database = new Database(this);

        load();

        luckperms.getContextManager().registerCalculator(new OverdueCalculator());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (disabled) return;
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
            disabled = true;
            this.getPluginLoader().disablePlugin(this);
            return false;
        }

        if (!getServer().getPluginManager().getPlugin(plugin).isEnabled()) {
            Error.noDependency(plugin);
            disabled = true;
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

    public Economy getEconomy() {
        return economy;
    }
}
