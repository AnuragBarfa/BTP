/*    */ package ciir.umass.edu.learning;
/*    */ 
/*    */ import ciir.umass.edu.utilities.Sorter;
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
/*    */ 
/*    */ 
/*    */ public class RankList
/*    */ {
/* 23 */   protected DataPoint[] rl = null;
/*    */ 
/*    */   
/*    */   public RankList(List<DataPoint> rl) {
/* 27 */     this.rl = new DataPoint[rl.size()];
/* 28 */     for (int i = 0; i < rl.size(); i++)
/* 29 */       this.rl[i] = rl.get(i); 
/*    */   }
/*    */   
/*    */   public RankList(RankList rl) {
/* 33 */     this.rl = new DataPoint[rl.size()];
/* 34 */     for (int i = 0; i < rl.size(); i++)
/* 35 */       this.rl[i] = rl.get(i); 
/*    */   }
/*    */   
/*    */   public RankList(RankList rl, int[] idx) {
/* 39 */     this.rl = new DataPoint[rl.size()];
/* 40 */     for (int i = 0; i < idx.length; i++)
/* 41 */       this.rl[i] = rl.get(idx[i]); 
/*    */   }
/*    */   
/*    */   public RankList(RankList rl, int[] idx, int offset) {
/* 45 */     this.rl = new DataPoint[rl.size()];
/* 46 */     for (int i = 0; i < idx.length; i++)
/* 47 */       this.rl[i] = rl.get(idx[i] - offset); 
/*    */   }
/*    */   
/*    */   public String getID() {
/* 51 */     return get(0).getID();
/*    */   }
/*    */   
/*    */   public int size() {
/* 55 */     return this.rl.length;
/*    */   }
/*    */   
/*    */   public DataPoint get(int k) {
/* 59 */     return this.rl[k];
/*    */   }
/*    */   
/*    */   public void set(int k, DataPoint p) {
/* 63 */     this.rl[k] = p;
/*    */   }
/*    */   
/*    */   public RankList getCorrectRanking() {
/* 67 */     double[] score = new double[this.rl.length];
/* 68 */     for (int i = 0; i < this.rl.length; i++)
/* 69 */       score[i] = this.rl[i].getLabel(); 
/* 70 */     int[] idx = Sorter.sort(score, false);
/* 71 */     return new RankList(this, idx);
/*    */   }
/*    */ 
/*    */   
/*    */   public RankList getRanking(short fid) {
/* 76 */     double[] score = new double[this.rl.length];
/* 77 */     for (int i = 0; i < this.rl.length; i++)
/* 78 */       score[i] = this.rl[i].getFeatureValue(fid); 
/* 79 */     int[] idx = Sorter.sort(score, false);
/* 80 */     return new RankList(this, idx);
/*    */   }
/*    */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\learning\RankList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */