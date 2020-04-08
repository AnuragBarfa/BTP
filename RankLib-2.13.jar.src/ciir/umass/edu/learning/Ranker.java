/*     */ package ciir.umass.edu.learning;
/*     */ 
/*     */ import ciir.umass.edu.metric.MetricScorer;
/*     */ import ciir.umass.edu.utilities.FileUtils;
/*     */ import ciir.umass.edu.utilities.MergeSorter;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.nio.file.attribute.PosixFilePermission;
/*     */ import java.nio.file.attribute.PosixFilePermissions;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ public abstract class Ranker
/*     */ {
/*     */   public static boolean verbose = true;
/*  43 */   protected List<RankList> samples = new ArrayList<>();
/*  44 */   protected int[] features = null;
/*  45 */   protected MetricScorer scorer = null;
/*  46 */   protected double scoreOnTrainingData = 0.0D;
/*  47 */   protected double bestScoreOnValidationData = 0.0D;
/*     */   
/*  49 */   protected List<RankList> validationSamples = null;
/*     */ 
/*     */ 
/*     */   
/*     */   protected Ranker() {}
/*     */ 
/*     */   
/*     */   protected Ranker(List<RankList> samples, int[] features, MetricScorer scorer) {
/*  57 */     this.samples = samples;
/*  58 */     this.features = features;
/*  59 */     this.scorer = scorer;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTrainingSet(List<RankList> samples) {
/*  65 */     this.samples = samples;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFeatures(int[] features) {
/*  70 */     this.features = features;
/*     */   }
/*     */   
/*     */   public void setValidationSet(List<RankList> samples) {
/*  74 */     this.validationSamples = samples;
/*     */   }
/*     */   
/*     */   public void setMetricScorer(MetricScorer scorer) {
/*  78 */     this.scorer = scorer;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getScoreOnTrainingData() {
/*  83 */     return this.scoreOnTrainingData;
/*     */   }
/*     */   
/*     */   public double getScoreOnValidationData() {
/*  87 */     return this.bestScoreOnValidationData;
/*     */   }
/*     */ 
/*     */   
/*     */   public int[] getFeatures() {
/*  92 */     return this.features;
/*     */   }
/*     */ 
/*     */   
/*     */   public RankList rank(RankList rl) {
/*  97 */     double[] scores = new double[rl.size()];
/*  98 */     for (int i = 0; i < rl.size(); i++)
/*  99 */       scores[i] = eval(rl.get(i)); 
/* 100 */     int[] idx = MergeSorter.sort(scores, false);
/* 101 */     return new RankList(rl, idx);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<RankList> rank(List<RankList> l) {
/* 106 */     List<RankList> ll = new ArrayList<>();
/* 107 */     for (int i = 0; i < l.size(); i++)
/* 108 */       ll.add(rank(l.get(i))); 
/* 109 */     return ll;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void save(String modelFile) {
/* 116 */     Path parentPath = Paths.get(modelFile, new String[0]).toAbsolutePath().getParent();
/*     */ 
/*     */     
/* 119 */     if (Files.notExists(parentPath, new java.nio.file.LinkOption[0])) {
/*     */       try {
/* 121 */         Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxr-xr-x");
/* 122 */         FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
/* 123 */         Path path = Files.createDirectory(parentPath, (FileAttribute<?>[])new FileAttribute[] { attr });
/*     */       }
/* 125 */       catch (Exception e) {
/* 126 */         System.out.println("Error creating kcv model file directory " + modelFile);
/*     */       } 
/*     */     }
/*     */     
/* 130 */     FileUtils.write(modelFile, "ASCII", model());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void PRINT(String msg) {
/* 135 */     if (verbose) {
/* 136 */       System.out.print(msg);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void PRINTLN(String msg) {
/* 141 */     if (verbose) {
/* 142 */       System.out.println(msg);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void PRINT(int[] len, String[] msgs) {
/* 147 */     if (verbose)
/*     */     {
/* 149 */       for (int i = 0; i < msgs.length; i++) {
/*     */         
/* 151 */         String msg = msgs[i];
/* 152 */         if (msg.length() > len[i]) {
/* 153 */           msg = msg.substring(0, len[i]);
/*     */         } else {
/* 155 */           while (msg.length() < len[i])
/* 156 */             msg = msg + " "; 
/* 157 */         }  System.out.print(msg + " | ");
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   protected void PRINTLN(int[] len, String[] msgs) {
/* 163 */     PRINT(len, msgs);
/* 164 */     PRINTLN("");
/*     */   }
/*     */   
/*     */   protected void PRINTTIME() {
/* 168 */     DateFormat dateFormat = new SimpleDateFormat("MM/dd HH:mm:ss");
/* 169 */     Date date = new Date();
/* 170 */     System.out.println(dateFormat.format(date));
/*     */   }
/*     */   
/*     */   protected void PRINT_MEMORY_USAGE() {
/* 174 */     System.out.println("***** " + Runtime.getRuntime().freeMemory() + " / " + Runtime.getRuntime().maxMemory());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void copy(double[] source, double[] target) {
/* 179 */     for (int j = 0; j < source.length; j++) {
/* 180 */       target[j] = source[j];
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract void init();
/*     */   
/*     */   public abstract void learn();
/*     */   
/*     */   public double eval(DataPoint p) {
/* 190 */     return -1.0D;
/*     */   }
/*     */   
/*     */   public abstract Ranker createNew();
/*     */   
/*     */   public abstract String toString();
/*     */   
/*     */   public abstract String model();
/*     */   
/*     */   public abstract void loadFromString(String paramString);
/*     */   
/*     */   public abstract String name();
/*     */   
/*     */   public abstract void printParameters();
/*     */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\learning\Ranker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */