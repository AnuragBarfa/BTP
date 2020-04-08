/*    */ package ciir.umass.edu.learning;
/*    */ 
/*    */ import ciir.umass.edu.learning.tree.Ensemble;
/*    */ import ciir.umass.edu.learning.tree.RFRanker;
/*    */ import ciir.umass.edu.utilities.FileUtils;
/*    */ import java.io.BufferedWriter;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.OutputStreamWriter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Combiner
/*    */ {
/*    */   public static void main(String[] args) {
/* 23 */     Combiner c = new Combiner();
/* 24 */     c.combine(args[0], args[1]);
/*    */   }
/*    */   
/*    */   public void combine(String directory, String outputFile) {
/* 28 */     RankerFactory rf = new RankerFactory();
/* 29 */     String[] fns = FileUtils.getAllFiles(directory);
/* 30 */     BufferedWriter out = null;
/*    */     try {
/* 32 */       out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "ASCII"));
/* 33 */       out.write("## " + (new RFRanker()).name() + "\n");
/* 34 */       for (int i = 0; i < fns.length; i++) {
/*    */         
/* 36 */         if (fns[i].indexOf(".progress") == -1) {
/*    */           
/* 38 */           String fn = directory + fns[i];
/* 39 */           RFRanker r = (RFRanker)rf.loadRankerFromFile(fn);
/* 40 */           Ensemble en = r.getEnsembles()[0];
/* 41 */           out.write(en.toString());
/*    */         } 
/* 43 */       }  out.close();
/*    */     }
/* 45 */     catch (Exception e) {
/*    */       
/* 47 */       System.out.println("Error in Combiner::combine(): " + e.toString());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\learning\Combiner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */