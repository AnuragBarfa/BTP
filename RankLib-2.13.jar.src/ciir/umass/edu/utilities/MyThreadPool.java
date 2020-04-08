/*     */ package ciir.umass.edu.utilities;
/*     */ 
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.Semaphore;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public class MyThreadPool
/*     */   extends ThreadPoolExecutor
/*     */ {
/*     */   private final Semaphore semaphore;
/*  25 */   private int size = 0;
/*     */ 
/*     */   
/*     */   private MyThreadPool(int size) {
/*  29 */     super(size, size, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
/*  30 */     this.semaphore = new Semaphore(size, true);
/*  31 */     this.size = size;
/*     */   }
/*     */   
/*  34 */   private static MyThreadPool singleton = null;
/*     */   
/*     */   public static MyThreadPool getInstance() {
/*  37 */     if (singleton == null)
/*  38 */       init(Runtime.getRuntime().availableProcessors()); 
/*  39 */     return singleton;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void init(int poolSize) {
/*  44 */     singleton = new MyThreadPool(poolSize);
/*     */   }
/*     */   
/*     */   public int size() {
/*  48 */     return this.size;
/*     */   }
/*     */   
/*     */   public WorkerThread[] execute(WorkerThread worker, int nTasks) {
/*  52 */     MyThreadPool p = getInstance();
/*  53 */     int[] partition = p.partition(nTasks);
/*  54 */     WorkerThread[] workers = new WorkerThread[partition.length - 1];
/*  55 */     for (int i = 0; i < partition.length - 1; i++) {
/*     */       
/*  57 */       WorkerThread w = worker.clone();
/*  58 */       w.set(partition[i], partition[i + 1] - 1);
/*  59 */       workers[i] = w;
/*  60 */       p.execute(w);
/*     */     } 
/*  62 */     await();
/*  63 */     return workers;
/*     */   }
/*     */   
/*     */   public void await() {
/*     */     int i;
/*  68 */     for (i = 0; i < this.size; i++) {
/*     */       
/*     */       try {
/*  71 */         this.semaphore.acquire();
/*     */       }
/*  73 */       catch (Exception ex) {
/*     */         
/*  75 */         throw RankLibError.create("Error in MyThreadPool.await(): ", ex);
/*     */       } 
/*     */     } 
/*  78 */     for (i = 0; i < this.size; i++)
/*  79 */       this.semaphore.release(); 
/*     */   }
/*     */   
/*     */   public int[] partition(int listSize) {
/*  83 */     int nChunks = Math.min(listSize, this.size);
/*  84 */     int chunkSize = listSize / nChunks;
/*  85 */     int mod = listSize % nChunks;
/*  86 */     int[] partition = new int[nChunks + 1];
/*  87 */     partition[0] = 0;
/*  88 */     for (int i = 1; i <= nChunks; i++)
/*  89 */       partition[i] = partition[i - 1] + chunkSize + ((i <= mod) ? 1 : 0); 
/*  90 */     return partition;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(Runnable task) {
/*     */     try {
/*  97 */       this.semaphore.acquire();
/*  98 */       super.execute(task);
/*     */     }
/* 100 */     catch (Exception ex) {
/*     */       
/* 102 */       throw RankLibError.create("Error in MyThreadPool.execute(): ", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void afterExecute(Runnable r, Throwable t) {
/* 107 */     super.afterExecute(r, t);
/* 108 */     this.semaphore.release();
/*     */   }
/*     */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\ed\\utilities\MyThreadPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */