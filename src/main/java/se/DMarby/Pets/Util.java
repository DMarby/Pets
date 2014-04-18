package se.DMarby.Pets;

import net.minecraft.server.v1_7_R3.Entity;
import net.minecraft.server.v1_7_R3.*;
import net.minecraft.server.v1_7_R3.World;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_7_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R3.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_7_R3.inventory.CraftItemStack;
import org.bukkit.entity.*;
import org.bukkit.entity.Ocelot.Type;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import se.DMarby.Pets.pet.*;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("unchecked")
public class Util {

    private static Map<Class<? extends Entity>, Integer> ENTITY_CLASS_TO_INT;
    private static Map<Integer, Class<? extends Entity>> ENTITY_INT_TO_CLASS;
    private static List<String> spawned = new ArrayList();
    public static String PERMISSIONS_MESSAGE;
    public static double MAX_DISTANCE = 10 * 10;
    public static boolean removeInFight = false;
    public static int MAX_LEVEL = -1;
    private static Field GOAL_FIELD;
    private static DyeColor[] colors = DyeColor.values();
    private static Profession[] professions = Profession.values();
    private static Random rand = new Random();
    private static List<Integer> allvariants = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 256, 257, 258, 259, 260, 261, 262, 512, 513, 514, 515, 516, 517, 518, 768, 769, 770, 771, 772, 773, 774, 1024, 1025, 1026, 1027, 1028, 1029, 1030);
    private static List<ItemStack> armor = Arrays.asList(new ItemStack(417), new ItemStack(418), new ItemStack(419));

    public static void clearGoals (PathfinderGoalSelector... goalSelectors) {
        if (GOAL_FIELD == null || goalSelectors == null) {
            return;
        }
        for (PathfinderGoalSelector selector : goalSelectors) {
            try {
                List<?> list = (List<?>) GOAL_FIELD.get(selector);
                list.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Field getField (Class<?> clazz, String field) {
        Field f = null;
        try {
            f = clazz.getDeclaredField(field);
            f.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    public static void load (FileConfiguration config) {
        /* if (!config.isSet("pet.max-distance-squared")) {
        config.set("pet.max-distance-squared", MAX_DISTANCE);
        }
        MAX_DISTANCE = config.getDouble("pet.max-distance-squared");
        if (!config.isSet("pet.max-level")) {
        config.set("pet.max-level", MAX_LEVEL);
        }
        MAX_LEVEL = config.getInt("pet.max-level");*/
        if (!config.isSet("remove-in-fight")) {
            config.set("remove-in-fight", false);
        }
        if (!config.isSet("permissions_message")) {
            config.set("permissions_message", "§cYou do not have access to this pet!");
        }
        PERMISSIONS_MESSAGE = config.getString("permissions_message");
        removeInFight = config.getBoolean("remove-in-fight");
    }

    public static void registerEntityClass (Class<? extends Entity> clazz) {
        if (ENTITY_CLASS_TO_INT.containsKey(clazz)) {
            return;
        }
        Class<?> search = clazz;
        while ((search = search.getSuperclass()) != null && Entity.class.isAssignableFrom(search)) {
            if (!ENTITY_CLASS_TO_INT.containsKey(search)) {
                continue;
            }
            int code = ENTITY_CLASS_TO_INT.get(search);
            ENTITY_INT_TO_CLASS.put(code, clazz);
            ENTITY_CLASS_TO_INT.put(clazz, code);
            return;
        }
        throw new IllegalArgumentException("unable to find valid entity superclass");
    }

    public static org.bukkit.entity.Entity spawnPet (Player player, String pet) {
        World world = ((CraftWorld) player.getWorld()).getHandle();
        Entity entity = null;
        if (pet.equalsIgnoreCase("slime")) {
            entity = new EntitySlimePet(world, player);
            ((Slime) entity.getBukkitEntity()).setSize(1);
        } else if (pet.equalsIgnoreCase("blaze")) {
            entity = new EntityBlazePet(world, player);
        } else if (pet.equalsIgnoreCase("cavespider")) {
            entity = new EntityCaveSpiderPet(world, player);
        } else if (pet.equalsIgnoreCase("chicken")) {
            entity = new EntityChickenPet(world, player);
            ((Chicken) entity.getBukkitEntity()).setBaby();
            ((Chicken) entity.getBukkitEntity()).setAgeLock(true);
        } else if (pet.equalsIgnoreCase("cow")) {
            entity = new EntityCowPet(world, player);
            ((Cow) entity.getBukkitEntity()).setBaby();
            ((Cow) entity.getBukkitEntity()).setAgeLock(true);
        } else if (pet.equalsIgnoreCase("magmacube")) {
            entity = new EntityMagmaCubePet(world, player);
            ((MagmaCube) entity.getBukkitEntity()).setSize(1);
        } else if (pet.equalsIgnoreCase("mooshroom")) {
            entity = new EntityMushroomCowPet(world, player);
            ((MushroomCow) entity.getBukkitEntity()).setBaby();
            ((MushroomCow) entity.getBukkitEntity()).setAgeLock(true);
        } else if (pet.equalsIgnoreCase("ocelot") || pet.equalsIgnoreCase("wildcat")) {
            entity = new EntityOcelotPet(world, player);
            ((Ocelot) entity.getBukkitEntity()).setBaby();
            ((Ocelot) entity.getBukkitEntity()).setCatType(Type.WILD_OCELOT);
            ((Ocelot) entity.getBukkitEntity()).setAgeLock(true);
        } else if (pet.equalsIgnoreCase("cat") || pet.equalsIgnoreCase("blackcat")) {
            entity = new EntityOcelotPet(world, player);
            ((Ocelot) entity.getBukkitEntity()).setBaby();
            ((Ocelot) entity.getBukkitEntity()).setCatType(Type.BLACK_CAT);
            ((Ocelot) entity.getBukkitEntity()).setAgeLock(true);
        } else if (pet.equalsIgnoreCase("redcat")) {
            entity = new EntityOcelotPet(world, player);
            ((Ocelot) entity.getBukkitEntity()).setBaby();
            ((Ocelot) entity.getBukkitEntity()).setCatType(Type.RED_CAT);
            ((Ocelot) entity.getBukkitEntity()).setAgeLock(true);
        } else if (pet.equalsIgnoreCase("siamesecat")) {
            entity = new EntityOcelotPet(world, player);
            ((Ocelot) entity.getBukkitEntity()).setBaby();
            ((Ocelot) entity.getBukkitEntity()).setCatType(Type.SIAMESE_CAT);
            ((Ocelot) entity.getBukkitEntity()).setAgeLock(true);
        } else if (pet.equalsIgnoreCase("pig")) {
            entity = new EntityPigPet(world, player);
            ((Pig) entity.getBukkitEntity()).setBaby();
            ((Pig) entity.getBukkitEntity()).setAgeLock(true);
        }else if (pet.equalsIgnoreCase("blacksheep")) {
            entity = new EntitySheepPet(world, player);
            ((Sheep) entity.getBukkitEntity()).setBaby();
            ((Sheep) entity.getBukkitEntity()).setColor(DyeColor.BLACK);
            ((Sheep) entity.getBukkitEntity()).setAgeLock(true);
        }else if (pet.equalsIgnoreCase("bluesheep")) {
            entity = new EntitySheepPet(world, player);
            ((Sheep) entity.getBukkitEntity()).setBaby();
            ((Sheep) entity.getBukkitEntity()).setColor(DyeColor.BLUE);
            ((Sheep) entity.getBukkitEntity()).setAgeLock(true);
        }else if (pet.equalsIgnoreCase("brownsheep")) {
            entity = new EntitySheepPet(world, player);
            ((Sheep) entity.getBukkitEntity()).setBaby();
            ((Sheep) entity.getBukkitEntity()).setColor(DyeColor.BROWN);
            ((Sheep) entity.getBukkitEntity()).setAgeLock(true);
        }else if (pet.equalsIgnoreCase("cyansheep")) {
            entity = new EntitySheepPet(world, player);
            ((Sheep) entity.getBukkitEntity()).setBaby();
            ((Sheep) entity.getBukkitEntity()).setColor(DyeColor.CYAN);
            ((Sheep) entity.getBukkitEntity()).setAgeLock(true);
        }else if (pet.equalsIgnoreCase("graysheep")) {
            entity = new EntitySheepPet(world, player);
            ((Sheep) entity.getBukkitEntity()).setBaby();
            ((Sheep) entity.getBukkitEntity()).setColor(DyeColor.GRAY);
            ((Sheep) entity.getBukkitEntity()).setAgeLock(true);
        }else if (pet.equalsIgnoreCase("greensheep")) {
            entity = new EntitySheepPet(world, player);
            ((Sheep) entity.getBukkitEntity()).setBaby();
            ((Sheep) entity.getBukkitEntity()).setColor(DyeColor.GREEN);
            ((Sheep) entity.getBukkitEntity()).setAgeLock(true);
        }else if (pet.equalsIgnoreCase("lightbluesheep")) {
            entity = new EntitySheepPet(world, player);
            ((Sheep) entity.getBukkitEntity()).setBaby();
            ((Sheep) entity.getBukkitEntity()).setColor(DyeColor.LIGHT_BLUE);
            ((Sheep) entity.getBukkitEntity()).setAgeLock(true);
        }else if (pet.equalsIgnoreCase("limesheep")) {
            entity = new EntitySheepPet(world, player);
            ((Sheep) entity.getBukkitEntity()).setBaby();
            ((Sheep) entity.getBukkitEntity()).setColor(DyeColor.LIME);
            ((Sheep) entity.getBukkitEntity()).setAgeLock(true);
        }else if (pet.equalsIgnoreCase("magentasheep")) {
            entity = new EntitySheepPet(world, player);
            ((Sheep) entity.getBukkitEntity()).setBaby();
            ((Sheep) entity.getBukkitEntity()).setColor(DyeColor.MAGENTA);
            ((Sheep) entity.getBukkitEntity()).setAgeLock(true);
        }else if (pet.equalsIgnoreCase("orangesheep")) {
            entity = new EntitySheepPet(world, player);
            ((Sheep) entity.getBukkitEntity()).setBaby();
            ((Sheep) entity.getBukkitEntity()).setColor(DyeColor.ORANGE);
            ((Sheep) entity.getBukkitEntity()).setAgeLock(true);
        }else if (pet.equalsIgnoreCase("pinksheep")) {
            entity = new EntitySheepPet(world, player);
            ((Sheep) entity.getBukkitEntity()).setBaby();
            ((Sheep) entity.getBukkitEntity()).setColor(DyeColor.PINK);
            ((Sheep) entity.getBukkitEntity()).setAgeLock(true);
        }else if (pet.equalsIgnoreCase("purplesheep")) {
            entity = new EntitySheepPet(world, player);
            ((Sheep) entity.getBukkitEntity()).setBaby();
            ((Sheep) entity.getBukkitEntity()).setColor(DyeColor.PURPLE);
            ((Sheep) entity.getBukkitEntity()).setAgeLock(true);
        }else if (pet.equalsIgnoreCase("redsheep")) {
            entity = new EntitySheepPet(world, player);
            ((Sheep) entity.getBukkitEntity()).setBaby();
            ((Sheep) entity.getBukkitEntity()).setColor(DyeColor.RED);
            ((Sheep) entity.getBukkitEntity()).setAgeLock(true);
        }else if (pet.equalsIgnoreCase("silversheep")) {
            entity = new EntitySheepPet(world, player);
            ((Sheep) entity.getBukkitEntity()).setBaby();
            ((Sheep) entity.getBukkitEntity()).setColor(DyeColor.SILVER);
            ((Sheep) entity.getBukkitEntity()).setAgeLock(true);
        }else if (pet.equalsIgnoreCase("whitesheep") || pet.equalsIgnoreCase("sheep")) {
            entity = new EntitySheepPet(world, player);
            ((Sheep) entity.getBukkitEntity()).setBaby();
            ((Sheep) entity.getBukkitEntity()).setColor(DyeColor.WHITE);
            ((Sheep) entity.getBukkitEntity()).setAgeLock(true);
        }else if (pet.equalsIgnoreCase("yellowsheep")) {
            entity = new EntitySheepPet(world, player);
            ((Sheep) entity.getBukkitEntity()).setBaby();
            ((Sheep) entity.getBukkitEntity()).setColor(DyeColor.YELLOW);
            ((Sheep) entity.getBukkitEntity()).setAgeLock(true);
        } else if (pet.equalsIgnoreCase("silverfish")) {
            entity = new EntitySilverfishPet(world, player);
        } else if (pet.equalsIgnoreCase("villager")) {
            entity = new EntityVillagerPet(world, player);
            ((Villager) entity.getBukkitEntity()).setBaby();
            ((Villager) entity.getBukkitEntity()).setProfession(professions[rand.nextInt(professions.length)]);
            ((Villager) entity.getBukkitEntity()).setAgeLock(true);
        } else if (pet.equalsIgnoreCase("greenvillager")) {
            entity = new EntityVillagerPet(world, player);
            ((Villager) entity.getBukkitEntity()).setBaby();
            ((Villager) entity.getBukkitEntity()).setAgeLock(true);
            ((EntityVillager) entity).setProfession(5);
        } else if (pet.equalsIgnoreCase("wolf")) {
            entity = new EntityWolfPet(world, player);
            ((Wolf) entity.getBukkitEntity()).setBaby();
            ((Wolf) entity.getBukkitEntity()).setAgeLock(true);
        } else if (pet.equalsIgnoreCase("dog")) {
            entity = new EntityWolfPet(world, player);
            ((Wolf) entity.getBukkitEntity()).setBaby();
            ((Wolf) entity.getBukkitEntity()).setTamed(true);
            ((Wolf) entity.getBukkitEntity()).setCollarColor(colors[rand.nextInt(colors.length)]);
            ((Wolf) entity.getBukkitEntity()).setAgeLock(true);
        } else if (pet.equalsIgnoreCase("snowman")) {
            entity = new EntitySnowmanPet(world, player);
        } else if (pet.equalsIgnoreCase("creeper")) {
            entity = new EntityCreeperPet(world, player);
        } else if (pet.equalsIgnoreCase("electrocreeper")) {
            entity = new EntityCreeperPet(world, player);
            ((Creeper) entity.getBukkitEntity()).setPowered(true);
        } else if (pet.equalsIgnoreCase("bat")) {
            entity = new EntityBatPet(world, player);
        } else if (pet.equalsIgnoreCase("squid")) {
            entity = new EntitySquidPet(world, player);
        } else if (pet.equalsIgnoreCase("zombiepigman") || pet.equalsIgnoreCase("babyzombiepigman")) {
            entity = new EntityPigZombiePet(world, player);
        } else if (pet.equalsIgnoreCase("zombie") || pet.equalsIgnoreCase("babyzombie")) {
            entity = new EntityZombiePet(world, player);
        } else if (pet.equalsIgnoreCase("zombievillager") || pet.equalsIgnoreCase("babyzombievillager")) {
            entity = new EntityZombiePet(world, player);
            ((EntityZombie) entity).setVillager(true);
        } else if (pet.equalsIgnoreCase("irongolem")) {
            entity = new EntityIronGolemPet(world, player);
        } else if (pet.equalsIgnoreCase("horse")) {
            entity = new EntityHorsePet(world, player);
            ((Horse) entity.getBukkitEntity()).setBaby();
            ((Horse) entity.getBukkitEntity()).setRemoveWhenFarAway(false);
            ((Horse) entity.getBukkitEntity()).setAgeLock(true);
            ((EntityHorse) entity).setType(0);
            ((EntityHorse) entity).setVariant(allvariants.get(rand.nextInt(allvariants.size())));
        } else if (pet.equalsIgnoreCase("donkey")) {
            entity = new EntityHorsePet(world, player);
            ((Horse) entity.getBukkitEntity()).setBaby();
            ((Horse) entity.getBukkitEntity()).setRemoveWhenFarAway(false);
            ((Horse) entity.getBukkitEntity()).setAgeLock(true);
            ((EntityHorse) entity).setType(1);
        } else if (pet.equalsIgnoreCase("mule")) {
            entity = new EntityHorsePet(world, player);
            ((Horse) entity.getBukkitEntity()).setBaby();
            ((Horse) entity.getBukkitEntity()).setRemoveWhenFarAway(false);
            ((Horse) entity.getBukkitEntity()).setAgeLock(true);
            ((EntityHorse) entity).setType(2);
        } else if (pet.equalsIgnoreCase("undeadhorse")) {
            entity = new EntityHorsePet(world, player);
            ((Horse) entity.getBukkitEntity()).setBaby();
            ((Horse) entity.getBukkitEntity()).setRemoveWhenFarAway(false);
            ((Horse) entity.getBukkitEntity()).setAgeLock(true);
            ((EntityHorse) entity).setType(3);
        } else if (pet.equalsIgnoreCase("skeletonhorse")) {
            entity = new EntityHorsePet(world, player);
            ((Horse) entity.getBukkitEntity()).setBaby();
            ((Horse) entity.getBukkitEntity()).setRemoveWhenFarAway(false);
            ((Horse) entity.getBukkitEntity()).setAgeLock(true);
            ((EntityHorse) entity).setType(4);
        } else if (pet.equalsIgnoreCase("ridablehorse")) {
            entity = new EntityHorsePet(world, player, true);
            ((Horse) entity.getBukkitEntity()).setAdult();
            ((Horse) entity.getBukkitEntity()).setRemoveWhenFarAway(false);
            ((Horse) entity.getBukkitEntity()).setAgeLock(true);
            ((EntityHorse) entity).setTame(true);
            ((EntityHorse) entity).setType(0);
            ((EntityHorse) entity).setVariant(allvariants.get(rand.nextInt(allvariants.size())));
            ((EntityHorsePet) entity).giveShit();
        } else if (pet.equalsIgnoreCase("enderman")) {
            entity = new EntityEndermanPet(world, player, true);
        } else if (pet.equalsIgnoreCase("spider")) {
            entity = new EntitySpiderPet(world, player);
        } else if (pet.equalsIgnoreCase("ghast")) {
            entity = new EntityGhastPet(world, player);
        } else if (pet.equalsIgnoreCase("witch")) {
            entity = new EntityWitchPet(world, player);
        } else if (pet.equalsIgnoreCase("skeleton")) {
            entity = new EntitySkeletonPet(world, player);
        } else if (pet.equalsIgnoreCase("witherskeleton")) {
            entity = new EntitySkeletonPet(world, player, true);
        } else if (pet.equalsIgnoreCase("wither")) {
            entity = new EntityWitherPet(world, player);
        } else if (pet.equalsIgnoreCase("bluewither")) {
            entity = new EntityWitherPet(world, player, true);
        } else if (pet.equalsIgnoreCase("witherskull")) {
            entity = new EntityWitherSkullPet(world, player);
        } else if (pet.equalsIgnoreCase("bluewitherskull")) {
            entity = new EntityWitherSkullPet(world, player, true);
        } else if (pet.equalsIgnoreCase("enderdragon")) {
            entity = new EntityEnderDragonPet(world, player);
        }
        if (entity != null) {
            entity.setPosition(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
            world.addEntity(entity, SpawnReason.CUSTOM);
            entity.getBukkitEntity().teleport(player);
            String timestamp = new SimpleDateFormat("MMdd").format(Calendar.getInstance().getTime());
            if (timestamp.equalsIgnoreCase("1231") || timestamp.equalsIgnoreCase("0101")) {
                if (!spawned.contains(player.getName())) {
                    for (int i = 0; i < 10; i++) {
                        final Player finalPlayer = player;
                        final Entity finalEntity = entity;
                        Pets.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Pets.getInstance(), new Runnable() {
                            @Override
                            public void run() {
                                Firework fw = finalPlayer.getPlayer().getWorld().spawn(finalEntity.getBukkitEntity().getLocation(), Firework.class);
                                fw.setFireworkMeta(getFireworkMeta(fw.getFireworkMeta()));;
                            }
                        }, 7L * i);
                    }
                    spawned.add(player.getName());
                    final String name = player.getName();
                    Pets.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Pets.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            spawned.remove(name);
                        }
                    }, 20L * 15);
                }
            }
            return (org.bukkit.entity.Entity) entity.getBukkitEntity();
        }
        System.err.println("Pet is null!");
        return null;
    }
    
    public static boolean isInt (String str) {
        try {
            Integer.parseInt(str);
                    return true;
        }
        catch(NumberFormatException nfe) {
                return false;
        }
    }

    public static void easterEgg (CraftEntity entity) {
        ((LivingEntity) entity).getEquipment().setItemInHand(new org.bukkit.inventory.ItemStack(org.bukkit.Material.CHEST));
        ((LivingEntity) entity).getEquipment().setItemInHandDropChance(0);
        org.bukkit.inventory.ItemStack hat = new org.bukkit.inventory.ItemStack(org.bukkit.Material.LEATHER_HELMET);
        LeatherArmorMeta hm = (LeatherArmorMeta) hat.getItemMeta();;
        hm.setColor(Color.RED);
        hat.setItemMeta(hm);
        ((LivingEntity) entity).getEquipment().setHelmet(hat);
        ((LivingEntity) entity).getEquipment().setHelmetDropChance(0);
    }

    public static void setInventoryItem (int slot, CraftInventory inventory, CraftItemStack item) {
        try {
            Field field = CraftItemStack.class.getDeclaredField("handle");
            field.setAccessible(true);
            inventory.getInventory().setItem(slot, (net.minecraft.server.v1_7_R3.ItemStack) field.get(item));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static CraftItemStack makeItemStack (org.bukkit.Material material, int amount, short damage, String name, List<String> description) {
        ItemStack internal = new ItemStack(material, amount, damage);
        return makeItemStack(internal, name, description);
    }

    public static CraftItemStack makeItemStack (ItemStack base, String name, List<String> description) {
        base = base.clone();
        ItemMeta meta = base.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(description);
        base.setItemMeta(meta);
        return removeAttributes(CraftItemStack.asCraftCopy(base));
    }

    public static CraftItemStack removeAttributes (CraftItemStack itemStack) {
        if (itemStack == null)
            return null;
        net.minecraft.server.v1_7_R3.ItemStack item = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tag;
        if (!item.hasTag()) {
            tag = new NBTTagCompound();
            item.setTag(tag);
        } else {
            tag = item.getTag();
        }
        NBTTagList am = new NBTTagList();
        tag.set("AttributeModifiers", am);
        item.setTag(tag);
        return CraftItemStack.asCraftMirror(item);
    }

    public static CraftItemStack makeItemStack (Material material, int amount, String name, List<String> description) {
        ItemStack internal = new ItemStack(material, amount);
        return makeItemStack(internal, name, description);
    }

    public static FireworkMeta getFireworkMeta (FireworkMeta input) {
        FireworkEffect effect = FireworkEffect.builder().flicker(rand.nextBoolean()).withColor(getColor(rand.nextInt(17) + 1)).withFade(getColor(rand.nextInt(17) + 1)).with(randomEnum(FireworkEffect.Type.class)).trail(rand.nextBoolean()).build();
        input.addEffect(effect);
        int power = rand.nextInt(2) + 1;
        input.setPower(power);
        return input;
    }

    public static <T extends Enum<?>> T randomEnum (Class<T> clazz) {
        int x = rand.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    public static Color getColor (int c) {
        switch (c) {
            case 1:
            default:
                return Color.AQUA;
            case 2:
                return Color.BLACK;
            case 3:
                return Color.BLUE;
            case 4:
                return Color.FUCHSIA;
            case 5:
                return Color.GRAY;
            case 6:
                return Color.GREEN;
            case 7:
                return Color.LIME;
            case 8:
                return Color.MAROON;
            case 9:
                return Color.NAVY;
            case 10:
                return Color.OLIVE;
            case 11:
                return Color.ORANGE;
            case 12:
                return Color.PURPLE;
            case 13:
                return Color.RED;
            case 14:
                return Color.SILVER;
            case 15:
                return Color.TEAL;
            case 16:
                return Color.YELLOW;
            case 17:
                return Color.WHITE;
        }
    }

    public static CraftItemStack[] makeCraftItemStacks (ItemStack base, String name, List<String> description, ChatColor[] colors, Boolean[] stripColors) {
        base = base.clone();
        CraftItemStack[] out = new CraftItemStack[colors.length];
        for (int i = 0; i < colors.length; i++) {
            CraftItemStack itemStack = CraftItemStack.asCraftCopy(base);
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName(colors[i] + (stripColors[i] ? ChatColor.stripColor(name) : name));
            meta.setLore(description);
            itemStack.setItemMeta(meta);
            out[i] = removeAttributes(itemStack);
        }
        return out;
    }

    public static CraftItemStack[] makeCraftItemStacks (Material material, int amount, String name, List<String> description, ChatColor[] colors, Boolean[] stripColors) {
        ItemStack internal = new ItemStack(material, amount);
        return makeCraftItemStacks(internal, name, description, colors, stripColors);
    }

    static {
        GOAL_FIELD = getField(PathfinderGoalSelector.class, "b");
        try {
            Field field = getField(EntityTypes.class, "e");
            ENTITY_INT_TO_CLASS = (Map<Integer, Class<? extends Entity>>) field.get(null);
            field = getField(EntityTypes.class, "f");
            ENTITY_CLASS_TO_INT = (Map<Class<? extends Entity>, Integer>) field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        registerEntityClass(EntityBlazePet.class);
        registerEntityClass(EntityCaveSpiderPet.class);
        registerEntityClass(EntityChickenPet.class);
        registerEntityClass(EntityCowPet.class);
        registerEntityClass(EntityMagmaCubePet.class);
        registerEntityClass(EntityMushroomCowPet.class);
        registerEntityClass(EntityOcelotPet.class);
        registerEntityClass(EntityPigPet.class);
        registerEntityClass(EntitySheepPet.class);
        registerEntityClass(EntitySilverfishPet.class);
        registerEntityClass(EntitySlimePet.class);
        registerEntityClass(EntityVillagerPet.class);
        registerEntityClass(EntityWolfPet.class);
        registerEntityClass(EntitySnowmanPet.class);
        registerEntityClass(EntityCreeperPet.class);
        registerEntityClass(EntitySquidPet.class);
        registerEntityClass(EntityBatPet.class);
        registerEntityClass(EntityZombiePet.class);
        registerEntityClass(EntityPigZombiePet.class);
        registerEntityClass(EntityIronGolemPet.class);
        registerEntityClass(EntityHorsePet.class);
        registerEntityClass(EntityEndermanPet.class);
        registerEntityClass(EntitySpiderPet.class);
        registerEntityClass(EntityGhastPet.class);
        registerEntityClass(EntityWitchPet.class);
        registerEntityClass(EntitySkeletonPet.class);
        registerEntityClass(EntityWitherPet.class);
        registerEntityClass(EntityWitherSkullPet.class);
        registerEntityClass(EntityEnderDragonPet.class);

    }
}
