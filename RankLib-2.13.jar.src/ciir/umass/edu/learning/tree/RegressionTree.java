/*     */ package ciir.umass.edu.learning.tree;
/*     */ 
/*     */ import ciir.umass.edu.learning.DataPoint;
/*     */ import java.util.ArrayList;
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
/*     */ public class RegressionTree
/*     */ {
/*  23 */   protected int nodes = 10;
/*  24 */   protected int minLeafSupport = 1;
/*     */ 
/*     */   
/*  27 */   protected Split root = null;
/*  28 */   protected List<Split> leaves = null;
/*     */   
/*  30 */   protected DataPoint[] trainingSamples = null;
/*  31 */   protected double[] trainingLabels = null;
/*  32 */   protected int[] features = null;
/*  33 */   protected float[][] thresholds = (float[][])null;
/*  34 */   protected int[] index = null;
/*  35 */   protected FeatureHistogram hist = null;
/*     */ 
/*     */   
/*     */   public RegressionTree(Split root) {
/*  39 */     this.root = root;
/*  40 */     this.leaves = root.leaves();
/*     */   }
/*     */   
/*     */   public RegressionTree(int nLeaves, DataPoint[] trainingSamples, double[] labels, FeatureHistogram hist, int minLeafSupport) {
/*  44 */     this.nodes = nLeaves;
/*  45 */     this.trainingSamples = trainingSamples;
/*  46 */     this.trainingLabels = labels;
/*  47 */     this.hist = hist;
/*  48 */     this.minLeafSupport = minLeafSupport;
/*  49 */     this.index = new int[trainingSamples.length];
/*  50 */     for (int i = 0; i < trainingSamples.length; i++) {
/*  51 */       this.index[i] = i;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fit() {
/*  59 */     List<Split> queue = new ArrayList<>();
/*  60 */     this.root = new Split(this.index, this.hist, 3.4028234663852886E38D, 0.0D);
/*  61 */     this.root.setRoot(true);
/*     */ 
/*     */     
/*  64 */     if (this.root.split(this.trainingLabels, this.minLeafSupport)) {
/*  65 */       insert(queue, this.root.getLeft());
/*  66 */       insert(queue, this.root.getRight());
/*     */     } 
/*     */     
/*  69 */     int taken = 0;
/*  70 */     while ((this.nodes == -1 || taken + queue.size() < this.nodes) && queue.size() > 0) {
/*     */       
/*  72 */       Split leaf = queue.get(0);
/*  73 */       queue.remove(0);
/*     */       
/*  75 */       if ((leaf.getSamples()).length < 2 * this.minLeafSupport) {
/*     */         
/*  77 */         taken++;
/*     */         
/*     */         continue;
/*     */       } 
/*  81 */       if (!leaf.split(this.trainingLabels, this.minLeafSupport)) {
/*  82 */         taken++;
/*     */         continue;
/*     */       } 
/*  85 */       insert(queue, leaf.getLeft());
/*  86 */       insert(queue, leaf.getRight());
/*     */     } 
/*     */     
/*  89 */     this.leaves = this.root.leaves();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double eval(DataPoint dp) {
/*  99 */     return this.root.eval(dp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Split> leaves() {
/* 107 */     return this.leaves;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearSamples() {
/* 114 */     this.trainingSamples = null;
/* 115 */     this.trainingLabels = null;
/* 116 */     this.features = null;
/* 117 */     this.thresholds = (float[][])null;
/* 118 */     this.index = null;
/* 119 */     this.hist = null;
/* 120 */     for (int i = 0; i < this.leaves.size(); i++) {
/* 121 */       ((Split)this.leaves.get(i)).clearSamples();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 129 */     if (this.root != null)
/* 130 */       return this.root.toString(); 
/* 131 */     return "";
/*     */   }
/*     */   
/*     */   public String toString(String indent) {
/* 135 */     if (this.root != null)
/* 136 */       return this.root.toString(indent); 
/* 137 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public double variance() {
/* 142 */     double var = 0.0D;
/* 143 */     for (int i = 0; i < this.leaves.size(); i++)
/* 144 */       var += ((Split)this.leaves.get(i)).getDeviance(); 
/* 145 */     return var;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void insert(List<Split> ls, Split s) {
/* 150 */     int i = 0;
/* 151 */     while (i < ls.size()) {
/*     */       
/* 153 */       if (((Split)ls.get(i)).getDeviance() > s.getDeviance()) {
/* 154 */         i++;
/*     */       }
/*     */     } 
/*     */     
/* 158 */     ls.add(i, s);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\learning\tree\RegressionTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */