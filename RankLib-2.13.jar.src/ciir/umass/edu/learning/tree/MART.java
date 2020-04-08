/*    */ package ciir.umass.edu.learning.tree;
/*    */ 
/*    */ import ciir.umass.edu.learning.RankList;
/*    */ import ciir.umass.edu.learning.Ranker;
/*    */ import ciir.umass.edu.metric.MetricScorer;
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
/*    */ 
/*    */ 
/*    */ public class MART
/*    */   extends LambdaMART
/*    */ {
/*    */   public MART() {}
/*    */   
/*    */   public MART(List<RankList> samples, int[] features, MetricScorer scorer) {
/* 33 */     super(samples, features, scorer);
/*    */   }
/*    */ 
/*    */   
/*    */   public Ranker createNew() {
/* 38 */     return new MART();
/*    */   }
/*    */   
/*    */   public String name() {
/* 42 */     return "MART";
/*    */   }
/*    */   
/*    */   protected void computePseudoResponses() {
/* 46 */     for (int i = 0; i < this.martSamples.length; i++)
/* 47 */       this.pseudoResponses[i] = this.martSamples[i].getLabel() - this.modelScores[i]; 
/*    */   }
/*    */   
/*    */   protected void updateTreeOutput(RegressionTree rt) {
/* 51 */     List<Split> leaves = rt.leaves();
/* 52 */     for (int i = 0; i < leaves.size(); i++) {
/*    */       
/* 54 */       float s1 = 0.0F;
/* 55 */       Split s = leaves.get(i);
/* 56 */       int[] idx = s.getSamples();
/* 57 */       for (int j = 0; j < idx.length; j++) {
/*    */         
/* 59 */         int k = idx[j];
/* 60 */         s1 = (float)(s1 + this.pseudoResponses[k]);
/*    */       } 
/* 62 */       s.setOutput(s1 / idx.length);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\learning\tree\MART.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */