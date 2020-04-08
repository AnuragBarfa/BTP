/*    */ package ciir.umass.edu.stats;
/*    */ 
/*    */ public class BasicStats
/*    */ {
/*    */   public static double mean(double[] values) {
/*  6 */     double mean = 0.0D;
/*  7 */     if (values.length == 0) {
/*    */       
/*  9 */       System.out.println("Error in BasicStats::mean(): Empty input array.");
/* 10 */       System.exit(1);
/*    */     } 
/* 12 */     for (int i = 0; i < values.length; i++)
/* 13 */       mean += values[i]; 
/* 14 */     return mean / values.length;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\stats\BasicStats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */