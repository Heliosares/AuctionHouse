/*    */ package com.spawnchunk.auctionhouse.events;
/*    */ 
/*    */ import com.spawnchunk.auctionhouse.menus.MenuClickType;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.Cancellable;
/*    */ import org.bukkit.event.Event;
/*    */ import org.bukkit.event.HandlerList;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MenuClickEvent
/*    */   extends Event
/*    */   implements Cancellable
/*    */ {
/*    */   private final Player player;
/*    */   private final String id;
/*    */   private final int slot;
/*    */   private final MenuClickType type;
/*    */   private boolean cancelled;
/* 21 */   private static final HandlerList handlers = new HandlerList();
/*    */ 
/*    */   
/*    */   public MenuClickEvent(Player player, String id, int slot, MenuClickType type) {
/* 25 */     this.player = player;
/* 26 */     this.id = id;
/* 27 */     this.slot = slot;
/* 28 */     this.type = type;
/* 29 */     this.cancelled = false;
/*    */   }
/*    */   
/*    */   public Player getPlayer() {
/* 33 */     return this.player;
/*    */   }
/*    */   
/*    */   public String getId() {
/* 37 */     return this.id;
/*    */   }
/*    */   
/*    */   public int getSlot() {
/* 41 */     return this.slot;
/*    */   }
/*    */   
/*    */   public MenuClickType getMenuClickType() {
/* 45 */     return this.type;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCancelled() {
/* 50 */     return this.cancelled;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setCancelled(boolean cancelled) {
/* 55 */     this.cancelled = cancelled;
/*    */   }
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public HandlerList getHandlers() {
/* 61 */     return handlers;
/*    */   }
/*    */   
/*    */   public static HandlerList getHandlerList() {
/* 65 */     return handlers;
/*    */   }
/*    */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\events\MenuClickEvent.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */