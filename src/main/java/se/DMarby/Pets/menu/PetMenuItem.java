package se.DMarby.Pets.menu;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import se.DMarby.Pets.Util;

import java.util.Arrays;
import java.util.List;

public class PetMenuItem {

    private CraftItemStack allow;
    private CraftItemStack deny;
    private String s;
    private String name;

    public short getShort(String mob) {
        switch (mob) {
            case "zombie":
                return EntityType.ZOMBIE.getTypeId();
            case "zombiepigman":
                return EntityType.PIG_ZOMBIE.getTypeId();
            case "blackcat":
            case "ocelot":
            case "redcat":
            case "siamesecat":
            case "bigcat":
                return EntityType.OCELOT.getTypeId();
            case "wolf":
            case "dog":
                return EntityType.WOLF.getTypeId();
            case "greenvillager":
            case "villager":
            case "zombievillager":
                return EntityType.VILLAGER.getTypeId();
            case "bat":
                return EntityType.BAT.getTypeId();
            case "magmacube":
                return EntityType.MAGMA_CUBE.getTypeId();
            case "mooshroom":
                return EntityType.MUSHROOM_COW.getTypeId();
            case "cavespider":
                return EntityType.CAVE_SPIDER.getTypeId();
            case "silverfish":
                return EntityType.SILVERFISH.getTypeId();
            case "slime":
                return EntityType.SLIME.getTypeId();
            case "chicken":
                return EntityType.CHICKEN.getTypeId();
            case "cow":
                return EntityType.COW.getTypeId();
            case "pig":
                return EntityType.PIG.getTypeId();
            case "irongolem":
                return EntityType.IRON_GOLEM.getTypeId();
            case "creeper":
            case "electrocreeper":
                return EntityType.CREEPER.getTypeId();
            case "blaze":
                return EntityType.BLAZE.getTypeId();
            case "squid":
                return EntityType.SQUID.getTypeId();
            case "undeadhorse":
            case "horse":
            case "mule":
            case "donkey":
            case "skeletonhorse":
            case "whitehorse":
            case "ridablehorse":
                return EntityType.HORSE.getTypeId();
            case "enderman":
                return EntityType.ENDERMAN.getTypeId();
            case "spider":
                return EntityType.SPIDER.getTypeId();
            case "ghast":
                return EntityType.GHAST.getTypeId();
            case "witch":
                return EntityType.WITCH.getTypeId();
            case "skeleton":
                return EntityType.SKELETON.getTypeId();
            case "witherskeleton":
            case "wither":
            case "bluewither":
            case "witherskull":
            case "bluewitherskull":
                return EntityType.WITHER.getTypeId();
            default:
                return 0;
        }
    }

    public PetMenuItem(String s) {
        this.s = s;
        CraftItemStack[] stacks;
        String[] split = s.split("\\.");
        this.name = split[1];
        List<String> description = Arrays.asList(ChatColor.GRAY + "A Pet");
        if (s.endsWith("sheep")) {
            String color = name.replace("sheep", "");
            String the_name = color.substring(0, 1).toUpperCase() + color.substring(1);
            if (color.equalsIgnoreCase("lightblue")) {
                the_name = "Lightblue";
                stacks = Util.makeCraftItemStacks(new ItemStack(Material.WOOL, 1, DyeColor.LIGHT_BLUE.getWoolData()), the_name + " sheep", description, new ChatColor[]{ChatColor.GREEN, ChatColor.RED}, new Boolean[]{false, false});
            } else {
                stacks = Util.makeCraftItemStacks(new ItemStack(Material.WOOL, 1, DyeColor.valueOf(color.toUpperCase()).getWoolData()), the_name + " sheep", description, new ChatColor[]{ChatColor.GREEN, ChatColor.RED}, new Boolean[]{false, false});
            }
        } else {
            String the_name = name.substring(0, 1).toUpperCase() + name.substring(1);
            if (name.equalsIgnoreCase("bigcat")) {
                the_name = "Adult Ocelot";
            } else if (name.endsWith("horse") && !name.equalsIgnoreCase("horse")) {
                String temp = name.replace("horse", "");
                the_name = temp.substring(0, 1).toUpperCase() + temp.substring(1) + " horse";
            } else if (name.endsWith("cat") && !name.equalsIgnoreCase("cat")) {
                String temp = name.replace("cat", "");
                the_name = temp.substring(0, 1).toUpperCase() + temp.substring(1) + " cat";
            } else if (name.endsWith("villager") && !name.equalsIgnoreCase("villager")) {
                String temp = name.replace("villager", "");
                the_name = temp.substring(0, 1).toUpperCase() + temp.substring(1) + " villager";
            } else if (name.equalsIgnoreCase("zombiepigman")) {
                the_name = "Zombie pigman";
            } else if (name.equalsIgnoreCase("irongolem")) {
                the_name = "Iron Golem";
            } else if (name.equalsIgnoreCase("witherskull")) {
                the_name = "Wither Skull";
            } else if (name.equalsIgnoreCase("bluewitherskull")) {
                the_name = "Blue Wither Skull";
            } else if (name.equalsIgnoreCase("bluewither")) {
                the_name = "Blue Wither";
            } else if (name.equalsIgnoreCase("witherskeleton")) {
                the_name = "Wither Skeleton";
            }

            if (name.equalsIgnoreCase("ridablehorse")) {
                description = Arrays.asList(ChatColor.GOLD + "Ridable Pet");
            }
            stacks = Util.makeCraftItemStacks(new ItemStack(Material.MONSTER_EGG, 1, getShort(name.toLowerCase())), the_name, description, new ChatColor[]{ChatColor.GREEN, ChatColor.RED}, new Boolean[]{false, false});
        }
        allow = stacks[0];
        deny = stacks[1];
    }

    public Boolean hasPermission(Player player) {
        return player.hasPermission(s);
    }

    public String getName() {
        return name;
    }

    public CraftItemStack getItem(Player player) {
        if (hasPermission(player)) {
            return allow;
        }
        return deny;
    }

}