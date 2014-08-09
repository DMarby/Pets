package se.DMarby.Pets;

import com.google.common.collect.Maps;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R4.util.CraftMagicNumbers;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import org.bukkit.projectiles.ProjectileSource;

import se.DMarby.Pets.pet.EntityEndermanPet;
import se.DMarby.Pets.pet.EntityHorsePet;
import se.DMarby.Pets.pet.EntityIronGolemPet;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class PetController implements Listener {

    private final Map<String, PlayerData> playerData = Maps.newHashMap();
    private final Plugin plugin;

    // private long timePeriod = TimeUnit.MILLISECONDS.convert(1,
    // TimeUnit.DAYS);
    public PetController(Plugin plugin) {
        this.plugin = plugin;
        // if (!plugin.getConfig().isSet("player.level-up-period"))
        // plugin.getConfig().set("player.level-up-period", timePeriod);

        // timePeriod = plugin.getConfig().getLong("player.level-up-period");
    }

    /*public long getAliveTime(Player player) {
    PlayerData data = playerData.get(player.getName());
    if (data == null)
    return 0;
    return data.getAliveTime();
    }*/

    /*private long getRemainder(long time) {
    return (timePeriod - (time % timePeriod)) * 20;
    }*/
    private PetEntity getPet(Entity entity) {
        return entity instanceof PetEntity ? (PetEntity) entity : null;
    }

    public boolean isActive(Player player) {
        return !playerData.containsKey(player.getName()) ? false : playerData.get(player.getName()).petActive;
    }

    public String getType(Player player) {
        return !playerData.containsKey(player.getName()) ? null : playerData.get(player.getName()).type;
    }

    public String getName(Player player) {
        return !playerData.containsKey(player.getName()) ? null : playerData.get(player.getName()).name;
    }

    public String getItem(Player player) {
        return !playerData.containsKey(player.getName()) ? null : playerData.get(player.getName()).item;
    }

    //public void loadPlayer(Player player, long aliveTime, boolean enabled, String type) {
    public void loadPlayer(Player player, boolean enabled, String type, String name, String item) {
        //PlayerData data = new PlayerData(player, aliveTime, enabled, type);
        PlayerData data = new PlayerData(player, enabled, type, name, item);
        playerData.put(player.getName(), data);
        /* scheduleTask(player, aliveTime);
        if (data.type != null) {
        data.spawn(data.type);
        } */
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        PetEntity pet = getPet(event.getRightClicked());
        if (pet == null || !(pet instanceof EntityHorsePet.BukkitHorsePet)) {
            return;
        }

        if (!event.getPlayer().equals(pet.getOwner())) {
            event.setCancelled(true);
        }

    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityInteract(EntityInteractEvent event) {
        PetEntity pet = getPet(event.getEntity());
        if (pet == null) {
            return;
        }
        if (event.getBlock().getType() != Material.WHEAT || event.getBlock().getType() != Material.SOIL) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        PetEntity pet = getPet(event.getEntity());
        if (pet == null) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityFormBlock(EntityBlockFormEvent event) {
        PetEntity pet = getPet(event.getEntity());
        if (pet == null) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        PetEntity pet = getPet(event.getEntity());
        if (pet == null) {
            if (event instanceof EntityDamageByEntityEvent) {
                PetEntity damager = getPet(((EntityDamageByEntityEvent) event).getDamager());
                if (damager != null) {
                    event.setCancelled(true);
                }
            }
            return;
        }
        event.setCancelled(true);
        if (event.getCause() == null) {
            return;
        }
        switch (event.getCause()) {
            case DROWNING:
            case LAVA:
                event.getEntity().teleport(pet.getOwner());
                break;
            case FIRE:
            case FIRE_TICK:
                event.getEntity().setFireTicks(0);
            default:
                break;
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (isActive(player)) {
            PlayerData data = playerData.get(player.getName());
            if (data == null) {
                return;
            }
            Entity pet = data.pet;
            if (pet == null) {
                return;
            }
            pet.teleport(player.getLocation());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        removePet(player, false);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (isActive(player)) {
            PlayerData data = playerData.get(player.getName());
            if (data == null) {
                return;
            }
            Entity pet = data.pet;
            if (pet != null) {
                pet.teleport(player.getLocation());
                return;
            }

            data.spawn(data.type, data.name, data.item);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        PetEntity pet = getPet(event.getEntity());
        if (pet == null) {
            return;
        }
        ((CraftEntity) event.getEntity()).getHandle().getDataWatcher().watch(16, Byte.valueOf((byte) -1));
        ((CraftEntity) event.getEntity()).getHandle().getDataWatcher().watch(18, Byte.valueOf((byte) 0));
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (Util.removeInFight) {
            if ((event.getEntity() instanceof Player) && (event.getDamager() instanceof Player || event.getDamager() instanceof Projectile)) {
                if (event.getDamager() instanceof Player) {
                    Player a = (Player) event.getEntity();
                    Player b = (Player) event.getDamager();
                    removePet(a, false);
                    removePet(b, false);
                } else {
                    ProjectileSource shooter = ((Projectile) event.getDamager()).getShooter();
                    if (shooter instanceof Player) {
                        Player a = (Player) event.getEntity();
                        Player b = (Player) shooter;
                        removePet(a, false);
                        removePet(b, false);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        if (Util.removeInBoat) {
            if (event.getVehicle() instanceof Boat && event.getEntered() instanceof Player) {
                removePet((Player) event.getEntered(), false);
            }
        }
    }

    @EventHandler
    public void onVehicleExit(VehicleExitEvent event) {
        if (Util.removeInBoat) {
            if (event.getVehicle() instanceof Boat && event.getExited() instanceof Player) {
                Player player = (Player) event.getExited();
                if (isActive(player)) {
                    PlayerData data = playerData.get(player.getName());
                    if (data == null) {
                        return;
                    }
                    Entity pet = data.pet;
                    if (pet != null) {
                        pet.teleport(player.getLocation());
                        return;
                    }
    
                    data.spawn(data.type, data.name, data.item);
                }
            }
        }
    }

    @EventHandler
    public void onVehicleDestroy(VehicleDestroyEvent event) {
        if (Util.removeInBoat) {
            if (event.getVehicle() instanceof Boat && event.getVehicle().getPassenger() instanceof Player) {
                Player player = (Player) event.getVehicle().getPassenger();
                if (isActive(player)) {
                    PlayerData data = playerData.get(player.getName());
                    if (data == null) {
                        return;
                    }
                    Entity pet = data.pet;
                    if (pet != null) {
                        pet.teleport(player.getLocation());
                        return;
                    }
    
                    data.spawn(data.type, data.name, data.item);
                }
            }
        }
    }

    public void removePet(Player player, boolean both) {
        PlayerData data = playerData.get(player.getName());
        if (data == null) {
            return;
        }
        // data.removePetAndTask(both);
        data.removeThePet();
        if (both) {
            playerData.remove(player.getName());
        }
    }

    /*private void scheduleTask(Player player, long aliveTime) {
    long ticks = getRemainder(aliveTime);
    new CreationTask(player).schedule(ticks);
    }*/
    public void togglePet(Player player, String type) {
        PlayerData data = playerData.get(player.getName());
        if (data == null) {
            return;
        }
        data.toggle(type);
    }

    public void togglePet(Player player) {
        PlayerData data = playerData.get(player.getName());
        if (data == null) {
            return;
        }
        data.toggle();
    }

    public void setName(Player player, String name) {
        PlayerData data = playerData.get(player.getName());
        if (data == null) {
            return;
        }
        data.setName(name);
    }

    public void setItem(Player player, String item) {
        PlayerData data = playerData.get(player.getName());
        if (data == null) {
            return;
        }
        data.setItem(item);
    }

    /*private class CreationTask implements Runnable {
    private final Player player;
    private final long startTime = System.currentTimeMillis();
    private int taskId;

    CreationTask(Player player) {
    this.player = player;
    }

    void cancel() {
    Bukkit.getScheduler().cancelTask(taskId);
    }

    long getRunDuration() {
    return System.currentTimeMillis() - startTime;
    }

    @Override
    public void run() {
    PlayerData data = playerData.get(player.getName());
    data.aliveTime = data.aliveTime + getRunDuration();
    data.upgradeSlime();
    scheduleTask(player, 0);
    }

    void schedule(long ticks) {
    taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this, ticks);
    playerData.get(player.getName()).task = this;
    }
    }*/
    private class PlayerData {

        //long aliveTime;
        Entity pet;
        boolean petActive;
        Player player;
        String type;
        String name;
        String item;

        // CreationTask task;
        //public PlayerData(Player player, long aliveTime, boolean enabled, String type) {
        public PlayerData(Player player, boolean enabled, String type, String name, String item) {
            //this.aliveTime = aliveTime;
            this.player = player;
            this.type = type;
            this.name = name;
            this.item = item;
            petActive = enabled;
            if (petActive && type != null) {
                // long days = aliveTime / timePeriod;
                // if (days > 0)
                // spawnAtLevel((int) days);
                spawn(type, name, item);
            }
        }

        private void spawn(String type, String name, String item) {
            Entity entity = Util.spawnPet(player, type);
            pet = entity;
            if (pet instanceof LivingEntity) {
                setName(name);
                String timestamp = new SimpleDateFormat("MMdd").format(Calendar.getInstance().getTime());
                if (!timestamp.equalsIgnoreCase("1225") && !timestamp.equalsIgnoreCase("1224")) {
                    setItem(item);
                }
            }
        }

        /*private void spawnAtLevel(int level) {
        //  if (level <= 0)
        //      return;
        Entity entity = Util.spawnPet(player);
        getPet(entity).setLevel(1);
        pet = entity;
        }*/

        /*public long getAliveTime() {
        return aliveTime + (task == null ? 0 : task.getRunDuration());
        }

        private int getLevel() {
        return (int) (getAliveTime() / timePeriod);
        }*/
        void removeThePet() {
            if (pet != null) {
                pet.remove();
                pet = null;
            }
        }

        /*void removePetAndTask(boolean both) {
        if (pet != null) {
        pet.remove();
        pet = null;
        }
         *
        if (both && task != null) {
        task.cancel();
        task = null;
        }
        }*/
        public void toggle(String type) {
            //petActive = !petActive;
            petActive = true;
            this.type = type;
            if (pet != null) {
                removePet(player, false);
            }
            spawn(type, name, item);
            player.sendMessage(ChatColor.GREEN + type.substring(0, 1).toUpperCase() + type.substring(1)
                    + " spawned.");
        }

        public void toggle() {
            petActive = !petActive;
            if (pet == null) {
                if (this.type == null) {
                    return;
                }
                spawn(this.type, name, item);
                player.sendMessage(ChatColor.GREEN + type.substring(0, 1).toUpperCase() + type.substring(1)
                        + " spawned.");
            } else {
                removePet(player, false);
                player.sendMessage(ChatColor.GREEN + "Pet toggled off.");
            }
        }

        public void setName(String name) {
            if (pet == null) {
                return;
            }

            if (name != null) {
                this.name = name;
                if (pet instanceof LivingEntity) {
                    ((LivingEntity) pet).setCustomName(name);
                    ((LivingEntity) pet).setCustomNameVisible(true);
                }
            } else {
                this.name = null;
                if (pet instanceof LivingEntity) {
                    ((LivingEntity) pet).setCustomName(null);
                    ((LivingEntity) pet).setCustomNameVisible(false);
                }
            }

        }

        public void setItem(String item) {
            if (pet == null) {
                return;
            }

            if (item != null) {
                Material mat;
                if (Util.isInt(item)) {
                    mat = Material.getMaterial(Integer.parseInt(item));
                } else {
                    mat = Material.getMaterial(item.toUpperCase());
                }

                if (mat != null) {
                    this.item = item;

                    net.minecraft.server.v1_7_R4.Entity craftPet = ((CraftEntity) pet).getHandle();
                    if (craftPet instanceof EntityIronGolemPet) {
                        if (mat.equals(Material.RED_ROSE)) {
                            ((EntityIronGolemPet) craftPet).setCarryFlower(true);
                        } else if (((EntityIronGolemPet) craftPet).getCarryFlower()) {
                            removeThePet();
                            spawn(this.type, this.name, this.item);
                        }
                        return;
                    }

                    if (craftPet instanceof EntityEndermanPet) {
                        MaterialData materialData = new MaterialData(mat);
                        ((EntityEndermanPet) craftPet).setCarried(CraftMagicNumbers.getBlock(materialData.getItemTypeId()));
                        ((EntityEndermanPet) craftPet).setCarriedData(materialData.getData());
                        return;
                    }

                    if (pet instanceof LivingEntity) {
                        ((LivingEntity) pet).getEquipment().setItemInHand(new ItemStack(mat));
                        ((LivingEntity) pet).getEquipment().setItemInHandDropChance(0);
                    }
                    return;
                }
            }
            this.item = null;
            if (pet instanceof LivingEntity) {
                ((LivingEntity) pet).getEquipment().clear();
            }
        }
        /*public void upgradeSlime() {
        if (!petActive)
        return;
        if (pet == null)
        spawnAtLevel(Math.max(getLevel(), 1));
        getPet(pet).upgrade();
        }*/
    }
}
