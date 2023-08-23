package net.cubicworld.tradeisland.trade;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

@RequiredArgsConstructor
public class CompletionTimer extends BukkitRunnable {
    private final Player player1;
    private final Player player2;
    private final TradeInventory inventory;

    private int countdown = 3;

    @Getter
    private boolean isRunning = false;
    @Getter
    private boolean isCompleted = false;

    @Override
    public void run() {
        isRunning = true;

        if (countdown == 0) {
            isCompleted = true;

            List<ItemStack> player1Items = inventory.getPlayer1Items();
            List<ItemStack> player2Items = inventory.getPlayer2Items();

            // Drop items at opposite players' locations
            for (ItemStack item : player1Items) player2.getWorld().dropItem(player2.getLocation(), item);
            for (ItemStack item : player2Items) player1.getWorld().dropItem(player1.getLocation(), item);

            inventory.close();

            player1.sendMessage("Transaction completed!");
            player2.sendMessage("Transaction completed!");

            this.cancel();
            return;
        }

        player1.sendMessage("Transaction completed in " + countdown + " seconds...");
        player2.sendMessage("Transaction completed in " + countdown + " seconds...");
        --countdown;
    }
}
