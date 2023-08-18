package net.cubicworld.tradeisland.trade;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TradeInventory {
    private static final int ITEM_SLOT_MAIN = 0;
    private static final int ITEM_SLOT_OTHER = 1;
    private static final int CONFIRM_MAIN = 2;
    private static final int CONFIRM_OTHER = 3;
    private static final int HEAD_MAIN = 4;
    private static final int HEAD_OTHER = 5;
    private static final int DECORATION = 6;

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

    private final Inventory player1Inventory;
    private final Inventory player2Inventory;

    private final Player player1;
    private final Player player2;

    public TradeInventory(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;

        player1Inventory = generateInventory(player1, player2);
        player2Inventory = generateInventory(player2, player1);
    }

    // TODO fix item names
    private Inventory generateInventory(Player main, Player other) {
        Inventory inventory = Bukkit.createInventory(main, 54, "Trade");

        for (int i = 0; i < LAYOUT.length; i++) {
            int cell = LAYOUT[i];
            ItemStack item = switch (cell) {
                case ITEM_SLOT_MAIN, ITEM_SLOT_OTHER -> new ItemStack(Material.AIR);
                case CONFIRM_MAIN -> newItemStackWithName(CONFIRM_OFF_MATERIAL, "Press to confirm");
                case CONFIRM_OTHER ->
                        newItemStackWithName(CONFIRM_OFF_MATERIAL, other.getDisplayName() + " confirmation");
                case DECORATION -> newItemStackWithName(DECORATION_MATERIAL, "");
                case HEAD_MAIN -> getHead(main);
                case HEAD_OTHER -> getHead(other);
                default -> throw new IllegalStateException("Unexpected value: " + cell);
            };

            inventory.setItem(i, item);
        }

        return inventory;
    }

    private ItemStack newItemStackWithName(Material material, String name) {
        ItemStack itemStack = new ItemStack(material);
        Objects.requireNonNull(itemStack.getItemMeta()).setDisplayName(name);

        return itemStack;
    }

    public void open() {
        player1.openInventory(player1Inventory);
        player2.openInventory(player2Inventory);
    }

    public void close() {
        if (player1.getOpenInventory().getTopInventory() == player1Inventory) {
            player1.closeInventory();
        }
        if (player2.getOpenInventory().getTopInventory() == player2Inventory) {
            player2.closeInventory();
        }
    }

    public static CellType getCellType(int index) {
        return switch (LAYOUT[index]) {
            case ITEM_SLOT_MAIN -> CellType.ITEM_SLOT_MAIN;
            case ITEM_SLOT_OTHER -> CellType.ITEM_SLOT_OTHER;
            case CONFIRM_MAIN -> CellType.CONFIRM_MAIN;
            case CONFIRM_OTHER -> CellType.CONFIRM_OTHER;
            case HEAD_MAIN -> CellType.HEAD_MAIN;
            case HEAD_OTHER -> CellType.HEAD_OTHER;
            case DECORATION -> CellType.DECORATION;
            default -> throw new IllegalStateException("Unexpected value: " + LAYOUT[index]);
        };
    }

    public List<ItemStack> getPlayer1Items() {
        return getIndices(ITEM_SLOT_MAIN).stream()
                .map(player1Inventory::getItem)
                .filter(Objects::nonNull)
                .toList();
    }

    public List<ItemStack> getPlayer2Items() {
        return getIndices(ITEM_SLOT_MAIN).stream()
                .map(player2Inventory::getItem)
                .filter(Objects::nonNull)
                .toList();
    }

    public boolean usesInventory(Inventory inventory) {
        return inventory.equals(player1Inventory) || inventory.equals(player2Inventory);
    }

    public void setPlayer1Confirm(boolean value) {
        setPlayerConfirm(value, player1Inventory, player2Inventory);
    }

    public void setPlayer2Confirm(boolean value) {
        setPlayerConfirm(value, player2Inventory, player1Inventory);
    }

    private void setPlayerConfirm(boolean value, Inventory mainInventory, Inventory otherInventory) {
        for (int i = 0; i < LAYOUT.length; i++) {
            if (LAYOUT[i] == CONFIRM_MAIN)
                mainInventory.setItem(i, value ? new ItemStack(CONFIRM_ON_MATERIAL) : new ItemStack(CONFIRM_OFF_MATERIAL));
            if (LAYOUT[i] == CONFIRM_OTHER)
                otherInventory.setItem(i, value ? new ItemStack(CONFIRM_ON_MATERIAL) : new ItemStack(CONFIRM_OFF_MATERIAL));
        }
    }

    // TODO handle adding items

    private List<Integer> getIndices(int cellType) {
        List<Integer> indices = new ArrayList<>();

        for (int i = 0; i < LAYOUT.length; i++) {
            if (LAYOUT[i] == cellType) {
                indices.add(i);
            }
        }

        return indices;
    }

    private static ItemStack getHead(Player player) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        if (skullMeta == null) return new ItemStack(Material.FIRE);

        skullMeta.setOwningPlayer(player);
        skullMeta.setDisplayName(player.getDisplayName());

        skull.setItemMeta(skullMeta);

        return skull;
    }
}
