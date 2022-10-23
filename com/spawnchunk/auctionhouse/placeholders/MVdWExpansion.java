/*    */ package com.spawnchunk.auctionhouse.placeholders;
/*    */ 
/*    */ import be.maximvdw.placeholderapi.PlaceholderAPI;
/*    */ import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
/*    */ import be.maximvdw.placeholderapi.PlaceholderReplacer;
/*    */ import com.spawnchunk.auctionhouse.AuctionHouse;
/*    */ import com.spawnchunk.auctionhouse.modules.Auctions;
/*    */ import org.bukkit.OfflinePlayer;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.plugin.Plugin;
/*    */ 
/*    */ public class MVdWExpansion {
/*    */   private final AuctionHouse plugin;
/*    */   
/*    */   public MVdWExpansion(AuctionHouse plugin) {
/* 16 */     this.plugin = plugin;
/* 17 */     this.identifier = plugin.getName().toLowerCase();
/*    */   }
/*    */   
/*    */   private final String identifier;
/*    */   
/*    */   public void register() {
/* 23 */     PlaceholderAPI.registerPlaceholder((Plugin)this.plugin, this.identifier + "_active_listings", new PlaceholderReplacer()
/*    */         {
/*    */           public String onPlaceholderReplace(PlaceholderReplaceEvent event)
/*    */           {
/* 27 */             Player player = event.getPlayer();
/* 28 */             Auctions.updateCounts(player);
/* 29 */             int count = Auctions.getActiveListingsCount();
/* 30 */             return String.format("%d", new Object[] { Integer.valueOf(count) });
/*    */           }
/*    */         });
/*    */ 
/*    */ 
/*    */     
/* 36 */     PlaceholderAPI.registerPlaceholder((Plugin)this.plugin, this.identifier + "_player_listings", new PlaceholderReplacer()
/*    */         {
/*    */           public String onPlaceholderReplace(PlaceholderReplaceEvent event)
/*    */           {
/* 40 */             Player player = event.getPlayer();
/* 41 */             Auctions.updateCounts(player);
/* 42 */             int count = Auctions.getPlayerListingsCount((OfflinePlayer)player);
/* 43 */             return String.format("%d", new Object[] { Integer.valueOf(count) });
/*    */           }
/*    */         });
/*    */ 
/*    */ 
/*    */     
/* 49 */     PlaceholderAPI.registerPlaceholder((Plugin)this.plugin, this.identifier + "_expired_listings", new PlaceholderReplacer()
/*    */         {
/*    */           public String onPlaceholderReplace(PlaceholderReplaceEvent event)
/*    */           {
/* 53 */             Player player = event.getPlayer();
/* 54 */             Auctions.updateCounts(player);
/* 55 */             int count = Auctions.getExpiredListingsCount((OfflinePlayer)player);
/* 56 */             return String.format("%d", new Object[] { Integer.valueOf(count) });
/*    */           }
/*    */         });
/*    */ 
/*    */ 
/*    */     
/* 62 */     PlaceholderAPI.registerPlaceholder((Plugin)this.plugin, this.identifier + "_sold_items", new PlaceholderReplacer()
/*    */         {
/*    */           public String onPlaceholderReplace(PlaceholderReplaceEvent event)
/*    */           {
/* 66 */             Player player = event.getPlayer();
/* 67 */             Auctions.updateCounts(player);
/* 68 */             int count = Auctions.getSoldItemsCount((OfflinePlayer)player);
/* 69 */             return String.format("%d", new Object[] { Integer.valueOf(count) });
/*    */           }
/*    */         });
/*    */ 
/*    */ 
/*    */     
/* 75 */     PlaceholderAPI.registerPlaceholder((Plugin)this.plugin, this.identifier + "_max_listings", new PlaceholderReplacer()
/*    */         {
/*    */           public String onPlaceholderReplace(PlaceholderReplaceEvent event)
/*    */           {
/* 79 */             Player player = event.getPlayer();
/* 80 */             int count = Auctions.getMaxListings(player);
/* 81 */             return String.format("%d", new Object[] { Integer.valueOf(count) });
/*    */           }
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\placeholders\MVdWExpansion.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */