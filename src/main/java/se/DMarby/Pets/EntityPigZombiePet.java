package se.DMarby.Pets;

import net.minecraft.server.v1_5_R2.*;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_5_R2.CraftServer;
import org.bukkit.craftbukkit.v1_5_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_5_R2.entity.CraftPigZombie;
import org.bukkit.craftbukkit.v1_5_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;

public class EntityPigZombiePet extends EntityPigZombie { // new AI
    private final Player owner;

    public EntityPigZombiePet(World world, Player owner) {
        super(world);
        this.owner = owner;
        if (owner != null) {
            Util.clearGoals(this.goalSelector, this.targetSelector);
            setBaby(true);
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
        if(owner == null){
            return super.findTarget();
        }
        return  ((CraftPlayer) owner).getHandle();
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, int i) {
        return false;
    }

    public void l_() {
        super.l_();
        if (owner == null)
            return;
        this.getNavigation().a(((CraftPlayer) owner).getHandle(), 0.3F);
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