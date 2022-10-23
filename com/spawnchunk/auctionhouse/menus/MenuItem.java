/*     */ package com.spawnchunk.auctionhouse.menus;
/*     */ 
/*     */ import com.spawnchunk.auctionhouse.AuctionHouse;
/*     */ import com.spawnchunk.auctionhouse.util.ItemUtil;
/*     */ import com.spawnchunk.auctionhouse.util.MessageUtil;
/*     */ import com.spawnchunk.auctionhouse.util.PlayerUtil;
/*     */ import java.util.List;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class MenuItem
/*     */ {
/*     */   private final ItemStack item;
/*     */   
/*     */   public MenuItem(String name, String key, int amount, List<String> lore) {
/*  26 */     this(null, name, key, amount, lore, null);
/*     */   }
/*     */   
/*     */   public MenuItem(String name, String key, int amount, List<String> lore, String nbt) {
/*  30 */     this(null, name, key, amount, lore, nbt);
/*     */   }
/*     */   
/*     */   public MenuItem(Player player, String name, String key, int amount, List<String> lore) {
/*  34 */     this(player, name, key, amount, lore, null);
/*     */   }
/*     */ 
/*     */   
/*     */   private MenuItem(Player player, String name, String key, int amount, List<String> lore, String nbt) {
/*  39 */     if (key.equals("auctionhouse:player_head")) {
/*     */       
/*  41 */       if (player != null) {
/*  42 */         item = PlayerUtil.getPlayerHead(player);
/*  43 */         item.setAmount(amount);
/*     */       } else {
/*  45 */         item = new ItemStack(Material.PLAYER_HEAD, amount);
/*     */       } 
/*  47 */     } else if (key.startsWith("texture:")) {
/*     */       
/*  49 */       String texture = key.replace("texture:", "");
/*  50 */       item = AuctionHouse.nms.getCustomSkull(texture);
/*  51 */       item.setAmount(amount);
/*  52 */     } else if (key.startsWith("hdb:")) {
/*     */       
/*  54 */       if (AuctionHouse.hdb != null) {
/*  55 */         String id = key.replace("hdb:", "");
/*  56 */         if (AuctionHouse.hdb.isHead(id)) {
/*  57 */           item = AuctionHouse.hdb.getItemHead(id);
/*  58 */           item.setAmount(amount);
/*     */         } else {
/*  60 */           item = new ItemStack(Material.AIR, 1);
/*     */         } 
/*     */       } else {
/*  63 */         item = new ItemStack(Material.AIR, 1);
/*     */       } 
/*     */     } else {
/*  66 */       Material material = Material.matchMaterial(key);
/*  67 */       if (material == null) material = Material.AIR; 
/*  68 */       item = new ItemStack(material, amount);
/*     */     } 
/*     */     
/*  71 */     if (nbt != null && !nbt.isEmpty()) {
/*  72 */       item = AuctionHouse.nms.setNBTString(item, nbt);
/*     */     }
/*  74 */     ItemMeta meta = item.hasItemMeta() ? item.getItemMeta() : Bukkit.getItemFactory().getItemMeta(item.getType());
/*  75 */     if (meta != null) {
/*  76 */       if (name != null && !name.isEmpty())
/*     */       {
/*     */         
/*  79 */         item = AuctionHouse.nms.setDisplayName(item, name);
/*     */       }
/*  81 */       if (lore != null && !lore.isEmpty()) {
/*  82 */         for (String s : lore) {
/*  83 */           lore.set(lore.indexOf(s), MessageUtil.sectionSymbol(s));
/*     */         }
/*  85 */         if (item.getType() == Material.PLAYER_HEAD) {
/*  86 */           item = AuctionHouse.nms.setLore(item, lore);
/*     */         } else {
/*     */           
/*  89 */           item = AuctionHouse.nms.setLore(item, lore);
/*     */         } 
/*     */       } 
/*     */       
/*  93 */       if (key.endsWith("shulker_box") || key.endsWith("chest")) {
/*  94 */         item = ItemUtil.hidePotionEffects(item);
/*     */       }
/*     */     } 
/*     */     
/*  98 */     ItemStack item = AuctionHouse.nms.addNBTLocator(item);
/*  99 */     this.item = item;
/*     */   }
/*     */   
/*     */   public ItemStack getItem() {
/* 103 */     return this.item;
/*     */   }
/*     */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\menus\MenuItem.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */