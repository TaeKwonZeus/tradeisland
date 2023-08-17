package net.cubicworld.tradeisland;

import net.cubicworld.tradeisland.trade.TradeManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class TradeIslandPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        TradeManager tradeManager = new TradeManager(this);

        Bukkit.getPluginManager().registerEvents(tradeManager, this);
        Objects.requireNonNull(getCommand("trade")).setExecutor(tradeManager);

        Bukkit.getLogger().info("Plugin enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Plugin disabled!");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().getInventory().addItem(new ItemStack(Material.DIAMOND, 64));
    }
}
