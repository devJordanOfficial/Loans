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

public class Payment extends GUI {

    public Payment(Loans plugin, Player player) {
        super(plugin, player);
    }

    @Override
    protected void create() {
        inv = Bukkit.createInventory(null, 27, Formatter.gradient("&lLoans"));
        initialize();
    }

    @Override
    protected void initialize() {
        inv.setItem(11, cancel());
        inv.setItem(13, paymentInfo());
        inv.setItem(15, confirm());
        fillSpace();
    }

    private ItemStack paymentInfo() {
        ItemStack item = Texture.MONEY_STACK.getHead();
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Formatter.gradient("&lMake Payment ($)"));

        List<String> lore = new ArrayList<String>();
        lore.add(Formatter.parse(""));
        lore.add(Formatter.parse("&fPayment Amount #005027&l» &7$" + Formatter.formatNumber(Players.get(player).getBalance())));
        lore.add(Formatter.parse("&fYour Balance #005027&l» &7$" + Formatter.formatNumber(plugin.getEconomy().getBalance(player))));

        lore.add(Formatter.parse(""));
        lore.add(Formatter.parse("#00984b&lConfirm &fto make the payment"));
        meta.setLore(lore);

        item.setItemMeta(meta);

        return item;
    }

    private ItemStack confirm() {
        ItemStack item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Formatter.parse("&a&lConfirm"));

        NamespacedKey key = new NamespacedKey(plugin, "action");
        meta.getPersistentDataContainer().set(key, new ActionDataType(), Action.CONFIRM);

        item.setItemMeta(meta);

        return item;
    }

    private ItemStack cancel() {
        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Formatter.parse("&c&lCancel"));

        NamespacedKey key = new NamespacedKey(plugin, "action");
        meta.getPersistentDataContainer().set(key, new ActionDataType(), Action.CANCEL);

        item.setItemMeta(meta);

        return item;
    }
}
