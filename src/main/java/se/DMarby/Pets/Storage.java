package se.DMarby.Pets;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.google.common.io.Files;

public class Storage implements Listener {

    private final PetController backend;
    private final File dataFile;
    private final FileConfiguration store;

    Storage(File file, PetController controller) {
        try {
            Files.createParentDirs(file);
        } catch (IOException e) {
        }
        dataFile = file;
        store = YamlConfiguration.loadConfiguration(file);
        backend = controller;
    }

    /*private long getAliveTime(Player player) {
    return store.getLong(player.getName() + ".alivetime", 0);
    }*/
    private String getType(Player player) {
        return store.getString(player.getName() + ".type");
    }

    private String getName(Player player) {
        return store.getString(player.getName() + ".name");
    }
    
    private String getItem(Player player) {
        return store.getString(player.getName() + ".item");
    }

    private boolean isEnabled(Player player) {
        return store.getBoolean(player.getName() + ".active", false);
    }

    public void load() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            loadPlayer(player);
        }
    }

    private void loadPlayer(Player player) {
        if (!player.hasPermission("pet.toggle")) {
            return;
        }
        // backend.loadPlayer(player, getAliveTime(player), isEnabled(player), getType(player));
        backend.loadPlayer(player, isEnabled(player), getType(player), getName(player), getItem(player));
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        loadPlayer(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerLeave(PlayerQuitEvent event) {
        savePlayer(event.getPlayer());
    }

    public void save() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            savePlayer(player);
        }

        try {
            File temp = File.createTempFile("pet", null, dataFile.getParentFile());
            temp.deleteOnExit();
            store.save(temp);
            temp.renameTo(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void savePlayer(Player player) {
        /*if (!player.hasPermission("pet.spawn"))
        return;
        long newAliveTime = backend.getAliveTime(player);
        store.set(player.getName() + ".alivetime", newAliveTime); */
        store.set(player.getName() + ".active", backend.isActive(player));
        String type = backend.getType(player);
        if (type != null) {
            store.set(player.getName() + ".type", type);
        }
        String name = backend.getName(player);
        String item = backend.getItem(player);
        store.set(player.getName() + ".name", name);
        store.set(player.getName() + ".item", item);
        backend.removePet(player, true);
    }
}
