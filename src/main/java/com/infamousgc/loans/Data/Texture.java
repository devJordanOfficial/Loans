package com.infamousgc.loans.Data;

import com.infamousgc.loans.Loans;
import org.bukkit.inventory.ItemStack;

public enum Texture {
    MONEY_STACK("21539"),
    MONEY_BAG("50790"),
    COIN("60568"),
    RED_COMPUTER("60261"),
    BLUE_COMPUTER("60262"),
    GRAY_ONE("10350"),
    GRAY_THREE("10348"),
    GREEN_ONE("10242"),
    GREEN_THREE("10240");

    private final String id;

    Texture(String id) {
        this.id = id;
    }

    public ItemStack getHead() {
        return Loans.hdb.getItemHead(id);
    }
}
