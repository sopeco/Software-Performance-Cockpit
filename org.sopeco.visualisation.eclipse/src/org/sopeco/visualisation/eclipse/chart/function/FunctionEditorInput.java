package org.sopeco.visualisation.eclipse.chart.function;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;
import org.sopeco.visualisation.eclipse.chart.ViewFactory;
import org.sopeco.visualisation.model.FunctionData;

public class FunctionEditorInput implements IEditorInput, IPersistableElement, IAdaptable {

	private List<FunctionData> payload;

	public FunctionEditorInput(List<FunctionData> payload) {
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
		return "Function View Input";
	}

	@Override
	public IPersistableElement getPersistable() {
		return this;
	}

	@Override
	public String getToolTipText() {
		return "";
	}

	public List<FunctionData> getPayload() {
		return payload;
	}

	public void setPayload(List<FunctionData> payload) {
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
