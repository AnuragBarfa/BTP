/*     */ package ciir.umass.edu.eval;
/*     */ 
/*     */ import ciir.umass.edu.stats.RandomPermutationTest;
/*     */ import ciir.umass.edu.utilities.FileUtils;
/*     */ import ciir.umass.edu.utilities.RankLibError;
/*     */ import ciir.umass.edu.utilities.SimpleMath;
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
/*     */ public class Analyzer
/*     */ {
/*     */   public static void main(String[] args) {
/*  23 */     String directory = "";
/*  24 */     String baseline = "";
/*  25 */     if (args.length < 2) {
/*     */       
/*  27 */       System.out.println("Usage: java -cp bin/RankLib.jar ciir.umass.edu.eval.Analyzer <Params>");
/*  28 */       System.out.println("Params:");
/*  29 */       System.out.println("\t-all <directory>\tDirectory of performance files (one per system)");
/*  30 */       System.out.println("\t-base <file>\t\tPerformance file for the baseline (MUST be in the same directory)");
/*  31 */       System.out.println("\t[ -np ] \t\tNumber of permutation (Fisher randomization test) [default=" + RandomPermutationTest.nPermutation + "]");
/*     */       
/*     */       return;
/*     */     } 
/*  35 */     for (int i = 0; i < args.length; i++) {
/*     */       
/*  37 */       if (args[i].compareTo("-all") == 0) {
/*  38 */         directory = args[++i];
/*  39 */       } else if (args[i].compareTo("-base") == 0) {
/*  40 */         baseline = args[++i];
/*  41 */       } else if (args[i].compareTo("-np") == 0) {
/*  42 */         RandomPermutationTest.nPermutation = Integer.parseInt(args[++i]);
/*     */       } 
/*     */     } 
/*  45 */     Analyzer a = new Analyzer();
/*  46 */     a.compare(directory, baseline);
/*     */   }
/*     */   
/*     */   static class Result
/*     */   {
/*  51 */     int status = 0;
/*  52 */     int win = 0;
/*  53 */     int loss = 0;
/*  54 */     int[] countByImprovementRange = null;
/*     */   }
/*     */   
/*  57 */   private RandomPermutationTest randomizedTest = new RandomPermutationTest();
/*  58 */   private static double[] improvementRatioThreshold = new double[] { -1.0D, -0.75D, -0.5D, -0.25D, 0.0D, 0.25D, 0.5D, 0.75D, 1.0D, 1000.0D };
/*  59 */   private int indexOfZero = 4;
/*     */   
/*     */   private int locateSegment(double value) {
/*  62 */     if (value > 0.0D) {
/*     */       
/*  64 */       for (int i = this.indexOfZero; i < improvementRatioThreshold.length; i++) {
/*  65 */         if (value <= improvementRatioThreshold[i])
/*  66 */           return i; 
/*     */       } 
/*  68 */     } else if (value < 0.0D) {
/*     */       
/*  70 */       for (int i = 0; i <= this.indexOfZero; i++) {
/*  71 */         if (value < improvementRatioThreshold[i])
/*  72 */           return i; 
/*     */       } 
/*  74 */     }  return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HashMap<String, Double> read(String filename) {
/*  84 */     HashMap<String, Double> performance = new HashMap<>();
/*  85 */     try (BufferedReader in = FileUtils.smartReader(filename)) {
/*     */       
/*  87 */       String content = "";
/*  88 */       while ((content = in.readLine()) != null) {
/*     */         
/*  90 */         content = content.trim();
/*  91 */         if (content.length() == 0) {
/*     */           continue;
/*     */         }
/*     */         
/*  95 */         while (content.contains("  "))
/*  96 */           content = content.replace("  ", " "); 
/*  97 */         content = content.replace(" ", "\t");
/*  98 */         String[] s = content.split("\t");
/*     */         
/* 100 */         String id = s[1];
/* 101 */         double p = Double.parseDouble(s[2]);
/* 102 */         performance.put(id, Double.valueOf(p));
/*     */       } 
/* 104 */       in.close();
/* 105 */       System.out.println("Reading " + filename + "... " + performance.size() + " ranked lists [Done]");
/*     */     }
/* 107 */     catch (IOException ex) {
/*     */       
/* 109 */       throw RankLibError.create(ex);
/*     */     } 
/* 111 */     return performance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void compare(String directory, String baseFile) {
/* 120 */     directory = FileUtils.makePathStandard(directory);
/* 121 */     List<String> targets = FileUtils.getAllFiles2(directory);
/* 122 */     for (int i = 0; i < targets.size(); i++) {
/*     */       
/* 124 */       if (((String)targets.get(i)).compareTo(baseFile) == 0) {
/*     */         
/* 126 */         targets.remove(i);
/* 127 */         i--;
/*     */       } else {
/*     */         
/* 130 */         targets.set(i, directory + (String)targets.get(i));
/*     */       } 
/* 132 */     }  compare(targets, directory + baseFile);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void compare(List<String> targetFiles, String baseFile) {
/* 141 */     HashMap<String, Double> base = read(baseFile);
/* 142 */     List<HashMap<String, Double>> targets = new ArrayList<>();
/* 143 */     for (int i = 0; i < targetFiles.size(); i++) {
/*     */       
/* 145 */       HashMap<String, Double> hm = read(targetFiles.get(i));
/* 146 */       targets.add(hm);
/*     */     } 
/* 148 */     Result[] rs = compare(base, targets);
/*     */ 
/*     */     
/* 151 */     System.out.println("");
/* 152 */     System.out.println("");
/* 153 */     System.out.println("Overall comparison");
/* 154 */     System.out.println("------------------------------------------------------------------------");
/* 155 */     System.out.println("System\tPerformance\tImprovement\tWin\tLoss\tp-value");
/* 156 */     System.out.println(FileUtils.getFileName(baseFile) + " [baseline]\t" + SimpleMath.round(((Double)base.get("all")).doubleValue(), 4));
/* 157 */     for (int j = 0; j < rs.length; j++) {
/*     */       
/* 159 */       if ((rs[j]).status == 0) {
/*     */         
/* 161 */         double delta = ((Double)((HashMap)targets.get(j)).get("all")).doubleValue() - ((Double)base.get("all")).doubleValue();
/* 162 */         double dp = delta * 100.0D / ((Double)base.get("all")).doubleValue();
/* 163 */         String msg = FileUtils.getFileName(targetFiles.get(j)) + "\t" + SimpleMath.round(((Double)((HashMap)targets.get(j)).get("all")).doubleValue(), 4);
/* 164 */         msg = msg + "\t" + ((delta > 0.0D) ? "+" : "") + SimpleMath.round(delta, 4) + " (" + ((delta > 0.0D) ? "+" : "") + SimpleMath.round(dp, 2) + "%)";
/* 165 */         msg = msg + "\t" + (rs[j]).win + "\t" + (rs[j]).loss;
/* 166 */         msg = msg + "\t" + this.randomizedTest.test(targets.get(j), base) + "";
/* 167 */         System.out.println(msg);
/*     */       } else {
/*     */         
/* 170 */         System.out.println("WARNING: [" + (String)targetFiles.get(j) + "] skipped: NOT comparable to the baseline due to different ranked list IDs.");
/*     */       } 
/*     */     } 
/* 173 */     System.out.println("");
/* 174 */     System.out.println("");
/* 175 */     System.out.println("Detailed break down");
/* 176 */     System.out.println("------------------------------------------------------------------------");
/* 177 */     String header = "";
/* 178 */     String[] tmp = new String[improvementRatioThreshold.length]; int k;
/* 179 */     for (k = 0; k < improvementRatioThreshold.length; k++) {
/*     */       
/* 181 */       String t = (int)(improvementRatioThreshold[k] * 100.0D) + "%";
/* 182 */       if (improvementRatioThreshold[k] > 0.0D)
/* 183 */         t = "+" + t; 
/* 184 */       tmp[k] = t;
/*     */     } 
/* 186 */     header = header + "[ < " + tmp[0] + ")\t";
/* 187 */     for (k = 0; k < improvementRatioThreshold.length - 2; k++) {
/*     */       
/* 189 */       if (k >= this.indexOfZero) {
/* 190 */         header = header + "(" + tmp[k] + ", " + tmp[k + 1] + "]\t";
/*     */       } else {
/* 192 */         header = header + "[" + tmp[k] + ", " + tmp[k + 1] + ")\t";
/*     */       } 
/* 194 */     }  header = header + "( > " + tmp[improvementRatioThreshold.length - 2] + "]";
/* 195 */     System.out.println("\t" + header);
/*     */     
/* 197 */     for (k = 0; k < targets.size(); k++) {
/*     */       
/* 199 */       String msg = FileUtils.getFileName(targetFiles.get(k));
/* 200 */       for (int m = 0; m < (rs[k]).countByImprovementRange.length; m++)
/* 201 */         msg = msg + "\t" + (rs[k]).countByImprovementRange[m]; 
/* 202 */       System.out.println(msg);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Result[] compare(HashMap<String, Double> base, List<HashMap<String, Double>> targets) {
/* 214 */     Result[] rs = new Result[targets.size()];
/* 215 */     for (int i = 0; i < targets.size(); i++)
/* 216 */       rs[i] = compare(base, targets.get(i)); 
/* 217 */     return rs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Result compare(HashMap<String, Double> base, HashMap<String, Double> target) {
/* 227 */     Result r = new Result();
/* 228 */     if (base.size() != target.size()) {
/*     */       
/* 230 */       r.status = -1;
/* 231 */       return r;
/*     */     } 
/*     */     
/* 234 */     r.countByImprovementRange = new int[improvementRatioThreshold.length];
/* 235 */     Arrays.fill(r.countByImprovementRange, 0);
/* 236 */     for (String key : base.keySet()) {
/*     */       
/* 238 */       if (!target.containsKey(key)) {
/*     */         
/* 240 */         r.status = -2;
/* 241 */         return r;
/*     */       } 
/* 243 */       if (key.compareTo("all") == 0)
/*     */         continue; 
/* 245 */       double p = ((Double)base.get(key)).doubleValue();
/* 246 */       double pt = ((Double)target.get(key)).doubleValue();
/* 247 */       if (pt > p) {
/* 248 */         r.win++;
/* 249 */       } else if (pt < p) {
/* 250 */         r.loss++;
/* 251 */       }  double change = pt - p;
/* 252 */       if (change != 0.0D)
/* 253 */         r.countByImprovementRange[locateSegment(change)] = r.countByImprovementRange[locateSegment(change)] + 1; 
/*     */     } 
/* 255 */     return r;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\eval\Analyzer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */