package net.cubicworld.tradeisland.trade;

import lombok.RequiredArgsConstructor;
import net.cubicworld.tradeisland.TradeIslandPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class TradeManager implements Listener, CommandExecutor {
    private final TradeIslandPlugin plugin;

    private final List<Transaction> transactions = new ArrayList<>();

    // TODO change simple text messages to TextComponents
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;
        if (args.length == 0) return false;

        Player other = Bukkit.getPlayer(args[0]);
        if (other == null) {
            player.sendMessage("Player not found!");
            return true;
        }

        if (other.equals(player)) {
            player.sendMessage("You can't trade with yourself!");
            return true;
        }

        transactions.add(new Transaction(plugin, player, other));

        return true;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Transaction transaction = getTransaction(event.getClickedInventory());
        if (transaction == null) return;

        transaction.processInventoryClick(event);

        if (transaction.isCompleted()) transactions.remove(transaction);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Transaction transaction = getTransaction(event.getInventory());
        if (transaction == null) return;

        if (!transaction.isCompleted()) transaction.cancel();
        transactions.remove(transaction);
    }

    private Transaction getTransaction(Inventory inventory) {
        if (inventory == null) return null;

        return transactions.stream()
                .filter(t -> t.usesInventory(inventory))
                .findFirst()
                .orElse(null);
    }
}
