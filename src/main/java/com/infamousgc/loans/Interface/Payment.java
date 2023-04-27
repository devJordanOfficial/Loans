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
import org.bukkit.persistence.PersistentDataType;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Payment extends GUI {

    private PlayerData playerData;
    private double payment;

    public Payment(Loans plugin, Player player) {
        super(plugin, player);
    }

    @Override
    protected void create() {
        inv = Bukkit.createInventory(null, 45, Formatter.gradient("&lLoans"));
        playerData = Players.get(player);
        initialize();
    }

    @Override
    protected void initialize() {
        inv.setItem(19, removeMoney(10000));
        inv.setItem(20, removeMoney(1000));
        inv.setItem(21, removeMoney(100));
        inv.setItem(13, maxMoney());
        inv.setItem(22, paymentInfo());
        inv.setItem(31, customMoney());
        inv.setItem(23, addMoney(100));
        inv.setItem(24, addMoney(1000));
        inv.setItem(25, addMoney(10000));
        fillSpace();
    }

    public void update() {
        inv.setItem(22, paymentInfo());
    }

    private ItemStack paymentInfo() {
        ItemStack item = Texture.MONEY_STACK.getHead();
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Formatter.gradient("&lMake Payment ($)"));

        List<String> lore = new ArrayList<String>();
        lore.add(Formatter.parse(""));
        lore.add(Formatter.parse("&fCurrent Balance #005027&l» &7$" + Formatter.formatNumber(Players.get(player).getBalance())));
        lore.add(Formatter.parse("&fPayment Amount #005027&l» &7$" + Formatter.formatNumber(payment)));
        lore.add(Formatter.parse("&fPost-Payment Balance #005027&l» &7$" + Formatter.formatNumber(Players.get(player).getBalance() - payment)));
        lore.add(Formatter.parse(""));
        lore.add(Formatter.parse("#00984b&lLMB &fto make the payment"));
        meta.setLore(lore);

        NamespacedKey key = new NamespacedKey(plugin, "action");
        meta.getPersistentDataContainer().set(key, new ActionDataType(), Action.MAKE_PAYMENT);

        item.setItemMeta(meta);

        return item;
    }

    private ItemStack addMoney(double amount) {
        DecimalFormat df = new DecimalFormat("#,###");

        ItemStack item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Formatter.parse("&f&l+" + df.format(amount)));

        NamespacedKey key = new NamespacedKey(plugin, "action");
        meta.getPersistentDataContainer().set(key, new ActionDataType(), Action.CHANGE_AMOUNT);
        key = new NamespacedKey(plugin, "amount");
        meta.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, amount);

        item.setItemMeta(meta);

        return item;
    }

    private ItemStack removeMoney(double amount) {
        amount *= -1;
        DecimalFormat df = new DecimalFormat("#,###");

        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Formatter.parse("&f&l" + df.format(amount)));

        NamespacedKey key = new NamespacedKey(plugin, "action");
        meta.getPersistentDataContainer().set(key, new ActionDataType(), Action.CHANGE_AMOUNT);
        key = new NamespacedKey(plugin, "amount");
        meta.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, amount);

        item.setItemMeta(meta);

        return item;
    }

    private ItemStack maxMoney() {
        ItemStack item = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Formatter.parse("&f&lMax Amount"));

        NamespacedKey key = new NamespacedKey(plugin, "action");
        meta.getPersistentDataContainer().set(key, new ActionDataType(), Action.MAX_AMOUNT);

        item.setItemMeta(meta);

        return item;
    }

    private ItemStack customMoney() {

        ItemStack item = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Formatter.parse("&f&lCustom Amount"));

        NamespacedKey key = new NamespacedKey(plugin, "action");
        meta.getPersistentDataContainer().set(key, new ActionDataType(), Action.CUSTOM_AMOUNT);

        item.setItemMeta(meta);

        return item;
    }

    public void setPayment(double amount) {
        payment = amount;
    }

    public double getPayment() {
        return payment;
    }
}
