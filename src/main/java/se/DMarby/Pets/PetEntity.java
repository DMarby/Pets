package se.DMarby.Pets;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface PetEntity {
    Entity getBukkitEntity();

    Player getOwner ();

    void upgrade ();

    void setLevel (int level);
}
