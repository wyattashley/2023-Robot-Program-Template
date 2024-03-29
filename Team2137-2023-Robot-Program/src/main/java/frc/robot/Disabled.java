//              Copyright 2022 Wyatt Ashley
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.functions.io.FileLogger;
import frc.robot.functions.io.xmlreader.XMLSettingReader;
import frc.robot.functions.io.xmlreader.XMLStepReader;
import frc.robot.library.Constants;
import frc.robot.library.OpMode;

public class Disabled implements OpMode {

    private FileLogger logger;
    private final int mintDebug = 0;

    @Override
    public void init(XMLSettingReader xmlSettingReader, XMLStepReader xmlStepReader, FileLogger fileLogger) {
//        this.logger = new FileLogger(mintDebug, Constants.RobotState.DISABLED);
        xmlSettingReader.getRobot().getEntityGroupByType("DriveTrain").getHardwareEntity("Pigeon").setOnChangeCallback(() -> {
            System.out.println("Pigeon Call Back Enabled");
        });

        xmlSettingReader.getRobot().getEntityGroupByType("DriveTrain").getHardwareEntity("Pigeon").setOnDestroyCallback(() -> {
            System.out.println("Pigeon Destroy Call Back Enabled");
            return false;
        });
    }

    @Override
    public void periodic() {
    }

    @Override
    public void end() {
//        logger.writeEvent(0, FileLogger.EventType.Status, "DISABLED Ending");
//        logger.close();
    }
}
