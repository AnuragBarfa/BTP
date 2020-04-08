/*     */ package ciir.umass.edu.learning.neuralnet;
/*     */ 
/*     */ import ciir.umass.edu.learning.RankList;
/*     */ import ciir.umass.edu.learning.Ranker;
/*     */ import ciir.umass.edu.metric.MetricScorer;
/*     */ import java.util.List;
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
/*     */ public class LambdaRank
/*     */   extends RankNet
/*     */ {
/*  23 */   protected float[][] targetValue = (float[][])null;
/*     */ 
/*     */ 
/*     */   
/*     */   public LambdaRank() {}
/*     */ 
/*     */   
/*     */   public LambdaRank(List<RankList> samples, int[] features, MetricScorer scorer) {
/*  31 */     super(samples, features, scorer);
/*     */   }
/*     */   
/*     */   protected int[][] batchFeedForward(RankList rl) {
/*  35 */     int[][] pairMap = new int[rl.size()][];
/*  36 */     this.targetValue = new float[rl.size()][];
/*  37 */     for (int i = 0; i < rl.size(); i++) {
/*     */       
/*  39 */       addInput(rl.get(i));
/*  40 */       propagate(i);
/*     */       
/*  42 */       int count = 0;
/*  43 */       for (int j = 0; j < rl.size(); j++) {
/*  44 */         if (rl.get(i).getLabel() > rl.get(j).getLabel() || rl.get(i).getLabel() < rl.get(j).getLabel())
/*  45 */           count++; 
/*     */       } 
/*  47 */       pairMap[i] = new int[count];
/*  48 */       this.targetValue[i] = new float[count];
/*     */       
/*  50 */       int k = 0;
/*  51 */       for (int m = 0; m < rl.size(); m++) {
/*  52 */         if (rl.get(i).getLabel() > rl.get(m).getLabel() || rl.get(i).getLabel() < rl.get(m).getLabel()) {
/*     */           
/*  54 */           pairMap[i][k] = m;
/*  55 */           if (rl.get(i).getLabel() > rl.get(m).getLabel()) {
/*  56 */             this.targetValue[i][k] = 1.0F;
/*     */           } else {
/*  58 */             this.targetValue[i][k] = 0.0F;
/*  59 */           }  k++;
/*     */         } 
/*     */       } 
/*  62 */     }  return pairMap;
/*     */   }
/*     */   
/*     */   protected void batchBackPropagate(int[][] pairMap, float[][] pairWeight) {
/*  66 */     for (int i = 0; i < pairMap.length; i++) {
/*     */       
/*  68 */       PropParameter p = new PropParameter(i, pairMap, pairWeight, this.targetValue);
/*     */       
/*  70 */       this.outputLayer.computeDelta(p); int j;
/*  71 */       for (j = this.layers.size() - 2; j >= 1; j--) {
/*  72 */         ((Layer)this.layers.get(j)).updateDelta(p);
/*     */       }
/*     */       
/*  75 */       this.outputLayer.updateWeight(p);
/*  76 */       for (j = this.layers.size() - 2; j >= 1; j--)
/*  77 */         ((Layer)this.layers.get(j)).updateWeight(p); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected RankList internalReorder(RankList rl) {
/*  82 */     return rank(rl);
/*     */   }
/*     */   
/*     */   protected float[][] computePairWeight(int[][] pairMap, RankList rl) {
/*  86 */     double[][] changes = this.scorer.swapChange(rl);
/*  87 */     float[][] weight = new float[pairMap.length][];
/*  88 */     for (int i = 0; i < weight.length; i++) {
/*     */       
/*  90 */       weight[i] = new float[(pairMap[i]).length];
/*  91 */       for (int j = 0; j < (pairMap[i]).length; j++) {
/*     */         
/*  93 */         int sign = (rl.get(i).getLabel() > rl.get(pairMap[i][j]).getLabel()) ? 1 : -1;
/*  94 */         weight[i][j] = (float)Math.abs(changes[i][pairMap[i][j]]) * sign;
/*     */       } 
/*     */     } 
/*  97 */     return weight;
/*     */   }
/*     */   
/*     */   protected void estimateLoss() {
/* 101 */     this.misorderedPairs = 0;
/* 102 */     for (int j = 0; j < this.samples.size(); j++) {
/*     */       
/* 104 */       RankList rl = this.samples.get(j);
/* 105 */       for (int k = 0; k < rl.size() - 1; k++) {
/*     */         
/* 107 */         double o1 = eval(rl.get(k));
/* 108 */         for (int l = k + 1; l < rl.size(); l++) {
/*     */           
/* 110 */           if (rl.get(k).getLabel() > rl.get(l).getLabel()) {
/*     */             
/* 112 */             double o2 = eval(rl.get(l));
/*     */             
/* 114 */             if (o1 < o2)
/* 115 */               this.misorderedPairs++; 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 120 */     this.error = 1.0D - this.scoreOnTrainingData;
/* 121 */     if (this.error > this.lastError) {
/*     */ 
/*     */       
/* 124 */       this.straightLoss++;
/*     */     } else {
/*     */       
/* 127 */       this.straightLoss = 0;
/* 128 */     }  this.lastError = this.error;
/*     */   }
/*     */ 
/*     */   
/*     */   public Ranker createNew() {
/* 133 */     return new LambdaRank();
/*     */   }
/*     */   
/*     */   public String name() {
/* 137 */     return "LambdaRank";
/*     */   }
/*     */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\learning\neuralnet\LambdaRank.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */