/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Encoder;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;


/**
 * Add your docs here.
 */
public class RearLiftMechanism extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  public WPI_TalonSRX rearLiftMotor1 = new WPI_TalonSRX(RobotMap.p_rearLiftMotor1);
  public WPI_TalonSRX rearLiftMotor2 = new WPI_TalonSRX(RobotMap.p_rearLiftMotor2);
  public WPI_TalonSRX rearDriveMotor = new WPI_TalonSRX(RobotMap.p_rearDriveMotor);
  public DigitalInput rearLowerLimit = new DigitalInput(RobotMap.p_rearLowerLimit);// bottom of the lift when stationary
  public DigitalInput rearUpperLimit = new DigitalInput(RobotMap.p_rearUpperLimit);// the top of the lift mechanism when stationary

  static final double KP = 0.4;
  static final double KI = 0.0;
  static final double KD = 0.00;
  static final double KF = 0.02;
  static final double KToleranceDegrees = 2.0f;
  static final int SLOT_IDX = 0;
  static final int kTimeoutMS = 1000;

  static final double lowerRearPosition = -1000;

  public boolean lowered = false;
  

  @Override
  public void initDefaultCommand() {
    rearLiftMotor2.follow(rearLiftMotor1);
    rearLiftMotor2.setInverted(true);
    rearLiftMotor1.setNeutralMode(NeutralMode.Brake);
    rearLiftMotor2.setNeutralMode(NeutralMode.Brake);
    rearLiftMotor1.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, kTimeoutMS);
    rearLiftMotor1.configNominalOutputForward(0, kTimeoutMS);
    rearLiftMotor1.configNominalOutputReverse(0, kTimeoutMS);
    rearLiftMotor1.configPeakOutputForward(0.45, kTimeoutMS);
    rearLiftMotor1.configPeakOutputReverse(-0.45, kTimeoutMS);
    rearLiftMotor1.selectProfileSlot(SLOT_IDX, 0);
    rearLiftMotor1.config_kF(SLOT_IDX, KF, kTimeoutMS);
    rearLiftMotor1.config_kP(SLOT_IDX, KP, kTimeoutMS);
    rearLiftMotor1.config_kI(SLOT_IDX, KI, kTimeoutMS);
    rearLiftMotor1.config_kD(SLOT_IDX, KD, kTimeoutMS);

  }
  public void raise(){
    // if(rearUpperLimit.get()){
    //   moveLiftMotor(0.0);
    //   lowered = false;
    // }
    // else{
      // moveLiftMotor(0.6);
    // } 

    rearLiftMotor1.set(ControlMode.Position, 0);
  }
  public void lower(){
    // if(rearLowerLimit.get()){
    //   moveLiftMotor(0.0);
    //   lowered = true;
    // }
    // else{
      // moveLiftMotor(-0.6);
    // } 

    rearLiftMotor1.set(ControlMode.Position, lowerRearPosition);
  }

  public void moveLiftMotor(double speed){
    rearLiftMotor1.set(speed);
  }
  public void moveDriveMotor(double speed){
    // if(isLowered()){
      rearDriveMotor.set(speed);
    // }
    // else rearDriveMotor.set(0.0);
  }

  public void checkLimits(){
    if(rearUpperLimit.get()){
      lowered = false;
    }
    if(rearLowerLimit.get()){
      lowered = true;
    }
  }
  // public boolean isUpperLimit(){
  //   if(rearUpperLimit.get()){
  //     lowered = true;
  //     // moveLiftMotor(0.0);
  //   }
  //   return rearUpperLimit.get();
  //   // return false;
  // }
  // public boolean isLowerLimit(){
  //   if(rearLowerLimit.get()){
  //     lowered = false;
  //     // moveLiftMotor(0.0);
  //   }
  //   return rearLowerLimit.get();
  //   // return false;
  // }
  public boolean isLowered(){
    return lowered;
  }
  
}
