package net.cubicworld.tradeisland.trade;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

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

    public TransactionStatus processInventoryClick(InventoryClickEvent event) {
        int cellType = TradeInventory.getCellType(event.getSlot());
        switch (cellType) {
            case TradeInventory.DECORATION, TradeInventory.HEAD_A, TradeInventory.HEAD_B -> event.setCancelled(true);
            case TradeInventory.CONFIRM_PLAYER_1 -> {
                event.setCancelled(true);
                player1Confirm = !player1Confirm;
                inventory.setPlayer1Confirm(player1Confirm);
                if (checkConfirmation()) {
                    return TransactionStatus.CLOSED;
                }
            }
            case TradeInventory.CONFIRM_PLAYER_2 -> {
                event.setCancelled(true);
                player2Confirm = !player2Confirm;
                inventory.setPlayer2Confirm(player2Confirm);
                if (checkConfirmation()) {
                    return TransactionStatus.CLOSED;
                }
            }
        }

        return TransactionStatus.PROCESSING;
    }

    public boolean usesInventory(Inventory inventory) {
        return this.inventory.usesInventory(inventory);
    }

    private boolean checkConfirmation() {
        if (!player1Confirm || !player2Confirm) return false;

        inventory.close();

        player1.sendMessage("Transaction commencing...");
        player2.sendMessage("Transaction commencing...");

        return true;
    }
}
