package se.DMarby.Pets.menu;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class MenuHolder implements InventoryHolder {

    private Inventory inventory;
    private Menu menu;

    public MenuHolder (Menu menu, Inventory inventory) {
        this.inventory = inventory;
        this.menu = menu;
    }

    public Inventory getInventory () {
        return inventory;
    }

    public Menu getMenu () {
        return menu;
    }

}