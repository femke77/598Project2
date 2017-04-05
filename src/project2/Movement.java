package project2;

//ToDo:
// afix the color sensor and the sound sensor properly
// tighten up the move towards center ? (requires one additional color sensor
// movement by sound is coded, just needs testing. 

import java.util.Random;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.NXTSoundSensor;
import lejos.robotics.Color;
import lejos.robotics.SampleProvider;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Delay;

public class Movement {

	MovePilot pilot;
	Random rand;

	EV3TouchSensor sensor;
	TouchSensor touch;

	NXTSoundSensor s_sensor;
	SoundSensor sound;

	EV3ColorSensor colorSensor;
	SampleProvider colorProvider;
	float[] colorSample;

	public static void main(String[] args) {
		new Movement();
	}

	// Movement is controlled by two sensors, the touch sensor, and the color sensor.
	// Touch changes direction/angle
	// Color controls where the robot ultimately stops, hopefully attempting to get to the center

	public Movement() {
		intro();
		// approximate measurements in inches, measure.
		Wheel leftWheel = WheeledChassis.modelWheel(Motor.B, 1.5f).offset(-2.25);
		Wheel rightWheel = WheeledChassis.modelWheel(Motor.C, 1.5f).offset(2.25);
		Chassis chassis = new WheeledChassis(new Wheel[] { leftWheel, rightWheel }, WheeledChassis.TYPE_DIFFERENTIAL);
		pilot = new MovePilot(chassis);
		rand = new Random();
		Brick brick = BrickFinder.getDefault();
		Port s3 = brick.getPort("S3"); // port 3 is where the touch sensor is
		Port s1 = brick.getPort("S1"); // port 1 is one color sensor
		Port s2 = brick.getPort("S2"); // port 2 is one sound sensor

		touch = new TouchSensor(sensor);
		sensor = new EV3TouchSensor(s3);

		s_sensor = new NXTSoundSensor(s2);
		sound = new SoundSensor(s_sensor.getDBAMode());

		colorSensor = new EV3ColorSensor(s1);
		colorProvider = colorSensor.getColorIDMode();   //recently changed and not fully tested. 
	                                                	//Used to be getRGBMode() but that occasionally stopped on wrong color
		colorSample = new float[colorProvider.sampleSize()];

		pilot.setLinearSpeed(4.5f); // speed is inches per second

		while (true) {
			Delay.msDelay(2);
			colorProvider.fetchSample(colorSample, 0); // start fetching color samples

			if (sound.music() == true) {
				pilot.forward();
				if (colorSensor.getColorID() == Color.BLACK) {
					pilot.stop();

				}
				if (touch.pressed()) { // if pressed is true
					pilot.stop();
					pilot.travel(-6); // back up 6 inches
					if (rand.nextBoolean()) {
						pilot.rotate(45); // right rotate
					} else {
						pilot.rotate(-45); // left rotate
					}
					pilot.forward();
					if (colorSample[0] == Color.BLACK) {
						pilot.stop();
					}
				}

			} else {
				pilot.stop();
			}
			
			// user cancel:
			if (Button.ESCAPE.isDown()) {
				pilot.stop();
				sensor.close();
				colorSensor.close();
				s_sensor.close();
				System.exit(0);
			}
		}
	}

	// this method starts the program gracefully. Hit go when ready or escape to quit
	public void intro() {

		GraphicsLCD g = LocalEV3.get().getGraphicsLCD();
		g.setFont(Font.getSmallFont());
		int y_quit = 100;
		int width_quit = 45;
		int height_quit = width_quit / 2;
		int arc_diam = 6;
		g.drawString("QUIT", 9, y_quit + 7, 0);
		g.drawLine(0, y_quit, 45, y_quit); // top line
		g.drawLine(0, y_quit, 0, y_quit + height_quit - arc_diam / 2); // left
		g.drawLine(width_quit, y_quit, width_quit, y_quit + height_quit / 2); // right
		g.drawLine(0 + arc_diam / 2, y_quit + height_quit, width_quit - 10, y_quit + height_quit); // bottom
		g.drawLine(width_quit - 10, y_quit + height_quit, width_quit, y_quit + height_quit / 2); // diagonal
		g.drawArc(0, y_quit + height_quit - arc_diam, arc_diam, arc_diam, 180, 90);
		g.fillRect(width_quit + 10, y_quit, height_quit, height_quit);
		g.drawString("GO", width_quit + 15, y_quit + 7, 0, true);
		Button.waitForAnyPress();
		if (Button.ESCAPE.isDown())
			System.exit(0);
		g.clear();
	}

}// end class movement
