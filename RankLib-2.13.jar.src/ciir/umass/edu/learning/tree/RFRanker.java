/*     */ package ciir.umass.edu.learning.tree;
/*     */ 
/*     */ import ciir.umass.edu.learning.DataPoint;
/*     */ import ciir.umass.edu.learning.RANKER_TYPE;
/*     */ import ciir.umass.edu.learning.RankList;
/*     */ import ciir.umass.edu.learning.Ranker;
/*     */ import ciir.umass.edu.learning.RankerFactory;
/*     */ import ciir.umass.edu.learning.Sampler;
/*     */ import ciir.umass.edu.metric.MetricScorer;
/*     */ import ciir.umass.edu.utilities.RankLibError;
/*     */ import ciir.umass.edu.utilities.SimpleMath;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.StringReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RFRanker
/*     */   extends Ranker
/*     */ {
/*  26 */   public static int nBag = 300;
/*  27 */   public static float subSamplingRate = 1.0F;
/*  28 */   public static float featureSamplingRate = 0.3F;
/*     */   
/*  30 */   public static RANKER_TYPE rType = RANKER_TYPE.MART;
/*  31 */   public static int nTrees = 1;
/*  32 */   public static int nTreeLeaves = 100;
/*  33 */   public static float learningRate = 0.1F;
/*  34 */   public static int nThreshold = 256;
/*  35 */   public static int minLeafSupport = 1;
/*     */ 
/*     */   
/*  38 */   protected Ensemble[] ensembles = null;
/*     */ 
/*     */   
/*     */   public RFRanker() {}
/*     */ 
/*     */   
/*     */   public RFRanker(List<RankList> samples, int[] features, MetricScorer scorer) {
/*  45 */     super(samples, features, scorer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void init() {
/*  50 */     PRINT("Initializing... ");
/*  51 */     this.ensembles = new Ensemble[nBag];
/*     */     
/*  53 */     LambdaMART.nTrees = nTrees;
/*  54 */     LambdaMART.nTreeLeaves = nTreeLeaves;
/*  55 */     LambdaMART.learningRate = learningRate;
/*  56 */     LambdaMART.nThreshold = nThreshold;
/*  57 */     LambdaMART.minLeafSupport = minLeafSupport;
/*  58 */     LambdaMART.nRoundToStopEarly = -1;
/*     */     
/*  60 */     FeatureHistogram.samplingRate = featureSamplingRate;
/*  61 */     PRINTLN("[Done]");
/*     */   }
/*     */   
/*     */   public void learn() {
/*  65 */     RankerFactory rf = new RankerFactory();
/*  66 */     PRINTLN("------------------------------------");
/*  67 */     PRINTLN("Training starts...");
/*  68 */     PRINTLN("------------------------------------");
/*  69 */     PRINTLN(new int[] { 9, 9, 11 }, new String[] { "bag", this.scorer.name() + "-B", this.scorer.name() + "-OOB" });
/*  70 */     PRINTLN("------------------------------------");
/*     */     
/*  72 */     for (int i = 0; i < nBag; i++) {
/*     */       
/*  74 */       if (i % LambdaMART.gcCycle == 0)
/*  75 */         System.gc(); 
/*  76 */       Sampler sp = new Sampler();
/*     */       
/*  78 */       List<RankList> bag = sp.doSampling(this.samples, subSamplingRate, true);
/*     */ 
/*     */       
/*  81 */       LambdaMART r = (LambdaMART)rf.createRanker(rType, bag, this.features, this.scorer);
/*     */ 
/*     */       
/*  84 */       boolean tmp = Ranker.verbose;
/*  85 */       Ranker.verbose = false;
/*  86 */       r.init();
/*  87 */       r.learn();
/*  88 */       Ranker.verbose = tmp;
/*     */       
/*  90 */       PRINTLN(new int[] { 9, 9 }, new String[] { "b[" + (i + 1) + "]", SimpleMath.round(r.getScoreOnTrainingData(), 4) + "" });
/*  91 */       this.ensembles[i] = r.getEnsemble();
/*     */     } 
/*     */     
/*  94 */     this.scoreOnTrainingData = this.scorer.score(rank(this.samples));
/*  95 */     PRINTLN("------------------------------------");
/*  96 */     PRINTLN("Finished sucessfully.");
/*  97 */     PRINTLN(this.scorer.name() + " on training data: " + SimpleMath.round(this.scoreOnTrainingData, 4));
/*  98 */     if (this.validationSamples != null) {
/*     */       
/* 100 */       this.bestScoreOnValidationData = this.scorer.score(rank(this.validationSamples));
/* 101 */       PRINTLN(this.scorer.name() + " on validation data: " + SimpleMath.round(this.bestScoreOnValidationData, 4));
/*     */     } 
/* 103 */     PRINTLN("------------------------------------");
/*     */   }
/*     */   
/*     */   public double eval(DataPoint dp) {
/* 107 */     double s = 0.0D;
/* 108 */     for (int i = 0; i < this.ensembles.length; i++)
/* 109 */       s += this.ensembles[i].eval(dp); 
/* 110 */     return s / this.ensembles.length;
/*     */   }
/*     */   
/*     */   public Ranker createNew() {
/* 114 */     return new RFRanker();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 118 */     String str = "";
/* 119 */     for (int i = 0; i < nBag; i++)
/* 120 */       str = str + this.ensembles[i].toString() + "\n"; 
/* 121 */     return str;
/*     */   }
/*     */   
/*     */   public String model() {
/* 125 */     String output = "## " + name() + "\n";
/* 126 */     output = output + "## No. of bags = " + nBag + "\n";
/* 127 */     output = output + "## Sub-sampling = " + subSamplingRate + "\n";
/* 128 */     output = output + "## Feature-sampling = " + featureSamplingRate + "\n";
/* 129 */     output = output + "## No. of trees = " + nTrees + "\n";
/* 130 */     output = output + "## No. of leaves = " + nTreeLeaves + "\n";
/* 131 */     output = output + "## No. of threshold candidates = " + nThreshold + "\n";
/* 132 */     output = output + "## Learning rate = " + learningRate + "\n";
/* 133 */     output = output + "\n";
/* 134 */     output = output + toString();
/* 135 */     return output;
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadFromString(String fullText) {
/*     */     try {
/* 141 */       String content = "";
/* 142 */       String model = "";
/* 143 */       BufferedReader in = new BufferedReader(new StringReader(fullText));
/* 144 */       List<Ensemble> ens = new ArrayList<>();
/* 145 */       while ((content = in.readLine()) != null) {
/*     */         
/* 147 */         content = content.trim();
/* 148 */         if (content.length() == 0)
/*     */           continue; 
/* 150 */         if (content.indexOf("##") == 0) {
/*     */           continue;
/*     */         }
/* 153 */         model = model + content;
/* 154 */         if (content.indexOf("</ensemble>") != -1) {
/*     */ 
/*     */           
/* 157 */           ens.add(new Ensemble(model));
/* 158 */           model = "";
/*     */         } 
/*     */       } 
/* 161 */       in.close();
/* 162 */       HashSet<Integer> uniqueFeatures = new HashSet<>();
/* 163 */       this.ensembles = new Ensemble[ens.size()];
/* 164 */       for (int i = 0; i < ens.size(); i++) {
/*     */         
/* 166 */         this.ensembles[i] = ens.get(i);
/*     */         
/* 168 */         int[] fids = ((Ensemble)ens.get(i)).getFeatures();
/* 169 */         for (int f = 0; f < fids.length; f++) {
/* 170 */           if (!uniqueFeatures.contains(Integer.valueOf(fids[f])))
/* 171 */             uniqueFeatures.add(Integer.valueOf(fids[f])); 
/*     */         } 
/* 173 */       }  int fi = 0;
/* 174 */       this.features = new int[uniqueFeatures.size()];
/* 175 */       for (Integer f : uniqueFeatures) {
/* 176 */         this.features[fi++] = f.intValue();
/*     */       }
/* 178 */     } catch (Exception ex) {
/*     */       
/* 180 */       throw RankLibError.create("Error in RFRanker::load(): ", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void printParameters() {
/* 185 */     PRINTLN("No. of bags: " + nBag);
/* 186 */     PRINTLN("Sub-sampling: " + subSamplingRate);
/* 187 */     PRINTLN("Feature-sampling: " + featureSamplingRate);
/* 188 */     PRINTLN("No. of trees: " + nTrees);
/* 189 */     PRINTLN("No. of leaves: " + nTreeLeaves);
/* 190 */     PRINTLN("No. of threshold candidates: " + nThreshold);
/* 191 */     PRINTLN("Learning rate: " + learningRate);
/*     */   }
/*     */   
/*     */   public String name() {
/* 195 */     return "Random Forests";
/*     */   }
/*     */ 
/*     */   
/*     */   public Ensemble[] getEnsembles() {
/* 200 */     return this.ensembles;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\learning\tree\RFRanker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */