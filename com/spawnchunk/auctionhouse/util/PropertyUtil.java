/*    */ package com.spawnchunk.auctionhouse.util;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.util.Properties;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class PropertyUtil
/*    */ {
/*    */   public static String getProperty(File f, String property) {
/* 16 */     Properties pr = new Properties();
/*    */     try {
/* 18 */       FileInputStream in = new FileInputStream(f);
/* 19 */       pr.load(in);
/* 20 */       return pr.getProperty(property);
/* 21 */     } catch (IOException iOException) {
/*    */       
/* 23 */       return "";
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void setProperty(File f, String property, String value) {
/* 28 */     Properties pr = new Properties();
/*    */     
/*    */     try {
/* 31 */       FileInputStream in = new FileInputStream(f);
/* 32 */       pr.load(in);
/* 33 */       in.close();
/* 34 */       pr.setProperty(property, value);
/* 35 */       FileOutputStream out = new FileOutputStream(f);
/* 36 */       pr.store(out, "");
/* 37 */       out.close();
/*    */     }
/* 39 */     catch (IOException iOException) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhous\\util\PropertyUtil.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */