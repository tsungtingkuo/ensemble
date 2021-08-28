import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import mulan.classifier.MultiLabelLearner;
import mulan.classifier.MultiLabelOutput;
import mulan.data.MultiLabelInstances;
import mulan.evaluation.Evaluation;
import mulan.evaluation.Evaluator;
import mulan.evaluation.MultipleEvaluation;
import mulan.evaluation.measure.ExampleBasedFMeasure;
import mulan.evaluation.measure.ExampleBasedPrecision;
import mulan.evaluation.measure.ExampleBasedRecall;
import mulan.evaluation.measure.Measure;
import mulan.evaluation.measure.MicroFMeasure;
import mulan.evaluation.measure.MicroPrecision;
import mulan.evaluation.measure.MicroRecall;
import weka.core.Instance;
import weka.core.Instances;

public class Mulan4pSCANNER {

	public static int DEFAULT_FOLD_NUMBER = 10;
	public static final String HEADER = "Condition,Method,Measure,Training-Precision,Training-Recall,Training-F1,Testing-Precision,Testing-Recall,Testing-F1\n";

	public static MultiLabelLearner train(String arffFilename, String xmlFilename, MultiLabelLearner learner)
			throws Exception {
		MultiLabelInstances dataset = new MultiLabelInstances(arffFilename, xmlFilename);
		learner.build(dataset);
		return learner;
	}

	public static HashSet<String> test(String arffFilename, String xmlFilename, MultiLabelLearner learner,
			Vector<String> entities) throws Exception {
		MultiLabelInstances dataset = new MultiLabelInstances(arffFilename, xmlFilename);
		HashSet<String> predictions = new HashSet<>();
		int numInstances = dataset.getNumInstances();
		for (int instanceIndex = 0; instanceIndex < numInstances; instanceIndex++) {
			Instance instance = dataset.getDataSet().instance(instanceIndex);
			try {
				MultiLabelOutput output = learner.makePrediction(instance);
				if (output.hasBipartition()) {
					boolean[] bs = output.getBipartition();
					for (int i = 0; i < bs.length; i++) {
						if (bs[i]) {
							predictions.add(entities.get(i));
						}
					}
				}
			} catch (Exception e) {
				// No prediction
			}
		}

		return predictions;
	}

	public static String learn(String trainingFilename, String testingFilename, String xmlFilename,
			MultiLabelLearner learner, String condition, String method, int foldNumber) throws Exception {
		return learn(trainingFilename, testingFilename, xmlFilename, learner, condition, method, foldNumber, null);
	}

	public static String learn(String trainingFilename, String testingFilename, String xmlFilename,
			MultiLabelLearner learner, String condition, String method, int foldNumber, String[] excludedTools)
					throws Exception {

		MultiLabelInstances trainingDataset = null;
		MultiLabelInstances testingDataset = null;

		try {
			trainingDataset = new MultiLabelInstances(trainingFilename, xmlFilename);
			testingDataset = new MultiLabelInstances(testingFilename, xmlFilename);
		} catch (Exception e) {
			System.out.println("Training or testing dataset error, please re-generate datasets.");
			e.printStackTrace();
			return "";
		}

		trainingDataset = exclude(trainingDataset, excludedTools);
		testingDataset = exclude(testingDataset, excludedTools);

		if (trainingDataset.getNumInstances() == 0 || testingDataset.getNumInstances() == 0) {
			System.out.println("No training or testing instances, please check your dataset.");
			return "";
		}

		int nl = trainingDataset.getNumLabels();
		ArrayList<Measure> measures = new ArrayList<>();
		measures.add(new MicroRecall(nl));
		measures.add(new MicroPrecision(nl));
		measures.add(new MicroFMeasure(nl));
		measures.add(new ExampleBasedRecall());
		measures.add(new ExampleBasedPrecision());
		measures.add(new ExampleBasedFMeasure());

		Evaluator eval = new Evaluator();
		MultipleEvaluation trainingResults = eval.crossValidate(learner, trainingDataset, measures, foldNumber);

		learner.build(trainingDataset);
		Evaluation testingResults = eval.evaluate(learner, testingDataset, measures);

		HashMap<String, Double> testingResultsMap = new HashMap<>();
		for (Measure m : testingResults.getMeasures()) {
			testingResultsMap.put(m.getName(), m.getValue());
		}

		String outputString = "";

		if (trainingResults.getMean("Micro-averaged Precision") == 0.0) {
			outputString += (condition + "," + method + ",label-based micro-averaging,-,-,-,-,-,-\n-,-,-,-,-,-\n");
			outputString += (condition + "," + method + ",example-based,-,-,-,-,-,-\n-,-,-,-,-,-\n");
		} else {
			outputString += (condition + "," + method + ",label-based micro-averaging,");

			outputString += String.format("%.4f,", trainingResults.getMean("Micro-averaged Precision"));
			outputString += String.format("%.4f,", trainingResults.getMean("Micro-averaged Recall"));
			outputString += String.format("%.4f,", trainingResults.getMean("Micro-averaged F-Measure"));

			outputString += String.format("%.4f,", testingResultsMap.get("Micro-averaged Precision"));
			outputString += String.format("%.4f,", testingResultsMap.get("Micro-averaged Recall"));
			outputString += String.format("%.4f\n", testingResultsMap.get("Micro-averaged F-Measure"));

			outputString += (condition + "," + method + ",example-based,");

			outputString += String.format("%.4f,", trainingResults.getMean("Example-Based Precision"));
			outputString += String.format("%.4f,", trainingResults.getMean("Example-Based Recall"));
			outputString += String.format("%.4f,", trainingResults.getMean("Example-Based F Measure"));

			outputString += String.format("%.4f,", testingResultsMap.get("Example-Based Precision"));
			outputString += String.format("%.4f,", testingResultsMap.get("Example-Based Recall"));
			outputString += String.format("%.4f\n", testingResultsMap.get("Example-Based F Measure"));
		}

		return outputString;
	}

	public static MultiLabelInstances exclude(MultiLabelInstances dataset, String[] excludedTools) throws Exception {
		if (excludedTools == null || excludedTools.length == 0) {
			return dataset;
		} else {
			Instances instances = dataset.getDataSet();
			String[] labelNames = dataset.getLabelNames();
			for (String excludedTool : excludedTools) {
				for (String labelName : labelNames) {
					int excludeIndex = -1;
					for (int i = 0; i < instances.numAttributes(); i++) {
						if (instances.attribute(i).name().equals(excludedTool + "_" + labelName)) {
							excludeIndex = i;
							break;
						}
					}
					if (excludeIndex > -1) {
						instances.deleteAttributeAt(excludeIndex);
					}
				}
			}
			return dataset.reintegrateModifiedDataSet(instances);
		}
	}
}