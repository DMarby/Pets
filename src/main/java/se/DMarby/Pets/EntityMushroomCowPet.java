package se.DMarby.Pets;

import net.minecraft.server.EntityMushroomCow;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.World;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftMushroomCow;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class EntityMushroomCowPet extends EntityMushroomCow { // new AI
    private final Player owner;

    public EntityMushroomCowPet(World world, Player owner) {
        super(world);
        this.owner = owner;
        if (owner != null)
            Util.clearGoals(this.goalSelector, this.targetSelector);
    }
  public EntityMushroomCowPet(World world) {
        this(world, null);
    }
    
    private int distToOwner() {
        EntityHuman handle = ((CraftPlayer) owner).getHandle();
        return (int) (Math.pow(locX - handle.locX, 2) + Math.pow(locY - handle.locY, 2) + Math.pow(locZ
                - handle.locZ, 2));
    }

    @Override
    protected void bi() {
        super.bi();
        if (owner == null)
            return;
        this.getNavigation().a(((CraftPlayer)owner).getHandle(), 0.3F);
        if (distToOwner() > Util.MAX_DISTANCE)
            this.getBukkitEntity().teleport(owner);
    }

    @Override
    public Entity getBukkitEntity() {
        if (owner != null && bukkitEntity == null)
            bukkitEntity = new BukkitMushroomCowPet(this);
        return super.getBukkitEntity();
    }

    public static class BukkitMushroomCowPet extends CraftMushroomCow implements PetEntity {
        private final Player owner;

        public BukkitMushroomCowPet(EntityMushroomCowPet entity) {
            super((CraftServer) Bukkit.getServer(), entity);
            this.owner = entity.owner;
        }

        @Override
        public MushroomCow getBukkitEntity() {
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