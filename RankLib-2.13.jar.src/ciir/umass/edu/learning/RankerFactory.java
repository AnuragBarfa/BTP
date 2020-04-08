/*     */ package ciir.umass.edu.learning;
/*     */ 
/*     */ import ciir.umass.edu.learning.boosting.AdaRank;
/*     */ import ciir.umass.edu.learning.boosting.RankBoost;
/*     */ import ciir.umass.edu.learning.neuralnet.LambdaRank;
/*     */ import ciir.umass.edu.learning.neuralnet.ListNet;
/*     */ import ciir.umass.edu.learning.neuralnet.RankNet;
/*     */ import ciir.umass.edu.learning.tree.LambdaMART;
/*     */ import ciir.umass.edu.learning.tree.MART;
/*     */ import ciir.umass.edu.learning.tree.RFRanker;
/*     */ import ciir.umass.edu.metric.MetricScorer;
/*     */ import ciir.umass.edu.utilities.FileUtils;
/*     */ import ciir.umass.edu.utilities.RankLibError;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.StringReader;
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
/*     */ public class RankerFactory
/*     */ {
/*  36 */   protected Ranker[] rFactory = new Ranker[] { (Ranker)new MART(), (Ranker)new RankBoost(), (Ranker)new RankNet(), (Ranker)new AdaRank(), new CoorAscent(), (Ranker)new LambdaRank(), (Ranker)new LambdaMART(), (Ranker)new ListNet(), (Ranker)new RFRanker(), new LinearRegRank() };
/*  37 */   protected static HashMap<String, RANKER_TYPE> map = new HashMap<>();
/*     */ 
/*     */   
/*     */   public RankerFactory() {
/*  41 */     map.put(createRanker(RANKER_TYPE.MART).name().toUpperCase(), RANKER_TYPE.MART);
/*  42 */     map.put(createRanker(RANKER_TYPE.RANKNET).name().toUpperCase(), RANKER_TYPE.RANKNET);
/*  43 */     map.put(createRanker(RANKER_TYPE.RANKBOOST).name().toUpperCase(), RANKER_TYPE.RANKBOOST);
/*  44 */     map.put(createRanker(RANKER_TYPE.ADARANK).name().toUpperCase(), RANKER_TYPE.ADARANK);
/*  45 */     map.put(createRanker(RANKER_TYPE.COOR_ASCENT).name().toUpperCase(), RANKER_TYPE.COOR_ASCENT);
/*  46 */     map.put(createRanker(RANKER_TYPE.LAMBDARANK).name().toUpperCase(), RANKER_TYPE.LAMBDARANK);
/*  47 */     map.put(createRanker(RANKER_TYPE.LAMBDAMART).name().toUpperCase(), RANKER_TYPE.LAMBDAMART);
/*  48 */     map.put(createRanker(RANKER_TYPE.LISTNET).name().toUpperCase(), RANKER_TYPE.LISTNET);
/*  49 */     map.put(createRanker(RANKER_TYPE.RANDOM_FOREST).name().toUpperCase(), RANKER_TYPE.RANDOM_FOREST);
/*  50 */     map.put(createRanker(RANKER_TYPE.LINEAR_REGRESSION).name().toUpperCase(), RANKER_TYPE.LINEAR_REGRESSION);
/*     */   }
/*     */   
/*     */   public Ranker createRanker(RANKER_TYPE type) {
/*  54 */     return this.rFactory[type.ordinal() - RANKER_TYPE.MART.ordinal()].createNew();
/*     */   }
/*     */   
/*     */   public Ranker createRanker(RANKER_TYPE type, List<RankList> samples, int[] features, MetricScorer scorer) {
/*  58 */     Ranker r = createRanker(type);
/*  59 */     r.setTrainingSet(samples);
/*  60 */     r.setFeatures(features);
/*  61 */     r.setMetricScorer(scorer);
/*  62 */     return r;
/*     */   }
/*     */ 
/*     */   
/*     */   public Ranker createRanker(String className) {
/*  67 */     Ranker r = null;
/*     */     try {
/*  69 */       Class<?> c = Class.forName(className);
/*  70 */       r = (Ranker)c.newInstance();
/*     */     }
/*  72 */     catch (ClassNotFoundException e) {
/*  73 */       System.out.println("Could find the class \"" + className + "\" you specified. Make sure the jar library is in your classpath.");
/*  74 */       e.printStackTrace();
/*  75 */       System.exit(1);
/*     */     }
/*  77 */     catch (InstantiationException e) {
/*  78 */       System.out.println("Cannot create objects from the class \"" + className + "\" you specified.");
/*  79 */       e.printStackTrace();
/*  80 */       System.exit(1);
/*     */     }
/*  82 */     catch (IllegalAccessException e) {
/*  83 */       System.out.println("The class \"" + className + "\" does not implement the Ranker interface.");
/*  84 */       e.printStackTrace();
/*  85 */       System.exit(1);
/*     */     } 
/*  87 */     return r;
/*     */   }
/*     */   
/*     */   public Ranker createRanker(String className, List<RankList> samples, int[] features, MetricScorer scorer) {
/*  91 */     Ranker r = createRanker(className);
/*  92 */     r.setTrainingSet(samples);
/*  93 */     r.setFeatures(features);
/*  94 */     r.setMetricScorer(scorer);
/*  95 */     return r;
/*     */   }
/*     */   
/*     */   public Ranker loadRankerFromFile(String modelFile) {
/*  99 */     return loadRankerFromString(FileUtils.read(modelFile, "ASCII"));
/*     */   }
/*     */   
/*     */   public Ranker loadRankerFromString(String fullText) {
/* 103 */     try (BufferedReader in = new BufferedReader(new StringReader(fullText))) {
/*     */       
/* 105 */       String content = in.readLine();
/* 106 */       content = content.replace("## ", "").trim();
/* 107 */       System.out.println("Model:\t\t" + content);
/* 108 */       Ranker r = createRanker(map.get(content.toUpperCase()));
/* 109 */       r.loadFromString(fullText);
/* 110 */       return r;
/*     */     }
/* 112 */     catch (Exception ex) {
/*     */       
/* 114 */       throw RankLibError.create(ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\learning\RankerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */