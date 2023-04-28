package com.infamousgc.loans.Interface;

import com.infamousgc.loans.Data.*;
import com.infamousgc.loans.Loans;
import com.infamousgc.loans.Utilities.Formatter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class SetPlan extends GUI {

    private PlayerData playerData;

    public SetPlan(Loans plugin, Player player) {
        super(plugin, player);
    }

    @Override
    protected void create() {
        inv = Bukkit.createInventory(null, 27, Formatter.gradient("&lLoans"));
        playerData = Players.get(player);
        initialize();
    }

    public void update() {
        inv.setItem(11, shortTerm());
        inv.setItem(13, forecast());
        inv.setItem(15, longTerm());
    }

    @Override
    protected void initialize() {
        inv.setItem(11, shortTerm());
        inv.setItem(13, forecast());
        inv.setItem(15, longTerm());
        fillSpace();
    }

    private ItemStack shortTerm() {
        Plan plan = playerData.getPlanCache();

        ItemStack item;

        if (plan == Plan.SHORT) {
            item = Texture.GREEN_ONE.getHead();
        } else {
            item = Texture.GRAY_ONE.getHead();
        }

        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Formatter.gradient("&lShort Term"));

        List<String> lore = new ArrayList<String>();
        lore.add(Formatter.parse(""));
        lore.add(Formatter.parse("&fDeadline #005027&l» &724 hours (1 day)"));
        lore.add(Formatter.parse("&fPeriod #005027&l» &71 hour"));
        lore.add(Formatter.parse("&fInterest #005027&l» &70.4% Compound"));
        lore.add(Formatter.parse(""));
        lore.add(Formatter.parse("#00984b&lLMB &fto select this plan"));
        meta.setLore(lore);

        NamespacedKey key = new NamespacedKey(plugin, "action");
        meta.getPersistentDataContainer().set(key, new ActionDataType(), Action.SHORT_TERM);

        item.setItemMeta(meta);

        return item;
    }

    private ItemStack longTerm() {
        Plan plan = playerData.getPlanCache();

        ItemStack item;

        if (plan == Plan.LONG) {
            item = Texture.GREEN_THREE.getHead();
        } else {
            item = Texture.GRAY_THREE.getHead();
        }

        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Formatter.gradient("&lLong Term"));

        if (plan == Plan.LONG) {
            meta.addEnchant(Enchantment.DURABILITY, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        List<String> lore = new ArrayList<String>();
        lore.add(Formatter.parse(""));
        lore.add(Formatter.parse("&fDeadline #005027&l» &772 hours (3 days)"));
        lore.add(Formatter.parse("&fPeriod #005027&l» &72 hours"));
        lore.add(Formatter.parse("&fInterest #005027&l» &71% Compound"));
        lore.add(Formatter.parse(""));
        lore.add(Formatter.parse("#00984b&lLMB &fto select this plan"));
        meta.setLore(lore);

        NamespacedKey key = new NamespacedKey(plugin, "action");
        meta.getPersistentDataContainer().set(key, new ActionDataType(), Action.LONG_TERM);

        item.setItemMeta(meta);

        return item;
    }

    private ItemStack forecast() {
        Plan plan = playerData.getPlanCache();
        double principal = playerData.getPrincipalCache();

        ItemStack item = Texture.MONEY_BAG.getHead();
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Formatter.gradient("&lLoan Forecast"));

        List<String> lore = new ArrayList<String>();
        lore.add(Formatter.parse(""));
        if (plan == null) {
            lore.add(Formatter.parse("&7You must select a plan before you"));
            lore.add(Formatter.parse("&7can see your forecast."));
        } else {
            lore.add(Formatter.parse("&7The following is a the predicted amount you will owe"));
            lore.add(Formatter.parse("&7after certain amounts of time."));
            lore.add(Formatter.parse(""));
            if (plan == Plan.SHORT) {
                lore.add(Formatter.parse("&f8 Hours #005027&l» &7$" + forecast(principal, 8, 0.004, 1)));
                lore.add(Formatter.parse("&f16 Hours #005027&l» &7$" + forecast(principal, 16, 0.004, 1)));
                lore.add(Formatter.parse("&f24 Hours #005027&l» &7$" + forecast(principal, 24, 0.004, 1)));
            }
            if (plan == Plan.LONG) {
                lore.add(Formatter.parse("&f24 Hours #005027&l» &7$" + forecast(principal, 24, 0.01, 2)));
                lore.add(Formatter.parse("&f48 Hours #005027&l» &7$" + forecast(principal, 48, 0.01, 2)));
                lore.add(Formatter.parse("&f72 Hours #005027&l» &7$" + forecast(principal, 72, 0.01, 2)));
            }
            lore.add(Formatter.parse(""));
            lore.add(Formatter.parse("#00984b&lLMB &fto confirm your selection"));
        }
        meta.setLore(lore);

        NamespacedKey key = new NamespacedKey(plugin, "action");
        meta.getPersistentDataContainer().set(key, new ActionDataType(), Action.PLAN_CONFIRM);

        item.setItemMeta(meta);

        return item;
    }

    public String forecast(double principal, long hours, double rate, int periodLength) {
        LocalDateTime predictedCreation = LocalDateTime.now().minus(hours, ChronoUnit.HOURS);
        Duration duration = Duration.between(predictedCreation, LocalDateTime.now());

        long periods = duration.toHours() / periodLength;

        double value = principal * Math.pow(1 + rate, periods);

        return Formatter.formatNumber(value);
    }
}
