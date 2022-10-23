/*    */ package com.spawnchunk.auctionhouse.util;
/*    */ 
/*    */ import com.spawnchunk.auctionhouse.config.Config;
/*    */ import org.bukkit.Sound;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SoundUtil
/*    */ {
/*    */   public static void clickSound(Player player) {
/*    */     try {
/* 16 */       Sound sound = Sound.valueOf(Config.click_sound.toUpperCase().replace(".", "_"));
/* 17 */       player.playSound(player.getLocation(), sound, 1.0F, 1.0F);
/* 18 */     } catch (Exception exception) {}
/*    */   }
/*    */ 
/*    */   
/*    */   public static void failSound(Player player) {
/*    */     try {
/* 24 */       Sound sound = Sound.valueOf(Config.fail_sound.toUpperCase().replace(".", "_"));
/* 25 */       player.playSound(player.getLocation(), sound, 1.0F, 1.0F);
/* 26 */     } catch (Exception exception) {}
/*    */   }
/*    */ 
/*    */   
/*    */   public static void dropSound(Player player) {
/*    */     try {
/* 32 */       Sound sound = Sound.valueOf(Config.drop_sound.toUpperCase().replace(".", "_"));
/* 33 */       player.playSound(player.getLocation(), sound, 1.0F, 1.0F);
/* 34 */     } catch (Exception exception) {}
/*    */   }
/*    */ 
/*    */   
/*    */   public static void soldSound(Player player) {
/*    */     try {
/* 40 */       Sound sound = Sound.valueOf(Config.sold_sound.toUpperCase().replace(".", "_"));
/* 41 */       player.playSound(player.getLocation(), sound, 1.0F, 1.0F);
/* 42 */     } catch (Exception exception) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhous\\util\SoundUtil.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */