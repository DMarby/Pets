package se.DMarby.Pets.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R2.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_7_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import se.DMarby.Pets.Pets;
import se.DMarby.Pets.Util;

import java.util.HashMap;

public class Menu extends MenuElement {

    protected String name;
    protected Size size;
    protected HashMap<Integer, MenuElement> elements = new HashMap();

    public Menu (String name, ItemStack item, Size size) {
        this.name = name;
        this.item = CraftItemStack.asCraftCopy(item);
        this.size = size;
    }

    public Menu (String name, Size size) {
        this(name, CraftItemStack.asCraftCopy(new ItemStack(Material.BEDROCK)), size);
    }

    @Deprecated
    public Menu (String name, ItemStack item, Integer size) {
        this.name = name;
        this.item = CraftItemStack.asCraftCopy(item);
        this.size = Size.parse(size);
    }

    @Deprecated
    public Menu (String name, Integer size) {
        this(name, CraftItemStack.asCraftCopy(new ItemStack(Material.BEDROCK)), size);
    }

    @Override
    public Boolean click (Player player) {
        display(player);
        return false;
    }

    public Inventory getInventory (Player player) {
        CraftInventory inventory = (CraftInventory) Bukkit.createInventory(new MenuHolder(this, Bukkit.createInventory(player.getPlayer(), size.getSize())), size.getSize(), name);
        for (Integer key : elements.keySet()) {
            Util.setInventoryItem(key, inventory, elements.get(key).getItem(player));
        }
        return inventory;
    }

    public final void display (Player player) {
        player.getPlayer().openInventory(getInventory(player));
    }

    public final void hide (final Player player) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Pets.getInstance(), new Runnable() {
            public void run() {
                if (player.getPlayer() != null) {
                    player.getPlayer().closeInventory();
                }
            }
        });
    }

    public HashMap<Integer, MenuElement> getElements () {
        return elements;
    }

    public void setSize (Size size) {
        this.size = size;
    }

    public Size getSize () {
        return size;
    }

    public enum Size {
        ONE_LINE(9),
        TWO_LINE(18),
        THREE_LINE(27),
        FOUR_LINE(36),
        FIVE_LINE(45),
        SIX_LINE(54);

        private int size;

        private Size (int i) {
            this.size = i;
        }

        public int getSize () {
            return size;
        }

        public static Size parse (int i) {
            switch (i) {
                case 9:
                    return ONE_LINE;
                case 18:
                    return TWO_LINE;
                case 27:
                    return THREE_LINE;
                case 36:
                    return FOUR_LINE;
                case 45:
                    return FIVE_LINE;
                case 54:
                    return SIX_LINE;
            }
            throw new IllegalArgumentException("Invalid menu size");
        }

        public static Size fit (int i) {
            if (i < 10) {
                return ONE_LINE;
            } else if (i < 19) {
                return TWO_LINE;
            } else if (i < 28) {
                return THREE_LINE;
            } else if (i < 37) {
                return FOUR_LINE;
            } else if (i < 46) {
                return FIVE_LINE;
            } else {
                return SIX_LINE;
            }
        }
    }
}