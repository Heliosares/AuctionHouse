/*    */ package com.spawnchunk.auctionhouse.events;
/*    */ 
/*    */ import com.spawnchunk.auctionhouse.modules.ListingType;
/*    */ import java.util.UUID;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.OfflinePlayer;
/*    */ import org.bukkit.event.Event;
/*    */ import org.bukkit.event.HandlerList;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AuctionItemEvent
/*    */   extends Event
/*    */ {
/*    */   private final ItemAction action;
/*    */   private final String world;
/*    */   private final String seller_uuid;
/*    */   private final String buyer_uuid;
/*    */   private final String bidder_uuid;
/*    */   private final float price;
/*    */   private final float reserve;
/*    */   private final float bid;
/*    */   private final ListingType type;
/*    */   private final ItemStack item;
/* 29 */   private static final HandlerList handlers = new HandlerList();
/*    */ 
/*    */   
/*    */   public AuctionItemEvent(@NotNull ItemAction action, String world, @NotNull String seller_uuid, String buyer_uuid, String bidder_uuid, float price, float reserve, float bid, ListingType type, @NotNull ItemStack item) {
/* 33 */     this.action = action;
/* 34 */     this.world = world;
/* 35 */     this.seller_uuid = seller_uuid;
/* 36 */     this.buyer_uuid = buyer_uuid;
/* 37 */     this.bidder_uuid = bidder_uuid;
/* 38 */     this.price = price;
/* 39 */     this.reserve = reserve;
/* 40 */     this.bid = bid;
/* 41 */     this.type = type;
/* 42 */     this.item = item;
/*    */   }
/*    */   
/*    */   public ItemAction getItemAction() {
/* 46 */     return this.action;
/*    */   }
/*    */   public String getWorld() {
/* 49 */     return this.world;
/*    */   }
/*    */   public OfflinePlayer getSeller() {
/*    */     try {
/* 53 */       UUID uuid = UUID.fromString(this.seller_uuid);
/* 54 */       return Bukkit.getOfflinePlayer(uuid);
/* 55 */     } catch (IllegalArgumentException illegalArgumentException) {
/*    */       
/* 57 */       return null;
/*    */     } 
/*    */   } public OfflinePlayer getBuyer() {
/* 60 */     UUID uuid = this.buyer_uuid.isEmpty() ? null : UUID.fromString(this.buyer_uuid);
/* 61 */     return (uuid != null) ? Bukkit.getOfflinePlayer(uuid) : null;
/*    */   }
/*    */   public OfflinePlayer getBidder() {
/* 64 */     UUID uuid = this.bidder_uuid.isEmpty() ? null : UUID.fromString(this.bidder_uuid);
/* 65 */     return (uuid != null) ? Bukkit.getOfflinePlayer(uuid) : null;
/*    */   }
/*    */   public String getSeller_UUID() {
/* 68 */     return this.seller_uuid;
/*    */   }
/*    */   public String getBuyer_UUID() {
/* 71 */     return this.buyer_uuid;
/*    */   }
/*    */   public String getBidder_UUID() {
/* 74 */     return this.bidder_uuid;
/*    */   }
/*    */   public float getPrice() {
/* 77 */     return this.price;
/*    */   }
/*    */   public float getReserve() {
/* 80 */     return this.reserve;
/*    */   }
/*    */   public float getBid() {
/* 83 */     return this.bid;
/*    */   }
/*    */   public ListingType getType() {
/* 86 */     return this.type;
/*    */   }
/*    */   public ItemStack getItem() {
/* 89 */     return this.item;
/*    */   }
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public HandlerList getHandlers() {
/* 95 */     return handlers;
/*    */   }
/*    */   
/*    */   public static HandlerList getHandlerList() {
/* 99 */     return handlers;
/*    */   }
/*    */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\events\AuctionItemEvent.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */