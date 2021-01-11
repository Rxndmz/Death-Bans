package me.rxndmz.deathbans;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ReviveHandler implements Listener {

    private DeathBans plugin;
    private ItemStack item = new ItemStack(Material.SUNFLOWER);

    public ReviveHandler(DeathBans plugin) {
        this.plugin = plugin;

        ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&5&kH &d&lRevive Token &5&kH"));
        List<String> lore = new ArrayList<String>();
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&d&lINFO:"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&5Right Click this item to"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&5redeem a revive token!"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&d&lHELP:"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&8/dbrevive {player} to revive someone."));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&8/dbrevivetokens to see your balance."));
        lore.add("");
        meta.setLore(lore);
        this.item.setItemMeta(meta);
        this.item.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 10);
    }

    /*
    Revive Item Recipe.
    */
    public ShapedRecipe getRecipe() {
        NamespacedKey key = new NamespacedKey(plugin, "sunflower");
        ShapedRecipe recipe = new ShapedRecipe(key, item);

        recipe.shape(" N "," E "," N ");

        recipe.setIngredient('N', Material.NETHERITE_INGOT);
        recipe.setIngredient('E', Material.EMERALD);

        return recipe;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        ItemStack i = this.item;
        i.setAmount(e.getItemInHand().getAmount());
        if (e.getItemInHand().equals(i)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void OnRightClick(PlayerInteractEvent e) {
        if (!e.hasItem())
            return;
        ItemStack i = this.item;
        i.setAmount(e.getItem().getAmount());
        if (e.getItem().equals(i)) {
            e.setCancelled(true);
            int tokensToRedeem = e.getItem().getAmount();
            e.getItem().setAmount(0);
            Player player = e.getPlayer();
            int tokens = 0;
            if (this.plugin.data.getConfig().contains("players." + player.getUniqueId().toString() + ".tokens")) {
                tokens = this.plugin.data.getConfig().getInt("players." + player.getUniqueId().toString() + ".tokens");
            }
            tokens = tokens + tokensToRedeem;
            this.plugin.data.getConfig().set("players." + player.getUniqueId().toString() + ".tokens", tokens);
            this.plugin.data.saveConfig();
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', tokensToRedeem + " &dRevive Token(s) has been redeemed!"));
        }
    }

}
