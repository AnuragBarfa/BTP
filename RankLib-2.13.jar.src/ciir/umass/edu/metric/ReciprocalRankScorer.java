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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReciprocalRankScorer
/*     */   extends MetricScorer
/*     */ {
/*     */   public double score(RankList rl) {
/*  27 */     int size = (rl.size() > this.k) ? this.k : rl.size();
/*  28 */     int firstRank = -1;
/*  29 */     for (int i = 0; i < size && firstRank == -1; i++) {
/*     */       
/*  31 */       if (rl.get(i).getLabel() > 0.0D)
/*  32 */         firstRank = i + 1; 
/*     */     } 
/*  34 */     return (firstRank == -1) ? 0.0D : (1.0F / firstRank);
/*     */   }
/*     */   
/*     */   public MetricScorer copy() {
/*  38 */     return new ReciprocalRankScorer();
/*     */   }
/*     */   
/*     */   public String name() {
/*  42 */     return "RR@" + this.k;
/*     */   }
/*     */   
/*     */   public double[][] swapChange(RankList rl) {
/*  46 */     int firstRank = -1;
/*  47 */     int secondRank = -1;
/*  48 */     int size = (rl.size() > this.k) ? this.k : rl.size();
/*  49 */     for (int i = 0; i < size; i++) {
/*     */       
/*  51 */       if (rl.get(i).getLabel() > 0.0D)
/*     */       {
/*  53 */         if (firstRank == -1) {
/*  54 */           firstRank = i;
/*  55 */         } else if (secondRank == -1) {
/*  56 */           secondRank = i;
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/*  61 */     double[][] changes = new double[rl.size()][];
/*  62 */     for (int j = 0; j < rl.size(); j++) {
/*     */       
/*  64 */       changes[j] = new double[rl.size()];
/*  65 */       Arrays.fill(changes[j], 0.0D);
/*     */     } 
/*     */     
/*  68 */     double rr = 0.0D;
/*     */     
/*  70 */     if (firstRank != -1) {
/*     */       
/*  72 */       rr = 1.0D / (firstRank + 1); int m;
/*  73 */       for (m = firstRank + 1; m < size; m++) {
/*     */         
/*  75 */         if ((int)rl.get(m).getLabel() == 0) {
/*     */ 
/*     */           
/*  78 */           changes[m][firstRank] = 1.0D / (m + 1) - rr; changes[firstRank][m] = 1.0D / (m + 1) - rr;
/*     */           
/*  80 */           changes[m][firstRank] = 1.0D / (secondRank + 1) - rr; changes[firstRank][m] = 1.0D / (secondRank + 1) - rr;
/*     */         } 
/*     */       } 
/*  83 */       for (m = size; m < rl.size(); m++) {
/*  84 */         if ((int)rl.get(m).getLabel() == 0) {
/*     */ 
/*     */           
/*  87 */           changes[m][firstRank] = -rr; changes[firstRank][m] = -rr;
/*     */           
/*  89 */           changes[m][firstRank] = 1.0D / (secondRank + 1) - rr; changes[firstRank][m] = 1.0D / (secondRank + 1) - rr;
/*     */         } 
/*     */       } 
/*     */     } else {
/*  93 */       firstRank = size;
/*     */     } 
/*     */     
/*  96 */     for (int k = 0; k < firstRank; k++) {
/*     */       
/*  98 */       for (int m = firstRank; m < rl.size(); m++) {
/*     */         
/* 100 */         if (rl.get(m).getLabel() > 0.0F) {
/* 101 */           changes[m][k] = 1.0D / (k + 1) - rr; changes[k][m] = 1.0D / (k + 1) - rr;
/*     */         } 
/*     */       } 
/* 104 */     }  return changes;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\metric\ReciprocalRankScorer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */