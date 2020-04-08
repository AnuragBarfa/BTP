/*    */ package ciir.umass.edu.utilities;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class KeyValuePair
/*    */ {
/* 19 */   protected List<String> keys = new ArrayList<>();
/* 20 */   protected List<String> values = new ArrayList<>();
/*    */ 
/*    */   
/*    */   public KeyValuePair(String text) {
/*    */     try {
/* 25 */       int idx = text.lastIndexOf("#");
/* 26 */       if (idx != -1) {
/* 27 */         text = text.substring(0, idx).trim();
/*    */       }
/* 29 */       String[] fs = text.split(" ");
/* 30 */       for (int i = 0; i < fs.length; i++) {
/*    */         
/* 32 */         fs[i] = fs[i].trim();
/* 33 */         if (fs[i].compareTo("") != 0)
/*    */         {
/* 35 */           this.keys.add(getKey(fs[i]));
/* 36 */           this.values.add(getValue(fs[i]));
/*    */         }
/*    */       
/*    */       } 
/* 40 */     } catch (Exception ex) {
/*    */       
/* 42 */       System.out.println("Error in KeyValuePair(text) constructor");
/*    */     } 
/*    */   }
/*    */   
/*    */   public List<String> keys() {
/* 47 */     return this.keys;
/*    */   }
/*    */   
/*    */   public List<String> values() {
/* 51 */     return this.values;
/*    */   }
/*    */ 
/*    */   
/*    */   private String getKey(String pair) {
/* 56 */     return pair.substring(0, pair.indexOf(":"));
/*    */   }
/*    */   
/*    */   private String getValue(String pair) {
/* 60 */     return pair.substring(pair.lastIndexOf(":") + 1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\ed\\utilities\KeyValuePair.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */