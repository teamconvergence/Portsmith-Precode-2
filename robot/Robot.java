/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import com.ctre.phoenix.motorcontrol.ControlMode;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  public static DriveTrain drivetrain = new DriveTrain();
  public static OI oi = new OI();
  public static LimelightCamera limelight = new LimelightCamera();

  //constants for autonoumous driving
  public static float kpAim = 0.04f;
  public static float kpDistance = -0.07f;
  public static float minAimCommand = 0.05f;

  //Declaration of USB Camera and Autonomous Command
  UsbCamera cam;
  public static DoubleSolenoidSubsystem sol = new DoubleSolenoidSubsystem();
  public static LimitSwitch limit = new LimitSwitch();
  public static PhotoelectricSensor photoEye = new PhotoelectricSensor();
  public static Gyroscope gyro = new Gyroscope();
  final double joystickXDampen = 0.7;
  final double joystickYDampen = 0.7;
  public boolean turn = false;
  public double elevatorEncoderDestination = 0;
  public double wristEncoderDestination = WristMechanismSpark.wristUp;
  public boolean checkPosition = false;
  public static ElevatorMechanism elevator = new ElevatorMechanism();
  public static LEDSubsystem LEDSystem = new LEDSubsystem();
  public static RearLiftMechanism rearLiftMechanism = new RearLiftMechanism();
  public static WristMechanismSpark wristMechanismSpark = new WristMechanismSpark();
  public static boolean wristTurnDownComplete;
  public static boolean wristTurnOutComplete;
  public static boolean wristTurnUpComplete;
  public static double liftHoldPosition = 0.0;
  public static double wristHoldPosition = 0.0;

  // TEST MODE VARIABLES

  public static long lastTime = 0;
  public static final long minDelay = 1000;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    //Initialize and start the USB Camera
    cam = CameraServer.getInstance().startAutomaticCapture();
    //Set the max FPS of the camera at 60
    cam.setFPS(60);
    // sol.initDefaultCommand();
    // sol.reverse();
    gyro.init(drivetrain);
    LEDSystem.rainbow();
    // wristMechanismSpark.resetEncoder();
    rearLiftMechanism.initDefaultCommand();
    elevator.resetEncoder();
    elevatorEncoderDestination = 0;
    wristMechanismSpark.init();
    wristMechanismSpark.resetEncoder();
    wristEncoderDestination = 0;
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    robotInit();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    // if(oi.isAButtonPressed()&& limelight.isTargetVisible()){
      teleopPeriodic();
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    
    // if(oi.isAButtonPressed()&& limelight.isTargetVisible()){
    if(oi.isAButtonPressed()&& limelight.isTargetVisible()){
      double xAdjust = 0.0;
      final double minSpeedX = 0.5;
      final double minSpeedY = 0.28;
      final double slowSpeedY = 0.4;
      final double xZone = 10.0;
      final double yZone = 1;
      final double maxSpeedY = 1;
      double ySpeed = 0.0;
      
      double currentX = limelight.getX();

      if(currentX>=xZone||-currentX>=xZone){
        xAdjust = kpAim*currentX;
      }
      else if(currentX>0&&currentX<xZone){
        xAdjust = minSpeedX;
      }
      else if(currentX<0&&currentX>-xZone){
        xAdjust = -minSpeedX;
      }
      else{
        xAdjust = 0;
      }
      double currentY = -limelight.getY();
      // if(currentY<yZone){
      //   ySpeed = kpDistance*currentY;
      //   if(ySpeed > maxSpeedY){
      //     ySpeed = maxSpeedY;
      //   }
      //   else if(ySpeed < -maxSpeedY){
      //     ySpeed = -maxSpeedY;
      //   }
      // }
      // else if(currentY>=yZone&&currentY<0){
      //   ySpeed = minSpeedY;
      // }
      // else if(currentY>0){
      //   ySpeed=-minSpeedY;
      // }
      // else {
      //   ySpeed = 0;
      // }
      
      
      if(currentY < 0){
        ySpeed = kpDistance*currentY;// f is for caleb
      }
      else if(currentY > 0){
        ySpeed = -minSpeedY;
      }
      else{
        ySpeed = 0;
      }

      if(ySpeed > maxSpeedY){
        ySpeed = maxSpeedY;
      }
      else if(ySpeed < -maxSpeedY){
        ySpeed = -maxSpeedY;
      }
      if(ySpeed < slowSpeedY && ySpeed > 0){
        ySpeed = slowSpeedY;
        if(currentY<0 && currentY > -yZone){
          ySpeed = minSpeedY;
        }
      }
      else if(ySpeed > -slowSpeedY && ySpeed < 0){
        ySpeed = -slowSpeedY;
        if(currentY>0 && currentY<yZone){
          ySpeed = -minSpeedY;
        }
      }
      // THIS NEEDS TO BE CHANGED LATER
      drivetrain.arcadeDrive(ySpeed*0.6, xAdjust*0.9);

    }
    else if(oi.isBButtonPressed()){
      double xAdjust = 0.0;
      if(limelight.getX()>1.0){
        xAdjust = kpAim*limelight.getX()-minAimCommand;
      }
      else{
        xAdjust = kpAim*limelight.getX()+minAimCommand;
      }
      if(Math.abs(kpDistance*limelight.getY()) < 0.35 && Math.abs(limelight.getY()) >= 1){
        drivetrain.arcadeDrive(0.35, xAdjust);
      }
      else if(Math.abs(limelight.getY()) < 1){
        drivetrain.arcadeDrive(0, xAdjust);
      }
      else{
        drivetrain.arcadeDrive(kpDistance*limelight.getY()*joystickYDampen, xAdjust);
      }
        
    }
      // IDEAL TURN RATE 0.4 
      // IDEAL SPEED 0.4
      
      // ANY POSITIVE Y CAMERA VALUE WE WANT SPEED AT -0.4
      // ANY NEGATIVE Y CAMERA VALUE GREATER THAN -10 WILL BE 0.4
      // ANYTHING LESS THAN -10 ON THE Y AXIS WILL BE A HIGHER SPEED

      //FOR X AXIS ABOVE |20| GET COMPUTED SPEED
      //FOR X AXIS BELOW |20| SET 0.4
      // drivetrain.arcadeDrive(kpDistance*limelight.getY(), xAdjust);
      // drivetrain.arcadeDrive(0.4, 0);

    // CREATE A FLAG VARIABLE TO TRIGGER A LOOP THAT MAKES IT TURN TO THE GYRO ANGLE. IT RESETS WHEN IT REACHES ITS TARGET.
    //JOYSTICKS WILL INTERUPT THE LOOP THROUGH A ! STATEMENT

    else{
      // if (Math.abs(oi.getLeftJoystick()) > .05 || Math.abs(oi.getActualRightJoystickY()) > .05) { 
      //   drivetrain.tankDrive(-oi.getLeftJoystick(),-oi.getActualRightJoystickY());
      // } 
      if (Math.abs(oi.getLeftJoystick()) > .05 || Math.abs(oi.getActualRightJoystickY()) > .05 || Math.abs(oi.getActualRightJoystickX()) > .05||Math.abs(oi.getRightJoystick()) > .05) { 
        drivetrain.arcadeDrive(-oi.getLeftJoystick()*joystickYDampen, oi.getActualRightJoystickX()*joystickXDampen);
        // drivetrain.tankDrive(-oi.getLeftJoystick()*joystickYDampen, -oi.getActualRightJoystickY()*joystickXDampen);
        turn = false;
        gyro.reset();
        gyro.disable();
        rearLiftMechanism.moveDriveMotor(-oi.getLeftJoystick()*joystickYDampen);
      }
      else if(oi.getLeftTrigger()>0){
        gyro.enable();
        gyro.setSetPoint(-90);
        // drivetrain.turnAngle();
        // SmartDashboard.putBoolean("Left Trigger:   ", true);
        turn = true;
      }
      else if(oi.getRightTrigger()>0){
        gyro.enable();
        gyro.setSetPoint(90);
        // drivetrain.turnAngle();
        // SmartDashboard.putBoolean("Right Trigger:   ", true);
        turn = true;
      }
      else {
        drivetrain.arcadeDrive(0, 0);
        // SmartDashboard.putBoolean("Left Trigger:   ", false);
        // SmartDashboard.putBoolean("Right Trigger:   ", false);
        rearLiftMechanism.moveDriveMotor(0.0);
      }

      if(turn){
        drivetrain.turnAngle();
      }
      if(gyro.onTarget()){
        turn = false;
        gyro.reset();
        gyro.disable();
      }
    }
    
    if(oi.isFloor()){ 
      elevatorEncoderDestination = ElevatorMechanism.FLOOR_POSITION;
    }
    else if(oi.isCargo1()){
      elevatorEncoderDestination = ElevatorMechanism.CARGO1_POSITION;
    }
    else if(oi.isCargo2()){
      elevatorEncoderDestination = ElevatorMechanism.CARGO2_POSITION;
    }
    else if(oi.isCargo3()){
      elevatorEncoderDestination = ElevatorMechanism.CARGO3_POSITION;
    }
    else if(oi.isPanel1()){
      elevatorEncoderDestination = ElevatorMechanism.PANEL1_POSITION;
    }
    else if(oi.isPanel2()){
      elevatorEncoderDestination = ElevatorMechanism.PANEL2_POSITION;
    }
    else if(oi.isPanel3()){
      elevatorEncoderDestination = ElevatorMechanism.PANEL3_POSITION;
    }
    elevator.setEncoderDestination(elevatorEncoderDestination);
    
    // if(elevator.upperLimit.get()){
    //   elevator.elevatorMotor1.set(ControlMode.PercentOutput, 0);
    // }

    // if(elevator.lowerLimit.get()){
    //   elevator.elevatorMotor1.set(ControlMode.PercentOutput, 0);
    // }
    
    if(oi.isCargoIn()){
      wristMechanismSpark.intakeIn();
    }
    else if(oi.isCargoOut()){
      wristMechanismSpark.intakeOut();
    }
    else {
      wristMechanismSpark.maintainIntake();
    }

    if(oi.isPanelIn()){
      sol.reversePanel();
    }
    else if(oi.isPanelOut()){
      sol.forwardPanel();
    }
    else{
      sol.solenoidOffPanel();
    }

    if(oi.isPunch()){
      sol.forwardPunch();
    }
    else{
      sol.reversePunch();
    }

    // LOCK SOLENOID CODE USING isCargo1 AND 2
    // if(oi.isCargo2()){
    //   sol.forwardLock();
    // }
    // else if(oi.isCargo1()){
    //   sol.reverseLock();
    // }
    // else{
    //   sol.solenoidOffLock();
    // }

    if(oi.isWristDown()){
      // wristMechanismSpark.moveWristDown();
      wristEncoderDestination = WristMechanismSpark.wristDown;
      wristMechanismSpark.wristMotor1.set(0.5);
      wristMechanismSpark.wristMotor2.set(0.5);
    }
    else if(oi.isWristOut()){
      // wristMechanismSpark.moveWristOut();
      wristEncoderDestination = WristMechanismSpark.wristOut;
    }
    else if(oi.isWristUp()){
      // wristMechanismSpark.moveWristUp();
      wristEncoderDestination = WristMechanismSpark.wristUp;
    }

    wristMechanismSpark.setEncoderDestination(wristEncoderDestination);

    rearLiftMechanism.checkLimits();

    if(oi.isRightBumper()){
      rearLiftMechanism.raise();
      // LEDSystem.pattern(0.77);
      LEDSystem.pattern(0.29);
      // wristMechanismSpark.resetEncoder();
    }
    else if(oi.isLeftBumper()){
      rearLiftMechanism.lower();
      // LEDSystem.pattern(0.63);
      LEDSystem.pattern(0.09);
    }
    // else {
    //   rearLiftMechanism.moveLiftMotor(0.0);
    // }
    
    gyro.updateGyro();

    SmartDashboard.putBoolean("turn:   ", turn);
    SmartDashboard.putNumber("Elevator Encoder Count:   ", elevator.getEncoderCount());
    SmartDashboard.putNumber("Wrist Encoder Count:   ", wristMechanismSpark.getEncoderCount());
    // SmartDashboard.putBoolean("rearUpperLimit:   ", rearLiftMechanism.rearUpperLimit.get());
    // SmartDashboard.putBoolean("rearLowerLimit:   ", rearLiftMechanism.rearLowerLimit.get());
    // SmartDashboard.putBoolean("frontUpperLimit:   ", elevator.upperLimit.get());
    // SmartDashboard.putBoolean("frontLowerLimit:   ", elevator.lowerLimit.get());
    // SmartDashboard.putBoolean("isLowered:   ", rearLiftMechanism.isLowered());
    
    limelight.readCamera();
    limelight.updateDashboard();
    oi.checkPanel();

  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    if(oi.isXButtonPressed()){
      elevator.resetEncoder();
      wristMechanismSpark.resetEncoder(); 
      liftHoldPosition = 0;
      wristHoldPosition = 0;
      elevatorEncoderDestination = 0;
    }
    // if(oi.isAButtonPressed()){
    //   elevator.elevatorMotor1.set(ControlMode.PercentOutput, 0.1);
    //   liftHoldPosition = elevator.getEncoderCount();
    // }
    // else if(oi.isBButtonPressed()){
    //   elevator.elevatorMotor1.set(ControlMode.PercentOutput, -0.1);
    //   liftHoldPosition = elevator.getEncoderCount();
    // }
    // else elevator.elevatorMotor1.set(ControlMode.Position, liftHoldPosition);
    long currentTime = System.currentTimeMillis();
    if(oi.isRightBumper()&& (currentTime > (lastTime + minDelay))){

      liftHoldPosition += 500;
      lastTime = currentTime;

    }
    else if(oi.isLeftBumper()&& (currentTime > (lastTime + minDelay))){
      if(liftHoldPosition>0 && elevator.getEncoderCount()>500){
        liftHoldPosition -= 500;
      }
      lastTime = currentTime;
    }
    else elevator.elevatorMotor1.set(ControlMode.Position, liftHoldPosition);
    boolean pressed = false;


    if(oi.isRightBumper()&& !pressed){
      liftHoldPosition += 500;
      pressed = true;
    }
    else if(oi.isLeftBumper()&&!pressed){
      liftHoldPosition -= 500;
      pressed = true;
    }
    else if(elevator.getEncoderCount() == liftHoldPosition+100 && elevator.getEncoderCount() == liftHoldPosition-100){
      pressed = false;
    }
    else elevator.elevatorMotor1.set(ControlMode.Position, liftHoldPosition);

    // if(oi.isRightBumper()){
    //   wristMechanism.wristMotor1.set(ControlMode.PercentOutput, 0.1);
    //   wristHoldPosition = wristMechanism.wristMotor1.getSelectedSensorPosition();
    // }
    // else if(oi.isLeftBumper()){
    //   wristMechanism.wristMotor1.set(ControlMode.PercentOutput, -0.1);
    //   wristHoldPosition = wristMechanism.wristMotor1.getSelectedSensorPosition();
    // }
    // else wristMechanism.wristMotor1.set(ControlMode.Position, wristHoldPosition);
    if (Math.abs(oi.getRightJoystick()) > .05) {
      // wristMechanism.wristMotor1.set(ControlMode.PercentOutput, oi.getRightJoystick());
    }
    // else wristMechanism.wristMotor1.set(ControlMode.PercentOutput, 0);

  }
}