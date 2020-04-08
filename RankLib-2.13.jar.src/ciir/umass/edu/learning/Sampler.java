/*    */ package ciir.umass.edu.learning;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Sampler
/*    */ {
/* 18 */   protected List<RankList> samples = null;
/* 19 */   protected List<RankList> remains = null;
/*    */   
/*    */   public List<RankList> doSampling(List<RankList> samplingPool, float samplingRate, boolean withReplacement) {
/* 22 */     Random r = new Random();
/* 23 */     this.samples = new ArrayList<>();
/* 24 */     int size = (int)(samplingRate * samplingPool.size());
/* 25 */     if (withReplacement) {
/*    */       
/* 27 */       int[] used = new int[samplingPool.size()];
/* 28 */       Arrays.fill(used, 0); int i;
/* 29 */       for (i = 0; i < size; i++) {
/*    */         
/* 31 */         int selected = r.nextInt(samplingPool.size());
/* 32 */         this.samples.add(samplingPool.get(selected));
/* 33 */         used[selected] = 1;
/*    */       } 
/* 35 */       this.remains = new ArrayList<>();
/* 36 */       for (i = 0; i < samplingPool.size(); i++) {
/* 37 */         if (used[i] == 0) {
/* 38 */           this.remains.add(samplingPool.get(i));
/*    */         }
/*    */       } 
/*    */     } else {
/* 42 */       List<Integer> l = new ArrayList<>(); int i;
/* 43 */       for (i = 0; i < samplingPool.size(); i++)
/* 44 */         l.add(Integer.valueOf(i)); 
/* 45 */       for (i = 0; i < size; i++) {
/*    */         
/* 47 */         int selected = r.nextInt(l.size());
/* 48 */         this.samples.add(samplingPool.get(((Integer)l.get(selected)).intValue()));
/* 49 */         l.remove(selected);
/*    */       } 
/* 51 */       this.remains = new ArrayList<>();
/* 52 */       for (i = 0; i < l.size(); i++)
/* 53 */         this.remains.add(samplingPool.get(((Integer)l.get(i)).intValue())); 
/*    */     } 
/* 55 */     return this.samples;
/*    */   }
/*    */   
/*    */   public List<RankList> getSamples() {
/* 59 */     return this.samples;
/*    */   }
/*    */   
/*    */   public List<RankList> getRemains() {
/* 63 */     return this.remains;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\learning\Sampler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */