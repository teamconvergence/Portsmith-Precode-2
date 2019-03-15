/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

// import com.ctre.phoenix.motorcontrol.can.TalonSRX;
// import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
// import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
// import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

// import edu.wpi.first.wpilibj.Encoder;
// import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.command.Subsystem;
// import edu.wpi.first.wpilibj.drive.DifferentialDrive;
// import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.PIDController;
// import edu.wpi.first.wpilibj.PIDOutput;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Add your docs here.
 */
public class WristMechanismSpark extends Subsystem{
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  public CANSparkMax wristMotor1 = new CANSparkMax(RobotMap.p_wristmotor1, MotorType.kBrushless);
  public CANSparkMax wristMotor2 = new CANSparkMax(RobotMap.p_wristmotor2, MotorType.kBrushless);

  // public CANEncoder wristEncoder = wristMotor1.getEncoder();

  public WPI_TalonSRX intakeMotor = new WPI_TalonSRX(RobotMap.p_intakemotor);

  public double speed = 0.7;

  public PIDController wristController;

  static final double KP = 0.4;
  static final double KI = 0.0;
  static final double KD = 0.00;
  static final double KF = 0.2;
  static final double KIZ = 0.0;
  static final double KFF = 0;
  // static final int kTimeoutMS = 1000;

  public static final double wristDown = -300;
  public static final double wristOut = -150;
  public static final double wristUp = 0;

  public static boolean cargoIn = false;

  public WristMechanismSpark(){
    wristMotor2.follow(wristMotor1);
    wristMotor1.setInverted(true);
    wristMotor1.setIdleMode(CANSparkMax.IdleMode.kBrake);
    wristMotor2.setIdleMode(CANSparkMax.IdleMode.kBrake);

  }
  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
  public void init(){
    wristMotor2.follow(wristMotor1);
    wristMotor2.setInverted(true);
    wristMotor1.setIdleMode(CANSparkMax.IdleMode.kBrake);
    wristMotor2.setIdleMode(CANSparkMax.IdleMode.kBrake);
    wristMotor1.getPIDController().setP(KP);
    wristMotor1.getPIDController().setI(KI);
    wristMotor1.getPIDController().setD(KD);
    wristMotor1.getPIDController().setIZone(KIZ);
    wristMotor1.getPIDController().setFF(KFF);
    wristMotor1.getPIDController().setOutputRange(-0.5, 0.5);
  }
  public void resetEncoder(){
    // wristMotor1.setSelectedSensorPosition(0);
    // wristMotor1.getPIDController().
  }
  public void moveWristMotorCounts(double counts){
    // wristMotor1.set(ControlMode.Position, counts);
    // wristMotor1.set(ControlMode.MotionMagic, counts);
  }
  public void intakeIn(){
    intakeMotor.set(-speed);
    cargoIn = true;
  }
  public void intakeOut(){
    intakeMotor.set(speed);
    cargoIn = false;
  }
  public void stopIntake(){
    intakeMotor.set(0.0);
  }
  public void maintainIntake(){
    if(cargoIn){
      intakeMotor.set(-0.7);
    }
    else intakeMotor.set(0.0);
  }

  public double getEncoderCount(){
    // return wristMotor1.getSelectedSensorPosition();
    return wristMotor1.getEncoder().getPosition();
  }

  // public boolean moveWristDown(){
  //   if(getEncoderCount()>wristDown+100){
  //     // moveWristMotor(-0.15);
  //     moveWristMotorCounts(wristDown);
  //     return false;
  //   }
  //   else if(getEncoderCount()<wristDown-100){
  //     // moveWristMotor(0.15);
  //     moveWristMotorCounts(wristDown);
  //     return false;
  //   }
  //   else {
  //     // moveWristMotor(0.0);
  //     return true;
  //   }
  // }
  // public boolean moveWristOut(){
  //   if(getEncoderCount()>wristOut+100){
  //     // moveWristMotor(-0.15);
  //     moveWristMotorCounts(wristOut);
  //     return false;
  //   }
  //   else if(getEncoderCount()<wristOut-100){
  //     // moveWristMotor(0.15);
  //     moveWristMotorCounts(wristOut);
  //     return false;
  //   }
  //   else {
  //     // moveWristMotor(0.0);
  //     return true;
  //   }
  // }
  // public boolean moveWristUp(){
  //   if(getEncoderCount()>wristUp+100){
  //     // moveWristMotor(-0.15);
  //     moveWristMotorCounts(wristUp);
  //     return false;
  //   }
  //   else if(getEncoderCount()<wristUp-100){
  //     // moveWristMotor(0.15);
  //     moveWristMotorCounts(wristUp);
  //     return false;
  //   }
  //   else {
  //     // moveWristMotor(0.0);
  //     return true;
  //   }
    
  // }
  public void setEncoderDestination(double destination){
    wristMotor1.getPIDController().setReference(destination, ControlType.kPosition);
  }
}
