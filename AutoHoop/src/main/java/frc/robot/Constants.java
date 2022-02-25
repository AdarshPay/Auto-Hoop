// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

  public static final class HoopConstants {

    // can ids
    public static final int kMotor1ID = 0;
    public static final int kMotor2ID = 1;
    public static final int kMotor3ID = 2;

    // gains
    public static final Gains khMasterGains = new Gains(0.1, 0.0, 0.0, 0.0, 0, 1.0);
    public static final Gains khFollowerGains = new Gains(0.1, 0.0, 0.0, 0.0, 0, 1.0);
    public static final Gains kvMasterGains = new Gains(0.1, 0.0, 0.0, 0.0, 0, 1.0);
  }

}
