package com.infamousgc.loans.Data;

import com.infamousgc.loans.Loans;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.logging.Level;

public class FileManager {
    private final Loans plugin;
    private FileConfiguration configuration = null;
    private File file = null;

    private final String name;
    private final File path;

    public FileManager(Loans plugin, File file) {
        this.plugin = plugin;
        this.file = file;
        name = null;
        path = null;
    }

    public FileManager(Loans plugin, String file) {
        this(plugin, file, null);
    }

    public FileManager(Loans plugin, String file, String folder) {
        this.plugin = plugin;
        this.name = file;

        if (folder == null)
            path = plugin.getDataFolder();
        else
            path = new File(plugin.getDataFolder() + File.separator + folder);

        saveDefaultConfig();
    }

    public void reloadConfig() {
        if (file == null)
            file = new File(path, name);

        configuration = YamlConfiguration.loadConfiguration(file);

        InputStream defaultStream;

        if (name != null)
            defaultStream = plugin.getResource(name);
        else {
            try {
                defaultStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                defaultStream = null;
            }
        }
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            configuration.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (configuration == null)
            reloadConfig();
        return configuration;
    }

    public void saveConfig() {
        if(configuration == null || file == null)
            return;

        try {
            getConfig().save(file);
        } catch(IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + file, e);
        }
    }

    public void saveDefaultConfig() {
        if (file == null)
            file = new File(path, name);

        if (!file.exists()) {
            if (path == plugin.getDataFolder())
                plugin.saveResource(name, false);
            else {
                try {
                    boolean created = file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
