package net.cubicworld.tradeisland.trade;

import lombok.Getter;
import org.bukkit.entity.Player;
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

    public boolean usesInventory(Inventory inventory) {
        return this.inventory.usesInventory(inventory);
    }
}
