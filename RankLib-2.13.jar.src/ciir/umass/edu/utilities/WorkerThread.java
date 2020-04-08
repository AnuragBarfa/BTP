/*   */ package ciir.umass.edu.utilities;
/*   */ 
/*   */ public abstract class WorkerThread implements Runnable {
/* 4 */   protected int start = -1;
/* 5 */   protected int end = -1;
/*   */   
/*   */   public void set(int start, int end) {
/* 8 */     this.start = start;
/* 9 */     this.end = end;
/*   */   }
/*   */   
/*   */   public abstract WorkerThread clone();
/*   */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\ed\\utilities\WorkerThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */