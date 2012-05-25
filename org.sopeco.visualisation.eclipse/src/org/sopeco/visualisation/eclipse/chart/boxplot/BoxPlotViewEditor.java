package org.sopeco.visualisation.eclipse.chart.boxplot;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.sopeco.visualisation.eclipse.chart.function.FunctionEditorInput;
import org.sopeco.visualisation.model.BoxPlotData;

public class BoxPlotViewEditor extends EditorPart{
	/**
	 * Unique Editor ID, used to open editors
	 */
	private static final String EDITOR_ID = "org.sopeco.visualisation.BoxPlotViewEditor";

	/**
	 * Returns the editor ID
	 * 
	 * @return ID
	 */
	public static String getEditorId() {
		return EDITOR_ID;
	}

	/**
	 * ChartComposite
	 */
	private ChartComposite chartViewer;
	
	private BoxPlotEditorInput input;

	@Override
	public void doSave(IProgressMonitor arg0) {

	}

	@Override
	public void doSaveAs() {

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		chartViewer = new ChartComposite(parent, 0);
		drawBoxPlot(input.getPayload());
	}

	

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	@Override
	protected void setInput(IEditorInput input) {
		if (input instanceof BoxPlotEditorInput) {
			super.setInput(input);
			this.input = (BoxPlotEditorInput)input;
		}
		
	}
	
	private void drawBoxPlot(List<BoxPlotData> payload) {
		BoxAndWhiskerCategoryDataset boxPlotDataSet = new DefaultBoxAndWhiskerCategoryDataset();
		String xAxisName = "";
		String yAxisName = payload.get(0).getObservationParameter().getName();
		JFreeChart chart = ChartFactory.createBoxAndWhiskerChart(
				"Box Plot", xAxisName, yAxisName,
				boxPlotDataSet, true);
		BoxAndWhiskerRenderer boxPlotRenderer = new BoxAndWhiskerRenderer();

		
		for(BoxPlotData box : payload){
			((DefaultBoxAndWhiskerCategoryDataset) boxPlotDataSet).add(
					box.getData(), box.getLabel(), "");
		}
		
		
		
		
		
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setRenderer(boxPlotRenderer);
		chartViewer.setChart(chart);
		chartViewer.forceRedraw();
		
	}
}
