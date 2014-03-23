package se.DMarby.Pets.pet;

import net.minecraft.server.v1_7_R2.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R2.CraftServer;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftBlaze;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Player;
import se.DMarby.Pets.PetEntity;
import se.DMarby.Pets.Util;

public class EntityBlazePet extends EntityBlaze { // old AI
    private final Player owner;

    public EntityBlazePet(World world, Player owner) {
        super(world);
        this.owner = owner;
    }

    public EntityBlazePet(World world) {
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
        /*this.getNavigation().a(owner.getLocation().getX(), owner.getLocation().getY(), owner.getLocation().getZ(), 10F);
        this.getNavigation().a(false);
        getEntitySenses().a();
        getNavigation().f();
        getControllerMove().c();
        getControllerLook().a();
        getControllerJump().b();*/

        ChunkCoordinates thing = new ChunkCoordinates((int)this.owner.getLocation().getX(), (int)this.owner.getEyeLocation().getY(), (int)owner.getLocation().getZ());

        double d1 = thing.x + 0.5D - this.locX;
        double d2 = thing.y + 0.1D - this.locY;
        double d3 = thing.z + 0.5D - this.locZ;

        if (distToOwner() > 5) {
            this.motY += (Math.signum(d2) * 1D - this.motY) * 0.1000000014901161D;
            this.motX += (Math.signum(d1) * 0.25D - this.motX) * 0.1000000014901161D;
            this.motZ += (Math.signum(d3) * 0.25D - this.motZ) * 0.1000000014901161D;
            this.bf = 0.5F;
        }else{
            this.bf = 0;
            this.motX = 0;
            this.motZ = 0;

        }

        float f1 = (float)(Math.atan2(this.motZ, this.motX) * 180.0D / 3.141592741012573D) - 90.0F;
        float f2 = MathHelper.g(f1 - this.yaw);
        this.yaw += f2;

        if (distToOwner() > Util.MAX_DISTANCE)
            this.getBukkitEntity().teleport(owner);
    }

    @Override
    public boolean L(){
        if(owner == null){
            return super.L();
        }
        return false;
    }

    @Override
    public boolean isInvulnerable(){
        if(owner == null){
            return super.isInvulnerable();
        }
        return true;
    }

    @Override
    public CraftEntity getBukkitEntity() {
        if (owner != null && bukkitEntity == null)
            bukkitEntity = new BukkitBlazePet(this);
        return super.getBukkitEntity();
    }

    public static class BukkitBlazePet extends CraftBlaze implements PetEntity {
        private final Player owner;

        public BukkitBlazePet(EntityBlazePet entity) {
            super((CraftServer) Bukkit.getServer(), entity);
            this.owner = entity.owner;
        }

        @Override
        public Blaze getBukkitEntity() {
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
