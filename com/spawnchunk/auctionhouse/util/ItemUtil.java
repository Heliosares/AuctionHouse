/*     */ package com.spawnchunk.auctionhouse.util;
/*     */ 
/*     */ import com.spawnchunk.auctionhouse.AuctionHouse;
/*     */ import com.spawnchunk.auctionhouse.config.Config;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.enchantments.Enchantment;
/*     */ import org.bukkit.inventory.ItemFlag;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.Damageable;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ import org.bukkit.inventory.meta.Repairable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ItemUtil
/*     */ {
/*     */   public static String getTranslationKey(ItemStack item) {
/*  27 */     Material material = item.getType();
/*  28 */     String key = material.isBlock() ? "block.minecraft." : "item.minecraft.";
/*  29 */     key = key + key;
/*  30 */     return String.format("::{%s}::", new Object[] { key });
/*     */   }
/*     */   
/*     */   public static boolean hasCustomName(ItemStack item) {
/*  34 */     if (item.hasItemMeta() && item.getItemMeta() != null) {
/*  35 */       ItemMeta meta = item.getItemMeta();
/*  36 */       return meta.hasDisplayName();
/*     */     } 
/*  38 */     return false;
/*     */   }
/*     */   
/*     */   public static String getCustomName(ItemStack item) {
/*  42 */     if (item.hasItemMeta() && item.getItemMeta() != null) {
/*  43 */       ItemMeta meta = item.getItemMeta();
/*  44 */       if (meta.hasDisplayName()) {
/*  45 */         return meta.getDisplayName();
/*     */       }
/*     */     } 
/*  48 */     return "";
/*     */   }
/*     */   
/*     */   public static boolean hasLocalizedName(ItemStack item) {
/*  52 */     if (item.hasItemMeta() && item.getItemMeta() != null) {
/*  53 */       ItemMeta meta = item.getItemMeta();
/*  54 */       return meta.hasLocalizedName();
/*     */     } 
/*  56 */     return false;
/*     */   }
/*     */   
/*     */   public static String getLocalizedName(ItemStack item) {
/*  60 */     if (item.hasItemMeta() && item.getItemMeta() != null) {
/*  61 */       ItemMeta meta = item.getItemMeta();
/*  62 */       if (meta.hasDisplayName()) {
/*  63 */         return meta.getLocalizedName();
/*     */       }
/*     */     } 
/*  66 */     return "";
/*     */   }
/*     */   
/*     */   public static String getTranslatableName(ItemStack item) {
/*  70 */     if (hasCustomName(item)) {
/*  71 */       String customName = getCustomName(item);
/*  72 */       if (Config.debug) AuctionHouse.logger.info(String.format("customName = %s", new Object[] { customName })); 
/*  73 */       return getCustomName(item);
/*     */     } 
/*  75 */     return getTranslationKey(item);
/*     */   }
/*     */   
/*     */   public static String getName(ItemStack item) {
/*  79 */     if (hasCustomName(item)) return getCustomName(item); 
/*  80 */     if (hasLocalizedName(item)) return getLocalizedName(item); 
/*  81 */     Material material = item.getType();
/*  82 */     String key = material.getKey().getKey();
/*  83 */     return (hasEnchants(item) ? "&b" : "&f") + (hasEnchants(item) ? "&b" : "&f");
/*     */   }
/*     */   
/*     */   public static String getItemName(ItemStack item) {
/*  87 */     Material material = item.getType();
/*  88 */     return material.getKey().getKey();
/*     */   }
/*     */   
/*     */   public static String getLoreString(ItemStack item) {
/*  92 */     ItemMeta meta = item.getItemMeta();
/*  93 */     if (meta != null && 
/*  94 */       meta.hasLore()) {
/*  95 */       List<String> lore = meta.getLore();
/*  96 */       if (lore != null) {
/*  97 */         boolean flag = true;
/*  98 */         StringBuilder sb = new StringBuilder("[");
/*  99 */         for (String line : lore) {
/* 100 */           if (flag) {
/* 101 */             sb.append(String.format("%s", new Object[] { line }));
/* 102 */             flag = false; continue;
/*     */           } 
/* 104 */           sb.append(String.format(", %s", new Object[] { line }));
/*     */         } 
/*     */         
/* 107 */         sb.append("]");
/* 108 */         return sb.toString();
/*     */       } 
/*     */     } 
/*     */     
/* 112 */     return "";
/*     */   }
/*     */   
/*     */   public static boolean hasEnchants(ItemStack item) {
/* 116 */     if (item.hasItemMeta() && item.getItemMeta() != null) {
/* 117 */       ItemMeta meta = item.getItemMeta();
/* 118 */       return meta.hasEnchants();
/*     */     } 
/* 120 */     return false;
/*     */   }
/*     */   
/*     */   public static List<String> getEnchants(ItemStack item) {
/* 124 */     List<String> enchants = new ArrayList<>();
/* 125 */     if (item.hasItemMeta() && item.getItemMeta() != null) {
/* 126 */       ItemMeta meta = item.getItemMeta();
/* 127 */       if (meta.hasEnchants()) {
/* 128 */         Map<Enchantment, Integer> map = meta.getEnchants();
/* 129 */         Set<Enchantment> keys = map.keySet();
/* 130 */         for (Enchantment ench : keys) {
/* 131 */           int level = ((Integer)map.get(ench)).intValue();
/* 132 */           if (ench.equals(Enchantment.BINDING_CURSE) || ench.equals(Enchantment.VANISHING_CURSE)) {
/* 133 */             enchants.add(String.format("&c%s", new Object[] { prettyEnchant(ench, level) })); continue;
/*     */           } 
/* 135 */           enchants.add(String.format("&7%s", new Object[] { prettyEnchant(ench, level) }));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 140 */     return enchants;
/*     */   }
/*     */   
/*     */   public static String getEnchantsString(ItemStack item) {
/* 144 */     List<String> enchants = getEnchants(item);
/* 145 */     StringBuilder sb = new StringBuilder("[");
/* 146 */     boolean flag = true;
/* 147 */     for (String ench : enchants) {
/* 148 */       if (flag) {
/* 149 */         sb.append(String.format("%s", new Object[] { MessageUtil.sectionSymbol(ench) }));
/* 150 */         flag = false; continue;
/*     */       } 
/* 152 */       sb.append(String.format(", %s", new Object[] { MessageUtil.sectionSymbol(ench) }));
/*     */     } 
/*     */     
/* 155 */     sb.append("]");
/* 156 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static String prettyEnchant(Enchantment enchantment, int level) {
/*     */     String sLevel;
/* 162 */     switch (level) {
/*     */       case 1:
/* 164 */         sLevel = " I";
/*     */         break;
/*     */       case 2:
/* 167 */         sLevel = " II";
/*     */         break;
/*     */       case 3:
/* 170 */         sLevel = " III";
/*     */         break;
/*     */       case 4:
/* 173 */         sLevel = " IV";
/*     */         break;
/*     */       case 5:
/* 176 */         sLevel = " V";
/*     */         break;
/*     */       case 6:
/* 179 */         sLevel = " VI";
/*     */         break;
/*     */       case 7:
/* 182 */         sLevel = " VII";
/*     */         break;
/*     */       case 8:
/* 185 */         sLevel = " VIII";
/*     */         break;
/*     */       case 9:
/* 188 */         sLevel = " IX";
/*     */         break;
/*     */       case 10:
/* 191 */         sLevel = " X";
/*     */         break;
/*     */       default:
/* 194 */         sLevel = String.format(" %s", new Object[] { Integer.valueOf(level) }); break;
/*     */     } 
/* 196 */     String enchantment_key = enchantment.getKey().getKey();
/* 197 */     switch (enchantment_key) {
/*     */       case "protection":
/* 199 */         return String.format("Protection%s", new Object[] { sLevel });
/*     */       case "fire_protection":
/* 201 */         return String.format("Fire Protection%s", new Object[] { sLevel });
/*     */       case "feather_falling":
/* 203 */         return String.format("Feather Falling%s", new Object[] { sLevel });
/*     */       case "blast_protection":
/* 205 */         return String.format("Blast Protection%s", new Object[] { sLevel });
/*     */       case "projectile_protection":
/* 207 */         return String.format("Projectile Protection%s", new Object[] { sLevel });
/*     */       case "respiration":
/* 209 */         return String.format("Respiration%s", new Object[] { sLevel });
/*     */       case "aqua_affinity":
/* 211 */         return String.format("Aqua Affinity%s", new Object[] { (level > 1) ? sLevel : "" });
/*     */       case "thorns":
/* 213 */         return String.format("Thorns%s", new Object[] { sLevel });
/*     */       case "depth_strider":
/* 215 */         return String.format("Depth Strider%s", new Object[] { sLevel });
/*     */       case "frost_walker":
/* 217 */         return String.format("Frost Walker%s", new Object[] { sLevel });
/*     */       case "binding_curse":
/* 219 */         return String.format("Curse of Binding%s", new Object[] { (level > 1) ? sLevel : "" });
/*     */       case "sharpness":
/* 221 */         return String.format("Sharpness%s", new Object[] { sLevel });
/*     */       case "smite":
/* 223 */         return String.format("Smite%s", new Object[] { sLevel });
/*     */       case "bane_of_arthropods":
/* 225 */         return String.format("Bane of Arthropods%s", new Object[] { sLevel });
/*     */       case "knockback":
/* 227 */         return String.format("Knockback%s", new Object[] { sLevel });
/*     */       case "fire_aspect":
/* 229 */         return String.format("Fire Aspect%s", new Object[] { sLevel });
/*     */       case "looting":
/* 231 */         return String.format("Looting%s", new Object[] { sLevel });
/*     */       case "sweeping":
/* 233 */         return String.format("Sweeping Edge%s", new Object[] { sLevel });
/*     */       case "efficiency":
/* 235 */         return String.format("Efficiency%s", new Object[] { sLevel });
/*     */       case "silk_touch":
/* 237 */         return String.format("Silk Touch%s", new Object[] { (level > 1) ? sLevel : "" });
/*     */       case "unbreaking":
/* 239 */         return String.format("Unbreaking%s", new Object[] { sLevel });
/*     */       case "fortune":
/* 241 */         return String.format("Fortune%s", new Object[] { sLevel });
/*     */       case "power":
/* 243 */         return String.format("Power%s", new Object[] { sLevel });
/*     */       case "punch":
/* 245 */         return String.format("Punch%s", new Object[] { sLevel });
/*     */       case "flame":
/* 247 */         return String.format("Flame%s", new Object[] { (level > 1) ? sLevel : "" });
/*     */       case "infinity":
/* 249 */         return String.format("Infinity%s", new Object[] { (level > 1) ? sLevel : "" });
/*     */       case "luck":
/* 251 */         return String.format("Luck of the Sea%s", new Object[] { sLevel });
/*     */       case "lure":
/* 253 */         return String.format("Lure%s", new Object[] { sLevel });
/*     */       case "loyalty":
/* 255 */         return String.format("Loyalty%s", new Object[] { sLevel });
/*     */       case "impaling":
/* 257 */         return String.format("Impaling%s", new Object[] { sLevel });
/*     */       case "riptide":
/* 259 */         return String.format("Riptide%s", new Object[] { sLevel });
/*     */       case "channeling":
/* 261 */         return String.format("Channeling%s", new Object[] { (level > 1) ? sLevel : "" });
/*     */       case "multishot":
/* 263 */         return String.format("Multishot%s", new Object[] { (level > 1) ? sLevel : "" });
/*     */       case "quick_charge":
/* 265 */         return String.format("Quick Charge%s", new Object[] { sLevel });
/*     */       case "piercing":
/* 267 */         return String.format("Piercing%s", new Object[] { sLevel });
/*     */       case "mending":
/* 269 */         return String.format("Mending%s", new Object[] { (level > 1) ? sLevel : "" });
/*     */       case "vanishing_curse":
/* 271 */         return String.format("Curse of Vanishing%s", new Object[] { (level > 1) ? sLevel : "" });
/*     */       case "soul_speed":
/* 273 */         return String.format("Soul Speed%s", new Object[] { sLevel });
/*     */     } 
/* 275 */     return "unknown";
/*     */   }
/*     */ 
/*     */   
/*     */   public static Enchantment getEnchantmentById(short id) {
/* 280 */     switch (id) {
/*     */       case 0:
/* 282 */         return Enchantment.PROTECTION_ENVIRONMENTAL;
/*     */       case 1:
/* 284 */         return Enchantment.PROTECTION_FIRE;
/*     */       case 2:
/* 286 */         return Enchantment.PROTECTION_FALL;
/*     */       case 3:
/* 288 */         return Enchantment.PROTECTION_EXPLOSIONS;
/*     */       case 4:
/* 290 */         return Enchantment.PROTECTION_PROJECTILE;
/*     */       case 5:
/* 292 */         return Enchantment.OXYGEN;
/*     */       case 6:
/* 294 */         return Enchantment.WATER_WORKER;
/*     */       case 7:
/* 296 */         return Enchantment.THORNS;
/*     */       case 8:
/* 298 */         return Enchantment.DEPTH_STRIDER;
/*     */       case 9:
/* 300 */         return Enchantment.FROST_WALKER;
/*     */       case 10:
/* 302 */         return Enchantment.BINDING_CURSE;
/*     */       case 16:
/* 304 */         return Enchantment.DAMAGE_ALL;
/*     */       case 17:
/* 306 */         return Enchantment.DAMAGE_UNDEAD;
/*     */       case 18:
/* 308 */         return Enchantment.DAMAGE_ARTHROPODS;
/*     */       case 19:
/* 310 */         return Enchantment.KNOCKBACK;
/*     */       case 20:
/* 312 */         return Enchantment.FIRE_ASPECT;
/*     */       case 21:
/* 314 */         return Enchantment.LOOT_BONUS_MOBS;
/*     */       case 22:
/* 316 */         return Enchantment.SWEEPING_EDGE;
/*     */       case 32:
/* 318 */         return Enchantment.DIG_SPEED;
/*     */       case 33:
/* 320 */         return Enchantment.SILK_TOUCH;
/*     */       case 34:
/* 322 */         return Enchantment.DURABILITY;
/*     */       case 35:
/* 324 */         return Enchantment.LOOT_BONUS_BLOCKS;
/*     */       case 48:
/* 326 */         return Enchantment.ARROW_DAMAGE;
/*     */       case 49:
/* 328 */         return Enchantment.ARROW_KNOCKBACK;
/*     */       case 50:
/* 330 */         return Enchantment.ARROW_FIRE;
/*     */       case 51:
/* 332 */         return Enchantment.ARROW_INFINITE;
/*     */       case 61:
/* 334 */         return Enchantment.LUCK;
/*     */       case 62:
/* 336 */         return Enchantment.LURE;
/*     */       case 65:
/* 338 */         return Enchantment.LOYALTY;
/*     */       case 66:
/* 340 */         return Enchantment.IMPALING;
/*     */       case 67:
/* 342 */         return Enchantment.RIPTIDE;
/*     */       case 68:
/* 344 */         return Enchantment.CHANNELING;
/*     */       case 70:
/* 346 */         return Enchantment.MENDING;
/*     */       case 71:
/* 348 */         return Enchantment.VANISHING_CURSE;
/*     */     } 
/* 350 */     return null;
/*     */   }
/*     */   
/*     */   public static boolean hasDamage(ItemStack item) {
/* 354 */     if (item.getType() == Material.PLAYER_HEAD) return false; 
/* 355 */     ItemMeta meta = item.getItemMeta();
/* 356 */     if (meta instanceof Damageable) {
/* 357 */       Damageable damageable = (Damageable)meta;
/* 358 */       return damageable.hasDamage();
/*     */     } 
/* 360 */     return false;
/*     */   }
/*     */   
/*     */   public static int getRepairCost(ItemStack itemstack) {
/* 364 */     if (itemstack.hasItemMeta()) {
/* 365 */       ItemMeta meta = itemstack.getItemMeta();
/* 366 */       if (meta instanceof Repairable) {
/* 367 */         return ((Repairable)meta).getRepairCost();
/*     */       }
/*     */     } 
/* 370 */     return -1;
/*     */   }
/*     */   
/*     */   public static boolean isFilledContainer(ItemStack itemstack) {
/* 374 */     if (AuctionHouse.nms.isContainer(itemstack)) {
/* 375 */       Map<Integer, ItemStack> containerItems = AuctionHouse.nms.getContainerItems(itemstack);
/* 376 */       return !containerItems.isEmpty();
/*     */     } 
/* 378 */     return false;
/*     */   }
/*     */   
/*     */   public static ItemStack hidePotionEffects(ItemStack itemstack) {
/* 382 */     ItemStack is = itemstack.clone();
/* 383 */     ItemMeta meta = is.hasItemMeta() ? is.getItemMeta() : Bukkit.getItemFactory().getItemMeta(itemstack.getType());
/* 384 */     if (meta == null) return itemstack; 
/* 385 */     meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_POTION_EFFECTS });
/* 386 */     is.setItemMeta(meta);
/* 387 */     return is;
/*     */   }
/*     */   
/*     */   public static String flatten(String id, short damage) {
/* 391 */     String key = id + "," + id;
/* 392 */     if (VARIANTS.containsKey(key)) return VARIANTS.get(key); 
/* 393 */     return id;
/*     */   }
/*     */   
/* 396 */   private static final HashMap<String, String> VARIANTS = new HashMap<>();
/*     */   static {
/* 398 */     VARIANTS.put("minecraft:stone,1", "minecraft:granite");
/* 399 */     VARIANTS.put("minecraft:stone,2", "minecraft:polished_granite");
/* 400 */     VARIANTS.put("minecraft:stone,3", "minecraft:diorite");
/* 401 */     VARIANTS.put("minecraft:stone,4", "minecraft:polished_diorite");
/* 402 */     VARIANTS.put("minecraft:stone,5", "minecraft:andesite");
/* 403 */     VARIANTS.put("minecraft:stone,6", "minecraft:polished_andesite");
/* 404 */     VARIANTS.put("minecraft:grass,0", "minecraft:grass_block");
/* 405 */     VARIANTS.put("minecraft:dirt,1", "minecraft:coarse_dirt");
/* 406 */     VARIANTS.put("minecraft:dirt,2", "minecraft:podzol");
/* 407 */     VARIANTS.put("minecraft:planks,0", "minecraft:oak_planks");
/* 408 */     VARIANTS.put("minecraft:planks,1", "minecraft:spruce_planks");
/* 409 */     VARIANTS.put("minecraft:planks,2", "minecraft:birch_planks");
/* 410 */     VARIANTS.put("minecraft:planks,3", "minecraft:jungle_planks");
/* 411 */     VARIANTS.put("minecraft:planks,4", "minecraft:acacia_planks");
/* 412 */     VARIANTS.put("minecraft:planks,5", "minecraft:dark_oak_planks");
/* 413 */     VARIANTS.put("minecraft:sapling,0", "minecraft:oak_sapling");
/* 414 */     VARIANTS.put("minecraft:sapling,1", "minecraft:spruce_sapling");
/* 415 */     VARIANTS.put("minecraft:sapling,2", "minecraft:birch_sapling");
/* 416 */     VARIANTS.put("minecraft:sapling,3", "minecraft:jungle_sapling");
/* 417 */     VARIANTS.put("minecraft:sapling,4", "minecraft:acacia_sapling");
/* 418 */     VARIANTS.put("minecraft:sapling,5", "minecraft:dark_oak_sapling");
/* 419 */     VARIANTS.put("minecraft:sand,1", "minecraft:red_sand");
/* 420 */     VARIANTS.put("minecraft:log,0", "minecraft:oak_log");
/* 421 */     VARIANTS.put("minecraft:log,1", "minecraft:spruce_log");
/* 422 */     VARIANTS.put("minecraft:log,2", "minecraft:birch_log");
/* 423 */     VARIANTS.put("minecraft:log,3", "minecraft:jungle_log");
/* 424 */     VARIANTS.put("minecraft:log,4", "minecraft:oak_wood");
/* 425 */     VARIANTS.put("minecraft:log,5", "minecraft:spruce_wood");
/* 426 */     VARIANTS.put("minecraft:log,6", "minecraft:birch_wood");
/* 427 */     VARIANTS.put("minecraft:log,7", "minecraft:jungle_wood");
/* 428 */     VARIANTS.put("minecraft:log2,0", "minecraft:acacia_log");
/* 429 */     VARIANTS.put("minecraft:log2,1", "minecraft:dark_oak_log");
/* 430 */     VARIANTS.put("minecraft:log2,2", "minecraft:acacia_wood");
/* 431 */     VARIANTS.put("minecraft:log2,3", "minecraft:dark_oak_wood");
/* 432 */     VARIANTS.put("minecraft:leaves,0", "minecraft:oak_leaves");
/* 433 */     VARIANTS.put("minecraft:leaves,1", "minecraft:spruce_leaves");
/* 434 */     VARIANTS.put("minecraft:leaves,2", "minecraft:birch_leaves");
/* 435 */     VARIANTS.put("minecraft:leaves,3", "minecraft:jungle_leaves");
/* 436 */     VARIANTS.put("minecraft:leaves2,0", "minecraft:acacia_leaves");
/* 437 */     VARIANTS.put("minecraft:leaves2,1", "minecraft:dark_oak_leaves");
/* 438 */     VARIANTS.put("minecraft:sponge,1", "minecraft:wet_sponge");
/* 439 */     VARIANTS.put("minecraft:sandstone,1", "minecraft:chiseled_sandstone");
/* 440 */     VARIANTS.put("minecraft:sandstone,2", "minecraft:cut_sandstone");
/* 441 */     VARIANTS.put("minecraft:noteblock,0", "minecraft:note_block");
/* 442 */     VARIANTS.put("minecraft:golden_rail,0", "minecraft:powered_rail");
/* 443 */     VARIANTS.put("minecraft:web,0", "minecraft:cobweb");
/* 444 */     VARIANTS.put("minecraft:tallgrass,0", "minecraft:dead_bush");
/* 445 */     VARIANTS.put("minecraft:tallgrass,1", "minecraft:grass");
/* 446 */     VARIANTS.put("minecraft:tallgrass,2", "minecraft:fern");
/* 447 */     VARIANTS.put("minecraft:deadbush,0", "minecraft:dead_bush");
/* 448 */     VARIANTS.put("minecraft:piston_extension,0", "minecraft:moving_piston");
/* 449 */     VARIANTS.put("minecraft:wool,0", "white_wool");
/* 450 */     VARIANTS.put("minecraft:wool,1", "orange_wool");
/* 451 */     VARIANTS.put("minecraft:wool,2", "magenta_wool");
/* 452 */     VARIANTS.put("minecraft:wool,3", "light_blue_wool");
/* 453 */     VARIANTS.put("minecraft:wool,4", "yellow_wool");
/* 454 */     VARIANTS.put("minecraft:wool,5", "lime_wool");
/* 455 */     VARIANTS.put("minecraft:wool,6", "pink_wool");
/* 456 */     VARIANTS.put("minecraft:wool,7", "gray_wool");
/* 457 */     VARIANTS.put("minecraft:wool,8", "light_gray_wool");
/* 458 */     VARIANTS.put("minecraft:wool,9", "cyan_wool");
/* 459 */     VARIANTS.put("minecraft:wool,10", "purple_wool");
/* 460 */     VARIANTS.put("minecraft:wool,11", "blue_wool");
/* 461 */     VARIANTS.put("minecraft:wool,12", "brown_wool");
/* 462 */     VARIANTS.put("minecraft:wool,13", "green_wool");
/* 463 */     VARIANTS.put("minecraft:wool,14", "red_wool");
/* 464 */     VARIANTS.put("minecraft:wool,15", "black_wool");
/* 465 */     VARIANTS.put("minecraft:yellow_flower,0", "dandelion");
/* 466 */     VARIANTS.put("minecraft:red_flower,0", "poppy");
/* 467 */     VARIANTS.put("minecraft:red_flower,1", "blue_orchid");
/* 468 */     VARIANTS.put("minecraft:red_flower,2", "allium");
/* 469 */     VARIANTS.put("minecraft:red_flower,3", "azure_bluet");
/* 470 */     VARIANTS.put("minecraft:red_flower,4", "red_tulip");
/* 471 */     VARIANTS.put("minecraft:red_flower,5", "orange_tulip");
/* 472 */     VARIANTS.put("minecraft:red_flower,6", "white_tulip");
/* 473 */     VARIANTS.put("minecraft:red_flower,7", "pink_tulip");
/* 474 */     VARIANTS.put("minecraft:red_flower,8", "oxeye_daisy");
/* 475 */     VARIANTS.put("minecraft:double_wooden_slab,0", "oak_slab");
/* 476 */     VARIANTS.put("minecraft:double_wooden_slab,1", "spruce_slab");
/* 477 */     VARIANTS.put("minecraft:double_wooden_slab,2", "birch_slab");
/* 478 */     VARIANTS.put("minecraft:wooden_slab,0", "jungle_slab");
/* 479 */     VARIANTS.put("minecraft:wooden_slab,1", "acacia_slab");
/* 480 */     VARIANTS.put("minecraft:wooden_slab,2", "dark_oak_slab");
/* 481 */     VARIANTS.put("minecraft:double_stone_slab,0", "stone_slab");
/* 482 */     VARIANTS.put("minecraft:double_stone_slab,1", "sandstone_slab");
/* 483 */     VARIANTS.put("minecraft:double_stone_slab,2", "petrified_oak_slab");
/* 484 */     VARIANTS.put("minecraft:double_stone_slab,3", "cobblestone_slab");
/* 485 */     VARIANTS.put("minecraft:double_stone_slab,4", "brick_slab");
/* 486 */     VARIANTS.put("minecraft:stone_slab,0", "stone_brick_slab");
/* 487 */     VARIANTS.put("minecraft:stone_slab,1", "nether_brick_slab");
/* 488 */     VARIANTS.put("minecraft:stone_slab,2", "quartz_slab");
/* 489 */     VARIANTS.put("minecraft:stone_slab,3", "smooth_stone");
/* 490 */     VARIANTS.put("minecraft:stone_slab,4", "smooth_sandstone");
/* 491 */     VARIANTS.put("minecraft:stone_slab,5", "smooth_quartz");
/* 492 */     VARIANTS.put("minecraft:double_stone_slab2,0", "red_sandstone_slab");
/* 493 */     VARIANTS.put("minecraft:stone_slab2,0", "smooth_red_sandstone");
/* 494 */     VARIANTS.put("minecraft:purpur_slab,0", "purpur_slab");
/* 495 */     VARIANTS.put("minecraft:brick_block,0", "bricks");
/* 496 */     VARIANTS.put("minecraft:mob_spawner,0", "spawner");
/* 497 */     VARIANTS.put("minecraft:portal,0", "nether_portal");
/* 498 */     VARIANTS.put("minecraft:stone_stairs,0", "cobblestone_stairs");
/* 499 */     VARIANTS.put("minecraft:wooden_pressure_plate,0", "oak_pressure_plate");
/* 500 */     VARIANTS.put("minecraft:snow_layer,0", "snow");
/* 501 */     VARIANTS.put("minecraft:snow,0", "snow_block");
/* 502 */     VARIANTS.put("minecraft:fence,0", "oak_fence");
/* 503 */     VARIANTS.put("minecraft:pumpkin,0", "carved_pumpkin");
/* 504 */     VARIANTS.put("minecraft:lit_pumpkin,0", "jack_o_lantern");
/* 505 */     VARIANTS.put("minecraft:trapdoor,0", "oak_trapdoor");
/* 506 */     VARIANTS.put("minecraft:monster_egg,0", "infested_stone");
/* 507 */     VARIANTS.put("minecraft:monster_egg,1", "infested_cobblestone");
/* 508 */     VARIANTS.put("minecraft:monster_egg,2", "infested_stone_bricks");
/* 509 */     VARIANTS.put("minecraft:monster_egg,3", "infested_mossy_stone_bricks");
/* 510 */     VARIANTS.put("minecraft:monster_egg,4", "infested_cracked_stone_bricks");
/* 511 */     VARIANTS.put("minecraft:monster_egg,5", "infested_chiseled_stone_bricks");
/* 512 */     VARIANTS.put("minecraft:stonebrick,0", "stone_bricks");
/* 513 */     VARIANTS.put("minecraft:stonebrick,1", "mossy_stone_bricks");
/* 514 */     VARIANTS.put("minecraft:stonebrick,2", "cracked_stone_bricks");
/* 515 */     VARIANTS.put("minecraft:stonebrick,3", "chiseled_stone_bricks");
/* 516 */     VARIANTS.put("minecraft:melon_block,0", "melon");
/* 517 */     VARIANTS.put("minecraft:fence_gate,0", "oak_fence_gate");
/* 518 */     VARIANTS.put("minecraft:waterlily,0", "lily_pad");
/* 519 */     VARIANTS.put("minecraft:nether_brick,0", "nether_bricks");
/* 520 */     VARIANTS.put("minecraft:end_bricks,0", "end_stone_bricks");
/* 521 */     VARIANTS.put("minecraft:cobblestone_wall,0", "cobblestone_wall");
/* 522 */     VARIANTS.put("minecraft:cobblestone_wall,1", "mossy_cobblestone_wall");
/* 523 */     VARIANTS.put("minecraft:wooden_button,0", "oak_button");
/* 524 */     VARIANTS.put("minecraft:anvil,1", "chipped_anvil");
/* 525 */     VARIANTS.put("minecraft:anvil,2", "damaged_anvil");
/* 526 */     VARIANTS.put("minecraft:quartz_ore,0", "nether_quartz_ore");
/* 527 */     VARIANTS.put("minecraft:quartz_block,0", "quartz_block");
/* 528 */     VARIANTS.put("minecraft:quartz_block,1", "chiseled_quartz_block");
/* 529 */     VARIANTS.put("minecraft:quartz_block,2", "quartz_pillar");
/* 530 */     VARIANTS.put("minecraft:stained_hardened_clay,0", "white_terracotta");
/* 531 */     VARIANTS.put("minecraft:stained_hardened_clay,1", "orange_terracotta");
/* 532 */     VARIANTS.put("minecraft:stained_hardened_clay,2", "magenta_terracotta");
/* 533 */     VARIANTS.put("minecraft:stained_hardened_clay,3", "light_blue_terracotta");
/* 534 */     VARIANTS.put("minecraft:stained_hardened_clay,4", "yellow_terracotta");
/* 535 */     VARIANTS.put("minecraft:stained_hardened_clay,5", "lime_terracotta");
/* 536 */     VARIANTS.put("minecraft:stained_hardened_clay,6", "pink_terracotta");
/* 537 */     VARIANTS.put("minecraft:stained_hardened_clay,7", "gray_terracotta");
/* 538 */     VARIANTS.put("minecraft:stained_hardened_clay,8", "light_gray_terracotta");
/* 539 */     VARIANTS.put("minecraft:,stained_hardened_clay9", "cyan_terracotta");
/* 540 */     VARIANTS.put("minecraft:stained_hardened_clay,10", "purple_terracotta");
/* 541 */     VARIANTS.put("minecraft:stained_hardened_clay,11", "blue_terracotta");
/* 542 */     VARIANTS.put("minecraft:stained_hardened_clay,12", "brown_terracotta");
/* 543 */     VARIANTS.put("minecraft:stained_hardened_clay,13", "green_terracotta");
/* 544 */     VARIANTS.put("minecraft:stained_hardened_clay,14", "red_terracotta");
/* 545 */     VARIANTS.put("minecraft:stained_hardened_clay,15", "black_terracotta");
/* 546 */     VARIANTS.put("minecraft:carpet,0", "white_carpet");
/* 547 */     VARIANTS.put("minecraft:carpet,1", "orange_carpet");
/* 548 */     VARIANTS.put("minecraft:carpet,2", "magenta_carpet");
/* 549 */     VARIANTS.put("minecraft:carpet,3", "light_blue_carpet");
/* 550 */     VARIANTS.put("minecraft:carpet,4", "yellow_carpet");
/* 551 */     VARIANTS.put("minecraft:carpet,5", "lime_carpet");
/* 552 */     VARIANTS.put("minecraft:carpet,6", "pink_carpet");
/* 553 */     VARIANTS.put("minecraft:carpet,7", "gray_carpet");
/* 554 */     VARIANTS.put("minecraft:carpet,8", "light_gray_carpet");
/* 555 */     VARIANTS.put("minecraft:carpet,9", "cyan_carpet");
/* 556 */     VARIANTS.put("minecraft:carpet,10", "purple_carpet");
/* 557 */     VARIANTS.put("minecraft:carpet,11", "blue_carpet");
/* 558 */     VARIANTS.put("minecraft:carpet,12", "brown_carpet");
/* 559 */     VARIANTS.put("minecraft:carpet,13", "green_carpet");
/* 560 */     VARIANTS.put("minecraft:carpet,14", "red_carpet");
/* 561 */     VARIANTS.put("minecraft:carpet,15", "black_carpet");
/* 562 */     VARIANTS.put("minecraft:hardened_clay,0", "terracotta");
/* 563 */     VARIANTS.put("minecraft:slime,0", "slime_block");
/* 564 */     VARIANTS.put("minecraft:double_plant,0", "sunflower");
/* 565 */     VARIANTS.put("minecraft:double_plant,1", "lilac");
/* 566 */     VARIANTS.put("minecraft:double_plant,2", "tall_grass");
/* 567 */     VARIANTS.put("minecraft:double_plant,3", "large_fern");
/* 568 */     VARIANTS.put("minecraft:double_plant,4", "rose_bush");
/* 569 */     VARIANTS.put("minecraft:double_plant,5", "peony");
/* 570 */     VARIANTS.put("minecraft:stained_glass,0", "white_stained_glass");
/* 571 */     VARIANTS.put("minecraft:stained_glass,1", "orange_stained_glass");
/* 572 */     VARIANTS.put("minecraft:stained_glass,2", "magenta_stained_glass");
/* 573 */     VARIANTS.put("minecraft:stained_glass,3", "light_blue_stained_glass");
/* 574 */     VARIANTS.put("minecraft:stained_glass,4", "yellow_stained_glass");
/* 575 */     VARIANTS.put("minecraft:stained_glass,5", "lime_stained_glass");
/* 576 */     VARIANTS.put("minecraft:stained_glass,6", "pink_stained_glass");
/* 577 */     VARIANTS.put("minecraft:stained_glass,7", "gray_stained_glass");
/* 578 */     VARIANTS.put("minecraft:stained_glass,8", "light_gray_stained_glass");
/* 579 */     VARIANTS.put("minecraft:stained_glass,9", "cyan_stained_glass");
/* 580 */     VARIANTS.put("minecraft:stained_glass,10", "purple_stained_glass");
/* 581 */     VARIANTS.put("minecraft:stained_glass,11", "blue_stained_glass");
/* 582 */     VARIANTS.put("minecraft:stained_glass,12", "brown_stained_glass");
/* 583 */     VARIANTS.put("minecraft:stained_glass,13", "green_stained_glass");
/* 584 */     VARIANTS.put("minecraft:stained_glass,14", "red_stained_glass");
/* 585 */     VARIANTS.put("minecraft:stained_glass,15", "black_stained_glass");
/* 586 */     VARIANTS.put("minecraft:stained_glass_pane,0", "white_stained_glass_pane");
/* 587 */     VARIANTS.put("minecraft:stained_glass_pane,1", "orange_stained_glass_pane");
/* 588 */     VARIANTS.put("minecraft:stained_glass_pane,2", "magenta_stained_glass_pane");
/* 589 */     VARIANTS.put("minecraft:stained_glass_pane,3", "light_blue_stained_glass_pane");
/* 590 */     VARIANTS.put("minecraft:stained_glass_pane,4", "yellow_stained_glass_pane");
/* 591 */     VARIANTS.put("minecraft:stained_glass_pane,5", "lime_stained_glass_pane");
/* 592 */     VARIANTS.put("minecraft:stained_glass_pane,6", "pink_stained_glass_pane");
/* 593 */     VARIANTS.put("minecraft:stained_glass_pane,7", "gray_stained_glass_pane");
/* 594 */     VARIANTS.put("minecraft:stained_glass_pane,8", "light_gray_stained_glass_pane");
/* 595 */     VARIANTS.put("minecraft:stained_glass_pane,9", "cyan_stained_glass_pane");
/* 596 */     VARIANTS.put("minecraft:stained_glass_pane,10", "purple_stained_glass_pane");
/* 597 */     VARIANTS.put("minecraft:stained_glass_pane,11", "blue_stained_glass_pane");
/* 598 */     VARIANTS.put("minecraft:stained_glass_pane,12", "brown_stained_glass_pane");
/* 599 */     VARIANTS.put("minecraft:stained_glass_pane,13", "green_stained_glass_pane");
/* 600 */     VARIANTS.put("minecraft:stained_glass_pane,14", "red_stained_glass_pane");
/* 601 */     VARIANTS.put("minecraft:stained_glass_pane,15", "black_stained_glass_pane");
/* 602 */     VARIANTS.put("minecraft:prismarine,1", "prismarine_bricks");
/* 603 */     VARIANTS.put("minecraft:prismarine,2", "dark_prismarine");
/* 604 */     VARIANTS.put("minecraft:red_sandstone,1", "chiseled_red_sandstone");
/* 605 */     VARIANTS.put("minecraft:red_sandstone,2", "cut_red_sandstone");
/* 606 */     VARIANTS.put("minecraft:magma,0", "magma_block");
/* 607 */     VARIANTS.put("minecraft:red_nether_brick,0", "red_nether_bricks");
/* 608 */     VARIANTS.put("minecraft:silver_shulker_box,0", "light_gray_shulker_box");
/* 609 */     VARIANTS.put("minecraft:silver_glazed_terracotta,0", "light_gray_glazed_terracotta");
/* 610 */     VARIANTS.put("minecraft:concrete,0", "white_concrete");
/* 611 */     VARIANTS.put("minecraft:concrete,1", "orange_concrete");
/* 612 */     VARIANTS.put("minecraft:concrete,2", "magenta_concrete");
/* 613 */     VARIANTS.put("minecraft:concrete,3", "light_blue_concrete");
/* 614 */     VARIANTS.put("minecraft:concrete,4", "yellow_concrete");
/* 615 */     VARIANTS.put("minecraft:concrete,5", "lime_concrete");
/* 616 */     VARIANTS.put("minecraft:concrete,6", "pink_concrete");
/* 617 */     VARIANTS.put("minecraft:concrete,7", "gray_concrete");
/* 618 */     VARIANTS.put("minecraft:concrete,8", "light_gray_concrete");
/* 619 */     VARIANTS.put("minecraft:concrete,9", "cyan_concrete");
/* 620 */     VARIANTS.put("minecraft:concrete,10", "purple_concrete");
/* 621 */     VARIANTS.put("minecraft:concrete,11", "blue_concrete");
/* 622 */     VARIANTS.put("minecraft:concrete,12", "brown_concrete");
/* 623 */     VARIANTS.put("minecraft:concrete,13", "green_concrete");
/* 624 */     VARIANTS.put("minecraft:concrete,14", "red_concrete");
/* 625 */     VARIANTS.put("minecraft:concrete,15", "black_concrete");
/* 626 */     VARIANTS.put("minecraft:concrete_powder,0", "white_concrete_powder");
/* 627 */     VARIANTS.put("minecraft:concrete_powder,1", "orange_concrete_powder");
/* 628 */     VARIANTS.put("minecraft:concrete_powder,2", "magenta_concrete_powder");
/* 629 */     VARIANTS.put("minecraft:concrete_powder,3", "light_blue_concrete_powder");
/* 630 */     VARIANTS.put("minecraft:concrete_powder,4", "yellow_concrete_powder");
/* 631 */     VARIANTS.put("minecraft:concrete_powder,5", "lime_concrete_powder");
/* 632 */     VARIANTS.put("minecraft:concrete_powder,6", "pink_concrete_powder");
/* 633 */     VARIANTS.put("minecraft:concrete_powder,7", "gray_concrete_powder");
/* 634 */     VARIANTS.put("minecraft:concrete_powder,8", "light_gray_concrete_powder");
/* 635 */     VARIANTS.put("minecraft:concrete_powder,9", "cyan_concrete_powder");
/* 636 */     VARIANTS.put("minecraft:concrete_powder,10", "purple_concrete_powder");
/* 637 */     VARIANTS.put("minecraft:concrete_powder,11", "blue_concrete_powder");
/* 638 */     VARIANTS.put("minecraft:concrete_powder,12", "brown_concrete_powder");
/* 639 */     VARIANTS.put("minecraft:concrete_powder,13", "green_concrete_powder");
/* 640 */     VARIANTS.put("minecraft:concrete_powder,14", "red_concrete_powder");
/* 641 */     VARIANTS.put("minecraft:concrete_powder,15", "black_concrete_powder");
/* 642 */     VARIANTS.put("minecraft:wooden_door,0", "oak_door");
/* 643 */     VARIANTS.put("minecraft:coal,1", "charcoal");
/* 644 */     VARIANTS.put("minecraft:golden_apple,1", "enchanted_golden_apple");
/* 645 */     VARIANTS.put("minecraft:boat,0", "oak_boat");
/* 646 */     VARIANTS.put("minecraft:reeds,0", "sugar_cane");
/* 647 */     VARIANTS.put("minecraft:fish,0", "cod");
/* 648 */     VARIANTS.put("minecraft:fish,1", "salmon");
/* 649 */     VARIANTS.put("minecraft:fish,2", "tropical_fish");
/* 650 */     VARIANTS.put("minecraft:fish,3", "pufferfish");
/* 651 */     VARIANTS.put("minecraft:cooked_fish,0", "cooked_cod");
/* 652 */     VARIANTS.put("minecraft:cooked_fish,1", "cooked_salmon");
/* 653 */     VARIANTS.put("minecraft:dye,0", "bone_meal");
/* 654 */     VARIANTS.put("minecraft:dye,1", "orange_dye");
/* 655 */     VARIANTS.put("minecraft:dye,2", "magenta_dye");
/* 656 */     VARIANTS.put("minecraft:dye,3", "light_blue_dye");
/* 657 */     VARIANTS.put("minecraft:dye,4", "dandelion_yellow");
/* 658 */     VARIANTS.put("minecraft:dye,5", "lime_dye");
/* 659 */     VARIANTS.put("minecraft:dye,6", "pink_dye");
/* 660 */     VARIANTS.put("minecraft:dye,7", "gray_dye");
/* 661 */     VARIANTS.put("minecraft:dye,8", "light_gray_dye");
/* 662 */     VARIANTS.put("minecraft:dye,9", "cyan_dye");
/* 663 */     VARIANTS.put("minecraft:dye,10", "purple_dye");
/* 664 */     VARIANTS.put("minecraft:dye,11", "lapis_lazuli");
/* 665 */     VARIANTS.put("minecraft:dye,12", "cocoa_beans");
/* 666 */     VARIANTS.put("minecraft:dye,13", "cactus_green");
/* 667 */     VARIANTS.put("minecraft:dye,14", "rose_red");
/* 668 */     VARIANTS.put("minecraft:dye,15", "ink_sac");
/* 669 */     VARIANTS.put("minecraft:bed,0", "white_bed");
/* 670 */     VARIANTS.put("minecraft:bed,1", "orange_bed");
/* 671 */     VARIANTS.put("minecraft:bed,2", "magenta_bed");
/* 672 */     VARIANTS.put("minecraft:bed,3", "light_blue_bed");
/* 673 */     VARIANTS.put("minecraft:bed,4", "yellow_bed");
/* 674 */     VARIANTS.put("minecraft:bed,5", "lime_bed");
/* 675 */     VARIANTS.put("minecraft:bed,6", "pink_bed");
/* 676 */     VARIANTS.put("minecraft:bed,7", "gray_bed");
/* 677 */     VARIANTS.put("minecraft:bed,8", "light_gray_bed");
/* 678 */     VARIANTS.put("minecraft:bed,9", "cyan_bed");
/* 679 */     VARIANTS.put("minecraft:bed,10", "purple_bed");
/* 680 */     VARIANTS.put("minecraft:bed,11", "blue_bed");
/* 681 */     VARIANTS.put("minecraft:bed,12", "brown_bed");
/* 682 */     VARIANTS.put("minecraft:bed,13", "green_bed");
/* 683 */     VARIANTS.put("minecraft:bed,14", "red_bed");
/* 684 */     VARIANTS.put("minecraft:bed,15", "black_bed");
/* 685 */     VARIANTS.put("minecraft:melon,0", "melon_slice");
/* 686 */     VARIANTS.put("minecraft:speckled_melon,0", "glistering_melon_slice");
/* 687 */     VARIANTS.put("minecraft:spawn_egg,65", "bat_spawn_egg");
/* 688 */     VARIANTS.put("minecraft:spawn_egg,61", "blaze_spawn_egg");
/* 689 */     VARIANTS.put("minecraft:spawn_egg,59", "cave_spider_spawn_egg");
/* 690 */     VARIANTS.put("minecraft:spawn_egg,93", "chicken_spawn_egg");
/* 691 */     VARIANTS.put("minecraft:spawn_egg,92", "cow_spawn_egg");
/* 692 */     VARIANTS.put("minecraft:spawn_egg,50", "creeper_spawn_egg");
/* 693 */     VARIANTS.put("minecraft:spawn_egg,31", "donkey_spawn_egg");
/* 694 */     VARIANTS.put("minecraft:spawn_egg,4", "elder_guardian_spawn_egg");
/* 695 */     VARIANTS.put("minecraft:spawn_egg,58", "enderman_spawn_egg");
/* 696 */     VARIANTS.put("minecraft:spawn_egg,67", "endermite_spawn_egg");
/* 697 */     VARIANTS.put("minecraft:spawn_egg,34", "evoker_spawn_egg");
/* 698 */     VARIANTS.put("minecraft:spawn_egg,56", "ghast_spawn_egg");
/* 699 */     VARIANTS.put("minecraft:spawn_egg,68", "guardian_spawn_egg");
/* 700 */     VARIANTS.put("minecraft:spawn_egg,100", "horse_spawn_egg");
/* 701 */     VARIANTS.put("minecraft:spawn_egg,23", "husk_spawn_egg");
/* 702 */     VARIANTS.put("minecraft:spawn_egg,103", "llama_spawn_egg");
/* 703 */     VARIANTS.put("minecraft:spawn_egg,62", "magma_cube_spawn_egg");
/* 704 */     VARIANTS.put("minecraft:spawn_egg,96", "mooshroom_spawn_egg");
/* 705 */     VARIANTS.put("minecraft:spawn_egg,32", "mule_spawn_egg");
/* 706 */     VARIANTS.put("minecraft:spawn_egg,98", "ocelot_spawn_egg");
/* 707 */     VARIANTS.put("minecraft:spawn_egg,90", "pig_spawn_egg");
/* 708 */     VARIANTS.put("minecraft:spawn_egg,102", "polar_bear_spawn_egg");
/* 709 */     VARIANTS.put("minecraft:spawn_egg,101", "rabbit_spawn_egg");
/* 710 */     VARIANTS.put("minecraft:spawn_egg,91", "sheep_spawn_egg");
/* 711 */     VARIANTS.put("minecraft:spawn_egg,69", "shulker_spawn_egg");
/* 712 */     VARIANTS.put("minecraft:spawn_egg,60", "silverfish_spawn_egg");
/* 713 */     VARIANTS.put("minecraft:spawn_egg,51", "skeleton_spawn_egg");
/* 714 */     VARIANTS.put("minecraft:spawn_egg,28", "skeleton_horse_spawn_egg");
/* 715 */     VARIANTS.put("minecraft:spawn_egg,55", "slime_spawn_egg");
/* 716 */     VARIANTS.put("minecraft:spawn_egg,52", "spider_spawn_egg");
/* 717 */     VARIANTS.put("minecraft:spawn_egg,94", "squid_spawn_egg");
/* 718 */     VARIANTS.put("minecraft:spawn_egg,6", "stray_spawn_egg");
/* 719 */     VARIANTS.put("minecraft:spawn_egg,35", "vex_spawn_egg");
/* 720 */     VARIANTS.put("minecraft:spawn_egg,120", "villager_spawn_egg");
/* 721 */     VARIANTS.put("minecraft:spawn_egg,36", "vindicator_spawn_egg");
/* 722 */     VARIANTS.put("minecraft:spawn_egg,66", "witch_spawn_egg");
/* 723 */     VARIANTS.put("minecraft:spawn_egg,5", "wither_skeleton_spawn_egg");
/* 724 */     VARIANTS.put("minecraft:spawn_egg,95", "wolf_spawn_egg");
/* 725 */     VARIANTS.put("minecraft:spawn_egg,54", "zombie_spawn_egg");
/* 726 */     VARIANTS.put("minecraft:spawn_egg,29", "zombie_horse_spawn_egg");
/* 727 */     VARIANTS.put("minecraft:spawn_egg,57", "zombie_pigman_spawn_egg");
/* 728 */     VARIANTS.put("minecraft:spawn_egg,27", "zombie_villager_spawn_egg");
/* 729 */     VARIANTS.put("minecraft:skull,0", "skeleton_skull");
/* 730 */     VARIANTS.put("minecraft:skull,1", "wither_skeleton_skull");
/* 731 */     VARIANTS.put("minecraft:skull,2", "zombie_head");
/* 732 */     VARIANTS.put("minecraft:skull,3", "player_head");
/* 733 */     VARIANTS.put("minecraft:skull,4", "creeper_head");
/* 734 */     VARIANTS.put("minecraft:skull,5", "dragon_head");
/* 735 */     VARIANTS.put("minecraft:fireworks,0", "firework_rocket");
/* 736 */     VARIANTS.put("minecraft:firework_charge,0", "firework_star");
/* 737 */     VARIANTS.put("minecraft:netherbrick,0", "nether_brick");
/* 738 */     VARIANTS.put("minecraft:banner,0", "white_banner");
/* 739 */     VARIANTS.put("minecraft:banner,1", "orange_banner");
/* 740 */     VARIANTS.put("minecraft:banner,2", "magenta_banner");
/* 741 */     VARIANTS.put("minecraft:banner,3", "light_blue_banner");
/* 742 */     VARIANTS.put("minecraft:banner,4", "yellow_banner");
/* 743 */     VARIANTS.put("minecraft:banner,5", "lime_banner");
/* 744 */     VARIANTS.put("minecraft:banner,6", "pink_banner");
/* 745 */     VARIANTS.put("minecraft:banner,7", "gray_banner");
/* 746 */     VARIANTS.put("minecraft:banner,8", "light_gray_banner");
/* 747 */     VARIANTS.put("minecraft:banner,9", "cyan_banner");
/* 748 */     VARIANTS.put("minecraft:banner,10", "purple_banner");
/* 749 */     VARIANTS.put("minecraft:banner,11", "blue_banner");
/* 750 */     VARIANTS.put("minecraft:banner,12", "brown_banner");
/* 751 */     VARIANTS.put("minecraft:,banner13", "green_banner");
/* 752 */     VARIANTS.put("minecraft:banner,14", "red_banner");
/* 753 */     VARIANTS.put("minecraft:banner,15", "black_banner");
/* 754 */     VARIANTS.put("minecraft:wall_banner,0", "white_wall_banner");
/* 755 */     VARIANTS.put("minecraft:wall_banner,1", "orange_wall_banner");
/* 756 */     VARIANTS.put("minecraft:wall_banner,2", "magenta_wall_banner");
/* 757 */     VARIANTS.put("minecraft:wall_banner,3", "light_blue_wall_banner");
/* 758 */     VARIANTS.put("minecraft:wall_banner,4", "yellow_wall_banner");
/* 759 */     VARIANTS.put("minecraft:wall_banner,5", "lime_wall_banner");
/* 760 */     VARIANTS.put("minecraft:wall_banner,6", "pink_wall_banner");
/* 761 */     VARIANTS.put("minecraft:wall_banner,7", "gray_wall_banner");
/* 762 */     VARIANTS.put("minecraft:wall_banner,8", "light_gray_wall_banner");
/* 763 */     VARIANTS.put("minecraft:wall_banner,9", "cyan_wall_banner");
/* 764 */     VARIANTS.put("minecraft:wall_banner,10", "purple_wall_banner");
/* 765 */     VARIANTS.put("minecraft:wall_banner,11", "blue_wall_banner");
/* 766 */     VARIANTS.put("minecraft:wall_banner,12", "brown_wall_banner");
/* 767 */     VARIANTS.put("minecraft:wall_banner,13", "green_wall_banner");
/* 768 */     VARIANTS.put("minecraft:wall_banner,14", "red_wall_banner");
/* 769 */     VARIANTS.put("minecraft:wall_banner,15", "black_wall_banner");
/* 770 */     VARIANTS.put("minecraft:chorus_fruit_popped,0", "popped_chorus_fruit");
/* 771 */     VARIANTS.put("minecraft:record_13,0", "music_disc_13");
/* 772 */     VARIANTS.put("minecraft:record_cat,0", "music_disc_cat");
/* 773 */     VARIANTS.put("minecraft:record_blocks,0", "music_disc_blocks");
/* 774 */     VARIANTS.put("minecraft:record_chirp,0", "music_disc_chirp");
/* 775 */     VARIANTS.put("minecraft:record_far,0", "music_disc_far");
/* 776 */     VARIANTS.put("minecraft:record_mall,0", "music_disc_mall");
/* 777 */     VARIANTS.put("minecraft:record_mellohi,0", "music_disc_mellohi");
/* 778 */     VARIANTS.put("minecraft:record_stal,0", "music_disc_stal");
/* 779 */     VARIANTS.put("minecraft:record_strad,0", "music_disc_strad");
/* 780 */     VARIANTS.put("minecraft:record_ward,0", "music_disc_ward");
/* 781 */     VARIANTS.put("minecraft:record_11,0", "music_disc_11");
/* 782 */     VARIANTS.put("minecraft:record_wait,0", "music_disc_wait");
/*     */   }
/*     */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhous\\util\ItemUtil.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */