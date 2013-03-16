package se.DMarby.Pets;

import net.minecraft.server.v1_5_R1.EntityWolf;
import net.minecraft.server.v1_5_R1.EntityHuman;
import net.minecraft.server.v1_5_R1.World;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_5_R1.CraftServer;
import org.bukkit.craftbukkit.v1_5_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_5_R1.entity.CraftWolf;
import org.bukkit.craftbukkit.v1_5_R1.entity.CraftPlayer;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Player;

public class EntityWolfPet extends EntityWolf { // new AI
    private final Player owner;

    public EntityWolfPet(World world, Player owner) {
        super(world);
        this.owner = owner;
        if (owner != null)
            Util.clearGoals(this.goalSelector, this.targetSelector);
    }
    
    public EntityWolfPet(World world) {
        this(world, null);
    }

    private int distToOwner() {
        EntityHuman handle = ((CraftPlayer) owner).getHandle();
        return (int) (Math.pow(locX - handle.locX, 2) + Math.pow(locY - handle.locY, 2) + Math.pow(locZ
                - handle.locZ, 2));
    }

    @Override
    protected void bo() {
        super.bo();
        if (owner == null)
            return;
        this.getNavigation().a(((CraftPlayer)owner).getHandle(), 0.3F);
        if (distToOwner() > Util.MAX_DISTANCE)
            this.getBukkitEntity().teleport(owner);
    }

    @Override
    public CraftEntity getBukkitEntity() {
        if (owner != null && bukkitEntity == null)
            bukkitEntity = new BukkitWolfPet(this);
        return super.getBukkitEntity();
    }

    public static class BukkitWolfPet extends CraftWolf implements PetEntity {
        private final Player owner;

        public BukkitWolfPet(EntityWolfPet entity) {
            super((CraftServer) Bukkit.getServer(), entity);
            this.owner = entity.owner;
        }

        @Override
        public Wolf getBukkitEntity() {
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