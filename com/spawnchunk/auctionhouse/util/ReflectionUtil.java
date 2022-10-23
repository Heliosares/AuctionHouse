/*    */ package com.spawnchunk.auctionhouse.util;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Method;
/*    */ 
/*    */ 
/*    */ public class ReflectionUtil
/*    */ {
/*    */   public static Field getField(Class<?> clazz, String field) throws Exception {
/*    */     Field f;
/*    */     try {
/* 13 */       f = clazz.getDeclaredField(field);
/* 14 */     } catch (NoSuchFieldException e) {
/* 15 */       f = clazz.getField(field);
/*    */     } 
/* 17 */     if (f == null) throw new NoSuchFieldException(); 
/* 18 */     f.setAccessible(true);
/* 19 */     return f;
/*    */   }
/*    */   
/*    */   public static Method getMethod(Class<?> clazz, String method, Class<?>... classes) throws Exception {
/*    */     Method m;
/*    */     try {
/* 25 */       m = clazz.getDeclaredMethod(method, classes);
/* 26 */     } catch (NoSuchMethodException e) {
/* 27 */       m = clazz.getMethod(method, classes);
/*    */     } 
/* 29 */     if (m == null) throw new NoSuchFieldException(); 
/* 30 */     m.setAccessible(true);
/* 31 */     return m;
/*    */   }
/*    */   
/*    */   public static Constructor getConstructor(Class<?> clazz, Class<?>... classes) throws Exception {
/*    */     Constructor<?> c;
/*    */     try {
/* 37 */       c = clazz.getDeclaredConstructor(classes);
/* 38 */     } catch (NoSuchMethodException e) {
/* 39 */       c = clazz.getConstructor(classes);
/*    */     } 
/* 41 */     if (c == null) throw new NoSuchFieldException(); 
/* 42 */     c.setAccessible(true);
/* 43 */     return c;
/*    */   }
/*    */   
/*    */   public static Field setFinal(Field f, Boolean value) throws Exception {
/* 47 */     f.setAccessible(true);
/* 48 */     Field modifiersField = Field.class.getDeclaredField("modifiers");
/* 49 */     modifiersField.setAccessible(true);
/* 50 */     if (value.booleanValue()) {
/* 51 */       modifiersField.setInt(f, f.getModifiers() & 0x10);
/*    */     } else {
/* 53 */       modifiersField.setInt(f, f.getModifiers() & 0xFFFFFFEF);
/*    */     } 
/* 55 */     return f;
/*    */   }
/*    */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhous\\util\ReflectionUtil.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */