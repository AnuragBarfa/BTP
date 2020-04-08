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
/*    */ 
/*    */ public class LogiFunction
/*    */   implements TransferFunction
/*    */ {
/*    */   public double compute(double x) {
/* 20 */     return 1.0D / (1.0D + Math.exp(-x));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public double computeDerivative(double x) {
/* 26 */     double output = compute(x);
/* 27 */     return output * (1.0D - output);
/*    */   }
/*    */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\learning\neuralnet\LogiFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */