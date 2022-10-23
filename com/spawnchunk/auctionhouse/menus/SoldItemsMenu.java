/*     */ package com.spawnchunk.auctionhouse.menus;
/*     */ 
/*     */ import com.spawnchunk.auctionhouse.AuctionHouse;
/*     */ import com.spawnchunk.auctionhouse.config.Config;
/*     */ import com.spawnchunk.auctionhouse.events.MenuClickEvent;
/*     */ import com.spawnchunk.auctionhouse.events.ServerTickEvent;
/*     */ import com.spawnchunk.auctionhouse.modules.Auctions;
/*     */ import com.spawnchunk.auctionhouse.modules.Listing;
/*     */ import com.spawnchunk.auctionhouse.modules.Listings;
/*     */ import com.spawnchunk.auctionhouse.modules.SortOrder;
/*     */ import com.spawnchunk.auctionhouse.storage.LocaleStorage;
/*     */ import com.spawnchunk.auctionhouse.util.ItemUtil;
/*     */ import com.spawnchunk.auctionhouse.util.MessageUtil;
/*     */ import com.spawnchunk.auctionhouse.util.PlayerUtil;
/*     */ import com.spawnchunk.auctionhouse.util.SoundUtil;
/*     */ import com.spawnchunk.auctionhouse.util.TimeUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.entity.HumanEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.scheduler.BukkitRunnable;
/*     */ 
/*     */ public class SoldItemsMenu
/*     */   implements Listener
/*     */ {
/*     */   private final String id;
/*  41 */   private SortOrder sortOrder = Config.auction_sort_order;
/*     */   private int soldItemsCount;
/*     */   private int current_page;
/*  44 */   private List<Long> page_keys = new ArrayList<>();
/*  45 */   private final Map<Integer, ItemTag> tags = new HashMap<>();
/*     */   
/*  47 */   final String menu_back = MessageUtil.sectionSymbol(LocaleStorage.translate("message.menu.back", Config.locale));
/*  48 */   final String menu_previous = LocaleStorage.translate("message.menu.page.previous", Config.locale);
/*  49 */   final String menu_next = LocaleStorage.translate("message.menu.page.next", Config.locale);
/*  50 */   final String menu_sort_listings = MessageUtil.sectionSymbol(LocaleStorage.translate("message.menu.sort_listings", Config.locale));
/*  51 */   final String order_value = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.order.value", Config.locale));
/*  52 */   final String menu_clear = MessageUtil.sectionSymbol(LocaleStorage.translate("message.sold_items.clear.button.title", Config.locale));
/*  53 */   final String clear_desc1 = MessageUtil.sectionSymbol(LocaleStorage.translate("message.sold_items.clear.button.desc1", Config.locale));
/*  54 */   final String clear_desc2 = MessageUtil.sectionSymbol(LocaleStorage.translate("message.sold_items.clear.button.desc2", Config.locale));
/*  55 */   final String menu_info = MessageUtil.sectionSymbol(LocaleStorage.translate("message.menu.info", Config.locale));
/*  56 */   final String info_desc1 = MessageUtil.sectionSymbol(LocaleStorage.translate("message.sold_items.info.desc1", Config.locale));
/*  57 */   final String info_desc2 = MessageUtil.sectionSymbol(LocaleStorage.translate("message.sold_items.info.desc2", Config.locale));
/*  58 */   final String info_desc3 = MessageUtil.sectionSymbol(LocaleStorage.translate("message.sold_items.info.desc3", Config.locale));
/*  59 */   final String info_desc4 = MessageUtil.sectionSymbol(LocaleStorage.translate("message.sold_items.info.desc4", Config.locale));
/*  60 */   final String info_desc5 = MessageUtil.sectionSymbol(LocaleStorage.translate("message.sold_items.info.desc5", Config.locale));
/*     */   
/*  62 */   final String sold_items_title = MessageUtil.sectionSymbol(LocaleStorage.translate("message.sold_items.title", Config.locale));
/*  63 */   final String purchased = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.purchased.value", Config.locale));
/*  64 */   final String key = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.purchased.key", Config.locale));
/*  65 */   final String top_rule = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.horizontal.rule.top", Config.locale));
/*     */   
/*  67 */   final String price_value = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.price.value", Config.locale));
/*  68 */   final String buyer_value = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.buyer.value", Config.locale));
/*  69 */   final String purchased_value = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.purchased.value", Config.locale));
/*     */   
/*  71 */   final String bottom_rule = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.horizontal.rule.top", Config.locale));
/*     */   
/*     */   public SoldItemsMenu(Player player) {
/*  74 */     UUID uuid = player.getUniqueId();
/*  75 */     String title = this.sold_items_title;
/*  76 */     if (title.isEmpty()) {
/*  77 */       title = "Recently Sold Items";
/*     */     }
/*  79 */     this.id = AuctionHouse.menuManager.createMenu(uuid, title, 54);
/*  80 */     Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin)AuctionHouse.plugin);
/*     */   }
/*     */ 
/*     */   
/*     */   public void initialize() {
/*  85 */     Menu menu = AuctionHouse.menuManager.getMenu(this.id);
/*  86 */     if (menu == null)
/*     */       return; 
/*  88 */     this.sortOrder = Config.auction_sort_order;
/*     */     
/*  90 */     for (int slot = 0; slot < 45; ) { this.tags.put(Integer.valueOf(slot), ItemTag.ITEM); slot++; }
/*  91 */      this.tags.put(Integer.valueOf(45), ItemTag.BACK);
/*  92 */     this.tags.put(Integer.valueOf(48), ItemTag.PREVIOUS);
/*  93 */     this.tags.put(Integer.valueOf(49), ItemTag.SORT_LISTINGS);
/*  94 */     this.tags.put(Integer.valueOf(50), ItemTag.NEXT);
/*  95 */     this.tags.put(Integer.valueOf(52), ItemTag.CLEAR);
/*     */     
/*  97 */     MenuItem back = new MenuItem(this.menu_back, Config.back_button, 1, null);
/*  98 */     String order = LocaleStorage.translate(this.sortOrder.key(), Config.locale);
/*     */     
/* 100 */     MenuItem sort_listings = new MenuItem(this.menu_sort_listings, Config.sort_listings_button, 1, MessageUtil.expand(Collections.singletonList(MessageUtil.populate(this.order_value, new Object[] { order }))));
/*     */     
/* 102 */     MenuItem clear = new MenuItem(this.menu_clear, Config.clear_button, 1, MessageUtil.expand(Arrays.asList(new String[] { this.clear_desc1, this.clear_desc2 })));
/*     */ 
/*     */ 
/*     */     
/* 106 */     MenuItem info = new MenuItem(this.menu_info, Config.info_button, 1, MessageUtil.expand(Arrays.asList(new String[] { this.info_desc1, this.info_desc2, this.info_desc3, this.info_desc4, this.info_desc5 })));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 113 */     menu.setItem(45, back);
/* 114 */     menu.setItem(49, sort_listings);
/* 115 */     menu.setItem(52, clear);
/* 116 */     menu.setItem(53, info);
/*     */   }
/*     */   
/*     */   public void show(final Player player, final int page) {
/* 120 */     initialize();
/* 121 */     (new BukkitRunnable()
/*     */       {
/*     */         public void run() {
/* 124 */           SoldItemsMenu.this.paginate_task(player, page);
/*     */         }
/* 126 */       }).runTaskLater((Plugin)AuctionHouse.plugin, 1L);
/*     */   }
/*     */   
/*     */   public void paginate_task(final Player player, int page) {
/* 130 */     this.current_page = page;
/* 131 */     paginate(player);
/* 132 */     (new BukkitRunnable()
/*     */       {
/*     */         public void run() {
/* 135 */           SoldItemsMenu.this.build_task(player);
/*     */         }
/* 137 */       }).runTaskLater((Plugin)AuctionHouse.plugin, 1L);
/*     */   }
/*     */   
/*     */   public void build_task(final Player player) {
/* 141 */     build(player, this.current_page);
/*     */     
/* 143 */     (new BukkitRunnable()
/*     */       {
/*     */         public void run() {
/* 146 */           SoldItemsMenu.this.open_task(player);
/*     */         }
/* 148 */       }).runTaskLater((Plugin)AuctionHouse.plugin, 1L);
/*     */   }
/*     */   
/*     */   public void open_task(Player player) {
/* 152 */     UUID uuid = player.getUniqueId();
/* 153 */     AuctionHouse.menuManager.openMenu(uuid, this.id);
/*     */   }
/*     */   
/*     */   public void close(Player player) {
/* 157 */     UUID uuid = player.getUniqueId();
/* 158 */     AuctionHouse.menuManager.closeMenu(uuid, this.id);
/*     */   }
/*     */   
/*     */   private int paginate(Player player) {
/* 162 */     Listings soldItems = AuctionHouse.listings.getSoldItems(player, this.sortOrder);
/* 163 */     Map<Long, Listing> map = soldItems.getListings();
/* 164 */     this.page_keys = new ArrayList<>(map.keySet());
/* 165 */     int keys = this.page_keys.size();
/* 166 */     return Math.floorDiv(Math.max(keys - 1, 0), 45);
/*     */   }
/*     */   
/*     */   private void build(Player player, int page) {
/* 170 */     Menu menu = AuctionHouse.menuManager.getMenu(this.id);
/* 171 */     if (menu == null)
/* 172 */       return;  int size = this.page_keys.size();
/* 173 */     long now = TimeUtil.now();
/* 174 */     int pages = Math.floorDiv(Math.max(size - 1, 0), 45);
/* 175 */     MenuItem blank = new MenuItem(null, "minecraft:air", 1, null);
/* 176 */     MenuItem previous = new MenuItem(this.menu_previous, Config.previous_button, 1, null);
/* 177 */     MenuItem next = new MenuItem(this.menu_next, Config.next_button, 1, null);
/* 178 */     Listings soldItems = AuctionHouse.listings.getSoldItems(player, this.sortOrder);
/* 179 */     int offset = page * 45;
/* 180 */     for (int i = 0; i < 45; i++) {
/* 181 */       int index = offset + i;
/* 182 */       if (index < size) {
/* 183 */         long timestamp = ((Long)this.page_keys.get(index)).longValue();
/* 184 */         Listing listing = soldItems.getListing(timestamp);
/* 185 */         ItemStack is = listing.getItem();
/* 186 */         if (is != null) {
/* 187 */           String name = ItemUtil.getCustomName(is);
/* 188 */           String key = is.getType().getKey().toString();
/* 189 */           int amount = is.getAmount();
/* 190 */           ItemMeta meta = is.getItemMeta();
/* 191 */           List<String> oldLore = (meta != null) ? meta.getLore() : new ArrayList<>();
/* 192 */           String nbt = AuctionHouse.nms.getNBTString(is);
/* 193 */           float price = listing.getPrice();
/* 194 */           String buyer = listing.getBuyerName();
/* 195 */           long elapsed = now - timestamp;
/* 196 */           List<String> desc = Arrays.asList(new String[] { this.top_rule, 
/*     */                 
/* 198 */                 MessageUtil.populate(this.price_value, new Object[] { Float.valueOf(price)
/* 199 */                   }), MessageUtil.populate(this.buyer_value, new Object[] { buyer
/* 200 */                   }), MessageUtil.populate(this.purchased_value, new Object[] { TimeUtil.duration(elapsed, Config.show_seconds) }), this.bottom_rule });
/*     */ 
/*     */           
/* 203 */           List<String> lore = new ArrayList<>();
/* 204 */           if (oldLore != null) lore.addAll(oldLore); 
/* 205 */           for (String s : MessageUtil.expand(desc)) {
/* 206 */             if (!s.isEmpty()) lore.add(s); 
/*     */           } 
/* 208 */           MenuItem menuItem = new MenuItem(name, key, amount, lore, nbt);
/* 209 */           menu.setItem(i, menuItem);
/*     */         } 
/*     */       } else {
/* 212 */         menu.setItem(i, blank);
/*     */       } 
/*     */     } 
/* 215 */     menu.setItem(48, (this.current_page > 0) ? previous : blank);
/* 216 */     menu.setItem(50, (this.current_page < pages) ? next : blank);
/* 217 */     OfflinePlayer op = AuctionHouse.plugin.getServer().getOfflinePlayer(player.getUniqueId());
/*     */     
/* 219 */     Auctions.updateCounts(player);
/* 220 */     int sold = Auctions.getSoldItemsCount(op);
/* 221 */     updateClearButton(menu, sold);
/*     */   }
/*     */   
/*     */   private void updateClearButton(Menu menu, int sold) {
/* 225 */     if (sold > 0) {
/*     */       
/* 227 */       List<String> lore = MessageUtil.expand(Arrays.asList(new String[] { this.clear_desc1, this.clear_desc2 }));
/*     */ 
/*     */       
/* 230 */       MenuItem clear = new MenuItem(this.menu_clear, Config.clear_button, 1, lore);
/* 231 */       menu.setItem(52, clear);
/*     */     } else {
/* 233 */       menu.setItem(52, new ItemStack(Material.AIR));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void update(Player player) {
/* 238 */     Menu menu = AuctionHouse.menuManager.getMenu(this.id);
/* 239 */     if (menu == null)
/*     */       return; 
/* 241 */     Auctions.updateCounts(player);
/* 242 */     OfflinePlayer op = AuctionHouse.plugin.getServer().getOfflinePlayer(player.getUniqueId());
/* 243 */     int sold = Auctions.getSoldItemsCount(op);
/*     */     
/* 245 */     if (this.soldItemsCount != sold) {
/* 246 */       this.soldItemsCount = sold;
/*     */       
/* 248 */       int pages = paginate(player);
/* 249 */       if (this.current_page > pages) this.current_page = pages; 
/* 250 */       build(player, this.current_page);
/*     */     } 
/*     */     
/* 253 */     int size = this.page_keys.size();
/* 254 */     long now = TimeUtil.now();
/* 255 */     int offset = this.current_page * 45;
/* 256 */     for (int i = 0; i < 54; i++) {
/* 257 */       ItemTag tag = this.tags.get(Integer.valueOf(i));
/* 258 */       if (tag != null && 
/* 259 */         tag.equals(ItemTag.ITEM)) {
/* 260 */         int index = offset + i;
/* 261 */         if (index < size) {
/* 262 */           long timestamp = ((Long)this.page_keys.get(index)).longValue();
/* 263 */           long elapsed = now - timestamp;
/* 264 */           menu.replaceItemLore(i, this.key, MessageUtil.populate(this.purchased, new Object[] { TimeUtil.duration(elapsed, Config.show_seconds) }));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST)
/*     */   public void onMenuClick(MenuClickEvent event) {
/* 274 */     Player player = event.getPlayer();
/* 275 */     String id = event.getId();
/* 276 */     if (player == null || id == null)
/* 277 */       return;  if (id.equals(this.id)) {
/* 278 */       int slot = event.getSlot();
/* 279 */       Menu menu = AuctionHouse.menuManager.getMenu(id);
/* 280 */       if (menu == null)
/* 281 */         return;  ItemStack item = menu.getItem(slot);
/* 282 */       if (item == null)
/* 283 */         return;  if (item.getType().equals(Material.AIR))
/* 284 */         return;  MenuClickType click = event.getMenuClickType();
/*     */       
/* 286 */       if (PlayerUtil.spamCheck(player))
/*     */         return; 
/* 288 */       if (click == MenuClickType.LEFT) {
/* 289 */         ItemTag tag = this.tags.get(Integer.valueOf(slot));
/* 290 */         if (tag != null) {
/* 291 */           if (tag.equals(ItemTag.BACK)) {
/* 292 */             SoundUtil.clickSound(player);
/*     */             
/* 294 */             Auctions.openPlayerListingsMenu(player);
/*     */             return;
/*     */           } 
/* 297 */           if (tag.equals(ItemTag.PREVIOUS)) {
/* 298 */             SoundUtil.clickSound(player);
/* 299 */             paginate(player);
/* 300 */             if (this.current_page > 0) this.current_page--; 
/* 301 */             build(player, this.current_page);
/*     */             return;
/*     */           } 
/* 304 */           if (tag.equals(ItemTag.SORT_LISTINGS)) {
/* 305 */             SoundUtil.clickSound(player);
/* 306 */             this.sortOrder = this.sortOrder.next();
/* 307 */             String order = LocaleStorage.translate(this.sortOrder.key(), Config.locale);
/* 308 */             List<String> lore = Collections.singletonList(MessageUtil.populate(this.order_value, new Object[] { order }));
/* 309 */             menu.setItemLore(49, MessageUtil.expand(lore));
/* 310 */             paginate(player);
/* 311 */             build(player, this.current_page);
/*     */             return;
/*     */           } 
/* 314 */           if (tag.equals(ItemTag.NEXT)) {
/* 315 */             SoundUtil.clickSound(player);
/*     */ 
/*     */ 
/*     */             
/* 319 */             int pages = paginate(player);
/* 320 */             if (this.current_page < pages) this.current_page++; 
/* 321 */             build(player, this.current_page);
/*     */             return;
/*     */           } 
/* 324 */           if (tag.equals(ItemTag.CLEAR)) {
/* 325 */             SoundUtil.clickSound(player);
/* 326 */             Auctions.clearSoldItems(player);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST)
/*     */   public void onServerTick(ServerTickEvent event) {
/* 336 */     Menu menu = AuctionHouse.menuManager.getMenu(this.id);
/* 337 */     if (menu == null)
/* 338 */       return;  List<HumanEntity> viewers = menu.getViewers();
/* 339 */     for (HumanEntity entity : viewers) {
/* 340 */       if (entity instanceof Player) {
/* 341 */         Player player = (Player)entity;
/* 342 */         update(player);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\menus\SoldItemsMenu.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */