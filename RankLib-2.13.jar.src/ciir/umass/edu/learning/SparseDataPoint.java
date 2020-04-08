/*     */ package ciir.umass.edu.learning;
/*     */ 
/*     */ import ciir.umass.edu.utilities.RankLibError;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SparseDataPoint
/*     */   extends DataPoint
/*     */ {
/*     */   private enum accessPattern
/*     */   {
/*  23 */     SEQUENTIAL, RANDOM; }
/*  24 */   private static accessPattern searchPattern = accessPattern.RANDOM;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int[] fIds;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  37 */   int lastMinId = -1;
/*  38 */   int lastMinPos = -1;
/*     */   
/*     */   public SparseDataPoint(String text) {
/*  41 */     super(text);
/*     */   }
/*     */ 
/*     */   
/*     */   public SparseDataPoint(SparseDataPoint dp) {
/*  46 */     this.label = dp.label;
/*  47 */     this.id = dp.id;
/*  48 */     this.description = dp.description;
/*  49 */     this.cached = dp.cached;
/*  50 */     this.fIds = new int[dp.fIds.length];
/*  51 */     this.fVals = new float[dp.fVals.length];
/*  52 */     System.arraycopy(dp.fIds, 0, this.fIds, 0, dp.fIds.length);
/*  53 */     System.arraycopy(dp.fVals, 0, this.fVals, 0, dp.fVals.length);
/*     */   }
/*     */   
/*     */   private int locate(int fid) {
/*  57 */     if (searchPattern == accessPattern.SEQUENTIAL) {
/*     */       
/*  59 */       if (this.lastMinId > fid) {
/*     */         
/*  61 */         this.lastMinId = -1;
/*  62 */         this.lastMinPos = -1;
/*     */       } 
/*  64 */       while (this.lastMinPos < this.knownFeatures && this.lastMinId < fid)
/*  65 */         this.lastMinId = this.fIds[++this.lastMinPos]; 
/*  66 */       if (this.lastMinId == fid) {
/*  67 */         return this.lastMinPos;
/*     */       }
/*  69 */     } else if (searchPattern == accessPattern.RANDOM) {
/*     */       
/*  71 */       int pos = Arrays.binarySearch(this.fIds, fid);
/*  72 */       if (pos >= 0) {
/*  73 */         return pos;
/*     */       }
/*     */     } else {
/*  76 */       System.err.println("Invalid search pattern specified for sparse data points.");
/*     */     } 
/*  78 */     return -1;
/*     */   }
/*     */   
/*     */   public boolean hasFeature(int fid) {
/*  82 */     return (locate(fid) != -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getFeatureValue(int fid) {
/*  89 */     if (fid <= 0 || fid > getFeatureCount()) {
/*     */       
/*  91 */       if (missingZero) return 0.0F; 
/*  92 */       throw RankLibError.create("Error in SparseDataPoint::getFeatureValue(): requesting unspecified feature, fid=" + fid);
/*     */     } 
/*  94 */     int pos = locate(fid);
/*     */ 
/*     */ 
/*     */     
/*  98 */     if (pos >= 0) {
/*  99 */       return this.fVals[pos];
/*     */     }
/* 101 */     return 0.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFeatureValue(int fid, float fval) {
/* 107 */     if (fid <= 0 || fid > getFeatureCount())
/*     */     {
/* 109 */       throw RankLibError.create("Error in SparseDataPoint::setFeatureValue(): feature (id=" + fid + ") out of range.");
/*     */     }
/* 111 */     int pos = locate(fid);
/* 112 */     if (pos >= 0) {
/* 113 */       this.fVals[pos] = fval;
/*     */     } else {
/*     */       
/* 116 */       System.err.println("Error in SparseDataPoint::setFeatureValue(): feature (id=" + fid + ") not found.");
/* 117 */       System.exit(1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFeatureVector(float[] dfVals) {
/* 124 */     this.fIds = new int[this.knownFeatures];
/* 125 */     this.fVals = new float[this.knownFeatures];
/* 126 */     int pos = 0;
/* 127 */     for (int i = 1; i < dfVals.length; i++) {
/*     */       
/* 129 */       if (!isUnknown(dfVals[i])) {
/*     */         
/* 131 */         this.fIds[pos] = i;
/* 132 */         this.fVals[pos] = dfVals[i];
/* 133 */         pos++;
/*     */       } 
/*     */     } 
/* 136 */     assert pos == this.knownFeatures;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float[] getFeatureVector() {
/* 142 */     float[] dfVals = new float[this.fIds[this.knownFeatures - 1]];
/* 143 */     Arrays.fill(dfVals, UNKNOWN);
/* 144 */     for (int i = 0; i < this.knownFeatures; i++)
/* 145 */       dfVals[this.fIds[i]] = this.fVals[i]; 
/* 146 */     return dfVals;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\learning\SparseDataPoint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */