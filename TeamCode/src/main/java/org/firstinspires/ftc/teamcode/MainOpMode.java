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
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

//#$#$#$#$#$#$#$#$#$#$#$#$#$> MAIN <#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#
@TeleOp(name="Main :: Op Mode", group="Robot")
//@Disabled
public class MainOpMode extends OpMode{
    private ElapsedTime runtime = new ElapsedTime();
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
        if (frontLeftPower != 0) {
            if (robot.FLHeld == true) {
                robot.FrontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.FLHeld = false;
            }
            robot.FrontLeftDrive.setPower(frontLeftPower);
        } else if (frontLeftPower == 0) {
            if (robot.FLHeld == false) {
                robot.FrontLeftDrive.setTargetPosition(robot.FrontLeftDrive.getCurrentPosition());
                robot.FrontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.FrontLeftDrive.setPower(0.5);
                robot.FLHeld = true;
            }
        }

        if (frontRightPower != 0) {
            if (robot.FRHeld == true) {
                robot.FrontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.FRHeld = false;
            }
            robot.FrontRightDrive.setPower(frontRightPower);
        } else if (frontRightPower == 0) {
            if (robot.FRHeld == false) {
                robot.FrontRightDrive.setTargetPosition(robot.FrontRightDrive.getCurrentPosition());
                robot.FrontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.FrontRightDrive.setPower(0.5);
                robot.FRHeld = true;
            }
        }

        if (backLeftPower != 0) {
            if (robot.BLHeld == true) {
                robot.BackLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.BLHeld = false;
            }
            robot.BackLeftDrive.setPower(backLeftPower);
        } else if (backLeftPower == 0) {
            if (robot.BLHeld == false) {
                robot.BackLeftDrive.setTargetPosition(robot.BackLeftDrive.getCurrentPosition());
                robot.BackLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.BackLeftDrive.setPower(0.5);
                robot.BLHeld = true;
            }
        }

        if (backRightPower != 0) {
            if (robot.BRHeld == true) {
                robot.BackRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.BRHeld = false;
            }
            robot.BackRightDrive.setPower(backRightPower);
        } else if (backLeftPower == 0) {
            if (robot.BRHeld == false) {
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
        if(gamepad2.right_bumper && robot.Arm0.getCurrentPosition() != 400) {
            armPower = (-gamepad2.left_stick_y / 1.5);
        } else {
            armPower = (-gamepad2.left_stick_y / 2);
        }

        if (armPower != 0) {
            if (robot.AHeld == true) {
                robot.Arm0.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.Arm1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.AHeld = false;
            }

            if (robot.armTouch.getState() == false) {
                robot.Arm0.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                robot.Arm1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                robot.Arm0.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.Arm1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                if (armPower <= 0) {
                    robot.Arm0.setPower(0);
                    robot.Arm1.setPower(0);
                } else {
                    robot.Arm0.setPower(armPower);
                    robot.Arm1.setPower(armPower);
                }
            } else {
                if (robot.Arm0.getCurrentPosition() >= 4350) {
                    if (armPower >= 0) {
                        robot.Arm0.setPower(0);
                        robot.Arm1.setPower(0);
                    } else {
                        robot.Arm0.setPower(armPower);
                        robot.Arm1.setPower(armPower);
                    }
                } else {
                    robot.Arm0.setPower(armPower);
                    robot.Arm1.setPower(armPower);
                }
            }


        } else if (armPower == 0) {
            if (robot.AHeld == false) {
                if (robot.Arm0.getCurrentPosition() <= 30) {
                    robot.Arm0.setTargetPosition(0);
                    robot.Arm1.setTargetPosition(0);
                } else {
                    robot.Arm0.setTargetPosition(robot.Arm0.getCurrentPosition());
                    robot.Arm1.setTargetPosition(robot.Arm1.getCurrentPosition());
                }
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




        //region ===========TELEMETRY=====================
        //Motors
        telemetry.addData("FL Power",  "%.2f :%7d", robot.FrontLeftDrive.getPower(), robot.FrontLeftDrive.getCurrentPosition());
        telemetry.addData("BL Power",  "%.2f :%7d", robot.BackLeftDrive.getPower(), robot.BackLeftDrive.getCurrentPosition());
        telemetry.addData("FR Power",  "%.2f :%7d", robot.FrontRightDrive.getPower(), robot.FrontRightDrive.getCurrentPosition());
        telemetry.addData("BR Power",  "%.2f :%7d", robot.BackRightDrive.getPower(), robot.BackRightDrive.getCurrentPosition());
        //Claw
        telemetry.addData("LeftClaw Position", "%.2f", robot.Claw0.getPosition());
        telemetry.addData("RightClaw Position", "%.2f", robot.Claw1.getPosition());


        telemetry.addData("Arm1", "%.2f :%7d", robot.Arm0.getPower(), robot.Arm0.getCurrentPosition());
        telemetry.addData("Arm2", "%.2f :%7d", robot.Arm1.getPower(), robot.Arm1.getCurrentPosition());

        //endregion

    }

    @Override //>>>>>>>>>>>>>> STOP <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    public void stop() { //Runs ONCE when driver hits STOP <<
    }
}