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
/*    */ public class PropParameter
/*    */ {
/* 14 */   public int current = -1;
/* 15 */   public int[][] pairMap = (int[][])null;
/*    */   
/*    */   public float[][] pairWeight;
/*    */   public float[][] targetValue;
/*    */   public float[] labels;
/*    */   
/*    */   public PropParameter(int current, int[][] pairMap) {
/* 22 */     this.pairWeight = (float[][])null;
/* 23 */     this.targetValue = (float[][])null;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 32 */     this.labels = null; this.current = current; this.pairMap = pairMap; } public PropParameter(int current, int[][] pairMap, float[][] pairWeight, float[][] targetValue) { this.pairWeight = (float[][])null; this.targetValue = (float[][])null; this.labels = null; this.current = current; this.pairMap = pairMap; this.pairWeight = pairWeight; this.targetValue = targetValue; } public PropParameter(float[] labels) { this.pairWeight = (float[][])null; this.targetValue = (float[][])null; this.labels = null;
/*    */ 
/*    */     
/* 35 */     this.labels = labels; }
/*    */ 
/*    */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\learning\neuralnet\PropParameter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */