/*    */ package ciir.umass.edu.metric;
/*    */ 
/*    */ import ciir.umass.edu.learning.RankList;
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
/*    */ public abstract class MetricScorer
/*    */ {
/* 23 */   protected int k = 10;
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
/*    */   public void setK(int k) {
/* 36 */     this.k = k;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getK() {
/* 41 */     return this.k;
/*    */   }
/*    */ 
/*    */   
/*    */   public void loadExternalRelevanceJudgment(String qrelFile) {}
/*    */ 
/*    */   
/*    */   public double score(List<RankList> rl) {
/* 49 */     double score = 0.0D;
/* 50 */     for (int i = 0; i < rl.size(); i++)
/* 51 */       score += score(rl.get(i)); 
/* 52 */     return score / rl.size();
/*    */   }
/*    */ 
/*    */   
/*    */   protected int[] getRelevanceLabels(RankList rl) {
/* 57 */     int[] rel = new int[rl.size()];
/* 58 */     for (int i = 0; i < rl.size(); i++)
/* 59 */       rel[i] = (int)rl.get(i).getLabel(); 
/* 60 */     return rel;
/*    */   }
/*    */   
/*    */   public abstract double score(RankList paramRankList);
/*    */   
/*    */   public abstract MetricScorer copy();
/*    */   
/*    */   public abstract String name();
/*    */   
/*    */   public abstract double[][] swapChange(RankList paramRankList);
/*    */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\metric\MetricScorer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */