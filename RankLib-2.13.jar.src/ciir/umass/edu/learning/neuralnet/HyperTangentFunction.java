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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HyperTangentFunction
/*    */   implements TransferFunction
/*    */ {
/*    */   public double compute(double x) {
/* 19 */     return 1.7159D * Math.tanh(x * 2.0D / 3.0D);
/*    */   }
/*    */ 
/*    */   
/*    */   public double computeDerivative(double x) {
/* 24 */     double output = Math.tanh(x * 2.0D / 3.0D);
/* 25 */     return 1.7159D * (1.0D - output * output) * 2.0D / 3.0D;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\learning\neuralnet\HyperTangentFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */