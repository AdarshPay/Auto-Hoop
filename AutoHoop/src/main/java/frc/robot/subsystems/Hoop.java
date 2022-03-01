// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Gains;
import frc.robot.Constants.HoopConstants;
import frc.robot.Constants.UnitConstants;

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

    hMaster.setSoftLimit(SoftLimitDirection.kForward, HoopConstants.kForwardLimits[0]);
    hMaster.setSoftLimit(SoftLimitDirection.kReverse, HoopConstants.kReverseLimits[0]);

    hFollower.setSoftLimit(SoftLimitDirection.kForward, HoopConstants.kForwardLimits[1]);
    hFollower.setSoftLimit(SoftLimitDirection.kReverse, HoopConstants.kReverseLimits[1]);

    vMaster.setSoftLimit(SoftLimitDirection.kForward, HoopConstants.kForwardLimits[2]);
    vMaster.setSoftLimit(SoftLimitDirection.kReverse, HoopConstants.kReverseLimits[2]);
    
    setSoftLimits(true);
  }

  /**
   * Set soft limits to enabled or disabled.
   * @param set State to set limits to.
   */
  public void setSoftLimits(boolean set) {
    hMaster.enableSoftLimit(SoftLimitDirection.kForward, set);
    hMaster.enableSoftLimit(SoftLimitDirection.kReverse, set);

    hFollower.enableSoftLimit(SoftLimitDirection.kForward, set);
    hFollower.enableSoftLimit(SoftLimitDirection.kReverse, set);
    
    vMaster.enableSoftLimit(SoftLimitDirection.kForward, set);
    vMaster.enableSoftLimit(SoftLimitDirection.kReverse, set);
  }

  /**
   * Configure PID constants for hoop motors.
   */
  public void configPIDs() {    
    hMasterGains.setSparkMAXGains(hMasterCtrl);
    hFollowerGains.setSparkMAXGains(hFollowerCtrl);
    vMasterGains.setSparkMAXGains(vMasterCtrl);
  }

  /**
   * Updates/puts values on SmartDashboard, for debugging purposes.
   */
  public void updateSmartDash() {
    SmartDashboard.putNumberArray("Motor Positions", getMotorPos());
    SmartDashboard.putNumberArray("Motor Velocities", getMotorVel());
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    updateSmartDash();
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

  /**
   * Set motors to move to position (x, y), where (0, 0) is the center of the frame.
   * @param x Horizontal distance in ft.
   * @param y Vertical distance in ft.
   */
  public void setToPos(double x, double y) {
    x = x * UnitConstants.kFeetToRotations;
    y = y * UnitConstants.kFeetToRotations;

    hMasterCtrl.setReference(x, ControlType.kPosition);
    vMasterCtrl.setReference(y, ControlType.kPosition);
  }

  /**
   * Returns motor positions.
   * @return Array of motor positions in feet [hMaster, hFollower, vMaster].
   */
  public double[] getMotorPos() {
    double[] ret = new double[3];

    ret[0] = hMasterEnc.getPosition() * UnitConstants.kRotationsToFeet;
    ret[1] = hFollowerEnc.getPosition() * UnitConstants.kRotationsToFeet;
    ret[2] = vMasterEnc.getPosition() * UnitConstants.kRotationsToFeet;

    return ret;
  }
  
  /**
   * Returns motor velocities.
   * @return Array of motor velocities in rpm [hMaster, hFollower, vMaster].
   */
  public double[] getMotorVel() {
    double[] ret = new double[3];

    ret[0] = hMasterEnc.getVelocity();
    ret[1] = hFollowerEnc.getVelocity();
    ret[2] = vMasterEnc.getVelocity();

    return ret;
  }

}
