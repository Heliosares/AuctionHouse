/*     */ package com.spawnchunk.auctionhouse.menus;
/*     */ 
/*     */ import com.spawnchunk.auctionhouse.util.PlayerUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Base64;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.InventoryView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MenuManager
/*     */ {
/*  19 */   private final Map<String, Menu> menus = new HashMap<>();
/*     */   
/*  21 */   private final Map<UUID, Menu> activeMenus = new HashMap<>();
/*     */ 
/*     */   
/*     */   public String createMenu(UUID uuid, String title, int size) {
/*  25 */     String key = uuid.toString() + uuid.toString();
/*  26 */     String id = Base64.getEncoder().encodeToString(key.getBytes());
/*  27 */     updateMenu(id, title, size);
/*  28 */     return id;
/*     */   }
/*     */   
/*     */   public void updateMenu(String id, String title, int size) {
/*  32 */     if (this.menus.containsKey(id)) {
/*  33 */       this.menus.replace(id, new Menu(title, size));
/*     */     } else {
/*  35 */       this.menus.put(id, new Menu(title, size));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Menu openMenu(UUID uuid, String id) {
/*  42 */     if (this.menus.containsKey(id)) {
/*  43 */       Menu menu = this.menus.get(id);
/*     */       
/*  45 */       Player player = PlayerUtil.getPlayer(uuid);
/*  46 */       if (player != null) {
/*  47 */         InventoryView view = player.openInventory(menu.getInventory());
/*     */         
/*  49 */         if (view != null) this.activeMenus.put(uuid, menu); 
/*  50 */         return menu;
/*     */       } 
/*     */     } 
/*  53 */     return null;
/*     */   }
/*     */   
/*     */   public void closeMenu(UUID uuid, String id) {
/*  57 */     if (this.menus.containsKey(id)) {
/*  58 */       Menu menu = this.menus.get(id);
/*  59 */       Player player = PlayerUtil.getPlayer(uuid);
/*  60 */       if (player != null) {
/*  61 */         if (menu.getViewers().contains(player)) player.closeInventory(); 
/*  62 */         this.activeMenus.remove(uuid);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void removeMenu(String id) {
/*  68 */     this.menus.remove(id);
/*     */   }
/*     */   
/*     */   public String getId(UUID uuid) {
/*  72 */     Menu menu = getActiveMenu(uuid);
/*  73 */     if (menu != null) {
/*  74 */       return getId(uuid, menu);
/*     */     }
/*  76 */     return null;
/*     */   }
/*     */   
/*     */   public String getId(UUID uuid, Menu menu) {
/*  80 */     String title = menu.getTitle();
/*  81 */     String key = uuid.toString() + uuid.toString();
/*  82 */     String id = Base64.getEncoder().encodeToString(key.getBytes());
/*  83 */     if (this.menus.containsKey(id)) return id; 
/*  84 */     return null;
/*     */   }
/*     */   
/*     */   public Map<String, Menu> getMenus() {
/*  88 */     return this.menus;
/*     */   }
/*     */   
/*     */   public Menu getActiveMenu(UUID uuid) {
/*  92 */     return this.activeMenus.get(uuid);
/*     */   }
/*     */   
/*     */   public Menu getMenu(String id) {
/*  96 */     return this.menus.get(id);
/*     */   }
/*     */   
/*     */   public Menu getMenu(UUID uuid, String title) {
/* 100 */     String key = uuid.toString() + uuid.toString();
/* 101 */     String id = Base64.getEncoder().encodeToString(key.getBytes());
/* 102 */     return this.menus.get(id);
/*     */   }
/*     */   
/*     */   public boolean isMenu(UUID uuid, String title) {
/* 106 */     String key = uuid.toString() + uuid.toString();
/* 107 */     String id = Base64.getEncoder().encodeToString(key.getBytes());
/* 108 */     return this.menus.containsKey(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeAllMenus() {
/* 113 */     List<UUID> toRemove = new ArrayList<>();
/* 114 */     for (UUID uuid : this.activeMenus.keySet()) {
/* 115 */       String id = getId(uuid);
/* 116 */       if (id != null) toRemove.add(uuid); 
/*     */     } 
/* 118 */     for (UUID uuid : toRemove) {
/* 119 */       String id = getId(uuid);
/* 120 */       closeMenu(uuid, id);
/* 121 */       removeMenu(id);
/*     */     } 
/*     */     
/* 124 */     this.activeMenus.clear();
/* 125 */     this.menus.clear();
/*     */   }
/*     */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\menus\MenuManager.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */