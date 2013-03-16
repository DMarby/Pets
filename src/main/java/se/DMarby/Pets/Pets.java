package se.DMarby.Pets;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Strings;

public class Pets extends JavaPlugin {

    private PetController controller;
    private Storage storage;
    private final List<String> pets = new ArrayList();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!label.equalsIgnoreCase("pet")) {
            return false;
        }
        if (args.length == 1) {
            if (!sender.hasPermission("pet.toggle")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to do this!");
                return true;
            }
            if (args[0].equalsIgnoreCase("list")) {
                String list = "";
                for (String pet : pets) {
                    if (sender.hasPermission("pet." + pet.toLowerCase())) {
                        String the_pet = pet.substring(0, 1).toUpperCase() + pet.substring(1);
                        list += the_pet + ", ";
                    }
                }
                if (list.length() <= 1) {
                    sender.sendMessage(ChatColor.GRAY + "No available pets.");
                    return true;
                }
                sender.sendMessage(ChatColor.GREEN + "Available pets:");
                sender.sendMessage(ChatColor.GREEN + list.substring(0, list.length() - 2));
                return true;
            }

            if (!sender.hasPermission("pet." + args[0].toLowerCase())) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to do this!");
                return true;
            }
            if (!pets.contains(args[0].toLowerCase())) {
                sender.sendMessage(ChatColor.RED
                        + "Invalid pet type! Do /pet list to get a list of all available types.");
                return true;
            }

            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.GRAY + "Must be ingame.");
                return true;
            }
            controller.togglePet((Player) sender, args[0]);
            return true;
        } else if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.GRAY + "Must be ingame.");
                return true;
            }
            if (!sender.hasPermission("pet.toggle")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to do this!");
                return true;
            }
            if (Strings.isNullOrEmpty(controller.getType((Player) sender))) {
                sender.sendMessage(ChatColor.RED + "No pet available.");
                return true;
            }
            controller.togglePet((Player) sender);
            return true;
        }else if (args.length == 2){
            if(args[0].equalsIgnoreCase("name")){
                if (!sender.hasPermission("pet.name")) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission to do this!");
                    return true;
                }
                controller.setName((Player) sender, args[1]);
                sender.sendMessage(ChatColor.GREEN + "Pet name changed to " + args[1]);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDisable() {
        storage.save();
    }

    @Override
    public void onEnable() {
        pets.add("blaze");
        pets.add("cavespider");
        pets.add("chicken");
        pets.add("cow");
        pets.add("magmacube");
        pets.add("mooshroom");
        pets.add("ocelot");
        pets.add("pig");
        pets.add("sheep");
        pets.add("silverfish");
        pets.add("slime");
        pets.add("snowman");
        pets.add("villager");
        pets.add("wolf");
        pets.add("creeper");
        pets.add("greenvillager");
        pets.add("bat");
        pets.add("squid");
        pets.add("zombie");
        pets.add("babyzombie");
        pets.add("zombiepigman");
        pets.add("babyzombiepigman");
        pets.add("zombievillager");
        pets.add("babyzombievillager");
        pets.add("irongolem");
        pets.add("cat");
        pets.add("dog");
        pets.add("electrocreeper");


        pets.add("blacksheep");
        pets.add("bluesheep");
        pets.add("brownsheep");
        pets.add("cyansheep");
        pets.add("graysheep");
        pets.add("greensheep");
        pets.add("lightbluesheep");
        pets.add("limesheep");
        pets.add("magentasheep");
        pets.add("orangesheep");
        pets.add("pinksheep");
        pets.add("purplesheep");
        pets.add("redsheep");
        pets.add("silversheep");
        pets.add("whitesheep");
        pets.add("yellowsheep");

        pets.add("wildcat");
        pets.add("blackcat");
        pets.add("redcat");
        pets.add("siamesecat");

        Util.load(getConfig());
        File dataFile = new File(getDataFolder(), "saves.yml");
        controller = new PetController(this);
        storage = new Storage(dataFile, controller);
        storage.load();
        Bukkit.getPluginManager().registerEvents(controller, this);
        Bukkit.getPluginManager().registerEvents(storage, this);
        saveConfig();
    }
}
