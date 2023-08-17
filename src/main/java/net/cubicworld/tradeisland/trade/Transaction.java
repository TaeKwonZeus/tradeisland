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

    private boolean player1Confirm = false;
    private boolean player2Confirm = false;

    private final TradeInventory inventory;

    public Transaction(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;

        inventory = new TradeInventory(player1, player2);
        inventory.open();
    }

    public void processInventoryClick(InventoryClickEvent event) {
        int cellType = TradeInventory.getCellType(event.getSlot());

        switch (cellType) {
            case TradeInventory.DECORATION, TradeInventory.HEAD_A, TradeInventory.HEAD_B -> event.setCancelled(true);
            case TradeInventory.CONFIRM_PLAYER_1 -> {
                event.setCancelled(true);
                if (!event.getWhoClicked().equals(player1)) return;
                player1Confirm = !player1Confirm;
                inventory.setPlayer1Confirm(player1Confirm);
            }
            case TradeInventory.CONFIRM_PLAYER_2 -> {
                event.setCancelled(true);
                if (!event.getWhoClicked().equals(player2)) return;
                player2Confirm = !player2Confirm;
                inventory.setPlayer2Confirm(player2Confirm);
            }
            case TradeInventory.ITEM_SLOT_PLAYER_1 -> {
                if (!event.getWhoClicked().equals(player1)) event.setCancelled(true);
            }
            case TradeInventory.ITEM_SLOT_PLAYER_2 -> {
                if (!event.getWhoClicked().equals(player2)) event.setCancelled(true);
            }
        }
    }

    public boolean usesInventory(Inventory inventory) {
        return this.inventory.usesInventory(inventory);
    }

    public boolean bothConfirmed() {
        return player1Confirm && player2Confirm;
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
