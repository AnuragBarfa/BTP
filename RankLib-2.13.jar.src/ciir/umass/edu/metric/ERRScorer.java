/*     */ package ciir.umass.edu.metric;
/*     */ 
/*     */ import ciir.umass.edu.learning.RankList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
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
/*     */ 
/*     */ 
/*     */ public class ERRScorer
/*     */   extends MetricScorer
/*     */ {
/*  25 */   public static double MAX = 16.0D;
/*     */ 
/*     */   
/*     */   public ERRScorer() {
/*  29 */     this.k = 10;
/*     */   }
/*     */   
/*     */   public ERRScorer(int k) {
/*  33 */     this.k = k;
/*     */   }
/*     */   
/*     */   public ERRScorer copy() {
/*  37 */     return new ERRScorer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double score(RankList rl) {
/*  45 */     int size = this.k;
/*  46 */     if (this.k > rl.size() || this.k <= 0) {
/*  47 */       size = rl.size();
/*     */     }
/*  49 */     List<Integer> rel = new ArrayList<>();
/*  50 */     for (int i = 0; i < rl.size(); i++) {
/*  51 */       rel.add(Integer.valueOf((int)rl.get(i).getLabel()));
/*     */     }
/*  53 */     double s = 0.0D;
/*  54 */     double p = 1.0D;
/*  55 */     for (int j = 1; j <= size; j++) {
/*     */       
/*  57 */       double R = R(((Integer)rel.get(j - 1)).intValue());
/*  58 */       s += p * R / j;
/*  59 */       p *= 1.0D - R;
/*     */     } 
/*  61 */     return s;
/*     */   }
/*     */   
/*     */   public String name() {
/*  65 */     return "ERR@" + this.k;
/*     */   }
/*     */   
/*     */   private double R(int rel) {
/*  69 */     return ((1 << rel) - 1) / MAX;
/*     */   }
/*     */   
/*     */   public double[][] swapChange(RankList rl) {
/*  73 */     int size = (rl.size() > this.k) ? this.k : rl.size();
/*  74 */     int[] labels = new int[rl.size()];
/*  75 */     double[] R = new double[rl.size()];
/*  76 */     double[] np = new double[rl.size()];
/*  77 */     double p = 1.0D;
/*     */     
/*  79 */     for (int i = 0; i < size; i++) {
/*     */       
/*  81 */       labels[i] = (int)rl.get(i).getLabel();
/*  82 */       R[i] = R(labels[i]);
/*  83 */       np[i] = p * (1.0D - R[i]);
/*  84 */       p *= np[i];
/*     */     } 
/*     */     
/*  87 */     double[][] changes = new double[rl.size()][]; int j;
/*  88 */     for (j = 0; j < rl.size(); j++) {
/*     */       
/*  90 */       changes[j] = new double[rl.size()];
/*  91 */       Arrays.fill(changes[j], 0.0D);
/*     */     } 
/*     */     
/*  94 */     for (j = 0; j < size; j++) {
/*     */       
/*  96 */       double v1 = 1.0D / (j + 1) * ((j == 0) ? 1.0D : np[j - 1]);
/*  97 */       double change = 0.0D;
/*  98 */       for (int k = j + 1; k < rl.size(); k++) {
/*     */         
/* 100 */         if (labels[j] == labels[k]) {
/* 101 */           change = 0.0D;
/*     */         } else {
/*     */           
/* 104 */           change = v1 * (R[k] - R[j]);
/* 105 */           p = ((j == 0) ? 1.0D : np[j - 1]) * (R[j] - R[k]);
/* 106 */           for (int m = j + 1; m < k; m++) {
/*     */             
/* 108 */             change += p * R[m] / (1 + m);
/* 109 */             p *= 1.0D - R[m];
/*     */           } 
/* 111 */           change += (np[k - 1] * (1.0D - R[k]) * R[j] / (1.0D - R[j]) - np[k - 1] * R[k]) / (k + 1);
/*     */         } 
/* 113 */         changes[j][k] = change; changes[k][j] = change;
/*     */       } 
/*     */     } 
/* 116 */     return changes;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\metric\ERRScorer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */