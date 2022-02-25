// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/** Add your docs here. */
public class Gains {
  public double kP;
  public double kI;
  public double kD;
  public double kF;
  public double kIzone;
  public double kPeakOutput;
  public double kMaxOutput;
  public double kMinOutput;

    /**
   * Creates Gains object for PIDs
   * @param p The P value.
   * @param i The I value.
   * @param d The D value.
   * @param f The F value.
   * @param izone The zone of the I value.
   * @param peakOutput The peak output setting the motors to run the gains at, in both forward and reverse directions. By default 1.0.
   */
  public Gains(double p, double i, double d, double f, int izone, double peakOutput)
  {
    kP = p;
    kI = i;
    kD = d;
    kF = f;
    kIzone = izone;
    kPeakOutput = peakOutput;
    kMaxOutput = peakOutput;
    kMinOutput = -peakOutput;
  }
}
