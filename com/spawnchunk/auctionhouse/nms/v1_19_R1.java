/*     */ package com.spawnchunk.auctionhouse.nms;
/*     */ import com.spawnchunk.auctionhouse.AuctionHouse;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.nbt.NBTTagLong;
/*     */ import net.minecraft.nbt.NBTTagString;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.NamespacedKey;
/*     */ import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
/*     */ import org.bukkit.enchantments.Enchantment;
/*     */ import org.bukkit.entity.EntityType;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ 
/*     */ public class v1_19_R1 implements NMS {
/*     */   public String getNBTString(ItemStack itemstack) {
/*  29 */     NBTTagCompound tag = CraftItemStack.asNMSCopy(itemstack).u();
/*  30 */     return (tag != null) ? tag.toString() : null;
/*     */   }
/*     */   
/*     */   public ItemStack setNBTString(ItemStack itemstack, String nbt) {
/*  34 */     ItemStack is = CraftItemStack.asNMSCopy(itemstack);
/*     */     try {
/*  36 */       NBTTagCompound tag = MojangsonParser.a(nbt);
/*  37 */       is.c(tag);
/*  38 */     } catch (CommandSyntaxException e) {
/*  39 */       e.printStackTrace();
/*     */     } 
/*  41 */     return CraftItemStack.asBukkitCopy(is);
/*     */   }
/*     */   
/*     */   public ItemStack addNBTLocator(ItemStack itemstack) {
/*  45 */     if (itemstack != null) {
/*  46 */       Material material = itemstack.getType();
/*  47 */       if (material != Material.AIR) {
/*  48 */         ItemStack is = CraftItemStack.asNMSCopy(itemstack);
/*  49 */         if (is != null) {
/*  50 */           NBTTagCompound tag = is.u();
/*  51 */           if (tag != null) {
/*  52 */             if (tag.b("AuctionHouse", 1)) return itemstack; 
/*  53 */             tag.a("AuctionHouse", (byte)1);
/*  54 */             is.c(tag);
/*     */           } 
/*  56 */           return CraftItemStack.asBukkitCopy(is);
/*     */         } 
/*     */       } 
/*     */     } 
/*  60 */     return itemstack;
/*     */   }
/*     */   
/*     */   public boolean hasNBTLocator(ItemStack itemstack) {
/*  64 */     if (itemstack == null) return false; 
/*  65 */     ItemStack is = CraftItemStack.asNMSCopy(itemstack);
/*  66 */     if (is != null && is.t()) {
/*  67 */       NBTTagCompound tag = is.u();
/*  68 */       return (tag != null && tag.b("AuctionHouse", 1));
/*     */     } 
/*  70 */     return false;
/*     */   }
/*     */   
/*     */   private int[] uuid2IntArray(UUID uuid) {
/*  74 */     int[] i = new int[4];
/*  75 */     long msb = uuid.getMostSignificantBits();
/*  76 */     long lsb = uuid.getLeastSignificantBits();
/*  77 */     i[0] = (int)(msb >> 32L);
/*  78 */     i[1] = (int)(msb & 0xFFFFFFL);
/*  79 */     i[2] = (int)(lsb >> 32L);
/*  80 */     i[3] = (int)(lsb & 0xFFFFFFL);
/*  81 */     return i;
/*     */   }
/*     */   
/*     */   public ItemStack getCustomSkull(String texture) {
/*  85 */     ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
/*  86 */     UUID uuid = UUID.randomUUID();
/*  87 */     ItemStack is = CraftItemStack.asNMSCopy(item);
/*  88 */     NBTTagCompound tag = new NBTTagCompound();
/*  89 */     NBTTagCompound skullOwner = new NBTTagCompound();
/*  90 */     skullOwner.a("Id", uuid2IntArray(uuid));
/*  91 */     NBTTagCompound properties = new NBTTagCompound();
/*  92 */     NBTTagList textures = new NBTTagList();
/*  93 */     NBTTagCompound entry = new NBTTagCompound();
/*  94 */     entry.a("Value", texture);
/*  95 */     textures.add(entry);
/*  96 */     properties.a("textures", (NBTBase)textures);
/*  97 */     skullOwner.a("Properties", (NBTBase)properties);
/*  98 */     tag.a("SkullOwner", (NBTBase)skullOwner);
/*  99 */     is.c(tag);
/* 100 */     item = CraftItemStack.asBukkitCopy(is);
/* 101 */     return item;
/*     */   }
/*     */   
/*     */   public boolean isDye(ItemStack itemstack) {
/* 105 */     Material material = itemstack.getType();
/* 106 */     NamespacedKey namespacedKey = material.getKey();
/* 107 */     String key = namespacedKey.getKey();
/* 108 */     if (!key.isEmpty()) {
/* 109 */       return key.contains("dye");
/*     */     }
/* 111 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isContainer(ItemStack itemstack) {
/* 115 */     ItemStack is = CraftItemStack.asNMSCopy(itemstack);
/* 116 */     if (is != null) {
/* 117 */       NBTTagCompound tag = is.u();
/* 118 */       if (tag != null && 
/* 119 */         tag.b("BlockEntityTag", 10)) {
/* 120 */         NBTTagCompound blockEntityTag = tag.p("BlockEntityTag");
/* 121 */         return blockEntityTag.b("Items", 9);
/*     */       } 
/*     */     } 
/*     */     
/* 125 */     return false;
/*     */   }
/*     */   
/*     */   public Map<Integer, ItemStack> getContainerItems(ItemStack itemstack) {
/* 129 */     Map<Integer, ItemStack> containerItems = new HashMap<>();
/* 130 */     ItemStack is = CraftItemStack.asNMSCopy(itemstack);
/* 131 */     if (is != null) {
/* 132 */       NBTTagCompound isTag = is.u();
/* 133 */       if (isTag != null && 
/* 134 */         isTag.b("BlockEntityTag", 10)) {
/* 135 */         NBTTagCompound blockEntityTag = isTag.p("BlockEntityTag");
/* 136 */         if (blockEntityTag.b("Items", 9)) {
/* 137 */           NBTTagList items = blockEntityTag.c("Items", 10);
/* 138 */           int size = items.size();
/* 139 */           for (int i = 0; i < size; i++) {
/* 140 */             NBTTagCompound item = items.a(i);
/* 141 */             if (item.b("Slot", 1)) {
/* 142 */               byte slot = item.f("Slot");
/* 143 */               if (item.b("id", 8)) {
/* 144 */                 String key = item.l("id");
/* 145 */                 if (key != null && item.b("Count", 1)) {
/* 146 */                   byte count = item.f("Count");
/* 147 */                   if (count > 0) {
/* 148 */                     Material material = Material.matchMaterial(key);
/* 149 */                     if (material != null) {
/* 150 */                       ItemStack itemStack = new ItemStack(material, count);
/* 151 */                       if (item.b("tag", 10)) {
/* 152 */                         NBTTagCompound tag = item.p("tag");
/* 153 */                         ItemStack is2 = CraftItemStack.asNMSCopy(itemStack);
/* 154 */                         is2.c(tag);
/* 155 */                         itemStack = CraftItemStack.asBukkitCopy(is2);
/*     */                       } 
/* 157 */                       containerItems.put(Integer.valueOf(slot), itemStack);
/*     */                     } 
/*     */                   } 
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 168 */     return containerItems;
/*     */   }
/*     */   
/*     */   public List<String> getMobs(ItemStack itemstack) {
/* 172 */     List<String> mobs = new ArrayList<>();
/* 173 */     ItemStack is = CraftItemStack.asNMSCopy(itemstack);
/* 174 */     if (is != null) {
/* 175 */       NBTTagCompound isTag = is.u();
/* 176 */       if (isTag != null) {
/* 177 */         if (isTag.b("BlockEntityTag", 10)) {
/* 178 */           NBTTagCompound blockEntityTag = isTag.p("BlockEntityTag");
/* 179 */           if (blockEntityTag.b("SpawnPotentials", 9)) {
/* 180 */             NBTTagList spawnPotentials = blockEntityTag.c("SpawnPotentials", 10);
/* 181 */             int size = spawnPotentials.size();
/* 182 */             if (size > 0) {
/* 183 */               int count = 0;
/* 184 */               for (int i = 0; i < size; i++) {
/* 185 */                 NBTTagCompound entry = spawnPotentials.a(i);
/* 186 */                 if (entry.b("Entity", 10)) {
/* 187 */                   NBTTagCompound entity = entry.p("Entity");
/* 188 */                   if (entity.b("id", 8)) {
/* 189 */                     String mob = entity.l("id");
/* 190 */                     mobs.add(mob);
/* 191 */                     count++;
/*     */                   } 
/*     */                 } 
/*     */               } 
/* 195 */               if (count > 0) return mobs; 
/*     */             } 
/*     */           } 
/* 198 */           if (blockEntityTag.b("SpawnData", 10)) {
/* 199 */             NBTTagCompound spawnData = blockEntityTag.p("SpawnData");
/* 200 */             if (spawnData.b("id", 8)) {
/* 201 */               String mob = spawnData.l("id");
/* 202 */               mobs.add(mob);
/* 203 */               return mobs;
/*     */             } 
/*     */           } 
/*     */           
/* 207 */           if (blockEntityTag.b("id", 8)) {
/* 208 */             String mob = blockEntityTag.l("id");
/* 209 */             mobs.add(mob);
/* 210 */             return mobs;
/*     */           } 
/*     */         } 
/* 213 */         if (isTag.b("SpawnData", 10)) {
/* 214 */           NBTTagCompound spawnData = isTag.p("SpawnData");
/* 215 */           if (spawnData.b("id", 8)) {
/* 216 */             String mob = spawnData.l("id");
/* 217 */             mobs.add(mob);
/* 218 */             return mobs;
/*     */           } 
/*     */         } 
/* 221 */         if (isTag.b("SilkSpawners", 10)) {
/* 222 */           NBTTagCompound silkSpawners = isTag.p("SilkSpawners");
/* 223 */           if (silkSpawners.b("EntityID", 2)) {
/* 224 */             short entityID = silkSpawners.g("EntityID");
/* 225 */             EntityType entity = EntityType.fromId(entityID);
/* 226 */             if (entity != null) {
/* 227 */               mobs.add(entity.name());
/*     */             }
/* 229 */             return mobs;
/*     */           } 
/*     */         } 
/* 232 */         if (isTag.b("ms_mob", 8)) {
/* 233 */           String mob = isTag.l("ms_mob");
/* 234 */           EntityType entity = EntityType.fromName(mob);
/* 235 */           if (entity != null) {
/* 236 */             mobs.add(entity.name());
/*     */           }
/* 238 */           return mobs;
/*     */         } 
/* 240 */         if (isTag.b("PublicBukkitValues", 10)) {
/* 241 */           NBTTagCompound publicBukkitValues = isTag.p("PublicBukkitValues");
/* 242 */           Set<String> keys = publicBukkitValues.d();
/* 243 */           for (String key : keys) {
/* 244 */             if (PublicBukkitValues.customKeys.contains(key) && publicBukkitValues.b(key, 8)) {
/* 245 */               String mob = publicBukkitValues.l(key);
/* 246 */               mobs.add(mob);
/* 247 */               return mobs;
/*     */             } 
/*     */           } 
/*     */         } 
/* 251 */         if (isValidNBT(isTag)) {
/* 252 */           mobs.add("minecraft:pig");
/*     */         }
/*     */       } else {
/* 255 */         mobs.add("minecraft:pig");
/*     */       } 
/*     */     } 
/* 258 */     return mobs;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isValidNBT(NBTTagCompound isTag) {
/* 263 */     Set<String> keys = isTag.d();
/* 264 */     for (String key : keys) {
/* 265 */       switch (key) {
/*     */         case "BlockEntityTag":
/* 267 */           if (isTag.b("BlockEntityTag", 10)) {
/* 268 */             NBTTagCompound blockEntityTag = isTag.p("BlockEntityTag");
/* 269 */             Set<String> bkeys = blockEntityTag.d();
/* 270 */             for (String bkey : bkeys) {
/* 271 */               switch (bkey) {
/*     */                 case "id":
/*     */                 case "SpawnData":
/*     */                 case "SpawnPotentials":
/*     */                 case "SpawnCount":
/*     */                 case "SpawnRange":
/*     */                 case "Delay":
/*     */                 case "MinSpawnDelay":
/*     */                 case "MaxSpawnDelay":
/*     */                 case "MaxNearbyEntities":
/*     */                 case "RequiredPlayerRange":
/*     */                   continue;
/*     */               } 
/* 284 */               return false;
/*     */             } 
/*     */           } 
/*     */           continue;
/*     */         
/*     */         case "display":
/*     */         case "SpawnData":
/*     */         case "SilkSpawners":
/*     */         case "ms_mob":
/*     */           continue;
/*     */       } 
/* 295 */       return false;
/*     */     } 
/*     */     
/* 298 */     return true;
/*     */   }
/*     */   
/*     */   public int getCustomModelData(ItemStack itemstack) {
/* 302 */     ItemStack is = CraftItemStack.asNMSCopy(itemstack);
/* 303 */     if (is != null) {
/* 304 */       NBTTagCompound tag = is.u();
/* 305 */       if (tag != null && 
/* 306 */         tag.b("CustomModelData", 3)) {
/* 307 */         return tag.h("CustomModelData");
/*     */       }
/*     */     } 
/*     */     
/* 311 */     return 0;
/*     */   }
/*     */   
/*     */   public ItemStack setCustomModelData(ItemStack itemstack, int customModelData) {
/* 315 */     ItemStack is = CraftItemStack.asNMSCopy(itemstack);
/* 316 */     if (is != null) {
/* 317 */       NBTTagCompound tag = is.u();
/* 318 */       if (tag != null)
/*     */       {
/* 320 */         tag.a("CustomModelData", customModelData);
/*     */       }
/* 322 */       is.c(tag);
/* 323 */       return CraftItemStack.asBukkitCopy(is);
/*     */     } 
/* 325 */     return itemstack;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack setLore(ItemStack itemstack, List<String> lore) {
/* 331 */     if (itemstack.getType() != Material.PLAYER_HEAD) {
/* 332 */       ItemMeta itemMeta = itemstack.hasItemMeta() ? itemstack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(itemstack.getType());
/* 333 */       if (itemMeta == null) return itemstack;
/*     */       
/* 335 */       itemMeta.setLore(lore);
/* 336 */       itemstack.setItemMeta(itemMeta);
/* 337 */       return itemstack;
/*     */     } 
/*     */     
/* 340 */     ItemStack clone = new ItemStack(Material.STONE);
/* 341 */     ItemMeta meta = clone.hasItemMeta() ? clone.getItemMeta() : Bukkit.getItemFactory().getItemMeta(Material.STONE);
/* 342 */     if (meta == null) {
/* 343 */       return itemstack;
/*     */     }
/* 345 */     meta.setLore(lore);
/* 346 */     clone.setItemMeta(meta);
/* 347 */     ItemStack isc = CraftItemStack.asNMSCopy(clone);
/* 348 */     List<String> temp_lore = new ArrayList<>();
/* 349 */     if (isc != null) {
/* 350 */       NBTTagCompound tag = isc.t() ? isc.u() : new NBTTagCompound();
/* 351 */       if (tag != null) {
/* 352 */         NBTTagCompound display = tag.b("display", 10) ? tag.p("display") : new NBTTagCompound();
/* 353 */         if (display != null && 
/* 354 */           display.b("Lore", 9)) {
/* 355 */           NBTTagList list = display.c("Lore", 8);
/* 356 */           for (NBTBase b : list) {
/* 357 */             if (b instanceof NBTTagString) {
/* 358 */               NBTTagString s = (NBTTagString)b;
/* 359 */               temp_lore.add(s.e_());
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 367 */     ItemStack is = CraftItemStack.asNMSCopy(itemstack);
/* 368 */     if (is != null) {
/* 369 */       NBTTagCompound tag = is.u();
/* 370 */       if (tag != null) {
/* 371 */         if (!tag.b("display", 10)) tag.a("display", (NBTBase)new NBTTagCompound()); 
/* 372 */         NBTTagCompound display = tag.p("display");
/* 373 */         if (display == null) display = new NBTTagCompound(); 
/* 374 */         NBTTagList list = new NBTTagList();
/*     */         try {
/* 376 */           Constructor<NBTTagString> constructor = ReflectionUtil.getConstructor(NBTTagString.class, new Class[] { String.class });
/* 377 */           for (String s : temp_lore) {
/* 378 */             NBTTagString l = constructor.newInstance(new Object[] { s });
/* 379 */             list.add(l);
/*     */           } 
/* 381 */         } catch (Exception exception) {}
/*     */         
/* 383 */         display.a("Lore", (NBTBase)list);
/* 384 */         tag.a("display", (NBTBase)display);
/* 385 */         is.c(tag);
/* 386 */         return CraftItemStack.asBukkitCopy(is);
/*     */       } 
/*     */     } 
/* 389 */     return itemstack;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack setDisplayName(ItemStack itemstack, String name) {
/* 395 */     if (itemstack.getType() != Material.PLAYER_HEAD) {
/* 396 */       ItemMeta itemMeta = itemstack.hasItemMeta() ? itemstack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(itemstack.getType());
/* 397 */       if (itemMeta == null) return itemstack; 
/* 398 */       itemMeta.setDisplayName(name);
/* 399 */       itemstack.setItemMeta(itemMeta);
/* 400 */       return itemstack;
/*     */     } 
/*     */     
/* 403 */     ItemStack clone = new ItemStack(Material.STONE);
/* 404 */     ItemMeta meta = clone.hasItemMeta() ? clone.getItemMeta() : Bukkit.getItemFactory().getItemMeta(Material.STONE);
/* 405 */     if (meta == null) return itemstack; 
/* 406 */     meta.setDisplayName(name);
/* 407 */     clone.setItemMeta(meta);
/* 408 */     ItemStack isc = CraftItemStack.asNMSCopy(clone);
/* 409 */     String temp_name = "";
/* 410 */     if (isc != null) {
/* 411 */       NBTTagCompound tag = isc.t() ? isc.u() : new NBTTagCompound();
/* 412 */       if (tag != null) {
/* 413 */         NBTTagCompound display = tag.b("display", 10) ? tag.p("display") : new NBTTagCompound();
/* 414 */         if (display != null && 
/* 415 */           display.b("Name", 8)) {
/* 416 */           temp_name = display.l("Name");
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 422 */     ItemStack is = CraftItemStack.asNMSCopy(itemstack);
/* 423 */     if (is != null) {
/* 424 */       NBTTagCompound tag = (is.t() && is.u() != null) ? is.u() : new NBTTagCompound();
/* 425 */       if (!tag.b("display", 10)) tag.a("display", (NBTBase)new NBTTagCompound()); 
/* 426 */       NBTTagCompound display = tag.p("display");
/* 427 */       if (display == null) display = new NBTTagCompound(); 
/* 428 */       display.a("Name", temp_name);
/* 429 */       tag.a("display", (NBTBase)display);
/* 430 */       is.c(tag);
/* 431 */       return CraftItemStack.asBukkitCopy(is);
/*     */     } 
/* 433 */     return itemstack;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasPersistentDataKey(ItemStack itemstack, String key) {
/* 438 */     ItemStack is = CraftItemStack.asNMSCopy(itemstack);
/* 439 */     if (is != null) {
/* 440 */       NBTTagCompound tag = (is.t() && is.u() != null) ? is.u() : new NBTTagCompound();
/* 441 */       if (tag.b("PublicBukkitValues", 10)) {
/* 442 */         NBTTagCompound pbv = tag.p("PublicBukkitValues");
/* 443 */         if (pbv != null) {
/* 444 */           return pbv.e(key);
/*     */         }
/*     */       } 
/*     */     } 
/* 448 */     return false;
/*     */   }
/*     */   
/*     */   public Object getPersistentDataKey(ItemStack itemstack, String key) {
/* 452 */     ItemStack is = CraftItemStack.asNMSCopy(itemstack);
/* 453 */     if (is != null) {
/* 454 */       NBTTagCompound tag = (is.t() && is.u() != null) ? is.u() : new NBTTagCompound();
/* 455 */       if (tag.b("PublicBukkitValues", 10)) {
/* 456 */         NBTTagCompound pbv = tag.p("PublicBukkitValues");
/* 457 */         if (pbv != null && 
/* 458 */           pbv.e(key)) {
/* 459 */           Object value = pbv.c(key);
/* 460 */           if (value instanceof NBTTagString)
/* 461 */             return ((NBTTagString)value).e_(); 
/* 462 */           if (value instanceof NBTTagByte)
/* 463 */             return Byte.valueOf(((NBTTagByte)value).h()); 
/* 464 */           if (value instanceof NBTTagShort)
/* 465 */             return Short.valueOf(((NBTTagShort)value).g()); 
/* 466 */           if (value instanceof NBTTagInt)
/* 467 */             return Integer.valueOf(((NBTTagInt)value).f()); 
/* 468 */           if (value instanceof NBTTagLong)
/* 469 */             return Long.valueOf(((NBTTagLong)value).e()); 
/* 470 */           if (value instanceof NBTTagFloat)
/* 471 */             return Float.valueOf(((NBTTagFloat)value).j()); 
/* 472 */           if (value instanceof NBTTagDouble) {
/* 473 */             return Double.valueOf(((NBTTagDouble)value).i());
/*     */           }
/* 475 */           AuctionHouse.logger.info("Unsupported persistent data key type!");
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 481 */     return null;
/*     */   }
/*     */   
/*     */   public Map<String, Object> getPersistentData(ItemStack itemstack) {
/* 485 */     ItemStack is = CraftItemStack.asNMSCopy(itemstack);
/* 486 */     Map<String, Object> persistentDataKeys = new HashMap<>();
/* 487 */     if (is != null) {
/* 488 */       NBTTagCompound tag = (is.t() && is.u() != null) ? is.u() : new NBTTagCompound();
/* 489 */       if (tag.b("PublicBukkitValues", 10)) {
/* 490 */         NBTTagCompound pbv = tag.p("PublicBukkitValues");
/* 491 */         if (pbv != null) {
/*     */           
/* 493 */           Set<String> keys = pbv.d();
/* 494 */           for (String key : keys) {
/* 495 */             Object value = pbv.c(key);
/* 496 */             if (value instanceof NBTTagString) {
/* 497 */               String v = ((NBTTagString)value).e_();
/* 498 */               persistentDataKeys.put(key, v); continue;
/* 499 */             }  if (value instanceof NBTTagByte) {
/* 500 */               Byte v = Byte.valueOf(((NBTTagByte)value).h());
/* 501 */               persistentDataKeys.put(key, v); continue;
/* 502 */             }  if (value instanceof NBTTagShort) {
/* 503 */               Short v = Short.valueOf(((NBTTagShort)value).g());
/* 504 */               persistentDataKeys.put(key, v); continue;
/* 505 */             }  if (value instanceof NBTTagInt) {
/* 506 */               Integer v = Integer.valueOf(((NBTTagInt)value).f());
/* 507 */               persistentDataKeys.put(key, v); continue;
/* 508 */             }  if (value instanceof NBTTagLong) {
/* 509 */               Long v = Long.valueOf(((NBTTagLong)value).e());
/* 510 */               persistentDataKeys.put(key, v); continue;
/* 511 */             }  if (value instanceof NBTTagFloat) {
/* 512 */               Float v = Float.valueOf(((NBTTagFloat)value).j());
/* 513 */               persistentDataKeys.put(key, v); continue;
/* 514 */             }  if (value instanceof NBTTagDouble) {
/* 515 */               Double v = Double.valueOf(((NBTTagDouble)value).i());
/* 516 */               persistentDataKeys.put(key, v); continue;
/*     */             } 
/* 518 */             AuctionHouse.logger.info("Unsupported persistent data key type!");
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 524 */     return persistentDataKeys;
/*     */   }
/*     */   
/*     */   public ItemStack setPersistentDataKey(ItemStack itemstack, String key, Object value) {
/* 528 */     ItemStack is = CraftItemStack.asNMSCopy(itemstack);
/* 529 */     if (is == null) return itemstack; 
/* 530 */     NBTTagCompound tag = (is.t() && is.u() != null) ? is.u() : new NBTTagCompound();
/* 531 */     NBTTagCompound pbv = tag.b("PublicBukkitValues", 10) ? tag.p("PublicBukkitValues") : new NBTTagCompound();
/* 532 */     if (value instanceof String) {
/* 533 */       String v = (String)value;
/* 534 */       pbv.a(key, v);
/* 535 */     } else if (value instanceof Boolean || value instanceof Byte) {
/* 536 */       byte v = ((Byte)value).byteValue();
/* 537 */       pbv.a(key, v);
/* 538 */     } else if (value instanceof Short) {
/* 539 */       short v = ((Short)value).shortValue();
/* 540 */       pbv.a(key, v);
/* 541 */     } else if (value instanceof Integer) {
/* 542 */       int v = ((Integer)value).intValue();
/* 543 */       pbv.a(key, v);
/* 544 */     } else if (value instanceof Long) {
/* 545 */       long v = ((Long)value).longValue();
/* 546 */       pbv.a(key, v);
/* 547 */     } else if (value instanceof Float) {
/* 548 */       float v = ((Float)value).floatValue();
/* 549 */       pbv.a(key, v);
/* 550 */     } else if (value instanceof Double) {
/* 551 */       double v = ((Double)value).doubleValue();
/* 552 */       pbv.a(key, v);
/*     */     } else {
/* 554 */       AuctionHouse.logger.info("Unsupported persistent data key type!");
/* 555 */       return itemstack;
/*     */     } 
/* 557 */     tag.a("PublicBukkitValues", (NBTBase)pbv);
/* 558 */     is.c(tag);
/* 559 */     return CraftItemStack.asBukkitCopy(is);
/*     */   }
/*     */   
/*     */   public ItemStack deserialize(String item) {
/* 563 */     if (item != null) {
/*     */       
/* 565 */       byte[] bytes = Base64.getDecoder().decode(item);
/* 566 */       ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
/*     */       try {
/* 568 */         NBTTagCompound nbt = NBTCompressedStreamTools.a(inputStream);
/* 569 */         if (Config.debug) AuctionHouse.logger.info(String.format("nbt = %s", new Object[] { nbt })); 
/* 570 */         NBTTagCompound tag = updateNBT(nbt);
/* 571 */         ItemStack is = ItemStack.a(tag);
/* 572 */         return CraftItemStack.asBukkitCopy(is);
/* 573 */       } catch (IOException e) {
/* 574 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/* 577 */     return null;
/*     */   }
/*     */   
/*     */   public ItemStack updateItem(ItemStack item) {
/* 581 */     if (item != null) {
/* 582 */       ItemStack is = CraftItemStack.asNMSCopy(item);
/* 583 */       if (is != null) {
/*     */ 
/*     */         
/* 586 */         NBTTagCompound nbt_old = is.b(new NBTTagCompound());
/* 587 */         NBTTagCompound nbt = updateNBT(nbt_old);
/* 588 */         is = ItemStack.a(nbt);
/* 589 */         return CraftItemStack.asBukkitCopy(is);
/*     */       } 
/* 591 */       return item;
/*     */     } 
/*     */     
/* 594 */     return null;
/*     */   }
/*     */   
/*     */   private NBTTagCompound updateNBT(NBTTagCompound nbt) {
/* 598 */     if (nbt.b("id", 8) && nbt.b("Damage", 2)) {
/*     */       
/* 600 */       String id = nbt.l("id");
/* 601 */       short damage = nbt.g("Damage");
/* 602 */       String result = ItemUtil.flatten(id, damage);
/* 603 */       if (!result.equals(id)) {
/* 604 */         nbt.a("id", result);
/* 605 */         nbt.a("Damage", (short)0);
/*     */       } 
/*     */     } 
/* 608 */     if (nbt.b("tag", 10)) {
/* 609 */       NBTTagCompound tag = nbt.p("tag");
/* 610 */       if (tag.b("display", 10)) {
/* 611 */         NBTTagCompound display = tag.p("display");
/* 612 */         if (display.b("Name", 8)) {
/*     */           
/* 614 */           String name = display.l("Name");
/* 615 */           String json = String.format("{\"text\":\"%s\"}", new Object[] { name });
/* 616 */           display.a("Name", json);
/*     */         } 
/* 618 */         if (display.b("Lore", 9)) {
/*     */           
/* 620 */           NBTTagList lore = display.c("Lore", 8);
/* 621 */           for (NBTBase base : lore) {
/* 622 */             if (base instanceof NBTTagString) {
/* 623 */               NBTTagString line = (NBTTagString)base;
/* 624 */               String json = String.format("{\"text\":\"%s\"}", new Object[] { line.e_() });
/* 625 */               lore.set(lore.indexOf(base), NBTTagString.a(json));
/*     */             } 
/*     */           } 
/*     */         } 
/* 629 */         tag.a("display", (NBTBase)display);
/*     */       } 
/* 631 */       if (tag.b("ench", 9)) {
/*     */         
/* 633 */         NBTTagList ench = tag.c("ench", 10);
/* 634 */         Map<String, Short> listEnchants = new HashMap<>();
/* 635 */         for (NBTBase base : ench) {
/* 636 */           if (base instanceof NBTTagCompound) {
/* 637 */             NBTTagCompound enchantment = (NBTTagCompound)base;
/* 638 */             if (enchantment.b("id", 2) && enchantment.b("lvl", 2)) {
/* 639 */               short id = enchantment.g("id");
/* 640 */               short lvl = enchantment.g("lvl");
/* 641 */               Enchantment en = ItemUtil.getEnchantmentById(id);
/* 642 */               if (en != null) {
/* 643 */                 listEnchants.put(en.getKey().toString(), Short.valueOf(lvl));
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/* 648 */         tag.r("ench");
/* 649 */         if (tag.b("Enchantments", 9)) {
/* 650 */           NBTTagList nBTTagList = tag.c("Enchantments", 10);
/* 651 */           for (NBTBase base : nBTTagList) {
/* 652 */             if (base instanceof NBTTagCompound) {
/* 653 */               NBTTagCompound enchantment = (NBTTagCompound)base;
/* 654 */               if (enchantment.b("id", 8) && enchantment.b("lvl", 2)) {
/* 655 */                 String id = enchantment.l("id");
/* 656 */                 short lvl = enchantment.g("lvl");
/* 657 */                 if (!listEnchants.containsKey(id)) {
/* 658 */                   listEnchants.put(id, Short.valueOf(lvl));
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/* 664 */         tag.r("Enchantments");
/* 665 */         NBTTagList enchantments = new NBTTagList();
/* 666 */         for (String key : listEnchants.keySet()) {
/* 667 */           short lvl = ((Short)listEnchants.get(key)).shortValue();
/* 668 */           NBTTagCompound entry = new NBTTagCompound();
/* 669 */           entry.a("id", key);
/* 670 */           entry.a("lvl", lvl);
/* 671 */           enchantments.add(entry);
/*     */         } 
/* 673 */         tag.a("Enchantments", (NBTBase)enchantments);
/*     */       } 
/* 675 */       nbt.a("tag", (NBTBase)tag);
/*     */     } 
/* 677 */     return nbt;
/*     */   }
/*     */ 
/*     */   
/*     */   public String parseInternal(String internal) {
/* 682 */     if (internal != null) {
/* 683 */       ByteArrayInputStream buf = new ByteArrayInputStream(Base64.getDecoder().decode(internal));
/*     */       try {
/* 685 */         NBTTagCompound internalTag = NBTCompressedStreamTools.a(buf);
/* 686 */         return internalTag.toString();
/* 687 */       } catch (IOException ex) {
/* 688 */         AuctionHouse.logger.log(Level.SEVERE, (String)null, ex);
/*     */       } 
/*     */     } 
/* 691 */     return internal;
/*     */   }
/*     */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\nms\v1_19_R1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */