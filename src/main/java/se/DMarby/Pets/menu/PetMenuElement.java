package se.DMarby.Pets.menu;

import org.bukkit.craftbukkit.v1_7_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import se.DMarby.Pets.Pets;
import se.DMarby.Pets.Util;

public class PetMenuElement extends MenuElement {

    private PetMenuItem petMenuItem;


    public PetMenuElement (PetMenuItem item) {
        this.petMenuItem = item;
    }

    @Override
    public Boolean click (Player player) {
        if (!petMenuItem.hasPermission(player)) {
            player.getPlayer().sendMessage(Util.PERMISSIONS_MESSAGE);
            return true;
        }
        Pets.getInstance().controller.togglePet(player.getPlayer(), petMenuItem.getName());
        return true;
    }

    @Override
    public CraftItemStack getItem (Player player) {
        return petMenuItem.getItem(player);
    }
}
