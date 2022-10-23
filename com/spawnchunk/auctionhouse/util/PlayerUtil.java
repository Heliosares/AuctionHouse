/*    */ package com.spawnchunk.auctionhouse.util;
/*    */ 
/*    */ import com.spawnchunk.auctionhouse.AuctionHouse;
/*    */ import com.spawnchunk.auctionhouse.config.Config;
/*    */ import com.spawnchunk.auctionhouse.modules.Economy;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.OfflinePlayer;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.bukkit.inventory.meta.ItemMeta;
/*    */ import org.bukkit.inventory.meta.SkullMeta;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PlayerUtil
/*    */ {
/* 25 */   private static final Map<UUID, Long> lastclick = new HashMap<>();
/* 26 */   private static final Map<UUID, Integer> lastcount = new HashMap<>();
/*    */   
/*    */   public static OfflinePlayer getOfflinePlayer(String name) {
/* 29 */     OfflinePlayer[] ops = AuctionHouse.plugin.getServer().getOfflinePlayers();
/* 30 */     for (OfflinePlayer op : ops) {
/* 31 */       String n = op.getName();
/* 32 */       if (n != null && n.equals(name)) {
/* 33 */         return op;
/*    */       }
/*    */     } 
/* 36 */     return null;
/*    */   }
/*    */   
/*    */   public static ItemStack getPlayerHead(Player player) {
/* 40 */     if (player != null) {
/* 41 */       OfflinePlayer op = getOfflinePlayer(player.getName());
/* 42 */       ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
/* 43 */       ItemMeta meta = item.getItemMeta();
/* 44 */       if (meta instanceof SkullMeta) {
/* 45 */         SkullMeta skullMeta = (SkullMeta)meta;
/* 46 */         skullMeta.setOwningPlayer(op);
/* 47 */         item.setItemMeta(meta);
/* 48 */         return item;
/*    */       } 
/*    */     } 
/* 51 */     return new ItemStack(Material.PLAYER_HEAD, 1);
/*    */   }
/*    */   
/*    */   public static Player getPlayer(UUID uuid) {
/* 55 */     for (Player p : Bukkit.getOnlinePlayers()) {
/* 56 */       if (p.getUniqueId() == uuid) return p; 
/*    */     } 
/* 58 */     return null;
/*    */   }
/*    */   
/*    */   public static Player getPlayer(String name) {
/* 62 */     OfflinePlayer op = getOfflinePlayer(name);
/* 63 */     if (op != null) {
/* 64 */       return getPlayer(op.getUniqueId());
/*    */     }
/* 66 */     return null;
/*    */   }
/*    */   
/*    */   public static double getPlayerBalance(Player player, String world) {
/* 70 */     return Economy.getBalance((OfflinePlayer)player, world);
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean spamCheck(Player player) {
/* 75 */     if (Config.spam_check) {
/* 76 */       UUID uuid = player.getUniqueId();
/* 77 */       long now = System.currentTimeMillis();
/* 78 */       long duration = now - ((Long)lastclick.getOrDefault(uuid, Long.valueOf(0L))).longValue();
/* 79 */       int count = ((Integer)lastcount.getOrDefault(uuid, Integer.valueOf(0))).intValue();
/* 80 */       lastclick.put(uuid, Long.valueOf(now));
/* 81 */       if (duration < 1000L) {
/* 82 */         if (count > 5) {
/* 83 */           SoundUtil.failSound(player);
/* 84 */           return true;
/*    */         } 
/* 86 */         lastcount.put(uuid, Integer.valueOf(count + 1));
/*    */       } else {
/*    */         
/* 89 */         lastcount.put(uuid, Integer.valueOf(0));
/*    */       } 
/*    */     } 
/* 92 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhous\\util\PlayerUtil.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */