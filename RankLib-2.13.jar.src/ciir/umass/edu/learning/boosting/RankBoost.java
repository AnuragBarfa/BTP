/*     */ package ciir.umass.edu.learning.boosting;
/*     */ 
/*     */ import ciir.umass.edu.learning.DataPoint;
/*     */ import ciir.umass.edu.learning.RankList;
/*     */ import ciir.umass.edu.learning.Ranker;
/*     */ import ciir.umass.edu.metric.MetricScorer;
/*     */ import ciir.umass.edu.utilities.MergeSorter;
/*     */ import ciir.umass.edu.utilities.RankLibError;
/*     */ import ciir.umass.edu.utilities.SimpleMath;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.StringReader;
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
/*     */ public class RankBoost
/*     */   extends Ranker
/*     */ {
/*  33 */   public static int nIteration = 300;
/*  34 */   public static int nThreshold = 10;
/*     */   
/*  36 */   protected double[][][] sweight = (double[][][])null;
/*  37 */   protected double[][] potential = (double[][])null;
/*  38 */   protected List<List<int[]>> sortedSamples = new ArrayList<>();
/*  39 */   protected double[][] thresholds = (double[][])null;
/*  40 */   protected int[][] tSortedIdx = (int[][])null;
/*     */   
/*  42 */   protected List<RBWeakRanker> wRankers = null;
/*  43 */   protected List<Double> rWeight = null;
/*     */ 
/*     */   
/*  46 */   protected List<RBWeakRanker> bestModelRankers = new ArrayList<>();
/*  47 */   protected List<Double> bestModelWeights = new ArrayList<>();
/*     */   
/*  49 */   private double R_t = 0.0D;
/*  50 */   private double Z_t = 1.0D;
/*  51 */   private int totalCorrectPairs = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   public RankBoost() {}
/*     */ 
/*     */   
/*     */   public RankBoost(List<RankList> samples, int[] features, MetricScorer scorer) {
/*  59 */     super(samples, features, scorer);
/*     */   }
/*     */ 
/*     */   
/*     */   private int[] reorder(RankList rl, int fid) {
/*  64 */     double[] score = new double[rl.size()];
/*  65 */     for (int i = 0; i < rl.size(); i++)
/*  66 */       score[i] = rl.get(i).getFeatureValue(fid); 
/*  67 */     return MergeSorter.sort(score, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void updatePotential() {
/*  75 */     for (int i = 0; i < this.samples.size(); i++) {
/*     */       
/*  77 */       RankList rl = this.samples.get(i);
/*  78 */       for (int j = 0; j < rl.size(); j++) {
/*     */         
/*  80 */         double p = 0.0D; int k;
/*  81 */         for (k = j + 1; k < rl.size(); k++)
/*  82 */           p += this.sweight[i][j][k]; 
/*  83 */         for (k = 0; k < j; k++)
/*  84 */           p -= this.sweight[i][k][j]; 
/*  85 */         this.potential[i][j] = p;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private RBWeakRanker learnWeakRanker() {
/*  97 */     int bestFid = -1;
/*  98 */     double maxR = -10.0D;
/*  99 */     double bestThreshold = -1.0D;
/* 100 */     for (int i = 0; i < this.features.length; i++) {
/*     */       
/* 102 */       List<int[]> sSortedIndex = this.sortedSamples.get(i);
/* 103 */       int[] idx = this.tSortedIdx[i];
/* 104 */       int[] last = new int[this.samples.size()];
/* 105 */       for (int j = 0; j < this.samples.size(); j++) {
/* 106 */         last[j] = -1;
/*     */       }
/* 108 */       double r = 0.0D;
/* 109 */       for (int k = 0; k < idx.length; k++) {
/*     */         
/* 111 */         double t = this.thresholds[i][idx[k]];
/*     */         
/* 113 */         for (int m = 0; m < this.samples.size(); m++) {
/*     */           
/* 115 */           RankList rl = this.samples.get(m);
/* 116 */           int[] sk = sSortedIndex.get(m);
/* 117 */           for (int l = last[m] + 1; l < rl.size(); ) {
/*     */             
/* 119 */             DataPoint p = rl.get(sk[l]);
/* 120 */             if (p.getFeatureValue(this.features[i]) > t) {
/*     */               
/* 122 */               r += this.potential[m][sk[l]];
/* 123 */               last[m] = l;
/*     */               
/*     */               l++;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 130 */         if (r > maxR) {
/*     */           
/* 132 */           maxR = r;
/* 133 */           bestThreshold = t;
/* 134 */           bestFid = this.features[i];
/*     */         } 
/*     */       } 
/*     */     } 
/* 138 */     if (bestFid == -1) {
/* 139 */       return null;
/*     */     }
/* 141 */     this.R_t = this.Z_t * maxR;
/*     */     
/* 143 */     return new RBWeakRanker(bestFid, bestThreshold);
/*     */   }
/*     */ 
/*     */   
/*     */   public void init() {
/* 148 */     PRINT("Initializing... ");
/*     */     
/* 150 */     this.wRankers = new ArrayList<>();
/* 151 */     this.rWeight = new ArrayList<>();
/*     */ 
/*     */ 
/*     */     
/* 155 */     this.totalCorrectPairs = 0; int i;
/* 156 */     for (i = 0; i < this.samples.size(); i++) {
/*     */       
/* 158 */       this.samples.set(i, ((RankList)this.samples.get(i)).getCorrectRanking());
/* 159 */       RankList rl = this.samples.get(i);
/* 160 */       for (int j = 0; j < rl.size() - 1; j++) {
/* 161 */         for (int k = rl.size() - 1; k >= j + 1 && rl.get(j).getLabel() > rl.get(k).getLabel(); k--)
/*     */         {
/*     */           
/* 164 */           this.totalCorrectPairs++;
/*     */         }
/*     */       } 
/*     */     } 
/* 168 */     this.sweight = new double[this.samples.size()][][];
/* 169 */     for (i = 0; i < this.samples.size(); i++) {
/*     */       
/* 171 */       RankList rl = this.samples.get(i);
/* 172 */       this.sweight[i] = new double[rl.size()][];
/* 173 */       for (int j = 0; j < rl.size() - 1; j++) {
/*     */         
/* 175 */         this.sweight[i][j] = new double[rl.size()];
/* 176 */         for (int k = j + 1; k < rl.size(); k++) {
/* 177 */           if (rl.get(j).getLabel() > rl.get(k).getLabel()) {
/* 178 */             this.sweight[i][j][k] = 1.0D / this.totalCorrectPairs;
/*     */           } else {
/* 180 */             this.sweight[i][j][k] = 0.0D;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 185 */     this.potential = new double[this.samples.size()][];
/* 186 */     for (i = 0; i < this.samples.size(); i++) {
/* 187 */       this.potential[i] = new double[((RankList)this.samples.get(i)).size()];
/*     */     }
/* 189 */     if (nThreshold <= 0) {
/*     */ 
/*     */       
/* 192 */       int count = 0; int j;
/* 193 */       for (j = 0; j < this.samples.size(); j++) {
/* 194 */         count += ((RankList)this.samples.get(j)).size();
/*     */       }
/* 196 */       this.thresholds = new double[this.features.length][];
/* 197 */       for (j = 0; j < this.features.length; j++) {
/* 198 */         this.thresholds[j] = new double[count];
/*     */       }
/* 200 */       int c = 0;
/* 201 */       for (int k = 0; k < this.samples.size(); k++) {
/*     */         
/* 203 */         RankList rl = this.samples.get(k);
/* 204 */         for (int m = 0; m < rl.size(); m++)
/*     */         {
/* 206 */           for (int n = 0; n < this.features.length; n++)
/* 207 */             this.thresholds[n][c] = rl.get(m).getFeatureValue(this.features[n]); 
/* 208 */           c++;
/*     */         }
/*     */       
/*     */       } 
/*     */     } else {
/*     */       
/* 214 */       double[] fmax = new double[this.features.length];
/* 215 */       double[] fmin = new double[this.features.length]; int j;
/* 216 */       for (j = 0; j < this.features.length; j++) {
/*     */         
/* 218 */         fmax[j] = -1000000.0D;
/* 219 */         fmin[j] = 1000000.0D;
/*     */       } 
/*     */       
/* 222 */       for (j = 0; j < this.samples.size(); j++) {
/*     */         
/* 224 */         RankList rl = this.samples.get(j);
/* 225 */         for (int k = 0; k < rl.size(); k++) {
/*     */           
/* 227 */           for (int m = 0; m < this.features.length; m++) {
/*     */             
/* 229 */             double f = rl.get(k).getFeatureValue(this.features[m]);
/* 230 */             if (f > fmax[m])
/* 231 */               fmax[m] = f; 
/* 232 */             if (f < fmin[m]) {
/* 233 */               fmin[m] = f;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/* 238 */       this.thresholds = new double[this.features.length][];
/* 239 */       for (j = 0; j < this.features.length; j++) {
/*     */         
/* 241 */         double step = Math.abs(fmax[j] - fmin[j]) / nThreshold;
/* 242 */         this.thresholds[j] = new double[nThreshold + 1];
/* 243 */         this.thresholds[j][0] = fmax[j];
/* 244 */         for (int k = 1; k < nThreshold; k++)
/* 245 */           this.thresholds[j][k] = this.thresholds[j][k - 1] - step; 
/* 246 */         this.thresholds[j][nThreshold] = fmin[j] - 1.0E8D;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 251 */     this.tSortedIdx = new int[this.features.length][];
/* 252 */     for (i = 0; i < this.features.length; i++) {
/* 253 */       this.tSortedIdx[i] = MergeSorter.sort(this.thresholds[i], false);
/*     */     }
/*     */ 
/*     */     
/* 257 */     for (i = 0; i < this.features.length; i++) {
/*     */       
/* 259 */       List<int[]> idx = (List)new ArrayList<>();
/* 260 */       for (int j = 0; j < this.samples.size(); j++)
/* 261 */         idx.add(reorder(this.samples.get(j), this.features[i])); 
/* 262 */       this.sortedSamples.add(idx);
/*     */     } 
/* 264 */     PRINTLN("[Done]");
/*     */   }
/*     */   
/*     */   public void learn() {
/* 268 */     PRINTLN("------------------------------------------");
/* 269 */     PRINTLN("Training starts...");
/* 270 */     PRINTLN("--------------------------------------------------------------------");
/* 271 */     PRINTLN(new int[] { 7, 8, 9, 9, 9, 9 }, new String[] { "#iter", "Sel. F.", "Threshold", "Error", this.scorer.name() + "-T", this.scorer.name() + "-V" });
/* 272 */     PRINTLN("--------------------------------------------------------------------");
/*     */     
/* 274 */     for (int t = 1; t <= nIteration; t++) {
/*     */       
/* 276 */       updatePotential();
/*     */       
/* 278 */       RBWeakRanker wr = learnWeakRanker();
/* 279 */       if (wr == null) {
/*     */         break;
/*     */       }
/* 282 */       double alpha_t = 0.5D * SimpleMath.ln((this.Z_t + this.R_t) / (this.Z_t - this.R_t));
/*     */       
/* 284 */       this.wRankers.add(wr);
/* 285 */       this.rWeight.add(Double.valueOf(alpha_t));
/*     */ 
/*     */       
/* 288 */       this.Z_t = 0.0D; int i;
/* 289 */       for (i = 0; i < this.samples.size(); i++) {
/*     */         
/* 291 */         RankList rl = this.samples.get(i);
/* 292 */         double[][] D_t = new double[rl.size()][];
/* 293 */         for (int j = 0; j < rl.size() - 1; j++) {
/*     */           
/* 295 */           D_t[j] = new double[rl.size()];
/* 296 */           for (int k = j + 1; k < rl.size(); k++) {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 301 */             D_t[j][k] = this.sweight[i][j][k] * Math.exp(alpha_t * (wr.score(rl.get(k)) - wr.score(rl.get(j))));
/* 302 */             this.Z_t += D_t[j][k];
/*     */           } 
/*     */         } 
/* 305 */         this.sweight[i] = D_t;
/*     */       } 
/*     */       
/* 308 */       PRINT(new int[] { 7, 8, 9, 9 }, new String[] { t + "", wr.getFid() + "", SimpleMath.round(wr.getThreshold(), 4) + "", SimpleMath.round(this.R_t, 4) + "" });
/* 309 */       if (t % 1 == 0) {
/*     */         
/* 311 */         PRINT(new int[] { 9 }, new String[] { SimpleMath.round(this.scorer.score(rank(this.samples)), 4) + "" });
/* 312 */         if (this.validationSamples != null) {
/*     */           
/* 314 */           double score = this.scorer.score(rank(this.validationSamples));
/* 315 */           if (score > this.bestScoreOnValidationData) {
/*     */             
/* 317 */             this.bestScoreOnValidationData = score;
/* 318 */             this.bestModelRankers.clear();
/* 319 */             this.bestModelRankers.addAll(this.wRankers);
/* 320 */             this.bestModelWeights.clear();
/* 321 */             this.bestModelWeights.addAll(this.rWeight);
/*     */           } 
/* 323 */           PRINT(new int[] { 9 }, new String[] { SimpleMath.round(score, 4) + "" });
/*     */         } 
/*     */       } 
/* 326 */       PRINTLN("");
/*     */ 
/*     */ 
/*     */       
/* 330 */       for (i = 0; i < this.samples.size(); i++) {
/*     */         
/* 332 */         RankList rl = this.samples.get(i);
/* 333 */         for (int j = 0; j < rl.size() - 1; j++) {
/* 334 */           for (int k = j + 1; k < rl.size(); k++)
/* 335 */             this.sweight[i][j][k] = this.sweight[i][j][k] / this.Z_t; 
/*     */         } 
/*     */       } 
/* 338 */       System.gc();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 343 */     if (this.validationSamples != null && this.bestModelRankers.size() > 0) {
/*     */       
/* 345 */       this.wRankers.clear();
/* 346 */       this.rWeight.clear();
/* 347 */       this.wRankers.addAll(this.bestModelRankers);
/* 348 */       this.rWeight.addAll(this.bestModelWeights);
/*     */     } 
/*     */     
/* 351 */     this.scoreOnTrainingData = SimpleMath.round(this.scorer.score(rank(this.samples)), 4);
/* 352 */     PRINTLN("--------------------------------------------------------------------");
/* 353 */     PRINTLN("Finished sucessfully.");
/* 354 */     PRINTLN(this.scorer.name() + " on training data: " + this.scoreOnTrainingData);
/* 355 */     if (this.validationSamples != null) {
/*     */       
/* 357 */       this.bestScoreOnValidationData = this.scorer.score(rank(this.validationSamples));
/* 358 */       PRINTLN(this.scorer.name() + " on validation data: " + SimpleMath.round(this.bestScoreOnValidationData, 4));
/*     */     } 
/* 360 */     PRINTLN("---------------------------------");
/*     */   }
/*     */   
/*     */   public double eval(DataPoint p) {
/* 364 */     double score = 0.0D;
/* 365 */     for (int j = 0; j < this.wRankers.size(); j++)
/* 366 */       score += ((Double)this.rWeight.get(j)).doubleValue() * ((RBWeakRanker)this.wRankers.get(j)).score(p); 
/* 367 */     return score;
/*     */   }
/*     */   
/*     */   public Ranker createNew() {
/* 371 */     return new RankBoost();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 375 */     String output = "";
/* 376 */     for (int i = 0; i < this.wRankers.size(); i++)
/* 377 */       output = output + ((RBWeakRanker)this.wRankers.get(i)).toString() + ":" + this.rWeight.get(i) + ((i == this.rWeight.size() - 1) ? "" : " "); 
/* 378 */     return output;
/*     */   }
/*     */   
/*     */   public String model() {
/* 382 */     String output = "## " + name() + "\n";
/* 383 */     output = output + "## Iteration = " + nIteration + "\n";
/* 384 */     output = output + "## No. of threshold candidates = " + nThreshold + "\n";
/* 385 */     output = output + toString();
/* 386 */     return output;
/*     */   }
/*     */   
/*     */   public void loadFromString(String fullText) {
/*     */     try {
/* 391 */       String content = "";
/* 392 */       BufferedReader in = new BufferedReader(new StringReader(fullText));
/*     */       
/* 394 */       while ((content = in.readLine()) != null) {
/*     */         
/* 396 */         content = content.trim();
/* 397 */         if (content.length() == 0)
/*     */           continue; 
/* 399 */         if (content.indexOf("##") == 0);
/*     */       } 
/*     */ 
/*     */       
/* 403 */       in.close();
/*     */       
/* 405 */       this.rWeight = new ArrayList<>();
/* 406 */       this.wRankers = new ArrayList<>();
/*     */       
/* 408 */       int idx = content.lastIndexOf("#");
/* 409 */       if (idx != -1) {
/* 410 */         content = content.substring(0, idx).trim();
/*     */       }
/* 412 */       String[] fs = content.split(" "); int i;
/* 413 */       for (i = 0; i < fs.length; i++) {
/*     */         
/* 415 */         fs[i] = fs[i].trim();
/* 416 */         if (fs[i].compareTo("") != 0) {
/*     */           
/* 418 */           String[] strs = fs[i].split(":");
/* 419 */           int fid = Integer.parseInt(strs[0]);
/* 420 */           double threshold = Double.parseDouble(strs[1]);
/* 421 */           double weight = Double.parseDouble(strs[2]);
/* 422 */           this.rWeight.add(Double.valueOf(weight));
/* 423 */           this.wRankers.add(new RBWeakRanker(fid, threshold));
/*     */         } 
/*     */       } 
/* 426 */       this.features = new int[this.rWeight.size()];
/* 427 */       for (i = 0; i < this.rWeight.size(); i++) {
/* 428 */         this.features[i] = ((RBWeakRanker)this.wRankers.get(i)).getFid();
/*     */       }
/* 430 */     } catch (Exception ex) {
/*     */       
/* 432 */       throw RankLibError.create("Error in RankBoost::load(): ", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void printParameters() {
/* 437 */     PRINTLN("No. of rounds: " + nIteration);
/* 438 */     PRINTLN("No. of threshold candidates: " + nThreshold);
/*     */   }
/*     */   
/*     */   public String name() {
/* 442 */     return "RankBoost";
/*     */   }
/*     */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\learning\boosting\RankBoost.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */