/*     */ package ciir.umass.edu.learning.tree;
/*     */ 
/*     */ import ciir.umass.edu.learning.DataPoint;
/*     */ import ciir.umass.edu.utilities.RankLibError;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
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
/*     */ public class Ensemble
/*     */ {
/*  29 */   protected List<RegressionTree> trees = null;
/*  30 */   protected List<Float> weights = null;
/*  31 */   protected int[] features = null;
/*     */ 
/*     */   
/*     */   public Ensemble() {
/*  35 */     this.trees = new ArrayList<>();
/*  36 */     this.weights = new ArrayList<>();
/*     */   }
/*     */   
/*     */   public Ensemble(Ensemble e) {
/*  40 */     this.trees = new ArrayList<>();
/*  41 */     this.weights = new ArrayList<>();
/*  42 */     this.trees.addAll(e.trees);
/*  43 */     this.weights.addAll(e.weights);
/*     */   }
/*     */   
/*     */   public Ensemble(String xmlRep) {
/*     */     try {
/*  48 */       this.trees = new ArrayList<>();
/*  49 */       this.weights = new ArrayList<>();
/*  50 */       DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
/*  51 */       DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
/*  52 */       byte[] xmlDATA = xmlRep.getBytes();
/*  53 */       ByteArrayInputStream in = new ByteArrayInputStream(xmlDATA);
/*  54 */       Document doc = dBuilder.parse(in);
/*  55 */       NodeList nl = doc.getElementsByTagName("tree");
/*  56 */       HashMap<Integer, Integer> fids = new HashMap<>(); int i;
/*  57 */       for (i = 0; i < nl.getLength(); i++) {
/*     */         
/*  59 */         Node n = nl.item(i);
/*     */         
/*  61 */         Split root = create(n.getFirstChild(), fids);
/*     */         
/*  63 */         float weight = Float.parseFloat(n.getAttributes().getNamedItem("weight").getNodeValue());
/*     */         
/*  65 */         this.trees.add(new RegressionTree(root));
/*  66 */         this.weights.add(Float.valueOf(weight));
/*     */       } 
/*  68 */       this.features = new int[fids.keySet().size()];
/*  69 */       i = 0;
/*  70 */       for (Integer fid : fids.keySet()) {
/*  71 */         this.features[i++] = fid.intValue();
/*     */       }
/*  73 */     } catch (Exception ex) {
/*     */       
/*  75 */       throw RankLibError.create("Error in Emsemble(xmlRepresentation): ", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(RegressionTree tree, float weight) {
/*  81 */     this.trees.add(tree);
/*  82 */     this.weights.add(Float.valueOf(weight));
/*     */   }
/*     */   
/*     */   public RegressionTree getTree(int k) {
/*  86 */     return this.trees.get(k);
/*     */   }
/*     */   
/*     */   public float getWeight(int k) {
/*  90 */     return ((Float)this.weights.get(k)).floatValue();
/*     */   }
/*     */   
/*     */   public double variance() {
/*  94 */     double var = 0.0D;
/*  95 */     for (RegressionTree tree : this.trees) var += tree.variance(); 
/*  96 */     return var;
/*     */   }
/*     */   
/*     */   public void remove(int k) {
/* 100 */     this.trees.remove(k);
/* 101 */     this.weights.remove(k);
/*     */   }
/*     */   
/*     */   public int treeCount() {
/* 105 */     return this.trees.size();
/*     */   }
/*     */   
/*     */   public int leafCount() {
/* 109 */     int count = 0;
/* 110 */     for (RegressionTree tree : this.trees) count += tree.leaves().size(); 
/* 111 */     return count;
/*     */   }
/*     */   
/*     */   public float eval(DataPoint dp) {
/* 115 */     float s = 0.0F;
/* 116 */     for (int i = 0; i < this.trees.size(); i++)
/* 117 */       s = (float)(s + ((RegressionTree)this.trees.get(i)).eval(dp) * ((Float)this.weights.get(i)).floatValue()); 
/* 118 */     return s;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 122 */     String strRep = "<ensemble>\n";
/* 123 */     for (int i = 0; i < this.trees.size(); i++) {
/*     */       
/* 125 */       strRep = strRep + "\t<tree id=\"" + (i + 1) + "\" weight=\"" + this.weights.get(i) + "\">\n";
/* 126 */       strRep = strRep + ((RegressionTree)this.trees.get(i)).toString("\t\t");
/* 127 */       strRep = strRep + "\t</tree>\n";
/*     */     } 
/* 129 */     strRep = strRep + "</ensemble>\n";
/* 130 */     return strRep;
/*     */   }
/*     */   
/*     */   public int[] getFeatures() {
/* 134 */     return this.features;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Split create(Node n, HashMap<Integer, Integer> fids) {
/* 144 */     Split s = null;
/* 145 */     if (n.getFirstChild().getNodeName().compareToIgnoreCase("feature") == 0) {
/*     */       
/* 147 */       NodeList nl = n.getChildNodes();
/* 148 */       int fid = Integer.parseInt(nl.item(0).getFirstChild().getNodeValue().trim());
/* 149 */       fids.put(Integer.valueOf(fid), Integer.valueOf(0));
/* 150 */       float threshold = Float.parseFloat(nl.item(1).getFirstChild().getNodeValue().trim());
/* 151 */       s = new Split(fid, threshold, 0.0D);
/* 152 */       s.setLeft(create(nl.item(2), fids));
/* 153 */       s.setRight(create(nl.item(3), fids));
/*     */     }
/*     */     else {
/*     */       
/* 157 */       float output = Float.parseFloat(n.getFirstChild().getFirstChild().getNodeValue().trim());
/* 158 */       s = new Split();
/* 159 */       s.setOutput(output);
/*     */     } 
/* 161 */     return s;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\learning\tree\Ensemble.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */