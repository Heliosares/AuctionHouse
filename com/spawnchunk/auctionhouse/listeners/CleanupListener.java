/*    */ package com.spawnchunk.auctionhouse.listeners;
/*    */ 
/*    */ import com.spawnchunk.auctionhouse.AuctionHouse;
/*    */ import com.spawnchunk.auctionhouse.events.ListingCleanupEvent;
/*    */ import com.spawnchunk.auctionhouse.modules.Auctions;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.EventPriority;
/*    */ import org.bukkit.event.Listener;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CleanupListener
/*    */   implements Listener
/*    */ {
/*    */   @EventHandler(priority = EventPriority.NORMAL)
/*    */   public void onCleanup(ListingCleanupEvent event) {
/* 20 */     int removed = 0;
/* 21 */     removed += Auctions.deleteAbandonedItems();
/* 22 */     removed += Auctions.deleteExpiredSoldItems();
/* 23 */     if (removed > 0) AuctionHouse.logger.info(String.format("Cleaned %d listing%s", new Object[] { Integer.valueOf(removed), (removed > 1) ? "s" : "" })); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\listeners\CleanupListener.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */