package project2;

import lejos.robotics.SampleProvider;
import lejos.robotics.filter.AbstractFilter;

public class TouchSensor extends AbstractFilter {

	float[] sample;
	
	
	public TouchSensor(SampleProvider source) {
		super(source);
		sample = new float[sampleSize];
	}

	public boolean pressed(){
		super.fetchSample(sample, 0);
		if (sample[0] == 0){ //not pressed
			return false;
		}
		else{
			return true; //pressed
		}
	}
	
	
	
	
	
}//end class
