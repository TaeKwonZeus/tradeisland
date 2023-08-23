package net.cubicworld.tradeisland.trade;

import net.cubicworld.tradeisland.TradeIslandPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Transaction {
    private final TradeIslandPlugin plugin;

    private final Player player1;
    private final Player player2;

    private boolean player1Confirmed = false;
    private boolean player2Confirmed = false;

    private final TradeInventory inventory;

    private CompletionTimer completionTimer;

    public boolean isCompleted() {
        return completionTimer.isCompleted();
    }

    public Transaction(TradeIslandPlugin plugin, Player player1, Player player2) {
        this.plugin = plugin;
        this.player1 = player1;
        this.player2 = player2;

        inventory = new TradeInventory(player1, player2);
        Bukkit.getScheduler().runTask(plugin, inventory::open);

        completionTimer = new CompletionTimer(player1, player2, inventory);
    }

    public void processInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!player.equals(player1) && !player.equals(player2)) return;

        CellType cellType = TradeInventory.getCellType(event.getSlot());

        if (cellType == CellType.CONFIRM_MAIN) {
            event.setCancelled(true);
            if (player.equals(player1)) {
                setPlayer1Confirmed(!player1Confirmed);
            } else {
                setPlayer2Confirmed(!player2Confirmed);
            }
        } else if (cellType == CellType.ITEM_SLOT_MAIN) {
            setPlayer1Confirmed(false);
            setPlayer2Confirmed(false);
            Bukkit.getScheduler().runTask(plugin, () -> inventory.reflectItemChange(player, event.getSlot()));
        } else {
            event.setCancelled(true);
        }

        Bukkit.getScheduler().runTask(plugin, () -> {
           if (bothConfirmed()) complete();
           else if (completionTimer.isRunning()) {
               completionTimer.cancel();
               completionTimer = new CompletionTimer(player1, player2, inventory);

               player1.sendMessage("Transaction suspended!");
               player2.sendMessage("Transaction suspended!");
           }
        });
    }

    public boolean usesInventory(Inventory inventory) {
        return this.inventory.usesInventory(inventory);
    }

    public boolean bothConfirmed() {
        return player1Confirmed && player2Confirmed;
    }

    public void complete() {
        completionTimer.runTaskTimer(plugin, 0L, 20L);
    }

    public void cancel() {
        List<ItemStack> player1Items = inventory.getPlayer1Items();
        List<ItemStack> player2Items = inventory.getPlayer2Items();

        // Drop items at opposite players' locations
        for (ItemStack item : player1Items) player1.getWorld().dropItem(player1.getLocation(), item);
        for (ItemStack item : player2Items) player2.getWorld().dropItem(player2.getLocation(), item);

        Bukkit.getScheduler().runTask(plugin, inventory::close);

        if (completionTimer.isRunning()) {
            completionTimer.cancel();
        }

        player1.sendMessage("Transaction cancelled!");
        player2.sendMessage("Transaction cancelled!");
    }

    private void setPlayer1Confirmed(boolean confirmed) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            player1Confirmed = confirmed;
            inventory.setPlayer1Confirmed(confirmed);
        });
    }

    private void setPlayer2Confirmed(boolean confirmed) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            player2Confirmed = confirmed;
            inventory.setPlayer2Confirmed(confirmed);
        });
    }
}
