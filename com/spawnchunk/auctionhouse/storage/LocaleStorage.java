/*     */ package com.spawnchunk.auctionhouse.storage;
/*     */ 
/*     */ import com.spawnchunk.auctionhouse.AuctionHouse;
/*     */ import com.spawnchunk.auctionhouse.config.Config;
/*     */ import com.spawnchunk.auctionhouse.util.FileUtil;
/*     */ import com.spawnchunk.auctionhouse.util.MessageUtil;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.TreeMap;
/*     */ import org.json.simple.JSONObject;
/*     */ import org.json.simple.parser.JSONParser;
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
/*     */ public class LocaleStorage
/*     */ {
/*     */   private static final int locale_version = 34;
/*     */   
/*     */   public static void reloadLocales() {
/*  32 */     AuctionHouse.locales.clear();
/*  33 */     loadLocales();
/*     */   }
/*     */   
/*     */   public static void loadLocales() {
/*  37 */     installLocaleFile();
/*  38 */     List<String> jsonFiles = listJsonFiles();
/*  39 */     for (String filename : jsonFiles) {
/*  40 */       String locale = containsLocale(filename);
/*  41 */       if (!locale.isEmpty()) {
/*  42 */         TreeMap<String, String> map = loadLocaleFile(filename);
/*  43 */         AuctionHouse.locales.put(locale, map);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void installLocaleFile() {
/*  49 */     String path = AuctionHouse.plugin.getDataFolder().getPath();
/*  50 */     String filename = "en_us.json";
/*  51 */     String fname = path + path + File.separator;
/*  52 */     File dir = new File(path);
/*  53 */     if (!dir.exists())
/*     */     {
/*  55 */       dir.mkdirs();
/*     */     }
/*  57 */     File fn = new File(fname);
/*     */ 
/*     */     
/*  60 */     if (!fn.exists()) {
/*  61 */       AuctionHouse.plugin.saveResource(filename, false);
/*  62 */     } else if (isOutdated(filename)) {
/*  63 */       if (FileUtil.backupFile(fn)) {
/*     */         
/*  65 */         AuctionHouse.plugin.saveResource(filename, true);
/*  66 */         MessageUtil.logWarning(String.format("Warning! Locale file %s has been updated!", new Object[] { filename }));
/*  67 */         MessageUtil.logWarning("A backup of the old file has been saved with .backup extension.");
/*  68 */         MessageUtil.logWarning("Please update the new file to include any customized translations.");
/*     */       } else {
/*  70 */         MessageUtil.logSevere("Error! Could not backup existing locale file");
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static List<String> listJsonFiles() {
/*  76 */     List<String> files = new ArrayList<>();
/*  77 */     String path = AuctionHouse.plugin.getDataFolder().getPath();
/*  78 */     File dir = new File(path);
/*  79 */     if (dir.exists()) {
/*     */       
/*  81 */       String[] list = dir.list((file, name) -> name.toLowerCase().endsWith(".json"));
/*  82 */       if (list != null) files = Arrays.asList(list); 
/*     */     } 
/*  84 */     return files;
/*     */   }
/*     */   
/*     */   private static boolean isOutdated(String filename) {
/*  88 */     String path = AuctionHouse.plugin.getDataFolder().getPath();
/*  89 */     String fname = path + path + File.separator;
/*  90 */     File fn = new File(fname);
/*  91 */     if (fn.exists()) {
/*  92 */       JSONParser parser = new JSONParser();
/*     */       try {
/*  94 */         Object object = parser.parse(new InputStreamReader(new FileInputStream(fname), StandardCharsets.UTF_8));
/*  95 */         JSONObject jsonObject = (JSONObject)object;
/*  96 */         String version = (String)jsonObject.get("version");
/*  97 */         if (version == null) return true; 
/*  98 */         if (Integer.parseInt(version) >= 34) return false; 
/*  99 */       } catch (Exception e) {
/* 100 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/* 103 */     return true;
/*     */   }
/*     */   
/*     */   private static String containsLocale(String filename) {
/* 107 */     String path = AuctionHouse.plugin.getDataFolder().getPath();
/* 108 */     String fname = path + path + File.separator;
/* 109 */     File fn = new File(fname);
/* 110 */     if (fn.exists()) {
/* 111 */       JSONParser parser = new JSONParser();
/*     */       try {
/* 113 */         Object object = parser.parse(new InputStreamReader(new FileInputStream(fname), StandardCharsets.UTF_8));
/* 114 */         JSONObject jsonObject = (JSONObject)object;
/* 115 */         String language = (String)jsonObject.get("language.name");
/* 116 */         String region = (String)jsonObject.get("language.region");
/* 117 */         String locale = (String)jsonObject.get("language.code");
/* 118 */         locale = locale.toLowerCase();
/* 119 */         if (!language.isEmpty() && !region.isEmpty() && locale.equalsIgnoreCase(filename.replace(".json", ""))) {
/* 120 */           AuctionHouse.logger.info(String.format("Found locale file %s", new Object[] { filename }));
/* 121 */           return locale;
/*     */         } 
/* 123 */       } catch (Exception e) {
/* 124 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/* 127 */     return "";
/*     */   }
/*     */   
/*     */   private static TreeMap<String, String> loadLocaleFile(String filename) {
/* 131 */     TreeMap<String, String> map = new TreeMap<>();
/* 132 */     String path = AuctionHouse.plugin.getDataFolder().getPath();
/* 133 */     String fname = path + path + File.separator;
/* 134 */     File fn = new File(fname);
/* 135 */     if (fn.exists()) {
/* 136 */       if (isOutdated(filename)) {
/* 137 */         MessageUtil.logWarning(String.format("Warning! Locale file %s is not the latest version!", new Object[] { filename }));
/* 138 */         MessageUtil.logWarning("File should be updated to include and missing translations");
/*     */       } 
/* 140 */       JSONParser parser = new JSONParser();
/*     */       try {
/* 142 */         Object object = parser.parse(new InputStreamReader(new FileInputStream(fname), StandardCharsets.UTF_8));
/* 143 */         JSONObject jsonObject = (JSONObject)object;
/* 144 */         for (Object obj : jsonObject.keySet()) {
/* 145 */           String key = (String)obj;
/* 146 */           String value = (String)jsonObject.get(key);
/* 147 */           map.put(key, value);
/*     */         } 
/* 149 */       } catch (Exception e) {
/* 150 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/* 153 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String translate(String key, String loc) {
/* 158 */     if (AuctionHouse.locales.containsKey(loc)) {
/*     */       
/* 160 */       if (Config.translations.isEmpty()) Config.translations = (TreeMap)AuctionHouse.locales.get(loc); 
/* 161 */       if (Config.translations.containsKey(key)) {
/* 162 */         return (String)Config.translations.get(key);
/*     */       }
/* 164 */       return String.format("<missing translation: %s>", new Object[] { key });
/*     */     } 
/*     */     
/* 167 */     MessageUtil.logWarning(String.format("Locale %s not found! Please check that %s.json exists.", new Object[] { loc, loc }));
/* 168 */     String fallback = "en_us";
/* 169 */     Config.locale = fallback;
/* 170 */     if (Config.translations_fallback.isEmpty()) Config.translations_fallback = (TreeMap)AuctionHouse.locales.get(fallback); 
/* 171 */     if (Config.translations_fallback.containsKey(key)) {
/* 172 */       return (String)Config.translations_fallback.get(key);
/*     */     }
/* 174 */     return String.format("<missing translation: %s>", new Object[] { key });
/*     */   }
/*     */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\storage\LocaleStorage.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */