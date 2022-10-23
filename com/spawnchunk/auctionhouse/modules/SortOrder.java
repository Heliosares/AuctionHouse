/*    */ package com.spawnchunk.auctionhouse.modules;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum SortOrder
/*    */ {
/*  8 */   CHRONO_OLDEST,
/*  9 */   CHRONO_NEWEST,
/* 10 */   PRICE_LOWEST,
/* 11 */   PRICE_HIGHEST;
/*    */   
/*    */   public String key() {
/* 14 */     String key = "";
/* 15 */     switch (name()) {
/*    */       case "CHRONO_OLDEST":
/* 17 */         key = "message.listing.order.oldest";
/*    */         break;
/*    */       case "CHRONO_NEWEST":
/* 20 */         key = "message.listing.order.newest";
/*    */         break;
/*    */       case "PRICE_LOWEST":
/* 23 */         key = "message.listing.order.lowest_price";
/*    */         break;
/*    */       case "PRICE_HIGHEST":
/* 26 */         key = "message.listing.order.highest_price";
/*    */         break;
/*    */     } 
/* 29 */     return key;
/*    */   }
/*    */   
/*    */   public SortOrder next() {
/* 33 */     int ordinal = ordinal() + 1;
/* 34 */     if (ordinal >= (values()).length) ordinal = 0; 
/* 35 */     return values()[ordinal];
/*    */   }
/*    */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\modules\SortOrder.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */