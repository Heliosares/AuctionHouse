/*    */ package com.spawnchunk.auctionhouse.events;
/*    */ 
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.Event;
/*    */ import org.bukkit.event.HandlerList;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MenuCloseEvent
/*    */   extends Event
/*    */ {
/*    */   private final Player player;
/*    */   private final String id;
/* 16 */   private static final HandlerList handlers = new HandlerList();
/*    */ 
/*    */   
/*    */   public MenuCloseEvent(Player player, String id) {
/* 20 */     this.player = player;
/* 21 */     this.id = id;
/*    */   }
/*    */   
/*    */   public Player getPlayer() {
/* 25 */     return this.player;
/*    */   }
/*    */   
/*    */   public String getId() {
/* 29 */     return this.id;
/*    */   }
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public HandlerList getHandlers() {
/* 35 */     return handlers;
/*    */   }
/*    */   
/*    */   public static HandlerList getHandlerList() {
/* 39 */     return handlers;
/*    */   }
/*    */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\events\MenuCloseEvent.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */