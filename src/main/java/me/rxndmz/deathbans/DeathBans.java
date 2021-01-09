package me.rxndmz.deathbans;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class DeathBans extends JavaPlugin {

    public PluginManager pm = getServer().getPluginManager();
    public Logger logger = getLogger();
    public DataManager data;

    @Override
    public void onEnable() {
        /*
        Generating config.yml
         */
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        // Starting DataManager
        this.data = new DataManager(this);

        /*
        Registering Events
         */
        pm.registerEvents(new DeathManager(this), this);
        pm.registerEvents(this.data, this);
    }

    @Override
    public void onDisable() {
        this.data.saveConfig();
        saveDefaultConfig();
    }
}
