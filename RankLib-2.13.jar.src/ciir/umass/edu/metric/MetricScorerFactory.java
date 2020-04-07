/*    */ package ciir.umass.edu.metric;
/*    */ 
/*    */ import java.util.HashMap;
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
/*    */ public class MetricScorerFactory
/*    */ {
/* 19 */   private static MetricScorer[] mFactory = new MetricScorer[] { new APScorer(), new NDCGScorer(), new DCGScorer(), new PrecisionScorer(), new ReciprocalRankScorer(), new BestAtKScorer(), new ERRScorer() };
/* 20 */   private static HashMap<String, MetricScorer> map = new HashMap<>();
/*    */ 
/*    */   
/*    */   public MetricScorerFactory() {
/* 24 */     map.put("MAP", new APScorer());
/* 25 */     map.put("NDCG", new NDCGScorer());
/* 26 */     map.put("DCG", new DCGScorer());
/* 27 */     map.put("P", new PrecisionScorer());
/* 28 */     map.put("RR", new ReciprocalRankScorer());
/* 29 */     map.put("BEST", new BestAtKScorer());
/* 30 */     map.put("ERR", new ERRScorer());
/*    */   }
/*    */   
/*    */   public MetricScorer createScorer(METRIC metric) {
/* 34 */     return mFactory[metric.ordinal() - METRIC.MAP.ordinal()].copy();
/*    */   }
/*    */   
/*    */   public MetricScorer createScorer(METRIC metric, int k) {
/* 38 */     MetricScorer s = mFactory[metric.ordinal() - METRIC.MAP.ordinal()].copy();
/* 39 */     s.setK(k);
/* 40 */     return s;
/*    */   }
/*    */   
/*    */   public MetricScorer createScorer(String metric) {
/* 44 */     int k = -1;
/* 45 */     String m = "";
/* 46 */     MetricScorer s = null;
/* 47 */     if (metric.indexOf("@") != -1) {
/*    */       
/* 49 */       m = metric.substring(0, metric.indexOf("@"));
/* 50 */       k = Integer.parseInt(metric.substring(metric.indexOf("@") + 1));
/* 51 */       s = ((MetricScorer)map.get(m.toUpperCase())).copy();
/* 52 */       s.setK(k);
/*    */     } else {
/*    */       
/* 55 */       s = ((MetricScorer)map.get(metric.toUpperCase())).copy();
/* 56 */     }  return s;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\metric\MetricScorerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */