/*     */ package ciir.umass.edu.learning;
/*     */ 
/*     */ import ciir.umass.edu.metric.MetricScorer;
/*     */ import ciir.umass.edu.utilities.KeyValuePair;
/*     */ import ciir.umass.edu.utilities.MergeSorter;
/*     */ import ciir.umass.edu.utilities.RankLibError;
/*     */ import ciir.umass.edu.utilities.SimpleMath;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.StringReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
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
/*     */ 
/*     */ public class CoorAscent
/*     */   extends Ranker
/*     */ {
/*  34 */   public static int nRestart = 5;
/*  35 */   public static int nMaxIteration = 25;
/*  36 */   public static double stepBase = 0.05D;
/*  37 */   public static double stepScale = 2.0D;
/*  38 */   public static double tolerance = 0.001D;
/*     */   public static boolean regularized = false;
/*  40 */   public static double slack = 0.001D;
/*     */ 
/*     */   
/*  43 */   public double[] weight = null;
/*     */   
/*  45 */   protected int current_feature = -1;
/*  46 */   protected double weight_change = -1.0D;
/*     */ 
/*     */ 
/*     */   
/*     */   public CoorAscent() {}
/*     */ 
/*     */   
/*     */   public CoorAscent(List<RankList> samples, int[] features, MetricScorer scorer) {
/*  54 */     super(samples, features, scorer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void init() {
/*  59 */     PRINT("Initializing... ");
/*  60 */     this.weight = new double[this.features.length];
/*  61 */     Arrays.fill(this.weight, 1.0D / this.features.length);
/*  62 */     PRINTLN("[Done]");
/*     */   }
/*     */   
/*     */   public void learn() {
/*  66 */     double[] regVector = new double[this.weight.length];
/*  67 */     copy(this.weight, regVector);
/*     */ 
/*     */     
/*  70 */     double[] bestModel = null;
/*  71 */     double bestModelScore = 0.0D;
/*     */ 
/*     */     
/*  74 */     int[] sign = { 1, -1, 0 };
/*     */     
/*  76 */     PRINTLN("---------------------------");
/*  77 */     PRINTLN("Training starts...");
/*  78 */     PRINTLN("---------------------------");
/*     */     
/*  80 */     for (int r = 0; r < nRestart; r++) {
/*     */       
/*  82 */       PRINTLN("[+] Random restart #" + (r + 1) + "/" + nRestart + "...");
/*  83 */       int consecutive_fails = 0;
/*     */ 
/*     */       
/*  86 */       for (int i = 0; i < this.weight.length; i++) {
/*  87 */         this.weight[i] = (1.0F / this.features.length);
/*     */       }
/*  89 */       this.current_feature = -1;
/*  90 */       double startScore = this.scorer.score(rank(this.samples));
/*     */ 
/*     */       
/*  93 */       double bestScore = startScore;
/*  94 */       double[] bestWeight = new double[this.weight.length];
/*  95 */       copy(this.weight, bestWeight);
/*     */ 
/*     */       
/*  98 */       while ((this.weight.length > 1 && consecutive_fails < this.weight.length - 1) || (this.weight.length == 1 && consecutive_fails == 0)) {
/*     */         
/* 100 */         PRINTLN("Shuffling features' order... [Done.]");
/* 101 */         PRINTLN("Optimizing weight vector... ");
/* 102 */         PRINTLN("------------------------------");
/* 103 */         PRINTLN(new int[] { 7, 8, 7 }, new String[] { "Feature", "weight", this.scorer.name() });
/* 104 */         PRINTLN("------------------------------");
/*     */         
/* 106 */         int[] fids = getShuffledFeatures();
/*     */         
/* 108 */         for (int j = 0; j < fids.length; j++) {
/*     */           
/* 110 */           this.current_feature = fids[j];
/*     */           
/* 112 */           double origWeight = this.weight[fids[j]];
/* 113 */           double totalStep = 0.0D;
/* 114 */           double bestTotalStep = 0.0D;
/* 115 */           boolean succeeds = false;
/* 116 */           for (int s = 0; s < sign.length; s++) {
/*     */             
/* 118 */             int dir = sign[s];
/* 119 */             double step = 0.001D * dir;
/* 120 */             if (origWeight != 0.0D && Math.abs(step) > 0.5D * Math.abs(origWeight))
/* 121 */               step = stepBase * Math.abs(origWeight); 
/* 122 */             totalStep = step;
/* 123 */             int numIter = nMaxIteration;
/* 124 */             if (dir == 0) {
/* 125 */               numIter = 1;
/* 126 */               totalStep = -origWeight;
/*     */             } 
/* 128 */             for (int k = 0; k < numIter; k++) {
/*     */               
/* 130 */               double w = origWeight + totalStep;
/* 131 */               this.weight_change = step;
/* 132 */               this.weight[fids[j]] = w;
/* 133 */               double score = this.scorer.score(rank(this.samples));
/* 134 */               if (regularized) {
/*     */                 
/* 136 */                 double penalty = slack * getDistance(this.weight, regVector);
/* 137 */                 score -= penalty;
/*     */               } 
/*     */               
/* 140 */               if (score > bestScore) {
/*     */                 
/* 142 */                 bestScore = score;
/* 143 */                 bestTotalStep = totalStep;
/* 144 */                 succeeds = true;
/* 145 */                 String bw = ((this.weight[fids[j]] > 0.0D) ? "+" : "") + SimpleMath.round(this.weight[fids[j]], 4);
/* 146 */                 PRINTLN(new int[] { 7, 8, 7 }, new String[] { this.features[fids[j]] + "", bw + "", SimpleMath.round(bestScore, 4) + "" });
/*     */               } 
/* 148 */               if (k < nMaxIteration - 1) {
/*     */                 
/* 150 */                 step *= stepScale;
/* 151 */                 totalStep += step;
/*     */               } 
/*     */             } 
/* 154 */             if (succeeds)
/*     */               break; 
/* 156 */             if (s < sign.length - 1) {
/*     */               
/* 158 */               this.weight_change = -totalStep;
/* 159 */               updateCached();
/*     */               
/* 161 */               this.weight[fids[j]] = origWeight;
/*     */             } 
/*     */           } 
/* 164 */           if (succeeds) {
/*     */             
/* 166 */             this.weight_change = bestTotalStep - totalStep;
/* 167 */             updateCached();
/* 168 */             this.weight[fids[j]] = origWeight + bestTotalStep;
/* 169 */             consecutive_fails = 0;
/* 170 */             double sum = normalize(this.weight);
/* 171 */             scaleCached(sum);
/* 172 */             copy(this.weight, bestWeight);
/*     */           }
/*     */           else {
/*     */             
/* 176 */             consecutive_fails++;
/* 177 */             this.weight_change = -totalStep;
/* 178 */             updateCached();
/*     */             
/* 180 */             this.weight[fids[j]] = origWeight;
/*     */           } 
/*     */         } 
/* 183 */         PRINTLN("------------------------------");
/*     */ 
/*     */         
/* 186 */         if (bestScore - startScore < tolerance) {
/*     */           break;
/*     */         }
/*     */       } 
/* 190 */       if (this.validationSamples != null) {
/*     */         
/* 192 */         this.current_feature = -1;
/* 193 */         bestScore = this.scorer.score(rank(this.validationSamples));
/*     */       } 
/* 195 */       if (bestModel == null || bestScore > bestModelScore) {
/*     */         
/* 197 */         bestModelScore = bestScore;
/* 198 */         bestModel = bestWeight;
/*     */       } 
/*     */     } 
/*     */     
/* 202 */     copy(bestModel, this.weight);
/* 203 */     this.current_feature = -1;
/* 204 */     this.scoreOnTrainingData = SimpleMath.round(this.scorer.score(rank(this.samples)), 4);
/* 205 */     PRINTLN("---------------------------------");
/* 206 */     PRINTLN("Finished sucessfully.");
/* 207 */     PRINTLN(this.scorer.name() + " on training data: " + this.scoreOnTrainingData);
/*     */     
/* 209 */     if (this.validationSamples != null) {
/*     */       
/* 211 */       this.bestScoreOnValidationData = this.scorer.score(rank(this.validationSamples));
/* 212 */       PRINTLN(this.scorer.name() + " on validation data: " + SimpleMath.round(this.bestScoreOnValidationData, 4));
/*     */     } 
/* 214 */     PRINTLN("---------------------------------");
/*     */   }
/*     */   
/*     */   public RankList rank(RankList rl) {
/* 218 */     double[] score = new double[rl.size()];
/* 219 */     if (this.current_feature == -1) {
/*     */       
/* 221 */       for (int i = 0; i < rl.size(); i++)
/*     */       {
/* 223 */         for (int j = 0; j < this.features.length; j++)
/* 224 */           score[i] = score[i] + this.weight[j] * rl.get(i).getFeatureValue(this.features[j]); 
/* 225 */         rl.get(i).setCached(score[i]);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 230 */       for (int i = 0; i < rl.size(); i++) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 235 */         score[i] = rl.get(i).getCached() + this.weight_change * rl.get(i).getFeatureValue(this.features[this.current_feature]);
/* 236 */         rl.get(i).setCached(score[i]);
/*     */       } 
/*     */     } 
/* 239 */     int[] idx = MergeSorter.sort(score, false);
/* 240 */     return new RankList(rl, idx);
/*     */   }
/*     */   
/*     */   public double eval(DataPoint p) {
/* 244 */     double score = 0.0D;
/* 245 */     for (int i = 0; i < this.features.length; i++)
/* 246 */       score += this.weight[i] * p.getFeatureValue(this.features[i]); 
/* 247 */     return score;
/*     */   }
/*     */   
/*     */   public Ranker createNew() {
/* 251 */     return new CoorAscent();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 255 */     String output = "";
/* 256 */     for (int i = 0; i < this.weight.length; i++)
/* 257 */       output = output + this.features[i] + ":" + this.weight[i] + ((i == this.weight.length - 1) ? "" : " "); 
/* 258 */     return output;
/*     */   }
/*     */   
/*     */   public String model() {
/* 262 */     String output = "## " + name() + "\n";
/* 263 */     output = output + "## Restart = " + nRestart + "\n";
/* 264 */     output = output + "## MaxIteration = " + nMaxIteration + "\n";
/* 265 */     output = output + "## StepBase = " + stepBase + "\n";
/* 266 */     output = output + "## StepScale = " + stepScale + "\n";
/* 267 */     output = output + "## Tolerance = " + tolerance + "\n";
/* 268 */     output = output + "## Regularized = " + regularized + "\n";
/* 269 */     output = output + "## Slack = " + slack + "\n";
/* 270 */     output = output + toString();
/* 271 */     return output;
/*     */   }
/*     */   
/*     */   public void loadFromString(String fullText) {
/*     */     try {
/* 276 */       String content = "";
/* 277 */       BufferedReader in = new BufferedReader(new StringReader(fullText));
/*     */       
/* 279 */       KeyValuePair kvp = null;
/* 280 */       while ((content = in.readLine()) != null) {
/*     */         
/* 282 */         content = content.trim();
/* 283 */         if (content.length() == 0)
/*     */           continue; 
/* 285 */         if (content.indexOf("##") == 0)
/*     */           continue; 
/* 287 */         kvp = new KeyValuePair(content);
/*     */       } 
/*     */       
/* 290 */       in.close();
/* 291 */       assert kvp != null;
/*     */       
/* 293 */       List<String> keys = kvp.keys();
/* 294 */       List<String> values = kvp.values();
/* 295 */       this.weight = new double[keys.size()];
/* 296 */       this.features = new int[keys.size()];
/* 297 */       for (int i = 0; i < keys.size(); i++)
/*     */       {
/* 299 */         this.features[i] = Integer.parseInt((String)keys.get(i));
/* 300 */         this.weight[i] = Double.parseDouble((String)values.get(i));
/*     */       }
/*     */     
/* 303 */     } catch (Exception ex) {
/*     */       
/* 305 */       throw RankLibError.create("Error in CoorAscent::load(): ", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void printParameters() {
/* 310 */     PRINTLN("No. of random restarts: " + nRestart);
/* 311 */     PRINTLN("No. of iterations to search in each direction: " + nMaxIteration);
/* 312 */     PRINTLN("Tolerance: " + tolerance);
/* 313 */     if (regularized) {
/* 314 */       PRINTLN("Reg. param: " + slack);
/*     */     } else {
/* 316 */       PRINTLN("Regularization: No");
/*     */     } 
/*     */   }
/*     */   public String name() {
/* 320 */     return "Coordinate Ascent";
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateCached() {
/* 325 */     for (int j = 0; j < this.samples.size(); j++) {
/*     */       
/* 327 */       RankList rl = this.samples.get(j);
/* 328 */       for (int i = 0; i < rl.size(); i++) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 333 */         double score = rl.get(i).getCached() + this.weight_change * rl.get(i).getFeatureValue(this.features[this.current_feature]);
/* 334 */         rl.get(i).setCached(score);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void scaleCached(double sum) {
/* 340 */     for (int j = 0; j < this.samples.size(); j++) {
/*     */       
/* 342 */       RankList rl = this.samples.get(j);
/* 343 */       for (int i = 0; i < rl.size(); i++)
/* 344 */         rl.get(i).setCached(rl.get(i).getCached() / sum); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private int[] getShuffledFeatures() {
/* 349 */     int[] fids = new int[this.features.length];
/* 350 */     List<Integer> l = new ArrayList<>(); int i;
/* 351 */     for (i = 0; i < this.features.length; i++)
/* 352 */       l.add(Integer.valueOf(i)); 
/* 353 */     Collections.shuffle(l);
/* 354 */     for (i = 0; i < l.size(); i++)
/* 355 */       fids[i] = ((Integer)l.get(i)).intValue(); 
/* 356 */     return fids;
/*     */   }
/*     */   
/*     */   private double getDistance(double[] w1, double[] w2) {
/* 360 */     assert w1.length == w2.length;
/* 361 */     double s1 = 0.0D;
/* 362 */     double s2 = 0.0D;
/* 363 */     for (int i = 0; i < w1.length; i++) {
/*     */       
/* 365 */       s1 += Math.abs(w1[i]);
/* 366 */       s2 += Math.abs(w2[i]);
/*     */     } 
/* 368 */     double dist = 0.0D;
/* 369 */     for (int j = 0; j < w1.length; j++) {
/*     */       
/* 371 */       double t = w1[j] / s1 - w2[j] / s2;
/* 372 */       dist += t * t;
/*     */     } 
/* 374 */     return Math.sqrt(dist);
/*     */   }
/*     */   
/*     */   private double normalize(double[] weights) {
/* 378 */     double sum = 0.0D; int j;
/* 379 */     for (j = 0; j < weights.length; j++)
/* 380 */       sum += Math.abs(weights[j]); 
/* 381 */     if (sum > 0.0D) {
/*     */       
/* 383 */       for (j = 0; j < weights.length; j++) {
/* 384 */         weights[j] = weights[j] / sum;
/*     */       }
/*     */     } else {
/*     */       
/* 388 */       sum = 1.0D;
/* 389 */       for (j = 0; j < weights.length; j++)
/* 390 */         weights[j] = 1.0D / weights.length; 
/*     */     } 
/* 392 */     return sum;
/*     */   }
/*     */ 
/*     */   
/*     */   public void copyModel(CoorAscent ranker) {
/* 397 */     this.weight = new double[this.features.length];
/* 398 */     if (ranker.weight.length != this.weight.length) {
/*     */       
/* 400 */       System.out.println("These two models use different feature set!!");
/* 401 */       System.exit(1);
/*     */     } 
/* 403 */     copy(ranker.weight, this.weight);
/* 404 */     PRINTLN("Model loaded.");
/*     */   }
/*     */   
/*     */   public double distance(CoorAscent ca) {
/* 408 */     return getDistance(this.weight, ca.weight);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\learning\CoorAscent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */