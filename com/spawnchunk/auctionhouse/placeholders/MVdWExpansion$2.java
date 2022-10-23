/*    */ package com.spawnchunk.auctionhouse.placeholders;
/*    */ 
/*    */ import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
/*    */ import be.maximvdw.placeholderapi.PlaceholderReplacer;
/*    */ import com.spawnchunk.auctionhouse.modules.Auctions;
/*    */ import org.bukkit.OfflinePlayer;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements PlaceholderReplacer
/*    */ {
/*    */   public String onPlaceholderReplace(PlaceholderReplaceEvent event) {
/* 40 */     Player player = event.getPlayer();
/* 41 */     Auctions.updateCounts(player);
/* 42 */     int count = Auctions.getPlayerListingsCount((OfflinePlayer)player);
/* 43 */     return String.format("%d", new Object[] { Integer.valueOf(count) });
/*    */   }
/*    */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\placeholders\MVdWExpansion$2.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */