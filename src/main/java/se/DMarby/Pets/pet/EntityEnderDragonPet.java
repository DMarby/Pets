package se.DMarby.Pets.pet;

import net.minecraft.server.v1_8_R1.DamageSource;
import net.minecraft.server.v1_8_R1.EntityEnderDragon;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.Navigation;
import net.minecraft.server.v1_8_R1.World;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R1.CraftServer;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEnderDragon;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;

import se.DMarby.Pets.PetEntity;
import se.DMarby.Pets.Util;

public class EntityEnderDragonPet extends EntityEnderDragon { // new AI
    private final Player owner;

    public EntityEnderDragonPet(World world, Player owner) {
        super(world);
        this.owner = owner;
        if (owner != null) {
            Util.clearGoals(this.goalSelector, this.targetSelector);
        }
    }

    public EntityEnderDragonPet(World world) {
        this(world, null);
    }

    private int distToOwner() {
        EntityHuman handle = ((CraftPlayer) owner).getHandle();
        return (int) (Math.pow(locX - handle.locX, 2) + Math.pow(locY - handle.locY, 2) + Math.pow(locZ
                - handle.locZ, 2));
    }

    @Override
    public void h() {
        if (owner == null) {
            super.h();
            return;
        }


        this.getNavigation().a(owner.getLocation().getX(), owner.getLocation().getY(), owner.getLocation().getZ(), 1.1F);
        ((Navigation)this.getNavigation()).d(false);
        getEntitySenses().a();
        getNavigation().k();
        getControllerMove().c();
        getControllerLook().a();
        getControllerJump().b();
      /*  BlockPosition thing = new BlockPosition((int)this.owner.getLocation().getX(), (int)this.owner.getEyeLocation().getY() + 5, (int)owner.getLocation().getZ());

        double d1 = thing.x + 0.5D - this.locX;
        double d2 = thing.y + 0.1D - this.locY;
        double d3 = thing.z + 0.5D - this.locZ;

        if (distToOwner() > 120) {
            this.motY += (Math.signum(d2) * 3D - this.motY) * 0.1000000014901161D;
            this.motX += (Math.signum(d1) * 4D - this.motX) * 0.1000000014901161D;
            this.motZ += (Math.signum(d3) * 4D - this.motZ) * 0.1000000014901161D;
            float f1 = (float)(Math.atan2(this.motZ, this.motX) * 180.0D / 3.141592741012573D) - 90.0F;
            float f2 = MathHelper.g(f1 - this.yaw);
            this.bf = 0.5F;
            this.yaw += f2;
           this.move(this.motX, this.motY, this.motZ);
          //  this.setPositionRotation(this.motX, this.motY, this.motZ, this.yaw, this.pitch);
        }         */

        double max = Util.MAX_DISTANCE * 5;
        if (distToOwner() > max) {
            this.getBukkitEntity().teleport(owner);
        }
    }

    @Override
    public void m() {
        if (owner == null) {
            super.m();
        }
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
            bukkitEntity = new BukkitEnderDragonPet(this);
        return super.getBukkitEntity();
    }

    public static class BukkitEnderDragonPet extends CraftEnderDragon implements PetEntity {
        private final Player owner;

        public BukkitEnderDragonPet(EntityEnderDragonPet entity) {
            super((CraftServer) Bukkit.getServer(), entity);
            this.owner = entity.owner;
        }

        @Override
        public EnderDragon getBukkitEntity() {
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
