/*    */ package ciir.umass.edu.learning;
/*    */ 
/*    */ import ciir.umass.edu.utilities.RankLibError;
/*    */ 
/*    */ public class DenseDataPoint
/*    */   extends DataPoint {
/*    */   public DenseDataPoint(String text) {
/*  8 */     super(text);
/*    */   }
/*    */ 
/*    */   
/*    */   public DenseDataPoint(DenseDataPoint dp) {
/* 13 */     this.label = dp.label;
/* 14 */     this.id = dp.id;
/* 15 */     this.description = dp.description;
/* 16 */     this.cached = dp.cached;
/* 17 */     this.fVals = new float[dp.fVals.length];
/* 18 */     System.arraycopy(dp.fVals, 0, this.fVals, 0, dp.fVals.length);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public float getFeatureValue(int fid) {
/* 24 */     if (fid <= 0 || fid >= this.fVals.length) {
/*    */       
/* 26 */       if (missingZero) return 0.0F; 
/* 27 */       throw RankLibError.create("Error in DenseDataPoint::getFeatureValue(): requesting unspecified feature, fid=" + fid);
/*    */     } 
/* 29 */     if (isUnknown(this.fVals[fid]))
/* 30 */       return 0.0F; 
/* 31 */     return this.fVals[fid];
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setFeatureValue(int fid, float fval) {
/* 37 */     if (fid <= 0 || fid >= this.fVals.length)
/*    */     {
/* 39 */       throw RankLibError.create("Error in DenseDataPoint::setFeatureValue(): feature (id=" + fid + ") not found.");
/*    */     }
/* 41 */     this.fVals[fid] = fval;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setFeatureVector(float[] dfVals) {
/* 48 */     this.fVals = dfVals;
/*    */   }
/*    */ 
/*    */   
/*    */   public float[] getFeatureVector() {
/* 53 */     return this.fVals;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\learning\DenseDataPoint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */