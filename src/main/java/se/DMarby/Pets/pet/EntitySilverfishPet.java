package se.DMarby.Pets.pet;

import net.minecraft.server.v1_7_R2.EntitySilverfish;
import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.World;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R2.CraftServer;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftSilverfish;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Player;
import se.DMarby.Pets.PetEntity;
import se.DMarby.Pets.Util;

public class EntitySilverfishPet extends EntitySilverfish { // old AI
    private final Player owner;

    public EntitySilverfishPet(World world, Player owner) {
        super(world);
        this.owner = owner;
    }

    public EntitySilverfishPet(World world) {
        this(world, null);
    }

    private int distToOwner() {
        EntityHuman handle = ((CraftPlayer) owner).getHandle();
        return (int) (Math.pow(locX - handle.locX, 2) + Math.pow(locY - handle.locY, 2) + Math.pow(locZ
                - handle.locZ, 2));
    }

    @Override
    protected void bp() {
        if (owner == null) {
            super.bp();
            return;
        }
        this.W = 10F;
        if(distToOwner() > 3){
            this.getNavigation().a(owner.getLocation().getX(), owner.getLocation().getY(), owner.getLocation().getZ(), 1.5F);
            this.getNavigation().a(false);
            getEntitySenses().a();
            getNavigation().f();
            getControllerMove().c();
            getControllerLook().a();
            getControllerJump().b();
        }
        if (distToOwner() > Util.MAX_DISTANCE)
            this.getBukkitEntity().teleport(owner);
    }

    @Override
    public boolean isInvulnerable(){
        if(owner == null){
            return super.isInvulnerable();
        }
        return true;
    }

    @Override
    public boolean L(){
        if(owner == null){
            return super.L();
        }
        return false;
    }


    @Override
    public CraftEntity getBukkitEntity() {
        if (owner != null && bukkitEntity == null)
            bukkitEntity = new BukkitSilverfishPet(this);
        return super.getBukkitEntity();
    }

    public static class BukkitSilverfishPet extends CraftSilverfish implements PetEntity {
        private final Player owner;

        public BukkitSilverfishPet(EntitySilverfishPet entity) {
            super((CraftServer) Bukkit.getServer(), entity);
            this.owner = entity.owner;
        }

        @Override
        public Silverfish getBukkitEntity() {
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
