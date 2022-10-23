/*     */ package com.spawnchunk.auctionhouse.storage;
/*     */ 
/*     */ import com.spawnchunk.auctionhouse.AuctionHouse;
/*     */ import com.spawnchunk.auctionhouse.config.Config;
/*     */ import com.spawnchunk.auctionhouse.modules.Listing;
/*     */ import com.spawnchunk.auctionhouse.modules.ListingType;
/*     */ import com.spawnchunk.auctionhouse.util.FileUtil;
/*     */ import com.spawnchunk.auctionhouse.util.MessageUtil;
/*     */ import java.io.File;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.bukkit.configuration.InvalidConfigurationException;
/*     */ import org.bukkit.configuration.file.YamlConfiguration;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ 
/*     */ public class DatabaseStorage {
/*  27 */   static String database = "auctions.db";
/*  28 */   static String path = AuctionHouse.plugin.getDataFolder().getAbsolutePath();
/*  29 */   static String set_pragma = "PRAGMA auto_vacuum = FULL;";
/*  30 */   static String check_table = "SELECT name FROM sqlite_master WHERE type='table' AND name='listings';";
/*  31 */   static String create_table = "CREATE TABLE listings (id INT PRIMARY KEY NOT NULL,world TEXT,seller_uuid TEXT,buyer_uuid TEXT,bidder_uuid TEXT,price REAL,reserve REAL,bid REAL,listing_type TEXT,item BLOB);";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  43 */   static String check_table_for_type = "PRAGMA table_info('listings');";
/*  44 */   static String alter_table_for_type = "ALTER TABLE listings ADD COLUMN listing_type STRING DEFAULT 'PLAYER_LISTING';";
/*  45 */   static String vacuum = "VACUUM;";
/*  46 */   static String select_all_rows = "SELECT * FROM listings;";
/*     */   
/*  48 */   static String check_row = "SELECT * FROM listings WHERE id = %d;";
/*  49 */   static String insert_row = "INSERT into listings (id, world, seller_uuid, buyer_uuid, bidder_uuid, price, reserve, bid, listing_type, item) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
/*     */ 
/*     */   
/*  52 */   static String update_row = "UPDATE listings SET id = ?, world = ?, seller_uuid = ?, buyer_uuid = ?, bidder_uuid = ?, price = ?, reserve = ?, bid = ?, listing_type = ?, item = ? WHERE id = ?;";
/*     */ 
/*     */ 
/*     */   
/*  56 */   static String delete_row = "DELETE FROM listings WHERE id = %d;";
/*  57 */   static String delete_all_rows = "DELETE FROM listings;";
/*     */   
/*     */   public static Connection getConnection() {
/*     */     Connection conn;
/*  61 */     boolean init = false;
/*  62 */     String url = path + path + File.separator;
/*  63 */     File db = new File(url);
/*  64 */     if (db.exists()) {
/*  65 */       if (FileUtil.backupFile(db)) {
/*  66 */         MessageUtil.logWarning("A backup of the database has been saved with .backup extension.");
/*     */       } else {
/*  68 */         MessageUtil.logSevere("Error! Could not backup database file");
/*     */       } 
/*     */     } else {
/*  71 */       init = true;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/*  76 */       conn = DriverManager.getConnection("jdbc:sqlite:" + url);
/*  77 */       if (init) AuctionHouse.logger.info("Created database");
/*     */       
/*     */       try {
/*  80 */         Statement stmt = conn.createStatement();
/*  81 */         stmt.execute(set_pragma);
/*  82 */         stmt.close();
/*  83 */       } catch (SQLException e) {
/*  84 */         e.printStackTrace();
/*     */       } 
/*     */       
/*     */       try {
/*  88 */         Statement stmt2 = conn.createStatement();
/*  89 */         ResultSet rs2 = stmt2.executeQuery(check_table);
/*  90 */         if (!rs2.next()) {
/*     */           
/*  92 */           try { Statement stmt3 = conn.createStatement(); 
/*  93 */             try { stmt3.executeUpdate(create_table);
/*  94 */               AuctionHouse.logger.info("Created tables");
/*  95 */               if (stmt3 != null) stmt3.close();  } catch (Throwable throwable) { if (stmt3 != null) try { stmt3.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (SQLException e)
/*  96 */           { e.printStackTrace(); }
/*     */         
/*     */         }
/*  99 */         stmt2.close();
/* 100 */       } catch (SQLException e) {
/* 101 */         e.printStackTrace();
/*     */       } 
/*     */       
/*     */       try {
/* 105 */         Statement stmt4 = conn.createStatement();
/* 106 */         ResultSet rs4 = stmt4.executeQuery(check_table_for_type);
/* 107 */         int count = 0;
/* 108 */         while (rs4.next()) {
/* 109 */           if (rs4.getString("name").equalsIgnoreCase("listing_type")) {
/* 110 */             count++;
/*     */           }
/*     */         } 
/* 113 */         if (count == 0) {
/*     */           
/* 115 */           Statement stmt5 = conn.createStatement();
/* 116 */           stmt5.executeUpdate(alter_table_for_type);
/* 117 */           AuctionHouse.logger.info("Updated table");
/* 118 */           stmt5.close();
/*     */         } 
/* 120 */         stmt4.close();
/* 121 */       } catch (SQLException e) {
/* 122 */         e.printStackTrace();
/*     */       } 
/*     */       
/*     */       try {
/* 126 */         Statement stmt6 = conn.createStatement();
/* 127 */         if (stmt6.execute(vacuum)) {
/* 128 */           AuctionHouse.logger.info("Packed database");
/*     */         }
/* 130 */         stmt6.close();
/* 131 */       } catch (SQLException e) {
/* 132 */         e.printStackTrace();
/*     */       } 
/* 134 */     } catch (SQLException e) {
/* 135 */       e.printStackTrace();
/* 136 */       conn = null;
/*     */     } 
/* 138 */     return conn;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean createListing(Connection conn, long key, Listing value) {
/* 143 */     String item_string, world = value.getWorld();
/* 144 */     String seller_uuid = (value.getSeller_UUID() != null) ? value.getSeller_UUID() : "";
/* 145 */     String buyer_uuid = (value.getBuyer_UUID() != null) ? value.getBuyer_UUID() : "";
/* 146 */     String bidder_uuid = (value.getBidder_UUID() != null) ? value.getBidder_UUID() : "";
/* 147 */     float price = value.getPrice();
/* 148 */     float reserve = value.getReserve();
/* 149 */     float bid = value.getBid();
/* 150 */     ListingType type = value.getType();
/* 151 */     String listing_type = type.name();
/* 152 */     ItemStack item = value.getItem();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 161 */       YamlConfiguration config = new YamlConfiguration();
/* 162 */       config.set("item", item);
/* 163 */       item_string = config.saveToString();
/* 164 */     } catch (YAMLException e) {
/* 165 */       String seller = type.isServer() ? AuctionHouse.servername : ((value.getSeller() != null) ? value.getSeller().getName() : value.getSeller_UUID());
/* 166 */       MessageUtil.logWarning(String.format("Warning! - A YAML exception occurred while %s tried to list a %s", new Object[] { seller, item.getType().getKey().getKey() }));
/* 167 */       MessageUtil.logWarning("This may be the result of a serialization bug or an attempted exploit by using a hacked client.");
/* 168 */       if (Config.debug) e.printStackTrace(); 
/* 169 */       return false;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 177 */     byte[] item_blob = item_string.getBytes(StandardCharsets.UTF_8);
/*     */     
/* 179 */     boolean result = false;
/* 180 */     boolean exists = false;
/*     */ 
/*     */     
/* 183 */     try { Statement stmt = conn.createStatement(); 
/* 184 */       try { ResultSet rs = stmt.executeQuery(String.format(check_row, new Object[] { Long.valueOf(key) }));
/*     */         
/* 186 */         try { exists = rs.next();
/* 187 */           if (rs != null) rs.close();  } catch (Throwable throwable) { if (rs != null) try { rs.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (stmt != null) stmt.close();  } catch (Throwable throwable) { if (stmt != null) try { stmt.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (SQLException e)
/*     */     
/* 189 */     { e.printStackTrace(); }
/*     */     
/* 191 */     if (exists) {
/* 192 */       MessageUtil.logWarning("Record already exists!");
/*     */     } else {
/*     */       
/* 195 */       try { PreparedStatement prep = conn.prepareStatement(insert_row); 
/* 196 */         try { conn.setAutoCommit(true);
/* 197 */           prep.setLong(1, key);
/* 198 */           prep.setString(2, world);
/* 199 */           prep.setString(3, seller_uuid);
/* 200 */           prep.setString(4, buyer_uuid);
/* 201 */           prep.setString(5, bidder_uuid);
/* 202 */           prep.setFloat(6, price);
/* 203 */           prep.setFloat(7, reserve);
/* 204 */           prep.setFloat(8, bid);
/* 205 */           prep.setString(9, listing_type);
/* 206 */           prep.setBytes(10, item_blob);
/* 207 */           prep.executeUpdate();
/* 208 */           if (Config.debug) AuctionHouse.logger.info(String.format("Created listing %d: (item = %dx %s, world = %s, seller = %s, buyer = %s, price = %.2f, reserve = %.2f, bid = %.2f, listing_type = %s)", new Object[] {
/* 209 */                     Long.valueOf(key), Integer.valueOf(item.getAmount()), item.getType().name(), world, seller_uuid, buyer_uuid, Float.valueOf(price), Float.valueOf(reserve), Float.valueOf(bid), listing_type })); 
/* 210 */           result = true;
/* 211 */           if (prep != null) prep.close();  } catch (Throwable throwable) { if (prep != null) try { prep.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (SQLException e)
/* 212 */       { e.printStackTrace(); }
/*     */     
/*     */     } 
/* 215 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void readAllListings(Connection conn) {
/*     */     try {
/* 221 */       Statement stmt = conn.createStatement();
/* 222 */       ResultSet rs = stmt.executeQuery(select_all_rows);
/*     */       
/* 224 */       while (rs.next()) {
/*     */         
/* 226 */         long id = rs.getLong("id");
/* 227 */         String world = rs.getString("world");
/* 228 */         String seller_uuid = rs.getString("seller_uuid");
/* 229 */         String buyer_uuid = rs.getString("buyer_uuid");
/* 230 */         String bidder_uuid = rs.getString("bidder_uuid");
/* 231 */         float price = rs.getFloat("price");
/* 232 */         float reserve = rs.getFloat("reserve");
/* 233 */         float bid = rs.getFloat("bid");
/* 234 */         String listing_type = rs.getString("listing_type");
/* 235 */         ListingType type = (listing_type != null && !listing_type.isEmpty()) ? ListingType.valueOf(listing_type) : ListingType.PLAYER_LISTING;
/* 236 */         byte[] item_blob = rs.getBytes("item");
/*     */ 
/*     */         
/* 239 */         String item_string = new String(item_blob, StandardCharsets.UTF_8);
/*     */ 
/*     */         
/* 242 */         if (Config.debug) {
/* 243 */           AuctionHouse.logger.info(String.format("read item = %s", new Object[] { item_string }));
/* 244 */           Pattern p = Pattern.compile("internal: .*");
/* 245 */           Matcher m = p.matcher(item_string);
/* 246 */           if (m.find()) {
/* 247 */             String tag = m.group().replace("internal: ", "");
/* 248 */             AuctionHouse.logger.info(String.format("tag = %s", new Object[] { AuctionHouse.nms.parseInternal(tag) }));
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 253 */         YamlConfiguration config = new YamlConfiguration();
/* 254 */         ItemStack item = null;
/*     */         try {
/* 256 */           config.loadFromString(item_string);
/* 257 */           item = config.getItemStack("item", null);
/* 258 */         } catch (InvalidConfigurationException e) {
/* 259 */           e.printStackTrace();
/*     */         } 
/* 261 */         if (item != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 272 */           Listing listing = new Listing(world, seller_uuid, buyer_uuid, bidder_uuid, price, reserve, bid, type, item);
/*     */           
/* 274 */           AuctionHouse.listings.putListing(listing, id);
/* 275 */           if (Config.debug) {
/* 276 */             AuctionHouse.logger.info(String.format("Read listing %d: (item = %dx %s, world = %s, seller = %s, buyer = %s, price = %.2f, reserve = %.2f, bid = %.2f, listing_type = %s)", new Object[] {
/* 277 */                     Long.valueOf(id), Integer.valueOf(item.getAmount()), item.getType().getKey(), world, seller_uuid, buyer_uuid, Float.valueOf(price), Float.valueOf(reserve), Float.valueOf(bid), listing_type
/*     */                   }));
/*     */           }
/*     */         } 
/*     */       } 
/* 282 */       stmt.close();
/* 283 */       int count = AuctionHouse.listings.count();
/* 284 */       AuctionHouse.logger.info(String.format("Loaded %d listing%s", new Object[] { Integer.valueOf(count), (count != 1) ? "s" : "" }));
/*     */     }
/* 286 */     catch (SQLException e) {
/* 287 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void updateListing(Connection conn, long key, long id, Listing value) {
/* 293 */     String world = value.getWorld();
/* 294 */     String seller_uuid = (value.getSeller_UUID() != null) ? value.getSeller_UUID() : "";
/* 295 */     String buyer_uuid = (value.getBuyer_UUID() != null) ? value.getBuyer_UUID() : "";
/* 296 */     String bidder_uuid = (value.getBidder_UUID() != null) ? value.getBidder_UUID() : "";
/* 297 */     float price = value.getPrice();
/* 298 */     float reserve = value.getReserve();
/* 299 */     float bid = value.getBid();
/* 300 */     ListingType type = value.getType();
/* 301 */     String listing_type = type.name();
/* 302 */     ItemStack item = value.getItem();
/* 303 */     YamlConfiguration config = new YamlConfiguration();
/* 304 */     config.set("item", item);
/*     */     
/* 306 */     String item_string = config.saveToString();
/* 307 */     byte[] item_blob = item_string.getBytes(StandardCharsets.UTF_8);
/*     */     
/* 309 */     String query = String.format(check_row, new Object[] { Long.valueOf(key) });
/* 310 */     boolean exists = false;
/*     */     
/*     */     try {
/* 313 */       Statement stmt = conn.createStatement();
/* 314 */       ResultSet rs = stmt.executeQuery(query);
/* 315 */       exists = rs.next();
/* 316 */       stmt.close();
/* 317 */     } catch (SQLException e) {
/* 318 */       e.printStackTrace();
/*     */     } 
/* 320 */     if (!exists)
/* 321 */     { MessageUtil.logWarning("Could not find record!"); }
/*     */     else { 
/* 323 */       try { PreparedStatement prep = conn.prepareStatement(update_row); 
/* 324 */         try { conn.setAutoCommit(true);
/* 325 */           prep.setLong(1, id);
/* 326 */           prep.setString(2, world);
/* 327 */           prep.setString(3, seller_uuid);
/* 328 */           prep.setString(4, buyer_uuid);
/* 329 */           prep.setString(5, bidder_uuid);
/* 330 */           prep.setFloat(6, price);
/* 331 */           prep.setFloat(7, reserve);
/* 332 */           prep.setFloat(8, bid);
/* 333 */           prep.setString(9, listing_type);
/* 334 */           prep.setBytes(10, item_blob);
/* 335 */           prep.setLong(11, key);
/* 336 */           prep.executeUpdate();
/* 337 */           if (Config.debug) AuctionHouse.logger.info(String.format("Updated listing %d: (item = %dx %s, world = %s, seller = %s, buyer = %s, price = %.2f, reserve = %.2f, bid = %.2f, listing_type = %s)", new Object[] {
/* 338 */                     Long.valueOf(key), Integer.valueOf(item.getAmount()), item.getType().getKey(), world, seller_uuid, buyer_uuid, Float.valueOf(price), Float.valueOf(reserve), Float.valueOf(bid), listing_type })); 
/* 339 */           if (prep != null) prep.close();  } catch (Throwable throwable) { if (prep != null) try { prep.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (SQLException e)
/* 340 */       { e.printStackTrace(); }
/*     */        }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public static void deleteListing(Connection conn, long key) {
/*     */     try {
/* 348 */       Statement stmt = conn.createStatement();
/*     */       
/* 350 */       stmt.executeUpdate(String.format(delete_row, new Object[] { Long.valueOf(key) }));
/* 351 */       stmt.close();
/* 352 */     } catch (SQLException e) {
/* 353 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void deleteListings(Connection conn, List<Long> keys) {
/*     */     try {
/* 360 */       Statement stmt = conn.createStatement();
/*     */       
/* 362 */       for (Iterator<Long> iterator = keys.iterator(); iterator.hasNext(); ) { long key = ((Long)iterator.next()).longValue(); stmt.executeUpdate(String.format(delete_row, new Object[] { Long.valueOf(key) })); }
/* 363 */        stmt.close();
/* 364 */     } catch (SQLException e) {
/* 365 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void deleteAllListings(Connection conn) {
/* 372 */     if (Config.debug) AuctionHouse.logger.info("deleteAllListings()"); 
/*     */     try {
/* 374 */       Statement stmt = conn.createStatement();
/*     */       
/* 376 */       stmt.executeUpdate(delete_all_rows);
/* 377 */       stmt.close();
/* 378 */     } catch (SQLException e) {
/* 379 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\storage\DatabaseStorage.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */