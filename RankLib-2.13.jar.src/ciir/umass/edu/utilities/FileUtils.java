/*     */ package ciir.umass.edu.utilities;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileUtils
/*     */ {
/*     */   public static BufferedReader smartReader(String inputFile) throws IOException {
/*  26 */     return smartReader(inputFile, "UTF-8");
/*     */   }
/*     */   public static BufferedReader smartReader(String inputFile, String encoding) throws IOException {
/*  29 */     InputStream input = new FileInputStream(inputFile);
/*  30 */     if (inputFile.endsWith(".gz")) {
/*  31 */       input = new GZIPInputStream(input);
/*     */     }
/*  33 */     return new BufferedReader(new InputStreamReader(input, encoding));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String read(String filename, String encoding) {
/*  45 */     StringBuffer content = new StringBuffer();
/*  46 */     try (BufferedReader in = smartReader(filename, encoding)) {
/*  47 */       char[] newContent = new char[40960];
/*  48 */       int numRead = -1;
/*  49 */       while ((numRead = in.read(newContent)) != -1)
/*     */       {
/*     */         
/*  52 */         content.append(new String(newContent, 0, numRead));
/*     */       }
/*     */     }
/*  55 */     catch (Exception e) {
/*     */ 
/*     */       
/*  58 */       content = new StringBuffer();
/*     */     } 
/*     */     
/*  61 */     return content.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static List<String> readLine(String filename, String encoding) {
/*  66 */     List<String> lines = new ArrayList<>();
/*     */     try {
/*  68 */       String content = "";
/*  69 */       BufferedReader in = smartReader(filename, encoding);
/*     */       
/*  71 */       while ((content = in.readLine()) != null) {
/*     */         
/*  73 */         content = content.trim();
/*  74 */         if (content.length() == 0)
/*     */           continue; 
/*  76 */         lines.add(content);
/*     */       } 
/*  78 */       in.close();
/*     */     }
/*  80 */     catch (Exception ex) {
/*     */       
/*  82 */       throw RankLibError.create(ex);
/*     */     } 
/*  84 */     return lines;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean write(String filename, String encoding, String strToWrite) {
/*  95 */     BufferedWriter out = null;
/*     */     
/*     */     try {
/*  98 */       out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), encoding));
/*     */       
/* 100 */       out.write(strToWrite);
/* 101 */       out.close();
/*     */     }
/* 103 */     catch (Exception e) {
/*     */       
/* 105 */       return false;
/*     */     } 
/* 107 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String[] getAllFiles(String directory) {
/* 116 */     File dir = new File(directory);
/* 117 */     String[] fns = dir.list();
/* 118 */     return fns;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<String> getAllFiles2(String directory) {
/* 127 */     File dir = new File(directory);
/* 128 */     String[] fns = dir.list();
/* 129 */     List<String> files = new ArrayList<>();
/* 130 */     if (fns != null)
/* 131 */       for (int i = 0; i < fns.length; i++)
/* 132 */         files.add(fns[i]);  
/* 133 */     return files;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getFileName(String pathName) {
/* 138 */     int idx1 = pathName.lastIndexOf("/");
/* 139 */     int idx2 = pathName.lastIndexOf("\\");
/* 140 */     int idx = (idx1 > idx2) ? idx1 : idx2;
/* 141 */     return pathName.substring(idx + 1);
/*     */   }
/*     */   
/*     */   public static String makePathStandard(String directory) {
/* 145 */     String dir = directory;
/* 146 */     char c = dir.charAt(dir.length() - 1);
/* 147 */     if (c != '/' && c != '\\')
/*     */     {
/*     */ 
/*     */       
/* 151 */       dir = dir + File.separator; } 
/* 152 */     return dir;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\ed\\utilities\FileUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */