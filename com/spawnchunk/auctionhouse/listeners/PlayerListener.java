/*     */ package com.spawnchunk.auctionhouse.listeners;
/*     */ 
/*     */ import com.spawnchunk.auctionhouse.AuctionHouse;
/*     */ import com.spawnchunk.auctionhouse.config.Config;
/*     */ import com.spawnchunk.auctionhouse.events.DropUnclaimedEvent;
/*     */ import com.spawnchunk.auctionhouse.modules.Auctions;
/*     */ import com.spawnchunk.auctionhouse.util.MessageUtil;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import org.bukkit.Nameable;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.BlockState;
/*     */ import org.bukkit.block.Sign;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.block.Action;
/*     */ import org.bukkit.event.player.PlayerChangedWorldEvent;
/*     */ import org.bukkit.event.player.PlayerInteractEntityEvent;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
/*     */ import org.bukkit.event.player.PlayerJoinEvent;
/*     */ import org.bukkit.event.player.PlayerQuitEvent;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlayerListener
/*     */   implements Listener
/*     */ {
/*     */   @EventHandler(priority = EventPriority.NORMAL)
/*     */   public void onPlayerJoin(PlayerJoinEvent event) {
/*  36 */     if (!Config.per_world_listings) {
/*  37 */       Player player = event.getPlayer();
/*  38 */       Auctions.returnUnclaimedItems(player);
/*  39 */       Auctions.checkExpiredItems(player);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler(priority = EventPriority.NORMAL)
/*     */   public void onPlayerQuit(PlayerQuitEvent event) {
/*  46 */     Player player = event.getPlayer();
/*  47 */     UUID uuid = player.getUniqueId();
/*  48 */     AuctionHouse.playerCooldowns.remove(uuid);
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler(priority = EventPriority.NORMAL)
/*     */   public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
/*  54 */     if (Config.per_world_listings && Config.unclaimed_check_on_world_change) {
/*  55 */       Player player = event.getPlayer();
/*  56 */       Auctions.returnUnclaimedItems(player);
/*  57 */       Auctions.checkExpiredItems(player);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler(priority = EventPriority.NORMAL)
/*     */   public void onDropUnclaimed(DropUnclaimedEvent event) {
/*  64 */     List<Player> players = Auctions.checkUnclaimedItems();
/*  65 */     for (Player player : players) {
/*  66 */       Auctions.returnUnclaimedItems(player);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler(priority = EventPriority.NORMAL)
/*     */   public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
/*  73 */     if (event.isCancelled())
/*  74 */       return;  Player player = event.getPlayer();
/*  75 */     Entity entity = event.getRightClicked();
/*  76 */     String customName = entity.getCustomName();
/*  77 */     if (customName != null && 
/*  78 */       Config.entity_name_triggers != null && !Config.entity_name_triggers.isEmpty() && 
/*  79 */       Config.entity_name_triggers.contains(customName) && (player.hasPermission("auctionhouse.trigger.entity") || player.isOp())) {
/*  80 */       Auctions.filter = null;
/*  81 */       Auctions.menu_mode = false;
/*  82 */       Auctions.openActiveListingsMenu(player);
/*  83 */       event.setCancelled(true);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @EventHandler(priority = EventPriority.NORMAL)
/*     */   public void onPlayerInteract(PlayerInteractEvent event) {
/*  92 */     if (event.isCancelled())
/*  93 */       return;  Player player = event.getPlayer();
/*  94 */     Block block = event.getClickedBlock();
/*  95 */     ItemStack item = event.getItem();
/*  96 */     if (block != null) {
/*  97 */       Action action = event.getAction();
/*  98 */       if (action == Action.RIGHT_CLICK_BLOCK) {
/*  99 */         BlockState bs = block.getState();
/* 100 */         if (Config.sign_trigger != null && !Config.sign_trigger.isEmpty() && (
/* 101 */           item == null || !AuctionHouse.nms.isDye(item)) && 
/* 102 */           bs instanceof Sign && (player.hasPermission("auctionhouse.trigger.sign") || player.isOp())) {
/* 103 */           Sign sign = (Sign)bs;
/* 104 */           String[] lines = sign.getLines();
/* 105 */           for (String line : lines) {
/* 106 */             if (MessageUtil.nocolor(line).contains(MessageUtil.nocolor(Config.sign_trigger))) {
/* 107 */               Auctions.filter = null;
/* 108 */               Auctions.menu_mode = false;
/* 109 */               Auctions.openActiveListingsMenu(player);
/* 110 */               event.setCancelled(true);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 116 */         if (Config.block_name_triggers != null && !Config.block_name_triggers.isEmpty() && 
/* 117 */           bs instanceof Nameable) {
/* 118 */           Nameable nameable = (Nameable)bs;
/* 119 */           String name = nameable.getCustomName();
/* 120 */           if (name != null && Config.block_name_triggers.contains(name) && (player.hasPermission("auctionhouse.trigger.block") || player.isOp())) {
/* 121 */             Auctions.filter = null;
/* 122 */             Auctions.menu_mode = false;
/* 123 */             Auctions.openActiveListingsMenu(player);
/* 124 */             event.setCancelled(true);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\listeners\PlayerListener.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */