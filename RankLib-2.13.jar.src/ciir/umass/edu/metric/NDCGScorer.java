/*     */ package ciir.umass.edu.metric;
/*     */ 
/*     */ import ciir.umass.edu.learning.RankList;
/*     */ import ciir.umass.edu.utilities.FileUtils;
/*     */ import ciir.umass.edu.utilities.RankLibError;
/*     */ import ciir.umass.edu.utilities.Sorter;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
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
/*     */ public class NDCGScorer
/*     */   extends DCGScorer
/*     */ {
/*  29 */   protected HashMap<String, Double> idealGains = null;
/*     */ 
/*     */ 
/*     */   
/*     */   public NDCGScorer() {
/*  34 */     this.idealGains = new HashMap<>();
/*     */   }
/*     */   
/*     */   public NDCGScorer(int k) {
/*  38 */     super(k);
/*  39 */     this.idealGains = new HashMap<>();
/*     */   }
/*     */   
/*     */   public MetricScorer copy() {
/*  43 */     return new NDCGScorer();
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadExternalRelevanceJudgment(String qrelFile) {
/*  48 */     try (BufferedReader in = FileUtils.smartReader(qrelFile)) {
/*     */       
/*  50 */       String content = "";
/*  51 */       String lastQID = "";
/*  52 */       List<Integer> rel = new ArrayList<>();
/*  53 */       int nQueries = 0;
/*  54 */       while ((content = in.readLine()) != null) {
/*     */         
/*  56 */         content = content.trim();
/*  57 */         if (content.length() == 0)
/*     */           continue; 
/*  59 */         String[] s = content.split(" ");
/*  60 */         String qid = s[0].trim();
/*     */         
/*  62 */         int label = (int)Math.rint(Double.parseDouble(s[3].trim()));
/*  63 */         if (lastQID.compareTo("") != 0 && lastQID.compareTo(qid) != 0) {
/*     */           
/*  65 */           int size = (rel.size() > this.k) ? this.k : rel.size();
/*  66 */           int[] r = new int[rel.size()];
/*  67 */           for (int i = 0; i < rel.size(); i++)
/*  68 */             r[i] = ((Integer)rel.get(i)).intValue(); 
/*  69 */           double ideal = getIdealDCG(r, size);
/*  70 */           this.idealGains.put(lastQID, Double.valueOf(ideal));
/*  71 */           rel.clear();
/*  72 */           nQueries++;
/*     */         } 
/*  74 */         lastQID = qid;
/*  75 */         rel.add(Integer.valueOf(label));
/*     */       } 
/*  77 */       if (rel.size() > 0) {
/*     */         
/*  79 */         int size = (rel.size() > this.k) ? this.k : rel.size();
/*  80 */         int[] r = new int[rel.size()];
/*  81 */         for (int i = 0; i < rel.size(); i++)
/*  82 */           r[i] = ((Integer)rel.get(i)).intValue(); 
/*  83 */         double ideal = getIdealDCG(r, size);
/*  84 */         this.idealGains.put(lastQID, Double.valueOf(ideal));
/*  85 */         rel.clear();
/*  86 */         nQueries++;
/*     */       } 
/*  88 */       System.out.println("Relevance judgment file loaded. [#q=" + nQueries + "]");
/*  89 */     } catch (IOException ex) {
/*  90 */       throw RankLibError.create("Error in NDCGScorer::loadExternalRelevanceJudgment(): ", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double score(RankList rl) {
/* 100 */     if (rl.size() == 0) {
/* 101 */       return 0.0D;
/*     */     }
/* 103 */     int size = this.k;
/* 104 */     if (this.k > rl.size() || this.k <= 0) {
/* 105 */       size = rl.size();
/*     */     }
/* 107 */     int[] rel = getRelevanceLabels(rl);
/*     */     
/* 109 */     double ideal = 0.0D;
/* 110 */     Double d = this.idealGains.get(rl.getID());
/* 111 */     if (d != null) {
/* 112 */       ideal = d.doubleValue();
/*     */     } else {
/*     */       
/* 115 */       ideal = getIdealDCG(rel, size);
/* 116 */       this.idealGains.put(rl.getID(), Double.valueOf(ideal));
/*     */     } 
/*     */     
/* 119 */     if (ideal <= 0.0D) {
/* 120 */       return 0.0D;
/*     */     }
/* 122 */     return getDCG(rel, size) / ideal;
/*     */   }
/*     */   
/*     */   public double[][] swapChange(RankList rl) {
/* 126 */     int size = (rl.size() > this.k) ? this.k : rl.size();
/*     */     
/* 128 */     int[] rel = getRelevanceLabels(rl);
/* 129 */     double ideal = 0.0D;
/* 130 */     Double d = this.idealGains.get(rl.getID());
/* 131 */     if (d != null) {
/* 132 */       ideal = d.doubleValue();
/*     */     } else {
/*     */       
/* 135 */       ideal = getIdealDCG(rel, size);
/*     */     } 
/*     */ 
/*     */     
/* 139 */     double[][] changes = new double[rl.size()][]; int i;
/* 140 */     for (i = 0; i < rl.size(); i++) {
/*     */       
/* 142 */       changes[i] = new double[rl.size()];
/* 143 */       Arrays.fill(changes[i], 0.0D);
/*     */     } 
/*     */     
/* 146 */     for (i = 0; i < size; i++) {
/* 147 */       for (int j = i + 1; j < rl.size(); j++) {
/* 148 */         if (ideal > 0.0D)
/* 149 */         { changes[i][j] = (discount(i) - discount(j)) * (gain(rel[i]) - gain(rel[j])) / ideal; changes[j][i] = (discount(i) - discount(j)) * (gain(rel[i]) - gain(rel[j])) / ideal; } 
/*     */       } 
/* 151 */     }  return changes;
/*     */   }
/*     */   
/*     */   public String name() {
/* 155 */     return "NDCG@" + this.k;
/*     */   }
/*     */ 
/*     */   
/*     */   private double getIdealDCG(int[] rel, int topK) {
/* 160 */     int[] idx = Sorter.sort(rel, false);
/* 161 */     double dcg = 0.0D;
/* 162 */     for (int i = 0; i < topK; i++)
/* 163 */       dcg += gain(rel[idx[i]]) * discount(i); 
/* 164 */     return dcg;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\metric\NDCGScorer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */