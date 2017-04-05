package project2;

import lejos.robotics.SampleProvider;
import lejos.robotics.filter.AbstractFilter;

public class SoundSensor extends AbstractFilter {

	float[] sample;
	
	public SoundSensor(SampleProvider source) {
		super(source);
		sample = new float[sampleSize];
		
	}



	public boolean music(){
		super.fetchSample(sample, 0);
		if (sample[0] < 60){   //assume music is >= 60 DB  ?? 
			return false;
		}
		else{
			return true;
		}
	}
	
	
	
	
	
}//end class