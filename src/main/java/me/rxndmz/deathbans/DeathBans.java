package me.rxndmz.deathbans;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
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

        // Adding recipe to server
        Bukkit.addRecipe(getReviveRecipe());
    }

    /*
    Revive Item Recipe.
     */
    public ShapedRecipe getReviveRecipe() {
        ItemStack item = new ItemStack(Material.SUNFLOWER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&k&5H &d&lRevive Wand &k&5H"));
        List<String> lore = meta.getLore();
        lore.clear();
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&d&lINFO:"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&5&nRight &r&5Click this item to redeem a revive token!"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&8Revive tokens can be used revive a fallen ally."));
        lore.add("");
        meta.setLore(lore);
        item.setItemMeta(meta);
        item.addEnchantment(Enchantment.ARROW_DAMAGE, 10);

        NamespacedKey key = new NamespacedKey(this, "sunflower");

        ShapedRecipe recipe = new ShapedRecipe(key, item);

        recipe.setIngredient('N', Material.NETHERITE_INGOT);
        recipe.setIngredient('E', Material.EMERALD);

        recipe.shape(" N "," E "," N ");

        return recipe;
    }

    @Override
    public void onDisable() {
        this.data.saveConfig();
        saveDefaultConfig();
    }
}
