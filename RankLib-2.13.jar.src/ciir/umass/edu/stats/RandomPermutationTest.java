/*    */ package ciir.umass.edu.stats;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Random;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RandomPermutationTest
/*    */   extends SignificanceTest
/*    */ {
/* 13 */   public static int nPermutation = 10000;
/* 14 */   private static String[] pad = new String[] { "", "0", "00", "000", "0000", "00000", "000000", "0000000", "00000000", "000000000" };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public double test(HashMap<String, Double> target, HashMap<String, Double> baseline) {
/* 24 */     double[] b = new double[baseline.keySet().size()];
/* 25 */     double[] t = new double[target.keySet().size()];
/* 26 */     int c = 0;
/* 27 */     for (String key : baseline.keySet()) {
/*    */       
/* 29 */       b[c] = ((Double)baseline.get(key)).doubleValue();
/* 30 */       t[c] = ((Double)target.get(key)).doubleValue();
/* 31 */       c++;
/*    */     } 
/* 33 */     double trueDiff = Math.abs(BasicStats.mean(b) - BasicStats.mean(t));
/* 34 */     double pvalue = 0.0D;
/* 35 */     double[] pb = new double[baseline.keySet().size()];
/* 36 */     double[] pt = new double[target.keySet().size()];
/* 37 */     for (int i = 0; i < nPermutation; i++) {
/*    */       
/* 39 */       char[] bits = randomBitVector(b.length).toCharArray();
/* 40 */       for (int j = 0; j < b.length; j++) {
/*    */         
/* 42 */         if (bits[j] == '0') {
/*    */           
/* 44 */           pb[j] = b[j];
/* 45 */           pt[j] = t[j];
/*    */         }
/*    */         else {
/*    */           
/* 49 */           pb[j] = t[j];
/* 50 */           pt[j] = b[j];
/*    */         } 
/*    */       } 
/* 53 */       double pDiff = Math.abs(BasicStats.mean(pb) - BasicStats.mean(pt));
/* 54 */       if (pDiff >= trueDiff)
/* 55 */         pvalue++; 
/*    */     } 
/* 57 */     return pvalue / nPermutation;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private String randomBitVector(int size) {
/* 67 */     Random r = new Random();
/* 68 */     String output = "";
/* 69 */     for (int i = 0; i < size / 10 + 1; i++) {
/*    */       
/* 71 */       int x = (int)(1024.0D * r.nextDouble());
/* 72 */       String s = Integer.toBinaryString(x);
/* 73 */       if (s.length() == 11) {
/* 74 */         output = output + s.substring(1);
/*    */       } else {
/* 76 */         output = output + pad[10 - s.length()] + s;
/*    */       } 
/* 78 */     }  return output;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\stats\RandomPermutationTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */