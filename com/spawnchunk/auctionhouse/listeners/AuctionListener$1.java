/*     */ package com.spawnchunk.auctionhouse.listeners;
/*     */ 
/*     */ import com.spawnchunk.auctionhouse.AuctionHouse;
/*     */ import com.spawnchunk.auctionhouse.config.Config;
/*     */ import com.spawnchunk.auctionhouse.events.ItemAction;
/*     */ import com.spawnchunk.auctionhouse.storage.LocaleStorage;
/*     */ import com.spawnchunk.auctionhouse.util.ItemUtil;
/*     */ import com.spawnchunk.auctionhouse.util.MessageUtil;
/*     */ import java.io.File;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.scheduler.BukkitRunnable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class null
/*     */   extends BukkitRunnable
/*     */ {
/*     */   public void run() {
/*  64 */     if (Config.log_listed || Config.log_sold || Config.log_cancelled || Config.log_returned || Config.log_dropped || Config.log_purged) {
/*  65 */       DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
/*  66 */       DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
/*  67 */       Date date = new Date();
/*  68 */       String path = AuctionHouse.plugin.getDataFolder().getPath() + AuctionHouse.plugin.getDataFolder().getPath() + "logs";
/*  69 */       String file = String.format("%s.log", new Object[] { dateFormat.format(date) });
/*  70 */       String filename = path + path + File.separator;
/*  71 */       String time = String.format("[%s]", new Object[] { timeFormat.format(date) });
/*  72 */       File dir = new File(path);
/*  73 */       if (!dir.exists())
/*     */       {
/*  75 */         dir.mkdirs();
/*     */       }
/*  77 */       int count = item.getAmount();
/*  78 */       String name = ItemUtil.getName(item);
/*  79 */       switch (AuctionListener.null.$SwitchMap$com$spawnchunk$auctionhouse$events$ItemAction[action.ordinal()]) {
/*     */         
/*     */         case 1:
/*  82 */           if (Config.log_listed) {
/*  83 */             String enchants = AuctionListener.this.getEnchants(item);
/*  84 */             AuctionListener.this.logMessage(filename, String.format("%s [ITEM_LISTED]: %s", new Object[] { time, MessageUtil.populate(LocaleStorage.translate("message.log.item_listed", Config.locale), new Object[] { this.val$seller, Integer.valueOf(count), name, enchants, Float.valueOf(this.val$price), this.val$world }) }));
/*     */           } 
/*     */           return;
/*     */         
/*     */         case 2:
/*  89 */           if (Config.log_sold) {
/*  90 */             String enchants = AuctionListener.this.getEnchants(item);
/*  91 */             AuctionListener.this.logMessage(filename, String.format("%s [ITEM_SOLD]: %s", new Object[] { time, MessageUtil.populate(LocaleStorage.translate("message.log.item_sold", Config.locale), new Object[] { this.val$seller, Integer.valueOf(count), name, enchants, this.val$buyer, Float.valueOf(this.val$price), this.val$world }) }));
/*     */           } 
/*     */           return;
/*     */         
/*     */         case 3:
/*  96 */           if (Config.log_cancelled) {
/*  97 */             String enchants = AuctionListener.this.getEnchants(item);
/*  98 */             AuctionListener.this.logMessage(filename, String.format("%s [ITEM_CANCELLED]: %s", new Object[] { time, MessageUtil.populate(LocaleStorage.translate("message.log.item_cancelled", Config.locale), new Object[] { this.val$seller, Integer.valueOf(count), name, enchants, Float.valueOf(this.val$price), this.val$world }) }));
/*     */           } 
/*     */           return;
/*     */         
/*     */         case 4:
/* 103 */           if (Config.log_returned) {
/* 104 */             String enchants = AuctionListener.this.getEnchants(item);
/* 105 */             AuctionListener.this.logMessage(filename, String.format("%s [ITEM_RETURNED]: %s", new Object[] { time, MessageUtil.populate(LocaleStorage.translate("message.log.item_returned", Config.locale), new Object[] { Integer.valueOf(count), name, enchants, this.val$seller, this.val$world }) }));
/*     */           } 
/*     */           return;
/*     */         
/*     */         case 5:
/* 110 */           if (Config.log_purged) {
/* 111 */             String enchants = AuctionListener.this.getEnchants(item);
/* 112 */             AuctionListener.this.logMessage(filename, String.format("%s [ITEM_PURGED]: %s", new Object[] { time, MessageUtil.populate(LocaleStorage.translate("message.log.item_purged", Config.locale), new Object[] { this.val$seller, Integer.valueOf(count), name, enchants, Float.valueOf(this.val$price), this.val$world }) }));
/*     */           } 
/*     */           return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\listeners\AuctionListener$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */