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
/*     */ public class ListNet
/*     */   extends RankNet
/*     */ {
/*  27 */   public static int nIteration = 1500;
/*  28 */   public static double learningRate = 1.0E-5D;
/*  29 */   public static int nHiddenLayer = 0;
/*     */ 
/*     */   
/*     */   public ListNet() {}
/*     */ 
/*     */   
/*     */   public ListNet(List<RankList> samples, int[] features, MetricScorer scorer) {
/*  36 */     super(samples, features, scorer);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float[] feedForward(RankList rl) {
/*  41 */     float[] labels = new float[rl.size()];
/*  42 */     for (int i = 0; i < rl.size(); i++) {
/*     */       
/*  44 */       addInput(rl.get(i));
/*  45 */       propagate(i);
/*  46 */       labels[i] = rl.get(i).getLabel();
/*     */     } 
/*  48 */     return labels;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void backPropagate(float[] labels) {
/*  53 */     PropParameter p = new PropParameter(labels);
/*  54 */     this.outputLayer.computeDelta(p);
/*     */ 
/*     */     
/*  57 */     this.outputLayer.updateWeight(p);
/*     */   }
/*     */   
/*     */   protected void estimateLoss() {
/*  61 */     this.error = 0.0D;
/*  62 */     double sumLabelExp = 0.0D;
/*  63 */     double sumScoreExp = 0.0D;
/*  64 */     for (int i = 0; i < this.samples.size(); i++) {
/*     */       
/*  66 */       RankList rl = this.samples.get(i);
/*  67 */       double[] scores = new double[rl.size()];
/*  68 */       double err = 0.0D; int j;
/*  69 */       for (j = 0; j < rl.size(); j++) {
/*     */         
/*  71 */         scores[j] = eval(rl.get(j));
/*  72 */         sumLabelExp += Math.exp(rl.get(j).getLabel());
/*  73 */         sumScoreExp += Math.exp(scores[j]);
/*     */       } 
/*  75 */       for (j = 0; j < rl.size(); j++) {
/*     */         
/*  77 */         double p1 = Math.exp(rl.get(j).getLabel()) / sumLabelExp;
/*  78 */         double p2 = Math.exp(scores[j]) / sumScoreExp;
/*  79 */         err += -p1 * SimpleMath.logBase2(p2);
/*     */       } 
/*  81 */       this.error += err / rl.size();
/*     */     } 
/*     */ 
/*     */     
/*  85 */     this.lastError = this.error;
/*     */   }
/*     */ 
/*     */   
/*     */   public void init() {
/*  90 */     PRINT("Initializing... ");
/*     */ 
/*     */     
/*  93 */     setInputOutput(this.features.length, 1, 1);
/*  94 */     wire();
/*     */     
/*  96 */     if (this.validationSamples != null)
/*  97 */       for (int i = 0; i < this.layers.size(); i++) {
/*  98 */         this.bestModelOnValidation.add(new ArrayList<>());
/*     */       } 
/* 100 */     Neuron.learningRate = learningRate;
/* 101 */     PRINTLN("[Done]");
/*     */   }
/*     */   
/*     */   public void learn() {
/* 105 */     PRINTLN("-----------------------------------------");
/* 106 */     PRINTLN("Training starts...");
/* 107 */     PRINTLN("--------------------------------------------------");
/* 108 */     PRINTLN(new int[] { 7, 14, 9, 9 }, new String[] { "#epoch", "C.E. Loss", this.scorer.name() + "-T", this.scorer.name() + "-V" });
/* 109 */     PRINTLN("--------------------------------------------------");
/*     */     
/* 111 */     for (int i = 1; i <= nIteration; i++) {
/*     */       
/* 113 */       for (int j = 0; j < this.samples.size(); j++) {
/*     */         
/* 115 */         float[] labels = feedForward(this.samples.get(j));
/* 116 */         backPropagate(labels);
/* 117 */         clearNeuronOutputs();
/*     */       } 
/*     */       
/* 120 */       PRINT(new int[] { 7, 14 }, new String[] { i + "", SimpleMath.round(this.error, 6) + "" });
/* 121 */       if (i % 1 == 0) {
/*     */         
/* 123 */         this.scoreOnTrainingData = this.scorer.score(rank(this.samples));
/* 124 */         PRINT(new int[] { 9 }, new String[] { SimpleMath.round(this.scoreOnTrainingData, 4) + "" });
/* 125 */         if (this.validationSamples != null) {
/*     */           
/* 127 */           double score = this.scorer.score(rank(this.validationSamples));
/* 128 */           if (score > this.bestScoreOnValidationData) {
/*     */             
/* 130 */             this.bestScoreOnValidationData = score;
/* 131 */             saveBestModelOnValidation();
/*     */           } 
/* 133 */           PRINT(new int[] { 9 }, new String[] { SimpleMath.round(score, 4) + "" });
/*     */         } 
/*     */       } 
/* 136 */       PRINTLN("");
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 141 */     if (this.validationSamples != null) {
/* 142 */       restoreBestModelOnValidation();
/*     */     }
/* 144 */     this.scoreOnTrainingData = SimpleMath.round(this.scorer.score(rank(this.samples)), 4);
/* 145 */     PRINTLN("--------------------------------------------------");
/* 146 */     PRINTLN("Finished sucessfully.");
/* 147 */     PRINTLN(this.scorer.name() + " on training data: " + this.scoreOnTrainingData);
/* 148 */     if (this.validationSamples != null) {
/*     */       
/* 150 */       this.bestScoreOnValidationData = this.scorer.score(rank(this.validationSamples));
/* 151 */       PRINTLN(this.scorer.name() + " on validation data: " + SimpleMath.round(this.bestScoreOnValidationData, 4));
/*     */     } 
/* 153 */     PRINTLN("---------------------------------");
/*     */   }
/*     */   
/*     */   public double eval(DataPoint p) {
/* 157 */     return super.eval(p);
/*     */   }
/*     */   
/*     */   public Ranker createNew() {
/* 161 */     return new ListNet();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 165 */     return super.toString();
/*     */   }
/*     */   
/*     */   public String model() {
/* 169 */     String output = "## " + name() + "\n";
/* 170 */     output = output + "## Epochs = " + nIteration + "\n";
/* 171 */     output = output + "## No. of features = " + this.features.length + "\n";
/*     */ 
/*     */     
/* 174 */     for (int i = 0; i < this.features.length; i++)
/* 175 */       output = output + this.features[i] + ((i == this.features.length - 1) ? "" : " "); 
/* 176 */     output = output + "\n";
/*     */     
/* 178 */     output = output + "0\n";
/*     */     
/* 180 */     output = output + toString();
/* 181 */     return output;
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadFromString(String fullText) {
/*     */     try {
/* 187 */       String content = "";
/* 188 */       BufferedReader in = new BufferedReader(new StringReader(fullText));
/*     */       
/* 190 */       List<String> l = new ArrayList<>();
/* 191 */       while ((content = in.readLine()) != null) {
/*     */         
/* 193 */         content = content.trim();
/* 194 */         if (content.length() == 0)
/*     */           continue; 
/* 196 */         if (content.indexOf("##") == 0)
/*     */           continue; 
/* 198 */         l.add(content);
/*     */       } 
/* 200 */       in.close();
/*     */ 
/*     */       
/* 203 */       String[] tmp = ((String)l.get(0)).split(" ");
/* 204 */       this.features = new int[tmp.length];
/* 205 */       for (int i = 0; i < tmp.length; i++) {
/* 206 */         this.features[i] = Integer.parseInt(tmp[i]);
/*     */       }
/* 208 */       int nHiddenLayer = Integer.parseInt(l.get(1));
/* 209 */       int[] nn = new int[nHiddenLayer];
/*     */       
/* 211 */       int k = 2;
/* 212 */       for (; k < 2 + nHiddenLayer; k++) {
/* 213 */         nn[k - 2] = Integer.parseInt((String)l.get(k));
/*     */       }
/* 215 */       setInputOutput(this.features.length, 1);
/* 216 */       for (int j = 0; j < nHiddenLayer; j++)
/* 217 */         addHiddenLayer(nn[j]); 
/* 218 */       wire();
/*     */       
/* 220 */       for (; k < l.size(); k++) {
/*     */         
/* 222 */         String[] s = ((String)l.get(k)).split(" ");
/* 223 */         int iLayer = Integer.parseInt(s[0]);
/* 224 */         int iNeuron = Integer.parseInt(s[1]);
/* 225 */         Neuron n = ((Layer)this.layers.get(iLayer)).get(iNeuron);
/* 226 */         for (int m = 0; m < n.getOutLinks().size(); m++) {
/* 227 */           ((Synapse)n.getOutLinks().get(m)).setWeight(Double.parseDouble(s[m + 2]));
/*     */         }
/*     */       } 
/* 230 */     } catch (Exception ex) {
/*     */       
/* 232 */       throw RankLibError.create("Error in ListNet::load(): ", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void printParameters() {
/* 237 */     PRINTLN("No. of epochs: " + nIteration);
/* 238 */     PRINTLN("Learning rate: " + learningRate);
/*     */   }
/*     */   
/*     */   public String name() {
/* 242 */     return "ListNet";
/*     */   }
/*     */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\learning\neuralnet\ListNet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */