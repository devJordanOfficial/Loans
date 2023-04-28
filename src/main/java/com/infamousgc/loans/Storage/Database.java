package com.infamousgc.loans.Storage;

import com.infamousgc.loans.Data.Plan;
import com.infamousgc.loans.Data.PlayerData;
import com.infamousgc.loans.Data.Players;
import com.infamousgc.loans.Loans;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

public class Database extends MySQL {
    private Loans plugin;
    public Database(Loans plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    /*
     * STATS
     * Setters and getters for the cf_stats table
     */

    // Setters
    public void setData(UUID uuid) {
        clearTable();

        PlayerData data = Players.get(uuid);

        try {

            PreparedStatement statement = getConnection().prepareStatement("INSERT INTO loans_playerdata" +
                    "(ID,HAS_LOAN,PLAN,PRINCIPAL,CREATED_AT,BALANCE,UPDATED) VALUES (?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE " +
                    "HAS_LOAN = VALUES(HAS_LOAN), PLAN = VALUES(PLAN), PRINCIPAL = VALUES(PRINCIPAL), " +
                    "CREATED_AT = VALUES(CREATED_AT), BALANCE = VALUES(BALANCE), UPDATED = VALUES(UPDATED)");

            statement.setString(1, uuid.toString());
            statement.setBoolean(2, data.hasLoan());
            statement.setByte(3, (data.getPlan() == null ? (byte) 3 : (byte) data.getPlan().ordinal()));
            statement.setDouble(4, data.getPrincipal());
            statement.setObject(5, data.getCreatedAt());
            statement.setDouble(6, data.getBalance());
            statement.setBoolean(7, data.isUpToDate());
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Getters
    public void loadData() {

        try {

            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM loans_playerdata");
            ResultSet result = statement.executeQuery();

            while(result.next()) {
                UUID owner = UUID.fromString(result.getString("ID"));
                boolean hasLoan = result.getBoolean("HAS_LOAN");
                Plan plan = Plan.valueOf(result.getByte("PLAN"));
                double principal = result.getDouble("PRINCIPAL");
                LocalDateTime createdAt = result.getObject("CREATED_AT", LocalDateTime.class);
                double balance = result.getDouble("BALANCE");

                Players.add(owner, hasLoan, plan, principal, createdAt, balance);
            }

            result.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearTable() {
        try {
            getConnection().prepareStatement("TRUNCATE TABLE loans_playerdata;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
