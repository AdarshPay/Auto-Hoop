// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.Constants.CANConstants;

/** Add your docs here. */
public class RobotMap {
  public CANSparkMax hMaster = new CANSparkMax(CANConstants.khMasterID, MotorType.kBrushless);
  public CANSparkMax hFollower = new CANSparkMax(CANConstants.khFollowerID, MotorType.kBrushless);
  public CANSparkMax vMaster = new CANSparkMax(CANConstants.kvMasterID, MotorType.kBrushless);
}
