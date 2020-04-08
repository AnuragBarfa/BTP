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
/*     */ 
/*     */ public class Split
/*     */ {
/*  24 */   private int featureID = -1;
/*  25 */   private float threshold = 0.0F;
/*  26 */   private double avgLabel = 0.0D;
/*     */ 
/*     */   
/*     */   private boolean isRoot = false;
/*     */   
/*  31 */   private double sumLabel = 0.0D;
/*  32 */   private double sqSumLabel = 0.0D;
/*  33 */   private Split left = null;
/*  34 */   private Split right = null;
/*  35 */   private double deviance = 0.0D;
/*  36 */   private int[][] sortedSampleIDs = (int[][])null;
/*  37 */   public int[] samples = null;
/*  38 */   public FeatureHistogram hist = null;
/*     */ 
/*     */ 
/*     */   
/*     */   public Split() {}
/*     */ 
/*     */   
/*     */   public Split(int featureID, float threshold, double deviance) {
/*  46 */     this.featureID = featureID;
/*  47 */     this.threshold = threshold;
/*  48 */     this.deviance = deviance;
/*     */   }
/*     */   
/*     */   public Split(int[][] sortedSampleIDs, double deviance, double sumLabel, double sqSumLabel) {
/*  52 */     this.sortedSampleIDs = sortedSampleIDs;
/*  53 */     this.deviance = deviance;
/*  54 */     this.sumLabel = sumLabel;
/*  55 */     this.sqSumLabel = sqSumLabel;
/*  56 */     this.avgLabel = sumLabel / (sortedSampleIDs[0]).length;
/*     */   }
/*     */   
/*     */   public Split(int[] samples, FeatureHistogram hist, double deviance, double sumLabel) {
/*  60 */     this.samples = samples;
/*  61 */     this.hist = hist;
/*  62 */     this.deviance = deviance;
/*  63 */     this.sumLabel = sumLabel;
/*  64 */     this.avgLabel = sumLabel / samples.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(int featureID, float threshold, double deviance) {
/*  69 */     this.featureID = featureID;
/*  70 */     this.threshold = threshold;
/*  71 */     this.deviance = deviance;
/*     */   }
/*     */   
/*     */   public void setLeft(Split s) {
/*  75 */     this.left = s;
/*     */   }
/*     */   
/*     */   public void setRight(Split s) {
/*  79 */     this.right = s;
/*     */   }
/*     */   
/*     */   public void setOutput(float output) {
/*  83 */     this.avgLabel = output;
/*     */   }
/*     */ 
/*     */   
/*     */   public Split getLeft() {
/*  88 */     return this.left;
/*     */   }
/*     */   
/*     */   public Split getRight() {
/*  92 */     return this.right;
/*     */   }
/*     */   
/*     */   public double getDeviance() {
/*  96 */     return this.deviance;
/*     */   }
/*     */   
/*     */   public double getOutput() {
/* 100 */     return this.avgLabel;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Split> leaves() {
/* 105 */     List<Split> list = new ArrayList<>();
/* 106 */     leaves(list);
/* 107 */     return list;
/*     */   }
/*     */   
/*     */   private void leaves(List<Split> leaves) {
/* 111 */     if (this.featureID == -1) {
/* 112 */       leaves.add(this);
/*     */     } else {
/*     */       
/* 115 */       this.left.leaves(leaves);
/* 116 */       this.right.leaves(leaves);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public double eval(DataPoint dp) {
/* 122 */     Split n = this;
/* 123 */     while (n.featureID != -1) {
/*     */       
/* 125 */       if (dp.getFeatureValue(n.featureID) <= n.threshold) {
/* 126 */         n = n.left; continue;
/*     */       } 
/* 128 */       n = n.right;
/*     */     } 
/* 130 */     return n.avgLabel;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 135 */     return toString("");
/*     */   }
/*     */   
/*     */   public String toString(String indent) {
/* 139 */     String strOutput = indent + "<split>\n";
/* 140 */     strOutput = strOutput + getString(indent + "\t");
/* 141 */     strOutput = strOutput + indent + "</split>\n";
/* 142 */     return strOutput;
/*     */   }
/*     */   
/*     */   public String getString(String indent) {
/* 146 */     String strOutput = "";
/* 147 */     if (this.featureID == -1) {
/*     */       
/* 149 */       strOutput = strOutput + indent + "<output> " + this.avgLabel + " </output>\n";
/*     */     }
/*     */     else {
/*     */       
/* 153 */       strOutput = strOutput + indent + "<feature> " + this.featureID + " </feature>\n";
/* 154 */       strOutput = strOutput + indent + "<threshold> " + this.threshold + " </threshold>\n";
/* 155 */       strOutput = strOutput + indent + "<split pos=\"left\">\n";
/* 156 */       strOutput = strOutput + this.left.getString(indent + "\t");
/* 157 */       strOutput = strOutput + indent + "</split>\n";
/* 158 */       strOutput = strOutput + indent + "<split pos=\"right\">\n";
/* 159 */       strOutput = strOutput + this.right.getString(indent + "\t");
/* 160 */       strOutput = strOutput + indent + "</split>\n";
/*     */     } 
/* 162 */     return strOutput;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean split(double[] trainingLabels, int minLeafSupport) {
/* 169 */     return this.hist.findBestSplit(this, trainingLabels, minLeafSupport);
/*     */   }
/*     */   
/*     */   public int[] getSamples() {
/* 173 */     if (this.sortedSampleIDs != null)
/* 174 */       return this.sortedSampleIDs[0]; 
/* 175 */     return this.samples;
/*     */   }
/*     */   
/*     */   public int[][] getSampleSortedIndex() {
/* 179 */     return this.sortedSampleIDs;
/*     */   }
/*     */   
/*     */   public double getSumLabel() {
/* 183 */     return this.sumLabel;
/*     */   }
/*     */   
/*     */   public double getSqSumLabel() {
/* 187 */     return this.sqSumLabel;
/*     */   }
/*     */   
/*     */   public void clearSamples() {
/* 191 */     this.sortedSampleIDs = (int[][])null;
/* 192 */     this.samples = null;
/* 193 */     this.hist = null;
/*     */   }
/*     */   
/*     */   public void setRoot(boolean isRoot) {
/* 197 */     this.isRoot = isRoot;
/*     */   }
/*     */   
/*     */   public boolean isRoot() {
/* 201 */     return this.isRoot;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\learning\tree\Split.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */