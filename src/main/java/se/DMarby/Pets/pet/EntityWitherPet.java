package se.DMarby.Pets.pet;

import net.minecraft.server.v1_7_R3.*;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R3.CraftServer;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftWither;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import se.DMarby.Pets.PetEntity;
import se.DMarby.Pets.Util;

public class EntityWitherPet extends EntityWither { // new AI
    private final Player owner;
    private Boolean blue;

    public EntityWitherPet(World world, Player owner, Boolean blue) {
        super(world);
        this.owner = owner;
        this.blue = blue;
        if (owner != null) {
            if (blue) {
                this.setHealth(1F);
            }
            Util.clearGoals(this.goalSelector, this.targetSelector);
        }
    }

    public EntityWitherPet(World world, Player owner) {
        super(world);
        this.owner = owner;
        this.blue = false;
        if (owner != null) {
            Util.clearGoals(this.goalSelector, this.targetSelector);
        }
    }

    public EntityWitherPet(World world) {
        this(world, null);
    }

    private int distToOwner() {
        EntityHuman handle = ((CraftPlayer) owner).getHandle();
        return (int) (Math.pow(locX - handle.locX, 2) + Math.pow(locY - handle.locY, 2) + Math.pow(locZ
                - handle.locZ, 2));
    }

    @Override
    protected void bm() {
        if (owner == null) {
            super.bm();
            return;
        }

        ChunkCoordinates thing = new ChunkCoordinates((int)this.owner.getLocation().getX(), (int)this.owner.getEyeLocation().getY(), (int)owner.getLocation().getZ());

        double d1 = thing.x + 0.5D - this.locX;
        double d2 = thing.y + 0.1D - this.locY;
        double d3 = thing.z + 0.5D - this.locZ;

        if (distToOwner() > 10) {
            this.motY += (Math.signum(d2) * 1D - this.motY) * 0.1000000014901161D;
            this.motX += (Math.signum(d1) * 0.2D - this.motX) * 0.1000000014901161D;
            this.motZ += (Math.signum(d3) * 0.2D - this.motZ) * 0.1000000014901161D;
        } else {
            this.motX = 0;
            this.motZ = 0;
        }

        float f1 = (float)(Math.atan2(this.motZ, this.motX) * 180.0D / 3.141592741012573D) - 90.0F;
        float f2 = MathHelper.g(f1 - this.yaw);
        this.bf = 0.5F;
        this.yaw += f2;

        if (distToOwner() > Util.MAX_DISTANCE)
            this.getBukkitEntity().teleport(owner);
    }

    @Override
    public boolean isInvulnerable () {
        if (owner == null) {
            return super.isInvulnerable();
        }
        return true;
    }

    @Override
    public boolean cb () {
        if (owner == null || blue) {
            return super.cb();
        }
        return false;
    }


    @Override
    public CraftEntity getBukkitEntity() {
        if (owner != null && bukkitEntity == null)
            bukkitEntity = new BukkitWitherPet(this);
        return super.getBukkitEntity();
    }

    public static class BukkitWitherPet extends CraftWither implements PetEntity {
        private final Player owner;

        public BukkitWitherPet(EntityWitherPet entity) {
            super((CraftServer) Bukkit.getServer(), entity);
            this.owner = entity.owner;
        }

        @Override
        public Wither getBukkitEntity() {
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
