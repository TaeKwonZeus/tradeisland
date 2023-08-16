package net.cubicworld.tradeisland.trade;

import lombok.RequiredArgsConstructor;
import net.cubicworld.tradeisland.TradeIslandPlugin;
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
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TradeManager implements Listener, CommandExecutor {
    private final TradeIslandPlugin plugin;

    private final List<Transaction> transactions = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;

        player.sendMessage("Transaction processing...");

        return true;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

    }

    private Map<Inventory, Transaction> getInventoryTransactionMap() {
        return transactions.stream().collect(Collectors.toMap(Transaction::getInventory, Function.identity()));
    }
}
