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
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class TradeManager implements Listener, CommandExecutor {
    private final TradeIslandPlugin plugin;

    private final List<Transaction> transactions = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;
        if (args.length == 0) return false;

        Player other = Bukkit.getPlayer(args[0]);
        if (other == null) {
            player.sendMessage("Player not found!");
            return true;
        }

        transactions.add(new Transaction(player, other));

        return true;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Transaction transaction = getTransaction(event.getClickedInventory());
        if (!transactions.contains(transaction)) return;

        transaction.processInventoryClick(event);

        event.getWhoClicked().sendMessage("Clicked in trade inventory!");
    }

    private Transaction getTransaction(Inventory inventory) {
        return transactions.stream()
                .filter(t -> t.usesInventory(inventory))
                .findFirst()
                .orElse(null);
    }
}
