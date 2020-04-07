/*     */ package ciir.umass.edu.learning.neuralnet;
/*     */ 
/*     */ import ciir.umass.edu.learning.DataPoint;
/*     */ import ciir.umass.edu.learning.RankList;
/*     */ import ciir.umass.edu.learning.Ranker;
/*     */ import ciir.umass.edu.metric.MetricScorer;
/*     */ import ciir.umass.edu.utilities.RankLibError;
/*     */ import ciir.umass.edu.utilities.SimpleMath;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.StringReader;
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ public class RankNet
/*     */   extends Ranker
/*     */ {
/*  34 */   public static int nIteration = 100;
/*  35 */   public static int nHiddenLayer = 1;
/*  36 */   public static int nHiddenNodePerLayer = 10;
/*  37 */   public static double learningRate = 5.0E-5D;
/*     */ 
/*     */   
/*  40 */   protected List<Layer> layers = new ArrayList<>();
/*  41 */   protected Layer inputLayer = null;
/*  42 */   protected Layer outputLayer = null;
/*     */ 
/*     */   
/*  45 */   protected List<List<Double>> bestModelOnValidation = new ArrayList<>();
/*     */   
/*  47 */   protected int totalPairs = 0;
/*  48 */   protected int misorderedPairs = 0;
/*  49 */   protected double error = 0.0D;
/*  50 */   protected double lastError = Double.MAX_VALUE;
/*  51 */   protected int straightLoss = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   public RankNet() {}
/*     */ 
/*     */   
/*     */   public RankNet(List<RankList> samples, int[] features, MetricScorer scorer) {
/*  59 */     super(samples, features, scorer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInputOutput(int nInput, int nOutput) {
/*  67 */     this.inputLayer = new Layer(nInput + 1);
/*  68 */     this.outputLayer = new Layer(nOutput);
/*  69 */     this.layers.clear();
/*  70 */     this.layers.add(this.inputLayer);
/*  71 */     this.layers.add(this.outputLayer);
/*     */   }
/*     */   
/*     */   protected void setInputOutput(int nInput, int nOutput, int nType) {
/*  75 */     this.inputLayer = new Layer(nInput + 1, nType);
/*  76 */     this.outputLayer = new Layer(nOutput, nType);
/*  77 */     this.layers.clear();
/*  78 */     this.layers.add(this.inputLayer);
/*  79 */     this.layers.add(this.outputLayer);
/*     */   }
/*     */   
/*     */   protected void addHiddenLayer(int size) {
/*  83 */     this.layers.add(this.layers.size() - 1, new Layer(size));
/*     */   }
/*     */   
/*     */   protected void wire() {
/*     */     int i;
/*  88 */     for (i = 0; i < this.inputLayer.size() - 1; i++) {
/*  89 */       for (int j = 0; j < ((Layer)this.layers.get(1)).size(); j++) {
/*  90 */         connect(0, i, 1, j);
/*     */       }
/*     */     } 
/*  93 */     for (i = 1; i < this.layers.size() - 1; i++) {
/*  94 */       for (int j = 0; j < ((Layer)this.layers.get(i)).size(); j++) {
/*  95 */         for (int k = 0; k < ((Layer)this.layers.get(i + 1)).size(); k++)
/*  96 */           connect(i, j, i + 1, k); 
/*     */       } 
/*     */     } 
/*  99 */     for (i = 1; i < this.layers.size(); i++) {
/* 100 */       for (int j = 0; j < ((Layer)this.layers.get(i)).size(); j++) {
/* 101 */         connect(0, this.inputLayer.size() - 1, i, j);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void connect(int sourceLayer, int sourceNeuron, int targetLayer, int targetNeuron) {
/* 119 */     new Synapse(((Layer)this.layers.get(sourceLayer)).get(sourceNeuron), ((Layer)this.layers.get(targetLayer)).get(targetNeuron));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addInput(DataPoint p) {
/* 127 */     for (int k = 0; k < this.inputLayer.size() - 1; k++) {
/* 128 */       this.inputLayer.get(k).addOutput(p.getFeatureValue(this.features[k]));
/*     */     }
/* 130 */     this.inputLayer.get(this.inputLayer.size() - 1).addOutput(1.0D);
/*     */   }
/*     */   
/*     */   protected void propagate(int i) {
/* 134 */     for (int k = 1; k < this.layers.size(); k++)
/* 135 */       ((Layer)this.layers.get(k)).computeOutput(i); 
/*     */   }
/*     */   
/*     */   protected int[][] batchFeedForward(RankList rl) {
/* 139 */     int[][] pairMap = new int[rl.size()][];
/* 140 */     for (int i = 0; i < rl.size(); i++) {
/*     */       
/* 142 */       addInput(rl.get(i));
/* 143 */       propagate(i);
/*     */       
/* 145 */       int count = 0;
/* 146 */       for (int j = 0; j < rl.size(); j++) {
/* 147 */         if (rl.get(i).getLabel() > rl.get(j).getLabel())
/* 148 */           count++; 
/*     */       } 
/* 150 */       pairMap[i] = new int[count];
/* 151 */       int k = 0;
/* 152 */       for (int m = 0; m < rl.size(); m++) {
/* 153 */         if (rl.get(i).getLabel() > rl.get(m).getLabel()) {
/* 154 */           pairMap[i][k++] = m;
/*     */         }
/*     */       } 
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
/* 167 */     return pairMap;
/*     */   }
/*     */   
/*     */   protected void batchBackPropagate(int[][] pairMap, float[][] pairWeight) {
/* 171 */     for (int i = 0; i < pairMap.length; i++) {
/*     */ 
/*     */       
/* 174 */       PropParameter p = new PropParameter(i, pairMap);
/* 175 */       this.outputLayer.computeDelta(p); int j;
/* 176 */       for (j = this.layers.size() - 2; j >= 1; j--) {
/* 177 */         ((Layer)this.layers.get(j)).updateDelta(p);
/*     */       }
/*     */       
/* 180 */       this.outputLayer.updateWeight(p);
/* 181 */       for (j = this.layers.size() - 2; j >= 1; j--)
/* 182 */         ((Layer)this.layers.get(j)).updateWeight(p); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void clearNeuronOutputs() {
/* 187 */     for (int k = 0; k < this.layers.size(); k++)
/* 188 */       ((Layer)this.layers.get(k)).clearOutputs(); 
/*     */   }
/*     */   
/*     */   protected float[][] computePairWeight(int[][] pairMap, RankList rl) {
/* 192 */     return (float[][])null;
/*     */   }
/*     */   
/*     */   protected RankList internalReorder(RankList rl) {
/* 196 */     return rl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void saveBestModelOnValidation() {
/* 204 */     for (int i = 0; i < this.layers.size() - 1; i++) {
/*     */       
/* 206 */       List<Double> l = this.bestModelOnValidation.get(i);
/* 207 */       l.clear();
/* 208 */       for (int j = 0; j < ((Layer)this.layers.get(i)).size(); j++) {
/*     */         
/* 210 */         Neuron n = ((Layer)this.layers.get(i)).get(j);
/* 211 */         for (int k = 0; k < n.getOutLinks().size(); k++)
/* 212 */           l.add(Double.valueOf(((Synapse)n.getOutLinks().get(k)).getWeight())); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void restoreBestModelOnValidation() {
/*     */     try {
/* 219 */       for (int i = 0; i < this.layers.size() - 1; i++) {
/*     */         
/* 221 */         List<Double> l = this.bestModelOnValidation.get(i);
/* 222 */         int c = 0;
/* 223 */         for (int j = 0; j < ((Layer)this.layers.get(i)).size(); j++) {
/*     */           
/* 225 */           Neuron n = ((Layer)this.layers.get(i)).get(j);
/* 226 */           for (int k = 0; k < n.getOutLinks().size(); k++) {
/* 227 */             ((Synapse)n.getOutLinks().get(k)).setWeight(((Double)l.get(c++)).doubleValue());
/*     */           }
/*     */         } 
/*     */       } 
/* 231 */     } catch (Exception ex) {
/*     */       
/* 233 */       throw RankLibError.create("Error in NeuralNetwork.restoreBestModelOnValidation(): ", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected double crossEntropy(double o1, double o2, double targetValue) {
/* 238 */     double oij = o1 - o2;
/* 239 */     double ce = -targetValue * oij + SimpleMath.logBase2(1.0D + Math.exp(oij));
/* 240 */     return ce;
/*     */   }
/*     */   
/*     */   protected void estimateLoss() {
/* 244 */     this.misorderedPairs = 0;
/* 245 */     this.error = 0.0D;
/* 246 */     for (int j = 0; j < this.samples.size(); j++) {
/*     */       
/* 248 */       RankList rl = this.samples.get(j);
/* 249 */       for (int k = 0; k < rl.size() - 1; k++) {
/*     */         
/* 251 */         double o1 = eval(rl.get(k));
/* 252 */         for (int l = k + 1; l < rl.size(); l++) {
/*     */           
/* 254 */           if (rl.get(k).getLabel() > rl.get(l).getLabel()) {
/*     */             
/* 256 */             double o2 = eval(rl.get(l));
/* 257 */             this.error += crossEntropy(o1, o2, 1.0D);
/* 258 */             if (o1 < o2)
/* 259 */               this.misorderedPairs++; 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 264 */     this.error = SimpleMath.round(this.error / this.totalPairs, 4);
/*     */ 
/*     */ 
/*     */     
/* 268 */     this.lastError = this.error;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init() {
/* 276 */     PRINT("Initializing... ");
/*     */ 
/*     */     
/* 279 */     setInputOutput(this.features.length, 1); int i;
/* 280 */     for (i = 0; i < nHiddenLayer; i++)
/* 281 */       addHiddenLayer(nHiddenNodePerLayer); 
/* 282 */     wire();
/*     */     
/* 284 */     this.totalPairs = 0;
/* 285 */     for (i = 0; i < this.samples.size(); i++) {
/*     */       
/* 287 */       RankList rl = ((RankList)this.samples.get(i)).getCorrectRanking();
/* 288 */       for (int j = 0; j < rl.size() - 1; j++) {
/* 289 */         for (int k = j + 1; k < rl.size(); k++) {
/* 290 */           if (rl.get(j).getLabel() > rl.get(k).getLabel())
/* 291 */             this.totalPairs++; 
/*     */         } 
/*     */       } 
/* 294 */     }  if (this.validationSamples != null)
/* 295 */       for (i = 0; i < this.layers.size(); i++) {
/* 296 */         this.bestModelOnValidation.add(new ArrayList<>());
/*     */       } 
/* 298 */     Neuron.learningRate = learningRate;
/* 299 */     PRINTLN("[Done]");
/*     */   }
/*     */   
/*     */   public void learn() {
/* 303 */     PRINTLN("-----------------------------------------");
/* 304 */     PRINTLN("Training starts...");
/* 305 */     PRINTLN("--------------------------------------------------");
/* 306 */     PRINTLN(new int[] { 7, 14, 9, 9 }, new String[] { "#epoch", "% mis-ordered", this.scorer.name() + "-T", this.scorer.name() + "-V" });
/* 307 */     PRINTLN(new int[] { 7, 14, 9, 9 }, new String[] { " ", "  pairs", " ", " " });
/* 308 */     PRINTLN("--------------------------------------------------");
/*     */     
/* 310 */     for (int i = 1; i <= nIteration; i++) {
/*     */       
/* 312 */       for (int j = 0; j < this.samples.size(); j++) {
/*     */         
/* 314 */         RankList rl = internalReorder(this.samples.get(j));
/* 315 */         int[][] pairMap = batchFeedForward(rl);
/* 316 */         float[][] pairWeight = computePairWeight(pairMap, rl);
/* 317 */         batchBackPropagate(pairMap, pairWeight);
/* 318 */         clearNeuronOutputs();
/*     */       } 
/*     */ 
/*     */       
/* 322 */       this.scoreOnTrainingData = this.scorer.score(rank(this.samples));
/* 323 */       estimateLoss();
/* 324 */       PRINT(new int[] { 7, 14 }, new String[] { i + "", SimpleMath.round(this.misorderedPairs / this.totalPairs, 4) + "" });
/*     */       
/* 326 */       if (i % 1 == 0) {
/*     */         
/* 328 */         PRINT(new int[] { 9 }, new String[] { SimpleMath.round(this.scoreOnTrainingData, 4) + "" });
/* 329 */         if (this.validationSamples != null) {
/*     */           
/* 331 */           double score = this.scorer.score(rank(this.validationSamples));
/* 332 */           if (score > this.bestScoreOnValidationData) {
/*     */             
/* 334 */             this.bestScoreOnValidationData = score;
/* 335 */             saveBestModelOnValidation();
/*     */           } 
/* 337 */           PRINT(new int[] { 9 }, new String[] { SimpleMath.round(score, 4) + "" });
/*     */         } 
/*     */       } 
/* 340 */       PRINTLN("");
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 345 */     if (this.validationSamples != null) {
/* 346 */       restoreBestModelOnValidation();
/*     */     }
/* 348 */     this.scoreOnTrainingData = SimpleMath.round(this.scorer.score(rank(this.samples)), 4);
/* 349 */     PRINTLN("--------------------------------------------------");
/* 350 */     PRINTLN("Finished sucessfully.");
/* 351 */     PRINTLN(this.scorer.name() + " on training data: " + this.scoreOnTrainingData);
/* 352 */     if (this.validationSamples != null) {
/*     */       
/* 354 */       this.bestScoreOnValidationData = this.scorer.score(rank(this.validationSamples));
/* 355 */       PRINTLN(this.scorer.name() + " on validation data: " + SimpleMath.round(this.bestScoreOnValidationData, 4));
/*     */     } 
/* 357 */     PRINTLN("---------------------------------");
/*     */   }
/*     */   
/*     */   public double eval(DataPoint p) {
/*     */     int k;
/* 362 */     for (k = 0; k < this.inputLayer.size() - 1; k++) {
/* 363 */       this.inputLayer.get(k).setOutput(p.getFeatureValue(this.features[k]));
/*     */     }
/* 365 */     this.inputLayer.get(this.inputLayer.size() - 1).setOutput(1.0D);
/*     */     
/* 367 */     for (k = 1; k < this.layers.size(); k++)
/* 368 */       ((Layer)this.layers.get(k)).computeOutput(); 
/* 369 */     return this.outputLayer.get(0).getOutput();
/*     */   }
/*     */   
/*     */   public Ranker createNew() {
/* 373 */     return new RankNet();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 377 */     String output = "";
/* 378 */     for (int i = 0; i < this.layers.size() - 1; i++) {
/*     */       
/* 380 */       for (int j = 0; j < ((Layer)this.layers.get(i)).size(); j++) {
/*     */         
/* 382 */         output = output + i + " " + j + " ";
/* 383 */         Neuron n = ((Layer)this.layers.get(i)).get(j);
/* 384 */         for (int k = 0; k < n.getOutLinks().size(); k++)
/* 385 */           output = output + ((Synapse)n.getOutLinks().get(k)).getWeight() + ((k == n.getOutLinks().size() - 1) ? "" : " "); 
/* 386 */         output = output + "\n";
/*     */       } 
/*     */     } 
/* 389 */     return output;
/*     */   }
/*     */   
/*     */   public String model() {
/* 393 */     String output = "## " + name() + "\n";
/* 394 */     output = output + "## Epochs = " + nIteration + "\n";
/* 395 */     output = output + "## No. of features = " + this.features.length + "\n";
/* 396 */     output = output + "## No. of hidden layers = " + (this.layers.size() - 2) + "\n"; int i;
/* 397 */     for (i = 1; i < this.layers.size() - 1; i++) {
/* 398 */       output = output + "## Layer " + i + ": " + ((Layer)this.layers.get(i)).size() + " neurons\n";
/*     */     }
/*     */     
/* 401 */     for (i = 0; i < this.features.length; i++)
/* 402 */       output = output + this.features[i] + ((i == this.features.length - 1) ? "" : " "); 
/* 403 */     output = output + "\n";
/*     */     
/* 405 */     output = output + (this.layers.size() - 2) + "\n";
/* 406 */     for (i = 1; i < this.layers.size() - 1; i++) {
/* 407 */       output = output + ((Layer)this.layers.get(i)).size() + "\n";
/*     */     }
/* 409 */     output = output + toString();
/* 410 */     return output;
/*     */   }
/*     */   
/*     */   public void loadFromString(String fullText) {
/*     */     try {
/* 415 */       String content = "";
/* 416 */       BufferedReader in = new BufferedReader(new StringReader(fullText));
/*     */       
/* 418 */       List<String> l = new ArrayList<>();
/* 419 */       while ((content = in.readLine()) != null) {
/*     */         
/* 421 */         content = content.trim();
/* 422 */         if (content.length() == 0)
/*     */           continue; 
/* 424 */         if (content.indexOf("##") == 0)
/*     */           continue; 
/* 426 */         l.add(content);
/*     */       } 
/* 428 */       in.close();
/*     */ 
/*     */       
/* 431 */       String[] tmp = ((String)l.get(0)).split(" ");
/* 432 */       this.features = new int[tmp.length];
/* 433 */       for (int i = 0; i < tmp.length; i++) {
/* 434 */         this.features[i] = Integer.parseInt(tmp[i]);
/*     */       }
/* 436 */       int nHiddenLayer = Integer.parseInt(l.get(1));
/* 437 */       int[] nn = new int[nHiddenLayer];
/*     */       
/* 439 */       int k = 2;
/* 440 */       for (; k < 2 + nHiddenLayer; k++) {
/* 441 */         nn[k - 2] = Integer.parseInt((String)l.get(k));
/*     */       }
/* 443 */       setInputOutput(this.features.length, 1);
/* 444 */       for (int j = 0; j < nHiddenLayer; j++)
/* 445 */         addHiddenLayer(nn[j]); 
/* 446 */       wire();
/*     */       
/* 448 */       for (; k < l.size(); k++) {
/*     */         
/* 450 */         String[] s = ((String)l.get(k)).split(" ");
/* 451 */         int iLayer = Integer.parseInt(s[0]);
/* 452 */         int iNeuron = Integer.parseInt(s[1]);
/* 453 */         Neuron n = ((Layer)this.layers.get(iLayer)).get(iNeuron);
/* 454 */         for (int m = 0; m < n.getOutLinks().size(); m++) {
/* 455 */           ((Synapse)n.getOutLinks().get(m)).setWeight(Double.parseDouble(s[m + 2]));
/*     */         }
/*     */       } 
/* 458 */     } catch (Exception ex) {
/*     */       
/* 460 */       throw RankLibError.create("Error in RankNet::load(): ", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void printParameters() {
/* 465 */     PRINTLN("No. of epochs: " + nIteration);
/* 466 */     PRINTLN("No. of hidden layers: " + nHiddenLayer);
/* 467 */     PRINTLN("No. of hidden nodes per layer: " + nHiddenNodePerLayer);
/* 468 */     PRINTLN("Learning rate: " + learningRate);
/*     */   }
/*     */   
/*     */   public String name() {
/* 472 */     return "RankNet";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void printNetworkConfig() {
/* 479 */     for (int i = 1; i < this.layers.size(); i++) {
/*     */       
/* 481 */       System.out.println("Layer-" + (i + 1));
/* 482 */       for (int j = 0; j < ((Layer)this.layers.get(i)).size(); j++) {
/*     */         
/* 484 */         Neuron n = ((Layer)this.layers.get(i)).get(j);
/* 485 */         System.out.print("Neuron-" + (j + 1) + ": " + n.getInLinks().size() + " inputs\t");
/* 486 */         for (int k = 0; k < n.getInLinks().size(); k++)
/* 487 */           System.out.print(((Synapse)n.getInLinks().get(k)).getWeight() + "\t"); 
/* 488 */         System.out.println("");
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
/*     */   protected void printWeightVector() {
/* 500 */     for (int j = 0; j < this.outputLayer.get(0).getInLinks().size(); j++)
/* 501 */       System.out.print(((Synapse)this.outputLayer.get(0).getInLinks().get(j)).getWeight() + " "); 
/* 502 */     System.out.println("");
/*     */   }
/*     */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\learning\neuralnet\RankNet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */