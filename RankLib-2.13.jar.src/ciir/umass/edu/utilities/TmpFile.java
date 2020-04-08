/*    */ package ciir.umass.edu.utilities;
/*    */ 
/*    */ import java.io.Closeable;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintWriter;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ 
/*    */ public class TmpFile
/*    */   implements Closeable
/*    */ {
/*    */   private final File fp;
/*    */   
/*    */   public TmpFile() throws IOException {
/* 17 */     this.fp = File.createTempFile("ranklib", "tmp");
/* 18 */     this.fp.deleteOnExit();
/*    */   }
/*    */   
/*    */   public File get() {
/* 22 */     return this.fp;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 27 */     if (!this.fp.delete()) {
/* 28 */       Logger.getAnonymousLogger().log(Level.WARNING, "Couldn't delete temporary file: " + this.fp.getAbsolutePath());
/*    */     }
/*    */   }
/*    */   
/*    */   public PrintWriter getWriter() throws IOException {
/* 33 */     return new PrintWriter(get());
/*    */   }
/*    */   
/*    */   public String getPath() {
/* 37 */     return this.fp.getAbsolutePath();
/*    */   }
/*    */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\ed\\utilities\TmpFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */