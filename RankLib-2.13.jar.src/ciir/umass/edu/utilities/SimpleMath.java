/*    */ package ciir.umass.edu.utilities;
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
/*    */ public class SimpleMath
/*    */ {
/* 17 */   private static double LOG2 = Math.log(2.0D);
/* 18 */   private static double LOG10 = Math.log(10.0D);
/* 19 */   private static double LOGE = Math.log(Math.E);
/*    */ 
/*    */   
/*    */   public static double logBase2(double value) {
/* 23 */     return Math.log(value) / LOG2;
/*    */   }
/*    */   
/*    */   public static double logBase10(double value) {
/* 27 */     return Math.log(value) / LOG10;
/*    */   }
/*    */   
/*    */   public static double ln(double value) {
/* 31 */     return Math.log(value) / LOGE;
/*    */   }
/*    */   
/*    */   public static int min(int a, int b) {
/* 35 */     return (a > b) ? b : a;
/*    */   }
/*    */   
/*    */   public static double p(long count, long total) {
/* 39 */     return (count + 0.5D) / (total + 1L);
/*    */   }
/*    */   
/*    */   public static double round(double val) {
/* 43 */     int precision = 10000;
/* 44 */     return Math.floor(val * precision + 0.5D) / precision;
/*    */   }
/*    */   
/*    */   public static double round(float val) {
/* 48 */     int precision = 10000;
/* 49 */     return Math.floor((val * precision) + 0.5D) / precision;
/*    */   }
/*    */   
/*    */   public static double round(double val, int n) {
/* 53 */     int precision = 1;
/* 54 */     for (int i = 0; i < n; i++)
/* 55 */       precision *= 10; 
/* 56 */     return Math.floor(val * precision + 0.5D) / precision;
/*    */   }
/*    */   
/*    */   public static float round(float val, int n) {
/* 60 */     int precision = 1;
/* 61 */     for (int i = 0; i < n; i++)
/* 62 */       precision *= 10; 
/* 63 */     return (float)(Math.floor((val * precision) + 0.5D) / precision);
/*    */   }
/*    */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\ed\\utilities\SimpleMath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */