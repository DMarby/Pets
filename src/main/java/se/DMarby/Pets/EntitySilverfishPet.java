package se.DMarby.Pets;

import net.minecraft.server.EntitySilverfish;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.World;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftSilverfish;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

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
    protected void bn() {
        if (owner == null) {
            super.bn();
            return;
        }
        this.getNavigation().a(((CraftPlayer)owner).getHandle(), 0.7F);
        this.getNavigation().e(); // this is only needed for old ai
        getControllerMove().c();
        getControllerLook().a();
        getControllerJump().b();
        if (distToOwner() > Util.MAX_DISTANCE)
            this.getBukkitEntity().teleport(owner);
    }

    @Override
    public Entity getBukkitEntity() {
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