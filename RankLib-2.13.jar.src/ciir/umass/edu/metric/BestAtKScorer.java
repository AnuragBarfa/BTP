/*     */ package ciir.umass.edu.metric;
/*     */ 
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
/*     */ 
/*     */ public class BestAtKScorer
/*     */   extends MetricScorer
/*     */ {
/*     */   public BestAtKScorer() {
/*  23 */     this.k = 10;
/*     */   }
/*     */   
/*     */   public BestAtKScorer(int k) {
/*  27 */     this.k = k;
/*     */   }
/*     */   
/*     */   public double score(RankList rl) {
/*  31 */     return rl.get(maxToK(rl, this.k - 1)).getLabel();
/*     */   }
/*     */   
/*     */   public MetricScorer copy() {
/*  35 */     return new BestAtKScorer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int maxToK(RankList rl, int k) {
/*  47 */     int size = k;
/*  48 */     if (size < 0 || size > rl.size() - 1) {
/*  49 */       size = rl.size() - 1;
/*     */     }
/*  51 */     double max = -1.0D;
/*  52 */     int max_i = 0;
/*  53 */     for (int i = 0; i <= size; i++) {
/*     */       
/*  55 */       if (max < rl.get(i).getLabel()) {
/*     */         
/*  57 */         max = rl.get(i).getLabel();
/*  58 */         max_i = i;
/*     */       } 
/*     */     } 
/*  61 */     return max_i;
/*     */   }
/*     */   
/*     */   public String name() {
/*  65 */     return "Best@" + this.k;
/*     */   }
/*     */ 
/*     */   
/*     */   public double[][] swapChange(RankList rl) {
/*  70 */     int[] labels = new int[rl.size()];
/*  71 */     int[] best = new int[rl.size()];
/*  72 */     int max = -1;
/*  73 */     int maxVal = -1;
/*  74 */     int secondMaxVal = -1;
/*  75 */     int maxCount = 0;
/*  76 */     for (int i = 0; i < rl.size(); i++) {
/*     */       
/*  78 */       int v = (int)rl.get(i).getLabel();
/*  79 */       labels[i] = v;
/*  80 */       if (maxVal < v) {
/*     */         
/*  82 */         if (i < this.k) {
/*     */           
/*  84 */           secondMaxVal = maxVal;
/*  85 */           maxCount = 0;
/*     */         } 
/*  87 */         maxVal = v;
/*  88 */         max = i;
/*     */       }
/*  90 */       else if (maxVal == v && i < this.k) {
/*  91 */         maxCount++;
/*  92 */       }  best[i] = max;
/*     */     } 
/*  94 */     if (secondMaxVal == -1) {
/*  95 */       secondMaxVal = 0;
/*     */     }
/*  97 */     double[][] changes = new double[rl.size()][]; int j;
/*  98 */     for (j = 0; j < rl.size(); j++) {
/*     */       
/* 100 */       changes[j] = new double[rl.size()];
/* 101 */       Arrays.fill(changes[j], 0.0D);
/*     */     } 
/*     */     
/* 104 */     for (j = 0; j < rl.size() - 1; j++) {
/*     */       
/* 106 */       for (int k = j + 1; k < rl.size(); k++) {
/*     */         
/* 108 */         double change = 0.0D;
/* 109 */         if (k < this.k || j >= this.k) {
/* 110 */           change = 0.0D;
/* 111 */         } else if (labels[j] == labels[k] || labels[k] == labels[best[this.k - 1]]) {
/* 112 */           change = 0.0D;
/* 113 */         } else if (labels[k] > labels[best[this.k - 1]]) {
/* 114 */           change = (labels[k] - labels[best[j]]);
/* 115 */         } else if (labels[j] < labels[best[this.k - 1]] || maxCount > 1) {
/* 116 */           change = 0.0D;
/*     */         } else {
/* 118 */           change = (maxVal - Math.max(secondMaxVal, labels[k]));
/* 119 */         }  changes[k][j] = change; changes[j][k] = change;
/*     */       } 
/*     */     } 
/* 122 */     return changes;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\metric\BestAtKScorer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */