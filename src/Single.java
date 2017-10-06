import java.util.Enumeration;

import mulan.classifier.InvalidDataException;
import mulan.classifier.MultiLabelLearnerBase;
import mulan.classifier.MultiLabelOutput;
import mulan.data.MultiLabelInstances;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.TechnicalInformation;

public class Single extends MultiLabelLearnerBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2437017736308690309L;

	String tool = null;
	String[] labelNames = null;

	public Single(String tool) {
		super();
		this.tool = tool;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected MultiLabelOutput makePredictionInternal(Instance instance) throws Exception, InvalidDataException {
		boolean[] bipartition = new boolean[labelNames.length];
		for (int i = 0; i < labelNames.length; i++) {
			for (Enumeration<Attribute> e = instance.enumerateAttributes(); e.hasMoreElements();) {
				Attribute attr = e.nextElement();
				if (attr.name().equals(tool + "_" + labelNames[i])) {
					if (instance.stringValue(attr).equals("1")) {
						bipartition[i] = true;
					} else {
						bipartition[i] = false;
					}
				}
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
