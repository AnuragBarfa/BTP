/*     */ package ciir.umass.edu.learning.boosting;
/*     */ 
/*     */ import ciir.umass.edu.learning.DataPoint;
/*     */ import ciir.umass.edu.learning.RankList;
/*     */ import ciir.umass.edu.learning.Ranker;
/*     */ import ciir.umass.edu.metric.MetricScorer;
/*     */ import ciir.umass.edu.utilities.KeyValuePair;
/*     */ import ciir.umass.edu.utilities.RankLibError;
/*     */ import ciir.umass.edu.utilities.SimpleMath;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.StringReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
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
/*     */ public class AdaRank
/*     */   extends Ranker
/*     */ {
/*  35 */   public static int nIteration = 500;
/*  36 */   public static double tolerance = 0.002D;
/*     */   public static boolean trainWithEnqueue = true;
/*  38 */   public static int maxSelCount = 5;
/*     */   
/*  40 */   protected HashMap<Integer, Integer> usedFeatures = new HashMap<>();
/*  41 */   protected double[] sweight = null;
/*  42 */   protected List<WeakRanker> rankers = null;
/*  43 */   protected List<Double> rweight = null;
/*     */   
/*  45 */   protected List<WeakRanker> bestModelRankers = null;
/*  46 */   protected List<Double> bestModelWeights = null;
/*     */ 
/*     */   
/*  49 */   int lastFeature = -1;
/*  50 */   int lastFeatureConsecutiveCount = 0;
/*     */   boolean performanceChanged = false;
/*  52 */   List<Integer> featureQueue = null;
/*  53 */   protected double[] backupSampleWeight = null;
/*  54 */   protected double backupTrainScore = 0.0D;
/*  55 */   protected double lastTrainedScore = -1.0D;
/*     */ 
/*     */ 
/*     */   
/*     */   public AdaRank() {}
/*     */ 
/*     */   
/*     */   public AdaRank(List<RankList> samples, int[] features, MetricScorer scorer) {
/*  63 */     super(samples, features, scorer);
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateBestModelOnValidation() {
/*  68 */     this.bestModelRankers.clear();
/*  69 */     this.bestModelRankers.addAll(this.rankers);
/*  70 */     this.bestModelWeights.clear();
/*  71 */     this.bestModelWeights.addAll(this.rweight);
/*     */   }
/*     */   
/*     */   private WeakRanker learnWeakRanker() {
/*  75 */     double bestScore = -1.0D;
/*  76 */     WeakRanker bestWR = null;
/*  77 */     for (int i : this.features) {
/*  78 */       if (!this.featureQueue.contains(Integer.valueOf(i)))
/*     */       {
/*     */         
/*  81 */         if (this.usedFeatures.get(Integer.valueOf(i)) == null) {
/*     */ 
/*     */           
/*  84 */           WeakRanker wr = new WeakRanker(i);
/*  85 */           double s = 0.0D;
/*  86 */           for (int j = 0; j < this.samples.size(); j++) {
/*  87 */             double t = this.scorer.score(wr.rank(this.samples.get(j))) * this.sweight[j];
/*  88 */             s += t;
/*     */           } 
/*     */           
/*  91 */           if (bestScore < s) {
/*  92 */             bestScore = s;
/*  93 */             bestWR = wr;
/*     */           } 
/*     */         }  } 
/*  96 */     }  return bestWR;
/*     */   }
/*     */   
/*     */   private int learn(int startIteration, boolean withEnqueue) {
/* 100 */     int t = startIteration; while (true) {
/* 101 */       if (t <= nIteration)
/*     */       
/* 103 */       { PRINT(new int[] { 7 }, new String[] { t + "" });
/*     */         
/* 105 */         WeakRanker bestWR = learnWeakRanker();
/* 106 */         if (bestWR == null) {
/*     */           break;
/*     */         }
/* 109 */         if (withEnqueue)
/*     */         
/* 111 */         { if (bestWR.getFID() == this.lastFeature)
/*     */           
/*     */           { 
/* 114 */             this.featureQueue.add(Integer.valueOf(this.lastFeature));
/*     */             
/* 116 */             this.rankers.remove(this.rankers.size() - 1);
/* 117 */             this.rweight.remove(this.rweight.size() - 1);
/* 118 */             copy(this.backupSampleWeight, this.sweight);
/* 119 */             this.bestScoreOnValidationData = 0.0D;
/* 120 */             this.lastTrainedScore = this.backupTrainScore;
/* 121 */             PRINTLN(new int[] { 8, 9, 9, 9 }, new String[] { bestWR.getFID() + "", "", "", "ROLLBACK" });
/*     */              }
/*     */           
/*     */           else
/*     */           
/* 126 */           { this.lastFeature = bestWR.getFID();
/*     */             
/* 128 */             copy(this.sweight, this.backupSampleWeight);
/* 129 */             this.backupTrainScore = this.lastTrainedScore;
/*     */ 
/*     */ 
/*     */             
/* 133 */             double num = 0.0D;
/* 134 */             double denom = 0.0D;
/* 135 */             int i = 0; }  continue; }  } else { break; }  double d1 = 0.0D; double d2 = 0.0D; boolean bool = false;
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
/*     */       t++;
/*     */     } 
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
/* 216 */     return t;
/*     */   }
/*     */ 
/*     */   
/*     */   public void init() {
/* 221 */     PRINT("Initializing... ");
/*     */     
/* 223 */     this.usedFeatures.clear();
/*     */     
/* 225 */     this.sweight = new double[this.samples.size()];
/* 226 */     for (int i = 0; i < this.sweight.length; i++)
/* 227 */       this.sweight[i] = (1.0F / this.samples.size()); 
/* 228 */     this.backupSampleWeight = new double[this.sweight.length];
/* 229 */     copy(this.sweight, this.backupSampleWeight);
/* 230 */     this.lastTrainedScore = -1.0D;
/*     */     
/* 232 */     this.rankers = new ArrayList<>();
/* 233 */     this.rweight = new ArrayList<>();
/*     */     
/* 235 */     this.featureQueue = new ArrayList<>();
/*     */     
/* 237 */     this.bestScoreOnValidationData = 0.0D;
/* 238 */     this.bestModelRankers = new ArrayList<>();
/* 239 */     this.bestModelWeights = new ArrayList<>();
/*     */     
/* 241 */     PRINTLN("[Done]");
/*     */   }
/*     */   
/*     */   public void learn() {
/* 245 */     PRINTLN("---------------------------");
/* 246 */     PRINTLN("Training starts...");
/* 247 */     PRINTLN("--------------------------------------------------------");
/* 248 */     PRINTLN(new int[] { 7, 8, 9, 9, 9 }, new String[] { "#iter", "Sel. F.", this.scorer.name() + "-T", this.scorer.name() + "-V", "Status" });
/* 249 */     PRINTLN("--------------------------------------------------------");
/*     */     
/* 251 */     if (trainWithEnqueue) {
/*     */       
/* 253 */       int t = learn(1, true);
/*     */       
/* 255 */       for (int i = this.featureQueue.size() - 1; i >= 0; i--) {
/*     */         
/* 257 */         this.featureQueue.remove(i);
/* 258 */         t = learn(t, false);
/*     */       } 
/*     */     } else {
/*     */       
/* 262 */       learn(1, false);
/*     */     } 
/*     */ 
/*     */     
/* 266 */     if (this.validationSamples != null && this.bestModelRankers.size() > 0) {
/*     */       
/* 268 */       this.rankers.clear();
/* 269 */       this.rweight.clear();
/* 270 */       this.rankers.addAll(this.bestModelRankers);
/* 271 */       this.rweight.addAll(this.bestModelWeights);
/*     */     } 
/*     */ 
/*     */     
/* 275 */     this.scoreOnTrainingData = SimpleMath.round(this.scorer.score(rank(this.samples)), 4);
/* 276 */     PRINTLN("--------------------------------------------------------");
/* 277 */     PRINTLN("Finished sucessfully.");
/* 278 */     PRINTLN(this.scorer.name() + " on training data: " + this.scoreOnTrainingData);
/* 279 */     if (this.validationSamples != null) {
/*     */       
/* 281 */       this.bestScoreOnValidationData = this.scorer.score(rank(this.validationSamples));
/* 282 */       PRINTLN(this.scorer.name() + " on validation data: " + SimpleMath.round(this.bestScoreOnValidationData, 4));
/*     */     } 
/* 284 */     PRINTLN("---------------------------------");
/*     */   }
/*     */   
/*     */   public double eval(DataPoint p) {
/* 288 */     double score = 0.0D;
/* 289 */     for (int j = 0; j < this.rankers.size(); j++)
/* 290 */       score += ((Double)this.rweight.get(j)).doubleValue() * p.getFeatureValue(((WeakRanker)this.rankers.get(j)).getFID()); 
/* 291 */     return score;
/*     */   }
/*     */   
/*     */   public Ranker createNew() {
/* 295 */     return new AdaRank();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 299 */     String output = "";
/* 300 */     for (int i = 0; i < this.rankers.size(); i++)
/* 301 */       output = output + ((WeakRanker)this.rankers.get(i)).getFID() + ":" + this.rweight.get(i) + ((i == this.rankers.size() - 1) ? "" : " "); 
/* 302 */     return output;
/*     */   }
/*     */   
/*     */   public String model() {
/* 306 */     String output = "## " + name() + "\n";
/* 307 */     output = output + "## Iteration = " + nIteration + "\n";
/* 308 */     output = output + "## Train with enqueue: " + (trainWithEnqueue ? "Yes" : "No") + "\n";
/* 309 */     output = output + "## Tolerance = " + tolerance + "\n";
/* 310 */     output = output + "## Max consecutive selection count = " + maxSelCount + "\n";
/* 311 */     output = output + toString();
/* 312 */     return output;
/*     */   }
/*     */   
/*     */   public void loadFromString(String fullText) {
/* 316 */     try (BufferedReader in = new BufferedReader(new StringReader(fullText))) {
/* 317 */       String content = "";
/*     */       
/* 319 */       KeyValuePair kvp = null;
/* 320 */       while ((content = in.readLine()) != null) {
/*     */         
/* 322 */         content = content.trim();
/* 323 */         if (content.length() == 0)
/*     */           continue; 
/* 325 */         if (content.indexOf("##") == 0)
/*     */           continue; 
/* 327 */         kvp = new KeyValuePair(content);
/*     */       } 
/*     */ 
/*     */       
/* 331 */       assert kvp != null;
/*     */       
/* 333 */       List<String> keys = kvp.keys();
/* 334 */       List<String> values = kvp.values();
/* 335 */       this.rweight = new ArrayList<>();
/* 336 */       this.rankers = new ArrayList<>();
/* 337 */       this.features = new int[keys.size()];
/* 338 */       for (int i = 0; i < keys.size(); i++)
/*     */       {
/* 340 */         this.features[i] = Integer.parseInt((String)keys.get(i));
/* 341 */         this.rankers.add(new WeakRanker(this.features[i]));
/* 342 */         this.rweight.add(Double.valueOf(Double.parseDouble(values.get(i))));
/*     */       }
/*     */     
/* 345 */     } catch (Exception ex) {
/*     */       
/* 347 */       throw RankLibError.create("Error in AdaRank::load(): ", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void printParameters() {
/* 352 */     PRINTLN("No. of rounds: " + nIteration);
/* 353 */     PRINTLN("Train with 'enequeue': " + (trainWithEnqueue ? "Yes" : "No"));
/* 354 */     PRINTLN("Tolerance: " + tolerance);
/* 355 */     PRINTLN("Max Sel. Count: " + maxSelCount);
/*     */   }
/*     */   
/*     */   public String name() {
/* 359 */     return "AdaRank";
/*     */   }
/*     */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\learning\boosting\AdaRank.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */