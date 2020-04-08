/*     */ package ciir.umass.edu.learning;
/*     */ 
/*     */ import ciir.umass.edu.metric.MetricScorer;
/*     */ import ciir.umass.edu.utilities.KeyValuePair;
/*     */ import ciir.umass.edu.utilities.RankLibError;
/*     */ import ciir.umass.edu.utilities.SimpleMath;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.StringReader;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
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
/*     */ public class LinearRegRank
/*     */   extends Ranker
/*     */ {
/*  24 */   public static double lambda = 1.0E-10D;
/*     */ 
/*     */   
/*  27 */   protected double[] weight = null;
/*     */ 
/*     */   
/*     */   public LinearRegRank() {}
/*     */ 
/*     */   
/*     */   public LinearRegRank(List<RankList> samples, int[] features, MetricScorer scorer) {
/*  34 */     super(samples, features, scorer);
/*     */   }
/*     */   
/*     */   public void init() {
/*  38 */     PRINTLN("Initializing... [Done]");
/*     */   }
/*     */   
/*     */   public void learn() {
/*  42 */     PRINTLN("--------------------------------");
/*  43 */     PRINTLN("Training starts...");
/*  44 */     PRINTLN("--------------------------------");
/*  45 */     PRINT("Learning the least square model... ");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  52 */     int nVar = DataPoint.getFeatureCount();
/*     */     
/*  54 */     double[][] xTx = new double[nVar][];
/*  55 */     for (int i = 0; i < nVar; i++) {
/*     */       
/*  57 */       xTx[i] = new double[nVar];
/*  58 */       Arrays.fill(xTx[i], 0.0D);
/*     */     } 
/*  60 */     double[] xTy = new double[nVar];
/*  61 */     Arrays.fill(xTy, 0.0D);
/*     */     
/*  63 */     for (int s = 0; s < this.samples.size(); s++) {
/*     */       
/*  65 */       RankList rl = this.samples.get(s);
/*  66 */       for (int j = 0; j < rl.size(); j++) {
/*     */         
/*  68 */         xTy[nVar - 1] = xTy[nVar - 1] + rl.get(j).getLabel();
/*  69 */         for (int m = 0; m < nVar - 1; m++) {
/*     */           
/*  71 */           xTy[m] = xTy[m] + (rl.get(j).getFeatureValue(m + 1) * rl.get(j).getLabel());
/*  72 */           for (int n = 0; n < nVar; n++) {
/*     */             
/*  74 */             double t = (n < nVar - 1) ? rl.get(j).getFeatureValue(n + 1) : 1.0D;
/*  75 */             xTx[m][n] = xTx[m][n] + rl.get(j).getFeatureValue(m + 1) * t;
/*     */           } 
/*     */         } 
/*  78 */         for (int k = 0; k < nVar - 1; k++)
/*  79 */           xTx[nVar - 1][k] = xTx[nVar - 1][k] + rl.get(j).getFeatureValue(k + 1); 
/*  80 */         xTx[nVar - 1][nVar - 1] = xTx[nVar - 1][nVar - 1] + 1.0D;
/*     */       } 
/*     */     } 
/*  83 */     if (lambda != 0.0D)
/*     */     {
/*  85 */       for (int j = 0; j < xTx.length; j++)
/*  86 */         xTx[j][j] = xTx[j][j] + lambda; 
/*     */     }
/*  88 */     this.weight = solve(xTx, xTy);
/*  89 */     PRINTLN("[Done]");
/*     */     
/*  91 */     this.scoreOnTrainingData = SimpleMath.round(this.scorer.score(rank(this.samples)), 4);
/*  92 */     PRINTLN("---------------------------------");
/*  93 */     PRINTLN("Finished sucessfully.");
/*  94 */     PRINTLN(this.scorer.name() + " on training data: " + this.scoreOnTrainingData);
/*     */     
/*  96 */     if (this.validationSamples != null) {
/*     */       
/*  98 */       this.bestScoreOnValidationData = this.scorer.score(rank(this.validationSamples));
/*  99 */       PRINTLN(this.scorer.name() + " on validation data: " + SimpleMath.round(this.bestScoreOnValidationData, 4));
/*     */     } 
/* 101 */     PRINTLN("---------------------------------");
/*     */   }
/*     */   
/*     */   public double eval(DataPoint p) {
/* 105 */     double score = this.weight[this.weight.length - 1];
/* 106 */     for (int i = 0; i < this.features.length; i++)
/* 107 */       score += this.weight[i] * p.getFeatureValue(this.features[i]); 
/* 108 */     return score;
/*     */   }
/*     */   
/*     */   public Ranker createNew() {
/* 112 */     return new LinearRegRank();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 116 */     String output = "0:" + this.weight[0] + " ";
/* 117 */     for (int i = 0; i < this.features.length; i++)
/* 118 */       output = output + this.features[i] + ":" + this.weight[i] + ((i == this.weight.length - 1) ? "" : " "); 
/* 119 */     return output;
/*     */   }
/*     */   
/*     */   public String model() {
/* 123 */     String output = "## " + name() + "\n";
/* 124 */     output = output + "## Lambda = " + lambda + "\n";
/* 125 */     output = output + toString();
/* 126 */     return output;
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadFromString(String fullText) {
/*     */     try {
/* 132 */       String content = "";
/* 133 */       BufferedReader in = new BufferedReader(new StringReader(fullText));
/*     */       
/* 135 */       KeyValuePair kvp = null;
/* 136 */       while ((content = in.readLine()) != null) {
/*     */         
/* 138 */         content = content.trim();
/* 139 */         if (content.length() == 0)
/*     */           continue; 
/* 141 */         if (content.indexOf("##") == 0)
/*     */           continue; 
/* 143 */         kvp = new KeyValuePair(content);
/*     */       } 
/*     */       
/* 146 */       in.close();
/*     */       
/* 148 */       assert kvp != null;
/* 149 */       List<String> keys = kvp.keys();
/* 150 */       List<String> values = kvp.values();
/* 151 */       this.weight = new double[keys.size()];
/* 152 */       this.features = new int[keys.size() - 1];
/* 153 */       int idx = 0;
/* 154 */       for (int i = 0; i < keys.size(); i++) {
/*     */         
/* 156 */         int fid = Integer.parseInt(keys.get(i));
/* 157 */         if (fid > 0) {
/*     */           
/* 159 */           this.features[idx] = fid;
/* 160 */           this.weight[idx] = Double.parseDouble((String)values.get(i));
/* 161 */           idx++;
/*     */         } else {
/*     */           
/* 164 */           this.weight[this.weight.length - 1] = Double.parseDouble((String)values.get(i));
/*     */         } 
/*     */       } 
/* 167 */     } catch (Exception ex) {
/*     */       
/* 169 */       throw RankLibError.create("Error in LinearRegRank::load(): ", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void printParameters() {
/* 174 */     PRINTLN("L2-norm regularization: lambda = " + lambda);
/*     */   }
/*     */   
/*     */   public String name() {
/* 178 */     return "Linear Regression";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected double[] solve(double[][] A, double[] B) {
/* 188 */     if (A.length == 0 || B.length == 0) {
/*     */       
/* 190 */       System.out.println("Error: some of the input arrays is empty.");
/* 191 */       System.exit(1);
/*     */     } 
/* 193 */     if ((A[0]).length == 0) {
/*     */       
/* 195 */       System.out.println("Error: some of the input arrays is empty.");
/* 196 */       System.exit(1);
/*     */     } 
/* 198 */     if (A.length != B.length) {
/*     */       
/* 200 */       System.out.println("Error: Solving Ax=B: A and B have different dimension.");
/* 201 */       System.exit(1);
/*     */     } 
/*     */ 
/*     */     
/* 205 */     double[][] a = new double[A.length][];
/* 206 */     double[] b = new double[B.length];
/* 207 */     System.arraycopy(B, 0, b, 0, B.length);
/* 208 */     for (int i = 0; i < a.length; i++) {
/*     */       
/* 210 */       a[i] = new double[(A[i]).length];
/* 211 */       if (i > 0)
/*     */       {
/* 213 */         if ((a[i]).length != (a[i - 1]).length) {
/*     */           
/* 215 */           System.out.println("Error: Solving Ax=B: A is NOT a square matrix.");
/* 216 */           System.exit(1);
/*     */         } 
/*     */       }
/* 219 */       System.arraycopy(A[i], 0, a[i], 0, (A[i]).length);
/*     */     } 
/*     */     
/* 222 */     double pivot = 0.0D;
/* 223 */     double multiplier = 0.0D;
/* 224 */     for (int j = 0; j < b.length - 1; j++) {
/*     */       
/* 226 */       pivot = a[j][j];
/* 227 */       for (int m = j + 1; m < b.length; m++) {
/*     */         
/* 229 */         multiplier = a[m][j] / pivot;
/*     */         
/* 231 */         for (int i1 = j + 1; i1 < b.length; i1++)
/* 232 */           a[m][i1] = a[m][i1] - a[j][i1] * multiplier; 
/* 233 */         b[m] = b[m] - b[j] * multiplier;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 238 */     double[] x = new double[b.length];
/* 239 */     int n = b.length;
/* 240 */     x[n - 1] = b[n - 1] / a[n - 1][n - 1];
/* 241 */     for (int k = n - 2; k >= 0; k--) {
/*     */       
/* 243 */       double val = b[k];
/* 244 */       for (int m = k + 1; m < n; m++)
/* 245 */         val -= a[k][m] * x[m]; 
/* 246 */       x[k] = val / a[k][k];
/*     */     } 
/*     */     
/* 249 */     return x;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\learning\LinearRegRank.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */