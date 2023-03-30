package com.infamousgc.loans.Interface;

import com.infamousgc.loans.Data.PlayerData;
import com.infamousgc.loans.Data.Players;
import com.infamousgc.loans.Data.Texture;
import com.infamousgc.loans.Utilities.Formatter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.infamousmc.ranktree.Data.Rank;
import org.infamousmc.ranktree.RankTreeAPI;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Menu {

    private Inventory inv;

    private Player player;

    public Menu() {
    }

    private void create() {
        inv = Bukkit.createInventory(null, 27, Formatter.gradient("&lLoans"));
        initialize();
    }

    private void initialize() {

        inv.setItem(13, noLoan());

        fillSpace();
    }

    public void open(Player player) {
        this.player = player;
        create();
        player.openInventory(inv);
    }

    private void fillSpace() {
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Formatter.parse("&r"));
        item.setItemMeta(meta);

        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null)
                inv.setItem(i, item);
        }
    }

    private ItemStack noLoan() {
        ItemStack item = Texture.MONEY.getHead();
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Formatter.gradient("-=- &lYou have no active loan&r -=-"));

        List<String> lore = new ArrayList<String>();
        lore.add(Formatter.parse(""));
        lore.add(Formatter.parse("&fWithdraw Limit #005027&l» &7$" + Players.get(player).getFormattedLimit()));
        meta.setLore(lore);

        item.setItemMeta(meta);

        return item;
    }

}
