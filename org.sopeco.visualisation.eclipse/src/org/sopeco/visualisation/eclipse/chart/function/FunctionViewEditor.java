package org.sopeco.visualisation.eclipse.chart.function;

import java.awt.Color;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.SimpleDataSet;
import org.sopeco.persistence.dataset.SimpleDataSetRow;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.visualisation.eclipse.util.CommonConstants;
import org.sopeco.visualisation.model.FunctionData;

public class FunctionViewEditor extends EditorPart {

	/**
	 * Unique Editor ID, used to open editors
	 */
	private static final String EDITOR_ID = "org.sopeco.visualisation.FunctionViewEditor";

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
	
	private FunctionEditorInput input;

	@Override
	public void doSave(IProgressMonitor arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		chartViewer = new ChartComposite(parent, 0);
		drawScatter(input.getPayload());
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	@Override
	protected void setInput(IEditorInput input) {
		if (input instanceof FunctionEditorInput) {
			super.setInput(input);
			this.input = (FunctionEditorInput)input;
		}
		
	}

	public void drawScatter(List<FunctionData> functions) {
		DefaultXYDataset functionDataSet = new DefaultXYDataset();
		JFreeChart chart = ChartFactory.createScatterPlot("", functions.get(0).getInputParameter().getName(), functions.get(0).getObservationParameter()
				.getName(), functionDataSet, PlotOrientation.VERTICAL, true, false, false);
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		for (FunctionData function : functions) {
			double[][] measured = getDataSeries(function.getLabel(), function.getInputParameter(), function.getObservationParameter(), function.getData());

			functionDataSet.addSeries(function.getLabel(), measured);
			Color c = CommonConstants.colors[(function.getId() == 0) ? 0 : (CommonConstants.colors.length % function.getId())];
			renderer.setSeriesPaint(function.getId(), c);
			renderer.setSeriesLinesVisible(function.getId(), false);
		}
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setRenderer(renderer);
		chartViewer.setChart(chart);

	}

	/**
	 * Transforms a DataSet to a Jfreechart representation, called XYSeries.
	 * 
	 * @param name
	 *            Name of the current series will be shown in the legend
	 * @param xParameter
	 *            name of the independent parameter
	 * @param yParameter
	 *            name of the dependent parameter
	 * @param dataset
	 *            DataSet to be transformed
	 * @return
	 */
	private XYSeries getXYSeries(String name, ParameterDefinition xParameter, ParameterDefinition yParameter, SimpleDataSet dataset) {
		org.jfree.data.xy.XYSeries series = new XYSeries(name, true, true);

		for (SimpleDataSetRow row : dataset) {
			ParameterValue<?> xValue = row.getParameterValue(xParameter);
			ParameterValue<?> yValue = row.getParameterValue(yParameter);
			double x = xValue.getValueAsDouble();
			double y = yValue.getValueAsDouble();
			series.add(x, y);

		}
		return series;
	}

	/**
	 * Transforms a DataSet to a Jfreechart representation, called XYSeries.
	 * 
	 * @param name
	 *            Name of the current series will be shown in the legend
	 * @param xParameter
	 *            name of the independent parameter
	 * @param yParameter
	 *            name of the dependent parameter
	 * @param dataset
	 *            DataSet to be transformed
	 * @return
	 */
	private double[][] getDataSeries(String name, ParameterDefinition xParameter, ParameterDefinition yParameter, SimpleDataSet dataset) {
		double[][] values = new double[2][dataset.size()];
		int j = 0;
		for (SimpleDataSetRow row : dataset) {
			ParameterValue<?> xValue = row.getParameterValue(xParameter);
			ParameterValue<?> yValue = row.getParameterValue(yParameter);
			values[0][j] = xValue.getValueAsDouble();
			values[1][j] = yValue.getValueAsDouble();
			j++;
		}
		return values;
	}

}
