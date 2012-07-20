package org.sopeco.analysis.wrapper;

public class Calculator implements Runnable {


	double result;
	double result_2;
	double a;
	double b;
	
	public Calculator(double a,double b) {
		this.a = a;
		this.b = b;
	}
	
	@Override
	public void run() {
		AnalysisWrapper wrapper = new AnalysisWrapper();
		wrapper.executeCommandString("a <- " + a);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		result = wrapper.executeCommandDouble("a");
		wrapper.executeCommandString("b <- " + b);
		result_2 = wrapper.executeCommandDouble("a*b");
		wrapper.shutdown();
	}

	public double getResult() {
		return result;
	}
	public double getResult2() {
		return result_2;
	}

}
