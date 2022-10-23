/*    */ package com.spawnchunk.auctionhouse.modules;
/*    */ 
/*    */ public enum ListingType {
/*  4 */   PLAYER_LISTING,
/*  5 */   PLAYER_AUCTION,
/*  6 */   SERVER_LISTING,
/*  7 */   SERVER_LISTING_UNLIMITED;
/*    */   
/*    */   public boolean isServer() {
/* 10 */     return name().startsWith("SERVER_");
/*    */   }
/*    */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\modules\ListingType.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */