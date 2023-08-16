package net.cubicworld.tradeisland.trade;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

@Getter
public class Transaction {
    private final Player player1;
    private final Player player2;

    private final Inventory inventory;

    public Transaction(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;

        inventory = TradeInventory.getInventory(player1, player2);

        player1.openInventory(inventory);
        player2.openInventory(inventory);
    }
}
