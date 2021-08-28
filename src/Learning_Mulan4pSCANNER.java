import mulan.classifier.MultiLabelLearner;
import mulan.classifier.lazy.IBLR_ML;
import mulan.classifier.lazy.MLkNN;
import mulan.classifier.meta.RAkEL;
import mulan.classifier.transformation.BinaryRelevance;
import mulan.classifier.transformation.EnsembleOfClassifierChains;
import mulan.classifier.transformation.LabelPowerset;
import utility.Utility;
import weka.classifiers.trees.J48;

public class Learning_Mulan4pSCANNER {

	private static String[] learners = { "br", "mlknn", "iblrml", "rakel", "ecc" };

	public static void main(String[] args) throws Exception {

		String condition = args[0];

		int foldNumber = Mulan4pSCANNER.DEFAULT_FOLD_NUMBER;
		if (args.length > 1) {
			foldNumber = Integer.parseInt(args[1]);
		}

		String outputFileName = "advanced-ensemble-results-" + condition + "-" + foldNumber + ".csv";

		String results = Mulan4pSCANNER.HEADER;

		for (String learner : learners) {

			String trainingFilename = "arff/" + condition + "_training.arff";
			String testingFilename = "arff/" + condition + "_testing.arff";
			String xmlFilename = "xml/" + condition + "_ANNOTATIONS.xml";

			MultiLabelLearner mll = null;

			switch (learner) {
			case "br":
				mll = new BinaryRelevance(new J48());
				break;
			case "mlknn":
				mll = new MLkNN();
				break;
			case "iblrml":
				mll = new IBLR_ML();
				break;
			case "rakel":
				mll = new RAkEL(new LabelPowerset(new J48()));
				break;
			case "ecc":
				mll = new EnsembleOfClassifierChains();
				break;
			}

			String result = Mulan4pSCANNER.learn(trainingFilename, testingFilename, xmlFilename, mll, condition,
					learner, foldNumber);

			System.out.println(result);

			results += result;
		}

		Utility.saveString(outputFileName, results);
	}
}
