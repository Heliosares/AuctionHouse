/*    */ package com.spawnchunk.auctionhouse.util;
/*    */ 
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.entity.HumanEntity;
/*    */ import org.bukkit.inventory.InventoryView;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.bukkit.inventory.PlayerInventory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InventoryUtil
/*    */ {
/*    */   public static int availableSlots(PlayerInventory inventory) {
/* 17 */     int count = 0;
/* 18 */     for (int i = 0; i < 45; i++) {
/* 19 */       ItemStack item = inventory.getItem(i);
/* 20 */       if (item == null || item.getType().equals(Material.AIR)) {
/* 21 */         count++;
/*    */       }
/*    */     } 
/* 24 */     return count;
/*    */   }
/*    */   
/*    */   public static int getFirstAvailableSlot(PlayerInventory inventory) {
/* 28 */     for (int i = 0; i < 45; i++) {
/* 29 */       ItemStack item = inventory.getItem(i);
/* 30 */       if (item == null || item.getType().equals(Material.AIR)) {
/* 31 */         return i;
/*    */       }
/*    */     } 
/* 34 */     return -1;
/*    */   }
/*    */   
/*    */   public static String getTitle(HumanEntity he) {
/* 38 */     InventoryView view = he.getOpenInventory();
/*    */     try {
/* 40 */       return view.getTitle();
/* 41 */     } catch (IllegalStateException illegalStateException) {
/*    */       
/* 43 */       return "";
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhous\\util\InventoryUtil.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */