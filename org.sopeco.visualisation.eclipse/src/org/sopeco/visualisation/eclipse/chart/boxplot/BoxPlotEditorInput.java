package org.sopeco.visualisation.eclipse.chart.boxplot;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;
import org.sopeco.visualisation.eclipse.chart.ViewFactory;
import org.sopeco.visualisation.eclipse.chart.function.FunctionEditorInput;
import org.sopeco.visualisation.model.BoxPlotData;
import org.sopeco.visualisation.model.FunctionData;

public class BoxPlotEditorInput implements IEditorInput, IPersistableElement, IAdaptable {
	private List<BoxPlotData> payload;

	public BoxPlotEditorInput(List<BoxPlotData> payload) {
		this.setPayload(payload);
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == FunctionEditorInput.class)
			return this;
		return null;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return ImageDescriptor.getMissingImageDescriptor();
	}

	@Override
	public String getName() {
		return "Box-Plot View Input";
	}

	@Override
	public IPersistableElement getPersistable() {
		return this;
	}

	@Override
	public String getToolTipText() {
		return "";
	}

	public List<BoxPlotData> getPayload() {
		return payload;
	}

	public void setPayload(List<BoxPlotData> payload) {
		this.payload = payload;
	}

	@Override
	public void saveState(IMemento arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getFactoryId() {
		return ViewFactory.FACTORY_ID;
	}
}
