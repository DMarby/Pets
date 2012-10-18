package com.fullwall.pets;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public interface PetEntity {
    LivingEntity getBukkitEntity();

    Player getOwner();

    void upgrade();

    void setLevel(int level);
}
