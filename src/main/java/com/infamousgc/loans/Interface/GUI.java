package com.infamousgc.loans.Interface;

import com.infamousgc.loans.Loans;
import com.infamousgc.loans.Utilities.Formatter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class GUI {

    protected Loans plugin;
    protected Inventory inv;
    protected Player player;

    public GUI(Loans plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        create();
        open();
    }

    protected abstract void create();

    protected abstract void initialize();

    public void open() {
        plugin.user.put(player.getUniqueId(), this);
        player.openInventory(inv);
    }

    protected void fillSpace() {
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Formatter.parse("&r"));
        item.setItemMeta(meta);

        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null)
                inv.setItem(i, item);
        }
    }
}
