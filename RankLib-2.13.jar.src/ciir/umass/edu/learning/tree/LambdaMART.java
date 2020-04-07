/*     */ package ciir.umass.edu.learning.tree;
/*     */ 
/*     */ import ciir.umass.edu.learning.DataPoint;
/*     */ import ciir.umass.edu.learning.RankList;
/*     */ import ciir.umass.edu.learning.Ranker;
/*     */ import ciir.umass.edu.metric.MetricScorer;
/*     */ import ciir.umass.edu.utilities.MergeSorter;
/*     */ import ciir.umass.edu.utilities.MyThreadPool;
/*     */ import ciir.umass.edu.utilities.RankLibError;
/*     */ import ciir.umass.edu.utilities.SimpleMath;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.StringReader;
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
/*     */ 
/*     */ 
/*     */ public class LambdaMART
/*     */   extends Ranker
/*     */ {
/*  36 */   public static int nTrees = 1000;
/*  37 */   public static float learningRate = 0.1F;
/*  38 */   public static int nThreshold = 256;
/*  39 */   public static int nRoundToStopEarly = 100;
/*  40 */   public static int nTreeLeaves = 10;
/*  41 */   public static int minLeafSupport = 1;
/*     */ 
/*     */   
/*  44 */   public static int gcCycle = 100;
/*     */ 
/*     */   
/*  47 */   protected float[][] thresholds = (float[][])null;
/*  48 */   protected Ensemble ensemble = null;
/*  49 */   protected double[] modelScores = null;
/*     */   
/*  51 */   protected double[][] modelScoresOnValidation = (double[][])null;
/*  52 */   protected int bestModelOnValidation = 2147483645;
/*     */ 
/*     */   
/*  55 */   protected DataPoint[] martSamples = null;
/*  56 */   protected int[][] sortedIdx = (int[][])null;
/*  57 */   protected FeatureHistogram hist = null;
/*  58 */   protected double[] pseudoResponses = null;
/*  59 */   protected double[] weights = null;
/*     */ 
/*     */ 
/*     */   
/*     */   public LambdaMART() {}
/*     */ 
/*     */   
/*     */   public LambdaMART(List<RankList> samples, int[] features, MetricScorer scorer) {
/*  67 */     super(samples, features, scorer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void init() {
/*  72 */     PRINT("Initializing... ");
/*     */     
/*  74 */     int dpCount = 0;
/*  75 */     for (int i = 0; i < this.samples.size(); i++) {
/*     */       
/*  77 */       RankList rl = this.samples.get(i);
/*  78 */       dpCount += rl.size();
/*     */     } 
/*  80 */     int current = 0;
/*  81 */     this.martSamples = new DataPoint[dpCount];
/*  82 */     this.modelScores = new double[dpCount];
/*  83 */     this.pseudoResponses = new double[dpCount];
/*  84 */     this.weights = new double[dpCount];
/*  85 */     for (int j = 0; j < this.samples.size(); j++) {
/*     */       
/*  87 */       RankList rl = this.samples.get(j);
/*  88 */       for (int k = 0; k < rl.size(); k++) {
/*     */         
/*  90 */         this.martSamples[current + k] = rl.get(k);
/*  91 */         this.modelScores[current + k] = 0.0D;
/*  92 */         this.pseudoResponses[current + k] = 0.0D;
/*  93 */         this.weights[current + k] = 0.0D;
/*     */       } 
/*  95 */       current += rl.size();
/*     */     } 
/*     */ 
/*     */     
/*  99 */     this.sortedIdx = new int[this.features.length][];
/* 100 */     MyThreadPool p = MyThreadPool.getInstance();
/* 101 */     if (p.size() == 1) {
/* 102 */       sortSamplesByFeature(0, this.features.length - 1);
/*     */     } else {
/*     */       
/* 105 */       int[] partition = p.partition(this.features.length);
/* 106 */       for (int k = 0; k < partition.length - 1; k++)
/* 107 */         p.execute(new SortWorker(this, partition[k], partition[k + 1] - 1)); 
/* 108 */       p.await();
/*     */     } 
/*     */ 
/*     */     
/* 112 */     this.thresholds = new float[this.features.length][];
/* 113 */     for (int f = 0; f < this.features.length; f++) {
/*     */ 
/*     */       
/* 116 */       List<Float> values = new ArrayList<>();
/* 117 */       float fmax = Float.NEGATIVE_INFINITY;
/* 118 */       float fmin = Float.MAX_VALUE; int k;
/* 119 */       for (k = 0; k < this.martSamples.length; k++) {
/*     */         
/* 121 */         int m = this.sortedIdx[f][k];
/* 122 */         float fv = this.martSamples[m].getFeatureValue(this.features[f]);
/* 123 */         values.add(Float.valueOf(fv));
/* 124 */         if (fmax < fv)
/* 125 */           fmax = fv; 
/* 126 */         if (fmin > fv) {
/* 127 */           fmin = fv;
/*     */         }
/* 129 */         int n = k + 1;
/* 130 */         while (n < this.martSamples.length) {
/*     */           
/* 132 */           if (this.martSamples[this.sortedIdx[f][n]].getFeatureValue(this.features[f]) > fv)
/*     */             break; 
/* 134 */           n++;
/*     */         } 
/* 136 */         k = n - 1;
/*     */       } 
/*     */       
/* 139 */       if (values.size() <= nThreshold || nThreshold == -1) {
/*     */         
/* 141 */         this.thresholds[f] = new float[values.size() + 1];
/* 142 */         for (k = 0; k < values.size(); k++)
/* 143 */           this.thresholds[f][k] = ((Float)values.get(k)).floatValue(); 
/* 144 */         this.thresholds[f][values.size()] = Float.MAX_VALUE;
/*     */       }
/*     */       else {
/*     */         
/* 148 */         float step = Math.abs(fmax - fmin) / nThreshold;
/* 149 */         this.thresholds[f] = new float[nThreshold + 1];
/* 150 */         this.thresholds[f][0] = fmin;
/* 151 */         for (int m = 1; m < nThreshold; m++)
/* 152 */           this.thresholds[f][m] = this.thresholds[f][m - 1] + step; 
/* 153 */         this.thresholds[f][nThreshold] = Float.MAX_VALUE;
/*     */       } 
/*     */     } 
/*     */     
/* 157 */     if (this.validationSamples != null) {
/*     */       
/* 159 */       this.modelScoresOnValidation = new double[this.validationSamples.size()][];
/* 160 */       for (int k = 0; k < this.validationSamples.size(); k++) {
/*     */         
/* 162 */         this.modelScoresOnValidation[k] = new double[((RankList)this.validationSamples.get(k)).size()];
/* 163 */         Arrays.fill(this.modelScoresOnValidation[k], 0.0D);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 168 */     this.hist = new FeatureHistogram();
/* 169 */     this.hist.construct(this.martSamples, this.pseudoResponses, this.sortedIdx, this.features, this.thresholds);
/*     */     
/* 171 */     this.sortedIdx = (int[][])null;
/*     */     
/* 173 */     System.gc();
/* 174 */     PRINTLN("[Done]");
/*     */   }
/*     */ 
/*     */   
/*     */   public void learn() {
/* 179 */     this.ensemble = new Ensemble();
/*     */     
/* 181 */     PRINTLN("---------------------------------");
/* 182 */     PRINTLN("Training starts...");
/* 183 */     PRINTLN("---------------------------------");
/* 184 */     PRINTLN(new int[] { 7, 9, 9 }, new String[] { "#iter", this.scorer.name() + "-T", this.scorer.name() + "-V" });
/* 185 */     PRINTLN("---------------------------------");
/*     */ 
/*     */     
/* 188 */     for (int m = 0; m < nTrees; m++) {
/*     */       
/* 190 */       PRINT(new int[] { 7 }, new String[] { (m + 1) + "" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 196 */       computePseudoResponses();
/*     */ 
/*     */       
/* 199 */       this.hist.update(this.pseudoResponses);
/*     */ 
/*     */       
/* 202 */       RegressionTree rt = new RegressionTree(nTreeLeaves, this.martSamples, this.pseudoResponses, this.hist, minLeafSupport);
/* 203 */       rt.fit();
/*     */ 
/*     */       
/* 206 */       this.ensemble.add(rt, learningRate);
/*     */ 
/*     */       
/* 209 */       updateTreeOutput(rt);
/*     */ 
/*     */       
/* 212 */       List<Split> leaves = rt.leaves(); int i;
/* 213 */       for (i = 0; i < leaves.size(); i++) {
/*     */         
/* 215 */         Split s = leaves.get(i);
/* 216 */         int[] idx = s.getSamples();
/* 217 */         for (int j = 0; j < idx.length; j++) {
/* 218 */           this.modelScores[idx[j]] = this.modelScores[idx[j]] + learningRate * s.getOutput();
/*     */         }
/*     */       } 
/*     */       
/* 222 */       rt.clearSamples();
/*     */ 
/*     */       
/* 225 */       if (m % gcCycle == 0) {
/* 226 */         System.gc();
/*     */       }
/*     */       
/* 229 */       this.scoreOnTrainingData = computeModelScoreOnTraining();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 238 */       PRINT(new int[] { 9 }, new String[] { SimpleMath.round(this.scoreOnTrainingData, 4) + "" });
/*     */ 
/*     */       
/* 241 */       if (this.validationSamples != null) {
/*     */ 
/*     */         
/* 244 */         for (i = 0; i < this.modelScoresOnValidation.length; i++) {
/* 245 */           for (int j = 0; j < (this.modelScoresOnValidation[i]).length; j++) {
/* 246 */             this.modelScoresOnValidation[i][j] = this.modelScoresOnValidation[i][j] + learningRate * rt.eval(((RankList)this.validationSamples.get(i)).get(j));
/*     */           }
/*     */         } 
/* 249 */         double score = computeModelScoreOnValidation();
/*     */         
/* 251 */         PRINT(new int[] { 9 }, new String[] { SimpleMath.round(score, 4) + "" });
/* 252 */         if (score > this.bestScoreOnValidationData) {
/*     */           
/* 254 */           this.bestScoreOnValidationData = score;
/* 255 */           this.bestModelOnValidation = this.ensemble.treeCount() - 1;
/*     */         } 
/*     */       } 
/*     */       
/* 259 */       PRINTLN("");
/*     */ 
/*     */       
/* 262 */       if (m - this.bestModelOnValidation > nRoundToStopEarly) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/* 267 */     while (this.ensemble.treeCount() > this.bestModelOnValidation + 1) {
/* 268 */       this.ensemble.remove(this.ensemble.treeCount() - 1);
/*     */     }
/*     */     
/* 271 */     this.scoreOnTrainingData = this.scorer.score(rank(this.samples));
/* 272 */     PRINTLN("---------------------------------");
/* 273 */     PRINTLN("Finished sucessfully.");
/* 274 */     PRINTLN(this.scorer.name() + " on training data: " + SimpleMath.round(this.scoreOnTrainingData, 4));
/* 275 */     if (this.validationSamples != null) {
/*     */       
/* 277 */       this.bestScoreOnValidationData = this.scorer.score(rank(this.validationSamples));
/* 278 */       PRINTLN(this.scorer.name() + " on validation data: " + SimpleMath.round(this.bestScoreOnValidationData, 4));
/*     */     } 
/* 280 */     PRINTLN("---------------------------------");
/*     */   }
/*     */ 
/*     */   
/*     */   public double eval(DataPoint dp) {
/* 285 */     return this.ensemble.eval(dp);
/*     */   }
/*     */ 
/*     */   
/*     */   public Ranker createNew() {
/* 290 */     return new LambdaMART();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 295 */     return this.ensemble.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String model() {
/* 300 */     String output = "## " + name() + "\n";
/* 301 */     output = output + "## No. of trees = " + nTrees + "\n";
/* 302 */     output = output + "## No. of leaves = " + nTreeLeaves + "\n";
/* 303 */     output = output + "## No. of threshold candidates = " + nThreshold + "\n";
/* 304 */     output = output + "## Learning rate = " + learningRate + "\n";
/* 305 */     output = output + "## Stop early = " + nRoundToStopEarly + "\n";
/* 306 */     output = output + "\n";
/* 307 */     output = output + toString();
/* 308 */     return output;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadFromString(String fullText) {
/*     */     try {
/* 315 */       String content = "";
/*     */       
/* 317 */       StringBuffer model = new StringBuffer();
/* 318 */       BufferedReader in = new BufferedReader(new StringReader(fullText));
/* 319 */       while ((content = in.readLine()) != null) {
/*     */         
/* 321 */         content = content.trim();
/* 322 */         if (content.length() == 0)
/*     */           continue; 
/* 324 */         if (content.indexOf("##") == 0) {
/*     */           continue;
/*     */         }
/*     */         
/* 328 */         model.append(content);
/*     */       } 
/* 330 */       in.close();
/*     */       
/* 332 */       this.ensemble = new Ensemble(model.toString());
/* 333 */       this.features = this.ensemble.getFeatures();
/*     */     }
/* 335 */     catch (Exception ex) {
/*     */       
/* 337 */       throw RankLibError.create("Error in LambdaMART::load(): ", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void printParameters() {
/* 343 */     PRINTLN("No. of trees: " + nTrees);
/* 344 */     PRINTLN("No. of leaves: " + nTreeLeaves);
/* 345 */     PRINTLN("No. of threshold candidates: " + nThreshold);
/* 346 */     PRINTLN("Min leaf support: " + minLeafSupport);
/* 347 */     PRINTLN("Learning rate: " + learningRate);
/* 348 */     PRINTLN("Stop early: " + nRoundToStopEarly + " rounds without performance gain on validation data");
/*     */   }
/*     */ 
/*     */   
/*     */   public String name() {
/* 353 */     return "LambdaMART";
/*     */   }
/*     */ 
/*     */   
/*     */   public Ensemble getEnsemble() {
/* 358 */     return this.ensemble;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void computePseudoResponses() {
/* 363 */     Arrays.fill(this.pseudoResponses, 0.0D);
/* 364 */     Arrays.fill(this.weights, 0.0D);
/* 365 */     MyThreadPool p = MyThreadPool.getInstance();
/* 366 */     if (p.size() == 1) {
/* 367 */       computePseudoResponses(0, this.samples.size() - 1, 0);
/*     */     } else {
/*     */       
/* 370 */       List<LambdaComputationWorker> workers = new ArrayList<>();
/*     */       
/* 372 */       int[] partition = p.partition(this.samples.size());
/* 373 */       int current = 0;
/* 374 */       for (int i = 0; i < partition.length - 1; i++) {
/*     */ 
/*     */         
/* 377 */         LambdaComputationWorker wk = new LambdaComputationWorker(this, partition[i], partition[i + 1] - 1, current);
/* 378 */         workers.add(wk);
/* 379 */         p.execute(wk);
/*     */         
/* 381 */         if (i < partition.length - 2) {
/* 382 */           for (int j = partition[i]; j <= partition[i + 1] - 1; j++) {
/* 383 */             current += ((RankList)this.samples.get(j)).size();
/*     */           }
/*     */         }
/*     */       } 
/* 387 */       p.await();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void computePseudoResponses(int start, int end, int current) {
/* 393 */     int cutoff = this.scorer.getK();
/*     */     
/* 395 */     for (int i = start; i <= end; i++) {
/*     */       
/* 397 */       RankList orig = this.samples.get(i);
/* 398 */       int[] idx = MergeSorter.sort(this.modelScores, current, current + orig.size() - 1, false);
/* 399 */       RankList rl = new RankList(orig, idx, current);
/* 400 */       double[][] changes = this.scorer.swapChange(rl);
/*     */ 
/*     */       
/* 403 */       for (int j = 0; j < rl.size(); j++) {
/*     */         
/* 405 */         DataPoint p1 = rl.get(j);
/* 406 */         int mj = idx[j];
/* 407 */         for (int k = 0; k < rl.size(); k++) {
/*     */           
/* 409 */           if (j > cutoff && k > cutoff)
/*     */             break; 
/* 411 */           DataPoint p2 = rl.get(k);
/* 412 */           int mk = idx[k];
/* 413 */           if (p1.getLabel() > p2.getLabel()) {
/*     */             
/* 415 */             double deltaNDCG = Math.abs(changes[j][k]);
/* 416 */             if (deltaNDCG > 0.0D) {
/*     */               
/* 418 */               double rho = 1.0D / (1.0D + Math.exp(this.modelScores[mj] - this.modelScores[mk]));
/* 419 */               double lambda = rho * deltaNDCG;
/* 420 */               this.pseudoResponses[mj] = this.pseudoResponses[mj] + lambda;
/* 421 */               this.pseudoResponses[mk] = this.pseudoResponses[mk] - lambda;
/* 422 */               double delta = rho * (1.0D - rho) * deltaNDCG;
/* 423 */               this.weights[mj] = this.weights[mj] + delta;
/* 424 */               this.weights[mk] = this.weights[mk] + delta;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 429 */       current += orig.size();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateTreeOutput(RegressionTree rt) {
/* 435 */     List<Split> leaves = rt.leaves();
/* 436 */     for (int i = 0; i < leaves.size(); i++) {
/*     */       
/* 438 */       float s1 = 0.0F;
/* 439 */       float s2 = 0.0F;
/* 440 */       Split s = leaves.get(i);
/* 441 */       int[] idx = s.getSamples();
/* 442 */       for (int j = 0; j < idx.length; j++) {
/*     */         
/* 444 */         int k = idx[j];
/* 445 */         s1 = (float)(s1 + this.pseudoResponses[k]);
/* 446 */         s2 = (float)(s2 + this.weights[k]);
/*     */       } 
/* 448 */       if (s2 == 0.0F) {
/* 449 */         s.setOutput(0.0F);
/*     */       } else {
/* 451 */         s.setOutput(s1 / s2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected int[] sortSamplesByFeature(DataPoint[] samples, int fid) {
/* 457 */     double[] score = new double[samples.length];
/* 458 */     for (int i = 0; i < samples.length; i++)
/* 459 */       score[i] = samples[i].getFeatureValue(fid); 
/* 460 */     int[] idx = MergeSorter.sort(score, true);
/* 461 */     return idx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected RankList rank(int rankListIndex, int current) {
/* 472 */     RankList orig = this.samples.get(rankListIndex);
/* 473 */     double[] scores = new double[orig.size()];
/* 474 */     for (int i = 0; i < scores.length; i++)
/* 475 */       scores[i] = this.modelScores[current + i]; 
/* 476 */     int[] idx = MergeSorter.sort(scores, false);
/* 477 */     return new RankList(orig, idx);
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
/*     */   
/*     */   protected float computeModelScoreOnTraining() {
/* 508 */     float s = computeModelScoreOnTraining(0, this.samples.size() - 1, 0);
/* 509 */     s /= this.samples.size();
/* 510 */     return s;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float computeModelScoreOnTraining(int start, int end, int current) {
/* 515 */     float s = 0.0F;
/* 516 */     int c = current;
/*     */     
/* 518 */     for (int i = start; i <= end; i++) {
/*     */       
/* 520 */       s = (float)(s + this.scorer.score(rank(i, c)));
/* 521 */       c += ((RankList)this.samples.get(i)).size();
/*     */     } 
/* 523 */     return s;
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
/*     */   protected float computeModelScoreOnValidation() {
/* 549 */     float score = computeModelScoreOnValidation(0, this.validationSamples.size() - 1);
/* 550 */     return score / this.validationSamples.size();
/*     */   }
/*     */ 
/*     */   
/*     */   protected float computeModelScoreOnValidation(int start, int end) {
/* 555 */     float score = 0.0F;
/* 556 */     for (int i = start; i <= end; i++) {
/*     */       
/* 558 */       int[] idx = MergeSorter.sort(this.modelScoresOnValidation[i], false);
/* 559 */       score = (float)(score + this.scorer.score(new RankList(this.validationSamples.get(i), idx)));
/*     */     } 
/* 561 */     return score;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void sortSamplesByFeature(int fStart, int fEnd) {
/* 566 */     for (int i = fStart; i <= fEnd; i++)
/* 567 */       this.sortedIdx[i] = sortSamplesByFeature(this.martSamples, this.features[i]); 
/*     */   }
/*     */   
/*     */   class SortWorker
/*     */     implements Runnable {
/* 572 */     LambdaMART ranker = null;
/* 573 */     int start = -1;
/* 574 */     int end = -1;
/*     */ 
/*     */     
/*     */     SortWorker(LambdaMART ranker, int start, int end) {
/* 578 */       this.ranker = ranker;
/* 579 */       this.start = start;
/* 580 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 585 */       this.ranker.sortSamplesByFeature(this.start, this.end);
/*     */     }
/*     */   }
/*     */   
/*     */   class LambdaComputationWorker implements Runnable {
/* 590 */     LambdaMART ranker = null;
/* 591 */     int rlStart = -1;
/* 592 */     int rlEnd = -1;
/* 593 */     int martStart = -1;
/*     */ 
/*     */     
/*     */     LambdaComputationWorker(LambdaMART ranker, int rlStart, int rlEnd, int martStart) {
/* 597 */       this.ranker = ranker;
/* 598 */       this.rlStart = rlStart;
/* 599 */       this.rlEnd = rlEnd;
/* 600 */       this.martStart = martStart;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 605 */       this.ranker.computePseudoResponses(this.rlStart, this.rlEnd, this.martStart);
/*     */     }
/*     */   }
/*     */   
/*     */   class Worker implements Runnable {
/* 610 */     LambdaMART ranker = null;
/* 611 */     int rlStart = -1;
/* 612 */     int rlEnd = -1;
/* 613 */     int martStart = -1;
/* 614 */     int type = -1;
/*     */ 
/*     */     
/* 617 */     float score = 0.0F;
/*     */ 
/*     */     
/*     */     Worker(LambdaMART ranker, int rlStart, int rlEnd) {
/* 621 */       this.type = 3;
/* 622 */       this.ranker = ranker;
/* 623 */       this.rlStart = rlStart;
/* 624 */       this.rlEnd = rlEnd;
/*     */     }
/*     */ 
/*     */     
/*     */     Worker(LambdaMART ranker, int rlStart, int rlEnd, int martStart) {
/* 629 */       this.type = 4;
/* 630 */       this.ranker = ranker;
/* 631 */       this.rlStart = rlStart;
/* 632 */       this.rlEnd = rlEnd;
/* 633 */       this.martStart = martStart;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 638 */       if (this.type == 4) {
/* 639 */         this.score = this.ranker.computeModelScoreOnTraining(this.rlStart, this.rlEnd, this.martStart);
/* 640 */       } else if (this.type == 3) {
/* 641 */         this.score = this.ranker.computeModelScoreOnValidation(this.rlStart, this.rlEnd);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\learning\tree\LambdaMART.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */