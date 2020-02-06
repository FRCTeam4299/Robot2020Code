package frc.robot;

import com.ctre.phoenix.motorcontrol.can.*;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.drive.*;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class Robot extends TimedRobot {
  WPI_VictorSPX _talonL1 = new WPI_VictorSPX(1);
  WPI_VictorSPX _talonL2 = new WPI_VictorSPX(2);
  WPI_VictorSPX _talonR1 = new WPI_VictorSPX(3);
  WPI_VictorSPX _talonR2 = new WPI_VictorSPX(4);
  SpeedControllerGroup _left = new SpeedControllerGroup(_talonL1, _talonL2);
  SpeedControllerGroup _right = new SpeedControllerGroup(_talonR1, _talonR2);
  DifferentialDrive _drive = new DifferentialDrive(_left, _right);
  Joystick _joystick = new Joystick(0);
  Gyro gyro = new ADXRS450_Gyro();
  //Servo servo = new Servo(0);

  @Override
  public void teleopInit() {
    /* factory default values */
    _talonL1.configFactoryDefault();
    _talonL2.configFactoryDefault();
    _talonR1.configFactoryDefault();
    _talonR2.configFactoryDefault();

    /* flip values so robot moves forward when stick-forward/LEDs-green */
    _talonL1.setInverted(true); // <<<<<< Adjust this
    _talonL2.setInverted(true); // <<<<<< Adjust this
    _talonR1.setInverted(true); // <<<<<< Adjust this
    _talonR2.setInverted(true); // <<<<<< Adjust this


    /*
     * WPI drivetrain classes defaultly assume left and right are opposite. call
     * this so we can apply + to both sides when moving forward. DO NOT CHANGE
     */
    _drive.setRightSideInverted(true);
  }

  @Override
  public void teleopPeriodic() {
    double x = _joystick.getY();
    double rotationRate = _joystick.getX();
    boolean triggerPressed = _joystick.getTrigger();

    // _drive.tankDrive(left,right);
    if (triggerPressed) {
      x /= 2;
      rotationRate /= 2;
    }
    //servo.set(Math.abs(_joystick.getX()));
    // System.out.println(servo.get() + " " + servo.getAngle());
    
    //System.out.println(gyro.getAngle());
    
    _drive.arcadeDrive(x, -rotationRate);

    @Override
    public void autonomousPeriodic() {
  }
}