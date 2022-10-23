/*     */ package com.spawnchunk.auctionhouse.menus;
/*     */ 
/*     */ import com.spawnchunk.auctionhouse.AuctionHouse;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.entity.HumanEntity;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Menu
/*     */ {
/*     */   private final String title;
/*     */   private final int size;
/*     */   private Inventory inventory;
/*  24 */   private final Map<Integer, ItemTag> tags = new HashMap<>();
/*     */   
/*     */   public Menu(String title, int size) {
/*  27 */     this.title = title;
/*  28 */     this.size = size;
/*  29 */     this.inventory = Bukkit.createInventory(null, size, title);
/*     */   }
/*     */   
/*     */   public String getTitle() {
/*  33 */     return this.title;
/*     */   }
/*     */   
/*     */   public int getSize() {
/*  37 */     return this.size;
/*     */   }
/*     */   
/*     */   public List<HumanEntity> getViewers() {
/*  41 */     return this.inventory.getViewers();
/*     */   }
/*     */   
/*     */   public ItemStack getItem(int slot) {
/*  45 */     return this.inventory.getItem(slot);
/*     */   }
/*     */   
/*     */   public ItemStack[] getContents() {
/*  49 */     return this.inventory.getContents();
/*     */   }
/*     */   
/*     */   public ItemStack[] getStorageContents() {
/*  53 */     return this.inventory.getStorageContents();
/*     */   }
/*     */   
/*     */   public void setMaxStackSize(int size) {
/*  57 */     this.inventory.setMaxStackSize(size);
/*     */   }
/*     */   
/*     */   public void setItem(int slot, ItemStack item) {
/*  61 */     this.inventory.setItem(slot, item);
/*     */   }
/*     */   
/*     */   public void setContents(ItemStack[] itemStacks) {
/*  65 */     this.inventory.setContents(itemStacks);
/*     */   }
/*     */   
/*     */   public void setStorageContents(ItemStack[] itemStacks) {
/*  69 */     this.inventory.setStorageContents(itemStacks);
/*     */   }
/*     */   
/*     */   public Inventory getInventory() {
/*  73 */     return this.inventory;
/*     */   }
/*     */   
/*     */   public void setInventory(Inventory inventory) {
/*  77 */     this.inventory = inventory;
/*     */   }
/*     */   
/*     */   public void setItem(int slot, MenuItem menuItem) {
/*  81 */     this.inventory.setItem(slot, menuItem.getItem());
/*     */   }
/*     */   
/*     */   public void setItemAmount(int slot, int amount) {
/*  85 */     ItemStack item = this.inventory.getItem(slot);
/*  86 */     if (item == null || item.getType().equals(Material.AIR))
/*  87 */       return;  item.setAmount(amount);
/*  88 */     this.inventory.setItem(slot, item);
/*     */   }
/*     */   
/*     */   public void setItemName(int slot, String name) {
/*  92 */     ItemStack item = this.inventory.getItem(slot);
/*  93 */     if (item == null || item.getType().equals(Material.AIR))
/*  94 */       return;  ItemMeta meta = item.getItemMeta();
/*  95 */     ((ItemMeta)Objects.<ItemMeta>requireNonNull(meta)).setDisplayName(name);
/*  96 */     item.setItemMeta(meta);
/*  97 */     this.inventory.setItem(slot, item);
/*     */   }
/*     */   
/*     */   public void setItemLore(int slot, List<String> lore) {
/* 101 */     ItemStack item = this.inventory.getItem(slot);
/* 102 */     if (item == null || item.getType().equals(Material.AIR) || lore.isEmpty())
/* 103 */       return;  ItemMeta meta = (item.getItemMeta() != null) ? item.getItemMeta() : Bukkit.getItemFactory().getItemMeta(item.getType());
/* 104 */     if (item.getType() == Material.PLAYER_HEAD) {
/* 105 */       item = AuctionHouse.nms.setLore(item, lore);
/*     */     }
/* 107 */     else if (meta != null) {
/* 108 */       meta.setLore(lore);
/* 109 */       item.setItemMeta(meta);
/*     */     } 
/*     */     
/* 112 */     this.inventory.setItem(slot, item);
/*     */   }
/*     */ 
/*     */   
/*     */   public void replaceItemLore(int slot, String search, String replace) {
/* 117 */     ItemStack item = this.inventory.getItem(slot);
/* 118 */     if (item == null || item.getType().equals(Material.AIR))
/* 119 */       return;  ItemMeta meta = item.getItemMeta();
/* 120 */     if (meta == null)
/* 121 */       return;  List<String> lore = (meta.getLore() != null) ? meta.getLore() : new ArrayList<>();
/* 122 */     if (lore.isEmpty())
/* 123 */       return;  boolean changed = false;
/* 124 */     for (String line : lore) {
/* 125 */       int index = lore.indexOf(line);
/* 126 */       if (line.contains(search)) {
/* 127 */         lore.set(index, replace);
/* 128 */         changed = true;
/*     */       } 
/*     */     } 
/* 131 */     if (changed) {
/* 132 */       if (item.getType() == Material.PLAYER_HEAD) {
/* 133 */         item = AuctionHouse.nms.setLore(item, lore);
/*     */       } else {
/* 135 */         meta.setLore(lore);
/* 136 */         item.setItemMeta(meta);
/*     */       } 
/* 138 */       this.inventory.setItem(slot, item);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setTag(int slot, ItemTag tag) {
/* 143 */     this.tags.put(Integer.valueOf(slot), tag);
/*     */   }
/*     */   
/*     */   public ItemTag getTag(int slot) {
/* 147 */     return this.tags.get(Integer.valueOf(slot));
/*     */   }
/*     */   
/*     */   public void removeItem(int slot) {
/* 151 */     this.tags.remove(Integer.valueOf(slot));
/* 152 */     this.inventory.setItem(slot, new ItemStack(Material.AIR));
/*     */   }
/*     */   
/*     */   public void removeTag(int slot) {
/* 156 */     this.tags.remove(Integer.valueOf(slot));
/*     */   }
/*     */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\menus\Menu.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */