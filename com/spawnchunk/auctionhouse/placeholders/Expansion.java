/*     */ package com.spawnchunk.auctionhouse.placeholders;
/*     */ 
/*     */ import com.spawnchunk.auctionhouse.AuctionHouse;
/*     */ import com.spawnchunk.auctionhouse.modules.Auctions;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import me.clip.placeholderapi.expansion.PlaceholderExpansion;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ public class Expansion
/*     */   extends PlaceholderExpansion {
/*     */   private final AuctionHouse plugin;
/*     */   private final String identifier;
/*     */   
/*     */   public Expansion(AuctionHouse plugin) {
/*  17 */     this.plugin = plugin;
/*  18 */     this.identifier = plugin.getName().toLowerCase();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getIdentifier() {
/*  23 */     return this.identifier;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAuthor() {
/*  28 */     return "klugemonkey";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getVersion() {
/*  33 */     return this.plugin.getDescription().getVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRequiredPlugin() {
/*  38 */     return this.plugin.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> getPlaceholders() {
/*  43 */     return Arrays.asList(new String[] {
/*  44 */           placeholder("active_listings"), 
/*  45 */           placeholder("player_listings"), 
/*  46 */           placeholder("expired_listings"), 
/*  47 */           placeholder("sold_items"), 
/*  48 */           placeholder("max_listings")
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean persist() {
/*  54 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String onRequest(OfflinePlayer op, String params) {
/*  59 */     if (op == null) return null; 
/*  60 */     if (!op.isOnline() || op.getPlayer() == null) return null; 
/*  61 */     Player player = op.getPlayer();
/*     */ 
/*     */     
/*  64 */     if (params.equals("active_listings")) {
/*  65 */       Auctions.updateCounts(player);
/*  66 */       int count = Auctions.getActiveListingsCount();
/*  67 */       return String.format("%d", new Object[] { Integer.valueOf(count) });
/*     */     } 
/*     */     
/*  70 */     if (params.equals("player_listings")) {
/*  71 */       Auctions.updateCounts(player);
/*  72 */       int count = Auctions.getPlayerListingsCount((OfflinePlayer)player);
/*  73 */       return String.format("%d", new Object[] { Integer.valueOf(count) });
/*     */     } 
/*     */     
/*  76 */     if (params.equals("expired_listings")) {
/*  77 */       Auctions.updateCounts(player);
/*  78 */       int count = Auctions.getExpiredListingsCount((OfflinePlayer)player);
/*  79 */       return String.format("%d", new Object[] { Integer.valueOf(count) });
/*     */     } 
/*     */     
/*  82 */     if (params.equals("sold_items")) {
/*  83 */       Auctions.updateCounts(player);
/*  84 */       int count = Auctions.getSoldItemsCount((OfflinePlayer)player);
/*  85 */       return String.format("%d", new Object[] { Integer.valueOf(count) });
/*     */     } 
/*     */     
/*  88 */     if (params.equals("max_listings")) {
/*  89 */       if (op.isOnline()) {
/*  90 */         int count = Auctions.getMaxListings(player);
/*  91 */         return String.format("%d", new Object[] { Integer.valueOf(count) });
/*     */       } 
/*  93 */       return "?";
/*     */     } 
/*     */     
/*  96 */     return null;
/*     */   }
/*     */   
/*     */   private String placeholder(String param) {
/* 100 */     return "%" + this.identifier + "_" + param + "%";
/*     */   }
/*     */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\placeholders\Expansion.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */