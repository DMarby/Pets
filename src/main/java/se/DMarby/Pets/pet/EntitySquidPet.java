package se.DMarby.Pets.pet;

import net.minecraft.server.v1_8_R1.BlockPosition;
import net.minecraft.server.v1_8_R1.DamageSource;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.EntitySquid;
import net.minecraft.server.v1_8_R1.MathHelper;
import net.minecraft.server.v1_8_R1.World;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R1.CraftServer;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftSquid;
import org.bukkit.entity.Player;
import org.bukkit.entity.Squid;

import se.DMarby.Pets.PetEntity;
import se.DMarby.Pets.Util;

public class EntitySquidPet extends EntitySquid { // old AI
    private final Player owner;

    public EntitySquidPet(World world, Player owner) {
        super(world);
        this.owner = owner;
    }

    public EntitySquidPet(World world) {
        this(world, null);
    }

    private int distToOwner() {
        EntityHuman handle = ((CraftPlayer) owner).getHandle();
        return (int) (Math.pow(locX - handle.locX, 2) + Math.pow(locY - handle.locY, 2) + Math.pow(locZ
                - handle.locZ, 2));
    }

    @Override
    protected void doTick() {
        if (owner == null) {
            super.doTick();
            return;
        }
       /* if (distToOwner() > 3) {
            this.getNavigation().a(owner.getLocation().getX(), owner.getLocation().getY(), owner.getLocation().getZ(), 0.55F);
            ((Navigation)this.getNavigation()).d(false);
            getEntitySenses().a();
            getNavigation().k();
            getControllerMove().c(); // old API
            getControllerLook().a(); // old API
            getControllerJump().b(); // etc
        }  */
        BlockPosition thing = new BlockPosition((int) this.owner.getLocation().getX(), (int) this.owner.getEyeLocation().getY(), (int) owner.getLocation().getZ());

        double d1 = thing.getX() + 0.5D - this.locX;
        double d2 = thing.getY() + 0.1D - this.locY;
        double d3 = thing.getZ() + 0.5D - this.locZ;

        if (distToOwner() > 2) {
            this.motY += (Math.signum(d2) * 1.5D - this.motY) * 0.1000000014901161D;
            this.motX += (Math.signum(d1) * 3D - this.motX) * 0.1000000014901161D;
            this.motZ += (Math.signum(d3) * 3D - this.motZ) * 0.1000000014901161D;

        } else {
            this.motX = 0;
            this.motZ = 0;
        }

        float f1 = (float) (Math.atan2(this.motZ, this.motX) * 180.0D / 3.141592741012573D) - 90.0F;
        float f2 = MathHelper.g(f1 - this.yaw);
        this.bf = 0.5F;
        this.yaw += f2;
        if (distToOwner() > Util.MAX_DISTANCE)
            this.getBukkitEntity().teleport(owner);
    }

    @Override
    public boolean isInvulnerable(DamageSource d) {
        if (owner == null) {
            return super.isInvulnerable(d);
        }
        return true;
    }

    @Override
    public boolean U() {
        if (owner == null) {
            return super.U();
        }
        return false;
    }

    @Override
    public CraftEntity getBukkitEntity() {
        if (owner != null && bukkitEntity == null)
            bukkitEntity = new BukkitSquidPet(this);
        return super.getBukkitEntity();
    }

    public static class BukkitSquidPet extends CraftSquid implements PetEntity {
        private final Player owner;

        public BukkitSquidPet(EntitySquidPet entity) {
            super((CraftServer) Bukkit.getServer(), entity);
            this.owner = entity.owner;
        }

        @Override
        public Squid getBukkitEntity() {
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
