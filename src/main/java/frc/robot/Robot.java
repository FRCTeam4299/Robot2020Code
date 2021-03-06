package frc.robot;

import com.ctre.phoenix.motorcontrol.can.*;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.drive.*;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class Robot extends TimedRobot {
  WPI_VictorSPX _talonL1 = new WPI_VictorSPX(2);
  WPI_VictorSPX _talonL2 = new WPI_VictorSPX(4);
  WPI_VictorSPX _talonR1 = new WPI_VictorSPX(1);
  WPI_VictorSPX _talonR2 = new WPI_VictorSPX(3);
  SpeedControllerGroup _left = new SpeedControllerGroup(_talonL1, _talonL2);
  SpeedControllerGroup _right = new SpeedControllerGroup(_talonR1, _talonR2);
  DifferentialDrive _drive = new DifferentialDrive(_left, _right);
  Joystick _joystick = new Joystick(0);
  Joystick _joystick1 = new Joystick(1);
  Gyro gyro = new ADXRS450_Gyro();
  // Spark shooter = new Spark(0);
  Spark grabber = new Spark(1);
  Spark inserter = new Spark(0);
  Relay shooter = new Relay(0, Relay.Direction.kReverse);
  // Servo servo = new Servo(0);
  long startTime = 0;
  boolean isShooting = false;
  long startTimeShootingPhase = 0;

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

    shooter.set(Relay.Value.kOff);
  }

  @Override
  public void teleopPeriodic() {
    double x = _joystick.getY();
    double rotationRate = _joystick.getX();
    boolean triggerPressed = _joystick.getTrigger();

    boolean buttonPressed1 = _joystick1.getRawButton(2);
    boolean buttonPressed2 = _joystick1.getRawButton(6);
    boolean buttonPressed3 = _joystick1.getRawButtonPressed(1);

    // double shooterSpeed = 0;
    double grabberSpeed = 0;
    double inserterSpeed = 0;

    if (buttonPressed1) {
      shooter.set(Relay.Value.kReverse);
    } else {
      shooter.set(Relay.Value.kOff);
    }

    if (buttonPressed2) {
      grabberSpeed = 1;
    }

    if (buttonPressed3) {
      inserterSpeed = -1;
    }
    // shooter.setSpeed(shooterSpeed);
    grabber.setSpeed(grabberSpeed);
    inserter.setSpeed(inserterSpeed);

    // _drive.tankDrive(left,right);
    if (triggerPressed) {
      x /= 2;
      rotationRate /= 2;
    }
    // servo.set(Math.abs(_joystick.getX()));
    // System.out.println(servo.get() + " " + servo.getAngle());

    // System.out.println(gyro.getAngle());

    _drive.arcadeDrive(x, -rotationRate);
  }

  @Override
  public void autonomousPeriodic() {
    long currentTime = System.currentTimeMillis();
    long elapsedTime = currentTime - startTime;

    if (elapsedTime < 1000) {
      _drive.arcadeDrive(0.5, 0);
    } else {
      _drive.arcadeDrive(0, 0);
    }
    if (elapsedTime > 1000 && elapsedTime < 7000) {
      shooter.set(Relay.Value.kReverse);
    }
    if (elapsedTime > 3000 && elapsedTime < 8000) {
      fireOneBall();
    }
    if (elapsedTime> 8000){
      shooter.set(Relay.Value.kOff);
    }

  }

  public void fireOneBall() {

    long currentTime = System.currentTimeMillis();
    long elapsedTime = currentTime - startTimeShootingPhase;

    if (elapsedTime > 1000) {
      isShooting = !isShooting;
      startTimeShootingPhase = currentTime;

    }

    if (isShooting) {
      inserter.set(-1);
    } else {
      inserter.set(0);
    }
  }

  @Override
  public void autonomousInit() {
    super.autonomousInit();

    startTime = System.currentTimeMillis();

  }

}