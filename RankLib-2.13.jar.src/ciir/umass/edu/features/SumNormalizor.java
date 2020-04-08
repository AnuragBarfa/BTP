/*    */ package ciir.umass.edu.features;
/*    */ 
/*    */ import ciir.umass.edu.learning.DataPoint;
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
/*    */ public class SumNormalizor
/*    */   extends Normalizer
/*    */ {
/*    */   public void normalize(RankList rl) {
/* 23 */     if (rl.size() == 0) {
/*    */       
/* 25 */       System.out.println("Error in SumNormalizor::normalize(): The input ranked list is empty");
/* 26 */       System.exit(1);
/*    */     } 
/* 28 */     int nFeature = DataPoint.getFeatureCount();
/* 29 */     double[] norm = new double[nFeature];
/* 30 */     Arrays.fill(norm, 0.0D); int i;
/* 31 */     for (i = 0; i < rl.size(); i++) {
/*    */       
/* 33 */       DataPoint dp = rl.get(i);
/* 34 */       for (int j = 1; j <= nFeature; j++)
/* 35 */         norm[j - 1] = norm[j - 1] + Math.abs(dp.getFeatureValue(j)); 
/*    */     } 
/* 37 */     for (i = 0; i < rl.size(); i++) {
/*    */       
/* 39 */       DataPoint dp = rl.get(i);
/* 40 */       for (int j = 1; j <= nFeature; j++) {
/*    */         
/* 42 */         if (norm[j - 1] > 0.0D)
/* 43 */           dp.setFeatureValue(j, (float)(dp.getFeatureValue(j) / norm[j - 1])); 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void normalize(RankList rl, int[] fids) {
/* 49 */     if (rl.size() == 0) {
/*    */       
/* 51 */       System.out.println("Error in SumNormalizor::normalize(): The input ranked list is empty");
/* 52 */       System.exit(1);
/*    */     } 
/*    */ 
/*    */     
/* 56 */     fids = removeDuplicateFeatures(fids);
/*    */     
/* 58 */     double[] norm = new double[fids.length];
/* 59 */     Arrays.fill(norm, 0.0D); int i;
/* 60 */     for (i = 0; i < rl.size(); i++) {
/*    */       
/* 62 */       DataPoint dp = rl.get(i);
/* 63 */       for (int j = 0; j < fids.length; j++)
/* 64 */         norm[j] = norm[j] + Math.abs(dp.getFeatureValue(fids[j])); 
/*    */     } 
/* 66 */     for (i = 0; i < rl.size(); i++) {
/*    */       
/* 68 */       DataPoint dp = rl.get(i);
/* 69 */       for (int j = 0; j < fids.length; j++) {
/* 70 */         if (norm[j] > 0.0D)
/* 71 */           dp.setFeatureValue(fids[j], (float)(dp.getFeatureValue(fids[j]) / norm[j])); 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   public String name() {
/* 76 */     return "sum";
/*    */   }
/*    */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\features\SumNormalizor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */