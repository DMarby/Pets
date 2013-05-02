package se.DMarby.Pets;

import net.minecraft.server.v1_5_R3.EntityCaveSpider;
import net.minecraft.server.v1_5_R3.EntityHuman;
import net.minecraft.server.v1_5_R3.World;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_5_R3.CraftServer;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftCaveSpider;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftPlayer;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Player;

public class EntityCaveSpiderPet extends EntityCaveSpider { // old AI
    private final Player owner;

    public EntityCaveSpiderPet(World world, Player owner) {
        super(world);
        this.owner = owner;
    }

    public EntityCaveSpiderPet(World world) {
        this(world, null);
    }

    private int distToOwner() {
        EntityHuman handle = ((CraftPlayer) owner).getHandle();
        return (int) (Math.pow(locX - handle.locX, 2) + Math.pow(locY - handle.locY, 2) + Math.pow(locZ
                - handle.locZ, 2));
    }

    @Override
    protected void bq() {
        if (owner == null) {
            super.bq();
            return;
        }
        this.getNavigation().a(((CraftPlayer)owner).getHandle(), 0.8F);
        this.getNavigation().e(); // this is only needed for old ai
        getControllerMove().c();
        getControllerLook().a();
        getControllerJump().b();
        if (distToOwner() > Util.MAX_DISTANCE)
            this.getBukkitEntity().teleport(owner);
    }

    @Override
    public CraftEntity getBukkitEntity() {
        if (owner != null && bukkitEntity == null)
            bukkitEntity = new BukkitCaveSpiderPet(this);
        return super.getBukkitEntity();
    }

    public static class BukkitCaveSpiderPet extends CraftCaveSpider implements PetEntity {
        private final Player owner;

        public BukkitCaveSpiderPet(EntityCaveSpiderPet entity) {
            super((CraftServer) Bukkit.getServer(), entity);
            this.owner = entity.owner;
        }

        @Override
        public CaveSpider getBukkitEntity() {
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