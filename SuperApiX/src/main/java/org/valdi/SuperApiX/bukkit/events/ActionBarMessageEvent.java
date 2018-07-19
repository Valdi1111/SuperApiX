package org.valdi.SuperApiX.bukkit.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ActionBarMessageEvent extends PlayerEvent implements Cancellable {
	  private static final HandlerList handlers = new HandlerList();
	  
	  public HandlerList getHandlers() {
	    return handlers;
	  }
	  
	  public static HandlerList getHandlerList() {
	    return handlers;
	  }
	  
	  private String message;
	  private boolean cancelled = false;
	  
	  public ActionBarMessageEvent(Player player, String message) {
	    super(player);
	    
	    this.message = message;
	  }
	  
	  public String getMessage() {
	    return message;
	  }
	  
	  public void setMessage(String message) {
	    this.message = message;
	  }
	  
	  @Override
	  public boolean isCancelled() {
	    return cancelled;
	  }

	  @Override
	  public void setCancelled(boolean cancelled) {
	    this.cancelled = cancelled;
	  }

}
