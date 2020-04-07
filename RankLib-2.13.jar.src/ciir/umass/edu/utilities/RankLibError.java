/*    */ package ciir.umass.edu.utilities;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RankLibError
/*    */   extends RuntimeException
/*    */ {
/*    */   private RankLibError(Exception e) {
/*  9 */     super(e);
/*    */   } private RankLibError(String message) {
/* 11 */     super(message);
/*    */   }
/*    */   private RankLibError(String message, Exception cause) {
/* 14 */     super(message, cause);
/*    */   }
/*    */ 
/*    */   
/*    */   public static RankLibError create(Exception e) {
/* 19 */     if (e instanceof RankLibError) {
/* 20 */       return (RankLibError)e;
/*    */     }
/* 22 */     return new RankLibError(e);
/*    */   }
/*    */   
/*    */   public static RankLibError create(String message) {
/* 26 */     return new RankLibError(message);
/*    */   }
/*    */ 
/*    */   
/*    */   public static RankLibError create(String message, Exception cause) {
/* 31 */     if (cause instanceof RankLibError) {
/* 32 */       return (RankLibError)cause;
/*    */     }
/* 34 */     return new RankLibError(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\ed\\utilities\RankLibError.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */