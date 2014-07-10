package se.DMarby.Pets.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class MenuManager implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            if (event.getInventory().getHolder() instanceof MenuHolder) {
                Menu openMenu = ((MenuHolder) event.getInventory().getHolder()).getMenu();
                if (event.getRawSlot() < openMenu.getSize().getSize() && event.getRawSlot() >= 0 && openMenu.getElements().containsKey(event.getSlot())) {
                    if (event.getSlotType() == InventoryType.SlotType.CONTAINER) {
                        int slot = event.getSlot();
                        MenuElement item = openMenu.getElements().get(slot);
                        if (item != null) {
                            Player player = Bukkit.getPlayer(event.getWhoClicked().getName());
                            if (item.click(player)) {
                                openMenu.hide(player);
                            }
                        }
                    }
                }
                event.setCancelled(true);
            }
        }
    }
}
