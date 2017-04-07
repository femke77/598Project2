package sound;

import lejos.robotics.SampleProvider;
import lejos.robotics.filter.AbstractFilter;

public class SoundSensor extends AbstractFilter{

	float[] sample;
	
	public SoundSensor(SampleProvider source) {
		super(source);
		sample = new float[sampleSize];
		
	}

//FIXME  
	public boolean music(){
		super.fetchSample(sample, 0);
		
		System.out.println("ss "+ sample[0]); //debug
		if (sample[0] > .7){  
			return true;
		}
		else{
			return false;
		}
	}
	
	
	
	
	
}//end class
