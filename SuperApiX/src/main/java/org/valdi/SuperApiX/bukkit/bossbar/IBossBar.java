package org.valdi.SuperApiX.bukkit.bossbar;

import java.util.List;

import org.bukkit.entity.Player;

public interface IBossBar {
	
	public String getTitle();
	
	public IBossBar setTitle(String message);
	
	public Color getColor();
	
	public IBossBar setColor(Color color);
	
	public Style getStyle();
	
	public IBossBar setStyle(Style style);

	public double getProgress();

	public IBossBar setProgress(double progress);
	
	public List<Player> getPlayers();
	
	public IBossBar addPlayer(Player player);
	
	public IBossBar removePlayer(Player player);
	
	public boolean hasPlayer(Player player);
	
	public IBossBar removeAll();
	
	public IBossBar addFlag(Flag flag);
	
	public IBossBar removeFlag(Flag flag);
	
	public boolean hasFlag(Flag flag);

	public static enum Color {
		
	    PINK,
	    
	    BLUE,
	    
	    RED,
	    
	    GREEN,
	    
	    YELLOW,
	    
	    PURPLE,
	    
	    WHITE;
		
	}
	
	public static enum Style {
		
	    /**
	     * Makes the boss bar solid (no segments)
	     */
	    SOLID,
	    /**
	     * Splits the boss bar into 6 segments
	     */
	    SEGMENTED_6,
	    /**
	     * Splits the boss bar into 10 segments
	     */
	    SEGMENTED_10,
	    /**
	     * Splits the boss bar into 12 segments
	     */
	    SEGMENTED_12,
	    /**
	     * Splits the boss bar into 20 segments
	     */
	    SEGMENTED_20;
		
	}
	
	public static enum Flag {
		
	    /**
	     * Darkens the sky like during fighting a wither.
	     */
	    DARKEN_SKY,
	    /**
	     * Tells the client to play the Ender Dragon boss music.
	     */
	    PLAY_BOSS_MUSIC,
	    /**
	     * Creates fog around the world.
	     */
	    CREATE_FOG;
		
	}

}
