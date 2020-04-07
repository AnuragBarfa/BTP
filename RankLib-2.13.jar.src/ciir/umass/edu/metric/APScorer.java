/*     */ package ciir.umass.edu.metric;
/*     */ 
/*     */ import ciir.umass.edu.learning.RankList;
/*     */ import ciir.umass.edu.utilities.FileUtils;
/*     */ import ciir.umass.edu.utilities.RankLibError;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
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
/*     */ public class APScorer
/*     */   extends MetricScorer
/*     */ {
/*  29 */   public HashMap<String, Integer> relDocCount = null;
/*     */ 
/*     */   
/*     */   public APScorer() {
/*  33 */     this.k = 0;
/*     */   }
/*     */   
/*     */   public MetricScorer copy() {
/*  37 */     return new APScorer();
/*     */   }
/*     */   
/*     */   public void loadExternalRelevanceJudgment(String qrelFile) {
/*  41 */     this.relDocCount = new HashMap<>();
/*  42 */     try (BufferedReader in = FileUtils.smartReader(qrelFile)) {
/*  43 */       String content = "";
/*  44 */       while ((content = in.readLine()) != null) {
/*     */         
/*  46 */         content = content.trim();
/*  47 */         if (content.length() == 0)
/*     */           continue; 
/*  49 */         String[] s = content.split(" ");
/*  50 */         String qid = s[0].trim();
/*     */         
/*  52 */         int label = (int)Math.rint(Double.parseDouble(s[3].trim()));
/*  53 */         if (label > 0) {
/*  54 */           int prev = ((Integer)this.relDocCount.getOrDefault(qid, Integer.valueOf(0))).intValue();
/*  55 */           this.relDocCount.put(qid, Integer.valueOf(prev + 1));
/*     */         } 
/*     */       } 
/*     */       
/*  59 */       System.out.println("Relevance judgment file loaded. [#q=" + this.relDocCount.size() + "]");
/*     */     }
/*  61 */     catch (IOException ex) {
/*     */       
/*  63 */       throw RankLibError.create("Error in APScorer::loadExternalRelevanceJudgment(): ", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double score(RankList rl) {
/*  73 */     double ap = 0.0D;
/*  74 */     int count = 0;
/*  75 */     for (int i = 0; i < rl.size(); i++) {
/*     */       
/*  77 */       if (rl.get(i).getLabel() > 0.0D) {
/*     */         
/*  79 */         count++;
/*  80 */         ap += count / (i + 1);
/*     */       } 
/*     */     } 
/*     */     
/*  84 */     int rdCount = 0;
/*  85 */     if (this.relDocCount != null) {
/*     */       
/*  87 */       Integer it = this.relDocCount.get(rl.getID());
/*  88 */       if (it != null) {
/*  89 */         rdCount = it.intValue();
/*     */       }
/*     */     } else {
/*  92 */       rdCount = count;
/*     */     } 
/*  94 */     if (rdCount == 0)
/*  95 */       return 0.0D; 
/*  96 */     return ap / rdCount;
/*     */   }
/*     */   
/*     */   public String name() {
/* 100 */     return "MAP";
/*     */   }
/*     */ 
/*     */   
/*     */   public double[][] swapChange(RankList rl) {
/* 105 */     int[] relCount = new int[rl.size()];
/* 106 */     int[] labels = new int[rl.size()];
/* 107 */     int count = 0;
/* 108 */     for (int i = 0; i < rl.size(); i++) {
/*     */       
/* 110 */       if (rl.get(i).getLabel() > 0.0F) {
/*     */         
/* 112 */         labels[i] = 1;
/* 113 */         count++;
/*     */       } else {
/*     */         
/* 116 */         labels[i] = 0;
/* 117 */       }  relCount[i] = count;
/*     */     } 
/* 119 */     int rdCount = 0;
/* 120 */     if (this.relDocCount != null) {
/*     */       
/* 122 */       Integer it = this.relDocCount.get(rl.getID());
/* 123 */       if (it != null) {
/* 124 */         rdCount = it.intValue();
/*     */       }
/*     */     } else {
/* 127 */       rdCount = count;
/*     */     } 
/* 129 */     double[][] changes = new double[rl.size()][]; int j;
/* 130 */     for (j = 0; j < rl.size(); j++) {
/*     */       
/* 132 */       changes[j] = new double[rl.size()];
/* 133 */       Arrays.fill(changes[j], 0.0D);
/*     */     } 
/*     */     
/* 136 */     if (rdCount == 0 || count == 0) {
/* 137 */       return changes;
/*     */     }
/* 139 */     for (j = 0; j < rl.size() - 1; j++) {
/*     */       
/* 141 */       for (int k = j + 1; k < rl.size(); k++) {
/*     */         
/* 143 */         double change = 0.0D;
/* 144 */         if (labels[j] != labels[k]) {
/*     */           
/* 146 */           int diff = labels[k] - labels[j];
/* 147 */           change += ((relCount[j] + diff) * labels[k] - relCount[j] * labels[j]) / (j + 1);
/* 148 */           for (int m = j + 1; m <= k - 1; m++) {
/* 149 */             if (labels[m] > 0)
/* 150 */               change += diff / (m + 1); 
/* 151 */           }  change += (-relCount[k] * diff) / (k + 1);
/*     */         } 
/*     */         
/* 154 */         changes[j][k] = change / rdCount; changes[k][j] = change / rdCount;
/*     */       } 
/*     */     } 
/* 157 */     return changes;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\metric\APScorer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */