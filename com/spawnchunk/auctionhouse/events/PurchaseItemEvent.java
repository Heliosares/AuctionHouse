/*    */ package com.spawnchunk.auctionhouse.events;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.OfflinePlayer;
/*    */ import org.bukkit.event.Cancellable;
/*    */ import org.bukkit.event.Event;
/*    */ import org.bukkit.event.HandlerList;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ import org.jetbrains.annotations.Nullable;
/*    */ 
/*    */ public class PurchaseItemEvent
/*    */   extends Event
/*    */   implements Cancellable {
/*    */   private final String world;
/*    */   private final String seller_uuid;
/*    */   private final String buyer_uuid;
/*    */   private float price;
/*    */   private ItemStack item;
/*    */   private boolean cancelled = false;
/* 22 */   private static final HandlerList handlers = new HandlerList();
/*    */ 
/*    */   
/*    */   public PurchaseItemEvent(String world, @NotNull String seller_uuid, @NotNull String buyer_uuid, float price, @Nullable ItemStack item) {
/* 26 */     this.world = world;
/* 27 */     this.seller_uuid = seller_uuid;
/* 28 */     this.buyer_uuid = buyer_uuid;
/* 29 */     this.price = price;
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
/*    */   public OfflinePlayer getBuyer() {
/* 41 */     UUID uuid = this.buyer_uuid.isEmpty() ? null : UUID.fromString(this.buyer_uuid);
/* 42 */     return (uuid != null) ? Bukkit.getOfflinePlayer(uuid) : null;
/*    */   }
/*    */   public String getSeller_UUID() {
/* 45 */     return this.seller_uuid;
/*    */   }
/*    */   public String getBuyer_UUID() {
/* 48 */     return this.buyer_uuid;
/*    */   }
/*    */   public float getPrice() {
/* 51 */     return this.price;
/*    */   } public void setPrice(float price) {
/* 53 */     this.price = price;
/*    */   } public ItemStack getItem() {
/* 55 */     return this.item;
/*    */   } public void setItem(ItemStack item) {
/* 57 */     this.item = item.clone();
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   public HandlerList getHandlers() {
/* 62 */     return handlers;
/*    */   }
/*    */   
/*    */   public static HandlerList getHandlerList() {
/* 66 */     return handlers;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCancelled() {
/* 71 */     return this.cancelled;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setCancelled(boolean cancelled) {
/* 76 */     this.cancelled = cancelled;
/*    */   }
/*    */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\events\PurchaseItemEvent.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */