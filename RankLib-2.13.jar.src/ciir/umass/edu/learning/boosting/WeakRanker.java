/*    */ package ciir.umass.edu.learning.boosting;
/*    */ 
/*    */ import ciir.umass.edu.learning.RankList;
/*    */ import ciir.umass.edu.utilities.Sorter;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WeakRanker
/*    */ {
/* 24 */   private int fid = -1;
/*    */ 
/*    */   
/*    */   public WeakRanker(int fid) {
/* 28 */     this.fid = fid;
/*    */   }
/*    */   
/*    */   public int getFID() {
/* 32 */     return this.fid;
/*    */   }
/*    */ 
/*    */   
/*    */   public RankList rank(RankList l) {
/* 37 */     double[] score = new double[l.size()];
/* 38 */     for (int i = 0; i < l.size(); i++)
/* 39 */       score[i] = l.get(i).getFeatureValue(this.fid); 
/* 40 */     int[] idx = Sorter.sort(score, false);
/* 41 */     return new RankList(l, idx);
/*    */   }
/*    */   
/*    */   public List<RankList> rank(List<RankList> l) {
/* 45 */     List<RankList> ll = new ArrayList<>();
/* 46 */     for (int i = 0; i < l.size(); i++)
/* 47 */       ll.add(rank(l.get(i))); 
/* 48 */     return ll;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\learning\boosting\WeakRanker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */