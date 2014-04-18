package se.DMarby.Pets.pet;

import net.minecraft.server.v1_7_R3.EntityIronGolem;
import net.minecraft.server.v1_7_R3.EntityHuman;
import net.minecraft.server.v1_7_R3.World;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R3.CraftServer;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftIronGolem;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import se.DMarby.Pets.PetEntity;
import se.DMarby.Pets.Util;

public class EntityIronGolemPet extends EntityIronGolem { // new AI

    private final Player owner;

    public EntityIronGolemPet(World world, Player owner) {
        super(world);
        this.owner = owner;
        if (owner != null) {
            Util.clearGoals(this.goalSelector, this.targetSelector);
        }
    }

    public EntityIronGolemPet(World world) {
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
            this.getNavigation().a(owner.getLocation().getX(), owner.getLocation().getY(), owner.getLocation().getZ(), 1.5F);
            this.getNavigation().a(false);
        }
        //this.getNavigation().a(((CraftPlayer) owner).getHandle(), super.bm() * 0.15F);
        if (distToOwner() > Util.MAX_DISTANCE) {
            this.getBukkitEntity().teleport(owner);
        }
    }

    @Override
    public CraftEntity getBukkitEntity() {
        if (owner != null && bukkitEntity == null) {
            bukkitEntity = new BukkitIronGolemPet(this);
        }
        return super.getBukkitEntity();
    }

    public static class BukkitIronGolemPet extends CraftIronGolem implements PetEntity {

        private final Player owner;

        public BukkitIronGolemPet(EntityIronGolemPet entity) {
            super((CraftServer) Bukkit.getServer(), entity);
            this.owner = entity.owner;
        }

        @Override
        public IronGolem getBukkitEntity() {
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
