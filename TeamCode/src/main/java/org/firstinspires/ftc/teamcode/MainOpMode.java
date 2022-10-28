/**
 * Welcome to the MainOpMode program
 *
 * This programs purpose is to make the robot follow the drivers commands like a puppy dog
 * and try to be a pleasent experience as well. This program is inteded to be used with two controllers.
 * Controller A controls all of the robots wheel based movement.
 * Controller B controls all of the robots arm and claw movement.
 */

//#$#$#$#$#$#$#$#$#$#$#$#$#$> IMPORTS <#$##$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

//#$#$#$#$#$#$#$#$#$#$#$#$#$> MAIN <#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#
@TeleOp(name="Main :: Op Mode", group="Robot")
//@Disabled
public class MainOpMode extends OpMode{
    //Instantiate the Hardware class
    Robot10662Hardware robot = new Robot10662Hardware();

    @Override //>>>>>>>>>>>>>> INT <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    public void init() { //Runs ONCE when driver hits INIT <<
        //Use 'init' methods from Hardware class to Map hardware to match robot's config
        robot.init(hardwareMap);

        //Signals ready
        telemetry.addData("~>", "Robot Ready.  Press Play.  Or else...");
    }

    @Override //>>>>>>>>>>>>>> INIT LOOP <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    public void init_loop() { //Runs REPEATEDLY when driver hits INIT (Before play) <<
    }

    @Override //>>>>>>>>>>>>>> START <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    public void start() { //Runs ONCE when driver hits PLAY <<
    }

    @Override //>>>>>>>>>>>>>> LOOP <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    public void loop() { //Runs REPEATEDLY when driver hits PLAY <<
        //===========GAMEPAD1=====================
        //Defining variables
        double axial;
        double lateral;
        double yaw;

        //Doubles speed when right bumper 1 is pressed
        if (gamepad1.right_bumper) { //Dividing by two to slow it down
            axial = (-gamepad1.left_stick_y);
            lateral = (gamepad1.left_stick_x);
            yaw = (gamepad1.right_stick_x);
        } else {
            axial = (-gamepad1.left_stick_y) / 2;
            lateral = (gamepad1.left_stick_x) / 2;
            yaw = (gamepad1.right_stick_x) / 2;
        }

        //Simple algebra to tell moters their speed for movement and strafe
        double frontLeftPower = axial + lateral + yaw;
        double frontRightPower = axial - lateral - yaw;
        double backLeftPower = axial - lateral + yaw;
        double backRightPower = axial + lateral - yaw;
        
        //Setting power to moters
        //Front Left Motor
        if (frontLeftPower != 0) {//While the motor is not stopped
            if (robot.FLHeld) {//Runs once
                //Defualt settings
                robot.FrontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.FLHeld = false;
            }
            //Powering the motor
            robot.FrontLeftDrive.setPower(frontLeftPower);
        } else if (frontLeftPower == 0) {//While motor is stopped
            if (!robot.FLHeld) {//Runs once
                //Holding motor at its current position
                robot.FrontLeftDrive.setTargetPosition(robot.FrontLeftDrive.getCurrentPosition());
                robot.FrontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.FrontLeftDrive.setPower(0.5);
                robot.FLHeld = true;
            }
        }
        //Front Right Motor
        if (frontRightPower != 0) {
            if (robot.FRHeld) {
                robot.FrontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.FRHeld = false;
            }
            robot.FrontRightDrive.setPower(frontRightPower);
        } else if (frontRightPower == 0) {
            if (!robot.FRHeld) {
                robot.FrontRightDrive.setTargetPosition(robot.FrontRightDrive.getCurrentPosition());
                robot.FrontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.FrontRightDrive.setPower(0.5);
                robot.FRHeld = true;
            }
        }
        //Back Left Motor
        if (backLeftPower != 0) {
            if (robot.BLHeld) {
                robot.BackLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.BLHeld = false;
            }
            robot.BackLeftDrive.setPower(backLeftPower);
        } else if (backLeftPower == 0) {
            if (!robot.BLHeld) {
                robot.BackLeftDrive.setTargetPosition(robot.BackLeftDrive.getCurrentPosition());
                robot.BackLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.BackLeftDrive.setPower(0.5);
                robot.BLHeld = true;
            }
        }
        //Back Right Motor
        if (backRightPower != 0) {
            if (robot.BRHeld) {
                robot.BackRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.BRHeld = false;
            }
            robot.BackRightDrive.setPower(backRightPower);
        } else if (backLeftPower == 0) {
            if (!robot.BRHeld) {
                robot.BackRightDrive.setTargetPosition(robot.BackRightDrive.getCurrentPosition());
                robot.BackRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.BackRightDrive.setPower(0.5);
                robot.BRHeld = true;
            }
        }

        //===========GAMEPAD2=====================
        //Defining variables
        double armPower;

        //Doubles speed when pressed
        if(gamepad2.right_bumper && robot.Arm0.getCurrentPosition() >= 400) {
            armPower = (-gamepad2.left_stick_y / 1.5);
        } else {
            armPower = (-gamepad2.left_stick_y / 2);
        }

        if (armPower != 0) {//While the arm is moving
            if (robot.AHeld) {//Runs once
                //Defualt settings
                robot.Arm0.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.Arm1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.AHeld = false;
            }

            if (!robot.armTouch.getState()) {//Button is presssed
                //Switching modes
                robot.Arm0.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                robot.Arm1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                robot.Arm0.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.Arm1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                if (armPower <= 0) {//If the user is still trying to lower arm cut power
                    robot.Arm0.setPower(0);
                    robot.Arm1.setPower(0);
                } else {//Allow user to continue up
                    robot.Arm0.setPower(armPower);
                    robot.Arm1.setPower(armPower);
                }
            } else {//Button not presssed
                if (robot.Arm0.getCurrentPosition() >= 4350) {//If the arm goes too high
                    if (armPower >= 0) {//Stop the user from going any higher
                        robot.Arm0.setPower(0);
                        robot.Arm1.setPower(0);
                    } else {//Allow user to lower the arm
                        robot.Arm0.setPower(armPower);
                        robot.Arm1.setPower(armPower);
                    }
                } else {//Defualt when no limits are hit
                    robot.Arm0.setPower(armPower);
                    robot.Arm1.setPower(armPower);
                }
            }

        //STOP AND HOLD
        } else if (armPower == 0) {//When the arm is stopped
            if (!robot.AHeld) {//Runs once
                if (robot.Arm0.getCurrentPosition() <= 30) {//Buffer to lower the arm to the button
                    //Setting hold position to zero if it is low enough
                    robot.Arm0.setTargetPosition(0);
                    robot.Arm1.setTargetPosition(0);
                } else if (robot.Arm0.getCurrentPosition() >= 4350) {//Preventing arm from holding too high
                    //Setting the hold position to the max position
                    robot.Arm0.setTargetPosition(4350);//!!!!!!!!!!!!!!!CAUTION, THIS MAY CAUSE THE ROBOTS MOTORS TO BE OFF SYNC DUE TO THE LACK OF ACCURACY!!!!!!!!!!!!!!!
                    robot.Arm1.setTargetPosition(4350);
                } else {//Any other height
                    //Hold the motors at the current position
                    robot.Arm0.setTargetPosition(robot.Arm0.getCurrentPosition());
                    robot.Arm1.setTargetPosition(robot.Arm1.getCurrentPosition());
                }
                //Holding the motors at their set position above
                robot.Arm0.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.Arm1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.Arm0.setPower(0.5);
                robot.Arm1.setPower(0.5);
                robot.AHeld = true;
            }
        }

        //Open and close claw
        if (gamepad2.x) {
            robot.Claw0.setPosition(robot.Claw0Open);
            robot.Claw1.setPosition(robot.Claw1Open);
        } else if (gamepad2.a) {
            robot.Claw0.setPosition(robot.Claw0Close);
            robot.Claw1.setPosition(robot.Claw1Close);
        }

        //Reset encoder position if errors
        if (gamepad2.y) {
            while (robot.armTouch.getState()) {//Lowering
                robot.Arm0.setPower(-0.4);
                robot.Arm1.setPower(-0.4);
            } //Stopping the motors
            //Setting the position.
            robot.Arm0.setPower(0);
            robot.Arm0.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.Arm0.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            robot.Arm1.setPower(0);
            robot.Arm1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.Arm1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }




       //===========TELEMETRY=====================
        //Motors
        telemetry.addData("FL Power",  "%.2f :%7d", robot.FrontLeftDrive.getPower(), robot.FrontLeftDrive.getCurrentPosition());
        telemetry.addData("BL Power",  "%.2f :%7d", robot.BackLeftDrive.getPower(), robot.BackLeftDrive.getCurrentPosition());
        telemetry.addData("FR Power",  "%.2f :%7d", robot.FrontRightDrive.getPower(), robot.FrontRightDrive.getCurrentPosition());
        telemetry.addData("BR Power",  "%.2f :%7d", robot.BackRightDrive.getPower(), robot.BackRightDrive.getCurrentPosition());
        //Arm motors
        telemetry.addData("Arm0", "%.2f :%7d", robot.Arm0.getPower(), robot.Arm0.getCurrentPosition());
        telemetry.addData("Arm1", "%.2f :%7d", robot.Arm1.getPower(), robot.Arm1.getCurrentPosition());
        //Claw
        telemetry.addData("LeftClaw Position", "%.2f", robot.Claw0.getPosition());
        telemetry.addData("RightClaw Position", "%.2f", robot.Claw1.getPosition());

    }

    @Override //>>>>>>>>>>>>>> STOP <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    public void stop() { //Runs ONCE when driver hits STOP <<
    }
}