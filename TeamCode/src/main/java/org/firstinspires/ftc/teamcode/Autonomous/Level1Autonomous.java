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
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Config.Robot10662Hardware;

///////////////////////////////////////////////////////////////////// CLASS ////////////////////////
@Autonomous(name="CavemanAutonomous :: Level 1", group = "Robot")
//@Disabled
public class Level1Autonomous extends LinearOpMode {
    //Defining the config files
    Robot10662Hardware robot = new Robot10662Hardware();

    //Timers
    ElapsedTime runtime = new ElapsedTime();

    //Variables
    private String side = "Left";

    ///////////////////////////////////////////////////////////////// OP MODE //////////////////////
    @Override
    public void runOpMode() {
        //Setting motors ect
        robot.init(hardwareMap);

        //Asking driver what side the robot is on
        int tmp = 0; //Temporary value
        if (opModeInInit()) {
            while (true) {
                //Display
                telemetry.addData(">>", "Are you on the left or right side?");

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
                        runtime.reset(); //Conformation
                        while (runtime.seconds() <= 2) {
                            if (!gamepad1.a && !gamepad2.a) {
                                break;
                            } else {
                                telemetry.addData("Continue to hold for ", " 2 Seconds, to select the Left Side");
                                telemetry.update();
                                telemetry.clearAll();
                            }
                        }
                        if (runtime.seconds() >= 2) {
                            side = "Left";
                            telemetry.clearAll();
                            break;
                        }
                    } else {
                        runtime.reset(); //Conformation
                        while (runtime.seconds() <= 2) {
                            if (!gamepad1.a && !gamepad2.a) {
                                break;
                            } else {
                                telemetry.addData("Continue to hold for ", " 2 Seconds, to select the Right Side");
                                telemetry.update();
                                telemetry.clearAll();
                            }
                        }
                        if (runtime.seconds() >= 2) {
                            side = "Right";
                            telemetry.clearAll();
                            break;
                        }
                    }
                }

                if (gamepad1.dpad_up || gamepad2.dpad_up) {
                    tmp = 0;

                }

                if (gamepad1.dpad_down || gamepad2.dpad_down) {
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

        grab();
        armHeightPreset(1);

        if (side.equals("Left")) { //Moving to the junction
            moveInches(0,15,0);
        } else {
            moveInches(0,-15,0);
        }
        moveInches(6,0,0); //Getting closer
        drop(); //Drops cone
        waitTime(0.5);

        moveInches(-4,0,0); //Moving back
        armHeightPreset(0); //Dropping arm back down

        if (side.equals("Left")) { //Moving to parking area
            moveInches(0,-15,0);
        } else {
            moveInches(0,15,0);
        }
        moveInches(26,0,0);

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

    private void waitTime(double time) {
        runtime.reset(); //Resets timer
        while (runtime.seconds() <= time) {} //Waits until inputted time is met
    }
}
