/*    */ package ciir.umass.edu.learning.neuralnet;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ 
/*    */ 
/*    */ public class Layer
/*    */ {
/* 21 */   protected List<Neuron> neurons = null;
/*    */ 
/*    */   
/*    */   public Layer(int size) {
/* 25 */     this.neurons = new ArrayList<>();
/* 26 */     for (int i = 0; i < size; i++) {
/* 27 */       this.neurons.add(new Neuron());
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Layer(int size, int nType) {
/* 36 */     this.neurons = new ArrayList<>();
/* 37 */     for (int i = 0; i < size; i++) {
/* 38 */       if (nType == 0) {
/* 39 */         this.neurons.add(new Neuron());
/*    */       } else {
/* 41 */         this.neurons.add(new ListNeuron());
/*    */       } 
/*    */     } 
/*    */   } public Neuron get(int k) {
/* 45 */     return this.neurons.get(k);
/*    */   }
/*    */   
/*    */   public int size() {
/* 49 */     return this.neurons.size();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void computeOutput(int i) {
/* 57 */     for (int j = 0; j < this.neurons.size(); j++)
/* 58 */       ((Neuron)this.neurons.get(j)).computeOutput(i); 
/*    */   }
/*    */   
/*    */   public void computeOutput() {
/* 62 */     for (int j = 0; j < this.neurons.size(); j++)
/* 63 */       ((Neuron)this.neurons.get(j)).computeOutput(); 
/*    */   }
/*    */   
/*    */   public void clearOutputs() {
/* 67 */     for (int i = 0; i < this.neurons.size(); i++) {
/* 68 */       ((Neuron)this.neurons.get(i)).clearOutputs();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void computeDelta(PropParameter param) {
/* 76 */     for (int i = 0; i < this.neurons.size(); i++) {
/* 77 */       ((Neuron)this.neurons.get(i)).computeDelta(param);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void updateDelta(PropParameter param) {
/* 84 */     for (int i = 0; i < this.neurons.size(); i++)
/* 85 */       ((Neuron)this.neurons.get(i)).updateDelta(param); 
/*    */   }
/*    */   
/*    */   public void updateWeight(PropParameter param) {
/* 89 */     for (int i = 0; i < this.neurons.size(); i++)
/* 90 */       ((Neuron)this.neurons.get(i)).updateWeight(param); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\learning\neuralnet\Layer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */