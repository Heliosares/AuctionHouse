/*    */ package com.spawnchunk.auctionhouse.util;
/*    */ 
/*    */ import com.spawnchunk.auctionhouse.config.Config;
/*    */ import com.spawnchunk.auctionhouse.storage.LocaleStorage;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TimeUtil
/*    */ {
/*    */   private static final long second = 1000L;
/*    */   private static final long minute = 60000L;
/*    */   private static final long hour = 3600000L;
/*    */   private static final long day = 86400000L;
/* 16 */   private static long last = System.currentTimeMillis();
/*    */   
/*    */   public static long now() {
/* 19 */     long timestamp = System.currentTimeMillis();
/* 20 */     if (timestamp == last) timestamp++; 
/* 21 */     last = timestamp;
/* 22 */     return timestamp;
/*    */   }
/*    */ 
/*    */   
/*    */   private static int days(long t) {
/* 27 */     long duration = Math.abs(t);
/* 28 */     return (int)Math.floorDiv(duration, 86400000L);
/*    */   }
/*    */ 
/*    */   
/*    */   private static int hours(long t) {
/* 33 */     long duration = Math.abs(t);
/* 34 */     long hours = Math.floorMod(duration, 86400000L);
/* 35 */     return (int)Math.floorDiv(hours, 3600000L);
/*    */   }
/*    */ 
/*    */   
/*    */   private static int minutes(long t) {
/* 40 */     long duration = Math.abs(t);
/* 41 */     long hours = Math.floorMod(duration, 86400000L);
/* 42 */     long minutes = Math.floorMod(hours, 3600000L);
/* 43 */     return (int)Math.floorDiv(minutes, 60000L);
/*    */   }
/*    */ 
/*    */   
/*    */   private static int seconds(long t) {
/* 48 */     long duration = Math.abs(t);
/* 49 */     long hours = Math.floorMod(duration, 86400000L);
/* 50 */     long minutes = Math.floorMod(hours, 3600000L);
/* 51 */     long seconds = Math.floorMod(minutes, 60000L);
/* 52 */     return (int)Math.floorDiv(seconds, 1000L);
/*    */   }
/*    */   
/*    */   public static long duration(int days, int hours, int minutes, int seconds) {
/* 56 */     return days * 86400000L + hours * 3600000L + minutes * 60000L + seconds * 1000L;
/*    */   }
/*    */   
/*    */   public static String duration(long t, boolean flag) {
/* 60 */     long duration = Math.abs(t);
/* 61 */     int d = days(duration);
/* 62 */     int h = hours(duration);
/* 63 */     int m = minutes(duration);
/* 64 */     int s = seconds(duration);
/* 65 */     String days = LocaleStorage.translate("duration.days", Config.locale);
/* 66 */     String hours = LocaleStorage.translate("duration.hours", Config.locale);
/* 67 */     String minutes = LocaleStorage.translate("duration.minutes", Config.locale);
/* 68 */     String seconds = LocaleStorage.translate("duration.seconds", Config.locale);
/* 69 */     if (d > 0) return flag ? String.format("%d%s %d%s %d%s %d%s", new Object[] { Integer.valueOf(d), days, Integer.valueOf(h), hours, Integer.valueOf(m), minutes, Integer.valueOf(s), seconds }) : String.format("%d%s %d%s %d%s", new Object[] { Integer.valueOf(d), days, Integer.valueOf(h), hours, Integer.valueOf(m), minutes }); 
/* 70 */     if (h > 0) return flag ? String.format("%d%s %d%s %d%s", new Object[] { Integer.valueOf(h), hours, Integer.valueOf(m), minutes, Integer.valueOf(s), seconds }) : String.format("%d%s %d%s", new Object[] { Integer.valueOf(h), hours, Integer.valueOf(m), minutes }); 
/* 71 */     if (m > 0) return flag ? String.format("%d%s %d%s", new Object[] { Integer.valueOf(m), minutes, Integer.valueOf(s), seconds }) : String.format("%d%s", new Object[] { Integer.valueOf(m), minutes }); 
/* 72 */     if (s > 0) return flag ? String.format("%d%s", new Object[] { Integer.valueOf(s), seconds }) : String.format("<1%s", new Object[] { minutes }); 
/* 73 */     return LocaleStorage.translate("duration.now", Config.locale);
/*    */   }
/*    */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhous\\util\TimeUtil.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */