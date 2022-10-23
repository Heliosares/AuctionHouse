/*    */ package com.spawnchunk.auctionhouse.events;
/*    */ 
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.event.Event;
/*    */ import org.bukkit.event.HandlerList;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ListingCleanupEvent
/*    */   extends Event
/*    */ {
/*    */   private final CommandSender sender;
/*    */   private final Server server;
/* 17 */   private static final HandlerList handlers = new HandlerList();
/*    */ 
/*    */   
/*    */   public ListingCleanupEvent(CommandSender sender, Server server) {
/* 21 */     this.sender = sender;
/* 22 */     this.server = server;
/*    */   }
/*    */   
/*    */   public CommandSender getSender() {
/* 26 */     return this.sender;
/*    */   }
/*    */   
/*    */   public Server getServer() {
/* 30 */     return this.server;
/*    */   }
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public HandlerList getHandlers() {
/* 36 */     return handlers;
/*    */   }
/*    */   
/*    */   public static HandlerList getHandlerList() {
/* 40 */     return handlers;
/*    */   }
/*    */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\events\ListingCleanupEvent.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */