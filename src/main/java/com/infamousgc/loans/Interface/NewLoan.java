package com.infamousgc.loans.Interface;

import com.infamousgc.loans.Data.*;
import com.infamousgc.loans.Loans;
import com.infamousgc.loans.Utilities.Formatter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class NewLoan extends GUI {

    private PlayerData playerData;

    public NewLoan(Loans plugin, Player player) {
        super(plugin, player);
    }

    @Override
    protected void create() {
        inv = Bukkit.createInventory(null, 36, Formatter.gradient("&lLoans"));
        playerData = Players.get(player);
        initialize();
    }

    public void update() {
        inv.setItem(11, chooseAmount());
        inv.setItem(15, choosePlan());
        inv.setItem(22, loanInfo());
        this.fillSpace();
    }

    @Override
    protected void initialize() {
        inv.setItem(11, chooseAmount());
        inv.setItem(15, choosePlan());
        inv.setItem(22, loanInfo());
        this.fillSpace();
    }

    private ItemStack chooseAmount() {
        double principal = playerData.getPrincipalCache();

        ItemStack item = Texture.COIN.getHead();
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Formatter.gradient("&lSet Amount ($)"));

        List<String> lore = new ArrayList<String>();
        lore.add(Formatter.parse(""));
        lore.add(Formatter.parse("&fInfo #005027&l» &7Select the amount of money you wish to withdraw."));
        lore.add(Formatter.parse("&7See the info box in the top right corner to understand"));
        lore.add(Formatter.parse("&7how loans work."));
        if (principal != 0) {
            lore.add(Formatter.parse(""));
            lore.add(Formatter.parse("&fCurrent Amount #005027&l» &7$" + Formatter.formatNumber(principal)));
        }
        lore.add(Formatter.parse(""));
        lore.add(Formatter.parse("#00984b&lLMB &fto set the amount"));
        meta.setLore(lore);

        NamespacedKey key = new NamespacedKey(plugin, "action");
        meta.getPersistentDataContainer().set(key, new ActionDataType(), Action.SET_PRINCIPAL);

        item.setItemMeta(meta);

        return item;
    }

    private ItemStack choosePlan() {
        double principal = playerData.getPrincipalCache();

        ItemStack item;
        if (principal == 0)
            item = Texture.RED_COMPUTER.getHead();
        else
            item = Texture.BLUE_COMPUTER.getHead();
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Formatter.gradient("&lSelect Plan (?)"));

        List<String> lore = new ArrayList<String>();
        lore.add(Formatter.parse(""));
        lore.add(Formatter.parse("&fInfo #005027&l» &7Your payment plan determines how long you have"));
        lore.add(Formatter.parse("&7to pay back the loaned amount and the interest rate"));
        lore.add(Formatter.parse("&7that will be charged."));
        lore.add(Formatter.parse(""));
        if (principal == 0)
            lore.add(Formatter.parse("&c* You must select your loan amount first *"));
        else
            lore.add(Formatter.parse("#00984b&lLMB &fto select your plan"));
        meta.setLore(lore);

        if (principal != 0) {
            NamespacedKey key = new NamespacedKey(plugin, "action");
            meta.getPersistentDataContainer().set(key, new ActionDataType(), Action.SET_PAYMENT);
        }

        item.setItemMeta(meta);

        return item;
    }

    private ItemStack loanInfo() {
        LoanType plan = playerData.getPlanCache();
        double principal = playerData.getPrincipalCache();

        ItemStack item = Texture.MONEY_BAG.getHead();
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Formatter.gradient("&lCurrent Loan"));

        List<String> lore = new ArrayList<String>();
        lore.add(Formatter.parse(""));
        lore.add(Formatter.parse("&fPrincipal Amount #005027&l» &7$" + Formatter.formatNumber(principal)));
        if (plan == null)
            lore.add(Formatter.parse("&fPayment Plan #005027&l» &7No Plan Selected"));
        if (plan != null) {
            lore.add(Formatter.parse("&fPayment Plan #005027&l»&7 " + plan.getDescription()));
            lore.add(Formatter.parse("&f    &l»&f Interest #005027&l»&7 " + plan.getRate() * 100 + "%"));
            lore.add(Formatter.parse("&f    &l»&f Period #005027&l»&7 " + plan.getPeriod() + " Hour" + (plan.getPeriod() == 1 ? "" : "s")));
        }
        lore.add(Formatter.parse(""));
        if (principal == 0)
            lore.add(Formatter.parse("&c* You must select your loan amount *"));
        else if (plan == null)
            lore.add(Formatter.parse("&c* You must select your payment plan *"));
        else
            lore.add(Formatter.parse("#00984b&lLMB &fto create this loan"));
        meta.setLore(lore);

        if (principal != 0 && plan != null) {
            NamespacedKey key = new NamespacedKey(plugin, "action");
            meta.getPersistentDataContainer().set(key, new ActionDataType(), Action.CREATE_LOAN);
        }

        item.setItemMeta(meta);

        return item;
    }

    @Override
    protected void fillSpace() {
        LoanType plan = playerData.getPlanCache();
        double principal = playerData.getPrincipalCache();

        ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Formatter.parse("&r"));
        item.setItemMeta(meta);

        if (principal != 0) {
            inv.setItem(2, item);
            inv.setItem(10, item);
            inv.setItem(12, item);
            inv.setItem(20, item);

        }

        if (plan != null) {
            inv.setItem(6, item);
            inv.setItem(14, item);
            inv.setItem(16, item);
            inv.setItem(24, item);
        }

        for (int i = 0; i < inv.getSize(); i++) {
            item.setType(Material.BLACK_STAINED_GLASS_PANE);
            if (inv.getItem(i) == null)
                inv.setItem(i, item);
        }
    }
}
