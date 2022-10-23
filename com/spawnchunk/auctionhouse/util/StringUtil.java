/*    */ package com.spawnchunk.auctionhouse.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StringUtil
/*    */ {
/*    */   public static String leftOf(String s, int pos) {
/* 10 */     return (pos > 0) ? s.substring(0, pos) : "";
/*    */   }
/*    */   
/*    */   public static String rightOf(String s, int pos) {
/* 14 */     return (pos + 1 < s.length()) ? s.substring(pos + 1) : "";
/*    */   }
/*    */   
/*    */   public static String unquote(String s) {
/* 18 */     if ((s.startsWith("\"") && s.endsWith("\"")) || (s.startsWith("'") && s.endsWith("'"))) {
/* 19 */       if (s.length() <= 2) return ""; 
/* 20 */       return s.substring(1, s.length() - 2);
/*    */     } 
/* 22 */     return s;
/*    */   }
/*    */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhous\\util\StringUtil.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */