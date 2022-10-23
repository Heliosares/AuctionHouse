/*    */ package com.spawnchunk.auctionhouse.events;
/*    */ 
/*    */ import com.spawnchunk.auctionhouse.modules.ListingType;
/*    */ import java.util.UUID;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.OfflinePlayer;
/*    */ import org.bukkit.event.Cancellable;
/*    */ import org.bukkit.event.Event;
/*    */ import org.bukkit.event.HandlerList;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ public class ListItemEvent
/*    */   extends Event
/*    */   implements Cancellable {
/*    */   private final String world;
/*    */   private final String seller_uuid;
/*    */   private float price;
/*    */   private ItemStack item;
/*    */   private ListingType type;
/*    */   private boolean cancelled = false;
/* 22 */   private static final HandlerList handlers = new HandlerList();
/*    */ 
/*    */   
/*    */   public ListItemEvent(String world, @NotNull String seller_uuid, float price, ListingType type, @NotNull ItemStack item) {
/* 26 */     this.world = world;
/* 27 */     this.seller_uuid = seller_uuid;
/* 28 */     this.price = price;
/* 29 */     this.type = type;
/* 30 */     this.item = item;
/*    */   }
/*    */   
/*    */   public String getWorld() {
/* 34 */     return this.world;
/*    */   }
/*    */   public OfflinePlayer getSeller() {
/* 37 */     UUID uuid = UUID.fromString(this.seller_uuid);
/* 38 */     return Bukkit.getOfflinePlayer(uuid);
/*    */   }
/*    */   public String getSeller_UUID() {
/* 41 */     return this.seller_uuid;
/*    */   }
/*    */   
/*    */   public float getPrice() {
/* 45 */     return this.price;
/*    */   }
/* 47 */   public void setPrice(float price) { this.price = price; }
/* 48 */   public ListingType getType() { return this.type; } public void setType(ListingType type) {
/* 49 */     this.type = type;
/*    */   } public ItemStack getItem() {
/* 51 */     return this.item;
/*    */   } public void setItem(ItemStack item) {
/* 53 */     this.item = item.clone();
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   public HandlerList getHandlers() {
/* 58 */     return handlers;
/*    */   }
/*    */   
/*    */   public static HandlerList getHandlerList() {
/* 62 */     return handlers;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCancelled() {
/* 67 */     return this.cancelled;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setCancelled(boolean cancelled) {
/* 72 */     this.cancelled = cancelled;
/*    */   }
/*    */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\events\ListItemEvent.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */