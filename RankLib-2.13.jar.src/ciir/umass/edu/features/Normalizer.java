/*    */ package ciir.umass.edu.features;
/*    */ 
/*    */ import ciir.umass.edu.learning.RankList;
/*    */ import java.util.HashSet;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Normalizer
/*    */ {
/*    */   public void normalize(RankList rl) {}
/*    */   
/*    */   public void normalize(List<RankList> samples) {
/* 29 */     for (int i = 0; i < samples.size(); i++) {
/* 30 */       normalize(samples.get(i));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void normalize(RankList rl, int[] fids) {}
/*    */   
/*    */   public void normalize(List<RankList> samples, int[] fids) {
/* 38 */     for (int i = 0; i < samples.size(); i++)
/* 39 */       normalize(samples.get(i), fids); 
/*    */   }
/*    */   
/*    */   public int[] removeDuplicateFeatures(int[] fids) {
/* 43 */     HashSet<Integer> uniqueSet = new HashSet<>();
/* 44 */     for (int i = 0; i < fids.length; i++) {
/* 45 */       if (!uniqueSet.contains(Integer.valueOf(fids[i])))
/* 46 */         uniqueSet.add(Integer.valueOf(fids[i])); 
/* 47 */     }  fids = new int[uniqueSet.size()];
/* 48 */     int fi = 0;
/* 49 */     for (Integer integer : uniqueSet)
/* 50 */       fids[fi++] = integer.intValue(); 
/* 51 */     return fids;
/*    */   }
/*    */ 
/*    */   
/*    */   public String name() {
/* 56 */     return "";
/*    */   }
/*    */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\features\Normalizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */