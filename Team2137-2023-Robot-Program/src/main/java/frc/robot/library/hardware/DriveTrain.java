package frc.robot.library.hardware;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.functions.io.xmlreader.EntityGroup;
import frc.robot.library.Constants;

public interface DriveTrain {

    default void setSpeed(double speed) {
        setLeftSpeed(speed);
        setRightSpeed(speed);
    }

    void setLeftSpeed(double speed);
    void setRightSpeed(double speed);

    Rotation2d getAngle();

    void setAngleOffset(Rotation2d offset);

    void configDrivetrainControlType(Constants.DriveControlType control);
}
