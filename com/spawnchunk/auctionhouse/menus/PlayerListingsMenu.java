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
/*     */ public class PlayerListingsMenu
/*     */   implements Listener
/*     */ {
/*     */   private final String id;
/*  41 */   private SortOrder sortOrder = Config.auction_sort_order;
/*     */   private int playerListingsCount;
/*     */   private int expiredListingsCount;
/*     */   private int soldItemsCount;
/*     */   private int current_page;
/*  46 */   private List<Long> page_keys = new ArrayList<>();
/*  47 */   private final Map<Integer, ItemTag> tags = new HashMap<>();
/*     */   
/*  49 */   final String menu_back = MessageUtil.sectionSymbol(LocaleStorage.translate("message.menu.back", Config.locale));
/*  50 */   final String menu_previous = MessageUtil.sectionSymbol(LocaleStorage.translate("message.menu.page.previous", Config.locale));
/*  51 */   final String menu_next = MessageUtil.sectionSymbol(LocaleStorage.translate("message.menu.page.next", Config.locale));
/*  52 */   final String menu_sort_listings = MessageUtil.sectionSymbol(LocaleStorage.translate("message.menu.sort_listings", Config.locale));
/*  53 */   final String order_value = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.order.value", Config.locale));
/*  54 */   final String menu_expired_listings = MessageUtil.sectionSymbol(LocaleStorage.translate("message.expired_listings.button.title", Config.locale));
/*  55 */   final String expired_listings_desc1 = MessageUtil.sectionSymbol(LocaleStorage.translate("message.expired_listings.button.desc1", Config.locale));
/*  56 */   final String expired_listings_desc2 = MessageUtil.sectionSymbol(LocaleStorage.translate("message.expired_listings.button.desc2", Config.locale));
/*  57 */   final String returnable_value = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.returnable.value", Config.locale));
/*  58 */   final String returnable_none = MessageUtil.sectionSymbol(LocaleStorage.translate("warning.returnable.none", Config.locale));
/*  59 */   final String menu_sold_items = MessageUtil.sectionSymbol(LocaleStorage.translate("message.sold_items.button.title", Config.locale));
/*  60 */   final String sold_items_desc1 = MessageUtil.sectionSymbol(LocaleStorage.translate("message.sold_items.button.desc1", Config.locale));
/*  61 */   final String sold_items_desc2 = MessageUtil.sectionSymbol(LocaleStorage.translate("message.sold_items.button.desc2", Config.locale));
/*  62 */   final String sold_items_value = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.sold_items.value", Config.locale));
/*  63 */   final String sold_items_none = MessageUtil.sectionSymbol(LocaleStorage.translate("warning.sold_items.none", Config.locale));
/*  64 */   final String menu_info = MessageUtil.sectionSymbol(LocaleStorage.translate("message.menu.info", Config.locale));
/*  65 */   final String info_desc1 = MessageUtil.sectionSymbol(LocaleStorage.translate("message.player_listings.info.desc1", Config.locale));
/*  66 */   final String info_desc2 = MessageUtil.sectionSymbol(LocaleStorage.translate("message.player_listings.info.desc2", Config.locale));
/*  67 */   final String info_desc3 = MessageUtil.sectionSymbol(LocaleStorage.translate("message.player_listings.info.desc3", Config.locale));
/*  68 */   final String info_desc4 = MessageUtil.sectionSymbol(LocaleStorage.translate("message.player_listings.info.desc4", Config.locale));
/*  69 */   final String info_desc5 = MessageUtil.sectionSymbol(LocaleStorage.translate("message.player_listings.info.desc5", Config.locale));
/*     */   
/*  71 */   final String player_listings_title = MessageUtil.sectionSymbol(LocaleStorage.translate("message.player_listings.title", Config.locale));
/*  72 */   final String top_rule = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.horizontal.rule.top", Config.locale));
/*  73 */   final String click = MessageUtil.sectionSymbol(LocaleStorage.translate("message.player_listings.click", Config.locale));
/*  74 */   final String top_spacing = MessageUtil.sectionSymbol(LocaleStorage.translate("message.player_listings.spacing.top", Config.locale));
/*  75 */   final String price_value = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.price.value", Config.locale));
/*  76 */   final String expire_value = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.expire.value", Config.locale));
/*  77 */   final String item_expired = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.item_expired", Config.locale));
/*  78 */   final String key = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.expire.key", Config.locale));
/*  79 */   final String bottom_spacing = MessageUtil.sectionSymbol(LocaleStorage.translate("message.player_listings.spacing.bottom", Config.locale));
/*  80 */   final String bottom_rule = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.horizontal.rule.top", Config.locale));
/*     */   
/*     */   public PlayerListingsMenu(Player player) {
/*  83 */     UUID uuid = player.getUniqueId();
/*  84 */     String title = this.player_listings_title;
/*  85 */     if (title.isEmpty()) {
/*  86 */       title = "Your Current Listings";
/*     */     }
/*  88 */     this.id = AuctionHouse.menuManager.createMenu(uuid, title, 54);
/*  89 */     Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin)AuctionHouse.plugin);
/*     */   }
/*     */ 
/*     */   
/*     */   public void initialize() {
/*  94 */     Menu menu = AuctionHouse.menuManager.getMenu(this.id);
/*  95 */     if (menu == null)
/*     */       return; 
/*  97 */     this.sortOrder = Config.auction_sort_order;
/*     */     
/*  99 */     for (int slot = 0; slot < 45; ) { this.tags.put(Integer.valueOf(slot), ItemTag.ITEM); slot++; }
/* 100 */      this.tags.put(Integer.valueOf(45), ItemTag.BACK);
/* 101 */     this.tags.put(Integer.valueOf(46), ItemTag.EXPIRED_LISTINGS);
/* 102 */     this.tags.put(Integer.valueOf(48), ItemTag.PREVIOUS);
/* 103 */     this.tags.put(Integer.valueOf(49), ItemTag.SORT_LISTINGS);
/* 104 */     this.tags.put(Integer.valueOf(50), ItemTag.NEXT);
/* 105 */     this.tags.put(Integer.valueOf(52), ItemTag.SOLD_ITEMS);
/*     */     
/* 107 */     MenuItem back = new MenuItem(this.menu_back, Config.back_button, 1, null);
/* 108 */     String order = LocaleStorage.translate(this.sortOrder.key(), Config.locale);
/*     */     
/* 110 */     MenuItem sort_listings = new MenuItem(this.menu_sort_listings, Config.sort_listings_button, 1, MessageUtil.expand(Collections.singletonList(MessageUtil.populate(this.order_value, new Object[] { order }))));
/*     */     
/* 112 */     MenuItem expired_listings = new MenuItem(this.menu_expired_listings, Config.expired_listings_button, 1, MessageUtil.expand(Arrays.asList(new String[] {
/*     */ 
/*     */               
/* 115 */               this.expired_listings_desc1, this.expired_listings_desc2, MessageUtil.populate(this.returnable_value, new Object[] { Integer.valueOf(0) })
/*     */             })));
/* 117 */     MenuItem sold_items = new MenuItem(this.menu_sold_items, Config.sold_items_button, 1, MessageUtil.expand(Arrays.asList(new String[] {
/*     */ 
/*     */               
/* 120 */               this.sold_items_desc1, this.sold_items_desc2, MessageUtil.populate(this.sold_items_value, new Object[] { Integer.valueOf(0) })
/*     */             })));
/* 122 */     MenuItem info = new MenuItem(this.menu_info, Config.info_button, 1, MessageUtil.expand(Arrays.asList(new String[] { this.info_desc1, this.info_desc2, this.info_desc3, this.info_desc4, this.info_desc5 })));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 129 */     menu.setItem(45, back);
/* 130 */     menu.setItem(46, expired_listings);
/* 131 */     menu.setItem(49, sort_listings);
/* 132 */     menu.setItem(52, sold_items);
/* 133 */     menu.setItem(53, info);
/*     */   }
/*     */   
/*     */   public void show(final Player player, final int page) {
/* 137 */     initialize();
/* 138 */     (new BukkitRunnable()
/*     */       {
/*     */         public void run() {
/* 141 */           PlayerListingsMenu.this.paginate_task(player, page);
/*     */         }
/* 143 */       }).runTaskLater((Plugin)AuctionHouse.plugin, 1L);
/*     */   }
/*     */   
/*     */   public void paginate_task(final Player player, int page) {
/* 147 */     this.current_page = page;
/* 148 */     paginate(player);
/* 149 */     (new BukkitRunnable()
/*     */       {
/*     */         public void run() {
/* 152 */           PlayerListingsMenu.this.build_task(player);
/*     */         }
/* 154 */       }).runTaskLater((Plugin)AuctionHouse.plugin, 1L);
/*     */   }
/*     */   
/*     */   public void build_task(final Player player) {
/* 158 */     build(player, this.current_page);
/*     */     
/* 160 */     (new BukkitRunnable()
/*     */       {
/*     */         public void run() {
/* 163 */           PlayerListingsMenu.this.open_task(player);
/*     */         }
/* 165 */       }).runTaskLater((Plugin)AuctionHouse.plugin, 1L);
/*     */   }
/*     */   
/*     */   public void open_task(Player player) {
/* 169 */     UUID uuid = player.getUniqueId();
/* 170 */     AuctionHouse.menuManager.openMenu(uuid, this.id);
/*     */   }
/*     */   
/*     */   public void close(Player player) {
/* 174 */     UUID uuid = player.getUniqueId();
/* 175 */     AuctionHouse.menuManager.closeMenu(uuid, this.id);
/*     */   }
/*     */   
/*     */   private int paginate(Player player) {
/* 179 */     Listings playerListings = AuctionHouse.listings.getPlayerListings(player, this.sortOrder);
/* 180 */     this.playerListingsCount = playerListings.count();
/* 181 */     Map<Long, Listing> map = playerListings.getListings();
/* 182 */     this.page_keys = new ArrayList<>(map.keySet());
/* 183 */     int keys = this.page_keys.size();
/* 184 */     return Math.floorDiv(Math.max(keys - 1, 0), 45);
/*     */   }
/*     */   
/*     */   private void build(Player player, int page) {
/* 188 */     Menu menu = AuctionHouse.menuManager.getMenu(this.id);
/* 189 */     if (menu == null)
/* 190 */       return;  int size = this.page_keys.size();
/* 191 */     long now = TimeUtil.now();
/* 192 */     int pages = Math.floorDiv(Math.max(size - 1, 0), 45);
/* 193 */     MenuItem blank = new MenuItem(null, "minecraft:air", 1, null);
/* 194 */     MenuItem previous = new MenuItem(this.menu_previous, Config.previous_button, 1, null);
/* 195 */     MenuItem next = new MenuItem(this.menu_next, Config.next_button, 1, null);
/* 196 */     Listings playerListings = AuctionHouse.listings.getPlayerListings(player, this.sortOrder);
/* 197 */     int offset = page * 45;
/* 198 */     for (int i = 0; i < 45; i++) {
/* 199 */       int index = offset + i;
/* 200 */       if (index < size) {
/* 201 */         long timestamp = ((Long)this.page_keys.get(index)).longValue();
/* 202 */         long remaining = timestamp - now;
/* 203 */         Listing listing = playerListings.getListing(timestamp);
/* 204 */         if (listing != null) {
/* 205 */           ItemStack is = listing.getItem();
/* 206 */           if (is != null) {
/* 207 */             String name = ItemUtil.getCustomName(is);
/* 208 */             String key = is.getType().getKey().toString();
/* 209 */             int amount = is.getAmount();
/* 210 */             ItemMeta meta = is.getItemMeta();
/* 211 */             List<String> oldLore = (meta != null) ? meta.getLore() : new ArrayList<>();
/* 212 */             String nbt = AuctionHouse.nms.getNBTString(is);
/* 213 */             float price = listing.getPrice();
/* 214 */             List<String> desc = Arrays.asList(new String[] { this.top_rule, this.click, this.top_spacing, 
/*     */ 
/*     */ 
/*     */                   
/* 218 */                   MessageUtil.populate(this.price_value, new Object[] { Float.valueOf(price)
/* 219 */                     }), (remaining > 0L) ? MessageUtil.populate(this.expire_value, new Object[] { TimeUtil.duration(remaining, Config.show_seconds) }) : this.item_expired, this.bottom_spacing, this.bottom_rule });
/*     */ 
/*     */ 
/*     */             
/* 223 */             List<String> lore = new ArrayList<>();
/* 224 */             if (oldLore != null) lore.addAll(oldLore); 
/* 225 */             for (String s : MessageUtil.expand(desc)) {
/* 226 */               if (!s.isEmpty()) lore.add(s); 
/*     */             } 
/* 228 */             MenuItem menuItem = new MenuItem(name, key, amount, lore, nbt);
/* 229 */             menu.setItem(i, menuItem);
/*     */           } 
/*     */         } 
/*     */       } else {
/* 233 */         menu.setItem(i, blank);
/*     */       } 
/*     */     } 
/* 236 */     menu.setItem(48, (this.current_page > 0) ? previous : blank);
/* 237 */     menu.setItem(50, (this.current_page < pages) ? next : blank);
/* 238 */     OfflinePlayer op = AuctionHouse.plugin.getServer().getOfflinePlayer(player.getUniqueId());
/*     */     
/* 240 */     Auctions.updateCounts(player);
/* 241 */     int expired = Auctions.getExpiredListingsCount(op);
/* 242 */     updateExpiredListingsButton(menu, expired);
/*     */     
/* 244 */     int sold = Auctions.getSoldItemsCount(op);
/* 245 */     updateSoldItemsButton(menu, sold);
/*     */   }
/*     */   
/*     */   private void updateExpiredListingsButton(Menu menu, int expired) {
/* 249 */     this.expiredListingsCount = expired;
/* 250 */     List<String> lore = MessageUtil.expand(Arrays.asList(new String[] { this.expired_listings_desc1, this.expired_listings_desc2, 
/*     */ 
/*     */             
/* 253 */             (expired > 0) ? MessageUtil.populate(this.returnable_value, new Object[] { Integer.valueOf(expired) }) : this.returnable_none }));
/* 254 */     MenuItem expired_listings = new MenuItem(this.menu_expired_listings, Config.expired_listings_button, 1, lore);
/* 255 */     menu.setItem(46, expired_listings);
/* 256 */     menu.setItemAmount(46, 1);
/*     */   }
/*     */   
/*     */   private void updateSoldItemsButton(Menu menu, int sold) {
/* 260 */     this.soldItemsCount = sold;
/* 261 */     if (sold > 0) {
/* 262 */       List<String> lore = Arrays.asList(new String[] { this.sold_items_desc1, this.sold_items_desc2, 
/*     */ 
/*     */             
/* 265 */             MessageUtil.populate(this.sold_items_value, new Object[] { Integer.valueOf(sold) }) });
/* 266 */       MenuItem sold_items = new MenuItem(this.menu_sold_items, Config.sold_items_button, 1, MessageUtil.expand(lore));
/* 267 */       menu.setItem(52, sold_items);
/*     */     } else {
/* 269 */       List<String> lore = Collections.singletonList(this.sold_items_none);
/* 270 */       menu.setItemLore(52, MessageUtil.expand(lore));
/*     */     } 
/* 272 */     menu.setItemAmount(52, 1);
/*     */   }
/*     */   
/*     */   private void update(Player player) {
/* 276 */     Menu menu = AuctionHouse.menuManager.getMenu(this.id);
/* 277 */     if (menu == null)
/*     */       return; 
/* 279 */     Auctions.updateCounts(player);
/* 280 */     OfflinePlayer op = AuctionHouse.plugin.getServer().getOfflinePlayer(player.getUniqueId());
/* 281 */     int count = Auctions.getPlayerListingsCount(op);
/*     */     
/* 283 */     if (this.playerListingsCount != count) {
/* 284 */       this.playerListingsCount = count;
/*     */ 
/*     */       
/* 287 */       int pages = paginate(player);
/* 288 */       if (this.current_page > pages) this.current_page = pages; 
/* 289 */       build(player, this.current_page);
/*     */     } 
/*     */     
/* 292 */     int size = this.page_keys.size();
/* 293 */     long now = TimeUtil.now();
/* 294 */     int offset = this.current_page * 45;
/* 295 */     for (int i = 0; i < 54; i++) {
/* 296 */       ItemTag tag = this.tags.get(Integer.valueOf(i));
/* 297 */       if (tag != null && tag.equals(ItemTag.ITEM)) {
/* 298 */         int index = offset + i;
/* 299 */         if (index < size) {
/* 300 */           long timestamp = ((Long)this.page_keys.get(index)).longValue();
/* 301 */           long remaining = timestamp - now;
/* 302 */           menu.replaceItemLore(i, this.key, (remaining > 0L) ? MessageUtil.populate(this.expire_value, new Object[] { TimeUtil.duration(remaining, Config.show_seconds) }) : this.item_expired);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 307 */     int expired = Auctions.getExpiredListingsCount(op);
/* 308 */     if (this.expiredListingsCount != expired) updateExpiredListingsButton(menu, expired);
/*     */     
/* 310 */     int sold = Auctions.getSoldItemsCount(op);
/* 311 */     if (this.soldItemsCount != sold) updateSoldItemsButton(menu, sold);
/*     */   
/*     */   }
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST)
/*     */   public void onMenuClick(MenuClickEvent event) {
/* 317 */     Player player = event.getPlayer();
/* 318 */     String id = event.getId();
/* 319 */     if (player == null || id == null)
/* 320 */       return;  if (id.equals(this.id)) {
/* 321 */       Menu menu = AuctionHouse.menuManager.getMenu(id);
/* 322 */       if (menu == null)
/* 323 */         return;  int slot = event.getSlot();
/* 324 */       ItemStack itemStack = menu.getItem(slot);
/* 325 */       if (itemStack == null)
/* 326 */         return;  if (itemStack.getType().equals(Material.AIR))
/* 327 */         return;  MenuClickType click = event.getMenuClickType();
/*     */       
/* 329 */       if (PlayerUtil.spamCheck(player))
/*     */         return; 
/* 331 */       if (click == MenuClickType.LEFT) {
/* 332 */         ItemTag tag = this.tags.get(Integer.valueOf(slot));
/* 333 */         if (tag != null) {
/* 334 */           if (tag.equals(ItemTag.ITEM)) {
/* 335 */             int index = this.current_page * 45 + slot;
/* 336 */             int size = this.page_keys.size();
/* 337 */             if (index < size && size > 0) {
/* 338 */               SoundUtil.clickSound(player);
/* 339 */               long timestamp = ((Long)this.page_keys.get(index)).longValue();
/* 340 */               OfflinePlayer buyer = Auctions.getBuyer(Long.valueOf(timestamp));
/* 341 */               if (buyer != null) {
/* 342 */                 MessageUtil.sendMessage(player, "warning.listing.expired", Config.locale);
/* 343 */                 SoundUtil.failSound(player);
/*     */               } else {
/* 345 */                 String name = MessageUtil.sectionSymbol(ItemUtil.getTranslatableName(itemStack));
/* 346 */                 int count = itemStack.getAmount();
/* 347 */                 if (Auctions.cancelItem(player, Long.valueOf(timestamp))) {
/* 348 */                   update(player);
/* 349 */                   MessageUtil.sendMessage(player, "message.cancel.success", Config.locale, new Object[] { player.getName(), Integer.valueOf(count), name });
/*     */                 } 
/*     */               } 
/* 352 */               paginate(player);
/* 353 */               int player_listing_count = Auctions.getPlayerListingsCount((OfflinePlayer)player);
/* 354 */               int pages = Math.floorDiv(Math.max(player_listing_count - 1, 0), 45);
/* 355 */               for (; this.current_page > pages; this.current_page--);
/* 356 */               build(player, this.current_page);
/*     */             } else {
/* 358 */               SoundUtil.failSound(player);
/*     */             } 
/*     */             return;
/*     */           } 
/* 362 */           if (tag.equals(ItemTag.BACK)) {
/* 363 */             SoundUtil.clickSound(player);
/*     */             
/* 365 */             Auctions.openActiveListingsMenu(player);
/*     */             return;
/*     */           } 
/* 368 */           if (tag.equals(ItemTag.EXPIRED_LISTINGS)) {
/* 369 */             SoundUtil.clickSound(player);
/*     */             
/* 371 */             Auctions.openExpiredListingsMenu(player);
/*     */             return;
/*     */           } 
/* 374 */           if (tag.equals(ItemTag.PREVIOUS)) {
/* 375 */             SoundUtil.clickSound(player);
/* 376 */             paginate(player);
/* 377 */             if (this.current_page > 0) this.current_page--; 
/* 378 */             build(player, this.current_page);
/*     */             return;
/*     */           } 
/* 381 */           if (tag.equals(ItemTag.SORT_LISTINGS)) {
/* 382 */             SoundUtil.clickSound(player);
/* 383 */             this.sortOrder = this.sortOrder.next();
/* 384 */             String order = LocaleStorage.translate(this.sortOrder.key(), Config.locale);
/* 385 */             List<String> lore = Collections.singletonList(MessageUtil.populate(this.order_value, new Object[] { order }));
/* 386 */             menu.setItemLore(49, MessageUtil.expand(lore));
/* 387 */             paginate(player);
/* 388 */             build(player, this.current_page);
/*     */             return;
/*     */           } 
/* 391 */           if (tag.equals(ItemTag.NEXT)) {
/* 392 */             SoundUtil.clickSound(player);
/*     */ 
/*     */ 
/*     */             
/* 396 */             int pages = paginate(player);
/* 397 */             if (this.current_page < pages) this.current_page++; 
/* 398 */             build(player, this.current_page);
/*     */             return;
/*     */           } 
/* 401 */           if (tag.equals(ItemTag.SOLD_ITEMS)) {
/* 402 */             SoundUtil.clickSound(player);
/*     */             
/* 404 */             Auctions.openSoldItemsMenu(player);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST)
/*     */   public void onServerTick(ServerTickEvent event) {
/* 414 */     Menu menu = AuctionHouse.menuManager.getMenu(this.id);
/* 415 */     if (menu == null)
/* 416 */       return;  List<HumanEntity> viewers = menu.getViewers();
/* 417 */     for (HumanEntity entity : viewers) {
/* 418 */       if (entity instanceof Player) {
/* 419 */         Player player = (Player)entity;
/* 420 */         update(player);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\menus\PlayerListingsMenu.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */