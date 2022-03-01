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
  
  public static final class CANConstants {
    
    public static final int khMasterID = 0;
    public static final int khFollowerID = 1;
    public static final int kvMasterID = 2;
  }
  public static final class HoopConstants {

    // gains
    public static final Gains khMasterGains = new Gains(0.1, 0.0, 0.0, 0.0, 0, 1.0);
    public static final Gains khFollowerGains = new Gains(0.1, 0.0, 0.0, 0.0, 0, 1.0);
    public static final Gains kvMasterGains = new Gains(0.1, 0.0, 0.0, 0.0, 0, 1.0);

    // soft limits
    public static final float[] kForwardLimits = new float[] {0, 0, 0}; // TODO: put actual numbers here
    public static final float[] kReverseLimits = new float[] {0, 0, 0}; // TODO: put actual numbers here
  }

  public static final class UnitConstants {
    public static final double kFeetToRotations = 0.0; // TODO: put actual number here
    public static final double kRotationsToFeet = 0.0; // TODO: put actual number here
  }

}
