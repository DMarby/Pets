package se.DMarby.Pets.pet;

import net.minecraft.server.v1_8_R1.DamageSource;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.EntityOcelot;
import net.minecraft.server.v1_8_R1.Navigation;
import net.minecraft.server.v1_8_R1.World;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R1.CraftServer;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftOcelot;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;

import se.DMarby.Pets.PetEntity;
import se.DMarby.Pets.Util;

public class EntityOcelotPet extends EntityOcelot { // new AI
    private final Player owner;
    private int idletime = 0;

    public EntityOcelotPet(World world, Player owner) {
        super(world);
        this.owner = owner;
        if (owner != null)
            Util.clearGoals(this.goalSelector, this.targetSelector);
    }

    public EntityOcelotPet(World world) {
        this(world, null);
    }

    private int distToOwner() {
        EntityHuman handle = ((CraftPlayer) owner).getHandle();
        return (int) (Math.pow(locX - handle.locX, 2) + Math.pow(locY - handle.locY, 2) + Math.pow(locZ
                - handle.locZ, 2));
    }

    @Override
    protected void doTick() {
        super.doTick();
        if (owner == null) {
            return;
        }
        this.S = 10F;
        if (distToOwner() > 1) {
            idletime = 0;
            this.setSitting(false);
            this.getNavigation().a(owner.getLocation().getX(), owner.getLocation().getY(), owner.getLocation().getZ(), 1F);
            ((Navigation)this.getNavigation()).d(false);
        } else {
            idletime++;
            if (idletime == 20) {
                this.setSitting(true);
            }
        }
        if (distToOwner() > Util.MAX_DISTANCE)
            this.getBukkitEntity().teleport(owner);
    }

    @Override
    public CraftEntity getBukkitEntity() {
        if (owner != null && bukkitEntity == null)
            bukkitEntity = new BukkitOcelotPet(this);
        return super.getBukkitEntity();
    }
    
    @Override
    public boolean isInvulnerable(DamageSource d) {
        if (owner == null) {
            return super.isInvulnerable(d);
        }
        return true;
    }

    public static class BukkitOcelotPet extends CraftOcelot implements PetEntity {
        private final Player owner;

        public BukkitOcelotPet(EntityOcelotPet entity) {
            super((CraftServer) Bukkit.getServer(), entity);
            this.owner = entity.owner;
        }

        @Override
        public Ocelot getBukkitEntity() {
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
