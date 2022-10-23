/*     */ package com.spawnchunk.auctionhouse.modules;
/*     */ 
/*     */ import com.google.gson.stream.MalformedJsonException;
/*     */ import com.spawnchunk.auctionhouse.AuctionHouse;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Listing
/*     */ {
/*     */   private String world;
/*     */   private String seller_uuid;
/*     */   private String buyer_uuid;
/*     */   private String bidder_uuid;
/*     */   private float price;
/*     */   private float reserve;
/*     */   private float bid;
/*  30 */   private ListingType type = ListingType.PLAYER_LISTING;
/*     */   private ItemStack item;
/*     */   
/*     */   public Listing(String world, @NotNull String seller_uuid, @Nullable String buyer_uuid, @Nullable String bidder_uuid, float price, float reserve, float bid, @NotNull ListingType type, ItemStack item) {
/*  34 */     this.world = world;
/*  35 */     this.seller_uuid = seller_uuid;
/*  36 */     this.buyer_uuid = (buyer_uuid != null) ? buyer_uuid : "";
/*  37 */     this.bidder_uuid = (bidder_uuid != null) ? bidder_uuid : "";
/*  38 */     this.price = price;
/*  39 */     this.reserve = reserve;
/*  40 */     this.bid = 0.0F;
/*  41 */     this.type = type;
/*  42 */     this.item = item;
/*     */   }
/*     */ 
/*     */   
/*     */   public Listing(String json) {
/*  47 */     String item = parseItem(json);
/*  48 */     String id = parseId(item);
/*  49 */     int count = parseCount(item);
/*  50 */     String nbt = parseNBTTag(item);
/*  51 */     Material material = Material.matchMaterial(id);
/*  52 */     if (material != null) {
/*  53 */       String world = parseWorld(json);
/*  54 */       String seller_uuid = parseSeller(json);
/*  55 */       String buyer_uuid = parseBuyer(json);
/*  56 */       String bidder_uuid = parseBidder(json);
/*  57 */       float price = parsePrice(json);
/*  58 */       float reserve = parseReserve(json);
/*  59 */       float bid = parseBid(json);
/*  60 */       ListingType type = parseType(json);
/*  61 */       ItemStack itemStack = new ItemStack(material, count);
/*  62 */       itemStack = AuctionHouse.nms.setNBTString(itemStack, nbt);
/*     */       
/*  64 */       if (seller_uuid != null) {
/*  65 */         this.world = world;
/*  66 */         this.seller_uuid = seller_uuid;
/*  67 */         this.buyer_uuid = (buyer_uuid != null) ? buyer_uuid : "";
/*  68 */         this.bidder_uuid = (bidder_uuid != null) ? bidder_uuid : "";
/*  69 */         this.price = price;
/*  70 */         this.reserve = reserve;
/*  71 */         this.bid = bid;
/*  72 */         this.type = type;
/*  73 */         this.item = itemStack;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private String getName(UUID uuid) {
/*  79 */     return AuctionHouse.plugin.getServer().getOfflinePlayer(uuid).getName();
/*     */   }
/*     */   
/*     */   public String getWorld() {
/*  83 */     return this.world;
/*     */   }
/*     */   public void setWorld(String world) {
/*  86 */     this.world = world;
/*     */   }
/*     */   
/*     */   public String getSeller_UUID() {
/*  90 */     return this.seller_uuid;
/*     */   }
/*     */   public void setSeller_UUID(String seller_uuid) {
/*  93 */     this.seller_uuid = (seller_uuid != null) ? seller_uuid : "";
/*     */   }
/*     */   
/*     */   public String getBuyer_UUID() {
/*  97 */     return (this.buyer_uuid != null) ? this.buyer_uuid : "";
/*     */   }
/*     */   public void setBuyer_UUID(String buyer_uuid) {
/* 100 */     this.buyer_uuid = (buyer_uuid != null) ? buyer_uuid : "";
/*     */   }
/*     */   
/*     */   public String getBidder_UUID() {
/* 104 */     return (this.bidder_uuid != null) ? this.bidder_uuid : "";
/*     */   }
/*     */   public void setBidder_UUID(String bidder_uuid) {
/* 107 */     this.bidder_uuid = (bidder_uuid != null) ? bidder_uuid : "";
/*     */   }
/*     */   
/*     */   public OfflinePlayer getSeller() {
/*     */     try {
/* 112 */       UUID uuid = !this.seller_uuid.isEmpty() ? UUID.fromString(this.seller_uuid) : null;
/* 113 */       return (uuid != null) ? Bukkit.getOfflinePlayer(uuid) : null;
/* 114 */     } catch (IllegalArgumentException illegalArgumentException) {
/*     */       
/* 116 */       return null;
/*     */     } 
/*     */   }
/*     */   public String getSellerName() {
/* 120 */     String uuid_string = this.seller_uuid;
/* 121 */     if (uuid_string.isEmpty()) return ""; 
/* 122 */     UUID uuid = null;
/*     */     try {
/* 124 */       uuid = UUID.fromString(uuid_string);
/* 125 */     } catch (IllegalArgumentException illegalArgumentException) {}
/*     */     
/* 127 */     if (uuid == null) return ""; 
/* 128 */     OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
/* 129 */     if (op.hasPlayedBefore()) {
/* 130 */       return op.getName();
/*     */     }
/* 132 */     return uuid_string;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSeller(OfflinePlayer seller) {
/* 137 */     this.seller_uuid = seller.getUniqueId().toString();
/*     */   }
/*     */   
/*     */   public OfflinePlayer getBuyer() {
/* 141 */     UUID uuid = !this.buyer_uuid.isEmpty() ? UUID.fromString(this.buyer_uuid) : null;
/* 142 */     return (uuid != null) ? Bukkit.getOfflinePlayer(uuid) : null;
/*     */   }
/*     */   
/*     */   public String getBuyerName() {
/* 146 */     String uuid_string = this.buyer_uuid;
/* 147 */     if (uuid_string.isEmpty()) return ""; 
/* 148 */     UUID uuid = null;
/*     */     try {
/* 150 */       uuid = UUID.fromString(uuid_string);
/* 151 */     } catch (IllegalArgumentException illegalArgumentException) {}
/*     */     
/* 153 */     if (uuid == null) return ""; 
/* 154 */     OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
/* 155 */     if (op.hasPlayedBefore()) {
/* 156 */       return op.getName();
/*     */     }
/* 158 */     return uuid_string;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBuyer(OfflinePlayer buyer) {
/* 163 */     this.buyer_uuid = buyer.getUniqueId().toString();
/*     */   }
/*     */   
/*     */   public OfflinePlayer getBidder() {
/* 167 */     UUID uuid = !this.bidder_uuid.isEmpty() ? UUID.fromString(this.bidder_uuid) : null;
/* 168 */     return (uuid != null) ? Bukkit.getOfflinePlayer(uuid) : null;
/*     */   }
/*     */   public void setBidder(OfflinePlayer bidder) {
/* 171 */     this.bidder_uuid = bidder.getUniqueId().toString();
/*     */   }
/*     */   
/*     */   public float getPrice() {
/* 175 */     return this.price;
/*     */   }
/*     */   public void setPrice(float price) {
/* 178 */     this.price = price;
/*     */   }
/*     */   
/*     */   public float getReserve() {
/* 182 */     return this.reserve;
/*     */   }
/*     */   public void setReserve(float reserve) {
/* 185 */     this.reserve = reserve;
/*     */   }
/*     */   
/*     */   public float getBid() {
/* 189 */     return this.bid;
/*     */   }
/*     */   
/*     */   public void setBid(float bid) {
/* 193 */     this.bid = bid;
/*     */   }
/*     */   @NotNull
/*     */   public ListingType getType() {
/* 197 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(ListingType type) {
/* 201 */     this.type = type;
/*     */   }
/*     */   
/*     */   public ItemStack getItem() {
/* 205 */     return this.item;
/*     */   }
/*     */   
/*     */   public void setItem(ItemStack itemStack) {
/* 209 */     this.item = itemStack;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 213 */     ItemStack item = this.item;
/* 214 */     if (item == null) return ""; 
/* 215 */     Material material = item.getType();
/* 216 */     String id = material.getKey().toString();
/* 217 */     int count = item.getAmount();
/* 218 */     String world = (this.world == null) ? "" : this.world;
/* 219 */     String seller_uuid = (this.seller_uuid != null) ? this.seller_uuid : "";
/* 220 */     String buyer_uuid = (this.buyer_uuid != null) ? this.buyer_uuid : "";
/* 221 */     String bidder_uuid = (this.bidder_uuid != null) ? this.bidder_uuid : "";
/* 222 */     float price = this.price;
/* 223 */     float reserve = this.reserve;
/* 224 */     float bid = this.bid;
/* 225 */     ListingType type = this.type;
/* 226 */     String nbt = AuctionHouse.nms.getNBTString(this.item);
/* 227 */     if (nbt == null) nbt = "{}"; 
/* 228 */     return String.format("{World:\"%s\",Seller:\"%s\",Buyer:\"%s\",Bidder:\"%s\",Price:%.2f,Reserve:%.2f,Bid:%.2f,Type:%s,Item:{id:\"%s\",Count:%d,nbt:%s}}", new Object[] { world, seller_uuid, buyer_uuid, bidder_uuid, 
/* 229 */           Float.valueOf(price), Float.valueOf(reserve), Float.valueOf(bid), type.name(), id, Integer.valueOf(count), nbt });
/*     */   }
/*     */   
/*     */   private String parseNBT(String json) throws MalformedJsonException {
/* 233 */     String s = json;
/* 234 */     if (s.startsWith("{")) s = unbracket(s); 
/* 235 */     List<String> entries = splitEscaped(s);
/* 236 */     for (String entry : entries) {
/* 237 */       if (entry.startsWith("nbt:")) {
/* 238 */         s = entry.replace("nbt:", "");
/* 239 */         return s;
/*     */       } 
/*     */     } 
/* 242 */     return "{}";
/*     */   }
/*     */   
/*     */   private String parseString(String json, String key) throws MalformedJsonException {
/* 246 */     String s = json;
/* 247 */     if (s.startsWith("{")) s = unbracket(s); 
/* 248 */     List<String> entries = splitEscaped(s);
/* 249 */     for (String entry : entries) {
/* 250 */       if (entry.startsWith(key + ":")) {
/* 251 */         s = entry.replace(key + ":", "");
/* 252 */         if (s.startsWith("\"")) s = unquote(s); 
/* 253 */         return s;
/*     */       } 
/*     */     } 
/* 256 */     return "";
/*     */   }
/*     */   
/*     */   private boolean parseBoolean(String json, String key) throws MalformedJsonException {
/* 260 */     boolean b = false;
/* 261 */     String s = json;
/* 262 */     if (s.startsWith("{")) s = unbracket(s); 
/* 263 */     List<String> entries = splitEscaped(s);
/* 264 */     for (String entry : entries) {
/* 265 */       if (entry.startsWith(key + ":")) {
/* 266 */         s = entry.replace(key + ":", "");
/* 267 */         b = Boolean.parseBoolean(s);
/*     */         break;
/*     */       } 
/*     */     } 
/* 271 */     return b;
/*     */   }
/*     */   
/*     */   private int parseInteger(String json, String key) throws MalformedJsonException {
/* 275 */     int n = 0;
/* 276 */     String s = json;
/* 277 */     if (s.startsWith("{")) s = unbracket(s); 
/* 278 */     List<String> entries = splitEscaped(s);
/* 279 */     for (String entry : entries) {
/* 280 */       if (entry.startsWith(key + ":")) {
/* 281 */         s = entry.replace(key + ":", "");
/* 282 */         n = Integer.parseInt(s);
/*     */         break;
/*     */       } 
/*     */     } 
/* 286 */     return n;
/*     */   }
/*     */   
/*     */   private float parseFloat(String json, String key) throws MalformedJsonException {
/* 290 */     float f = 0.0F;
/* 291 */     String s = json;
/* 292 */     if (s.startsWith("{")) s = unbracket(s); 
/* 293 */     List<String> entries = splitEscaped(s);
/* 294 */     for (String entry : entries) {
/* 295 */       if (entry.startsWith(key + ":")) {
/* 296 */         s = entry.replace(key + ":", "");
/* 297 */         f = Float.parseFloat(s);
/*     */         break;
/*     */       } 
/*     */     } 
/* 301 */     return f;
/*     */   }
/*     */   
/*     */   private String parseItem(String json) {
/* 305 */     String item = null;
/*     */     try {
/* 307 */       item = parseString(json, "Item");
/* 308 */     } catch (MalformedJsonException e) {
/* 309 */       AuctionHouse.logger.warning(String.format("MalformedJsonException parsing Item - %s", new Object[] { e.getMessage() }));
/* 310 */       AuctionHouse.logger.info(String.format("json: %s", new Object[] { json }));
/*     */     } 
/* 312 */     return item;
/*     */   }
/*     */   
/*     */   private String parseId(String json) {
/* 316 */     String id = null;
/*     */     try {
/* 318 */       id = parseString(json, "id");
/* 319 */     } catch (MalformedJsonException e) {
/* 320 */       AuctionHouse.logger.warning(String.format("MalformedJsonException parsing Id - %s", new Object[] { e.getMessage() }));
/* 321 */       AuctionHouse.logger.info(String.format("json: %s", new Object[] { json }));
/*     */     } 
/* 323 */     return id;
/*     */   }
/*     */   
/*     */   private int parseCount(String json) {
/* 327 */     int count = 0;
/*     */     try {
/* 329 */       count = parseInteger(json, "Count");
/* 330 */     } catch (MalformedJsonException e) {
/* 331 */       AuctionHouse.logger.warning(String.format("MalformedJsonException parsing Count - %s", new Object[] { e.getMessage() }));
/* 332 */       AuctionHouse.logger.info(String.format("json: %s", new Object[] { json }));
/*     */     } 
/* 334 */     return count;
/*     */   }
/*     */   
/*     */   private String parseWorld(String json) {
/* 338 */     String world = null;
/*     */     try {
/* 340 */       world = parseString(json, "World");
/*     */       
/* 342 */       if (world.isEmpty()) world = ((World)Bukkit.getServer().getWorlds().get(0)).getName(); 
/* 343 */     } catch (MalformedJsonException e) {
/* 344 */       AuctionHouse.logger.warning(String.format("MalformedJsonException parsing World - %s", new Object[] { e.getMessage() }));
/* 345 */       AuctionHouse.logger.info(String.format("json: %s", new Object[] { json }));
/*     */     } 
/* 347 */     return world;
/*     */   }
/*     */   
/*     */   private String parseSeller(String json) {
/* 351 */     String seller = null;
/*     */     try {
/* 353 */       seller = parseString(json, "Seller");
/* 354 */     } catch (MalformedJsonException e) {
/* 355 */       AuctionHouse.logger.warning(String.format("MalformedJsonException parsing Seller - %s", new Object[] { e.getMessage() }));
/* 356 */       AuctionHouse.logger.info(String.format("json: %s", new Object[] { json }));
/*     */     } 
/* 358 */     return seller;
/*     */   }
/*     */   
/*     */   private String parseBuyer(String json) {
/* 362 */     String buyer = null;
/*     */     try {
/* 364 */       buyer = parseString(json, "Buyer");
/* 365 */     } catch (MalformedJsonException e) {
/* 366 */       AuctionHouse.logger.warning(String.format("MalformedJsonException parsing Buyer - %s", new Object[] { e.getMessage() }));
/* 367 */       AuctionHouse.logger.info(String.format("json: %s", new Object[] { json }));
/*     */     } 
/* 369 */     return buyer;
/*     */   }
/*     */   
/*     */   private String parseBidder(String json) {
/* 373 */     String bidder = null;
/*     */     try {
/* 375 */       bidder = parseString(json, "Bidder");
/* 376 */     } catch (MalformedJsonException e) {
/* 377 */       AuctionHouse.logger.warning(String.format("MalformedJsonException parsing Bidder - %s", new Object[] { e.getMessage() }));
/* 378 */       AuctionHouse.logger.info(String.format("json: %s", new Object[] { json }));
/*     */     } 
/* 380 */     return bidder;
/*     */   }
/*     */   
/*     */   private float parsePrice(String json) {
/* 384 */     float price = 0.0F;
/*     */     try {
/* 386 */       price = parseFloat(json, "Price");
/* 387 */     } catch (MalformedJsonException e) {
/* 388 */       AuctionHouse.logger.warning(String.format("MalformedJsonException parsing Price - %s", new Object[] { e.getMessage() }));
/* 389 */       AuctionHouse.logger.info(String.format("json: %s", new Object[] { json }));
/*     */     } 
/* 391 */     return price;
/*     */   }
/*     */   
/*     */   private float parseReserve(String json) {
/* 395 */     float reserve = 0.0F;
/*     */     try {
/* 397 */       reserve = parseFloat(json, "Reserve");
/* 398 */     } catch (MalformedJsonException e) {
/* 399 */       AuctionHouse.logger.warning(String.format("MalformedJsonException parsing Reserve - %s", new Object[] { e.getMessage() }));
/* 400 */       AuctionHouse.logger.info(String.format("json: %s", new Object[] { json }));
/*     */     } 
/* 402 */     return reserve;
/*     */   }
/*     */   
/*     */   private float parseBid(String json) {
/* 406 */     float bid = 0.0F;
/*     */     try {
/* 408 */       bid = parseFloat(json, "Bid");
/* 409 */     } catch (MalformedJsonException e) {
/* 410 */       AuctionHouse.logger.warning(String.format("MalformedJsonException parsing Bid - %s", new Object[] { e.getMessage() }));
/* 411 */       AuctionHouse.logger.info(String.format("json: %s", new Object[] { json }));
/*     */     } 
/* 413 */     return bid;
/*     */   }
/*     */   
/*     */   private ListingType parseType(String json) {
/* 417 */     String type = "";
/*     */     try {
/* 419 */       type = parseString(json, "Type");
/* 420 */     } catch (MalformedJsonException e) {
/* 421 */       AuctionHouse.logger.warning(String.format("MalformedJsonException parsing Type - %s", new Object[] { e.getMessage() }));
/* 422 */       AuctionHouse.logger.info(String.format("json: %s", new Object[] { json }));
/*     */     } 
/* 424 */     switch (type) {
/*     */       case "SERVER_LISTING_UNLIMITED":
/* 426 */         return ListingType.SERVER_LISTING_UNLIMITED;
/*     */       case "SERVER_LISTING":
/* 428 */         return ListingType.SERVER_LISTING;
/*     */       case "PLAYER_AUCTION":
/* 430 */         return ListingType.PLAYER_AUCTION;
/*     */     } 
/*     */     
/* 433 */     return ListingType.PLAYER_LISTING;
/*     */   }
/*     */ 
/*     */   
/*     */   private String parseNBTTag(String json) {
/* 438 */     String nbt = null;
/*     */     try {
/* 440 */       nbt = parseNBT(json);
/*     */       
/* 442 */       if (nbt.isEmpty()) nbt = "{}"; 
/* 443 */     } catch (MalformedJsonException e) {
/* 444 */       AuctionHouse.logger.warning(String.format("MalformedJsonException parsing NBT - %s", new Object[] { e.getMessage() }));
/* 445 */       AuctionHouse.logger.info(String.format("json: %s", new Object[] { json }));
/*     */     } 
/* 447 */     return nbt;
/*     */   }
/*     */   
/*     */   private List<String> splitEscaped(String json) {
/* 451 */     List<String> strings = new ArrayList<>();
/* 452 */     List<Integer> split = new ArrayList<>();
/* 453 */     int escaped = 0;
/* 454 */     for (int i = 0; i < json.length(); i++) {
/* 455 */       char c = json.charAt(i);
/* 456 */       if (c == '{') {
/* 457 */         escaped++;
/* 458 */       } else if (c == '}') {
/* 459 */         escaped--;
/* 460 */       } else if (c == ',' && escaped == 0) {
/*     */         
/* 462 */         split.add(Integer.valueOf(i));
/*     */       } 
/*     */     } 
/* 465 */     if (split.isEmpty()) {
/* 466 */       strings.add(json);
/*     */     } else {
/* 468 */       int l = 0;
/* 469 */       for (Integer pos : split) {
/* 470 */         int r = pos.intValue();
/* 471 */         String str = json.substring(l, r);
/* 472 */         strings.add(str);
/* 473 */         l = r + 1;
/*     */       } 
/* 475 */       String s = json.substring(l);
/* 476 */       strings.add(s);
/*     */     } 
/* 478 */     return strings;
/*     */   }
/*     */   
/*     */   private String unquote(String json) throws MalformedJsonException {
/* 482 */     int lb = json.indexOf("\"");
/* 483 */     int rb = json.lastIndexOf("\"");
/* 484 */     if (lb != -1 && rb != -1 && lb < rb) {
/* 485 */       return json.substring(lb + 1, rb);
/*     */     }
/* 487 */     throw new MalformedJsonException("Missing quotes");
/*     */   }
/*     */ 
/*     */   
/*     */   private String unbracket(String json) throws MalformedJsonException {
/* 492 */     int lb = json.indexOf("{");
/* 493 */     int rb = json.lastIndexOf("}");
/* 494 */     if (lb != -1 && rb != -1 && lb < rb) {
/* 495 */       return json.substring(lb + 1, rb);
/*     */     }
/* 497 */     throw new MalformedJsonException("Missing brackets");
/*     */   }
/*     */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\modules\Listing.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */