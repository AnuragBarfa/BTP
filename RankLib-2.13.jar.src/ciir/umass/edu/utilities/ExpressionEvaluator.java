/*     */ package ciir.umass.edu.utilities;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
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
/*     */ public class ExpressionEvaluator
/*     */ {
/*     */   public static void main(String[] args) {
/*  23 */     ExpressionEvaluator ev = new ExpressionEvaluator();
/*  24 */     String exp = "sqrt(16)/exp(4^2)";
/*  25 */     System.out.println(ev.getRPN(exp) + "");
/*  26 */     System.out.println(ev.eval(exp) + "");
/*     */   }
/*     */   
/*     */   class Queue {
/*  30 */     private List<String> l = new ArrayList<>();
/*     */     
/*     */     public void enqueue(String s) {
/*  33 */       this.l.add(s);
/*     */     }
/*     */     
/*     */     public String dequeue() {
/*  37 */       if (this.l.size() == 0)
/*  38 */         return ""; 
/*  39 */       String s = this.l.get(0);
/*  40 */       this.l.remove(0);
/*  41 */       return s;
/*     */     }
/*     */     
/*     */     public int size() {
/*  45 */       return this.l.size();
/*     */     }
/*     */     
/*     */     public String toString() {
/*  49 */       String output = "";
/*  50 */       for (int i = 0; i < this.l.size(); i++)
/*  51 */         output = output + (String)this.l.get(i) + " "; 
/*  52 */       return output.trim();
/*     */     } }
/*     */   
/*     */   class Stack {
/*  56 */     private List<String> l = new ArrayList<>();
/*     */     
/*     */     public void push(String s) {
/*  59 */       this.l.add(s);
/*     */     }
/*     */     
/*     */     public String pop() {
/*  63 */       if (this.l.size() == 0)
/*  64 */         return ""; 
/*  65 */       String s = this.l.get(this.l.size() - 1);
/*  66 */       this.l.remove(this.l.size() - 1);
/*  67 */       return s;
/*     */     }
/*     */     
/*     */     public int size() {
/*  71 */       return this.l.size();
/*     */     }
/*     */     
/*     */     public String toString() {
/*  75 */       String output = "";
/*  76 */       for (int i = this.l.size() - 1; i >= 0; i--)
/*  77 */         output = output + (String)this.l.get(i) + " "; 
/*  78 */       return output.trim();
/*     */     }
/*     */   }
/*     */   
/*  82 */   private static String[] operators = new String[] { "+", "-", "*", "/", "^" };
/*  83 */   private static String[] functions = new String[] { "log", "ln", "log2", "exp", "sqrt", "neg" };
/*  84 */   private static HashMap<String, Integer> priority = null;
/*     */ 
/*     */   
/*     */   private boolean isOperator(String token) {
/*  88 */     for (int i = 0; i < operators.length; i++) {
/*  89 */       if (token.compareTo(operators[i]) == 0)
/*  90 */         return true; 
/*  91 */     }  return false;
/*     */   }
/*     */   
/*     */   private boolean isFunction(String token) {
/*  95 */     for (int i = 0; i < functions.length; i++) {
/*  96 */       if (token.compareTo(functions[i]) == 0)
/*  97 */         return true; 
/*  98 */     }  return false;
/*     */   }
/*     */   
/*     */   private Queue toPostFix(String expression) {
/* 102 */     expression = expression.replace(" ", "");
/* 103 */     Queue output = new Queue();
/* 104 */     Stack op = new Stack();
/* 105 */     String lastReadToken = "";
/* 106 */     for (int i = 0; i < expression.length(); i++) {
/*     */       
/* 108 */       String token = expression.charAt(i) + "";
/* 109 */       if (token.compareTo("(") == 0) {
/* 110 */         op.push(token);
/* 111 */       } else if (token.compareTo(")") == 0) {
/*     */         
/* 113 */         boolean foundOpen = false;
/* 114 */         while (op.size() > 0 && !foundOpen) {
/*     */           
/* 116 */           String last = op.pop();
/* 117 */           if (last.compareTo("(") != 0) {
/* 118 */             output.enqueue(last); continue;
/*     */           } 
/* 120 */           foundOpen = true;
/*     */         } 
/* 122 */         if (!foundOpen)
/*     */         {
/* 124 */           throw RankLibError.create("Error: Invalid expression: \"" + expression + "\". Parentheses mismatched.");
/*     */         }
/*     */       }
/* 127 */       else if (isOperator(token)) {
/*     */         
/* 129 */         if (lastReadToken.compareTo("(") == 0 || isOperator(lastReadToken))
/*     */         {
/* 131 */           if (token.compareTo("-") == 0) {
/* 132 */             op.push("neg");
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 137 */           if (op.size() > 0) {
/*     */             
/* 139 */             String last = op.pop();
/* 140 */             if (last.compareTo("(") == 0) {
/* 141 */               op.push(last);
/* 142 */             } else if (((Integer)priority.get(token)).intValue() > ((Integer)priority.get(last)).intValue()) {
/* 143 */               op.push(last);
/* 144 */             } else if (((Integer)priority.get(token)).intValue() < ((Integer)priority.get(last)).intValue()) {
/* 145 */               output.enqueue(last);
/*     */             
/*     */             }
/* 148 */             else if (token.compareTo("^") == 0) {
/* 149 */               op.push(last);
/*     */             } else {
/* 151 */               output.enqueue(last);
/*     */             } 
/*     */           } 
/* 154 */           op.push(token);
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 159 */         int j = i + 1;
/* 160 */         while (j < expression.length()) {
/*     */           
/* 162 */           String next = expression.charAt(j) + "";
/* 163 */           if (next.compareTo(")") == 0 || next.compareTo("(") == 0 || isOperator(next)) {
/*     */             break;
/*     */           }
/*     */           
/* 167 */           token = token + next;
/* 168 */           j++;
/*     */         } 
/*     */         
/* 171 */         i = j - 1;
/*     */         
/* 173 */         if (isFunction(token)) {
/*     */           
/* 175 */           if (j == expression.length())
/*     */           {
/* 177 */             throw RankLibError.create("Error: Invalid expression: \"" + expression + "\". Function specification requires parentheses.");
/*     */           }
/* 179 */           if (expression.charAt(j) != '(')
/*     */           {
/* 181 */             throw RankLibError.create("Error: Invalid expression: \"" + expression + "\". Function specification requires parentheses.");
/*     */           }
/* 183 */           op.push(token);
/*     */         } else {
/*     */ 
/*     */           
/*     */           try {
/* 188 */             Double.parseDouble(token);
/*     */           }
/* 190 */           catch (Exception ex) {
/*     */             
/* 192 */             throw RankLibError.create("Error: \"" + token + "\" is not a valid token.");
/*     */           } 
/* 194 */           output.enqueue(token);
/*     */         } 
/*     */       } 
/* 197 */       lastReadToken = token;
/*     */     } 
/* 199 */     while (op.size() > 0) {
/*     */       
/* 201 */       String last = op.pop();
/* 202 */       if (last.compareTo("(") == 0)
/*     */       {
/* 204 */         throw RankLibError.create("Error: Invalid expression: \"" + expression + "\". Parentheses mismatched.");
/*     */       }
/* 206 */       output.enqueue(last);
/*     */     } 
/* 208 */     return output;
/*     */   }
/*     */ 
/*     */   
/*     */   public ExpressionEvaluator() {
/* 213 */     if (priority == null) {
/*     */       
/* 215 */       priority = new HashMap<>();
/* 216 */       priority.put("+", Integer.valueOf(2));
/* 217 */       priority.put("-", Integer.valueOf(2));
/* 218 */       priority.put("*", Integer.valueOf(3));
/* 219 */       priority.put("/", Integer.valueOf(3));
/* 220 */       priority.put("^", Integer.valueOf(4));
/* 221 */       priority.put("neg", Integer.valueOf(5));
/* 222 */       priority.put("log", Integer.valueOf(6));
/* 223 */       priority.put("ln", Integer.valueOf(6));
/* 224 */       priority.put("sqrt", Integer.valueOf(6));
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getRPN(String expression) {
/* 229 */     return toPostFix(expression).toString();
/*     */   }
/*     */   
/*     */   public double eval(String expression) {
/* 233 */     Queue output = toPostFix(expression);
/* 234 */     double[] eval = new double[output.size()];
/* 235 */     int cp = 0;
/*     */     try {
/* 237 */       while (output.size() > 0) {
/*     */         
/* 239 */         String token = output.dequeue();
/* 240 */         double v = 0.0D;
/* 241 */         if (isOperator(token)) {
/*     */           
/* 243 */           if (token.compareTo("+") == 0) {
/* 244 */             v = eval[cp - 2] + eval[cp - 1];
/* 245 */           } else if (token.compareTo("-") == 0) {
/* 246 */             v = eval[cp - 2] + eval[cp - 1];
/* 247 */           } else if (token.compareTo("*") == 0) {
/* 248 */             v = eval[cp - 2] * eval[cp - 1];
/* 249 */           } else if (token.compareTo("/") == 0) {
/* 250 */             v = eval[cp - 2] / eval[cp - 1];
/* 251 */           } else if (token.compareTo("^") == 0) {
/* 252 */             v = Math.pow(eval[cp - 2], eval[cp - 1]);
/* 253 */           }  eval[cp - 2] = v;
/* 254 */           cp--; continue;
/*     */         } 
/* 256 */         if (isFunction(token)) {
/*     */           
/* 258 */           if (token.compareTo("log") == 0) {
/*     */             
/* 260 */             if (eval[cp - 1] < 0.0D)
/*     */             {
/* 262 */               throw RankLibError.create("Error: expression " + expression + " involves taking log of a non-positive number");
/*     */             }
/* 264 */             v = Math.log10(eval[cp - 1]);
/*     */           }
/* 266 */           else if (token.compareTo("ln") == 0) {
/*     */             
/* 268 */             if (eval[cp - 1] < 0.0D)
/*     */             {
/* 270 */               throw RankLibError.create("Error: expression " + expression + " involves taking log of a non-positive number");
/*     */             }
/* 272 */             v = Math.log(eval[cp - 1]);
/*     */           }
/* 274 */           else if (token.compareTo("log2") == 0) {
/*     */             
/* 276 */             if (eval[cp - 1] < 0.0D)
/*     */             {
/* 278 */               throw RankLibError.create("Error: expression " + expression + " involves taking log of a non-positive number");
/*     */             }
/* 280 */             v = Math.log(eval[cp - 1]) / Math.log(2.0D);
/*     */           }
/* 282 */           else if (token.compareTo("exp") == 0) {
/* 283 */             v = Math.exp(eval[cp - 1]);
/* 284 */           } else if (token.compareTo("sqrt") == 0) {
/*     */             
/* 286 */             if (eval[cp - 1] < 0.0D)
/*     */             {
/* 288 */               throw RankLibError.create("Error: expression " + expression + " involves taking square root of a negative number");
/*     */             }
/* 290 */             v = Math.sqrt(eval[cp - 1]);
/*     */           }
/* 292 */           else if (token.compareTo("neg") == 0) {
/* 293 */             v = -eval[cp - 1];
/* 294 */           }  eval[cp - 1] = v;
/*     */           continue;
/*     */         } 
/* 297 */         eval[cp++] = Double.parseDouble(token);
/*     */       } 
/* 299 */       if (cp != 1)
/*     */       {
/* 301 */         throw RankLibError.create("Error: invalid expression: " + expression);
/*     */       }
/*     */     }
/* 304 */     catch (Exception ex) {
/*     */       
/* 306 */       throw RankLibError.create("Unknown error in ExpressionEvaluator::eval() with \"" + expression + "\"", ex);
/*     */     } 
/* 308 */     return eval[cp - 1];
/*     */   }
/*     */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\ed\\utilities\ExpressionEvaluator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */