package com.infamousgc.loans.Data;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Players {
    // This class is static, no instances should be created of it
    private Players() {}

    // All cached players - Any player that has accessed loans before
    private static Map<UUID, PlayerData> players = new HashMap<UUID, PlayerData>();

    /**
     * Obtain the playerdata for the given Player object. Will create data if none exists.
     *
     * @param player The player to get the data for
     * @return Returns the playerdata for the given parameter
     */
    public static PlayerData get(Player player) {
        if (!hasData(player.getUniqueId()))
            createData(player.getUniqueId());

        return players.get(player.getUniqueId());
    }

    /**
     * Obtain the playerdata for the given UUID. Will create data if none exists.
     *
     * @param uuid The player's UUID to get the data for
     * @return Returns the playerdata for the given parameter
     */
    public static PlayerData get(UUID uuid) {
        if (!hasData(uuid))
            createData(uuid);

        return players.get(uuid);
    }

    // Check if the player has a PlayerData object or not
    private static boolean hasData(UUID uuid) {
        if (players.containsKey(uuid))
            return true;
        else
            return false;
    }

    // Create a PlayerData object for the player and cache it
    private static void createData(UUID uuid) {
        players.put(uuid, new PlayerData(uuid));
    }
}
