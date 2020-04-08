/*    */ package ciir.umass.edu.learning.neuralnet;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ListNeuron
/*    */   extends Neuron
/*    */ {
/*    */   protected double[] d1;
/*    */   protected double[] d2;
/*    */   
/*    */   public void computeDelta(PropParameter param) {
/* 19 */     double sumLabelExp = 0.0D;
/* 20 */     double sumScoreExp = 0.0D; int i;
/* 21 */     for (i = 0; i < this.outputs.size(); i++) {
/*    */       
/* 23 */       sumLabelExp += Math.exp(param.labels[i]);
/* 24 */       sumScoreExp += Math.exp(((Double)this.outputs.get(i)).doubleValue());
/*    */     } 
/*    */     
/* 27 */     this.d1 = new double[this.outputs.size()];
/* 28 */     this.d2 = new double[this.outputs.size()];
/* 29 */     for (i = 0; i < this.outputs.size(); i++) {
/*    */       
/* 31 */       this.d1[i] = Math.exp(param.labels[i]) / sumLabelExp;
/* 32 */       this.d2[i] = Math.exp(((Double)this.outputs.get(i)).doubleValue()) / sumScoreExp;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void updateWeight(PropParameter param) {
/* 37 */     Synapse s = null;
/* 38 */     for (int k = 0; k < this.inLinks.size(); k++) {
/*    */       
/* 40 */       s = this.inLinks.get(k);
/* 41 */       double dw = 0.0D;
/* 42 */       for (int l = 0; l < this.d1.length; l++) {
/* 43 */         dw += (this.d1[l] - this.d2[l]) * s.getSource().getOutput(l);
/*    */       }
/* 45 */       dw *= learningRate;
/* 46 */       s.setWeightAdjustment(dw);
/* 47 */       s.updateWeight();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\learning\neuralnet\ListNeuron.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */