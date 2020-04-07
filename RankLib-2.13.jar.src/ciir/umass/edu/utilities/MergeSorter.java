/*     */ package ciir.umass.edu.utilities;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MergeSorter
/*     */ {
/*     */   public static void main(String[] args) {
/*  23 */     float[][] f = new float[1000][];
/*  24 */     for (int r = 0; r < f.length; r++) {
/*     */       
/*  26 */       f[r] = new float[500];
/*  27 */       Random rd = new Random();
/*  28 */       for (int j = 0; j < (f[r]).length; j++) {
/*     */ 
/*     */         
/*  31 */         float x = rd.nextInt(10);
/*     */         
/*  33 */         f[r][j] = x;
/*     */       } 
/*     */     } 
/*     */     
/*  37 */     double start = System.nanoTime();
/*  38 */     for (int i = 0; i < f.length; i++)
/*  39 */       sort(f[i], false); 
/*  40 */     double end = System.nanoTime();
/*  41 */     System.out.println("# " + ((end - start) / 1.0E9D) + " ");
/*     */   }
/*     */ 
/*     */   
/*     */   public static int[] sort(float[] list, boolean asc) {
/*  46 */     return sort(list, 0, list.length - 1, asc);
/*     */   }
/*     */   
/*     */   public static int[] sort(float[] list, int begin, int end, boolean asc) {
/*  50 */     int len = end - begin + 1;
/*  51 */     int[] idx = new int[len];
/*  52 */     int[] tmp = new int[len]; int i;
/*  53 */     for (i = begin; i <= end; i++) {
/*  54 */       idx[i - begin] = i;
/*     */     }
/*     */     
/*  57 */     i = 1;
/*  58 */     int j = 0;
/*  59 */     int k = 0;
/*  60 */     int start = 0;
/*  61 */     int[] ph = new int[len / 2 + 3];
/*  62 */     ph[0] = 0;
/*  63 */     int p = 1;
/*     */     while (true) {
/*  65 */       start = i - 1;
/*  66 */       for (; i < idx.length && ((asc && list[begin + i] >= list[begin + i - 1]) || (!asc && list[begin + i] <= list[begin + i - 1])); i++);
/*  67 */       if (i == idx.length) {
/*     */         
/*  69 */         System.arraycopy(idx, start, tmp, k, i - start);
/*  70 */         k = i;
/*     */       }
/*     */       else {
/*     */         
/*  74 */         j = i + 1;
/*  75 */         for (; j < idx.length && ((asc && list[begin + j] >= list[begin + j - 1]) || (!asc && list[begin + j] <= list[begin + j - 1])); j++);
/*  76 */         merge(list, idx, start, i - 1, i, j - 1, tmp, k, asc);
/*  77 */         i = j + 1;
/*  78 */         k = j;
/*     */       } 
/*  80 */       ph[p++] = k;
/*  81 */       if (k >= idx.length) {
/*  82 */         System.arraycopy(tmp, 0, idx, 0, idx.length);
/*     */ 
/*     */         
/*  85 */         while (p > 2) {
/*     */           
/*  87 */           if (p % 2 == 0)
/*  88 */             ph[p++] = idx.length; 
/*  89 */           k = 0;
/*  90 */           int np = 1;
/*  91 */           for (int w = 0; w < p - 1; w += 2) {
/*     */             
/*  93 */             merge(list, idx, ph[w], ph[w + 1] - 1, ph[w + 1], ph[w + 2] - 1, tmp, k, asc);
/*  94 */             k = ph[w + 2];
/*  95 */             ph[np++] = k;
/*     */           } 
/*  97 */           p = np;
/*  98 */           System.arraycopy(tmp, 0, idx, 0, idx.length);
/*     */         } 
/* 100 */         return idx;
/*     */       } 
/*     */     } 
/*     */   } private static void merge(float[] list, int[] idx, int s1, int e1, int s2, int e2, int[] tmp, int l, boolean asc) {
/* 104 */     int i = s1;
/* 105 */     int j = s2;
/* 106 */     int k = l;
/* 107 */     while (i <= e1 && j <= e2) {
/*     */       
/* 109 */       if (asc) {
/*     */         
/* 111 */         if (list[idx[i]] <= list[idx[j]]) {
/* 112 */           tmp[k++] = idx[i++]; continue;
/*     */         } 
/* 114 */         tmp[k++] = idx[j++];
/*     */         
/*     */         continue;
/*     */       } 
/* 118 */       if (list[idx[i]] >= list[idx[j]]) {
/* 119 */         tmp[k++] = idx[i++]; continue;
/*     */       } 
/* 121 */       tmp[k++] = idx[j++];
/*     */     } 
/*     */     
/* 124 */     while (i <= e1)
/* 125 */       tmp[k++] = idx[i++]; 
/* 126 */     while (j <= e2) {
/* 127 */       tmp[k++] = idx[j++];
/*     */     }
/*     */   }
/*     */   
/*     */   public static int[] sort(double[] list, boolean asc) {
/* 132 */     return sort(list, 0, list.length - 1, asc);
/*     */   }
/*     */   
/*     */   public static int[] sort(double[] list, int begin, int end, boolean asc) {
/* 136 */     int len = end - begin + 1;
/* 137 */     int[] idx = new int[len];
/* 138 */     int[] tmp = new int[len]; int i;
/* 139 */     for (i = begin; i <= end; i++) {
/* 140 */       idx[i - begin] = i;
/*     */     }
/*     */     
/* 143 */     i = 1;
/* 144 */     int j = 0;
/* 145 */     int k = 0;
/* 146 */     int start = 0;
/* 147 */     int[] ph = new int[len / 2 + 3];
/* 148 */     ph[0] = 0;
/* 149 */     int p = 1;
/*     */     while (true) {
/* 151 */       start = i - 1;
/* 152 */       for (; i < idx.length && ((asc && list[begin + i] >= list[begin + i - 1]) || (!asc && list[begin + i] <= list[begin + i - 1])); i++);
/* 153 */       if (i == idx.length) {
/*     */         
/* 155 */         System.arraycopy(idx, start, tmp, k, i - start);
/* 156 */         k = i;
/*     */       }
/*     */       else {
/*     */         
/* 160 */         j = i + 1;
/* 161 */         for (; j < idx.length && ((asc && list[begin + j] >= list[begin + j - 1]) || (!asc && list[begin + j] <= list[begin + j - 1])); j++);
/* 162 */         merge(list, idx, start, i - 1, i, j - 1, tmp, k, asc);
/* 163 */         i = j + 1;
/* 164 */         k = j;
/*     */       } 
/* 166 */       ph[p++] = k;
/* 167 */       if (k >= idx.length) {
/* 168 */         System.arraycopy(tmp, 0, idx, 0, idx.length);
/*     */ 
/*     */         
/* 171 */         while (p > 2) {
/*     */           
/* 173 */           if (p % 2 == 0)
/* 174 */             ph[p++] = idx.length; 
/* 175 */           k = 0;
/* 176 */           int np = 1;
/* 177 */           for (int w = 0; w < p - 1; w += 2) {
/*     */             
/* 179 */             merge(list, idx, ph[w], ph[w + 1] - 1, ph[w + 1], ph[w + 2] - 1, tmp, k, asc);
/* 180 */             k = ph[w + 2];
/* 181 */             ph[np++] = k;
/*     */           } 
/* 183 */           p = np;
/* 184 */           System.arraycopy(tmp, 0, idx, 0, idx.length);
/*     */         } 
/* 186 */         return idx;
/*     */       } 
/*     */     } 
/*     */   } private static void merge(double[] list, int[] idx, int s1, int e1, int s2, int e2, int[] tmp, int l, boolean asc) {
/* 190 */     int i = s1;
/* 191 */     int j = s2;
/* 192 */     int k = l;
/* 193 */     while (i <= e1 && j <= e2) {
/*     */       
/* 195 */       if (asc) {
/*     */         
/* 197 */         if (list[idx[i]] <= list[idx[j]]) {
/* 198 */           tmp[k++] = idx[i++]; continue;
/*     */         } 
/* 200 */         tmp[k++] = idx[j++];
/*     */         
/*     */         continue;
/*     */       } 
/* 204 */       if (list[idx[i]] >= list[idx[j]]) {
/* 205 */         tmp[k++] = idx[i++]; continue;
/*     */       } 
/* 207 */       tmp[k++] = idx[j++];
/*     */     } 
/*     */     
/* 210 */     while (i <= e1)
/* 211 */       tmp[k++] = idx[i++]; 
/* 212 */     while (j <= e2)
/* 213 */       tmp[k++] = idx[j++]; 
/*     */   }
/*     */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\ed\\utilities\MergeSorter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */