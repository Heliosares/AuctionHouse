/*    */ package com.spawnchunk.auctionhouse.storage;
/*    */ 
/*    */ import com.spawnchunk.auctionhouse.AuctionHouse;
/*    */ import com.spawnchunk.auctionhouse.util.MessageUtil;
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.util.TreeMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AuctionsStorage
/*    */ {
/*    */   public static TreeMap<Long, String> loadAuctionsFile() {
/* 20 */     TreeMap<Long, String> map = new TreeMap<>();
/* 21 */     String path = AuctionHouse.plugin.getDataFolder().getPath();
/* 22 */     String fname = path + path + "auctions.dat";
/* 23 */     File dir = new File(path);
/* 24 */     if (!dir.exists()) {
/* 25 */       dir.mkdirs();
/*    */     }
/* 27 */     File fn = new File(fname);
/* 28 */     if (!fn.exists()) {
/*    */       try {
/* 30 */         fn.createNewFile();
/* 31 */       } catch (Exception e) {
/* 32 */         MessageUtil.logSevere("Error! Could not create the data file!");
/* 33 */         e.printStackTrace();
/* 34 */         return null;
/*    */       } 
/*    */     }
/*    */     try {
/* 38 */       FileInputStream fis = new FileInputStream(fname);
/* 39 */       if (fis.available() > 0) {
/*    */         try {
/* 41 */           ObjectInputStream ois = new ObjectInputStream(fis);
/* 42 */           map = (TreeMap<Long, String>)ois.readObject();
/* 43 */           ois.close();
/* 44 */         } catch (Exception e) {
/* 45 */           MessageUtil.logSevere("Error! Could not read the data file!");
/* 46 */           e.printStackTrace();
/* 47 */           return null;
/*    */         } 
/*    */       }
/* 50 */       fis.close();
/* 51 */       AuctionHouse.logger.info(String.format("Loaded %d listings", new Object[] { Integer.valueOf((map != null) ? map.size() : 0) }));
/* 52 */     } catch (Exception e) {
/* 53 */       MessageUtil.logSevere("Error! Could not load the data file!");
/* 54 */       e.printStackTrace();
/* 55 */       return null;
/*    */     } 
/* 57 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\storage\AuctionsStorage.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */