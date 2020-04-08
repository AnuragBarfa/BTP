/*    */ package ciir.umass.edu.learning.neuralnet;
/*    */ 
/*    */ import java.util.Random;
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
/*    */ 
/*    */ 
/*    */ public class Synapse
/*    */ {
/* 18 */   static Random random = new Random();
/* 19 */   protected double weight = 0.0D;
/* 20 */   protected double dW = 0.0D;
/* 21 */   protected Neuron source = null;
/* 22 */   protected Neuron target = null;
/*    */ 
/*    */   
/*    */   public Synapse(Neuron source, Neuron target) {
/* 26 */     this.source = source;
/* 27 */     this.target = target;
/* 28 */     this.source.getOutLinks().add(this);
/* 29 */     this.target.getInLinks().add(this);
/*    */     
/* 31 */     this.weight = (((random.nextInt(2) == 0) ? true : -1) * random.nextFloat() / 10.0F);
/*    */   }
/*    */   
/*    */   public Neuron getSource() {
/* 35 */     return this.source;
/*    */   }
/*    */   
/*    */   public Neuron getTarget() {
/* 39 */     return this.target;
/*    */   }
/*    */   
/*    */   public void setWeight(double w) {
/* 43 */     this.weight = w;
/*    */   }
/*    */   
/*    */   public double getWeight() {
/* 47 */     return this.weight;
/*    */   }
/*    */   
/*    */   public double getLastWeightAdjustment() {
/* 51 */     return this.dW;
/*    */   }
/*    */   
/*    */   public void setWeightAdjustment(double dW) {
/* 55 */     this.dW = dW;
/*    */   }
/*    */   
/*    */   public void updateWeight() {
/* 59 */     this.weight += this.dW;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\learning\neuralnet\Synapse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */