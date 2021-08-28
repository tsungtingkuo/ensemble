import java.util.Enumeration;

import mulan.classifier.InvalidDataException;
import mulan.classifier.MultiLabelLearnerBase;
import mulan.classifier.MultiLabelOutput;
import mulan.data.MultiLabelInstances;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.TechnicalInformation;

public class Ensemble extends MultiLabelLearnerBase {

	private static final long serialVersionUID = 2437017736308690309L;

	String ensemble = "";
	String[] tools = null;
	String[] labelNames = null;

	public Ensemble(String ensemble, String[] tools) {
		super();
		this.ensemble = ensemble;
		this.tools = tools;
	}

	private boolean union(boolean[] values) {
		boolean result = false;
		for (boolean value : values) {
			result = result || value;
		}
		return result;
	}

	private boolean intersect(boolean[] values) {
		boolean result = true;
		for (boolean value : values) {
			result = result && value;
		}
		return result;
	}

	private boolean votingup(boolean[] values) {
		int count = 0;
		for (boolean value : values) {
			if (value) {
				count++;
			}
		}
		int threshold = (int) Math.ceil((double) values.length / 2.0);
		if (count >= threshold) {
			return true;
		} else {
			return false;
		}
	}

	private boolean votingdown(boolean[] values) {
		int count = 0;
		for (boolean value : values) {
			if (value) {
				count++;
			}
		}
		int threshold = values.length / 2;
		if (count > threshold) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected MultiLabelOutput makePredictionInternal(Instance instance) throws Exception, InvalidDataException {
		boolean[] bipartition = new boolean[labelNames.length];
		for (int i = 0; i < labelNames.length; i++) {
			boolean[] values = new boolean[tools.length];
			for (int j = 0; j < tools.length; j++) {
				for (Enumeration<Attribute> e = instance.enumerateAttributes(); e.hasMoreElements();) {
					Attribute attr = e.nextElement();
					if (attr.name().equals(tools[j] + "_" + labelNames[i])) {
						if (instance.stringValue(attr).equals("1")) {
							values[j] = true;
						} else {
							values[j] = false;
						}
					}
				}
			}

			switch (ensemble) {
			case "union":
				bipartition[i] = union(values);
				break;
			case "intersect":
				bipartition[i] = intersect(values);
				break;
			case "votingup":
				bipartition[i] = votingup(values);
				break;
			case "votingdown":
				bipartition[i] = votingdown(values);
				break;
			}
		}
		return new MultiLabelOutput(bipartition);
	}

	@Override
	protected void buildInternal(MultiLabelInstances instances) throws Exception {
		this.labelNames = instances.getLabelNames();
	}

	@Override
	public TechnicalInformation getTechnicalInformation() {
		// Tim Kuo: Do nothing
		return null;
	}
}
