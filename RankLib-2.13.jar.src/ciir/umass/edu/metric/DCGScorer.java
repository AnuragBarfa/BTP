/*     */ package ciir.umass.edu.metric;
/*     */ 
/*     */ import ciir.umass.edu.learning.RankList;
/*     */ import ciir.umass.edu.utilities.SimpleMath;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DCGScorer
/*     */   extends MetricScorer
/*     */ {
/*  17 */   protected static double[] discount = null;
/*  18 */   protected static double[] gain = null;
/*     */ 
/*     */   
/*     */   public DCGScorer() {
/*  22 */     this.k = 10;
/*     */     
/*  24 */     if (discount == null) {
/*     */       
/*  26 */       discount = new double[5000]; int i;
/*  27 */       for (i = 0; i < discount.length; i++)
/*  28 */         discount[i] = 1.0D / SimpleMath.logBase2((i + 2)); 
/*  29 */       gain = new double[6];
/*  30 */       for (i = 0; i < 6; i++)
/*  31 */         gain[i] = ((1 << i) - 1); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public DCGScorer(int k) {
/*  36 */     this.k = k;
/*     */     
/*  38 */     if (discount == null) {
/*     */       
/*  40 */       discount = new double[5000]; int i;
/*  41 */       for (i = 0; i < discount.length; i++)
/*  42 */         discount[i] = 1.0D / SimpleMath.logBase2((i + 2)); 
/*  43 */       gain = new double[6];
/*  44 */       for (i = 0; i < 6; i++)
/*  45 */         gain[i] = ((1 << i) - 1); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public MetricScorer copy() {
/*  50 */     return new DCGScorer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double score(RankList rl) {
/*  57 */     if (rl.size() == 0) {
/*  58 */       return 0.0D;
/*     */     }
/*  60 */     int size = this.k;
/*  61 */     if (this.k > rl.size() || this.k <= 0) {
/*  62 */       size = rl.size();
/*     */     }
/*  64 */     int[] rel = getRelevanceLabels(rl);
/*  65 */     return getDCG(rel, size);
/*     */   }
/*     */   
/*     */   public double[][] swapChange(RankList rl) {
/*  69 */     int[] rel = getRelevanceLabels(rl);
/*  70 */     int size = (rl.size() > this.k) ? this.k : rl.size();
/*  71 */     double[][] changes = new double[rl.size()][]; int i;
/*  72 */     for (i = 0; i < rl.size(); i++) {
/*  73 */       changes[i] = new double[rl.size()];
/*     */     }
/*     */     
/*  76 */     for (i = 0; i < size; i++) {
/*  77 */       for (int j = i + 1; j < rl.size(); j++) {
/*  78 */         changes[i][j] = (discount(i) - discount(j)) * (gain(rel[i]) - gain(rel[j])); changes[j][i] = (discount(i) - discount(j)) * (gain(rel[i]) - gain(rel[j]));
/*     */       } 
/*  80 */     }  return changes;
/*     */   }
/*     */   
/*     */   public String name() {
/*  84 */     return "DCG@" + this.k;
/*     */   }
/*     */ 
/*     */   
/*     */   protected double getDCG(int[] rel, int topK) {
/*  89 */     double dcg = 0.0D;
/*  90 */     for (int i = 0; i < topK; i++)
/*  91 */       dcg += gain(rel[i]) * discount(i); 
/*  92 */     return dcg;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected double discount(int index) {
/*  98 */     if (index < discount.length) {
/*  99 */       return discount[index];
/*     */     }
/*     */     
/* 102 */     int cacheSize = discount.length + 1000;
/* 103 */     while (cacheSize <= index)
/* 104 */       cacheSize += 1000; 
/* 105 */     double[] tmp = new double[cacheSize];
/* 106 */     System.arraycopy(discount, 0, tmp, 0, discount.length);
/* 107 */     for (int i = discount.length; i < tmp.length; i++)
/* 108 */       tmp[i] = 1.0D / SimpleMath.logBase2((i + 2)); 
/* 109 */     discount = tmp;
/* 110 */     return discount[index];
/*     */   }
/*     */   
/*     */   protected double gain(int rel) {
/* 114 */     if (rel < gain.length) {
/* 115 */       return gain[rel];
/*     */     }
/*     */     
/* 118 */     int cacheSize = gain.length + 10;
/* 119 */     while (cacheSize <= rel)
/* 120 */       cacheSize += 10; 
/* 121 */     double[] tmp = new double[cacheSize];
/* 122 */     System.arraycopy(gain, 0, tmp, 0, gain.length);
/* 123 */     for (int i = gain.length; i < tmp.length; i++)
/* 124 */       tmp[i] = ((1 << i) - 1); 
/* 125 */     gain = tmp;
/* 126 */     return gain[rel];
/*     */   }
/*     */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\metric\DCGScorer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */