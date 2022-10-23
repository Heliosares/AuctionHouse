/*    */ package com.spawnchunk.auctionhouse.util;
/*    */ 
/*    */ import java.io.File;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WorldUtil
/*    */ {
/*    */   public static String getWorldPrefix(String world) {
/* 12 */     return world.split("_")[0];
/*    */   }
/*    */   
/*    */   public static String getMainWorld() {
/* 16 */     File f = new File("server.properties");
/* 17 */     return PropertyUtil.getProperty(f, "level-name");
/*    */   }
/*    */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhous\\util\WorldUtil.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */