package com.fullwall.pets;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import java.util.Random;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityBlaze;
import net.minecraft.server.EntityCaveSpider;
import net.minecraft.server.EntityChicken;
import net.minecraft.server.EntityCow;
import net.minecraft.server.EntityCreeper;
import net.minecraft.server.EntityMagmaCube;
import net.minecraft.server.EntityMushroomCow;
import net.minecraft.server.EntityOcelot;
import net.minecraft.server.EntityPig;
import net.minecraft.server.EntitySheep;
import net.minecraft.server.EntitySilverfish;
import net.minecraft.server.EntitySlime;
import net.minecraft.server.EntitySnowman;
import net.minecraft.server.EntityTypes;
import net.minecraft.server.EntityVillager;
import net.minecraft.server.EntityWolf;
import net.minecraft.server.PathfinderGoalSelector;
import net.minecraft.server.World;

import org.bukkit.DyeColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

@SuppressWarnings("unchecked")
public class Util {

    private static Map<Class<? extends Entity>, Integer> ENTITY_CLASS_TO_INT;
    private static Map<Integer, Class<? extends Entity>> ENTITY_INT_TO_CLASS;
    public static double MAX_DISTANCE = 10 * 10;
    public static int MAX_LEVEL = -1;
    private static Field GOAL_FIELD;
    private static DyeColor[] colors = DyeColor.values();
    private static Profession[] professions = Profession.values();

