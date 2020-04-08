/*      */ package ciir.umass.edu.eval;
/*      */ 
/*      */ import ciir.umass.edu.features.FeatureManager;
/*      */ import ciir.umass.edu.features.LinearNormalizer;
/*      */ import ciir.umass.edu.features.Normalizer;
/*      */ import ciir.umass.edu.features.SumNormalizor;
/*      */ import ciir.umass.edu.features.ZScoreNormalizor;
/*      */ import ciir.umass.edu.learning.CoorAscent;
/*      */ import ciir.umass.edu.learning.DataPoint;
/*      */ import ciir.umass.edu.learning.LinearRegRank;
/*      */ import ciir.umass.edu.learning.RANKER_TYPE;
/*      */ import ciir.umass.edu.learning.RankList;
/*      */ import ciir.umass.edu.learning.Ranker;
/*      */ import ciir.umass.edu.learning.RankerFactory;
/*      */ import ciir.umass.edu.learning.RankerTrainer;
/*      */ import ciir.umass.edu.learning.boosting.AdaRank;
/*      */ import ciir.umass.edu.learning.boosting.RankBoost;
/*      */ import ciir.umass.edu.learning.neuralnet.ListNet;
/*      */ import ciir.umass.edu.learning.neuralnet.Neuron;
/*      */ import ciir.umass.edu.learning.neuralnet.RankNet;
/*      */ import ciir.umass.edu.learning.tree.LambdaMART;
/*      */ import ciir.umass.edu.learning.tree.RFRanker;
/*      */ import ciir.umass.edu.metric.ERRScorer;
/*      */ import ciir.umass.edu.metric.METRIC;
/*      */ import ciir.umass.edu.metric.MetricScorer;
/*      */ import ciir.umass.edu.metric.MetricScorerFactory;
/*      */ import ciir.umass.edu.utilities.FileUtils;
/*      */ import ciir.umass.edu.utilities.MergeSorter;
/*      */ import ciir.umass.edu.utilities.MyThreadPool;
/*      */ import ciir.umass.edu.utilities.RankLibError;
/*      */ import ciir.umass.edu.utilities.SimpleMath;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.text.DecimalFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Evaluator
/*      */ {
/*      */   public static void main(String[] args) {
/*   47 */     String[] rType = { "MART", "RankNet", "RankBoost", "AdaRank", "Coordinate Ascent", "LambdaRank", "LambdaMART", "ListNet", "Random Forests", "Linear Regression" };
/*      */ 
/*      */     
/*   50 */     RANKER_TYPE[] rType2 = { RANKER_TYPE.MART, RANKER_TYPE.RANKNET, RANKER_TYPE.RANKBOOST, RANKER_TYPE.ADARANK, RANKER_TYPE.COOR_ASCENT, RANKER_TYPE.LAMBDARANK, RANKER_TYPE.LAMBDAMART, RANKER_TYPE.LISTNET, RANKER_TYPE.RANDOM_FOREST, RANKER_TYPE.LINEAR_REGRESSION };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   56 */     String trainFile = "";
/*   57 */     String featureDescriptionFile = "";
/*   58 */     float ttSplit = 0.0F;
/*   59 */     float tvSplit = 0.0F;
/*   60 */     int foldCV = -1;
/*   61 */     String validationFile = "";
/*   62 */     String testFile = "";
/*   63 */     List<String> testFiles = new ArrayList<>();
/*   64 */     int rankerType = 4;
/*   65 */     String trainMetric = "ERR@10";
/*   66 */     String testMetric = "";
/*   67 */     normalize = false;
/*   68 */     String savedModelFile = "";
/*   69 */     List<String> savedModelFiles = new ArrayList<>();
/*   70 */     String kcvModelDir = "";
/*   71 */     String kcvModelFile = "";
/*   72 */     String rankFile = "";
/*   73 */     String prpFile = "";
/*      */     
/*   75 */     int nThread = -1;
/*      */     
/*   77 */     String indriRankingFile = "";
/*   78 */     String scoreFile = "";
/*      */     
/*   80 */     if (args.length < 2) {
/*      */       
/*   82 */       System.out.println("Usage: java -jar RankLib.jar <Params>");
/*   83 */       System.out.println("Params:");
/*   84 */       System.out.println("  [+] Training (+ tuning and evaluation)");
/*   85 */       System.out.println("\t-train <file>\t\tTraining data");
/*   86 */       System.out.println("\t-ranker <type>\t\tSpecify which ranking algorithm to use");
/*   87 */       System.out.println("\t\t\t\t0: MART (gradient boosted regression tree)");
/*   88 */       System.out.println("\t\t\t\t1: RankNet");
/*   89 */       System.out.println("\t\t\t\t2: RankBoost");
/*   90 */       System.out.println("\t\t\t\t3: AdaRank");
/*   91 */       System.out.println("\t\t\t\t4: Coordinate Ascent");
/*   92 */       System.out.println("\t\t\t\t6: LambdaMART");
/*   93 */       System.out.println("\t\t\t\t7: ListNet");
/*   94 */       System.out.println("\t\t\t\t8: Random Forests");
/*   95 */       System.out.println("\t\t\t\t9: Linear regression (L2 regularization)");
/*   96 */       System.out.println("\t[ -feature <file> ]\tFeature description file: list features to be considered by the learner, each on a separate line");
/*   97 */       System.out.println("\t\t\t\tIf not specified, all features will be used.");
/*      */       
/*   99 */       System.out.println("\t[ -metric2t <metric> ]\tMetric to optimize on the training data.  Supported: MAP, NDCG@k, DCG@k, P@k, RR@k, ERR@k (default=" + trainMetric + ")");
/*      */       
/*  101 */       System.out.println("\t[ -gmax <label> ]\tHighest judged relevance label. It affects the calculation of ERR (default=" + 
/*  102 */           (int)SimpleMath.logBase2(ERRScorer.MAX) + ", i.e. 5-point scale {0,1,2,3,4})");
/*  103 */       System.out.println("\t[ -qrel <file> ]\tTREC-style relevance judgment file. It only affects MAP and NDCG (default=unspecified)");
/*  104 */       System.out.println("\t[ -silent ]\t\tDo not print progress messages (which are printed by default)");
/*  105 */       System.out.println("\t[ -missingZero ]\tSubstitute zero for missing feature values rather than throwing an exception.");
/*      */       
/*  107 */       System.out.println("");
/*      */       
/*  109 */       System.out.println("\t[ -validate <file> ]\tSpecify if you want to tune your system on the validation data (default=unspecified)");
/*  110 */       System.out.println("\t\t\t\tIf specified, the final model will be the one that performs best on the validation data");
/*  111 */       System.out.println("\t[ -tvs <x \\in [0..1]> ]\tIf you don't have separate validation data, use this to set train-validation split to be (x)(1.0-x)");
/*      */       
/*  113 */       System.out.println("\t[ -save <model> ]\tSave the model learned (default=not-save)");
/*      */       
/*  115 */       System.out.println("");
/*  116 */       System.out.println("\t[ -test <file> ]\tSpecify if you want to evaluate the trained model on this data (default=unspecified)");
/*  117 */       System.out.println("\t[ -tts <x \\in [0..1]> ]\tSet train-test split to be (x)(1.0-x). -tts will override -tvs");
/*  118 */       System.out.println("\t[ -metric2T <metric> ]\tMetric to evaluate on the test data (default to the same as specified for -metric2t)");
/*      */       
/*  120 */       System.out.println("");
/*  121 */       System.out.println("\t[ -norm <method>]\tNormalize all feature vectors (default=no-normalization). Method can be:");
/*  122 */       System.out.println("\t\t\t\tsum: normalize each feature by the sum of all its values");
/*  123 */       System.out.println("\t\t\t\tzscore: normalize each feature by its mean/standard deviation");
/*  124 */       System.out.println("\t\t\t\tlinear: normalize each feature by its min/max values");
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  129 */       System.out.println("");
/*  130 */       System.out.println("\t[ -kcv <k> ]\t\tSpecify if you want to perform k-fold cross validation using the specified training data (default=NoCV)");
/*  131 */       System.out.println("\t\t\t\t-tvs can be used to further reserve a portion of the training data in each fold for validation");
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  136 */       System.out.println("\t[ -kcvmd <dir> ]\tDirectory for models trained via cross-validation (default=not-save)");
/*  137 */       System.out.println("\t[ -kcvmn <model> ]\tName for model learned in each fold. It will be prefix-ed with the fold-number (default=empty)");
/*      */       
/*  139 */       System.out.println("");
/*  140 */       System.out.println("    [-] RankNet-specific parameters");
/*  141 */       System.out.println("\t[ -epoch <T> ]\t\tThe number of epochs to train (default=" + RankNet.nIteration + ")");
/*  142 */       System.out.println("\t[ -layer <layer> ]\tThe number of hidden layers (default=" + RankNet.nHiddenLayer + ")");
/*  143 */       System.out.println("\t[ -node <node> ]\tThe number of hidden nodes per layer (default=" + RankNet.nHiddenNodePerLayer + ")");
/*  144 */       System.out.println("\t[ -lr <rate> ]\t\tLearning rate (default=" + (new DecimalFormat("###.########")).format(RankNet.learningRate) + ")");
/*      */       
/*  146 */       System.out.println("");
/*  147 */       System.out.println("    [-] RankBoost-specific parameters");
/*  148 */       System.out.println("\t[ -round <T> ]\t\tThe number of rounds to train (default=" + RankBoost.nIteration + ")");
/*  149 */       System.out.println("\t[ -tc <k> ]\t\tNumber of threshold candidates to search. -1 to use all feature values (default=" + RankBoost.nThreshold + ")");
/*      */ 
/*      */       
/*  152 */       System.out.println("");
/*  153 */       System.out.println("    [-] AdaRank-specific parameters");
/*  154 */       System.out.println("\t[ -round <T> ]\t\tThe number of rounds to train (default=" + AdaRank.nIteration + ")");
/*  155 */       System.out.println("\t[ -noeq ]\t\tTrain without enqueuing too-strong features (default=unspecified)");
/*  156 */       System.out.println("\t[ -tolerance <t> ]\tTolerance between two consecutive rounds of learning (default=" + AdaRank.tolerance + ")");
/*  157 */       System.out.println("\t[ -max <times> ]\tThe maximum number of times a feature can be consecutively selected without changing performance (default=" + AdaRank.maxSelCount + ")");
/*      */ 
/*      */       
/*  160 */       System.out.println("");
/*  161 */       System.out.println("    [-] Coordinate Ascent-specific parameters");
/*  162 */       System.out.println("\t[ -r <k> ]\t\tThe number of random restarts (default=" + CoorAscent.nRestart + ")");
/*  163 */       System.out.println("\t[ -i <iteration> ]\tThe number of iterations to search in each dimension (default=" + CoorAscent.nMaxIteration + ")");
/*  164 */       System.out.println("\t[ -tolerance <t> ]\tPerformance tolerance between two solutions (default=" + CoorAscent.tolerance + ")");
/*  165 */       System.out.println("\t[ -reg <slack> ]\tRegularization parameter (default=no-regularization)");
/*      */       
/*  167 */       System.out.println("");
/*  168 */       System.out.println("    [-] {MART, LambdaMART}-specific parameters");
/*  169 */       System.out.println("\t[ -tree <t> ]\t\tNumber of trees (default=" + LambdaMART.nTrees + ")");
/*  170 */       System.out.println("\t[ -leaf <l> ]\t\tNumber of leaves for each tree (default=" + LambdaMART.nTreeLeaves + ")");
/*  171 */       System.out.println("\t[ -shrinkage <factor> ]\tShrinkage, or learning rate (default=" + LambdaMART.learningRate + ")");
/*  172 */       System.out.println("\t[ -tc <k> ]\t\tNumber of threshold candidates for tree spliting. -1 to use all feature values (default=" + LambdaMART.nThreshold + ")");
/*      */       
/*  174 */       System.out.println("\t[ -mls <n> ]\t\tMin leaf support -- minimum % of docs each leaf has to contain (default=" + LambdaMART.minLeafSupport + ")");
/*      */       
/*  176 */       System.out.println("\t[ -estop <e> ]\t\tStop early when no improvement is observed on validaton data in e consecutive rounds (default=" + LambdaMART.nRoundToStopEarly + ")");
/*      */ 
/*      */       
/*  179 */       System.out.println("");
/*  180 */       System.out.println("    [-] ListNet-specific parameters");
/*  181 */       System.out.println("\t[ -epoch <T> ]\t\tThe number of epochs to train (default=" + ListNet.nIteration + ")");
/*  182 */       System.out.println("\t[ -lr <rate> ]\t\tLearning rate (default=" + (new DecimalFormat("###.########")).format(ListNet.learningRate) + ")");
/*      */       
/*  184 */       System.out.println("");
/*  185 */       System.out.println("    [-] Random Forests-specific parameters");
/*  186 */       System.out.println("\t[ -bag <r> ]\t\tNumber of bags (default=" + RFRanker.nBag + ")");
/*  187 */       System.out.println("\t[ -srate <r> ]\t\tSub-sampling rate (default=" + RFRanker.subSamplingRate + ")");
/*  188 */       System.out.println("\t[ -frate <r> ]\t\tFeature sampling rate (default=" + RFRanker.featureSamplingRate + ")");
/*  189 */       int type = RFRanker.rType.ordinal() - RANKER_TYPE.MART.ordinal();
/*  190 */       System.out.println("\t[ -rtype <type> ]\tRanker to bag (default=" + type + ", i.e. " + rType[type] + ")");
/*  191 */       System.out.println("\t[ -tree <t> ]\t\tNumber of trees in each bag (default=" + RFRanker.nTrees + ")");
/*  192 */       System.out.println("\t[ -leaf <l> ]\t\tNumber of leaves for each tree (default=" + RFRanker.nTreeLeaves + ")");
/*  193 */       System.out.println("\t[ -shrinkage <factor> ]\tShrinkage, or learning rate (default=" + RFRanker.learningRate + ")");
/*  194 */       System.out.println("\t[ -tc <k> ]\t\tNumber of threshold candidates for tree spliting. -1 to use all feature values (default=" + RFRanker.nThreshold + ")");
/*      */       
/*  196 */       System.out.println("\t[ -mls <n> ]\t\tMin leaf support -- minimum % of docs each leaf has to contain (default=" + RFRanker.minLeafSupport + ")");
/*      */       
/*  198 */       System.out.println("");
/*  199 */       System.out.println("    [-] Linear Regression-specific parameters");
/*  200 */       System.out.println("\t[ -L2 <reg> ]\t\tL2 regularization parameter (default=" + LinearRegRank.lambda + ")");
/*      */       
/*  202 */       System.out.println("");
/*  203 */       System.out.println("  [+] Testing previously saved models");
/*  204 */       System.out.println("\t-load <model>\t\tThe model to load");
/*  205 */       System.out.println("\t\t\t\tMultiple -load can be used to specify models from multiple folds (in increasing order),");
/*  206 */       System.out.println("\t\t\t\t  in which case the test/rank data will be partitioned accordingly.");
/*  207 */       System.out.println("\t-test <file>\t\tTest data to evaluate the model(s) (specify either this or -rank but not both)");
/*  208 */       System.out.println("\t-rank <file>\t\tRank the samples in the specified file (specify either this or -test but not both)");
/*  209 */       System.out.println("\t[ -metric2T <metric> ]\tMetric to evaluate on the test data (default=" + trainMetric + ")");
/*  210 */       System.out.println("\t[ -gmax <label> ]\tHighest judged relevance label. It affects the calculation of ERR (default=" + (int)SimpleMath.logBase2(ERRScorer.MAX) + ", i.e. 5-point scale {0,1,2,3,4})");
/*  211 */       System.out.println("\t[ -score <file>]\tStore ranker's score for each object being ranked (has to be used with -rank)");
/*  212 */       System.out.println("\t[ -qrel <file> ]\tTREC-style relevance judgment file. It only affects MAP and NDCG (default=unspecified)");
/*  213 */       System.out.println("\t[ -idv <file> ]\t\tSave model performance (in test metric) on individual ranked lists (has to be used with -test)");
/*  214 */       System.out.println("\t[ -norm ]\t\tNormalize feature vectors (similar to -norm for training/tuning)");
/*      */ 
/*      */       
/*  217 */       System.out.println("");
/*      */       
/*      */       return;
/*      */     } 
/*  221 */     for (int i = 0; i < args.length; i++) {
/*      */       
/*  223 */       if (args[i].equalsIgnoreCase("-train")) {
/*  224 */         trainFile = args[++i];
/*  225 */       } else if (args[i].equalsIgnoreCase("-ranker")) {
/*  226 */         rankerType = Integer.parseInt(args[++i]);
/*  227 */       } else if (args[i].equalsIgnoreCase("-feature")) {
/*  228 */         featureDescriptionFile = args[++i];
/*  229 */       } else if (args[i].equals("-metric2t")) {
/*  230 */         trainMetric = args[++i];
/*  231 */       } else if (args[i].equals("-metric2T")) {
/*  232 */         testMetric = args[++i];
/*  233 */       } else if (args[i].equalsIgnoreCase("-gmax")) {
/*  234 */         ERRScorer.MAX = Math.pow(2.0D, Double.parseDouble(args[++i]));
/*  235 */       } else if (args[i].equalsIgnoreCase("-qrel")) {
/*  236 */         qrelFile = args[++i];
/*  237 */       } else if (args[i].equalsIgnoreCase("-tts")) {
/*  238 */         ttSplit = Float.parseFloat(args[++i]);
/*  239 */       } else if (args[i].equalsIgnoreCase("-tvs")) {
/*  240 */         tvSplit = Float.parseFloat(args[++i]);
/*  241 */       } else if (args[i].equalsIgnoreCase("-kcv")) {
/*  242 */         foldCV = Integer.parseInt(args[++i]);
/*  243 */       } else if (args[i].equalsIgnoreCase("-validate")) {
/*  244 */         validationFile = args[++i];
/*  245 */       } else if (args[i].equalsIgnoreCase("-test")) {
/*      */         
/*  247 */         testFile = args[++i];
/*  248 */         testFiles.add(testFile);
/*      */       }
/*  250 */       else if (args[i].equalsIgnoreCase("-norm")) {
/*      */         
/*  252 */         normalize = true;
/*  253 */         String n = args[++i];
/*  254 */         if (n.equalsIgnoreCase("sum")) {
/*  255 */           nml = (Normalizer)new SumNormalizor();
/*  256 */         } else if (n.equalsIgnoreCase("zscore")) {
/*  257 */           nml = (Normalizer)new ZScoreNormalizor();
/*  258 */         } else if (n.equalsIgnoreCase("linear")) {
/*  259 */           nml = (Normalizer)new LinearNormalizer();
/*      */         } else {
/*      */           
/*  262 */           throw RankLibError.create("Unknown normalizor: " + n);
/*      */         }
/*      */       
/*  265 */       } else if (args[i].equalsIgnoreCase("-sparse")) {
/*  266 */         useSparseRepresentation = true;
/*  267 */       } else if (args[i].equalsIgnoreCase("-save")) {
/*  268 */         modelFile = args[++i];
/*  269 */       } else if (args[i].equalsIgnoreCase("-kcvmd")) {
/*  270 */         kcvModelDir = args[++i];
/*  271 */       } else if (args[i].equalsIgnoreCase("-kcvmn")) {
/*  272 */         kcvModelFile = args[++i];
/*  273 */       } else if (args[i].equalsIgnoreCase("-silent")) {
/*  274 */         Ranker.verbose = false;
/*  275 */       } else if (args[i].equalsIgnoreCase("-missingZero")) {
/*  276 */         DataPoint.missingZero = true;
/*      */       }
/*  278 */       else if (args[i].equalsIgnoreCase("-load")) {
/*      */         
/*  280 */         savedModelFile = args[++i];
/*  281 */         savedModelFiles.add(args[i]);
/*      */       }
/*  283 */       else if (args[i].equalsIgnoreCase("-idv")) {
/*  284 */         prpFile = args[++i];
/*  285 */       } else if (args[i].equalsIgnoreCase("-rank")) {
/*  286 */         rankFile = args[++i];
/*  287 */       } else if (args[i].equalsIgnoreCase("-score")) {
/*  288 */         scoreFile = args[++i];
/*      */ 
/*      */       
/*      */       }
/*  292 */       else if (args[i].equalsIgnoreCase("-epoch")) {
/*      */         
/*  294 */         RankNet.nIteration = Integer.parseInt(args[++i]);
/*  295 */         ListNet.nIteration = Integer.parseInt(args[i]);
/*      */       }
/*  297 */       else if (args[i].equalsIgnoreCase("-layer")) {
/*  298 */         RankNet.nHiddenLayer = Integer.parseInt(args[++i]);
/*  299 */       } else if (args[i].equalsIgnoreCase("-node")) {
/*  300 */         RankNet.nHiddenNodePerLayer = Integer.parseInt(args[++i]);
/*  301 */       } else if (args[i].equalsIgnoreCase("-lr")) {
/*      */         
/*  303 */         RankNet.learningRate = Double.parseDouble(args[++i]);
/*  304 */         ListNet.learningRate = Neuron.learningRate;
/*      */ 
/*      */       
/*      */       }
/*  308 */       else if (args[i].equalsIgnoreCase("-tc")) {
/*      */         
/*  310 */         RankBoost.nThreshold = Integer.parseInt(args[++i]);
/*  311 */         LambdaMART.nThreshold = Integer.parseInt(args[i]);
/*      */ 
/*      */       
/*      */       }
/*  315 */       else if (args[i].equalsIgnoreCase("-noeq")) {
/*  316 */         AdaRank.trainWithEnqueue = false;
/*  317 */       } else if (args[i].equalsIgnoreCase("-max")) {
/*  318 */         AdaRank.maxSelCount = Integer.parseInt(args[++i]);
/*      */       
/*      */       }
/*  321 */       else if (args[i].equalsIgnoreCase("-r")) {
/*  322 */         CoorAscent.nRestart = Integer.parseInt(args[++i]);
/*  323 */       } else if (args[i].equalsIgnoreCase("-i")) {
/*  324 */         CoorAscent.nMaxIteration = Integer.parseInt(args[++i]);
/*      */       
/*      */       }
/*  327 */       else if (args[i].equalsIgnoreCase("-round")) {
/*      */         
/*  329 */         RankBoost.nIteration = Integer.parseInt(args[++i]);
/*  330 */         AdaRank.nIteration = Integer.parseInt(args[i]);
/*      */       }
/*  332 */       else if (args[i].equalsIgnoreCase("-reg")) {
/*      */         
/*  334 */         CoorAscent.slack = Double.parseDouble(args[++i]);
/*  335 */         CoorAscent.regularized = true;
/*      */       }
/*  337 */       else if (args[i].equalsIgnoreCase("-tolerance")) {
/*      */         
/*  339 */         AdaRank.tolerance = Double.parseDouble(args[++i]);
/*  340 */         CoorAscent.tolerance = Double.parseDouble(args[i]);
/*      */ 
/*      */       
/*      */       }
/*  344 */       else if (args[i].equalsIgnoreCase("-tree")) {
/*      */         
/*  346 */         LambdaMART.nTrees = Integer.parseInt(args[++i]);
/*  347 */         RFRanker.nTrees = Integer.parseInt(args[i]);
/*      */       }
/*  349 */       else if (args[i].equalsIgnoreCase("-leaf")) {
/*      */         
/*  351 */         LambdaMART.nTreeLeaves = Integer.parseInt(args[++i]);
/*  352 */         RFRanker.nTreeLeaves = Integer.parseInt(args[i]);
/*      */       }
/*  354 */       else if (args[i].equalsIgnoreCase("-shrinkage")) {
/*      */         
/*  356 */         LambdaMART.learningRate = Float.parseFloat(args[++i]);
/*  357 */         RFRanker.learningRate = Float.parseFloat(args[i]);
/*      */       }
/*  359 */       else if (args[i].equalsIgnoreCase("-mls")) {
/*      */         
/*  361 */         LambdaMART.minLeafSupport = Integer.parseInt(args[++i]);
/*  362 */         RFRanker.minLeafSupport = LambdaMART.minLeafSupport;
/*      */       }
/*  364 */       else if (args[i].equalsIgnoreCase("-estop")) {
/*  365 */         LambdaMART.nRoundToStopEarly = Integer.parseInt(args[++i]);
/*      */       }
/*  367 */       else if (args[i].equalsIgnoreCase("-gcc")) {
/*  368 */         LambdaMART.gcCycle = Integer.parseInt(args[++i]);
/*      */       
/*      */       }
/*  371 */       else if (args[i].equalsIgnoreCase("-bag")) {
/*  372 */         RFRanker.nBag = Integer.parseInt(args[++i]);
/*  373 */       } else if (args[i].equalsIgnoreCase("-srate")) {
/*  374 */         RFRanker.subSamplingRate = Float.parseFloat(args[++i]);
/*  375 */       } else if (args[i].equalsIgnoreCase("-frate")) {
/*  376 */         RFRanker.featureSamplingRate = Float.parseFloat(args[++i]);
/*  377 */       } else if (args[i].equalsIgnoreCase("-rtype")) {
/*      */         
/*  379 */         int rt = Integer.parseInt(args[++i]);
/*  380 */         if (rt == 0 || rt == 6) {
/*  381 */           RFRanker.rType = rType2[rt];
/*      */         } else {
/*      */           
/*  384 */           throw RankLibError.create(rType[rt] + " cannot be bagged. Random Forests only supports MART/LambdaMART.");
/*      */         }
/*      */       
/*      */       }
/*  388 */       else if (args[i].equalsIgnoreCase("-L2")) {
/*  389 */         LinearRegRank.lambda = Double.parseDouble(args[++i]);
/*      */       }
/*  391 */       else if (args[i].equalsIgnoreCase("-thread")) {
/*  392 */         nThread = Integer.parseInt(args[++i]);
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*  397 */       else if (args[i].equalsIgnoreCase("-nf")) {
/*  398 */         newFeatureFile = args[++i];
/*  399 */       } else if (args[i].equalsIgnoreCase("-keep")) {
/*  400 */         keepOrigFeatures = true;
/*  401 */       } else if (args[i].equalsIgnoreCase("-t")) {
/*  402 */         topNew = Integer.parseInt(args[++i]);
/*  403 */       } else if (args[i].equalsIgnoreCase("-indri")) {
/*  404 */         indriRankingFile = args[++i];
/*  405 */       } else if (args[i].equalsIgnoreCase("-hr")) {
/*  406 */         mustHaveRelDoc = true;
/*      */       } else {
/*      */         
/*  409 */         throw RankLibError.create("Unknown command-line parameter: " + args[i]);
/*      */       } 
/*      */     } 
/*      */     
/*  413 */     if (nThread == -1)
/*  414 */       nThread = Runtime.getRuntime().availableProcessors(); 
/*  415 */     MyThreadPool.init(nThread);
/*      */     
/*  417 */     if (testMetric.compareTo("") == 0) {
/*  418 */       testMetric = trainMetric;
/*      */     }
/*  420 */     System.out.println("");
/*  421 */     System.out.println(keepOrigFeatures ? "Keep orig. features" : "Discard orig. features");
/*  422 */     Evaluator e = new Evaluator(rType2[rankerType], trainMetric, testMetric);
/*      */     
/*  424 */     if (trainFile.compareTo("") != 0) {
/*      */       
/*  426 */       System.out.println("Training data:\t" + trainFile);
/*      */ 
/*      */       
/*  429 */       if (foldCV != -1) {
/*      */         
/*  431 */         System.out.println("Cross validation: " + foldCV + " folds.");
/*  432 */         if (tvSplit > 0.0F) {
/*  433 */           System.out.println("Train-Validation split: " + tvSplit);
/*      */         }
/*      */       } else {
/*      */         
/*  437 */         if (testFile.compareTo("") != 0) {
/*  438 */           System.out.println("Test data:\t" + testFile);
/*  439 */         } else if (ttSplit > 0.0F) {
/*  440 */           System.out.println("Train-Test split: " + ttSplit);
/*      */         } 
/*  442 */         if (validationFile.compareTo("") != 0) {
/*  443 */           System.out.println("Validation data:\t" + validationFile);
/*  444 */         } else if (ttSplit <= 0.0F && tvSplit > 0.0F) {
/*  445 */           System.out.println("Train-Validation split: " + tvSplit);
/*      */         } 
/*  447 */       }  System.out.println("Feature vector representation: " + (useSparseRepresentation ? "Sparse" : "Dense") + ".");
/*  448 */       System.out.println("Ranking method:\t" + rType[rankerType]);
/*  449 */       if (featureDescriptionFile.compareTo("") != 0) {
/*  450 */         System.out.println("Feature description file:\t" + featureDescriptionFile);
/*      */       } else {
/*  452 */         System.out.println("Feature description file:\tUnspecified. All features will be used.");
/*  453 */       }  System.out.println("Train metric:\t" + trainMetric);
/*  454 */       System.out.println("Test metric:\t" + testMetric);
/*      */       
/*  456 */       if (trainMetric.toUpperCase().startsWith("ERR") || testMetric.toUpperCase().startsWith("ERR"))
/*  457 */         System.out.println("Highest relevance label (to compute ERR): " + (int)SimpleMath.logBase2(ERRScorer.MAX)); 
/*  458 */       if (qrelFile.compareTo("") != 0)
/*  459 */         System.out.println("TREC-format relevance judgment (only affects MAP and NDCG scores): " + qrelFile); 
/*  460 */       System.out.println("Feature normalization: " + (normalize ? nml.name() : "No"));
/*      */       
/*  462 */       if (kcvModelDir.compareTo("") != 0) {
/*  463 */         System.out.println("Models directory: " + kcvModelDir);
/*      */       }
/*  465 */       if (kcvModelFile.compareTo("") != 0) {
/*  466 */         System.out.println("Models' name: " + kcvModelFile);
/*      */       }
/*  468 */       if (modelFile.compareTo("") != 0) {
/*  469 */         System.out.println("Model file: " + modelFile);
/*      */       }
/*      */       
/*  472 */       System.out.println("");
/*  473 */       System.out.println("[+] " + rType[rankerType] + "'s Parameters:");
/*  474 */       RankerFactory rf = new RankerFactory();
/*      */       
/*  476 */       rf.createRanker(rType2[rankerType]).printParameters();
/*  477 */       System.out.println("");
/*      */ 
/*      */       
/*  480 */       if (foldCV != -1) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  488 */         if (kcvModelDir.compareTo("") != 0 && kcvModelFile.compareTo("") == 0) {
/*  489 */           kcvModelFile = "kcv";
/*      */         }
/*  491 */         else if (kcvModelDir.compareTo("") == 0 && kcvModelFile.compareTo("") != 0) {
/*  492 */           kcvModelDir = "kcvmodels";
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*  497 */         e.evaluate(trainFile, featureDescriptionFile, foldCV, tvSplit, kcvModelDir, kcvModelFile);
/*      */ 
/*      */       
/*      */       }
/*  501 */       else if (ttSplit > 0.0D) {
/*  502 */         e.evaluate(trainFile, validationFile, featureDescriptionFile, ttSplit);
/*  503 */       } else if (tvSplit > 0.0D) {
/*  504 */         e.evaluate(trainFile, tvSplit, testFile, featureDescriptionFile);
/*      */       } else {
/*  506 */         e.evaluate(trainFile, validationFile, testFile, featureDescriptionFile);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  511 */       System.out.println("Model file:\t" + savedModelFile);
/*  512 */       System.out.println("Feature normalization: " + (normalize ? nml.name() : "No"));
/*  513 */       if (rankFile.compareTo("") != 0) {
/*      */         
/*  515 */         if (scoreFile.compareTo("") != 0) {
/*      */           
/*  517 */           if (savedModelFiles.size() > 1) {
/*  518 */             e.score(savedModelFiles, rankFile, scoreFile);
/*      */           } else {
/*  520 */             e.score(savedModelFile, rankFile, scoreFile);
/*      */           } 
/*  522 */         } else if (indriRankingFile.compareTo("") != 0) {
/*      */           
/*  524 */           if (savedModelFiles.size() > 1) {
/*  525 */             e.rank(savedModelFiles, rankFile, indriRankingFile);
/*  526 */           } else if (savedModelFiles.size() == 1) {
/*  527 */             e.rank(savedModelFile, rankFile, indriRankingFile);
/*      */           }
/*      */           else {
/*      */             
/*  531 */             e.rank(rankFile, indriRankingFile);
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/*  536 */           throw RankLibError.create("This function has been removed.\nConsider using -score in addition to your current parameters, and do the ranking yourself based on these scores.");
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */         
/*  544 */         System.out.println("Test metric:\t" + testMetric);
/*  545 */         if (testMetric.startsWith("ERR")) {
/*  546 */           System.out.println("Highest relevance label (to compute ERR): " + (int)SimpleMath.logBase2(ERRScorer.MAX));
/*      */         }
/*  548 */         if (savedModelFile.compareTo("") != 0) {
/*      */           
/*  550 */           if (savedModelFiles.size() > 1) {
/*      */             
/*  552 */             if (testFiles.size() > 1) {
/*  553 */               e.test(savedModelFiles, testFiles, prpFile);
/*      */             } else {
/*  555 */               e.test(savedModelFiles, testFile, prpFile);
/*      */             } 
/*  557 */           } else if (savedModelFiles.size() == 1) {
/*  558 */             e.test(savedModelFile, testFile, prpFile);
/*      */           } 
/*  560 */         } else if (scoreFile.compareTo("") != 0) {
/*  561 */           e.testWithScoreFile(testFile, scoreFile);
/*      */         } else {
/*      */           
/*  564 */           e.test(testFile, prpFile);
/*      */         } 
/*      */       } 
/*  567 */     }  MyThreadPool.getInstance().shutdown();
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean mustHaveRelDoc = false;
/*      */   public static boolean useSparseRepresentation = false;
/*      */   public static boolean normalize = false;
/*  574 */   public static Normalizer nml = (Normalizer)new SumNormalizor();
/*  575 */   public static String modelFile = "";
/*      */   
/*  577 */   public static String qrelFile = "";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  585 */   public static String newFeatureFile = "";
/*      */   public static boolean keepOrigFeatures = false;
/*  587 */   public static int topNew = 2000;
/*      */   
/*  589 */   protected RankerFactory rFact = new RankerFactory();
/*  590 */   protected MetricScorerFactory mFact = new MetricScorerFactory();
/*      */   
/*  592 */   protected MetricScorer trainScorer = null;
/*  593 */   protected MetricScorer testScorer = null;
/*  594 */   protected RANKER_TYPE type = RANKER_TYPE.MART;
/*      */ 
/*      */ 
/*      */   
/*      */   public Evaluator(RANKER_TYPE rType, METRIC trainMetric, METRIC testMetric) {
/*  599 */     this.type = rType;
/*  600 */     this.trainScorer = this.mFact.createScorer(trainMetric);
/*  601 */     this.testScorer = this.mFact.createScorer(testMetric);
/*  602 */     if (qrelFile.compareTo("") != 0) {
/*      */       
/*  604 */       this.trainScorer.loadExternalRelevanceJudgment(qrelFile);
/*  605 */       this.testScorer.loadExternalRelevanceJudgment(qrelFile);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Evaluator(RANKER_TYPE rType, METRIC trainMetric, int trainK, METRIC testMetric, int testK) {
/*  612 */     this.type = rType;
/*  613 */     this.trainScorer = this.mFact.createScorer(trainMetric, trainK);
/*  614 */     this.testScorer = this.mFact.createScorer(testMetric, testK);
/*  615 */     if (qrelFile.compareTo("") != 0) {
/*      */       
/*  617 */       this.trainScorer.loadExternalRelevanceJudgment(qrelFile);
/*  618 */       this.testScorer.loadExternalRelevanceJudgment(qrelFile);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Evaluator(RANKER_TYPE rType, METRIC trainMetric, METRIC testMetric, int k) {
/*  625 */     this.type = rType;
/*  626 */     this.trainScorer = this.mFact.createScorer(trainMetric, k);
/*  627 */     this.testScorer = this.mFact.createScorer(testMetric, k);
/*  628 */     if (qrelFile.compareTo("") != 0) {
/*      */       
/*  630 */       this.trainScorer.loadExternalRelevanceJudgment(qrelFile);
/*  631 */       this.testScorer.loadExternalRelevanceJudgment(qrelFile);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Evaluator(RANKER_TYPE rType, METRIC metric, int k) {
/*  638 */     this.type = rType;
/*  639 */     this.trainScorer = this.mFact.createScorer(metric, k);
/*  640 */     if (qrelFile.compareTo("") != 0)
/*  641 */       this.trainScorer.loadExternalRelevanceJudgment(qrelFile); 
/*  642 */     this.testScorer = this.trainScorer;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Evaluator(RANKER_TYPE rType, String trainMetric, String testMetric) {
/*  648 */     this.type = rType;
/*  649 */     this.trainScorer = this.mFact.createScorer(trainMetric);
/*  650 */     this.testScorer = this.mFact.createScorer(testMetric);
/*  651 */     if (qrelFile.compareTo("") != 0) {
/*      */       
/*  653 */       this.trainScorer.loadExternalRelevanceJudgment(qrelFile);
/*  654 */       this.testScorer.loadExternalRelevanceJudgment(qrelFile);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<RankList> readInput(String inputFile) {
/*  661 */     return FeatureManager.readInput(inputFile, mustHaveRelDoc, useSparseRepresentation);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void normalize(List<RankList> samples) {
/*  667 */     for (RankList sample : samples) nml.normalize(sample);
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   public void normalize(List<RankList> samples, int[] fids) {
/*  673 */     for (RankList sample : samples) nml.normalize(sample, fids);
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   public void normalizeAll(List<List<RankList>> samples, int[] fids) {
/*  679 */     for (List<RankList> sample : samples) normalize(sample, fids);
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int[] readFeature(String featureDefFile) {
/*  686 */     if (featureDefFile.isEmpty())
/*  687 */       return null; 
/*  688 */     return FeatureManager.readFeature(featureDefFile);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public double evaluate(Ranker ranker, List<RankList> rl) {
/*  694 */     List<RankList> l = rl;
/*  695 */     if (ranker != null)
/*  696 */       l = ranker.rank(rl); 
/*  697 */     return this.testScorer.score(l);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void evaluate(String trainFile, String validationFile, String testFile, String featureDefFile) {
/*  710 */     List<RankList> train = readInput(trainFile);
/*      */     
/*  712 */     List<RankList> validation = null;
/*      */     
/*  714 */     if (!validationFile.isEmpty()) {
/*  715 */       validation = readInput(validationFile);
/*      */     }
/*  717 */     List<RankList> test = null;
/*      */     
/*  719 */     if (!testFile.isEmpty()) {
/*  720 */       test = readInput(testFile);
/*      */     }
/*  722 */     int[] features = readFeature(featureDefFile);
/*  723 */     if (features == null) {
/*  724 */       features = FeatureManager.getFeatureFromSampleVector(train);
/*      */     }
/*  726 */     if (normalize) {
/*      */       
/*  728 */       normalize(train, features);
/*  729 */       if (validation != null)
/*  730 */         normalize(validation, features); 
/*  731 */       if (test != null) {
/*  732 */         normalize(test, features);
/*      */       }
/*      */     } 
/*  735 */     RankerTrainer trainer = new RankerTrainer();
/*  736 */     Ranker ranker = trainer.train(this.type, train, validation, features, this.trainScorer);
/*      */     
/*  738 */     if (test != null) {
/*      */       
/*  740 */       double rankScore = evaluate(ranker, test);
/*  741 */       System.out.println(this.testScorer.name() + " on test data: " + SimpleMath.round(rankScore, 4));
/*      */     } 
/*  743 */     if (modelFile.compareTo("") != 0) {
/*      */       
/*  745 */       System.out.println("");
/*  746 */       ranker.save(modelFile);
/*  747 */       System.out.println("Model saved to: " + modelFile);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void evaluate(String sampleFile, String validationFile, String featureDefFile, double percentTrain) {
/*  761 */     List<RankList> trainingData = new ArrayList<>();
/*  762 */     List<RankList> testData = new ArrayList<>();
/*  763 */     int[] features = prepareSplit(sampleFile, featureDefFile, percentTrain, normalize, trainingData, testData);
/*  764 */     List<RankList> validation = null;
/*      */ 
/*      */     
/*  767 */     if (!validationFile.isEmpty()) {
/*      */       
/*  769 */       validation = readInput(validationFile);
/*  770 */       if (normalize) {
/*  771 */         normalize(validation, features);
/*      */       }
/*      */     } 
/*  774 */     RankerTrainer trainer = new RankerTrainer();
/*  775 */     Ranker ranker = trainer.train(this.type, trainingData, validation, features, this.trainScorer);
/*      */     
/*  777 */     double rankScore = evaluate(ranker, testData);
/*      */     
/*  779 */     System.out.println(this.testScorer.name() + " on test data: " + SimpleMath.round(rankScore, 4));
/*  780 */     if (modelFile.compareTo("") != 0) {
/*      */       
/*  782 */       System.out.println("");
/*  783 */       ranker.save(modelFile);
/*  784 */       System.out.println("Model saved to: " + modelFile);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void evaluate(String trainFile, double percentTrain, String testFile, String featureDefFile) {
/*  799 */     List<RankList> train = new ArrayList<>();
/*  800 */     List<RankList> validation = new ArrayList<>();
/*  801 */     int[] features = prepareSplit(trainFile, featureDefFile, percentTrain, normalize, train, validation);
/*  802 */     List<RankList> test = null;
/*      */ 
/*      */     
/*  805 */     if (!testFile.isEmpty()) {
/*      */       
/*  807 */       test = readInput(testFile);
/*  808 */       if (normalize) {
/*  809 */         normalize(test, features);
/*      */       }
/*      */     } 
/*  812 */     RankerTrainer trainer = new RankerTrainer();
/*  813 */     Ranker ranker = trainer.train(this.type, train, validation, features, this.trainScorer);
/*      */     
/*  815 */     if (test != null) {
/*      */       
/*  817 */       double rankScore = evaluate(ranker, test);
/*  818 */       System.out.println(this.testScorer.name() + " on test data: " + SimpleMath.round(rankScore, 4));
/*      */     } 
/*  820 */     if (modelFile.compareTo("") != 0) {
/*      */       
/*  822 */       System.out.println("");
/*  823 */       ranker.save(modelFile);
/*  824 */       System.out.println("Model saved to: " + modelFile);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void evaluate(String sampleFile, String featureDefFile, int nFold, String modelDir, String modelFile) {
/*  839 */     evaluate(sampleFile, featureDefFile, nFold, -1.0F, modelDir, modelFile);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void evaluate(String sampleFile, String featureDefFile, int nFold, float tvs, String modelDir, String modelFile) {
/*  854 */     List<List<RankList>> trainingData = new ArrayList<>();
/*  855 */     List<List<RankList>> validationData = new ArrayList<>();
/*  856 */     List<List<RankList>> testData = new ArrayList<>();
/*      */ 
/*      */ 
/*      */     
/*  860 */     List<RankList> samples = readInput(sampleFile);
/*      */ 
/*      */     
/*  863 */     int[] features = readFeature(featureDefFile);
/*  864 */     if (features == null) {
/*  865 */       features = FeatureManager.getFeatureFromSampleVector(samples);
/*      */     }
/*  867 */     FeatureManager.prepareCV(samples, nFold, tvs, trainingData, validationData, testData);
/*      */ 
/*      */     
/*  870 */     if (normalize)
/*      */     {
/*  872 */       for (int j = 0; j < nFold; j++) {
/*      */         
/*  874 */         normalizeAll(trainingData, features);
/*  875 */         normalizeAll(validationData, features);
/*  876 */         normalizeAll(testData, features);
/*      */       } 
/*      */     }
/*      */     
/*  880 */     Ranker ranker = null;
/*  881 */     double scoreOnTrain = 0.0D;
/*  882 */     double scoreOnTest = 0.0D;
/*  883 */     double totalScoreOnTest = 0.0D;
/*  884 */     int totalTestSampleSize = 0;
/*      */     
/*  886 */     double[][] scores = new double[nFold][]; int i;
/*  887 */     for (i = 0; i < nFold; i++) {
/*  888 */       (new double[2])[0] = 0.0D; (new double[2])[1] = 0.0D; scores[i] = new double[2];
/*  889 */     }  for (i = 0; i < nFold; i++) {
/*      */       
/*  891 */       List<RankList> train = trainingData.get(i);
/*  892 */       List<RankList> vali = null;
/*  893 */       if (tvs > 0.0F)
/*  894 */         vali = validationData.get(i); 
/*  895 */       List<RankList> test = testData.get(i);
/*      */       
/*  897 */       RankerTrainer trainer = new RankerTrainer();
/*  898 */       ranker = trainer.train(this.type, train, vali, features, this.trainScorer);
/*      */       
/*  900 */       double s2 = evaluate(ranker, test);
/*  901 */       scoreOnTrain += ranker.getScoreOnTrainingData();
/*  902 */       scoreOnTest += s2;
/*  903 */       totalScoreOnTest += s2 * test.size();
/*  904 */       totalTestSampleSize += test.size();
/*      */ 
/*      */       
/*  907 */       scores[i][0] = ranker.getScoreOnTrainingData();
/*  908 */       scores[i][1] = s2;
/*      */ 
/*      */       
/*  911 */       if (!modelDir.isEmpty()) {
/*      */         
/*  913 */         ranker.save(FileUtils.makePathStandard(modelDir) + "f" + (i + 1) + "." + modelFile);
/*  914 */         System.out.println("Fold-" + (i + 1) + " model saved to: " + modelFile);
/*      */       } 
/*      */     } 
/*  917 */     System.out.println("Summary:");
/*  918 */     System.out.println(this.testScorer.name() + "\t|   Train\t| Test");
/*  919 */     System.out.println("----------------------------------");
/*  920 */     for (i = 0; i < nFold; i++)
/*  921 */       System.out.println("Fold " + (i + 1) + "\t|   " + SimpleMath.round(scores[i][0], 4) + "\t|  " + SimpleMath.round(scores[i][1], 4) + "\t"); 
/*  922 */     System.out.println("----------------------------------");
/*  923 */     System.out.println("Avg.\t|   " + SimpleMath.round(scoreOnTrain / nFold, 4) + "\t|  " + SimpleMath.round(scoreOnTest / nFold, 4) + "\t");
/*  924 */     System.out.println("----------------------------------");
/*  925 */     System.out.println("Total\t|   \t\t|  " + SimpleMath.round(totalScoreOnTest / totalTestSampleSize, 4) + "\t");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void test(String testFile) {
/*  935 */     List<RankList> test = readInput(testFile);
/*  936 */     double rankScore = evaluate(null, test);
/*  937 */     System.out.println(this.testScorer.name() + " on test data: " + SimpleMath.round(rankScore, 4));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void test(String testFile, String prpFile) {
/*  943 */     List<RankList> test = readInput(testFile);
/*  944 */     double rankScore = 0.0D;
/*  945 */     List<String> ids = new ArrayList<>();
/*  946 */     List<Double> scores = new ArrayList<>();
/*  947 */     for (RankList l : test) {
/*  948 */       double score = this.testScorer.score(l);
/*  949 */       ids.add(l.getID());
/*  950 */       scores.add(Double.valueOf(score));
/*  951 */       rankScore += score;
/*      */     } 
/*  953 */     rankScore /= test.size();
/*  954 */     ids.add("all");
/*  955 */     scores.add(Double.valueOf(rankScore));
/*  956 */     System.out.println(this.testScorer.name() + " on test data: " + SimpleMath.round(rankScore, 4));
/*      */ 
/*      */     
/*  959 */     if (!prpFile.isEmpty()) {
/*      */       
/*  961 */       savePerRankListPerformanceFile(ids, scores, prpFile);
/*  962 */       System.out.println("Per-ranked list performance saved to: " + prpFile);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void test(String modelFile, String testFile, String prpFile) {
/*  975 */     Ranker ranker = this.rFact.loadRankerFromFile(modelFile);
/*  976 */     int[] features = ranker.getFeatures();
/*  977 */     List<RankList> test = readInput(testFile);
/*  978 */     if (normalize) {
/*  979 */       normalize(test, features);
/*      */     }
/*  981 */     double rankScore = 0.0D;
/*  982 */     List<String> ids = new ArrayList<>();
/*  983 */     List<Double> scores = new ArrayList<>();
/*  984 */     for (RankList aTest : test) {
/*  985 */       RankList l = ranker.rank(aTest);
/*  986 */       double score = this.testScorer.score(l);
/*  987 */       ids.add(l.getID());
/*  988 */       scores.add(Double.valueOf(score));
/*  989 */       rankScore += score;
/*      */     } 
/*  991 */     rankScore /= test.size();
/*  992 */     ids.add("all");
/*  993 */     scores.add(Double.valueOf(rankScore));
/*  994 */     System.out.println(this.testScorer.name() + " on test data: " + SimpleMath.round(rankScore, 4));
/*      */ 
/*      */     
/*  997 */     if (!prpFile.isEmpty()) {
/*      */       
/*  999 */       savePerRankListPerformanceFile(ids, scores, prpFile);
/* 1000 */       System.out.println("Per-ranked list performance saved to: " + prpFile);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void test(List<String> modelFiles, String testFile, String prpFile) {
/* 1014 */     List<List<RankList>> trainingData = new ArrayList<>();
/* 1015 */     List<List<RankList>> testData = new ArrayList<>();
/*      */ 
/*      */     
/* 1018 */     int nFold = modelFiles.size();
/*      */     
/* 1020 */     List<RankList> samples = readInput(testFile);
/*      */     
/* 1022 */     System.out.print("Preparing " + nFold + "-fold test data... ");
/* 1023 */     FeatureManager.prepareCV(samples, nFold, trainingData, testData);
/* 1024 */     System.out.println("[Done.]");
/* 1025 */     double rankScore = 0.0D;
/* 1026 */     List<String> ids = new ArrayList<>();
/* 1027 */     List<Double> scores = new ArrayList<>();
/* 1028 */     for (int f = 0; f < nFold; f++) {
/*      */       
/* 1030 */       List<RankList> test = testData.get(f);
/* 1031 */       Ranker ranker = this.rFact.loadRankerFromFile(modelFiles.get(f));
/* 1032 */       int[] features = ranker.getFeatures();
/* 1033 */       if (normalize) {
/* 1034 */         normalize(test, features);
/*      */       }
/* 1036 */       for (RankList aTest : test) {
/* 1037 */         RankList l = ranker.rank(aTest);
/* 1038 */         double score = this.testScorer.score(l);
/* 1039 */         ids.add(l.getID());
/* 1040 */         scores.add(Double.valueOf(score));
/* 1041 */         rankScore += score;
/*      */       } 
/*      */     } 
/* 1044 */     rankScore /= ids.size();
/* 1045 */     ids.add("all");
/* 1046 */     scores.add(Double.valueOf(rankScore));
/* 1047 */     System.out.println(this.testScorer.name() + " on test data: " + SimpleMath.round(rankScore, 4));
/*      */     
/* 1049 */     if (!prpFile.isEmpty()) {
/*      */       
/* 1051 */       savePerRankListPerformanceFile(ids, scores, prpFile);
/* 1052 */       System.out.println("Per-ranked list performance saved to: " + prpFile);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void test(List<String> modelFiles, List<String> testFiles, String prpFile) {
/* 1065 */     int nFold = modelFiles.size();
/* 1066 */     double rankScore = 0.0D;
/* 1067 */     List<String> ids = new ArrayList<>();
/* 1068 */     List<Double> scores = new ArrayList<>();
/* 1069 */     for (int f = 0; f < nFold; f++) {
/*      */ 
/*      */       
/* 1072 */       List<RankList> test = readInput(testFiles.get(f));
/* 1073 */       Ranker ranker = this.rFact.loadRankerFromFile(modelFiles.get(f));
/* 1074 */       int[] features = ranker.getFeatures();
/*      */       
/* 1076 */       if (normalize) {
/* 1077 */         normalize(test, features);
/*      */       }
/* 1079 */       for (RankList aTest : test) {
/* 1080 */         RankList l = ranker.rank(aTest);
/* 1081 */         double score = this.testScorer.score(l);
/* 1082 */         ids.add(l.getID());
/* 1083 */         scores.add(Double.valueOf(score));
/* 1084 */         rankScore += score;
/*      */       } 
/*      */     } 
/* 1087 */     rankScore /= ids.size();
/* 1088 */     ids.add("all");
/* 1089 */     scores.add(Double.valueOf(rankScore));
/* 1090 */     System.out.println(this.testScorer.name() + " on test data: " + SimpleMath.round(rankScore, 4));
/*      */ 
/*      */     
/* 1093 */     if (!prpFile.isEmpty()) {
/*      */       
/* 1095 */       savePerRankListPerformanceFile(ids, scores, prpFile);
/* 1096 */       System.out.println("Per-ranked list performance saved to: " + prpFile);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void testWithScoreFile(String testFile, String scoreFile) {
/* 1108 */     try (BufferedReader in = FileUtils.smartReader(scoreFile)) {
/* 1109 */       List<RankList> test = readInput(testFile);
/* 1110 */       String content = "";
/*      */       
/* 1112 */       List<Double> scores = new ArrayList<>();
/* 1113 */       while ((content = in.readLine()) != null) {
/*      */         
/* 1115 */         content = content.trim();
/* 1116 */         if (content.compareTo("") == 0)
/*      */           continue; 
/* 1118 */         scores.add(Double.valueOf(Double.parseDouble(content)));
/*      */       } 
/* 1120 */       in.close();
/* 1121 */       int k = 0;
/* 1122 */       for (int i = 0; i < test.size(); i++) {
/*      */         
/* 1124 */         RankList rl = test.get(i);
/* 1125 */         double[] s = new double[rl.size()];
/* 1126 */         for (int j = 0; j < rl.size(); j++)
/* 1127 */           s[j] = ((Double)scores.get(k++)).doubleValue(); 
/* 1128 */         rl = new RankList(rl, MergeSorter.sort(s, false));
/* 1129 */         test.set(i, rl);
/*      */       } 
/*      */       
/* 1132 */       double rankScore = evaluate(null, test);
/* 1133 */       System.out.println(this.testScorer.name() + " on test data: " + SimpleMath.round(rankScore, 4));
/* 1134 */     } catch (IOException e) {
/* 1135 */       throw RankLibError.create(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void score(String modelFile, String testFile, String outputFile) {
/* 1148 */     Ranker ranker = this.rFact.loadRankerFromFile(modelFile);
/* 1149 */     int[] features = ranker.getFeatures();
/* 1150 */     List<RankList> test = readInput(testFile);
/* 1151 */     if (normalize) {
/* 1152 */       normalize(test, features);
/*      */     }
/*      */     try {
/* 1155 */       BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
/* 1156 */       for (RankList l : test) {
/* 1157 */         for (int j = 0; j < l.size(); j++) {
/* 1158 */           out.write(l.getID() + "\t" + j + "\t" + ranker.eval(l.get(j)) + "");
/* 1159 */           out.newLine();
/*      */         } 
/*      */       } 
/* 1162 */       out.close();
/*      */     }
/* 1164 */     catch (IOException ex) {
/*      */       
/* 1166 */       throw RankLibError.create("Error in Evaluator::rank(): ", ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void score(List<String> modelFiles, String testFile, String outputFile) {
/* 1180 */     List<List<RankList>> trainingData = new ArrayList<>();
/* 1181 */     List<List<RankList>> testData = new ArrayList<>();
/*      */ 
/*      */     
/* 1184 */     int nFold = modelFiles.size();
/*      */     
/* 1186 */     List<RankList> samples = readInput(testFile);
/* 1187 */     System.out.print("Preparing " + nFold + "-fold test data... ");
/* 1188 */     FeatureManager.prepareCV(samples, nFold, trainingData, testData);
/* 1189 */     System.out.println("[Done.]");
/*      */     try {
/* 1191 */       BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
/* 1192 */       for (int f = 0; f < nFold; f++) {
/*      */         
/* 1194 */         List<RankList> test = testData.get(f);
/* 1195 */         Ranker ranker = this.rFact.loadRankerFromFile(modelFiles.get(f));
/* 1196 */         int[] features = ranker.getFeatures();
/* 1197 */         if (normalize)
/* 1198 */           normalize(test, features); 
/* 1199 */         for (RankList l : test) {
/* 1200 */           for (int j = 0; j < l.size(); j++) {
/* 1201 */             out.write(l.getID() + "\t" + j + "\t" + ranker.eval(l.get(j)) + "");
/* 1202 */             out.newLine();
/*      */           } 
/*      */         } 
/*      */       } 
/* 1206 */       out.close();
/*      */     }
/* 1208 */     catch (IOException ex) {
/*      */       
/* 1210 */       throw RankLibError.create("Error in Evaluator::score(): ", ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void score(List<String> modelFiles, List<String> testFiles, String outputFile) {
/* 1223 */     int nFold = modelFiles.size();
/* 1224 */     try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"))) {
/* 1225 */       for (int f = 0; f < nFold; f++) {
/*      */ 
/*      */         
/* 1228 */         List<RankList> test = readInput(testFiles.get(f));
/* 1229 */         Ranker ranker = this.rFact.loadRankerFromFile(modelFiles.get(f));
/* 1230 */         int[] features = ranker.getFeatures();
/*      */         
/* 1232 */         if (normalize) {
/* 1233 */           normalize(test, features);
/*      */         }
/* 1235 */         for (RankList l : test) {
/* 1236 */           for (int j = 0; j < l.size(); j++) {
/* 1237 */             out.write(l.getID() + "\t" + j + "\t" + ranker.eval(l.get(j)) + "");
/* 1238 */             out.newLine();
/*      */           }
/*      */         
/*      */         } 
/*      */       } 
/* 1243 */     } catch (IOException ex) {
/*      */       
/* 1245 */       throw RankLibError.create("Error in Evaluator::score(): ", ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void rank(String modelFile, String testFile, String indriRanking) {
/* 1258 */     Ranker ranker = this.rFact.loadRankerFromFile(modelFile);
/* 1259 */     int[] features = ranker.getFeatures();
/* 1260 */     List<RankList> test = readInput(testFile);
/*      */     
/* 1262 */     if (normalize)
/* 1263 */       normalize(test, features); 
/*      */     try {
/* 1265 */       BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(indriRanking), "UTF-8"));
/* 1266 */       for (RankList l : test) {
/* 1267 */         double[] scores = new double[l.size()];
/* 1268 */         for (int j = 0; j < l.size(); j++)
/* 1269 */           scores[j] = ranker.eval(l.get(j)); 
/* 1270 */         int[] idx = MergeSorter.sort(scores, false);
/* 1271 */         for (int i = 0; i < idx.length; i++) {
/* 1272 */           int k = idx[i];
/*      */           
/* 1274 */           String str = l.getID() + " Q0 " + l.get(k).getDescription().replace("#", "").trim() + " " + (i + 1) + " " + SimpleMath.round(scores[k], 5) + " indri";
/* 1275 */           out.write(str);
/* 1276 */           out.newLine();
/*      */         } 
/*      */       } 
/* 1279 */       out.close();
/*      */     }
/* 1281 */     catch (IOException ex) {
/*      */       
/* 1283 */       throw RankLibError.create("Error in Evaluator::rank(): ", ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void rank(String testFile, String indriRanking) {
/* 1295 */     List<RankList> test = readInput(testFile);
/*      */     
/*      */     try {
/* 1298 */       BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(indriRanking), "UTF-8"));
/* 1299 */       for (RankList l : test) {
/* 1300 */         for (int j = 0; j < l.size(); j++) {
/*      */           
/* 1302 */           String str = l.getID() + " Q0 " + l.get(j).getDescription().replace("#", "").trim() + " " + (j + 1) + " " + SimpleMath.round(1.0D - 1.0E-4D * j, 5) + " indri";
/* 1303 */           out.write(str);
/* 1304 */           out.newLine();
/*      */         } 
/*      */       } 
/* 1307 */       out.close();
/*      */     }
/* 1309 */     catch (IOException ex) {
/*      */       
/* 1311 */       throw RankLibError.create("Error in Evaluator::rank(): ", ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void rank(List<String> modelFiles, String testFile, String indriRanking) {
/* 1325 */     List<List<RankList>> trainingData = new ArrayList<>();
/* 1326 */     List<List<RankList>> testData = new ArrayList<>();
/*      */ 
/*      */     
/* 1329 */     int nFold = modelFiles.size();
/*      */     
/* 1331 */     List<RankList> samples = readInput(testFile);
/* 1332 */     System.out.print("Preparing " + nFold + "-fold test data... ");
/* 1333 */     FeatureManager.prepareCV(samples, nFold, trainingData, testData);
/* 1334 */     System.out.println("[Done.]");
/*      */     
/*      */     try {
/* 1337 */       BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(indriRanking), "UTF-8"));
/* 1338 */       for (int f = 0; f < nFold; f++) {
/*      */         
/* 1340 */         List<RankList> test = testData.get(f);
/* 1341 */         Ranker ranker = this.rFact.loadRankerFromFile(modelFiles.get(f));
/* 1342 */         int[] features = ranker.getFeatures();
/* 1343 */         if (normalize) {
/* 1344 */           normalize(test, features);
/*      */         }
/* 1346 */         for (RankList l : test) {
/* 1347 */           double[] scores = new double[l.size()];
/* 1348 */           for (int j = 0; j < l.size(); j++)
/* 1349 */             scores[j] = ranker.eval(l.get(j)); 
/* 1350 */           int[] idx = MergeSorter.sort(scores, false);
/* 1351 */           for (int i = 0; i < idx.length; i++) {
/* 1352 */             int k = idx[i];
/*      */             
/* 1354 */             String str = l.getID() + " Q0 " + l.get(k).getDescription().replace("#", "").trim() + " " + (i + 1) + " " + SimpleMath.round(scores[k], 5) + " indri";
/* 1355 */             out.write(str);
/* 1356 */             out.newLine();
/*      */           } 
/*      */         } 
/*      */       } 
/* 1360 */       out.close();
/*      */     }
/* 1362 */     catch (Exception ex) {
/*      */       
/* 1364 */       throw RankLibError.create("Error in Evaluator::rank(): ", ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void rank(List<String> modelFiles, List<String> testFiles, String indriRanking) {
/* 1377 */     int nFold = modelFiles.size();
/*      */     
/*      */     try {
/* 1380 */       BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(indriRanking), "UTF-8"));
/*      */       
/* 1382 */       for (int f = 0; f < nFold; f++) {
/*      */ 
/*      */         
/* 1385 */         List<RankList> test = readInput(testFiles.get(f));
/* 1386 */         Ranker ranker = this.rFact.loadRankerFromFile(modelFiles.get(f));
/* 1387 */         int[] features = ranker.getFeatures();
/*      */         
/* 1389 */         if (normalize) {
/* 1390 */           normalize(test, features);
/*      */         }
/* 1392 */         for (RankList l : test) {
/* 1393 */           double[] scores = new double[l.size()];
/*      */           
/* 1395 */           for (int j = 0; j < l.size(); j++) {
/* 1396 */             scores[j] = ranker.eval(l.get(j));
/*      */           }
/* 1398 */           int[] idx = MergeSorter.sort(scores, false);
/*      */           
/* 1400 */           for (int i = 0; i < idx.length; i++) {
/* 1401 */             int k = idx[i];
/* 1402 */             String str = l.getID() + " Q0 " + l.get(k).getDescription().replace("#", "").trim() + " " + (i + 1) + " " + SimpleMath.round(scores[k], 5) + " indri";
/* 1403 */             out.write(str);
/* 1404 */             out.newLine();
/*      */           } 
/*      */         } 
/*      */       } 
/* 1408 */       out.close();
/*      */     }
/* 1410 */     catch (IOException ex) {
/*      */       
/* 1412 */       throw RankLibError.create("Error in Evaluator::rank(): ", ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int[] prepareSplit(String sampleFile, String featureDefFile, double percentTrain, boolean normalize, List<RankList> trainingData, List<RankList> testData) {
/* 1430 */     List<RankList> data = readInput(sampleFile);
/*      */ 
/*      */     
/* 1433 */     int[] features = readFeature(featureDefFile);
/*      */ 
/*      */     
/* 1436 */     if (features == null) {
/* 1437 */       features = FeatureManager.getFeatureFromSampleVector(data);
/*      */     }
/* 1439 */     if (normalize) {
/* 1440 */       normalize(data, features);
/*      */     }
/* 1442 */     FeatureManager.prepareSplit(data, percentTrain, trainingData, testData);
/* 1443 */     return features;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void savePerRankListPerformanceFile(List<String> ids, List<Double> scores, String prpFile) {
/* 1455 */     try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(prpFile)))) {
/* 1456 */       for (int i = 0; i < ids.size(); i++)
/*      */       {
/*      */         
/* 1459 */         out.write(this.testScorer.name() + "   " + (String)ids.get(i) + "   " + scores.get(i));
/* 1460 */         out.newLine();
/*      */       }
/*      */     
/* 1463 */     } catch (Exception ex) {
/*      */       
/* 1465 */       throw RankLibError.create("Error in Evaluator::savePerRankListPerformanceFile(): ", ex);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\Ashish Ranjan\BTP\RankLib-2.13.jar!\cii\\umass\edu\eval\Evaluator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */