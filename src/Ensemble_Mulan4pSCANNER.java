import utility.Utility;

public class Ensemble_Mulan4pSCANNER {

	private static String[] ensembles = { "union", "intersect" };

	public static void main(String[] args) throws Exception {

		String condition = args[0];

		int foldNumber = Mulan4pSCANNER.DEFAULT_FOLD_NUMBER;
		if (args.length > 1) {
			foldNumber = Integer.parseInt(args[1]);
		}

		String toolsFileName = "conf/NLP_TOOLS.txt";
		String outputFileName = "basic-ensemble-results-" + condition + "-" + foldNumber + ".csv";

		String results = Mulan4pSCANNER.HEADER;

		String[] tools = Utility.loadStringArray(toolsFileName);

		for (String ensemble : ensembles) {

			String trainingFilename = "arff/" + condition + "_training.arff";
			String testingFilename = "arff/" + condition + "_testing.arff";
			String xmlFilename = "xml/" + condition + "_ANNOTATIONS.xml";

			Ensemble mll = new Ensemble(ensemble, tools);

			String result = Mulan4pSCANNER.learn(trainingFilename, testingFilename, xmlFilename, mll, condition,
					ensemble, foldNumber);

			System.out.println(result);

			results += result;
		}

		Utility.saveString(outputFileName, results);
	}
}
