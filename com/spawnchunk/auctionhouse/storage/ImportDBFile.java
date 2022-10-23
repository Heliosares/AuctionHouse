/*     */ package com.spawnchunk.auctionhouse.storage;
/*     */ import com.spawnchunk.auctionhouse.AuctionHouse;
/*     */ import com.spawnchunk.auctionhouse.modules.Listing;
/*     */ import com.spawnchunk.auctionhouse.modules.ListingType;
/*     */ import com.spawnchunk.auctionhouse.util.ItemUtil;
/*     */ import com.spawnchunk.auctionhouse.util.TimeUtil;
/*     */ import java.io.File;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ 
/*     */ public class ImportDBFile {
/*  16 */   static String path = AuctionHouse.plugin.getDataFolder().getAbsolutePath();
/*  17 */   static String check_table_listings = "SELECT name FROM sqlite_master WHERE type='table' AND name='listings';";
/*  18 */   static String check_table_expired = "SELECT name FROM sqlite_master WHERE type='table' AND name='expired';";
/*  19 */   static String check_table_sell_log = "SELECT name FROM sqlite_master WHERE type='table' AND name='sell_log';";
/*  20 */   static String select_all_listings = "SELECT * FROM listings;";
/*  21 */   static String select_all_expired = "SELECT * FROM expired;";
/*  22 */   static String select_all_sell_log = "SELECT * FROM sell_log;";
/*     */ 
/*     */ 
/*     */   
/*     */   public static void importData() {
/*  27 */     String url = path + path + "data.db";
/*  28 */     String url2 = path + path + "data.db.old";
/*  29 */     File db = new File(url);
/*  30 */     if (db.exists()) {
/*  31 */       AuctionHouse.logger.info("Found existing data.db, attempting import...");
/*     */ 
/*     */       
/*     */       try {
/*  35 */         Connection conn = DriverManager.getConnection("jdbc:sqlite:" + url);
/*     */ 
/*     */         
/*     */         try {
/*  39 */           Statement stmt = conn.createStatement();
/*  40 */           ResultSet rs = stmt.executeQuery(check_table_listings);
/*  41 */           if (rs.next()) {
/*     */             
/*     */             try {
/*  44 */               Statement stmt2 = conn.createStatement();
/*  45 */               ResultSet rs2 = stmt2.executeQuery(select_all_listings);
/*  46 */               int count = 0;
/*  47 */               int error = 0;
/*     */               
/*  49 */               while (rs2.next()) {
/*     */                 
/*  51 */                 String uuid = rs2.getString("id");
/*  52 */                 if (uuid != null) {
/*  53 */                   String world = AuctionHouse.levelname;
/*  54 */                   String seller_uuid = rs2.getString("listed_by");
/*  55 */                   String buyer_uuid = "";
/*  56 */                   String bidder_uuid = "";
/*  57 */                   float reserve = 0.0F;
/*  58 */                   float bid = 0.0F;
/*  59 */                   float price = (float)rs2.getLong("price");
/*  60 */                   long id = rs2.getLong("end_time");
/*  61 */                   ListingType type = ListingType.PLAYER_LISTING;
/*  62 */                   String listing_type = type.name();
/*  63 */                   ItemStack item = AuctionHouse.nms.deserialize(rs2.getString("item"));
/*  64 */                   Listing listing = new Listing(world, seller_uuid, buyer_uuid, bidder_uuid, price, reserve, bid, type, item);
/*     */                   
/*  66 */                   boolean result = AuctionHouse.listings.importListing(listing, id);
/*  67 */                   if (result) {
/*  68 */                     count++;
/*     */                   } else {
/*  70 */                     error++;
/*     */                   } 
/*  72 */                   AuctionHouse.logger.info(String.format("Read listing %d: (name = %s, item = %dx %s, lore = %s, enchants = %s, world = %s, seller = %s, buyer = %s, price = %.2f, reserve = %.2f, bid = %.2f, listing_type = %s)", new Object[] { 
/*  73 */                           Long.valueOf(id), ItemUtil.getName(item), Integer.valueOf(item.getAmount()), item.getType().getKey(), ItemUtil.getLoreString(item), ItemUtil.getEnchantsString(item), world, seller_uuid, buyer_uuid, Float.valueOf(price), Float.valueOf(reserve), Float.valueOf(bid), listing_type }));
/*     */                 } 
/*     */               } 
/*  76 */               stmt2.close();
/*  77 */               AuctionHouse.logger.info(String.format("Imported %d listing%s", new Object[] { Integer.valueOf(count), (count != 1) ? "s" : "" }));
/*  78 */               if (error > 0) AuctionHouse.logger.info(String.format("Could not read %d listing%s", new Object[] { Integer.valueOf(error), (error != 1) ? "s" : "" })); 
/*  79 */             } catch (SQLException e) {
/*  80 */               e.printStackTrace();
/*     */             } 
/*     */           }
/*  83 */           stmt.close();
/*  84 */         } catch (SQLException sQLException) {}
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/*  89 */           Statement stmt3 = conn.createStatement();
/*  90 */           ResultSet rs3 = stmt3.executeQuery(check_table_expired);
/*  91 */           if (rs3.next()) {
/*     */             
/*     */             try {
/*  94 */               Statement stmt4 = conn.createStatement();
/*  95 */               ResultSet rs4 = stmt4.executeQuery(select_all_expired);
/*  96 */               int count = 0;
/*  97 */               int error = 0;
/*     */               
/*  99 */               while (rs4.next()) {
/*     */                 
/* 101 */                 String uuid = rs4.getString("id");
/* 102 */                 if (uuid != null) {
/* 103 */                   String world = AuctionHouse.levelname;
/* 104 */                   String seller_uuid = rs4.getString("player");
/* 105 */                   String buyer_uuid = "";
/* 106 */                   String bidder_uuid = "";
/* 107 */                   float reserve = 0.0F;
/* 108 */                   float bid = 0.0F;
/* 109 */                   float price = 0.0F;
/*     */                   
/* 111 */                   long id = TimeUtil.now();
/* 112 */                   ListingType type = ListingType.PLAYER_LISTING;
/* 113 */                   String listing_type = type.name();
/* 114 */                   ItemStack item = AuctionHouse.nms.deserialize(rs4.getString("item"));
/* 115 */                   Listing listing = new Listing(world, seller_uuid, buyer_uuid, bidder_uuid, price, reserve, bid, type, item);
/*     */                   
/* 117 */                   boolean result = AuctionHouse.listings.importListing(listing, id);
/* 118 */                   if (result) {
/* 119 */                     count++;
/*     */                   } else {
/* 121 */                     error++;
/*     */                   } 
/* 123 */                   AuctionHouse.logger.info(String.format("Read expired listing %d: (name = %s, item = %dx %s, lore = %s, enchants = %s, world = %s, seller = %s, buyer = %s, price = %.2f, reserve = %.2f, bid = %.2f, listing_type = %s)", new Object[] { 
/* 124 */                           Long.valueOf(id), ItemUtil.getName(item), Integer.valueOf(item.getAmount()), item.getType().getKey(), ItemUtil.getLoreString(item), ItemUtil.getEnchantsString(item), world, seller_uuid, buyer_uuid, Float.valueOf(price), Float.valueOf(reserve), Float.valueOf(bid), listing_type }));
/*     */                 } 
/*     */               } 
/* 127 */               stmt4.close();
/* 128 */               AuctionHouse.logger.info(String.format("Imported %d expired listing%s", new Object[] { Integer.valueOf(count), (count != 1) ? "s" : "" }));
/* 129 */               if (error > 0) AuctionHouse.logger.info(String.format("Could not read %d expired listing%s", new Object[] { Integer.valueOf(error), (error != 1) ? "s" : "" })); 
/* 130 */             } catch (SQLException e) {
/* 131 */               e.printStackTrace();
/*     */             } 
/*     */           }
/* 134 */           stmt3.close();
/* 135 */         } catch (SQLException sQLException) {}
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 140 */           Statement stmt5 = conn.createStatement();
/* 141 */           ResultSet rs5 = stmt5.executeQuery(check_table_sell_log);
/* 142 */           if (rs5.next()) {
/*     */             
/*     */             try {
/* 145 */               Statement stmt6 = conn.createStatement();
/* 146 */               ResultSet rs6 = stmt6.executeQuery(select_all_sell_log);
/* 147 */               int count = 0;
/* 148 */               int error = 0;
/*     */               
/* 150 */               while (rs6.next()) {
/*     */                 
/* 152 */                 String uuid = rs6.getString("id");
/* 153 */                 if (uuid != null) {
/* 154 */                   String world = AuctionHouse.levelname;
/* 155 */                   String seller_uuid = rs6.getString("seller");
/* 156 */                   String buyer_uuid = rs6.getString("buyer");
/* 157 */                   String bidder_uuid = "";
/* 158 */                   float reserve = 0.0F;
/* 159 */                   float bid = 0.0F;
/* 160 */                   long id = rs6.getLong("time");
/* 161 */                   ListingType type = ListingType.PLAYER_LISTING;
/* 162 */                   String listing_type = type.name();
/* 163 */                   String item_string = rs6.getString("item");
/* 164 */                   float price = Float.parseFloat(item_string.substring(item_string.lastIndexOf(":") + 1, item_string.length() - 1));
/* 165 */                   ItemStack item = AuctionHouse.nms.deserialize(item_string.substring(0, item_string.lastIndexOf(":") - 1));
/* 166 */                   Listing listing = new Listing(world, seller_uuid, buyer_uuid, bidder_uuid, price, reserve, bid, type, item);
/*     */                   
/* 168 */                   boolean result = AuctionHouse.listings.importListing(listing, id);
/* 169 */                   if (result) {
/* 170 */                     count++;
/*     */                   } else {
/* 172 */                     error++;
/*     */                   } 
/* 174 */                   AuctionHouse.logger.info(String.format("Read sold item %d: (name = %s, item = %dx %s, lore = %s, enchants = %s, world = %s, seller = %s, buyer = %s, price = %.2f, reserve = %.2f, bid = %.2f, listing_type = %s)", new Object[] { 
/* 175 */                           Long.valueOf(id), ItemUtil.getName(item), Integer.valueOf(item.getAmount()), item.getType().getKey(), ItemUtil.getLoreString(item), ItemUtil.getEnchantsString(item), world, seller_uuid, buyer_uuid, Float.valueOf(price), Float.valueOf(reserve), Float.valueOf(bid), listing_type }));
/*     */                 } 
/*     */               } 
/* 178 */               stmt6.close();
/* 179 */               AuctionHouse.logger.info(String.format("Imported %d sold item%s", new Object[] { Integer.valueOf(count), (count != 1) ? "s" : "" }));
/* 180 */               if (error > 0) AuctionHouse.logger.info(String.format("Could not read %d sold item%s", new Object[] { Integer.valueOf(error), (error != 1) ? "s" : "" })); 
/* 181 */             } catch (SQLException e) {
/* 182 */               e.printStackTrace();
/*     */             } 
/*     */           }
/* 185 */           stmt5.close();
/* 186 */         } catch (SQLException e) {
/* 187 */           e.printStackTrace();
/*     */         } 
/* 189 */       } catch (SQLException sQLException) {}
/*     */ 
/*     */       
/* 192 */       File old = new File(url2);
/* 193 */       if (db.renameTo(old)) AuctionHouse.logger.info("Renamed data.db to data.db.old"); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\storage\ImportDBFile.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */