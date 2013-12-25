package se.DMarby.Pets;

import net.minecraft.server.v1_7_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftEnderman;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Player;

public class EntityEndermanPet extends EntityEnderman { // new AI
    private final Player owner;
    public EntityEndermanPet(World world, Player owner) {
        super(world);
        this.owner = owner;
        if (owner != null)
            Util.clearGoals(this.goalSelector, this.targetSelector);
    }

    public EntityEndermanPet(World world, Player owner, Boolean ridable) {
        super(world);
        this.owner = owner;
        if (owner != null)
            Util.clearGoals(this.goalSelector, this.targetSelector);
    }

    public EntityEndermanPet(World world) {
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
        return null;
    }

    @Override
    public void bq() {;
        if (owner == null){
            super.bq();
            return;
        }
        if(distToOwner() > 3){
            this.getNavigation().a(owner.getLocation().getX(), owner.getLocation().getY(), owner.getLocation().getZ(), 5F);
            this.getNavigation().a(false);
            getEntitySenses().a();
            getNavigation().f();
            getControllerMove().c(); // old API
            getControllerLook().a(); // old API
            getControllerJump().b(); // etc
        }
        this.a(false);
        if (distToOwner() > Util.MAX_DISTANCE) {
            this.getBukkitEntity().teleport(owner);
        }
    }

    @Override
    protected boolean bX() {
        if(owner == null){
            return super.bX();
        }
        return false;
    }

    @Override
    protected boolean k(double d0, double d1, double d2) {
        if(owner == null){
            return super.k(d0, d1, d2);
        }
        return false;
    }

    @Override
    public Block getCarried(){
        if(owner == null){
            return super.getCarried();
        }
        return Block.e(1);
    }

    @Override
    public int getCarriedData(){
        if(owner == null){
            return super.getCarriedData();
        }
        return 1;
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
            bukkitEntity = new BukkitEndermanPet(this);
        return super.getBukkitEntity();
    }

    public static class BukkitEndermanPet extends CraftEnderman implements PetEntity {
        private final Player owner;

        public BukkitEndermanPet(EntityEndermanPet entity) {
            super((CraftServer) Bukkit.getServer(), (EntityEnderman) entity);
            this.owner = entity.owner;
        }

        @Override
        public Enderman getBukkitEntity() {
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
