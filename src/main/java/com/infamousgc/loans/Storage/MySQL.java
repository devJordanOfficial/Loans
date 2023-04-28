package com.infamousgc.loans.Storage;

import com.infamousgc.loans.Loans;
import com.infamousgc.loans.Utilities.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
    private Loans plugin;

    // Login
    private final String host;
    private final String port;
    private final String database;
    private final String username;
    private final String password;

    public MySQL(Loans plugin) {
        this.plugin = plugin;

        host = plugin.config().getConfig().getString("mysql.host");
        port = plugin.config().getConfig().getString("mysql.port");
        database = plugin.config().getConfig().getString("mysql.database");
        username = plugin.config().getConfig().getString("mysql.username");
        password = plugin.config().getConfig().getString("mysql.password");

        if (!validLogin()) {
            Logger.severe("Database connection is not filled out in config.yml. Please provide a host, port, " +
                    "database name, and username and restart your server. This plugin will not work without a " +
                    "connection to MySQL");
            plugin.getPluginLoader().disablePlugin(plugin);
        }

        connect();
        createTable();
    }

    private boolean validLogin() {
        return host != null && port != null && database != null && username != null;
    }

    private Connection connection;

    protected boolean isConnected() {
        return connection != null;
    }

    private void connect() {
        if (isConnected()) return;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://" +
                            host + ":" + port + "/" + database + "?useSSL=false",
                    username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if (!isConnected()) return;

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected Connection getConnection() {
        return connection;
    }

    private void createTable() {
        try {
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS loans_playerdata (" +
                    "ID CHAR(36) NOT NULL PRIMARY KEY, " +
                    "HAS_LOAN BOOLEAN, " +
                    "PLAN TINYINT, " +
                    "PRINCIPAL DOUBLE, " +
                    "CREATED_AT DATETIME, " +
                    "BALANCE DOUBLE, " +
                    "UPDATED BOOLEAN" +
                    ");").executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
