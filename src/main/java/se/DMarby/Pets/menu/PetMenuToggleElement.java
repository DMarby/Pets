package se.DMarby.Pets.menu;

import com.google.common.base.Strings;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import se.DMarby.Pets.Pets;
import se.DMarby.Pets.Util;

import java.util.Arrays;

public class PetMenuToggleElement extends MenuElement {

    public PetMenuToggleElement() {
    }

    @Override
    public Boolean click(Player player) {
        if (Strings.isNullOrEmpty(Pets.getInstance().controller.getType(player.getPlayer()))) {
            player.getPlayer().sendMessage(ChatColor.RED + "You need to select a pet before you can toggle it!");
            return true;
        }
        if (!player.hasPermission("pet.toggle") && !player.hasPermission("pet.admin")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to do this!");
            return true;
        }
        Pets.getInstance().controller.togglePet(player.getPlayer());
        return true;
    }

    @Override
    public CraftItemStack getItem(Player player) {
        return Util.makeItemStack(Material.BONE, 1, ChatColor.GREEN + "Toggle pet", Arrays.asList(ChatColor.GRAY + "Toggles your pet on and off."));
    }
}
