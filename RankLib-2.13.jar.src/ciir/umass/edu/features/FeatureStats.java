/*     */ package ciir.umass.edu.features;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
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
/*     */ public class FeatureStats
/*     */ {
/*     */   private String modelName;
/*     */   private String modelFileName;
/*     */   private File f;
/*     */   private BufferedReader br;
/*     */   
/*     */   protected FeatureStats(String modelFileName) {
/*     */     try {
/*  40 */       this.f = new File(modelFileName);
/*  41 */       this.modelFileName = this.f.getAbsolutePath();
/*  42 */       this.br = new BufferedReader(new FileReader(this.f));
/*     */ 
/*     */       
/*  45 */       String modelLine = this.br.readLine().trim();
/*  46 */       String[] nameparts = modelLine.split(" ");
/*  47 */       int len = nameparts.length;
/*     */       
/*  49 */       if (len == 2) {
/*  50 */         this.modelName = nameparts[1].trim();
/*     */       }
/*  52 */       else if (len == 3) {
/*  53 */         this.modelName = nameparts[1].trim() + " " + nameparts[2].trim();
/*     */       }
/*     */     
/*  56 */     } catch (IOException ioex) {
/*  57 */       System.out.println("IOException opening model file " + modelFileName + ". Quitting.");
/*  58 */       System.exit(1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private TreeMap<Integer, Integer> getFeatureWeightFeatureFrequencies() {
/*  65 */     TreeMap<Integer, Integer> tm = new TreeMap<>();
/*     */     
/*     */     try {
/*  68 */       String line = null;
/*  69 */       while ((line = this.br.readLine()) != null) {
/*  70 */         line = line.trim().toLowerCase();
/*     */         
/*  72 */         if (line.length() == 0) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/*  77 */         if (line.contains("##")) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  84 */         String[] featureLines = line.split(" ");
/*  85 */         int featureFreq = 0;
/*     */         
/*  87 */         for (int i = 0; i < featureLines.length; i++) {
/*  88 */           Integer featureID = Integer.valueOf(featureLines[i].split(":")[0]);
/*     */           
/*  90 */           if (tm.containsKey(featureID)) {
/*  91 */             featureFreq = ((Integer)tm.get(featureID)).intValue();
/*  92 */             featureFreq++;
/*  93 */             tm.put(featureID, Integer.valueOf(featureFreq));
/*     */           } else {
/*     */             
/*  96 */             tm.put(featureID, Integer.valueOf(1));
/*     */           }
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 104 */     catch (Exception ex) {
/* 105 */       System.out.println("Exception: " + ex.toString());
/* 106 */       System.exit(1);
/*     */     } 
/*     */     
/* 109 */     return tm;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private TreeMap<Integer, Integer> getTreeFeatureFrequencies() {
/* 115 */     TreeMap<Integer, Integer> tm = new TreeMap<>();
/*     */     
/*     */     try {
/* 118 */       String line = null;
/* 119 */       while ((line = this.br.readLine()) != null) {
/* 120 */         line = line.trim().toLowerCase();
/*     */         
/* 122 */         if (line.length() == 0) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/* 127 */         if (line.contains("##")) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/* 132 */         if (line.contains("<feature>")) {
/* 133 */           int quote1 = line.indexOf('>', 0);
/* 134 */           int quote2 = line.indexOf('<', quote1 + 1);
/* 135 */           String featureIdStr = line.substring(quote1 + 1, quote2);
/* 136 */           Integer featureID = Integer.valueOf(featureIdStr.trim());
/*     */           
/* 138 */           if (tm.containsKey(featureID)) {
/* 139 */             int featureFreq = ((Integer)tm.get(featureID)).intValue();
/* 140 */             featureFreq++;
/* 141 */             tm.put(featureID, Integer.valueOf(featureFreq));
/*     */             continue;
/*     */           } 
/* 144 */           tm.put(featureID, Integer.valueOf(1));
/*     */         }
/*     */       
/*     */       }
/*     */     
/* 149 */     } catch (Exception ex) {
/* 150 */       System.out.println("Exception: " + ex.toString());
/* 151 */       System.exit(1);
/*     */     } 
/*     */     
/* 154 */     return tm;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeFeatureStats() {
/* 160 */     int featureMin = Integer.MAX_VALUE;
/* 161 */     int featureMax = 0;
/* 162 */     int featuresUsed = 0;
/* 163 */     int featureFreq = 0;
/* 164 */     String modelName = this.modelName;
/* 165 */     TreeMap<Integer, Integer> featureTM = null;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 170 */       if (modelName == null) {
/* 171 */         System.out.println("No model name defined.  Quitting.");
/* 172 */         System.exit(1);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 177 */       if (modelName.equals("Coordinate Ascent") || modelName
/* 178 */         .equals("LambdaRank") || modelName
/* 179 */         .equals("Linear Regression") || modelName
/* 180 */         .equals("ListNet") || modelName
/* 181 */         .equals("RankNet")) {
/* 182 */         System.out.println(modelName + " uses all features.  Can't do selected model statistics for this algorithm.");
/* 183 */         System.exit(0);
/*     */ 
/*     */       
/*     */       }
/* 187 */       else if (modelName.equals("AdaRank") || modelName
/* 188 */         .equals("RankBoost")) {
/* 189 */         featureTM = getFeatureWeightFeatureFrequencies();
/*     */ 
/*     */       
/*     */       }
/* 193 */       else if (modelName.equals("LambdaMART") || modelName
/* 194 */         .equals("MART") || modelName
/* 195 */         .equals("Random Forests")) {
/* 196 */         featureTM = getTreeFeatureFrequencies();
/*     */       } 
/*     */       
/* 199 */       this.br.close();
/*     */     }
/* 201 */     catch (IOException ioe) {
/* 202 */       System.out.println("IOException on file " + this.modelFileName);
/* 203 */       System.exit(1);
/*     */     } 
/*     */ 
/*     */     
/* 207 */     featuresUsed = featureTM.size();
/*     */ 
/*     */     
/* 210 */     System.out.println("\nModel File: " + this.modelFileName);
/* 211 */     System.out.println("Algorithm : " + modelName);
/* 212 */     System.out.println("");
/* 213 */     System.out.println("Feature frequencies : ");
/*     */     
/* 215 */     Set<Map.Entry<Integer, Integer>> s = featureTM.entrySet();
/* 216 */     DescriptiveStatistics ds = new DescriptiveStatistics();
/*     */     
/* 218 */     Iterator<Map.Entry<Integer, Integer>> it = s.iterator();
/* 219 */     while (it.hasNext()) {
/* 220 */       Map.Entry e = it.next();
/* 221 */       int freqID = ((Integer)e.getKey()).intValue();
/* 222 */       int freq = ((Integer)e.getValue()).intValue();
/* 223 */       System.out.printf("\tFeature[%d] : %7d\n", new Object[] { Integer.valueOf(freqID), Integer.valueOf(freq) });
/* 224 */       ds.addValue(freq);
/*     */     } 
/*     */ 
/*     */     
/* 228 */     System.out.println(" ");
/* 229 */     System.out.printf("Total Features Used: %d\n\n", new Object[] { Integer.valueOf(featuresUsed) });
/* 230 */     System.out.printf("Min frequency    : %10.2f\n", new Object[] { Double.valueOf(ds.getMin()) });
/* 231 */     System.out.printf("Max frequency    : %10.2f\n", new Object[] { Double.valueOf(ds.getMax()) });
/*     */     
/* 233 */     System.out.printf("Median frequency : %10.2f\n", new Object[] { Double.valueOf(ds.getPercentile(50.0D)) });
/*     */     
/* 235 */     System.out.printf("Avg frequency    : %10.2f\n", new Object[] { Double.valueOf(ds.getMean()) });
/* 236 */     System.out.printf("Variance         : %10.2f\n", new Object[] { Double.valueOf(ds.getVariance()) });
/* 237 */     System.out.printf("STD              : %10.2f\n", new Object[] { Double.valueOf(ds.getStandardDeviation()) });
/*     */   }
/*     */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\features\FeatureStats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */