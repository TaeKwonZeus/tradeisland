package net.cubicworld.tradeisland.trade;

import lombok.Getter;
import net.cubicworld.tradeisland.TradeIslandPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class Transaction {
    private final TradeIslandPlugin plugin;

    private final Player player1;
    private final Player player2;

    private boolean player1Confirmed = false;
    private boolean player2Confirmed = false;

    private final TradeInventory inventory;

    public Transaction(TradeIslandPlugin plugin, Player player1, Player player2) {
        this.plugin = plugin;
        this.player1 = player1;
        this.player2 = player2;

        inventory = new TradeInventory(plugin, player1, player2);
        inventory.open();
    }

    public void processInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!player.equals(player1) && !player.equals(player2)) return;

        CellType cellType = TradeInventory.getCellType(event.getSlot());

        if (cellType == CellType.CONFIRM_MAIN) {
            event.setCancelled(true);
            if (player.equals(player1)) {
                player1Confirmed = !player1Confirmed;
                inventory.setPlayer1Confirm(player1Confirmed);
            } else {
                player2Confirmed = !player2Confirmed;
                inventory.setPlayer2Confirm(player2Confirmed);
            }
        } else if (cellType == CellType.ITEM_SLOT_MAIN) {
            Bukkit.getScheduler().runTask(plugin, () -> inventory.reflectItemChange(player, event.getSlot()));
        } else {
            event.setCancelled(true);
        }
    }

    public boolean usesInventory(Inventory inventory) {
        return this.inventory.usesInventory(inventory);
    }

    public boolean bothConfirmed() {
        return player1Confirmed && player2Confirmed;
    }

    public void complete() {
        List<ItemStack> player1Items = inventory.getPlayer1Items();
        List<ItemStack> player2Items = inventory.getPlayer2Items();

        // Drop items at opposite players' locations
        for (ItemStack item : player1Items) player2.getWorld().dropItem(player2.getLocation(), item);
        for (ItemStack item : player2Items) player2.getWorld().dropItem(player1.getLocation(), item);

        inventory.close();

        player1.sendMessage("Transaction completed!");
        player2.sendMessage("Transaction completed!");
    }
}
