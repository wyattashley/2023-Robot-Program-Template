package frc.robot.library;

import frc.robot.functions.io.FileLogger;
import frc.robot.functions.io.xmlreader.XMLSettingReader;

public interface OpMode {

    /**
     * Runs once when the robot state changed.
     */
    void init(XMLSettingReader xmlSettingReader, FileLogger fileLogger);

    /**
     * Repeats until robot state is changed.
     */
    void periodic();

    /**
     * Runs once after the robot state has changed.
     */
    void end();
}
