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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DataPoint
/*     */ {
/*     */   public static boolean missingZero = false;
/*  25 */   public static int MAX_FEATURE = 51;
/*  26 */   public static int FEATURE_INCREASE = 10;
/*  27 */   protected static int featureCount = 0;
/*     */   
/*  29 */   protected static float UNKNOWN = Float.NaN;
/*     */ 
/*     */   
/*  32 */   protected float label = 0.0F;
/*  33 */   protected String id = "";
/*  34 */   protected String description = "";
/*  35 */   protected float[] fVals = null;
/*     */ 
/*     */   
/*     */   protected int knownFeatures;
/*     */ 
/*     */   
/*  41 */   protected double cached = -1.0D;
/*     */ 
/*     */   
/*     */   protected static boolean isUnknown(float fVal) {
/*  45 */     return Float.isNaN(fVal);
/*     */   }
/*     */   
/*     */   protected static String getKey(String pair) {
/*  49 */     return pair.substring(0, pair.indexOf(":"));
/*     */   }
/*     */   
/*     */   protected static String getValue(String pair) {
/*  53 */     return pair.substring(pair.lastIndexOf(":") + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected float[] parse(String text) {
/*  63 */     float[] fVals = new float[MAX_FEATURE];
/*  64 */     Arrays.fill(fVals, UNKNOWN);
/*  65 */     int lastFeature = -1;
/*     */     try {
/*  67 */       int idx = text.indexOf("#");
/*  68 */       if (idx != -1) {
/*     */         
/*  70 */         this.description = text.substring(idx);
/*  71 */         text = text.substring(0, idx).trim();
/*     */       } 
/*  73 */       String[] fs = text.split("\\s+");
/*  74 */       this.label = Float.parseFloat(fs[0]);
/*  75 */       if (this.label < 0.0F) {
/*     */         
/*  77 */         System.out.println("Relevance label cannot be negative. System will now exit.");
/*  78 */         System.exit(1);
/*     */       } 
/*  80 */       this.id = getValue(fs[1]);
/*  81 */       String key = "";
/*  82 */       String val = "";
/*  83 */       for (int i = 2; i < fs.length; i++) {
/*     */         
/*  85 */         this.knownFeatures++;
/*  86 */         key = getKey(fs[i]);
/*  87 */         val = getValue(fs[i]);
/*  88 */         int f = Integer.parseInt(key);
/*  89 */         if (f <= 0) throw RankLibError.create("Cannot use feature numbering less than or equal to zero. Start your features at 1."); 
/*  90 */         if (f >= MAX_FEATURE) {
/*     */           
/*  92 */           while (f >= MAX_FEATURE)
/*  93 */             MAX_FEATURE += FEATURE_INCREASE; 
/*  94 */           float[] arrayOfFloat = new float[MAX_FEATURE];
/*  95 */           System.arraycopy(fVals, 0, arrayOfFloat, 0, fVals.length);
/*  96 */           Arrays.fill(arrayOfFloat, fVals.length, MAX_FEATURE, UNKNOWN);
/*  97 */           fVals = arrayOfFloat;
/*     */         } 
/*  99 */         fVals[f] = Float.parseFloat(val);
/*     */         
/* 101 */         if (f > featureCount) {
/* 102 */           featureCount = f;
/*     */         }
/* 104 */         if (f > lastFeature) {
/* 105 */           lastFeature = f;
/*     */         }
/*     */       } 
/* 108 */       float[] tmp = new float[lastFeature + 1];
/* 109 */       System.arraycopy(fVals, 0, tmp, 0, lastFeature + 1);
/* 110 */       fVals = tmp;
/*     */     }
/* 112 */     catch (Exception ex) {
/*     */       
/* 114 */       throw RankLibError.create("Error in DataPoint::parse()", ex);
/*     */     } 
/* 116 */     return fVals;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract float getFeatureValue(int paramInt);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void setFeatureValue(int paramInt, float paramFloat);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void setFeatureVector(float[] paramArrayOffloat);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract float[] getFeatureVector();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DataPoint() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DataPoint(String text) {
/* 154 */     float[] fVals = parse(text);
/* 155 */     setFeatureVector(fVals);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getID() {
/* 160 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setID(String id) {
/* 164 */     this.id = id;
/*     */   }
/*     */   
/*     */   public float getLabel() {
/* 168 */     return this.label;
/*     */   }
/*     */   
/*     */   public void setLabel(float label) {
/* 172 */     this.label = label;
/*     */   }
/*     */   
/*     */   public String getDescription() {
/* 176 */     return this.description;
/*     */   }
/*     */   public void setDescription(String description) {
/* 179 */     assert description.contains("#");
/* 180 */     this.description = description;
/*     */   }
/*     */   
/*     */   public void setCached(double c) {
/* 184 */     this.cached = c;
/*     */   }
/*     */   
/*     */   public double getCached() {
/* 188 */     return this.cached;
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetCached() {
/* 193 */     this.cached = -1.0E8D;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 198 */     float[] fVals = getFeatureVector();
/* 199 */     String output = (int)this.label + " qid:" + this.id + " ";
/* 200 */     for (int i = 1; i < fVals.length; i++) {
/* 201 */       if (!isUnknown(fVals[i]))
/* 202 */         output = output + i + ":" + fVals[i] + ((i == fVals.length - 1) ? "" : " "); 
/* 203 */     }  output = output + " " + this.description;
/* 204 */     return output;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getFeatureCount() {
/* 209 */     return featureCount;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\learning\DataPoint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */