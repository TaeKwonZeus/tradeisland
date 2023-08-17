package net.cubicworld.tradeisland.trade;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class TradeInventory {
    public static final int ITEM_SLOT_PLAYER_1 = 0;
    public static final int ITEM_SLOT_PLAYER_2 = 1;
    public static final int CONFIRM_PLAYER_1 = 2;
    public static final int CONFIRM_PLAYER_2 = 3;
    public static final int HEAD_A = 4;
    public static final int HEAD_B = 5;
    public static final int DECORATION = 6;

    private static final int[] LAYOUT = {
            6, 6, 6, 6, 6, 6, 6, 6, 6,
            0, 6, 4, 2, 6, 3, 5, 6, 1,
            0, 6, 6, 6, 6, 6, 6, 6, 1,
            0, 0, 0, 0, 6, 1, 1, 1, 1,
            0, 0, 0, 0, 6, 1, 1, 1, 1,
            0, 0, 0, 0, 6, 1, 1, 1, 1
    };

    private static final Material CONFIRM_OFF_MATERIAL = Material.RED_TERRACOTTA;
    private static final Material CONFIRM_ON_MATERIAL = Material.LIME_TERRACOTTA;
    private static final Material DECORATION_MATERIAL = Material.BLUE_STAINED_GLASS_PANE;
    private static final Material CURRENCY_MATERIAL = Material.DIAMOND;

    public static int getCellType(int index) {
        return LAYOUT[index];
    }

    private final Inventory inventory;

    private final Player player1;
    private final Player player2;

    public TradeInventory(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;

        inventory = Bukkit.createInventory(null, 54, "Trade");

        for (int i = 0; i < LAYOUT.length; i++) {
            int cell = LAYOUT[i];
            ItemStack item = switch (cell) {
                case ITEM_SLOT_PLAYER_1, ITEM_SLOT_PLAYER_2 -> new ItemStack(Material.AIR);
                case CONFIRM_PLAYER_1, CONFIRM_PLAYER_2 -> new ItemStack(CONFIRM_OFF_MATERIAL);
                case DECORATION -> new ItemStack(DECORATION_MATERIAL);
                case HEAD_A -> getHead(player1);
                case HEAD_B -> getHead(player2);
                default -> throw new IllegalStateException("Unexpected value: " + cell);
            };

            inventory.setItem(i, item);
        }
    }

    public void open() {
        player1.openInventory(inventory);
        player2.openInventory(inventory);
    }

    public void close() {
        if (player1.getOpenInventory().getTopInventory() == inventory) {
            player1.closeInventory();
        }
        if (player2.getOpenInventory().getTopInventory() == inventory) {
            player2.closeInventory();
        }
    }

    public boolean usesInventory(Inventory inventory) {
        return this.inventory.equals(inventory);
    }

    public void setPlayer1Confirm(boolean value) {
        setConfirm(value, CONFIRM_PLAYER_1);
    }

    public void setPlayer2Confirm(boolean value) {
        setConfirm(value, CONFIRM_PLAYER_2);
    }

    private void setConfirm(boolean value, int confirmCell) {
        for (int i = 0; i < LAYOUT.length; i++) {
            if (LAYOUT[i] != confirmCell) continue;

            if (value) {
                inventory.setItem(i, new ItemStack(CONFIRM_ON_MATERIAL));
            } else {
                inventory.setItem(i, new ItemStack(CONFIRM_OFF_MATERIAL));
            }
        }
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
