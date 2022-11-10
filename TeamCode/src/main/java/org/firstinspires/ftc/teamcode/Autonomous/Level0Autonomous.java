/* !CAVEMAN AUTONOMOUS LEVEL 0

    Ever wondered why this is a level one caveman script, well the only thing it dose is drive
    forward. Yup, for those dyer situations like the first scrimmage where we had super big bugs with
    autonomous to the point that it did not even work! This script simply parks the bot one square
    in-front of it.
 */

///////////////////////////////////////////////////////////////////// IMPORT ///////////////////////
package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Config.Robot10662Hardware;

///////////////////////////////////////////////////////////////////// CLASS ////////////////////////
@Autonomous(name="CavemanAutonomous :: Level 0", group = "Robot")
@Disabled
public class Level0Autonomous extends LinearOpMode {
    //Defining the config files
    Robot10662Hardware robot = new Robot10662Hardware();

    ///////////////////////////////////////////////////////////////// OP MODE //////////////////////
    @Override
    public void runOpMode() {
        //Setting motors ect
        robot.init(hardwareMap);

        //Waiting for the user
        telemetry.addData(">>","Team rocket is blasting off again, hit play!");
        telemetry.update();
        waitForStart();
        ///////////////////////////////////////////////////////////// RUNNING //////////////////////

        //Amount to move in inches
        double axial    = 24; //Move forward inches
        double lateral  = 0; //Moves to the side inches
        double yaw      = 0; //Turns inches

        //Changing above to code readable format
        int frontLeftPos = (int)((axial + lateral + yaw) * robot.ticksPerInch) + robot.FrontLeftDrive.getCurrentPosition();
        int frontRightPos = (int)((axial - lateral - yaw) * robot.ticksPerInch) + robot.FrontRightDrive.getCurrentPosition();
        int backLeftPos = (int)((axial - lateral + yaw) * robot.ticksPerInch) + robot.BackLeftDrive.getCurrentPosition();
        int backRightPos = (int)((axial + lateral - yaw) * robot.ticksPerInch) + robot.BackRightDrive.getCurrentPosition();

        //Setting target position to motors
        robot.FrontLeftDrive.setTargetPosition(frontLeftPos);
        robot.FrontRightDrive.setTargetPosition(frontRightPos);
        robot.BackLeftDrive.setTargetPosition(backLeftPos);
        robot.BackRightDrive.setTargetPosition(backRightPos);

        //Setting mode
        robot.FrontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.FrontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.BackLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.BackRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Setting power
        robot.FrontLeftDrive.setPower(0.5);
        robot.FrontRightDrive.setPower(0.5);
        robot.BackLeftDrive.setPower(0.5);
        robot.BackRightDrive.setPower(0.5);

        //Waiting until finished
        while (robot.FrontLeftDrive.isBusy() && robot.FrontRightDrive.isBusy() && robot.BackLeftDrive.isBusy() && robot.BackRightDrive.isBusy()) {}

        //Stopping motors
        robot.FrontLeftDrive.setTargetPosition(robot.FrontLeftDrive.getCurrentPosition());
        robot.FrontRightDrive.setTargetPosition(robot.FrontRightDrive.getCurrentPosition());
        robot.BackLeftDrive.setTargetPosition(robot.BackLeftDrive.getCurrentPosition());
        robot.BackRightDrive.setTargetPosition(robot.BackRightDrive.getCurrentPosition());

        //Setting power again
        robot.FrontLeftDrive.setPower(0.5);
        robot.FrontRightDrive.setPower(0.5);
        robot.BackLeftDrive.setPower(0.5);
        robot.BackRightDrive.setPower(0.5);
    }
}
