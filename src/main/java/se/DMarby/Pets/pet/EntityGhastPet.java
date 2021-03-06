package se.DMarby.Pets.pet;

import net.minecraft.server.v1_8_R1.BlockPosition;
import net.minecraft.server.v1_8_R1.DamageSource;
import net.minecraft.server.v1_8_R1.EntityGhast;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.MathHelper;
import net.minecraft.server.v1_8_R1.World;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R1.CraftServer;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftGhast;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Player;

import se.DMarby.Pets.PetEntity;
import se.DMarby.Pets.Util;

public class EntityGhastPet extends EntityGhast { // old AI
    private final Player owner;

    public EntityGhastPet(World world, Player owner) {
        super(world);
        this.owner = owner;
        if (owner != null) {
            this.f(false);
        }
    }

    public EntityGhastPet(World world) {
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
       /* this.getNavigation().a(owner.getLocation().getX(), owner.getEyeLocation().getY(), owner.getLocation().getZ(), 0.55F);
        ((Navigation)this.getNavigation()).d(false);    */

        BlockPosition thing = new BlockPosition((int) this.owner.getLocation().getX(), (int) this.owner.getEyeLocation().getY() + 5, (int) owner.getLocation().getZ());

        double d1 = thing.getX() + 0.5D - this.locX;
        double d2 = thing.getY() + 0.1D - this.locY;
        double d3 = thing.getZ() + 0.5D - this.locZ;

        if (distToOwner() > 80) {
            this.motY += (Math.signum(d2) * 0.5D - this.motY) * 0.1000000014901161D;
            this.motX += (Math.signum(d1) * 0.3D - this.motX) * 0.1000000014901161D;
            this.motZ += (Math.signum(d3) * 0.3D - this.motZ) * 0.1000000014901161D;
            float f1 = (float) (Math.atan2(this.motZ, this.motX) * 180.0D / 3.141592741012573D) - 90.0F;
            float f2 = MathHelper.g(f1 - this.yaw);
            this.bf = 0.5F;
            this.yaw += f2;
        } else {
            this.motX = 0;
            this.motZ = 0;
        }

      /*  getEntitySenses().a();
        getNavigation().k();
        getControllerMove().c(); // old API
        getControllerLook().a(); // old API
        getControllerJump().b(); // etc     */

        if (distToOwner() > (Util.MAX_DISTANCE * 2))
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
    public CraftEntity getBukkitEntity() {
        if (owner != null && bukkitEntity == null)
            bukkitEntity = new BukkitGhastPet(this);
        return super.getBukkitEntity();
    }

    public static class BukkitGhastPet extends CraftGhast implements PetEntity {
        private final Player owner;

        public BukkitGhastPet(EntityGhastPet entity) {
            super((CraftServer) Bukkit.getServer(), entity);
            this.owner = entity.owner;
        }

        @Override
        public Ghast getBukkitEntity() {
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
