package se.DMarby.Pets.pet;

import net.minecraft.server.v1_7_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftWitherSkull;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import se.DMarby.Pets.PetEntity;
import se.DMarby.Pets.Util;

public class EntityWitherSkullPet extends EntityWitherSkull { // new AI
    private final Player owner;
    private Boolean blue;

    public EntityWitherSkullPet(World world, Player owner, Boolean blue) {
        super(world, ((CraftPlayer) owner).getHandle(), 0, 0, 0);
        this.blue = blue;
        this.owner = owner;
    }

    public EntityWitherSkullPet(World world, Player owner) {
        super(world, ((CraftPlayer) owner).getHandle(), 0, 0, 0);
        this.blue = false;
        this.owner = owner;
    }

    public EntityWitherSkullPet(World world) {
        super(world);
        this.blue = false;
        this.owner = null;
    }

    private int distToOwner() {
        EntityHuman handle = ((CraftPlayer) owner).getHandle();
        return (int) (Math.pow(locX - handle.locX, 2) + Math.pow(locY - handle.locY, 2) + Math.pow(locZ
                - handle.locZ, 2));
    }

    @Override
    protected void a(MovingObjectPosition movingobjectposition) {
      if(owner == null){
          super.a(movingobjectposition);
      }
    }

    @Override
    public float e() {
        if(owner == null){
            return super.e();
        }
        return 0;
    }

    @Override
    public void h() {
        if (owner == null){
            super.h();
            return;
        }

        if(this.blue){
            this.a(true);
        }

        //super.C();
        this.dirY = 0;
        this.dirX = 0;
        this.dirZ = 0;


        ChunkCoordinates thing = new ChunkCoordinates((int)this.owner.getLocation().getX(), (int)this.owner.getEyeLocation().getY() + 1, (int)owner.getLocation().getZ());

        double d1 = thing.x + 0.5D - this.locX;
        double d2 = thing.y + 0.1D - this.locY;
        double d3 = thing.z + 0.5D - this.locZ;


        if (distToOwner() > 2) {
            this.motY += (Math.signum(d2) * 0.22D - this.motY) * 0.1000000014901161D;
            this.motX += (Math.signum(d1) * 0.22D - this.motX) * 0.1000000014901161D;
            this.motZ += (Math.signum(d3) * 0.22D - this.motZ) * 0.1000000014901161D;
            this.locX += this.motX;
            this.locY += this.motY;
            this.locZ += this.motZ;

            this.pitch = this.lastPitch + (this.pitch - this.lastPitch) * 0.2F;
            this.yaw = this.lastYaw + (this.yaw - this.lastYaw) * 0.2F;

        }else{
            this.motX = 0;
            this.motY = 0;
            this.motZ = 0;

        }

        this.C();
        this.setPositionRotation(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
        this.setPosition(this.locX, this.locY, this.locZ);


        if (distToOwner() > Util.MAX_DISTANCE) {
            this.getBukkitEntity().teleport(owner);
        }

        this.positionChanged = true;
        this.velocityChanged = true;
    }

    @Override
    public void die(){
        if(owner == null){
            super.die();
        }
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
            bukkitEntity = new BukkitWitherSkullPet(this);
        return super.getBukkitEntity();
    }

    public static class BukkitWitherSkullPet extends CraftWitherSkull implements PetEntity {
        private final Player owner;

        public BukkitWitherSkullPet(EntityWitherSkullPet entity) {
            super((CraftServer) Bukkit.getServer(), entity);
            this.owner = entity.owner;
        }

        @Override
        public WitherSkull getBukkitEntity() {
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