    public static void clearGoals(PathfinderGoalSelector... goalSelectors) {
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

    private static Field getField(Class<?> clazz, String field) {
        Field f = null;
        try {
            f = clazz.getDeclaredField(field);
            f.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    public static void load(FileConfiguration config) {
        if (!config.isSet("pet.max-distance-squared")) {
            config.set("pet.max-distance-squared", MAX_DISTANCE);
        }
        MAX_DISTANCE = config.getDouble("pet.max-distance-squared");
        if (!config.isSet("pet.max-level")) {
            config.set("pet.max-level", MAX_LEVEL);
        }
        MAX_LEVEL = config.getInt("pet.max-level");
    }

    public static void registerEntityClass(Class<? extends Entity> clazz) {
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

    /*public static LivingEntity spawnPet(Player player) {
        World world = ((CraftWorld) player.getWorld()).getHandle();
        EntitySlime entity = new EntitySlimePet(world, player);
        world.addEntity(entity, SpawnReason.CUSTOM);
        entity.getBukkitEntity().teleport(player);
        return (Slime) entity.getBukkitEntity();
    }*/

    public static LivingEntity spawnPet(Player player, String pet) {
        World world = ((CraftWorld) player.getWorld()).getHandle();
        if (pet.equalsIgnoreCase("slime")) {
            EntitySlime entity = new EntitySlimePet(world, player);
            world.addEntity(entity, SpawnReason.CUSTOM);
            entity.getBukkitEntity().teleport(player);
            ((Slime) entity.getBukkitEntity()).setSize(1);
            return (Slime) entity.getBukkitEntity();
        } else if (pet.equalsIgnoreCase("blaze")) {
            EntityBlaze entity = new EntityBlazePet(world, player);
            world.addEntity(entity, SpawnReason.CUSTOM);
            entity.getBukkitEntity().teleport(player);
            return (Blaze) entity.getBukkitEntity();
        } else if (pet.equalsIgnoreCase("cavespider")) {
            EntityCaveSpider entity = new EntityCaveSpiderPet(world, player);
            world.addEntity(entity, SpawnReason.CUSTOM);
            entity.getBukkitEntity().teleport(player);
            return (CaveSpider) entity.getBukkitEntity();
        } else if (pet.equalsIgnoreCase("chicken")) {
            EntityChicken entity = new EntityChickenPet(world, player);
            world.addEntity(entity, SpawnReason.CUSTOM);
            entity.getBukkitEntity().teleport(player);
            ((Chicken) entity.getBukkitEntity()).setBaby();
            ((Chicken) entity.getBukkitEntity()).setAgeLock(true);
            return (Chicken) entity.getBukkitEntity();
        } else if (pet.equalsIgnoreCase("cow")) {
            EntityCow entity = new EntityCowPet(world, player);
            world.addEntity(entity, SpawnReason.CUSTOM);
            entity.getBukkitEntity().teleport(player);
            ((Cow) entity.getBukkitEntity()).setBaby();
            ((Cow) entity.getBukkitEntity()).setAgeLock(true);
            return (Cow) entity.getBukkitEntity();
        } else if (pet.equalsIgnoreCase("magmacube")) {
            EntityMagmaCube entity = new EntityMagmaCubePet(world, player);
            world.addEntity(entity, SpawnReason.CUSTOM);
            entity.getBukkitEntity().teleport(player);
            ((MagmaCube) entity.getBukkitEntity()).setSize(1);
            return (MagmaCube) entity.getBukkitEntity();
        } else if (pet.equalsIgnoreCase("mooshroom")) {
            EntityMushroomCow entity = new EntityMushroomCowPet(world, player);
            world.addEntity(entity, SpawnReason.CUSTOM);
            entity.getBukkitEntity().teleport(player);
            ((MushroomCow) entity.getBukkitEntity()).setBaby();
            ((MushroomCow) entity.getBukkitEntity()).setAgeLock(true);
            return (MushroomCow) entity.getBukkitEntity();
        } else if (pet.equalsIgnoreCase("ocelot")) {
            EntityOcelot entity = new EntityOcelotPet(world, player);
            world.addEntity(entity, SpawnReason.CUSTOM);
            entity.getBukkitEntity().teleport(player);
            ((Ocelot) entity.getBukkitEntity()).setBaby();
            ((Ocelot) entity.getBukkitEntity()).setAgeLock(true);
            return (Ocelot) entity.getBukkitEntity();
        }else if (pet.equalsIgnoreCase("pig")) {
            EntityPig entity = new EntityPigPet(world, player);
            world.addEntity(entity, SpawnReason.CUSTOM);
            entity.getBukkitEntity().teleport(player);
            ((Pig) entity.getBukkitEntity()).setBaby();
            ((Pig) entity.getBukkitEntity()).setAgeLock(true);
            return (Pig) entity.getBukkitEntity();
        } else if (pet.equalsIgnoreCase("sheep")) {
            EntitySheep entity = new EntitySheepPet(world, player);
            world.addEntity(entity, SpawnReason.CUSTOM);
            entity.getBukkitEntity().teleport(player);
            ((Sheep) entity.getBukkitEntity()).setBaby();
            Random rand = new Random();
            ((Sheep) entity.getBukkitEntity()).setColor(colors[rand.nextInt(colors.length)]);
            ((Sheep) entity.getBukkitEntity()).setAgeLock(true);
            return (Sheep) entity.getBukkitEntity();
        } else if (pet.equalsIgnoreCase("silverfish")) {
            EntitySilverfish entity = new EntitySilverfishPet(world, player);
            world.addEntity(entity, SpawnReason.CUSTOM);
            entity.getBukkitEntity().teleport(player);
            return (Silverfish) entity.getBukkitEntity();
        } else if (pet.equalsIgnoreCase("villager")) {
            EntityVillager entity = new EntityVillagerPet(world, player);
            world.addEntity(entity, SpawnReason.CUSTOM);
            entity.getBukkitEntity().teleport(player);
            ((Villager) entity.getBukkitEntity()).setBaby();
            Random rand = new Random();
            ((Villager) entity.getBukkitEntity()).setProfession(professions[rand.nextInt(professions.length)]);
            //entity.setProfession(5);
            ((Villager) entity.getBukkitEntity()).setAgeLock(true);
            return (Villager) entity.getBukkitEntity();
        }else if (pet.equalsIgnoreCase("greenvillager")) {
            EntityVillager entity = new EntityVillagerPet(world, player);
            world.addEntity(entity, SpawnReason.CUSTOM);
            entity.getBukkitEntity().teleport(player);
            ((Villager) entity.getBukkitEntity()).setBaby();
            //Random rand = new Random();
            //((Villager) entity.getBukkitEntity()).setProfession(professions[rand.nextInt(professions.length)]);
            ((Villager) entity.getBukkitEntity()).setAgeLock(true);
            entity.setProfession(5);
            return (Villager) entity.getBukkitEntity();
        } else if (pet.equalsIgnoreCase("wolf")) {
            EntityWolf entity = new EntityWolfPet(world, player);
            world.addEntity(entity, SpawnReason.CUSTOM);
            entity.getBukkitEntity().teleport(player);
            ((Wolf) entity.getBukkitEntity()).setBaby();
            ((Wolf) entity.getBukkitEntity()).setAgeLock(true);
            //((Wolf) entity.getBukkitEntity()).setTamed(true);
            return (Wolf) entity.getBukkitEntity();
        } else if (pet.equalsIgnoreCase("snowman")) {
            EntitySnowman entity = new EntitySnowmanPet(world, player);
            world.addEntity(entity, SpawnReason.CUSTOM);
            entity.getBukkitEntity().teleport(player);
            return (Snowman) entity.getBukkitEntity();
        } else if (pet.equalsIgnoreCase("creeper")) {
            EntityCreeper entity = new EntityCreeperPet(world, player);
            world.addEntity(entity, SpawnReason.CUSTOM);
            entity.getBukkitEntity().teleport(player);
            return (Creeper) entity.getBukkitEntity();
        }
        return null;
    }

    static {
        GOAL_FIELD = getField(PathfinderGoalSelector.class, "a");
        try {
            Field field = getField(EntityTypes.class, "d");
            ENTITY_INT_TO_CLASS = (Map<Integer, Class<? extends Entity>>) field.get(null);
            field = getField(EntityTypes.class, "e");
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
    }
}