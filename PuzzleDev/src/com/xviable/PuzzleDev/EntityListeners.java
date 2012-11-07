package com.xviable.PuzzleDev;

import java.util.ArrayList;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.entity.CraftChicken;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.entity.CraftSquid;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Diode;
import org.bukkit.util.Vector;

public class EntityListeners implements Listener {

	private PuzzleDev plugin;

	public EntityListeners(PuzzleDev plugin) {
		this.plugin = plugin;
	}


	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		
		if (event.getAction() == event.getAction().RIGHT_CLICK_BLOCK && p.getItemInHand().getTypeId() == 280) {
			Block b = event.getClickedBlock();
			BlockFace bf = event.getBlockFace();
			BlockFace obf = event.getBlockFace().getOppositeFace();
			
			if (b.getType() == Material.SAND && obf != BlockFace.DOWN) {
				
				Location loc = b.getLocation();
				World w = b.getWorld();
				
				int faceX = obf.getModX();
				int faceZ = obf.getModZ();
				
				b.setTypeId(0);
				
				Entity sand = w.spawnFallingBlock(loc, Material.SAND, (byte) 0);
				Vector v = new Vector((faceX * 1.2), .2, (faceZ * 1.2));
				sand.setVelocity(v);
				
			}
		}
		
		
		if (event.getAction() == event.getAction().PHYSICAL) {
		Block b = event.getClickedBlock();
		if (b.getType() == Material.STONE_PLATE) {
		for (int z = -1; z <= 1; z++) {
		for (int x = -1; x <= 1; x++) {
		for (int y = -1; y <= 1; y++) {
		if ((x * x + y * y + z * z == 1) && b.getRelative(x, y, z).getTypeId() == 94 || (x * x + y * y + z * z == 1) && b.getRelative(x, y, z).getTypeId() == 93) {
		Diode diode = (Diode) b.getRelative(x, y, z).getState().getData();
		BlockFace bf = diode.getFacing();
		int delay = diode.getDelay();
		Vector v1 = new Vector(0, 0, 0);
		p.setVelocity(v1);
		Vector v = new Vector(bf.getModX(), bf.getModY(), bf.getModZ());
		v.setX(bf.getModX() * (delay / 1.45));
		v.setZ(bf.getModZ() * (delay / 1.45));
		v.setY(1);
		p.setVelocity(v);
		}
		}
		}
		}
		}
		}

		if (event.getAction() == event.getAction().LEFT_CLICK_AIR || event.getAction() == event.getAction().LEFT_CLICK_BLOCK) {
		if (p.getPassenger() != null) {
		Entity target = p.getPassenger();
		if (target instanceof CraftSquid) {
		((CraftSquid) target).leaveVehicle();
		Location loc = p.getLocation();
		loc.setY(loc.getY() + 8);
		loc.add(p.getLocation().getDirection().multiply(2));
		target.teleport(loc);
		((CraftLivingEntity) target).setHealth(((CraftLivingEntity) target).getMaxHealth());
		}
		}
		}
		}

	
	@EventHandler
	public void onEntityInteract(EntityInteractEvent event) {
		Block b = event.getBlock();
		if (b.getType() == Material.STONE_PLATE) {
			Entity p = event.getEntity();
			for (int z = -1; z <= 1; z++) {
				for (int x = -1; x <= 1; x++) {
					for (int y = -1; y <= 1; y++) {
						if ((x * x + y * y + z * z == 1) && b.getRelative(x, y, z).getTypeId() == 94 || (x * x + y * y + z * z == 1) && b.getRelative(x, y, z).getTypeId() == 93) {
							Location loc = b.getRelative(0,1,0).getLocation();
//							System.out.println(b.getRelative(0,1,0).getLocation());
							Vector reset = new Vector(0,0,0);
							p.setVelocity(reset);
							Diode diode = (Diode) b.getRelative(x, y, z).getState().getData();
							BlockFace bf = diode.getFacing();
							int delay = diode.getDelay();
							Vector v = new Vector(bf.getModX(), bf.getModY(), bf.getModZ());
							p.setVelocity(v);
							v.setX(bf.getModX() * (delay/1.5));
							v.setZ(bf.getModZ() * (delay/1.5));
							v.setY(1);
//							System.out.println(delay);
							p.setVelocity(v);
						}
					}
				}
			}
		}
	}
	
	
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
	Player player = event.getPlayer();
	Entity target = event.getRightClicked();
	Entity vehicle = target.getVehicle();

	if (player.getPassenger() != null) {
	Entity passenger = player.getPassenger();
	if (target instanceof CraftSquid) {
	((CraftSquid) passenger).leaveVehicle();
	Location loc = player.getLocation();
	loc.setY(loc.getY() + 8);
	loc.add(player.getLocation().getDirection().multiply(2));
	passenger.teleport(loc);
	((CraftLivingEntity) passenger).setHealth(((CraftLivingEntity) passenger).getMaxHealth());
	}
	} else if (target instanceof CraftSquid) {
	player.setPassenger(target);
	}
	}


}
