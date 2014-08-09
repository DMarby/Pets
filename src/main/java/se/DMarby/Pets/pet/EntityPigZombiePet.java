package se.DMarby.Pets.pet;

import net.minecraft.server.v1_7_R4.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPigZombie;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import se.DMarby.Pets.PetEntity;
import se.DMarby.Pets.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EntityPigZombiePet extends EntityPigZombie { // new AI
    private final Player owner;

    public EntityPigZombiePet(World world, Player owner) {
        super(world);
        this.owner = owner;
        if (owner != null) {
            Util.clearGoals(this.goalSelector, this.targetSelector);
            setBaby(true);
            String timestamp = new SimpleDateFormat("MMdd").format(Calendar.getInstance().getTime());
            if (timestamp.equalsIgnoreCase("1225") || timestamp.equalsIgnoreCase("1224")) {
                Util.easterEgg(this.getBukkitEntity());
            }
        }
    }

    public EntityPigZombiePet(World world) {
        this(world, null);
    }

    private int distToOwner() {
        EntityHuman handle = ((CraftPlayer) owner).getHandle();
        return (int) (Math.pow(locX - handle.locX, 2) + Math.pow(locY - handle.locY, 2) + Math.pow(locZ
                - handle.locZ, 2));
    }

    @Override
    protected Entity findTarget() {
        if (owner == null) {
            return super.findTarget();
        }
        return ((CraftPlayer) owner).getHandle();
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (owner == null) {
            return super.damageEntity(damagesource, f);
        }
        return false;
    }

    public void h() {
        super.h();
        if (owner == null) {
            return;
        }
        this.W = 10F;
        if (distToOwner() > 3) {
            this.getNavigation().a(owner.getLocation().getX(), owner.getLocation().getY(), owner.getLocation().getZ(), 0.3F);
            this.getNavigation().a(false);
        } else {
            this.motX = 0;
            this.bf = 0;
            this.motZ = 0;
        }
        if (distToOwner() > Util.MAX_DISTANCE)
            this.getBukkitEntity().teleport(owner);

    }

    @Override
    public CraftEntity getBukkitEntity() {
        if (owner != null && bukkitEntity == null)
            bukkitEntity = new BukkitPigZombiePet(this);
        return super.getBukkitEntity();
    }

    public static class BukkitPigZombiePet extends CraftPigZombie implements PetEntity {
        private final Player owner;

        public BukkitPigZombiePet(EntityPigZombiePet entity) {
            super((CraftServer) Bukkit.getServer(), entity);
            this.owner = entity.owner;
        }

        @Override
        public Zombie getBukkitEntity() {
            return this;
        }

        @Override
        public Player getOwner() {
            return owner;
        }

        @Override
        public void upgrade() {
        }

        @Override
        public void setLevel(int level) {
            // setSize(level);
        }
    }
}
