/*     */ package ciir.umass.edu.features;
/*     */ 
/*     */ import ciir.umass.edu.learning.DataPoint;
/*     */ import ciir.umass.edu.learning.DenseDataPoint;
/*     */ import ciir.umass.edu.learning.RankList;
/*     */ import ciir.umass.edu.learning.SparseDataPoint;
/*     */ import ciir.umass.edu.utilities.FileUtils;
/*     */ import ciir.umass.edu.utilities.RankLibError;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStreamWriter;
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
/*     */ public class FeatureManager
/*     */ {
/*     */   public static void main(String[] args) {
/*  34 */     List<String> rankingFiles = new ArrayList<>();
/*  35 */     String outputDir = "";
/*  36 */     String modelFileName = "";
/*  37 */     boolean shuffle = false;
/*  38 */     boolean doFeatureStats = false;
/*     */     
/*  40 */     int nFold = 0;
/*  41 */     float tvs = -1.0F;
/*  42 */     int argsLen = args.length;
/*     */     
/*  44 */     if ((argsLen < 3 && !Arrays.<String>asList(args).contains("-feature_stats")) || (argsLen != 2 && 
/*  45 */       Arrays.<String>asList(args).contains("-feature_stats"))) {
/*     */       
/*  47 */       System.out.println("Usage: java -cp bin/RankLib.jar ciir.umass.edu.features.FeatureManager <Params>");
/*  48 */       System.out.println("Params:");
/*  49 */       System.out.println("\t-input <file>\t\tSource data (ranked lists)");
/*  50 */       System.out.println("\t-output <dir>\t\tThe output directory");
/*     */       
/*  52 */       System.out.println("");
/*  53 */       System.out.println("  [+] Shuffling");
/*  54 */       System.out.println("\t-shuffle\t\tCreate a copy of the input file in which the ordering of all ranked lists (e.g. queries) is randomized.");
/*  55 */       System.out.println("\t\t\t\t(the order among objects (e.g. documents) within each ranked list is certainly unchanged).");
/*     */ 
/*     */       
/*  58 */       System.out.println("  [+] k-fold Partitioning (sequential split)");
/*  59 */       System.out.println("\t-k <fold>\t\tThe number of folds");
/*  60 */       System.out.println("\t[ -tvs <x \\in [0..1]> ] Train-validation split ratio (x)(1.0-x)");
/*     */       
/*  62 */       System.out.println("");
/*  63 */       System.out.println("  NOTE: If both -shuffle and -k are specified, the input data will be shuffled and then sequentially partitioned.");
/*     */       
/*  65 */       System.out.println("");
/*  66 */       System.out.println("Feature Statistics -- Saved model feature use frequencies and statistics.");
/*  67 */       System.out.println("-input and -output parameters are not used.");
/*  68 */       System.out.println("\t-feature_stats\tName of a saved, feature-limited, LTR model text file.");
/*  69 */       System.out.println("\t\t\tDoes not process Coordinate Ascent, LambdaRank, ListNet or RankNet models.");
/*  70 */       System.out.println("\t\t\tas they include all features rather than selected feature subsets.");
/*  71 */       System.out.println("");
/*     */       
/*     */       return;
/*     */     } 
/*  75 */     for (int i = 0; i < args.length; i++) {
/*     */       
/*  77 */       if (args[i].equalsIgnoreCase("-input")) {
/*  78 */         rankingFiles.add(args[++i]);
/*  79 */       } else if (args[i].equalsIgnoreCase("-k")) {
/*  80 */         nFold = Integer.parseInt(args[++i]);
/*  81 */       } else if (args[i].equalsIgnoreCase("-shuffle")) {
/*  82 */         shuffle = true;
/*  83 */       } else if (args[i].equalsIgnoreCase("-tvs")) {
/*  84 */         tvs = Float.parseFloat(args[++i]);
/*  85 */       } else if (args[i].equalsIgnoreCase("-output")) {
/*  86 */         outputDir = FileUtils.makePathStandard(args[++i]);
/*     */       }
/*  88 */       else if (args[i].equalsIgnoreCase("-feature_stats")) {
/*  89 */         doFeatureStats = true;
/*  90 */         modelFileName = args[++i];
/*     */       } 
/*     */     } 
/*     */     
/*  94 */     if (shuffle || nFold > 0) {
/*     */       
/*  96 */       List<RankList> samples = readInput(rankingFiles);
/*     */       
/*  98 */       if (samples.size() == 0) {
/*     */         
/* 100 */         System.out.println("Error: The input file is empty.");
/*     */         
/*     */         return;
/*     */       } 
/* 104 */       String fn = FileUtils.getFileName(rankingFiles.get(0));
/*     */       
/* 106 */       if (shuffle) {
/*     */         
/* 108 */         fn = fn + ".shuffled";
/* 109 */         System.out.print("Shuffling... ");
/* 110 */         Collections.shuffle(samples);
/* 111 */         System.out.println("[Done]");
/* 112 */         System.out.print("Saving... ");
/* 113 */         save(samples, outputDir + fn);
/* 114 */         System.out.println("[Done]");
/*     */       } 
/*     */       
/* 117 */       if (nFold > 0)
/*     */       {
/* 119 */         List<List<RankList>> trains = new ArrayList<>();
/* 120 */         List<List<RankList>> tests = new ArrayList<>();
/* 121 */         List<List<RankList>> valis = new ArrayList<>();
/* 122 */         System.out.println("Partitioning... ");
/* 123 */         prepareCV(samples, nFold, tvs, trains, valis, tests);
/* 124 */         System.out.println("[Done]");
/*     */         
/*     */         try {
/* 127 */           for (int j = 0; j < trains.size(); j++)
/*     */           {
/* 129 */             System.out.print("Saving fold " + (j + 1) + "/" + nFold + "... ");
/* 130 */             save(trains.get(j), outputDir + "f" + (j + 1) + ".train." + fn);
/* 131 */             save(tests.get(j), outputDir + "f" + (j + 1) + ".test." + fn);
/* 132 */             if (tvs > 0.0F)
/* 133 */               save(valis.get(j), outputDir + "f" + (j + 1) + ".validation." + fn); 
/* 134 */             System.out.println("[Done]");
/*     */           }
/*     */         
/* 137 */         } catch (Exception ex) {
/*     */           
/* 139 */           throw RankLibError.create("Cannot save partition data.\nOccured in FeatureManager::main(): ", ex);
/*     */         }
/*     */       
/*     */       }
/*     */     
/* 144 */     } else if (doFeatureStats) {
/*     */       
/*     */       try {
/* 147 */         FeatureStats fs = new FeatureStats(modelFileName);
/* 148 */         fs.writeFeatureStats();
/*     */       }
/* 150 */       catch (Exception ex) {
/* 151 */         throw RankLibError.create("Failure processing saved " + modelFileName + " model file.\nError occurred in FeatureManager::main(): ", ex);
/*     */       } 
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
/*     */   
/*     */   public static List<RankList> readInput(String inputFile) {
/* 165 */     return readInput(inputFile, false, false);
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
/*     */   public static List<RankList> readInput(String inputFile, boolean mustHaveRelDoc, boolean useSparseRepresentation) {
/* 178 */     List<RankList> samples = new ArrayList<>();
/* 179 */     int countRL = 0;
/* 180 */     int countEntries = 0;
/*     */     
/*     */     try {
/* 183 */       String content = "";
/* 184 */       BufferedReader in = FileUtils.smartReader(inputFile);
/*     */       
/* 186 */       String lastID = "";
/* 187 */       boolean hasRel = false;
/* 188 */       List<DataPoint> rl = new ArrayList<>();
/*     */       
/* 190 */       while ((content = in.readLine()) != null) {
/*     */         DenseDataPoint denseDataPoint;
/* 192 */         content = content.trim();
/* 193 */         if (content.length() == 0) {
/*     */           continue;
/*     */         }
/* 196 */         if (content.indexOf("#") == 0) {
/*     */           continue;
/*     */         }
/* 199 */         if (countEntries % 10000 == 0) {
/* 200 */           System.out.print("\rReading feature file [" + inputFile + "]: " + countRL + "... ");
/*     */         }
/* 202 */         DataPoint qp = null;
/*     */         
/* 204 */         if (useSparseRepresentation) {
/* 205 */           SparseDataPoint sparseDataPoint = new SparseDataPoint(content);
/*     */         } else {
/* 207 */           denseDataPoint = new DenseDataPoint(content);
/*     */         } 
/* 209 */         if (lastID.compareTo("") != 0 && lastID.compareTo(denseDataPoint.getID()) != 0) {
/*     */           
/* 211 */           if (!mustHaveRelDoc || hasRel)
/* 212 */             samples.add(new RankList(rl)); 
/* 213 */           rl = new ArrayList<>();
/* 214 */           hasRel = false;
/*     */         } 
/*     */         
/* 217 */         if (denseDataPoint.getLabel() > 0.0F)
/* 218 */           hasRel = true; 
/* 219 */         lastID = denseDataPoint.getID();
/* 220 */         rl.add(denseDataPoint);
/* 221 */         countEntries++;
/*     */       } 
/*     */       
/* 224 */       if (rl.size() > 0 && (!mustHaveRelDoc || hasRel)) {
/* 225 */         samples.add(new RankList(rl));
/*     */       }
/* 227 */       in.close();
/* 228 */       System.out.println("\rReading feature file [" + inputFile + "]... [Done.]            ");
/* 229 */       System.out.println("(" + samples.size() + " ranked lists, " + countEntries + " entries read)");
/*     */     }
/* 231 */     catch (Exception ex) {
/*     */       
/* 233 */       throw RankLibError.create("Error in FeatureManager::readInput(): ", ex);
/*     */     } 
/* 235 */     return samples;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<RankList> readInput(List<String> inputFiles) {
/* 246 */     List<RankList> samples = new ArrayList<>();
/*     */     
/* 248 */     for (int i = 0; i < inputFiles.size(); i++) {
/*     */       
/* 250 */       List<RankList> s = readInput(inputFiles.get(i), false, false);
/* 251 */       samples.addAll(s);
/*     */     } 
/* 253 */     return samples;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int[] readFeature(String featureDefFile) {
/* 264 */     int[] features = null;
/* 265 */     List<String> fids = new ArrayList<>();
/*     */     
/* 267 */     try (BufferedReader in = FileUtils.smartReader(featureDefFile)) {
/* 268 */       String content = "";
/*     */       
/* 270 */       while ((content = in.readLine()) != null) {
/*     */         
/* 272 */         content = content.trim();
/*     */         
/* 274 */         if (content.length() == 0) {
/*     */           continue;
/*     */         }
/* 277 */         if (content.indexOf("#") == 0) {
/*     */           continue;
/*     */         }
/* 280 */         fids.add(content.split("\t")[0].trim());
/*     */       } 
/* 282 */       in.close();
/* 283 */       features = new int[fids.size()];
/*     */       
/* 285 */       for (int i = 0; i < fids.size(); i++) {
/* 286 */         features[i] = Integer.parseInt((String)fids.get(i));
/*     */       }
/* 288 */     } catch (IOException ex) {
/*     */       
/* 290 */       throw RankLibError.create("Error in FeatureManager::readFeature(): ", ex);
/*     */     } 
/* 292 */     return features;
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
/*     */   public static int[] getFeatureFromSampleVector(List<RankList> samples) {
/* 307 */     if (samples.size() == 0)
/*     */     {
/* 309 */       throw RankLibError.create("Error in FeatureManager::getFeatureFromSampleVector(): There are no training samples.");
/*     */     }
/*     */     
/* 312 */     int fc = DataPoint.getFeatureCount();
/* 313 */     int[] features = new int[fc];
/*     */     
/* 315 */     for (int i = 1; i <= fc; i++) {
/* 316 */       features[i - 1] = i;
/*     */     }
/* 318 */     return features;
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
/*     */   public static void prepareCV(List<RankList> samples, int nFold, List<List<RankList>> trainingData, List<List<RankList>> testData) {
/* 333 */     prepareCV(samples, nFold, -1.0F, trainingData, null, testData);
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
/*     */   public static void prepareCV(List<RankList> samples, int nFold, float tvs, List<List<RankList>> trainingData, List<List<RankList>> validationData, List<List<RankList>> testData) {
/* 352 */     List<List<Integer>> trainSamplesIdx = new ArrayList<>();
/* 353 */     int size = samples.size() / nFold;
/* 354 */     int start = 0;
/* 355 */     int total = 0;
/*     */     
/* 357 */     for (int f = 0; f < nFold; f++) {
/*     */       
/* 359 */       List<Integer> t = new ArrayList<>();
/* 360 */       for (int j = 0; j < size && start + j < samples.size(); j++)
/* 361 */         t.add(Integer.valueOf(start + j)); 
/* 362 */       trainSamplesIdx.add(t);
/* 363 */       total += t.size();
/* 364 */       start += size;
/*     */     } 
/*     */     
/* 367 */     for (; total < samples.size(); total++) {
/* 368 */       ((List<Integer>)trainSamplesIdx.get(trainSamplesIdx.size() - 1)).add(Integer.valueOf(total));
/*     */     }
/* 370 */     for (int i = 0; i < trainSamplesIdx.size(); i++) {
/*     */       
/* 372 */       System.out.print("\rCreating data for fold-" + (i + 1) + "...");
/* 373 */       List<RankList> train = new ArrayList<>();
/* 374 */       List<RankList> test = new ArrayList<>();
/* 375 */       List<RankList> vali = new ArrayList<>();
/*     */ 
/*     */       
/* 378 */       List<Integer> t = trainSamplesIdx.get(i);
/*     */       
/* 380 */       for (int j = 0; j < samples.size(); j++) {
/*     */         
/* 382 */         if (t.contains(Integer.valueOf(j))) {
/* 383 */           test.add(new RankList(samples.get(j)));
/*     */         } else {
/* 385 */           train.add(new RankList(samples.get(j)));
/*     */         } 
/*     */       } 
/*     */       
/* 389 */       if (tvs > 0.0F) {
/*     */         
/* 391 */         int validationSize = (int)(train.size() * (1.0D - tvs));
/* 392 */         for (int k = 0; k < validationSize; k++) {
/*     */           
/* 394 */           vali.add(train.get(train.size() - 1));
/* 395 */           train.remove(train.size() - 1);
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 400 */       trainingData.add(train);
/* 401 */       testData.add(test);
/*     */       
/* 403 */       if (tvs > 0.0F)
/* 404 */         validationData.add(vali); 
/*     */     } 
/* 406 */     System.out.println("\rCreating data for " + nFold + " folds... [Done]            ");
/*     */     
/* 408 */     printQueriesForSplit("Train", trainingData);
/* 409 */     printQueriesForSplit("Validate", validationData);
/* 410 */     printQueriesForSplit("Test", testData);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void printQueriesForSplit(String name, List<List<RankList>> split) {
/* 415 */     if (split == null) {
/* 416 */       System.out.print("No " + name + " split.");
/*     */       return;
/*     */     } 
/* 419 */     for (int i = 0; i < split.size(); i++) {
/* 420 */       List<RankList> rankLists = split.get(i);
/* 421 */       System.out.print(name + "[" + i + "]=");
/*     */       
/* 423 */       for (RankList rankList : rankLists) {
/* 424 */         System.out.print(" \"" + rankList.getID() + "\"");
/*     */       }
/* 426 */       System.out.println();
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
/*     */ 
/*     */   
/*     */   public static void prepareSplit(List<RankList> samples, double percentTrain, List<RankList> trainingData, List<RankList> testData) {
/* 440 */     int size = (int)(samples.size() * percentTrain);
/*     */     int i;
/* 442 */     for (i = 0; i < size; i++) {
/* 443 */       trainingData.add(new RankList(samples.get(i)));
/*     */     }
/* 445 */     for (i = size; i < samples.size(); i++) {
/* 446 */       testData.add(new RankList(samples.get(i)));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void save(List<RankList> samples, String outputFile) {
/*     */     try {
/* 458 */       BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
/*     */       
/* 460 */       for (RankList sample : samples) save(sample, out); 
/* 461 */       out.close();
/*     */     }
/* 463 */     catch (Exception ex) {
/*     */       
/* 465 */       throw RankLibError.create("Error in FeatureManager::save(): ", ex);
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
/*     */   
/*     */   private static void save(RankList r, BufferedWriter out) throws Exception {
/* 478 */     for (int j = 0; j < r.size(); j++) {
/*     */       
/* 480 */       out.write(r.get(j).toString());
/* 481 */       out.newLine();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\features\FeatureManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */