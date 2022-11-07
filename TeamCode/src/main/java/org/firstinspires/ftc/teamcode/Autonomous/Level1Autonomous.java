/* !CAVEMAN AUTONOMOUS LEVEL 0

    Ever wondered why this is a level one caveman script, well the only thing it dose is drive
    forward. Yup, for those dyer situations like the first scrimmage where we had super big bugs with
    autonomous to the point that it did not even work! This script simply parks the bot one square
    in-front of it.
 */

///////////////////////////////////////////////////////////////////// IMPORT ///////////////////////
package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Config.Robot10662Hardware;

///////////////////////////////////////////////////////////////////// CLASS ////////////////////////
@Autonomous(name="CavemanAutonomous :: Level 1", group = "Robot")
public class Level1Autonomous extends LinearOpMode {
    //Defining the config files
    Robot10662Hardware robot = new Robot10662Hardware();

    //Timers
    ElapsedTime runtime = new ElapsedTime();

    //Variables
    private String side = null;

    ///////////////////////////////////////////////////////////////// OP MODE //////////////////////
    @Override
    public void runOpMode() {
        //Setting motors ect
        robot.init(hardwareMap);

        //Asking driver
        if (opModeInInit()) {
            while (true) {
                //Display
                telemetry.addData(">>", "Are you on the left or right side?");

                //Temporary variable
                int tmp = 0;

                //Display
                if (tmp == 0) {
                    telemetry.addData("   *", "Left Side");
                    telemetry.addData("    ", "Right Side");
                } else {
                    telemetry.addData("    ", "Left Side");
                    telemetry.addData("   *", "Right Side");
                }

                //Keymapping to navigate and select
                if (gamepad1.a || gamepad2.a) {
                    if (tmp == 0) {
                        side = "Left";
                        break;
                    } else {
                        side = "Right";
                        break;
                    }
                } else if (gamepad1.dpad_up || gamepad2.dpad_up) {
                    tmp = 0;
                } else if (gamepad1.dpad_down || gamepad2.dpad_down) {
                    tmp = 1;
                }

                //Updating telemetry
                telemetry.update();
            }


        }

        //Waiting for the user
        telemetry.addData(">>","LET IT RIP! Smack that play button!");
        telemetry.update();
        waitForStart();
        ///////////////////////////////////////////////////////////// RUNNING //////////////////////

        grab(); //Grabs preloaded code
        moveInches(60,0,0); //Moving to the epik area
        armHeightPreset(4); //All the way up


    }

    ///////////////////////////////////////////////////////////////// MOVE INCHES //////////////////
    private void moveInches(double axial, double lateral, double yaw) {
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

        //Short pause to prevent bot from knocking itself around too much
        runtime.reset();
        while (runtime.seconds() < 0.2) {}
    }

    ///////////////////////////////////////////////////////////////// ARM HEIGHT ///////////////////
    private void armHeightPreset(int pos) {
        //Grabbing height from table
        int targetHeight = robot.armPositions[pos];

        //Setting positions
        robot.Arm0.setTargetPosition(targetHeight);
        robot.Arm1.setTargetPosition(targetHeight);

        //Setting mode
        robot.Arm0.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.Arm1.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Setting power
        robot.Arm0.setPower(0.5);
        robot.Arm1.setPower(0.5);

        //Waiting until finished
        while (robot.Arm0.isBusy() && robot.Arm1.isBusy()) {}

        //Stopping
        robot.Arm0.setTargetPosition(robot.Arm0.getCurrentPosition());
        robot.Arm1.setTargetPosition(robot.Arm1.getCurrentPosition());

        //Setting power again
        robot.Arm0.setPower(0.5);
        robot.Arm1.setPower(0.5);
    }

    ///////////////////////////////////////////////////////////////// GRAB /////////////////////////
    private void grab() {
        robot.Claw0.setPosition(robot.Claw0Close);
        robot.Claw1.setPosition(robot.Claw1Close);
    }

    ///////////////////////////////////////////////////////////////// DROP /////////////////////////
    private void drop() {
        robot.Claw0.setPosition(robot.Claw0Open);
        robot.Claw1.setPosition(robot.Claw1Open);
    }
}
