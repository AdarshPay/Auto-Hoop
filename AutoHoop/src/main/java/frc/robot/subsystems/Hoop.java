// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Gains;
import frc.robot.Constants.HoopConstants;

public class Hoop extends SubsystemBase {
  
  private CANSparkMax hMaster;
  private CANSparkMax hFollower;
  private CANSparkMax vMaster;

  private SparkMaxPIDController hMasterCtrl;
  private SparkMaxPIDController hFollowerCtrl;
  private SparkMaxPIDController vMasterCtrl;

  private Gains hMasterGains = HoopConstants.khMasterGains;
  private Gains hFollowerGains = HoopConstants.khFollowerGains;
  private Gains vMasterGains = HoopConstants.kvMasterGains;

  private RelativeEncoder hMasterEnc;
  private RelativeEncoder hFollowerEnc;
  private RelativeEncoder vMasterEnc;

  /** Creates a new Hoop. */
  public Hoop(CANSparkMax m1, CANSparkMax m2, CANSparkMax m3) {
    hMaster = m1;
    hFollower = m2;
    vMaster = m3;

    hMasterCtrl = m1.getPIDController();
    hFollowerCtrl = m2.getPIDController();
    vMasterCtrl = m3.getPIDController();

    hMasterEnc = m1.getEncoder();
    hFollowerEnc = m2.getEncoder();
    vMasterEnc = m3.getEncoder();

    configMotors();
    configPIDs();
  }

  /**
   * Configure hoop motors.
   */
  public void configMotors() {

    hMaster.restoreFactoryDefaults();
    hFollower.restoreFactoryDefaults();
    vMaster.restoreFactoryDefaults();

    hFollower.follow(hMaster);

    hMaster.setIdleMode(IdleMode.kBrake);
    hFollower.setIdleMode(IdleMode.kBrake);
		vMaster.setIdleMode(IdleMode.kBrake);

		hMaster.setInverted(false);
		hFollower.setInverted(false);
		vMaster.setInverted(false);
  }

  /**
   * Configure PID constants for hoop motors.
   */
  public void configPIDs() {    

    hMasterCtrl.setP(hMasterGains.kP);
    hMasterCtrl.setI(hMasterGains.kI);
    hMasterCtrl.setD(hMasterGains.kD);
    hMasterCtrl.setFF(hMasterGains.kF);
    hMasterCtrl.setIZone(hMasterGains.kIzone);
    hMasterCtrl.setOutputRange(hMasterGains.kMinOutput, hMasterGains.kMaxOutput);

    hFollowerCtrl.setP(hFollowerGains.kP);
    hFollowerCtrl.setI(hFollowerGains.kI);
    hFollowerCtrl.setD(hFollowerGains.kD);
    hFollowerCtrl.setFF(hFollowerGains.kF);
    hFollowerCtrl.setIZone(hFollowerGains.kIzone);
    hFollowerCtrl.setOutputRange(hFollowerGains.kMinOutput, hFollowerGains.kMaxOutput);

    vMasterCtrl.setP(vMasterGains.kP);
    vMasterCtrl.setI(vMasterGains.kI);
    vMasterCtrl.setD(vMasterGains.kD);
    vMasterCtrl.setFF(vMasterGains.kF);
    vMasterCtrl.setIZone(vMasterGains.kIzone);
    vMasterCtrl.setOutputRange(vMasterGains.kMinOutput, vMasterGains.kMaxOutput);
  }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  /**
   * Run all hoop motors at given outputs, respectively.
   * @param output1 Output for first motor (between -1 and 1).
   * @param output2 Output for second motor (between -1 and 1).
   * @param output3 Output for third motor (between -1 and 1).
   */
  public void runOutput(double output1, double output2, double output3) {
    hMaster.set(output1);
    hMaster.set(output2);
    hMaster.set(output3);
  } 

  /**
   * Runs motors at target velocity.
   * @param targetRPM Target velocity (in rpm).
   */
  public void runVelPID(double targetRPM) {
    hMasterCtrl.setReference(targetRPM, ControlType.kVelocity);
    vMasterCtrl.setReference(targetRPM, ControlType.kVelocity);
  }
  
}
