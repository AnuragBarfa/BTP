/*    */ package ciir.umass.edu.learning;
/*    */ 
/*    */ import ciir.umass.edu.metric.MetricScorer;
/*    */ import ciir.umass.edu.utilities.SimpleMath;
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
/*    */ public class RankerTrainer
/*    */ {
/* 24 */   protected RankerFactory rf = new RankerFactory();
/* 25 */   protected double trainingTime = 0.0D;
/*    */ 
/*    */   
/*    */   public Ranker train(RANKER_TYPE type, List<RankList> train, int[] features, MetricScorer scorer) {
/* 29 */     Ranker ranker = this.rf.createRanker(type, train, features, scorer);
/* 30 */     long start = System.nanoTime();
/* 31 */     ranker.init();
/* 32 */     ranker.learn();
/* 33 */     this.trainingTime = (System.nanoTime() - start);
/*    */     
/* 35 */     return ranker;
/*    */   }
/*    */   
/*    */   public Ranker train(RANKER_TYPE type, List<RankList> train, List<RankList> validation, int[] features, MetricScorer scorer) {
/* 39 */     Ranker ranker = this.rf.createRanker(type, train, features, scorer);
/* 40 */     ranker.setValidationSet(validation);
/* 41 */     long start = System.nanoTime();
/* 42 */     ranker.init();
/* 43 */     ranker.learn();
/* 44 */     this.trainingTime = (System.nanoTime() - start);
/*    */     
/* 46 */     return ranker;
/*    */   }
/*    */   
/*    */   public double getTrainingTime() {
/* 50 */     return this.trainingTime;
/*    */   }
/*    */   
/*    */   public void printTrainingTime() {
/* 54 */     System.out.println("Training time: " + SimpleMath.round(this.trainingTime / 1.0E9D, 2) + " seconds");
/*    */   }
/*    */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\learning\RankerTrainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */