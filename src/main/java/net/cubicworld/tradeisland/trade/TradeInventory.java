package net.cubicworld.tradeisland.trade;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class TradeInventory {
    private static final int ITEM_SLOT_A = 0;
    private static final int ITEM_SLOT_B = 1;
    private static final int CONFIRM_A = 2;
    private static final int CONFIRM_B = 3;
    private static final int HEAD_A = 4;
    private static final int HEAD_B = 5;
    private static final int DECORATION = 6;
    private static final int CURRENCY = 7;

    private static final int[] LAYOUT = {
            6, 6, 6, 6, 6, 6, 6, 6, 6,
            0, 6, 4, 2, 6, 3, 5, 6, 1,
            0, 6, 6, 7, 6, 7, 6, 6, 1,
            0, 0, 6, 6, 6, 6, 6, 1, 1,
            0, 0, 0, 0, 6, 1, 1, 1, 1,
            0, 0, 0, 0, 6, 1, 1, 1, 1
    };

    private static final Material CONFIRM_OFF_MATERIAL = Material.RED_TERRACOTTA;
    private static final Material CONFIRM_ON_MATERIAL = Material.LIME_TERRACOTTA;
    private static final Material DECORATION_MATERIAL = Material.BLUE_STAINED_GLASS_PANE;
    private static final Material CURRENCY_MATERIAL = Material.DIAMOND;

    public static Inventory getInventory(Player player1, Player player2) {
        Inventory inventory = Bukkit.createInventory(null, 54, "Trade");

        for (int i = 0; i < LAYOUT.length; i++) {
            int cell = LAYOUT[i];
            ItemStack item = switch (cell) {
                case ITEM_SLOT_A, ITEM_SLOT_B -> new ItemStack(Material.AIR);
                case CONFIRM_A, CONFIRM_B -> new ItemStack(CONFIRM_OFF_MATERIAL);
                case DECORATION -> new ItemStack(DECORATION_MATERIAL);
                case CURRENCY -> new ItemStack(CURRENCY_MATERIAL);
                case HEAD_A -> getHead(player1);
                case HEAD_B -> getHead(player2);
                default -> throw new IllegalStateException("Unexpected value: " + cell);
            };

            inventory.setItem(i, item);
        }

        return inventory;
    }

    private static ItemStack getHead(Player player) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        if (skullMeta == null) return new ItemStack(Material.FIRE);

        skullMeta.setOwningPlayer(player);
        skull.setItemMeta(skullMeta);

        return skull;
    }
}
