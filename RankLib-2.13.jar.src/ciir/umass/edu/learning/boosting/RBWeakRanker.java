/*    */ package ciir.umass.edu.learning.boosting;
/*    */ 
/*    */ import ciir.umass.edu.learning.DataPoint;
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
/*    */ public class RBWeakRanker
/*    */ {
/* 20 */   private int fid = -1;
/* 21 */   private double threshold = 0.0D;
/*    */ 
/*    */   
/*    */   public RBWeakRanker(int fid, double threshold) {
/* 25 */     this.fid = fid;
/* 26 */     this.threshold = threshold;
/*    */   }
/*    */   
/*    */   public int score(DataPoint p) {
/* 30 */     if (p.getFeatureValue(this.fid) > this.threshold)
/* 31 */       return 1; 
/* 32 */     return 0;
/*    */   }
/*    */   
/*    */   public int getFid() {
/* 36 */     return this.fid;
/*    */   }
/*    */   
/*    */   public double getThreshold() {
/* 40 */     return this.threshold;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 44 */     return this.fid + ":" + this.threshold;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\learning\boosting\RBWeakRanker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */