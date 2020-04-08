/*    */ package ciir.umass.edu.metric;
/*    */ 
/*    */ import ciir.umass.edu.learning.RankList;
/*    */ import java.util.Arrays;
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
/*    */ public class PrecisionScorer
/*    */   extends MetricScorer
/*    */ {
/*    */   public PrecisionScorer() {
/* 23 */     this.k = 10;
/*    */   }
/*    */   
/*    */   public PrecisionScorer(int k) {
/* 27 */     this.k = k;
/*    */   }
/*    */   
/*    */   public double score(RankList rl) {
/* 31 */     int count = 0;
/*    */     
/* 33 */     int size = this.k;
/* 34 */     if (this.k > rl.size() || this.k <= 0) {
/* 35 */       size = rl.size();
/*    */     }
/* 37 */     for (int i = 0; i < size; i++) {
/*    */       
/* 39 */       if (rl.get(i).getLabel() > 0.0D)
/* 40 */         count++; 
/*    */     } 
/* 42 */     return count / size;
/*    */   }
/*    */   
/*    */   public MetricScorer copy() {
/* 46 */     return new PrecisionScorer();
/*    */   }
/*    */   
/*    */   public String name() {
/* 50 */     return "P@" + this.k;
/*    */   }
/*    */   
/*    */   public double[][] swapChange(RankList rl) {
/* 54 */     int size = (rl.size() > this.k) ? this.k : rl.size();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 60 */     double[][] changes = new double[rl.size()][]; int i;
/* 61 */     for (i = 0; i < rl.size(); i++) {
/*    */       
/* 63 */       changes[i] = new double[rl.size()];
/* 64 */       Arrays.fill(changes[i], 0.0D);
/*    */     } 
/*    */     
/* 67 */     for (i = 0; i < size; i++) {
/*    */       
/* 69 */       for (int j = size; j < rl.size(); j++) {
/*    */         
/* 71 */         int c = getBinaryRelevance(rl.get(j).getLabel()) - getBinaryRelevance(rl.get(i).getLabel());
/* 72 */         changes[j][i] = (c / size); changes[i][j] = (c / size);
/*    */       } 
/*    */     } 
/* 75 */     return changes;
/*    */   }
/*    */   
/*    */   private int getBinaryRelevance(float label) {
/* 79 */     if (label > 0.0D)
/* 80 */       return 1; 
/* 81 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\metric\PrecisionScorer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */