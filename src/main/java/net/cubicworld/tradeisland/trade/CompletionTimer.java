package net.cubicworld.tradeisland.trade;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.cubicworld.tradeisland.util.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
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

            player1.sendMessage(Message.info("trade.completed"));
            player2.sendMessage(Message.info("trade.completed"));

            isRunning = false;
            this.cancel();
            return;
        }

        TextComponent message = Message.info("trade.remaining")
                .append(Component.text(" " + countdown + "...", Message.INFO_COLOR));
        player1.sendMessage(message);
        player2.sendMessage(message);
        --countdown;
    }
}
