package se.DMarby.Pets.pet;

import net.minecraft.server.v1_7_R2.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R2.CraftServer;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftSnowman;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowman;
import se.DMarby.Pets.PetEntity;
import se.DMarby.Pets.Util;

public class EntitySnowmanPet extends EntitySnowman { // new AI

    private final Player owner;

    public EntitySnowmanPet(World world, Player owner) {
        super(world);
        this.owner = owner;
        if (owner != null) {
            Util.clearGoals(this.goalSelector, this.targetSelector);
        }
    }

    public EntitySnowmanPet(World world) {
        this(world, null);
    }

    private int distToOwner() {
        EntityHuman handle = ((CraftPlayer) owner).getHandle();
        return (int) (Math.pow(locX - handle.locX, 2) + Math.pow(locY - handle.locY, 2) + Math.pow(locZ
                - handle.locZ, 2));
    }

    @Override
    public void bm() {
        super.bm();
        if (owner == null) {
            return;
        }
        this.W = 10F;
        if (distToOwner() > 3) {
            this.getNavigation().a(owner.getLocation().getX(), owner.getLocation().getY(), owner.getLocation().getZ(), 1.7D);
            this.getNavigation().a(false);
        }
        //this.getNavigation().a(((CraftPlayer) owner).getHandle(), super.bm() * 0.15F);
        if (distToOwner() > Util.MAX_DISTANCE) {
            this.getBukkitEntity().teleport(owner);
        }
    }

    @Override
    public boolean isInvulnerable () {
        if (owner == null) {
            return super.isInvulnerable();
        }
        return true;
    }

    @Override
    public boolean L () {
        if (owner == null) {
            return super.L();
        }
        return false;
    }


    @Override
    public CraftEntity getBukkitEntity() {
        if (owner != null && bukkitEntity == null) {
            bukkitEntity = new BukkitSnowmanPet(this);
        }
        return super.getBukkitEntity();
    }

    public static class BukkitSnowmanPet extends CraftSnowman implements PetEntity {

        private final Player owner;

        public BukkitSnowmanPet(EntitySnowmanPet entity) {
            super((CraftServer) Bukkit.getServer(), entity);
            this.owner = entity.owner;
        }

        @Override
        public Snowman getBukkitEntity() {
            return this;
        }

        @Override
        public Player getOwner() {
            return owner;
        }

        @Override
        public void upgrade() {
            // upgrade logic here
            /*
            int size = getSize() + 1;
            if (Util.MAX_LEVEL != -1)
            size = Math.min(Util.MAX_LEVEL, size);
            setSize(size);
            Location petLoc = getLocation();
            getWorld().playSound(petLoc, Sound.LEVEL_UP, size, 1);
            for (int i = 0; i < size; i++)
            getWorld().playEffect(petLoc, Effect.SMOKE, (size + i) / 8);
             */
        }

        @Override
        public void setLevel(int level) {
            // setSize(level);
        }
    }
}
