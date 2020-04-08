/*     */ package ciir.umass.edu.features;
/*     */ 
/*     */ import ciir.umass.edu.learning.DataPoint;
/*     */ import ciir.umass.edu.learning.RankList;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ZScoreNormalizor
/*     */   extends Normalizer
/*     */ {
/*     */   public void normalize(RankList rl) {
/*  23 */     if (rl.size() == 0) {
/*     */       
/*  25 */       System.out.println("Error in ZScoreNormalizor::normalize(): The input ranked list is empty");
/*  26 */       System.exit(1);
/*     */     } 
/*  28 */     int nFeature = DataPoint.getFeatureCount();
/*  29 */     double[] means = new double[nFeature];
/*  30 */     Arrays.fill(means, 0.0D);
/*  31 */     for (int i = 0; i < rl.size(); i++) {
/*     */       
/*  33 */       DataPoint dp = rl.get(i);
/*  34 */       for (int k = 1; k <= nFeature; k++) {
/*  35 */         means[k - 1] = means[k - 1] + dp.getFeatureValue(k);
/*     */       }
/*     */     } 
/*  38 */     for (int j = 1; j <= nFeature; j++) {
/*     */       
/*  40 */       means[j - 1] = means[j - 1] / rl.size();
/*  41 */       double std = 0.0D; int k;
/*  42 */       for (k = 0; k < rl.size(); k++) {
/*     */         
/*  44 */         DataPoint p = rl.get(k);
/*  45 */         double x = p.getFeatureValue(j) - means[j - 1];
/*  46 */         std += x * x;
/*     */       } 
/*  48 */       std = Math.sqrt(std / (rl.size() - 1));
/*     */       
/*  50 */       if (std > 0.0D)
/*     */       {
/*  52 */         for (k = 0; k < rl.size(); k++) {
/*     */           
/*  54 */           DataPoint p = rl.get(k);
/*  55 */           double x = (p.getFeatureValue(j) - means[j - 1]) / std;
/*  56 */           p.setFeatureValue(j, (float)x);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void normalize(RankList rl, int[] fids) {
/*  63 */     if (rl.size() == 0) {
/*     */       
/*  65 */       System.out.println("Error in ZScoreNormalizor::normalize(): The input ranked list is empty");
/*  66 */       System.exit(1);
/*     */     } 
/*     */ 
/*     */     
/*  70 */     fids = removeDuplicateFeatures(fids);
/*     */     
/*  72 */     double[] means = new double[fids.length];
/*  73 */     Arrays.fill(means, 0.0D);
/*  74 */     for (int i = 0; i < rl.size(); i++) {
/*     */       
/*  76 */       DataPoint dp = rl.get(i);
/*  77 */       for (int k = 0; k < fids.length; k++) {
/*  78 */         means[k] = means[k] + dp.getFeatureValue(fids[k]);
/*     */       }
/*     */     } 
/*  81 */     for (int j = 0; j < fids.length; j++) {
/*     */       
/*  83 */       means[j] = means[j] / rl.size();
/*  84 */       double std = 0.0D; int k;
/*  85 */       for (k = 0; k < rl.size(); k++) {
/*     */         
/*  87 */         DataPoint p = rl.get(k);
/*  88 */         double x = p.getFeatureValue(fids[j]) - means[j];
/*  89 */         std += x * x;
/*     */       } 
/*  91 */       std = Math.sqrt(std / (rl.size() - 1));
/*     */       
/*  93 */       if (std > 0.0D)
/*     */       {
/*  95 */         for (k = 0; k < rl.size(); k++) {
/*     */           
/*  97 */           DataPoint p = rl.get(k);
/*  98 */           double x = (p.getFeatureValue(fids[j]) - means[j]) / std;
/*  99 */           p.setFeatureValue(fids[j], (float)x);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public String name() {
/* 106 */     return "zscore";
/*     */   }
/*     */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\features\ZScoreNormalizor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */