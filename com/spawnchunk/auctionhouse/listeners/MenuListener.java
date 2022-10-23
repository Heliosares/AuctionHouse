/*     */ package com.spawnchunk.auctionhouse.listeners;
/*     */ 
/*     */ import com.spawnchunk.auctionhouse.AuctionHouse;
/*     */ import com.spawnchunk.auctionhouse.config.Config;
/*     */ import com.spawnchunk.auctionhouse.events.MenuClickEvent;
/*     */ import com.spawnchunk.auctionhouse.events.MenuCloseEvent;
/*     */ import com.spawnchunk.auctionhouse.menus.Menu;
/*     */ import com.spawnchunk.auctionhouse.menus.MenuClickType;
/*     */ import com.spawnchunk.auctionhouse.util.InventoryUtil;
/*     */ import com.spawnchunk.auctionhouse.util.ItemUtil;
/*     */ import com.spawnchunk.auctionhouse.util.MessageUtil;
/*     */ import com.spawnchunk.auctionhouse.util.SoundUtil;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.entity.HumanEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.Event;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.inventory.ClickType;
/*     */ import org.bukkit.event.inventory.DragType;
/*     */ import org.bukkit.event.inventory.InventoryAction;
/*     */ import org.bukkit.event.inventory.InventoryClickEvent;
/*     */ import org.bukkit.event.inventory.InventoryCloseEvent;
/*     */ import org.bukkit.event.inventory.InventoryDragEvent;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.InventoryView;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ 
/*     */ public class MenuListener
/*     */   implements Listener {
/*     */   private MenuClickType getMenuClickType(ClickType click) {
/*  36 */     if (click.isShiftClick()) {
/*  37 */       if (click.isLeftClick()) return MenuClickType.SHIFT_LEFT; 
/*  38 */       if (click.isRightClick()) return MenuClickType.SHIFT_RIGHT; 
/*  39 */       if (click == ClickType.MIDDLE) return MenuClickType.SHIFT_MIDDLE; 
/*     */     } else {
/*  41 */       if (click.isLeftClick()) return MenuClickType.LEFT; 
/*  42 */       if (click.isRightClick()) return MenuClickType.RIGHT; 
/*  43 */       if (click == ClickType.MIDDLE) return MenuClickType.MIDDLE; 
/*     */     } 
/*  45 */     if (click.isKeyboardClick()) return MenuClickType.KEYBOARD; 
/*  46 */     return MenuClickType.UNKNOWN;
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST)
/*     */   public void onInventoryClose(InventoryCloseEvent event) {
/*  52 */     HumanEntity he = event.getPlayer();
/*  53 */     String title = InventoryUtil.getTitle(he);
/*  54 */     Player player = (Player)he;
/*  55 */     if (!player.isOnline())
/*  56 */       return;  UUID uuid = player.getUniqueId();
/*     */     
/*  58 */     if (AuctionHouse.menuManager.isMenu(uuid, title) && !title.isEmpty()) {
/*  59 */       Menu menu = AuctionHouse.menuManager.getMenu(uuid, title);
/*  60 */       if (menu != null) {
/*  61 */         String id = AuctionHouse.menuManager.getId(uuid, menu);
/*  62 */         if (id != null) {
/*  63 */           MenuCloseEvent menuCloseEvent = new MenuCloseEvent(player, id);
/*  64 */           Bukkit.getPluginManager().callEvent((Event)menuCloseEvent);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST)
/*     */   public void onInventoryClick(InventoryClickEvent event) {
/*  73 */     HumanEntity he = event.getWhoClicked();
/*  74 */     String title = InventoryUtil.getTitle(he);
/*  75 */     Player player = (Player)he;
/*  76 */     if (!player.isOnline())
/*  77 */       return;  UUID uuid = player.getUniqueId();
/*     */     
/*  79 */     if (AuctionHouse.menuManager.isMenu(uuid, title) && !title.isEmpty()) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  84 */       InventoryAction action = event.getAction();
/*  85 */       ClickType click = event.getClick();
/*  86 */       MenuClickType type = getMenuClickType(click);
/*  87 */       int size = event.getInventory().getSize();
/*  88 */       int rawSlot = event.getRawSlot();
/*  89 */       int slot = (rawSlot > size) ? (rawSlot - size) : rawSlot;
/*     */       
/*  91 */       if (Config.debug) {
/*  92 */         AuctionHouse.logger.info(String.format("RawSlot = %d", new Object[] { Integer.valueOf(rawSlot) }));
/*  93 */         AuctionHouse.logger.info(String.format("ClickType = %s", new Object[] { click.name() }));
/*  94 */         AuctionHouse.logger.info(String.format("InventoryAction = %s", new Object[] { action.name() }));
/*     */       } 
/*     */       
/*  97 */       if (rawSlot == -999) {
/*     */         
/*  99 */         event.setCancelled(true);
/*     */         
/*     */         return;
/*     */       } 
/* 103 */       if (rawSlot < size) {
/*     */ 
/*     */ 
/*     */         
/* 107 */         if (action.toString().startsWith("PICKUP")) {
/* 108 */           ItemStack item = event.getCurrentItem();
/* 109 */           if (item != null && item.getType() != Material.AIR) {
/* 110 */             Menu menu = AuctionHouse.menuManager.getMenu(uuid, title);
/* 111 */             if (menu != null) {
/* 112 */               String id = AuctionHouse.menuManager.getId(uuid, menu);
/* 113 */               if (id != null) {
/* 114 */                 MenuClickEvent menuClickEvent = new MenuClickEvent(player, id, slot, type);
/* 115 */                 Bukkit.getPluginManager().callEvent((Event)menuClickEvent);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 122 */         if (rawSlot < size - 9 && (type == MenuClickType.SHIFT_LEFT || type == MenuClickType.SHIFT_RIGHT)) {
/* 123 */           ItemStack item = event.getCurrentItem();
/* 124 */           if (item != null && item.getType() != Material.AIR) {
/* 125 */             if (player.hasPermission("auctionhouse.cancel.others") || player.isOp()) {
/* 126 */               Menu menu = AuctionHouse.menuManager.getMenu(uuid, title);
/* 127 */               if (menu != null) {
/* 128 */                 String id = AuctionHouse.menuManager.getId(uuid, menu);
/* 129 */                 if (id != null) {
/* 130 */                   MenuClickEvent menuClickEvent = new MenuClickEvent(player, id, slot, type);
/* 131 */                   Bukkit.getPluginManager().callEvent((Event)menuClickEvent);
/*     */                 } 
/*     */               } 
/*     */             } else {
/* 135 */               SoundUtil.failSound(player);
/*     */             } 
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 141 */         if (rawSlot < size - 9 && type == MenuClickType.MIDDLE) {
/* 142 */           if (action != InventoryAction.CLONE_STACK && player.hasPermission("auctionhouse.pick")) {
/*     */             
/* 144 */             ItemStack item = event.getCurrentItem();
/* 145 */             if (item != null && item.getType() != Material.AIR) {
/* 146 */               ItemStack cursor = player.getItemOnCursor();
/* 147 */               if (cursor.getType() == Material.AIR) {
/* 148 */                 player.setItemOnCursor(item.clone());
/*     */               }
/*     */             } 
/*     */           } else {
/* 152 */             SoundUtil.failSound(player);
/*     */           } 
/*     */         }
/*     */         
/* 156 */         event.setCancelled(true);
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 162 */         if (Config.strict) {
/* 163 */           ItemStack itemStack = event.getCurrentItem();
/* 164 */           if (itemStack != null) {
/* 165 */             Material material = itemStack.getType();
/* 166 */             if (material != Material.AIR && AuctionHouse.nms.hasNBTLocator(itemStack)) {
/*     */               try {
/* 168 */                 event.setCurrentItem(null);
/* 169 */                 int amount = itemStack.getAmount();
/* 170 */                 String item = ItemUtil.getName(itemStack);
/* 171 */                 String nbt = AuctionHouse.nms.getNBTString(itemStack);
/* 172 */                 MessageUtil.logMessage("message.menu_item.removed", Config.locale, new Object[] { Integer.valueOf(amount), item, nbt, player.getName() });
/* 173 */                 event.setCancelled(false);
/*     */                 return;
/* 175 */               } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {}
/*     */             }
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 182 */         String actionName = action.name();
/* 183 */         if (actionName.startsWith("PICKUP") || actionName.startsWith("PLACE"))
/* 184 */           return;  if (actionName.equalsIgnoreCase("CLONE_STACK") && player.hasPermission("auctionhouse.pick"))
/* 185 */           return;  if (Config.debug) AuctionHouse.logger.info("cancelling action"); 
/*     */       } 
/* 187 */       event.setCancelled(true);
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 192 */     else if (Config.strict) {
/* 193 */       int size = event.getInventory().getSize();
/* 194 */       int rawSlot = event.getRawSlot();
/* 195 */       int slot = (rawSlot > size) ? (rawSlot - size) : rawSlot;
/* 196 */       ItemStack itemStack = event.getCurrentItem();
/* 197 */       if (itemStack != null) {
/* 198 */         Material material = itemStack.getType();
/* 199 */         if (material != Material.AIR && AuctionHouse.nms.hasNBTLocator(itemStack)) {
/*     */           try {
/* 201 */             event.setCurrentItem(null);
/* 202 */             int amount = itemStack.getAmount();
/* 203 */             String item = ItemUtil.getName(itemStack);
/* 204 */             String nbt = AuctionHouse.nms.getNBTString(itemStack);
/* 205 */             MessageUtil.logMessage("message.menu_item.removed", Config.locale, new Object[] { Integer.valueOf(amount), item, nbt, player.getName() });
/* 206 */             event.setCancelled(false);
/* 207 */           } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {}
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST)
/*     */   public void onInventoryDrag(InventoryDragEvent event) {
/* 218 */     boolean cancelled = event.isCancelled();
/*     */     
/* 220 */     DragType type = event.getType();
/* 221 */     HumanEntity he = event.getWhoClicked();
/* 222 */     InventoryView view = he.getOpenInventory();
/* 223 */     Player player = (Player)he;
/* 224 */     if (!player.isOnline())
/* 225 */       return;  UUID uuid = player.getUniqueId();
/* 226 */     String name = InventoryUtil.getTitle(he);
/*     */     
/* 228 */     if (AuctionHouse.menuManager.isMenu(uuid, name) && !name.isEmpty()) {
/*     */ 
/*     */       
/* 231 */       Set<Integer> rawSlots = event.getRawSlots();
/* 232 */       int size = event.getInventory().getSize();
/* 233 */       for (Integer rawSlot : rawSlots) {
/* 234 */         if (rawSlot.intValue() < size) {
/*     */           
/* 236 */           event.setCancelled(true);
/*     */           
/*     */           break;
/*     */         } 
/* 240 */         int slot = rawSlot.intValue() - size;
/* 241 */         if (Config.strict) {
/*     */           
/*     */           try {
/* 244 */             Inventory bottomInventory = view.getBottomInventory();
/* 245 */             ItemStack itemStack = bottomInventory.getItem(slot);
/* 246 */             if (itemStack != null) {
/* 247 */               Material material = itemStack.getType();
/* 248 */               if (material != Material.AIR && AuctionHouse.nms.hasNBTLocator(itemStack)) {
/* 249 */                 event.setCursor(null);
/* 250 */                 event.getInventory().clear(rawSlot.intValue());
/* 251 */                 int amount = itemStack.getAmount();
/* 252 */                 String item = ItemUtil.getName(itemStack);
/* 253 */                 String nbt = AuctionHouse.nms.getNBTString(itemStack);
/* 254 */                 MessageUtil.logMessage("message.menu_item.removed", Config.locale, new Object[] { Integer.valueOf(amount), item, nbt, player.getName() });
/* 255 */                 event.setCancelled(false);
/*     */               } 
/*     */             } 
/* 258 */           } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {}
/*     */         
/*     */         }
/*     */       }
/*     */     
/*     */     }
/* 264 */     else if (Config.strict) {
/*     */       
/* 266 */       Set<Integer> rawSlots = event.getRawSlots();
/* 267 */       int size = event.getInventory().getSize();
/* 268 */       boolean cancel = false;
/* 269 */       for (Integer rawSlot : rawSlots) {
/* 270 */         int slot = (rawSlot.intValue() > size) ? (rawSlot.intValue() - size) : rawSlot.intValue();
/*     */         try {
/* 272 */           Inventory topInventory = view.getTopInventory();
/* 273 */           Inventory bottomInventory = view.getBottomInventory();
/* 274 */           ItemStack itemStack = (rawSlot.intValue() < size) ? topInventory.getItem(slot) : bottomInventory.getItem(slot);
/* 275 */           if (itemStack != null) {
/* 276 */             Material material = itemStack.getType();
/* 277 */             if (material != Material.AIR && AuctionHouse.nms.hasNBTLocator(itemStack)) {
/* 278 */               event.setCursor(null);
/* 279 */               event.getInventory().clear(rawSlot.intValue());
/* 280 */               int amount = itemStack.getAmount();
/* 281 */               String item = ItemUtil.getName(itemStack);
/* 282 */               String nbt = AuctionHouse.nms.getNBTString(itemStack);
/* 283 */               MessageUtil.logMessage("message.menu_item.removed", Config.locale, new Object[] { Integer.valueOf(amount), item, nbt, player.getName() });
/* 284 */               event.setCancelled(false);
/*     */             } 
/*     */           } 
/* 287 */         } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {}
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\listeners\MenuListener.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */