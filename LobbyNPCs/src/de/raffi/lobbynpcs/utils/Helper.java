package de.raffi.lobbynpcs.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Helper {
	
	public static String stringify(Location t)  {
		return t.getWorld().getName() + ";" + t.getX() + ";"+ t.getY() + ";"+ t.getZ() + ";"+ t.getYaw() + ";"+ t.getPitch();
	}
	public static Location createLocation(String s){
		String[] l = s.split(";");
		return new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]),Double.valueOf(l[2]), Double.valueOf(l[3]), Float.valueOf(l[4]), Float.valueOf(l[5]));
	}
}
