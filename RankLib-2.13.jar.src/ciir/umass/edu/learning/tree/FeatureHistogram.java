/*     */ package ciir.umass.edu.learning.tree;
/*     */ 
/*     */ import ciir.umass.edu.learning.DataPoint;
/*     */ import ciir.umass.edu.utilities.MyThreadPool;
/*     */ import ciir.umass.edu.utilities.WorkerThread;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Random;
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
/*     */ public class FeatureHistogram
/*     */ {
/*     */   class Config
/*     */   {
/*  26 */     int featureIdx = -1;
/*  27 */     int thresholdIdx = -1;
/*  28 */     double S = -1.0D;
/*     */   }
/*     */ 
/*     */   
/*  32 */   public static float samplingRate = 1.0F;
/*     */ 
/*     */   
/*  35 */   public int[] features = null;
/*  36 */   public float[][] thresholds = (float[][])null;
/*  37 */   public double[][] sum = (double[][])null;
/*  38 */   public double sumResponse = 0.0D;
/*  39 */   public double sqSumResponse = 0.0D;
/*  40 */   public int[][] count = (int[][])null;
/*  41 */   public int[][] sampleToThresholdMap = (int[][])null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean reuseParent = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void construct(DataPoint[] samples, double[] labels, int[][] sampleSortedIdx, int[] features, float[][] thresholds) {
/*  54 */     this.features = features;
/*  55 */     this.thresholds = thresholds;
/*     */     
/*  57 */     this.sumResponse = 0.0D;
/*  58 */     this.sqSumResponse = 0.0D;
/*     */     
/*  60 */     this.sum = new double[features.length][];
/*  61 */     this.count = new int[features.length][];
/*  62 */     this.sampleToThresholdMap = new int[features.length][];
/*     */     
/*  64 */     MyThreadPool p = MyThreadPool.getInstance();
/*  65 */     if (p.size() == 1) {
/*  66 */       construct(samples, labels, sampleSortedIdx, thresholds, 0, features.length - 1);
/*     */     } else {
/*  68 */       p.execute(new Worker(this, samples, labels, sampleSortedIdx, thresholds), features.length);
/*     */     } 
/*     */   }
/*     */   protected void construct(DataPoint[] samples, double[] labels, int[][] sampleSortedIdx, float[][] thresholds, int start, int end) {
/*  72 */     for (int i = start; i <= end; i++) {
/*     */       
/*  74 */       int fid = this.features[i];
/*     */       
/*  76 */       int[] idx = sampleSortedIdx[i];
/*     */       
/*  78 */       double sumLeft = 0.0D;
/*  79 */       float[] threshold = thresholds[i];
/*  80 */       double[] sumLabel = new double[threshold.length];
/*  81 */       int[] c = new int[threshold.length];
/*  82 */       int[] stMap = new int[samples.length];
/*     */       
/*  84 */       int last = -1;
/*  85 */       for (int t = 0; t < threshold.length; t++) {
/*     */         
/*  87 */         int j = last + 1;
/*     */         
/*  89 */         for (; j < idx.length; j++) {
/*     */           
/*  91 */           int k = idx[j];
/*  92 */           if (samples[k].getFeatureValue(fid) > threshold[t])
/*     */             break; 
/*  94 */           sumLeft += labels[k];
/*  95 */           if (i == 0) {
/*     */             
/*  97 */             this.sumResponse += labels[k];
/*  98 */             this.sqSumResponse += labels[k] * labels[k];
/*     */           } 
/* 100 */           stMap[k] = t;
/*     */         } 
/* 102 */         last = j - 1;
/* 103 */         sumLabel[t] = sumLeft;
/* 104 */         c[t] = last + 1;
/*     */       } 
/* 106 */       this.sampleToThresholdMap[i] = stMap;
/* 107 */       this.sum[i] = sumLabel;
/* 108 */       this.count[i] = c;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void update(double[] labels) {
/* 114 */     this.sumResponse = 0.0D;
/* 115 */     this.sqSumResponse = 0.0D;
/*     */     
/* 117 */     MyThreadPool p = MyThreadPool.getInstance();
/* 118 */     if (p.size() == 1) {
/* 119 */       update(labels, 0, this.features.length - 1);
/*     */     } else {
/* 121 */       p.execute(new Worker(this, labels), this.features.length);
/*     */     } 
/*     */   }
/*     */   protected void update(double[] labels, int start, int end) {
/* 125 */     for (int i = start; i <= end; i++)
/* 126 */       Arrays.fill(this.sum[i], 0.0D); 
/* 127 */     for (int k = 0; k < labels.length; k++) {
/*     */       
/* 129 */       for (int j = start; j <= end; j++) {
/*     */         
/* 131 */         int t = this.sampleToThresholdMap[j][k];
/* 132 */         this.sum[j][t] = this.sum[j][t] + labels[k];
/* 133 */         if (j == 0) {
/*     */           
/* 135 */           this.sumResponse += labels[k];
/* 136 */           this.sqSumResponse += labels[k] * labels[k];
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 141 */     for (int f = start; f <= end; f++) {
/*     */       
/* 143 */       for (int t = 1; t < (this.thresholds[f]).length; t++) {
/* 144 */         this.sum[f][t] = this.sum[f][t] + this.sum[f][t - 1];
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void construct(FeatureHistogram parent, int[] soi, double[] labels) {
/* 150 */     this.features = parent.features;
/* 151 */     this.thresholds = parent.thresholds;
/* 152 */     this.sumResponse = 0.0D;
/* 153 */     this.sqSumResponse = 0.0D;
/* 154 */     this.sum = new double[this.features.length][];
/* 155 */     this.count = new int[this.features.length][];
/* 156 */     this.sampleToThresholdMap = parent.sampleToThresholdMap;
/*     */     
/* 158 */     MyThreadPool p = MyThreadPool.getInstance();
/* 159 */     if (p.size() == 1) {
/* 160 */       construct(parent, soi, labels, 0, this.features.length - 1);
/*     */     } else {
/* 162 */       p.execute(new Worker(this, parent, soi, labels), this.features.length);
/*     */     } 
/*     */   }
/*     */   protected void construct(FeatureHistogram parent, int[] soi, double[] labels, int start, int end) {
/*     */     int i;
/* 167 */     for (i = start; i <= end; i++) {
/*     */       
/* 169 */       float[] threshold = this.thresholds[i];
/* 170 */       this.sum[i] = new double[threshold.length];
/* 171 */       this.count[i] = new int[threshold.length];
/* 172 */       Arrays.fill(this.sum[i], 0.0D);
/* 173 */       Arrays.fill(this.count[i], 0);
/*     */     } 
/*     */ 
/*     */     
/* 177 */     for (i = 0; i < soi.length; i++) {
/*     */       
/* 179 */       int k = soi[i];
/* 180 */       for (int j = start; j <= end; j++) {
/*     */         
/* 182 */         int t = this.sampleToThresholdMap[j][k];
/* 183 */         this.sum[j][t] = this.sum[j][t] + labels[k];
/* 184 */         this.count[j][t] = this.count[j][t] + 1;
/* 185 */         if (j == 0) {
/*     */           
/* 187 */           this.sumResponse += labels[k];
/* 188 */           this.sqSumResponse += labels[k] * labels[k];
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 193 */     for (int f = start; f <= end; f++) {
/*     */       
/* 195 */       for (int t = 1; t < (this.thresholds[f]).length; t++) {
/*     */         
/* 197 */         this.sum[f][t] = this.sum[f][t] + this.sum[f][t - 1];
/* 198 */         this.count[f][t] = this.count[f][t] + this.count[f][t - 1];
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void construct(FeatureHistogram parent, FeatureHistogram leftSibling, boolean reuseParent) {
/* 205 */     this.reuseParent = reuseParent;
/* 206 */     this.features = parent.features;
/* 207 */     this.thresholds = parent.thresholds;
/* 208 */     parent.sumResponse -= leftSibling.sumResponse;
/* 209 */     parent.sqSumResponse -= leftSibling.sqSumResponse;
/*     */     
/* 211 */     if (reuseParent) {
/*     */       
/* 213 */       this.sum = parent.sum;
/* 214 */       this.count = parent.count;
/*     */     }
/*     */     else {
/*     */       
/* 218 */       this.sum = new double[this.features.length][];
/* 219 */       this.count = new int[this.features.length][];
/*     */     } 
/* 221 */     this.sampleToThresholdMap = parent.sampleToThresholdMap;
/*     */     
/* 223 */     MyThreadPool p = MyThreadPool.getInstance();
/* 224 */     if (p.size() == 1) {
/* 225 */       construct(parent, leftSibling, 0, this.features.length - 1);
/*     */     } else {
/* 227 */       p.execute(new Worker(this, parent, leftSibling), this.features.length);
/*     */     } 
/*     */   }
/*     */   protected void construct(FeatureHistogram parent, FeatureHistogram leftSibling, int start, int end) {
/* 231 */     for (int f = start; f <= end; f++) {
/*     */       
/* 233 */       float[] threshold = this.thresholds[f];
/* 234 */       if (!this.reuseParent) {
/*     */         
/* 236 */         this.sum[f] = new double[threshold.length];
/* 237 */         this.count[f] = new int[threshold.length];
/*     */       } 
/* 239 */       for (int t = 0; t < threshold.length; t++) {
/*     */         
/* 241 */         this.sum[f][t] = parent.sum[f][t] - leftSibling.sum[f][t];
/* 242 */         this.count[f][t] = parent.count[f][t] - leftSibling.count[f][t];
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Config findBestSplit(int[] usedFeatures, int minLeafSupport, int start, int end) {
/* 249 */     Config cfg = new Config();
/* 250 */     int totalCount = this.count[start][(this.count[start]).length - 1];
/* 251 */     for (int f = start; f <= end; f++) {
/*     */       
/* 253 */       int i = usedFeatures[f];
/* 254 */       float[] threshold = this.thresholds[i];
/*     */       
/* 256 */       for (int t = 0; t < threshold.length; t++) {
/*     */         
/* 258 */         int countLeft = this.count[i][t];
/* 259 */         int countRight = totalCount - countLeft;
/* 260 */         if (countLeft >= minLeafSupport && countRight >= minLeafSupport) {
/*     */ 
/*     */           
/* 263 */           double sumLeft = this.sum[i][t];
/* 264 */           double sumRight = this.sumResponse - sumLeft;
/*     */           
/* 266 */           double S = sumLeft * sumLeft / countLeft + sumRight * sumRight / countRight;
/* 267 */           if (cfg.S < S) {
/*     */             
/* 269 */             cfg.S = S;
/* 270 */             cfg.featureIdx = i;
/* 271 */             cfg.thresholdIdx = t;
/*     */           } 
/*     */         } 
/*     */       } 
/* 275 */     }  return cfg;
/*     */   }
/*     */   
/*     */   public boolean findBestSplit(Split sp, double[] labels, int minLeafSupport) {
/* 279 */     if (sp.getDeviance() >= 0.0D && sp.getDeviance() <= 0.0D) {
/* 280 */       return false;
/*     */     }
/* 282 */     int[] usedFeatures = null;
/* 283 */     if (samplingRate < 1.0F) {
/*     */       
/* 285 */       int size = (int)(samplingRate * this.features.length);
/* 286 */       usedFeatures = new int[size];
/*     */       
/* 288 */       List<Integer> fpool = new ArrayList<>();
/* 289 */       for (int i = 0; i < this.features.length; i++) {
/* 290 */         fpool.add(Integer.valueOf(i));
/*     */       }
/* 292 */       Random random = new Random();
/* 293 */       for (int m = 0; m < size; m++)
/*     */       {
/* 295 */         int sel = random.nextInt(fpool.size());
/* 296 */         usedFeatures[m] = ((Integer)fpool.get(sel)).intValue();
/* 297 */         fpool.remove(sel);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 302 */       usedFeatures = new int[this.features.length];
/* 303 */       for (int i = 0; i < this.features.length; i++) {
/* 304 */         usedFeatures[i] = i;
/*     */       }
/*     */     } 
/*     */     
/* 308 */     Config best = new Config();
/* 309 */     MyThreadPool p = MyThreadPool.getInstance();
/* 310 */     if (p.size() == 1) {
/* 311 */       best = findBestSplit(usedFeatures, minLeafSupport, 0, usedFeatures.length - 1);
/*     */     } else {
/*     */       
/* 314 */       WorkerThread[] workers = p.execute(new Worker(this, usedFeatures, minLeafSupport), usedFeatures.length);
/* 315 */       for (int i = 0; i < workers.length; i++) {
/*     */         
/* 317 */         Worker wk = (Worker)workers[i];
/* 318 */         if (best.S < wk.cfg.S) {
/* 319 */           best = wk.cfg;
/*     */         }
/*     */       } 
/*     */     } 
/* 323 */     if (best.S == -1.0D) {
/* 324 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 329 */     double[] sumLabel = this.sum[best.featureIdx];
/* 330 */     int[] sampleCount = this.count[best.featureIdx];
/*     */     
/* 332 */     double s = sumLabel[sumLabel.length - 1];
/* 333 */     int c = sampleCount[sumLabel.length - 1];
/*     */     
/* 335 */     double sumLeft = sumLabel[best.thresholdIdx];
/* 336 */     int countLeft = sampleCount[best.thresholdIdx];
/*     */     
/* 338 */     double sumRight = s - sumLeft;
/* 339 */     int countRight = c - countLeft;
/*     */     
/* 341 */     int[] left = new int[countLeft];
/* 342 */     int[] right = new int[countRight];
/* 343 */     int l = 0;
/* 344 */     int r = 0;
/* 345 */     int k = 0;
/* 346 */     int[] idx = sp.getSamples();
/* 347 */     for (int j = 0; j < idx.length; j++) {
/*     */       
/* 349 */       k = idx[j];
/* 350 */       if (this.sampleToThresholdMap[best.featureIdx][k] <= best.thresholdIdx) {
/* 351 */         left[l++] = k;
/*     */       } else {
/* 353 */         right[r++] = k;
/*     */       } 
/*     */     } 
/* 356 */     FeatureHistogram lh = new FeatureHistogram();
/* 357 */     lh.construct(sp.hist, left, labels);
/* 358 */     FeatureHistogram rh = new FeatureHistogram();
/* 359 */     rh.construct(sp.hist, lh, !sp.isRoot());
/*     */     
/* 361 */     double var = this.sqSumResponse - this.sumResponse * this.sumResponse / idx.length;
/* 362 */     double varLeft = lh.sqSumResponse - lh.sumResponse * lh.sumResponse / left.length;
/* 363 */     double varRight = rh.sqSumResponse - rh.sumResponse * rh.sumResponse / right.length;
/*     */     
/* 365 */     sp.set(this.features[best.featureIdx], this.thresholds[best.featureIdx][best.thresholdIdx], var);
/* 366 */     sp.setLeft(new Split(left, lh, varLeft, sumLeft));
/* 367 */     sp.setRight(new Split(right, rh, varRight, sumRight));
/*     */     
/* 369 */     sp.clearSamples();
/*     */     
/* 371 */     return true;
/*     */   }
/*     */   
/*     */   class Worker extends WorkerThread {
/* 375 */     FeatureHistogram fh = null;
/* 376 */     int type = -1;
/*     */ 
/*     */     
/* 379 */     int[] usedFeatures = null;
/* 380 */     int minLeafSup = -1;
/* 381 */     FeatureHistogram.Config cfg = null;
/*     */ 
/*     */     
/* 384 */     double[] labels = null;
/*     */ 
/*     */     
/* 387 */     FeatureHistogram parent = null;
/* 388 */     int[] soi = null;
/*     */ 
/*     */     
/* 391 */     FeatureHistogram leftSibling = null;
/*     */ 
/*     */     
/*     */     DataPoint[] samples;
/*     */ 
/*     */     
/*     */     int[][] sampleSortedIdx;
/*     */     
/*     */     float[][] thresholds;
/*     */ 
/*     */     
/*     */     public Worker(FeatureHistogram fh, int[] usedFeatures, int minLeafSup) {
/* 403 */       this.type = 0;
/* 404 */       this.fh = fh;
/* 405 */       this.usedFeatures = usedFeatures;
/* 406 */       this.minLeafSup = minLeafSup;
/*     */     }
/*     */     
/*     */     public Worker(FeatureHistogram fh, double[] labels) {
/* 410 */       this.type = 1;
/* 411 */       this.fh = fh;
/* 412 */       this.labels = labels;
/*     */     }
/*     */     
/*     */     public Worker(FeatureHistogram fh, FeatureHistogram parent, int[] soi, double[] labels) {
/* 416 */       this.type = 2;
/* 417 */       this.fh = fh;
/* 418 */       this.parent = parent;
/* 419 */       this.soi = soi;
/* 420 */       this.labels = labels;
/*     */     }
/*     */     
/*     */     public Worker(FeatureHistogram fh, FeatureHistogram parent, FeatureHistogram leftSibling) {
/* 424 */       this.type = 3;
/* 425 */       this.fh = fh;
/* 426 */       this.parent = parent;
/* 427 */       this.leftSibling = leftSibling;
/*     */     }
/*     */     
/*     */     public Worker(FeatureHistogram fh, DataPoint[] samples, double[] labels, int[][] sampleSortedIdx, float[][] thresholds) {
/* 431 */       this.type = 4;
/* 432 */       this.fh = fh;
/* 433 */       this.samples = samples;
/* 434 */       this.labels = labels;
/* 435 */       this.sampleSortedIdx = sampleSortedIdx;
/* 436 */       this.thresholds = thresholds;
/*     */     }
/*     */     
/*     */     public void run() {
/* 440 */       if (this.type == 0) {
/* 441 */         this.cfg = this.fh.findBestSplit(this.usedFeatures, this.minLeafSup, this.start, this.end);
/* 442 */       } else if (this.type == 1) {
/* 443 */         this.fh.update(this.labels, this.start, this.end);
/* 444 */       } else if (this.type == 2) {
/* 445 */         this.fh.construct(this.parent, this.soi, this.labels, this.start, this.end);
/* 446 */       } else if (this.type == 3) {
/* 447 */         this.fh.construct(this.parent, this.leftSibling, this.start, this.end);
/* 448 */       } else if (this.type == 4) {
/* 449 */         this.fh.construct(this.samples, this.labels, this.sampleSortedIdx, this.thresholds, this.start, this.end);
/*     */       } 
/*     */     }
/*     */     public WorkerThread clone() {
/* 453 */       Worker wk = new Worker();
/* 454 */       wk.fh = this.fh;
/* 455 */       wk.type = this.type;
/*     */ 
/*     */       
/* 458 */       wk.usedFeatures = this.usedFeatures;
/* 459 */       wk.minLeafSup = this.minLeafSup;
/*     */ 
/*     */ 
/*     */       
/* 463 */       wk.labels = this.labels;
/*     */ 
/*     */       
/* 466 */       wk.parent = this.parent;
/* 467 */       wk.soi = this.soi;
/*     */ 
/*     */       
/* 470 */       wk.leftSibling = this.leftSibling;
/*     */ 
/*     */       
/* 473 */       wk.samples = this.samples;
/* 474 */       wk.sampleSortedIdx = this.sampleSortedIdx;
/* 475 */       wk.thresholds = this.thresholds;
/*     */       
/* 477 */       return wk;
/*     */     }
/*     */     
/*     */     public Worker() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\learning\tree\FeatureHistogram.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */