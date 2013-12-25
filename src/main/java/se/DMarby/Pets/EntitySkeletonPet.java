package se.DMarby.Pets;

import net.minecraft.server.v1_7_R1.EntityHuman;
import net.minecraft.server.v1_7_R1.EntitySkeleton;
import net.minecraft.server.v1_7_R1.GenericAttributes;
import net.minecraft.server.v1_7_R1.World;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftSkeleton;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;

public class EntitySkeletonPet extends EntitySkeleton { // new AI
    private final Player owner;
    private Boolean wither;

    public EntitySkeletonPet(World world, Player owner) {
        super(world);
        this.owner = owner;
        this.wither = false;
        if (owner != null) {
            Util.clearGoals(this.goalSelector, this.targetSelector);
        }
    }

    public EntitySkeletonPet(World world, Player owner, Boolean wither) {
        super(world);
        this.owner = owner;
        this.wither = wither;
        if (owner != null) {
            if(wither){
                this.setSkeletonType(1);
            }
            Util.clearGoals(this.goalSelector, this.targetSelector);
        }
    }

    public EntitySkeletonPet(World world) {
        this(world, null);
    }

    private int distToOwner() {
        EntityHuman handle = ((CraftPlayer) owner).getHandle();
        return (int) (Math.pow(locX - handle.locX, 2) + Math.pow(locY - handle.locY, 2) + Math.pow(locZ
                - handle.locZ, 2));
    }

    @Override
    protected void bn() {
        super.bn();
        if (owner == null)
            return;
        if(distToOwner() > 3){
            this.getNavigation().a(owner.getLocation().getX(), owner.getLocation().getY(), owner.getLocation().getZ(), 1.5F);
            this.getNavigation().a(false);
        }
        if (distToOwner() > Util.MAX_DISTANCE)
            this.getBukkitEntity().teleport(owner);
    }

    @Override
    public float d(float f){
        if(owner == null){
            return super.d(f);
        }
        return 0;
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
            bukkitEntity = new BukkitSkeletonPet(this);
        return super.getBukkitEntity();
    }

    public static class BukkitSkeletonPet extends CraftSkeleton implements PetEntity {
        private final Player owner;

        public BukkitSkeletonPet(EntitySkeletonPet entity) {
            super((CraftServer) Bukkit.getServer(), entity);
            this.owner = entity.owner;
        }

        @Override
        public Skeleton getBukkitEntity() {
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
