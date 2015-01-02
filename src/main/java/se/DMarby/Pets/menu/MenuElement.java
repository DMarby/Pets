package se.DMarby.Pets.menu;

import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;

public abstract class MenuElement {

    protected CraftItemStack item;

    /**
     * Fired when this menu element is clicked
     *
     * @param player The player who clicked it
     * @return Whether to close the menu or not
     */
    public abstract Boolean click(Player player);

    public CraftItemStack getItem(Player player) {
        return item;
    }
}
