/*     */ package ciir.umass.edu.utilities;
/*     */ 
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Sorter
/*     */ {
/*     */   public static int[] sort(double[] sortVal, boolean asc) {
/*  27 */     int[] freqIdx = new int[sortVal.length]; int i;
/*  28 */     for (i = 0; i < sortVal.length; i++)
/*  29 */       freqIdx[i] = i; 
/*  30 */     for (i = 0; i < sortVal.length - 1; i++) {
/*     */       
/*  32 */       int max = i;
/*  33 */       for (int j = i + 1; j < sortVal.length; j++) {
/*     */         
/*  35 */         if (asc) {
/*     */           
/*  37 */           if (sortVal[freqIdx[max]] > sortVal[freqIdx[j]]) {
/*  38 */             max = j;
/*     */           
/*     */           }
/*     */         }
/*  42 */         else if (sortVal[freqIdx[max]] < sortVal[freqIdx[j]]) {
/*  43 */           max = j;
/*     */         } 
/*     */       } 
/*     */       
/*  47 */       int tmp = freqIdx[i];
/*  48 */       freqIdx[i] = freqIdx[max];
/*  49 */       freqIdx[max] = tmp;
/*     */     } 
/*  51 */     return freqIdx;
/*     */   }
/*     */   
/*     */   public static int[] sort(float[] sortVal, boolean asc) {
/*  55 */     int[] freqIdx = new int[sortVal.length]; int i;
/*  56 */     for (i = 0; i < sortVal.length; i++)
/*  57 */       freqIdx[i] = i; 
/*  58 */     for (i = 0; i < sortVal.length - 1; i++) {
/*     */       
/*  60 */       int max = i;
/*  61 */       for (int j = i + 1; j < sortVal.length; j++) {
/*     */         
/*  63 */         if (asc) {
/*     */           
/*  65 */           if (sortVal[freqIdx[max]] > sortVal[freqIdx[j]]) {
/*  66 */             max = j;
/*     */           
/*     */           }
/*     */         }
/*  70 */         else if (sortVal[freqIdx[max]] < sortVal[freqIdx[j]]) {
/*  71 */           max = j;
/*     */         } 
/*     */       } 
/*     */       
/*  75 */       int tmp = freqIdx[i];
/*  76 */       freqIdx[i] = freqIdx[max];
/*  77 */       freqIdx[max] = tmp;
/*     */     } 
/*  79 */     return freqIdx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int[] sort(int[] sortVal, boolean asc) {
/*  89 */     return qSort(sortVal, asc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int[] sort(List<Integer> sortVal, boolean asc) {
/*  99 */     return qSort(sortVal, asc);
/*     */   }
/*     */   
/*     */   public static int[] sortString(List<String> sortVal, boolean asc) {
/* 103 */     return qSortString(sortVal, asc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int[] sortLong(List<Long> sortVal, boolean asc) {
/* 113 */     return qSortLong(sortVal, asc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int[] sortDesc(List<Double> sortVal) {
/* 122 */     return qSortDouble(sortVal, false);
/*     */   }
/*     */   
/* 125 */   private static long count = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[] qSort(List<Integer> l, boolean asc) {
/* 134 */     count = 0L;
/* 135 */     int[] idx = new int[l.size()];
/* 136 */     List<Integer> idxList = new ArrayList<>(); int i;
/* 137 */     for (i = 0; i < l.size(); i++) {
/* 138 */       idxList.add(Integer.valueOf(i));
/*     */     }
/* 140 */     idxList = qSort(l, idxList, asc);
/* 141 */     for (i = 0; i < l.size(); i++) {
/* 142 */       idx[i] = ((Integer)idxList.get(i)).intValue();
/*     */     }
/* 144 */     return idx;
/*     */   }
/*     */   
/*     */   private static int[] qSortString(List<String> l, boolean asc) {
/* 148 */     count = 0L;
/* 149 */     int[] idx = new int[l.size()];
/* 150 */     List<Integer> idxList = new ArrayList<>(); int i;
/* 151 */     for (i = 0; i < l.size(); i++)
/* 152 */       idxList.add(Integer.valueOf(i)); 
/* 153 */     System.out.print("Sorting...");
/* 154 */     idxList = qSortString(l, idxList, asc);
/* 155 */     for (i = 0; i < l.size(); i++)
/* 156 */       idx[i] = ((Integer)idxList.get(i)).intValue(); 
/* 157 */     System.out.println("[Done.]");
/* 158 */     return idx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[] qSortLong(List<Long> l, boolean asc) {
/* 168 */     count = 0L;
/* 169 */     int[] idx = new int[l.size()];
/* 170 */     List<Integer> idxList = new ArrayList<>(); int i;
/* 171 */     for (i = 0; i < l.size(); i++)
/* 172 */       idxList.add(Integer.valueOf(i)); 
/* 173 */     System.out.print("Sorting...");
/* 174 */     idxList = qSortLong(l, idxList, asc);
/* 175 */     for (i = 0; i < l.size(); i++)
/* 176 */       idx[i] = ((Integer)idxList.get(i)).intValue(); 
/* 177 */     System.out.println("[Done.]");
/* 178 */     return idx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[] qSortDouble(List<Double> l, boolean asc) {
/* 188 */     count = 0L;
/* 189 */     int[] idx = new int[l.size()];
/* 190 */     List<Integer> idxList = new ArrayList<>(); int i;
/* 191 */     for (i = 0; i < l.size(); i++) {
/* 192 */       idxList.add(Integer.valueOf(i));
/*     */     }
/* 194 */     idxList = qSortDouble(l, idxList, asc);
/* 195 */     for (i = 0; i < l.size(); i++) {
/* 196 */       idx[i] = ((Integer)idxList.get(i)).intValue();
/*     */     }
/* 198 */     return idx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[] qSort(int[] l, boolean asc) {
/* 208 */     count = 0L;
/* 209 */     int[] idx = new int[l.length];
/* 210 */     List<Integer> idxList = new ArrayList<>(); int i;
/* 211 */     for (i = 0; i < l.length; i++) {
/* 212 */       idxList.add(Integer.valueOf(i));
/*     */     }
/* 214 */     idxList = qSort(l, idxList, asc);
/* 215 */     for (i = 0; i < l.length; i++) {
/* 216 */       idx[i] = ((Integer)idxList.get(i)).intValue();
/*     */     }
/* 218 */     return idx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static List<Integer> qSort(List<Integer> l, List<Integer> idxList, boolean asc) {
/* 229 */     int mid = idxList.size() / 2;
/* 230 */     List<Integer> left = new ArrayList<>();
/* 231 */     List<Integer> right = new ArrayList<>();
/* 232 */     List<Integer> pivot = new ArrayList<>();
/* 233 */     for (int i = 0; i < idxList.size(); i++) {
/*     */       
/* 235 */       if (((Integer)l.get(((Integer)idxList.get(i)).intValue())).intValue() > ((Integer)l.get(((Integer)idxList.get(mid)).intValue())).intValue()) {
/*     */         
/* 237 */         if (asc) {
/* 238 */           right.add(idxList.get(i));
/*     */         } else {
/* 240 */           left.add(idxList.get(i));
/*     */         } 
/* 242 */       } else if (((Integer)l.get(((Integer)idxList.get(i)).intValue())).intValue() < ((Integer)l.get(((Integer)idxList.get(mid)).intValue())).intValue()) {
/*     */         
/* 244 */         if (asc) {
/* 245 */           left.add(idxList.get(i));
/*     */         } else {
/* 247 */           right.add(idxList.get(i));
/*     */         } 
/*     */       } else {
/* 250 */         pivot.add(idxList.get(i));
/*     */       } 
/* 252 */     }  count++;
/* 253 */     if (left.size() > 1)
/* 254 */       left = qSort(l, left, asc); 
/* 255 */     count++;
/* 256 */     if (right.size() > 1)
/* 257 */       right = qSort(l, right, asc); 
/* 258 */     List<Integer> newIdx = new ArrayList<>();
/* 259 */     newIdx.addAll(left);
/* 260 */     newIdx.addAll(pivot);
/* 261 */     newIdx.addAll(right);
/* 262 */     return newIdx;
/*     */   }
/*     */   
/*     */   private static List<Integer> qSortString(List<String> l, List<Integer> idxList, boolean asc) {
/* 266 */     int mid = idxList.size() / 2;
/* 267 */     List<Integer> left = new ArrayList<>();
/* 268 */     List<Integer> right = new ArrayList<>();
/* 269 */     List<Integer> pivot = new ArrayList<>();
/* 270 */     for (int i = 0; i < idxList.size(); i++) {
/*     */       
/* 272 */       if (((String)l.get(((Integer)idxList.get(i)).intValue())).compareTo(l.get(((Integer)idxList.get(mid)).intValue())) > 0) {
/*     */         
/* 274 */         if (asc) {
/* 275 */           right.add(idxList.get(i));
/*     */         } else {
/* 277 */           left.add(idxList.get(i));
/*     */         } 
/* 279 */       } else if (((String)l.get(((Integer)idxList.get(i)).intValue())).compareTo(l.get(((Integer)idxList.get(mid)).intValue())) < 0) {
/*     */         
/* 281 */         if (asc) {
/* 282 */           left.add(idxList.get(i));
/*     */         } else {
/* 284 */           right.add(idxList.get(i));
/*     */         } 
/*     */       } else {
/* 287 */         pivot.add(idxList.get(i));
/*     */       } 
/* 289 */     }  count++;
/* 290 */     if (left.size() > 1)
/* 291 */       left = qSortString(l, left, asc); 
/* 292 */     count++;
/* 293 */     if (right.size() > 1)
/* 294 */       right = qSortString(l, right, asc); 
/* 295 */     List<Integer> newIdx = new ArrayList<>();
/* 296 */     newIdx.addAll(left);
/* 297 */     newIdx.addAll(pivot);
/* 298 */     newIdx.addAll(right);
/* 299 */     return newIdx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static List<Integer> qSort(int[] l, List<Integer> idxList, boolean asc) {
/* 310 */     int mid = idxList.size() / 2;
/* 311 */     List<Integer> left = new ArrayList<>();
/* 312 */     List<Integer> right = new ArrayList<>();
/* 313 */     List<Integer> pivot = new ArrayList<>();
/* 314 */     for (int i = 0; i < idxList.size(); i++) {
/*     */       
/* 316 */       if (l[((Integer)idxList.get(i)).intValue()] > l[((Integer)idxList.get(mid)).intValue()]) {
/*     */         
/* 318 */         if (asc) {
/* 319 */           right.add(idxList.get(i));
/*     */         } else {
/* 321 */           left.add(idxList.get(i));
/*     */         } 
/* 323 */       } else if (l[((Integer)idxList.get(i)).intValue()] < l[((Integer)idxList.get(mid)).intValue()]) {
/*     */         
/* 325 */         if (asc) {
/* 326 */           left.add(idxList.get(i));
/*     */         } else {
/* 328 */           right.add(idxList.get(i));
/*     */         } 
/*     */       } else {
/* 331 */         pivot.add(idxList.get(i));
/*     */       } 
/* 333 */     }  count++;
/* 334 */     if (left.size() > 1)
/* 335 */       left = qSort(l, left, asc); 
/* 336 */     count++;
/* 337 */     if (right.size() > 1)
/* 338 */       right = qSort(l, right, asc); 
/* 339 */     List<Integer> newIdx = new ArrayList<>();
/* 340 */     newIdx.addAll(left);
/* 341 */     newIdx.addAll(pivot);
/* 342 */     newIdx.addAll(right);
/* 343 */     return newIdx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static List<Integer> qSortDouble(List<Double> l, List<Integer> idxList, boolean asc) {
/* 354 */     int mid = idxList.size() / 2;
/* 355 */     List<Integer> left = new ArrayList<>();
/* 356 */     List<Integer> right = new ArrayList<>();
/* 357 */     List<Integer> pivot = new ArrayList<>();
/* 358 */     for (int i = 0; i < idxList.size(); i++) {
/*     */       
/* 360 */       if (((Double)l.get(((Integer)idxList.get(i)).intValue())).doubleValue() > ((Double)l.get(((Integer)idxList.get(mid)).intValue())).doubleValue()) {
/*     */         
/* 362 */         if (asc) {
/* 363 */           right.add(idxList.get(i));
/*     */         } else {
/* 365 */           left.add(idxList.get(i));
/*     */         } 
/* 367 */       } else if (((Double)l.get(((Integer)idxList.get(i)).intValue())).doubleValue() < ((Double)l.get(((Integer)idxList.get(mid)).intValue())).doubleValue()) {
/*     */         
/* 369 */         if (asc) {
/* 370 */           left.add(idxList.get(i));
/*     */         } else {
/* 372 */           right.add(idxList.get(i));
/*     */         } 
/*     */       } else {
/* 375 */         pivot.add(idxList.get(i));
/*     */       } 
/* 377 */     }  count++;
/* 378 */     if (left.size() > 1)
/* 379 */       left = qSortDouble(l, left, asc); 
/* 380 */     count++;
/* 381 */     if (right.size() > 1)
/* 382 */       right = qSortDouble(l, right, asc); 
/* 383 */     List<Integer> newIdx = new ArrayList<>();
/* 384 */     newIdx.addAll(left);
/* 385 */     newIdx.addAll(pivot);
/* 386 */     newIdx.addAll(right);
/* 387 */     return newIdx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static List<Integer> qSortLong(List<Long> l, List<Integer> idxList, boolean asc) {
/* 398 */     int mid = idxList.size() / 2;
/* 399 */     List<Integer> left = new ArrayList<>();
/* 400 */     List<Integer> right = new ArrayList<>();
/* 401 */     List<Integer> pivot = new ArrayList<>();
/* 402 */     for (int i = 0; i < idxList.size(); i++) {
/*     */       
/* 404 */       if (((Long)l.get(((Integer)idxList.get(i)).intValue())).longValue() > ((Long)l.get(((Integer)idxList.get(mid)).intValue())).longValue()) {
/*     */         
/* 406 */         if (asc) {
/* 407 */           right.add(idxList.get(i));
/*     */         } else {
/* 409 */           left.add(idxList.get(i));
/*     */         } 
/* 411 */       } else if (((Long)l.get(((Integer)idxList.get(i)).intValue())).longValue() < ((Long)l.get(((Integer)idxList.get(mid)).intValue())).longValue()) {
/*     */         
/* 413 */         if (asc) {
/* 414 */           left.add(idxList.get(i));
/*     */         } else {
/* 416 */           right.add(idxList.get(i));
/*     */         } 
/*     */       } else {
/* 419 */         pivot.add(idxList.get(i));
/*     */       } 
/* 421 */     }  count++;
/* 422 */     if (left.size() > 1)
/* 423 */       left = qSortLong(l, left, asc); 
/* 424 */     count++;
/* 425 */     if (right.size() > 1)
/* 426 */       right = qSortLong(l, right, asc); 
/* 427 */     List<Integer> newIdx = new ArrayList<>();
/* 428 */     newIdx.addAll(left);
/* 429 */     newIdx.addAll(pivot);
/* 430 */     newIdx.addAll(right);
/* 431 */     return newIdx;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\ed\\utilities\Sorter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */