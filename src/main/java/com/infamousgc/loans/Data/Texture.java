package com.infamousgc.loans.Data;

import com.infamousgc.loans.Loans;
import org.bukkit.inventory.ItemStack;

public enum Texture {
    MONEY("50790");

    private final String id;

    Texture(String id) {
        this.id = id;
    }

    public ItemStack getHead() {
        return Loans.hdb.getItemHead(id);
    }
}
