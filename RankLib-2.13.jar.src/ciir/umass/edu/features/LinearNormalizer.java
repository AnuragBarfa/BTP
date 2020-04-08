/*    */ package ciir.umass.edu.features;
/*    */ 
/*    */ import ciir.umass.edu.learning.DataPoint;
/*    */ import ciir.umass.edu.learning.RankList;
/*    */ import ciir.umass.edu.utilities.RankLibError;
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
/*    */ public class LinearNormalizer
/*    */   extends Normalizer
/*    */ {
/*    */   public void normalize(RankList rl) {
/* 25 */     if (rl.size() == 0)
/*    */     {
/* 27 */       throw RankLibError.create("Error in LinearNormalizor::normalize(): The input ranked list is empty");
/*    */     }
/* 29 */     int nFeature = DataPoint.getFeatureCount();
/* 30 */     int[] fids = new int[nFeature];
/* 31 */     for (int i = 1; i <= nFeature; i++)
/* 32 */       fids[i - 1] = i; 
/* 33 */     normalize(rl, fids);
/*    */   }
/*    */ 
/*    */   
/*    */   public void normalize(RankList rl, int[] fids) {
/* 38 */     if (rl.size() == 0)
/*    */     {
/* 40 */       throw RankLibError.create("Error in LinearNormalizor::normalize(): The input ranked list is empty");
/*    */     }
/*    */ 
/*    */     
/* 44 */     fids = removeDuplicateFeatures(fids);
/*    */     
/* 46 */     float[] min = new float[fids.length];
/* 47 */     float[] max = new float[fids.length];
/*    */     
/* 49 */     Arrays.fill(min, Float.MAX_VALUE);
/*    */     
/* 51 */     Arrays.fill(max, Float.MIN_VALUE);
/*    */     int i;
/* 53 */     for (i = 0; i < rl.size(); i++) {
/*    */       
/* 55 */       DataPoint dp = rl.get(i);
/* 56 */       for (int j = 0; j < fids.length; j++) {
/*    */         
/* 58 */         min[j] = Math.min(min[j], dp.getFeatureValue(fids[j]));
/* 59 */         max[j] = Math.max(max[j], dp.getFeatureValue(fids[j]));
/*    */       } 
/*    */     } 
/* 62 */     for (i = 0; i < rl.size(); i++) {
/*    */       
/* 64 */       DataPoint dp = rl.get(i);
/* 65 */       for (int j = 0; j < fids.length; j++) {
/*    */         
/* 67 */         if (max[j] > min[j]) {
/*    */           
/* 69 */           float value = (dp.getFeatureValue(fids[j]) - min[j]) / (max[j] - min[j]);
/* 70 */           dp.setFeatureValue(fids[j], value);
/*    */         } else {
/*    */           
/* 73 */           dp.setFeatureValue(fids[j], 0.0F);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   public String name() {
/* 79 */     return "linear";
/*    */   }
/*    */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\features\LinearNormalizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */