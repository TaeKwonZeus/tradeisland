package net.cubicworld.tradeisland.trade;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class Transaction {
    private final Player player1;
    private final Player player2;

    private boolean player1Confirmed = false;
    private boolean player2Confirmed = false;

    private final TradeInventory inventory;

    public Transaction(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;

        inventory = new TradeInventory(player1, player2);
        inventory.open();
    }

    public void processInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!player.equals(player1) && !player.equals(player2)) return;

        CellType cellType = TradeInventory.getCellType(event.getSlot());

        event.setCancelled(true);

        if (cellType == CellType.CONFIRM_MAIN) {
            if (player.equals(player1)) {
                player1Confirmed = !player1Confirmed;
                inventory.setPlayer1Confirm(player1Confirmed);
            } else {
                player2Confirmed = !player2Confirmed;
                inventory.setPlayer2Confirm(player2Confirmed);
            }
        } else if (cellType == CellType.ITEM_SLOT_MAIN) {
            // TODO handle setting items
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
