package se.DMarby.Pets.pet;

import net.minecraft.server.v1_8_R1.DamageSource;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.EntitySpider;
import net.minecraft.server.v1_8_R1.Navigation;
import net.minecraft.server.v1_8_R1.World;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R1.CraftServer;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftSpider;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;

import se.DMarby.Pets.PetEntity;
import se.DMarby.Pets.Util;

public class EntitySpiderPet extends EntitySpider { // old AI
    private final Player owner;

    public EntitySpiderPet(World world, Player owner) {
        super(world);
        this.owner = owner;
    }

    public EntitySpiderPet(World world) {
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
        this.S = 10F;
        if (distToOwner() > 3) {
            this.getNavigation().a(owner.getLocation().getX(), owner.getLocation().getY(), owner.getLocation().getZ(), 1.2F);
            ((Navigation)this.getNavigation()).d(false);
            getEntitySenses().a();
            getNavigation().k();
            getControllerMove().c();
            getControllerLook().a();
            getControllerJump().b();
        }
        if (distToOwner() > Util.MAX_DISTANCE)
            this.getBukkitEntity().teleport(owner);
    }

    @Override
    public CraftEntity getBukkitEntity() {
        if (owner != null && bukkitEntity == null)
            bukkitEntity = new BukkitSpiderPet(this);
        return super.getBukkitEntity();
    }
    
    @Override
    public boolean isInvulnerable(DamageSource d) {
        if (owner == null) {
            return super.isInvulnerable(d);
        }
        return true;
    }

    public static class BukkitSpiderPet extends CraftSpider implements PetEntity {
        private final Player owner;

        public BukkitSpiderPet(EntitySpiderPet entity) {
            super((CraftServer) Bukkit.getServer(), entity);
            this.owner = entity.owner;
        }

        @Override
        public Spider getBukkitEntity() {
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
