/*     */ package com.spawnchunk.auctionhouse.menus;
/*     */ 
/*     */ import com.spawnchunk.auctionhouse.AuctionHouse;
/*     */ import com.spawnchunk.auctionhouse.config.Config;
/*     */ import com.spawnchunk.auctionhouse.events.MenuClickEvent;
/*     */ import com.spawnchunk.auctionhouse.modules.Auctions;
/*     */ import com.spawnchunk.auctionhouse.modules.Listing;
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
/*     */ public class PurchaseItemMenu
/*     */   implements Listener {
/*     */   private final String id;
/*     */   private long timestamp;
/*     */   private int return_page;
/*  34 */   private final Map<Integer, ItemTag> tags = new HashMap<>();
/*     */   
/*  36 */   final String menu_confirm = MessageUtil.sectionSymbol(LocaleStorage.translate("message.menu.confirm", Config.locale));
/*  37 */   final String menu_cancel = MessageUtil.sectionSymbol(LocaleStorage.translate("message.menu.cancel", Config.locale));
/*     */   
/*  39 */   final String purchase_item_title = MessageUtil.sectionSymbol(LocaleStorage.translate("message.purchase_item.title", Config.locale));
/*  40 */   final String top_rule = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.horizontal.rule.top", Config.locale));
/*  41 */   final String price_value = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.price.value", Config.locale));
/*  42 */   final String seller_value = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.seller.value", Config.locale));
/*  43 */   final String bottom_rule = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.horizontal.rule.top", Config.locale));
/*     */   
/*     */   public PurchaseItemMenu(Player player) {
/*  46 */     UUID uuid = player.getUniqueId();
/*  47 */     String title = this.purchase_item_title;
/*  48 */     if (title.isEmpty()) {
/*  49 */       title = "Purchase Item: Are you sure?";
/*     */     }
/*  51 */     this.id = AuctionHouse.menuManager.createMenu(uuid, title, 9);
/*  52 */     Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin)AuctionHouse.plugin);
/*     */   }
/*     */ 
/*     */   
/*     */   public void initialize() {
/*  57 */     Menu menu = AuctionHouse.menuManager.getMenu(this.id);
/*  58 */     if (menu == null)
/*     */       return; 
/*  60 */     this.tags.put(Integer.valueOf(0), ItemTag.CONFIRM);
/*  61 */     this.tags.put(Integer.valueOf(1), ItemTag.CONFIRM);
/*  62 */     this.tags.put(Integer.valueOf(2), ItemTag.CONFIRM);
/*  63 */     this.tags.put(Integer.valueOf(3), ItemTag.CONFIRM);
/*  64 */     this.tags.put(Integer.valueOf(5), ItemTag.CANCEL);
/*  65 */     this.tags.put(Integer.valueOf(6), ItemTag.CANCEL);
/*  66 */     this.tags.put(Integer.valueOf(7), ItemTag.CANCEL);
/*  67 */     this.tags.put(Integer.valueOf(8), ItemTag.CANCEL);
/*     */     
/*  69 */     MenuItem confirm = new MenuItem(this.menu_confirm, Config.confirm_button, 1, null);
/*  70 */     MenuItem cancel = new MenuItem(this.menu_cancel, Config.cancel_button, 1, null);
/*     */     
/*  72 */     menu.setItem(0, confirm);
/*  73 */     menu.setItem(1, confirm);
/*  74 */     menu.setItem(2, confirm);
/*  75 */     menu.setItem(3, confirm);
/*  76 */     menu.setItem(5, cancel);
/*  77 */     menu.setItem(6, cancel);
/*  78 */     menu.setItem(7, cancel);
/*  79 */     menu.setItem(8, cancel);
/*     */   }
/*     */   
/*     */   public void show(Player player, long key, int page) {
/*  83 */     UUID uuid = player.getUniqueId();
/*  84 */     initialize();
/*  85 */     this.return_page = page;
/*  86 */     this.timestamp = key;
/*  87 */     confirmPurchase(this.timestamp);
/*  88 */     AuctionHouse.menuManager.openMenu(uuid, this.id);
/*     */   }
/*     */   
/*     */   public void close(Player player) {
/*  92 */     UUID uuid = player.getUniqueId();
/*  93 */     AuctionHouse.menuManager.closeMenu(uuid, this.id);
/*     */   }
/*     */   
/*     */   private void confirmPurchase(long timestamp) {
/*  97 */     Menu menu = AuctionHouse.menuManager.getMenu(this.id);
/*  98 */     if (menu == null)
/*  99 */       return;  Listing listing = AuctionHouse.listings.getListing(timestamp);
/* 100 */     ItemStack is = listing.getItem();
/* 101 */     if (is != null) {
/* 102 */       String name = ItemUtil.getCustomName(is);
/* 103 */       String key = is.getType().getKey().toString();
/* 104 */       int amount = is.getAmount();
/* 105 */       ItemMeta meta = is.getItemMeta();
/* 106 */       List<String> oldLore = (meta != null) ? meta.getLore() : new ArrayList<>();
/* 107 */       String nbt = AuctionHouse.nms.getNBTString(is);
/* 108 */       float price = listing.getPrice();
/* 109 */       ListingType type = listing.getType();
/* 110 */       String seller = type.isServer() ? AuctionHouse.servername : ((listing.getSeller() != null) ? listing.getSeller().getName() : listing.getSeller_UUID());
/* 111 */       List<String> desc = Arrays.asList(new String[] { this.top_rule, 
/*     */             
/* 113 */             MessageUtil.populate(this.price_value, new Object[] { Float.valueOf(price)
/* 114 */               }), MessageUtil.populate(this.seller_value, new Object[] { seller }), this.bottom_rule });
/*     */       
/* 116 */       List<String> lore = new ArrayList<>();
/* 117 */       if (oldLore != null) lore.addAll(oldLore); 
/* 118 */       for (String s : MessageUtil.expand(desc)) {
/* 119 */         if (!s.isEmpty()) lore.add(s); 
/*     */       } 
/* 121 */       MenuItem menuItem = new MenuItem(name, key, amount, lore, nbt);
/* 122 */       menu.setItem(4, menuItem);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST)
/*     */   public void onMenuClick(MenuClickEvent event) {
/* 129 */     Player player = event.getPlayer();
/* 130 */     String id = event.getId();
/* 131 */     if (player == null || id == null)
/* 132 */       return;  Menu menu = AuctionHouse.menuManager.getMenu(id);
/* 133 */     if (menu == null)
/* 134 */       return;  int slot = event.getSlot();
/* 135 */     ItemStack item = menu.getItem(slot);
/* 136 */     if (item == null)
/* 137 */       return;  if (item.getType().equals(Material.AIR))
/* 138 */       return;  MenuClickType click = event.getMenuClickType();
/* 139 */     if (id.equals(this.id))
/*     */     {
/* 141 */       if (click == MenuClickType.LEFT) {
/* 142 */         ItemTag tag = this.tags.get(Integer.valueOf(slot));
/* 143 */         if (tag != null)
/* 144 */           if (tag.equals(ItemTag.CONFIRM)) {
/* 145 */             SoundUtil.clickSound(player);
/* 146 */             if (!Auctions.purchaseItem(player, Long.valueOf(this.timestamp))) {
/* 147 */               SoundUtil.failSound(player);
/*     */             }
/*     */             
/* 150 */             Auctions.openActiveListingsMenu(player, this.return_page);
/* 151 */           } else if (tag.equals(ItemTag.CANCEL)) {
/* 152 */             SoundUtil.clickSound(player);
/*     */             
/* 154 */             Auctions.openActiveListingsMenu(player, this.return_page);
/*     */           }  
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\menus\PurchaseItemMenu.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */