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

public class Main extends GUI {

    private PlayerData playerData;

    public Main(Loans plugin, Player player) {
        super(plugin, player);
    }

    @Override
    protected void create() {
        inv = Bukkit.createInventory(null, 27, Formatter.gradient("&lLoans"));
        playerData = Players.get(player);
        initialize();
    }

    @Override
    protected void initialize() {
        if (playerData.hasLoan()) {
            inv.setItem(13, activeLoan());
            fillIndicator();
        } else
            inv.setItem(13, noLoan());

        fillSpace();
    }

    private ItemStack noLoan() {
        ItemStack item = Texture.MONEY_BAG.getHead();
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Formatter.gradient("-=- &lYou have no active loan&r -=-"));

        List<String> lore = new ArrayList<String>();
        lore.add(Formatter.parse(""));
        lore.add(Formatter.parse("&fWithdraw Limit #005027&l» &7$" + Players.get(player).getFormattedLimit()));
        lore.add(Formatter.parse(""));
        lore.add(Formatter.parse("#00984b&lLMB &fto take out a loan"));
        meta.setLore(lore);

        NamespacedKey key = new NamespacedKey(plugin, "action");
        meta.getPersistentDataContainer().set(key, new ActionDataType(), Action.NEW_LOAN);

        item.setItemMeta(meta);

        return item;
    }

    private ItemStack activeLoan() {
        ItemStack item = Texture.MONEY_BAG.getHead();
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Formatter.gradient("-=- &lYou have an active loan&r -=-"));

        List<String> lore = new ArrayList<String>();
        lore.add(Formatter.parse(""));
        lore.add(Formatter.parse("&fRemaining Balance #005027&l» &7$" + Formatter.formatNumber(playerData.getBalance())));
        if (!playerData.isOverdue())
            lore.add(Formatter.parse("&fNext Period #005027&l» &7In " + playerData.getNextPeriod()));
        lore.add(Formatter.parse("&fPayment Due #005027&l» &7" + playerData.getDue()));
        lore.add(Formatter.parse(""));
        lore.add(Formatter.parse("&fPrincipal Amount #005027&l» &7$" + Formatter.formatNumber(playerData.getPrincipal())));
        lore.add(Formatter.parse("&fInterest Accrued #005027&l» &7$" + Formatter.formatNumber(playerData.getBalance() - playerData.getPrincipal())));
        lore.add(Formatter.parse("&fInterest #005027&l» &7" + (playerData.getPlan().getRate() * 100)) + "%");
        lore.add(Formatter.parse("&fCompound Period #005027&l» &7" + playerData.getPlan().getPeriod() + " Hour" + (playerData.getPlan().getPeriod() == 1 ? "" : "s")));
        lore.add(Formatter.parse(""));
        lore.add(Formatter.parse("#00984b&lLMB &fto make a payment"));
        meta.setLore(lore);

        NamespacedKey key = new NamespacedKey(plugin, "action");
        meta.getPersistentDataContainer().set(key, new ActionDataType(), Action.PAY);

        item.setItemMeta(meta);

        return item;
    }

    private void fillIndicator() {
        ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Formatter.parse("&r"));
        item.setItemMeta(meta);

        if (playerData.getPercentUntilDue() >= 0.33 && playerData.getPercentUntilDue() < 0.66)
            item.setType(Material.YELLOW_STAINED_GLASS_PANE);
        else if (playerData.getPercentUntilDue() < 0.33)
            item.setType(Material.RED_STAINED_GLASS_PANE);

        inv.setItem(4, item);
        inv.setItem(12, item);
        inv.setItem(14, item);
        inv.setItem(22, item);
    }

}
