/*    */ package com.spawnchunk.auctionhouse.modules;
/*    */ 
/*    */ import com.spawnchunk.auctionhouse.AuctionHouse;
/*    */ import com.spawnchunk.auctionhouse.config.Config;
/*    */ import net.milkbowl.vault.economy.EconomyResponse;
/*    */ import org.bukkit.OfflinePlayer;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Economy
/*    */ {
/*    */   public static double getBalance(OfflinePlayer player, String world) {
/* 13 */     if (Config.economy.equalsIgnoreCase("vault"))
/* 14 */       return AuctionHouse.econ.getBalance(player, world); 
/* 15 */     if (Config.economy.equalsIgnoreCase("tokenenchant")) {
/* 16 */       return AuctionHouse.te.getTokens(player);
/*    */     }
/* 18 */     return 0.0D;
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean withdrawPlayer(OfflinePlayer player, String world, double amount) {
/* 23 */     if (Config.economy.equalsIgnoreCase("vault")) {
/* 24 */       EconomyResponse withdrawResponse = AuctionHouse.econ.withdrawPlayer(player, world, amount);
/* 25 */       if (withdrawResponse != null && withdrawResponse.transactionSuccess()) {
/* 26 */         return true;
/*    */       }
/* 28 */       if (Config.debug) {
/* 29 */         AuctionHouse.logger.info("Warning! Vault could not withdraw from the buyer");
/* 30 */         if (withdrawResponse == null) {
/* 31 */           AuctionHouse.logger.info("Economy plugin response is null!");
/*    */         } else {
/* 33 */           AuctionHouse.logger.info(String.format("Economy plugin response type %s", new Object[] { withdrawResponse.type.toString() }));
/* 34 */           AuctionHouse.logger.info(String.format("Economy plugin response message %s", new Object[] { withdrawResponse.errorMessage }));
/*    */         } 
/*    */       } 
/* 37 */       return false;
/*    */     } 
/* 39 */     if (Config.economy.equalsIgnoreCase("tokenenchant")) {
/* 40 */       double tokens = AuctionHouse.te.getTokens(player);
/* 41 */       if (tokens >= amount) {
/* 42 */         AuctionHouse.te.removeTokens(player, amount);
/* 43 */         return true;
/*    */       } 
/* 45 */       return false;
/*    */     } 
/*    */     
/* 48 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean depositPlayer(OfflinePlayer player, String world, double amount) {
/* 53 */     if (Config.economy.equalsIgnoreCase("vault")) {
/* 54 */       EconomyResponse depositResponse = AuctionHouse.econ.depositPlayer(player, world, amount);
/* 55 */       if (depositResponse != null && depositResponse.transactionSuccess()) {
/* 56 */         return true;
/*    */       }
/* 58 */       if (Config.debug) {
/* 59 */         AuctionHouse.logger.info("Warning! Vault could not deposit to the seller");
/* 60 */         if (depositResponse == null) {
/* 61 */           AuctionHouse.logger.info("Economy plugin response is null!");
/*    */         } else {
/* 63 */           AuctionHouse.logger.info(String.format("Economy plugin response type %s", new Object[] { depositResponse.type.toString() }));
/* 64 */           AuctionHouse.logger.info(String.format("Economy plugin response message %s", new Object[] { depositResponse.errorMessage }));
/*    */         } 
/*    */       } 
/* 67 */       return false;
/*    */     } 
/*    */     
/* 70 */     if (Config.economy.equalsIgnoreCase("tokenenchant")) {
/* 71 */       AuctionHouse.te.addTokens(player, amount);
/*    */       
/* 73 */       return true;
/*    */     } 
/* 75 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\modules\Economy.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */