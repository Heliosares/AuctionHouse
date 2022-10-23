/*     */ package com.spawnchunk.auctionhouse.menus;
/*     */ 
/*     */ import com.spawnchunk.auctionhouse.AuctionHouse;
/*     */ import com.spawnchunk.auctionhouse.config.Config;
/*     */ import com.spawnchunk.auctionhouse.events.MenuClickEvent;
/*     */ import com.spawnchunk.auctionhouse.events.MenuCloseEvent;
/*     */ import com.spawnchunk.auctionhouse.modules.Auctions;
/*     */ import com.spawnchunk.auctionhouse.modules.ListingType;
/*     */ import com.spawnchunk.auctionhouse.storage.LocaleStorage;
/*     */ import com.spawnchunk.auctionhouse.util.ItemUtil;
/*     */ import com.spawnchunk.auctionhouse.util.MessageUtil;
/*     */ import com.spawnchunk.auctionhouse.util.SoundUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ 
/*     */ public class ConfirmListingMenu
/*     */   implements Listener {
/*     */   private final String id;
/*     */   private ItemStack item;
/*     */   private final float price;
/*     */   private final double listing_fee;
/*     */   private final ListingType type;
/*  36 */   private final Map<Integer, ItemTag> tags = new HashMap<>();
/*     */   
/*     */   private boolean cancelled = false;
/*  39 */   final String menu_confirm = MessageUtil.sectionSymbol(LocaleStorage.translate("message.menu.confirm", Config.locale));
/*  40 */   final String menu_cancel = MessageUtil.sectionSymbol(LocaleStorage.translate("message.menu.cancel", Config.locale));
/*     */   
/*  42 */   final String confirm_listing_title = MessageUtil.sectionSymbol(LocaleStorage.translate("message.confirm_listing.title", Config.locale));
/*  43 */   final String top_rule = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.horizontal.rule.top", Config.locale));
/*  44 */   final String bottom_rule = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.horizontal.rule.top", Config.locale));
/*  45 */   final String fee_value = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.fee.value", Config.locale));
/*     */   
/*     */   public ConfirmListingMenu(Player player, float price, double listing_fee, ListingType type) {
/*  48 */     UUID uuid = player.getUniqueId();
/*  49 */     this.price = price;
/*  50 */     this.listing_fee = listing_fee;
/*  51 */     this.type = type;
/*  52 */     String title = MessageUtil.populate(this.confirm_listing_title, new Object[] { Double.valueOf(listing_fee) });
/*  53 */     if (title.isEmpty()) {
/*  54 */       title = String.format("Pay $%s Listing Fee?", new Object[] { Double.valueOf(listing_fee) });
/*     */     }
/*  56 */     this.id = AuctionHouse.menuManager.createMenu(uuid, title, 9);
/*  57 */     Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin)AuctionHouse.plugin);
/*     */   }
/*     */ 
/*     */   
/*     */   private void initialize() {
/*  62 */     Menu menu = AuctionHouse.menuManager.getMenu(this.id);
/*  63 */     if (menu == null)
/*     */       return; 
/*  65 */     this.tags.put(Integer.valueOf(0), ItemTag.CONFIRM);
/*  66 */     this.tags.put(Integer.valueOf(1), ItemTag.CONFIRM);
/*  67 */     this.tags.put(Integer.valueOf(2), ItemTag.CONFIRM);
/*  68 */     this.tags.put(Integer.valueOf(3), ItemTag.CONFIRM);
/*  69 */     this.tags.put(Integer.valueOf(5), ItemTag.CANCEL);
/*  70 */     this.tags.put(Integer.valueOf(6), ItemTag.CANCEL);
/*  71 */     this.tags.put(Integer.valueOf(7), ItemTag.CANCEL);
/*  72 */     this.tags.put(Integer.valueOf(8), ItemTag.CANCEL);
/*     */     
/*  74 */     MenuItem confirm = new MenuItem(this.menu_confirm, Config.confirm_button, 1, null);
/*  75 */     MenuItem cancel = new MenuItem(this.menu_cancel, Config.cancel_button, 1, null);
/*     */     
/*  77 */     menu.setItem(0, confirm);
/*  78 */     menu.setItem(1, confirm);
/*  79 */     menu.setItem(2, confirm);
/*  80 */     menu.setItem(3, confirm);
/*  81 */     menu.setItem(5, cancel);
/*  82 */     menu.setItem(6, cancel);
/*  83 */     menu.setItem(7, cancel);
/*  84 */     menu.setItem(8, cancel);
/*     */   }
/*     */   
/*     */   public void show(Player player, ItemStack item) {
/*  88 */     UUID uuid = player.getUniqueId();
/*  89 */     initialize();
/*  90 */     this.item = item;
/*  91 */     confirmListing(item);
/*  92 */     AuctionHouse.menuManager.openMenu(uuid, this.id);
/*     */   }
/*     */   
/*     */   public void close(Player player) {
/*  96 */     UUID uuid = player.getUniqueId();
/*  97 */     AuctionHouse.menuManager.closeMenu(uuid, this.id);
/*  98 */     AuctionHouse.menuManager.removeMenu(this.id);
/*     */   }
/*     */   
/*     */   private void confirmListing(ItemStack is) {
/* 102 */     Menu menu = AuctionHouse.menuManager.getMenu(this.id);
/* 103 */     if (menu == null)
/* 104 */       return;  if (is != null) {
/* 105 */       String name = ItemUtil.getCustomName(is);
/* 106 */       String key = is.getType().getKey().toString();
/* 107 */       int amount = is.getAmount();
/* 108 */       ItemMeta meta = is.getItemMeta();
/* 109 */       List<String> oldLore = (meta != null) ? meta.getLore() : new ArrayList<>();
/* 110 */       String nbt = AuctionHouse.nms.getNBTString(is);
/* 111 */       List<String> desc = Arrays.asList(new String[] { this.top_rule, 
/*     */             
/* 113 */             MessageUtil.populate(this.fee_value, new Object[] { Double.valueOf(this.listing_fee) }), this.bottom_rule });
/*     */       
/* 115 */       List<String> lore = new ArrayList<>();
/* 116 */       if (oldLore != null) lore.addAll(oldLore); 
/* 117 */       for (String s : MessageUtil.expand(desc)) {
/* 118 */         if (!s.isEmpty()) lore.add(s); 
/*     */       } 
/* 120 */       MenuItem menuItem = new MenuItem(name, key, amount, lore, nbt);
/* 121 */       menu.setItem(4, menuItem);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST)
/*     */   public void onMenuClose(MenuCloseEvent event) {
/* 128 */     Player player = event.getPlayer();
/* 129 */     String id = event.getId();
/* 130 */     if (player == null || id == null)
/* 131 */       return;  Menu menu = AuctionHouse.menuManager.getMenu(id);
/* 132 */     if (menu != null && 
/* 133 */       id.equals(this.id)) {
/*     */       
/* 135 */       if (!player.isOnline()) this.cancelled = true; 
/* 136 */       Auctions.completeListing(player, this.item, this.price, this.listing_fee, this.type, this.cancelled);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST)
/*     */   public void onMenuClick(MenuClickEvent event) {
/* 144 */     Player player = event.getPlayer();
/* 145 */     String id = event.getId();
/* 146 */     if (player == null || id == null)
/* 147 */       return;  Menu menu = AuctionHouse.menuManager.getMenu(id);
/* 148 */     if (menu != null) {
/* 149 */       int slot = event.getSlot();
/* 150 */       ItemStack item = menu.getItem(slot);
/* 151 */       if (item == null)
/* 152 */         return;  if (item.getType().equals(Material.AIR))
/* 153 */         return;  MenuClickType click = event.getMenuClickType();
/* 154 */       if (id.equals(this.id))
/*     */       {
/* 156 */         if (click == MenuClickType.LEFT) {
/* 157 */           ItemTag tag = this.tags.get(Integer.valueOf(slot));
/* 158 */           if (tag != null)
/* 159 */             if (tag.equals(ItemTag.CONFIRM)) {
/* 160 */               SoundUtil.clickSound(player);
/* 161 */               this.cancelled = false;
/* 162 */               close(player);
/* 163 */             } else if (tag.equals(ItemTag.CANCEL)) {
/* 164 */               SoundUtil.clickSound(player);
/* 165 */               this.cancelled = true;
/* 166 */               close(player);
/*     */             }  
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\menus\ConfirmListingMenu.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */