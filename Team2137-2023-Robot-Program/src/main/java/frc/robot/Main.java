// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.spline.PoseWithCurvature;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.functions.io.FileLogger;
import frc.robot.functions.io.xmlreader.EntityGroup;
import frc.robot.functions.io.xmlreader.XMLSettingReader;
import frc.robot.functions.io.xmlreader.XMLStepReader;
import frc.robot.functions.io.xmlreader.data.Step;
import frc.robot.functions.splines.QuinticSpline;
import frc.robot.functions.splines.VelocityGenerator;
import frc.robot.library.Constants;
import frc.robot.library.hardware.swerve.SwerveDrivetrain;
import frc.robot.library.hardware.swerve.module.SwerveModuleState;
import frc.robot.library.units.Distance2d;
import frc.robot.library.units.Speed2d;
import frc.robot.library.units.Time2d;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Do NOT add any static variables to this class, or any initialization at all. Unless you know what
 * you are doing, do not modify this file except to change the parameter class to the startRobot call.
 */
public final class Main
{
    private Main() {}

    public static FileLogger fileLogger;
   /**
    * Main initialization method. Do not perform any initialization here.
    * <p>
    * If you change your main Robot class (name), change the parameter type.
    */
    public static void main(String... args)
    {
        fileLogger = new FileLogger(10,Constants.RobotState.MAIN, "C:\\FRC Code\\2023\\Team2137-2023-Robot-Program\\FileLogs\\");
        fileLogger.writeEvent(0, FileLogger.EventType.Status, "Started FileLogger Continuing with code...");

        testXMLFunction("C:\\FRC Code\\2023\\Team2137-2023-Robot-Program\\XMLExamples\\Settings.xml");
//        while(true) {
//            for(int i = 0; i < Robot.subSystemCallList.size(); i++){
//                Robot.subSystemCallList.get(i).periodic();
//            }
//        }

        fileLogger.close();
        //return;
        //RobotBase.startRobot(Robot::new);
    }

    /**
     * Test XML Reading and writing....
     */
    public static void testXMLFunction(String settingDirectory) {
        XMLSettingReader settingReader = new XMLSettingReader(settingDirectory, true, fileLogger);

        EntityGroup robotHardware = settingReader.getRobot();

        SwerveDrivetrain swerveDrivetrain = (SwerveDrivetrain) robotHardware.getEntityGroupByType("DriveTrain");

        XMLStepReader stepReader = new XMLStepReader("C:\\FRC Code\\2023\\Team2137-2023-Robot-Program\\XMLExamples\\", "Team2137Red1.xml");
//        SwerveModuleState[] states = swerveDrivetrain.calculateSwerveMotorSpeeds(0, 0, 0, 3, 3, Constants.DriveControlType.RAW);
//
//        swerveDrivetrain.setSwerveModuleStates(states);
//        swerveDrivetrain.logModuleStates(states);
        List<Translation2d> wayPointList = new ArrayList<>();
        for(Step step : stepReader.getSteps()) {
            if(step.getCommand().equalsIgnoreCase("drive")) {
                wayPointList.add(new Translation2d(step.getXDistance(), step.getYDistance()));
            }
        }
        testSpline(swerveDrivetrain, wayPointList);
//        for(int i = 5; i < 25; i+=3.5) {
//            swerveDrivetrain.currentRobotPosition = new Translation2d(i, i);
//            SwerveModuleState[] states = swerveDrivetrain.calculateSwerveMotorSpeeds(0, 0, 1, 3, 3, Constants.DriveControlType.RAW);
//
//            swerveDrivetrain.setSwerveModuleStates(states);
//            swerveDrivetrain.logModuleStates(states);
//        }
        fileLogger.flush();
    }

    public static void testSpline(SwerveDrivetrain drivetrain, List<Translation2d> waypoints) {
//        list.add(new Translation2d(10, 20));
//        list.add(new Translation2d(5, 10));
//        list.add(new Translation2d(0, 15));

        QuinticSpline spline = new QuinticSpline(waypoints, 0.8, Rotation2d.fromDegrees(0), Rotation2d.fromDegrees(0));

        List<PoseWithCurvature> tmp = spline.getSplinePoints();

        VelocityGenerator velocityGenerator = new VelocityGenerator(tmp, Speed2d.fromFeetPerSecond(1), Speed2d.fromFeetPerSecond(.25), 1);
        List<Speed2d> tmp2 = velocityGenerator.getSpeeds();

        for(int i = 0; i < tmp.size() - 1; i += 8) {
            drivetrain.currentRobotPosition = new Translation2d(tmp.get(i).poseMeters.getX(), tmp.get(i).poseMeters.getY());

            double dy = tmp.get(i + 1).poseMeters.getY() - tmp.get(i).poseMeters.getY();
            double dx = tmp.get(i + 1).poseMeters.getX() - tmp.get(i).poseMeters.getX();
            double a = Math.atan2(dy, dx);

            SwerveModuleState[] states = drivetrain.calculateSwerveMotorSpeeds(Math.cos(a), Math.sin(a), 0, 3, 3, Constants.DriveControlType.RAW);
//            SwerveModuleState[] states = drivetrain.calculateSwerveMotorSpeeds(1, 0, 0, 3, 3, Constants.DriveControlType.RAW);

//            SwerveModuleState lfState = new SwerveModuleState(1, new Rotation2d(a), SwerveModuleState.SwerveModulePositions.LEFT_FRONT);
//            SwerveModuleState lbState = new SwerveModuleState(1, new Rotation2d(a), SwerveModuleState.SwerveModulePositions.LEFT_BACK);
//            SwerveModuleState rfState = new SwerveModuleState(1, new Rotation2d(a), SwerveModuleState.SwerveModulePositions.RIGHT_FRONT);
//            SwerveModuleState rbState = new SwerveModuleState(1, new Rotation2d(a), SwerveModuleState.SwerveModulePositions.RIGHT_BACK);

//            drivetrain.setSwerveModuleStates(new SwerveModuleState[] {lfState, lbState, rfState, rbState});
            drivetrain.setSwerveModuleStates(states);
            drivetrain.logModuleStates(states);
        }
    }
}
