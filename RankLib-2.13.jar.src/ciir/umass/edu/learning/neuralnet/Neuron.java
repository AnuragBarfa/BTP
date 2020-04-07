/*     */ package ciir.umass.edu.learning.neuralnet;
/*     */ 
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
/*     */ public class Neuron
/*     */ {
/*  21 */   public static double momentum = 0.9D;
/*  22 */   public static double learningRate = 0.001D;
/*     */ 
/*     */   
/*  25 */   protected TransferFunction tfunc = new LogiFunction();
/*     */   
/*     */   protected double output;
/*  28 */   protected List<Double> outputs = null;
/*  29 */   protected double delta_i = 0.0D;
/*  30 */   protected double[] deltas_j = null;
/*     */   
/*  32 */   protected List<Synapse> inLinks = null;
/*  33 */   protected List<Synapse> outLinks = null;
/*     */ 
/*     */   
/*     */   public Neuron() {
/*  37 */     this.output = 0.0D;
/*  38 */     this.inLinks = new ArrayList<>();
/*  39 */     this.outLinks = new ArrayList<>();
/*     */     
/*  41 */     this.outputs = new ArrayList<>();
/*  42 */     this.delta_i = 0.0D;
/*     */   }
/*     */   
/*     */   public double getOutput() {
/*  46 */     return this.output;
/*     */   }
/*     */   
/*     */   public double getOutput(int k) {
/*  50 */     return ((Double)this.outputs.get(k)).doubleValue();
/*     */   }
/*     */   
/*     */   public List<Synapse> getInLinks() {
/*  54 */     return this.inLinks;
/*     */   }
/*     */   
/*     */   public List<Synapse> getOutLinks() {
/*  58 */     return this.outLinks;
/*     */   }
/*     */   
/*     */   public void setOutput(double output) {
/*  62 */     this.output = output;
/*     */   }
/*     */   
/*     */   public void addOutput(double output) {
/*  66 */     this.outputs.add(Double.valueOf(output));
/*     */   }
/*     */   
/*     */   public void computeOutput() {
/*  70 */     Synapse s = null;
/*  71 */     double wsum = 0.0D;
/*  72 */     for (int j = 0; j < this.inLinks.size(); j++) {
/*     */       
/*  74 */       s = this.inLinks.get(j);
/*  75 */       wsum += s.getSource().getOutput() * s.getWeight();
/*     */     } 
/*  77 */     this.output = this.tfunc.compute(wsum);
/*     */   }
/*     */ 
/*     */   
/*     */   public void computeOutput(int i) {
/*  82 */     Synapse s = null;
/*  83 */     double wsum = 0.0D;
/*  84 */     for (int j = 0; j < this.inLinks.size(); j++) {
/*     */       
/*  86 */       s = this.inLinks.get(j);
/*  87 */       wsum += s.getSource().getOutput(i) * s.getWeight();
/*     */     } 
/*  89 */     this.output = this.tfunc.compute(wsum);
/*  90 */     this.outputs.add(Double.valueOf(this.output));
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearOutputs() {
/*  95 */     this.outputs.clear();
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
/*     */   public void computeDelta(PropParameter param) {
/* 107 */     int[][] pairMap = param.pairMap;
/* 108 */     int current = param.current;
/*     */     
/* 110 */     this.delta_i = 0.0D;
/* 111 */     this.deltas_j = new double[(pairMap[current]).length];
/* 112 */     for (int k = 0; k < (pairMap[current]).length; k++) {
/*     */       
/* 114 */       int j = pairMap[current][k];
/* 115 */       float weight = 1.0F;
/* 116 */       double pij = 0.0D;
/* 117 */       if (param.pairWeight == null) {
/*     */         
/* 119 */         weight = 1.0F;
/* 120 */         pij = 1.0D / (1.0D + Math.exp(((Double)this.outputs.get(current)).doubleValue() - ((Double)this.outputs.get(j)).doubleValue()));
/*     */       }
/*     */       else {
/*     */         
/* 124 */         weight = param.pairWeight[current][k];
/* 125 */         pij = param.targetValue[current][k] - 1.0D / (1.0D + Math.exp(-(((Double)this.outputs.get(current)).doubleValue() - ((Double)this.outputs.get(j)).doubleValue())));
/*     */       } 
/* 127 */       double lambda = weight * pij;
/* 128 */       this.delta_i += lambda;
/* 129 */       this.deltas_j[k] = lambda * this.tfunc.computeDerivative(((Double)this.outputs.get(j)).doubleValue());
/*     */     } 
/* 131 */     this.delta_i *= this.tfunc.computeDerivative(((Double)this.outputs.get(current)).doubleValue());
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
/*     */   public void updateDelta(PropParameter param) {
/* 149 */     int[][] pairMap = param.pairMap;
/* 150 */     float[][] pairWeight = param.pairWeight;
/* 151 */     int current = param.current;
/* 152 */     this.delta_i = 0.0D;
/* 153 */     this.deltas_j = new double[(pairMap[current]).length];
/* 154 */     for (int k = 0; k < (pairMap[current]).length; k++) {
/*     */       
/* 156 */       int j = pairMap[current][k];
/* 157 */       float weight = (pairWeight != null) ? pairWeight[current][k] : 1.0F;
/* 158 */       double errorSum = 0.0D;
/* 159 */       for (int l = 0; l < this.outLinks.size(); l++) {
/*     */         
/* 161 */         Synapse s = this.outLinks.get(l);
/* 162 */         errorSum += (s.getTarget()).deltas_j[k] * s.weight;
/* 163 */         if (k == 0)
/* 164 */           this.delta_i += (s.getTarget()).delta_i * s.weight; 
/*     */       } 
/* 166 */       if (k == 0)
/* 167 */         this.delta_i *= weight * this.tfunc.computeDerivative(((Double)this.outputs.get(current)).doubleValue()); 
/* 168 */       this.deltas_j[k] = errorSum * weight * this.tfunc.computeDerivative(((Double)this.outputs.get(j)).doubleValue());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateWeight(PropParameter param) {
/* 177 */     Synapse s = null;
/* 178 */     for (int k = 0; k < this.inLinks.size(); k++) {
/*     */       
/* 180 */       s = this.inLinks.get(k);
/* 181 */       double sum_j = 0.0D;
/* 182 */       for (int l = 0; l < this.deltas_j.length; l++)
/* 183 */         sum_j += this.deltas_j[l] * s.getSource().getOutput(param.pairMap[param.current][l]); 
/* 184 */       double dw = learningRate * (this.delta_i * s.getSource().getOutput(param.current) - sum_j);
/* 185 */       s.setWeightAdjustment(dw);
/* 186 */       s.updateWeight();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\learning\neuralnet\Neuron.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */