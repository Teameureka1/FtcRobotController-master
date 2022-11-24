///////////////////////////////////////////////////////////////////// IMPORT ///////////////////////

//#$#$#$#$#$#$#$#$#$#$#$#$#$> IMPORTS <#$##$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#
package org.firstinspires.ftc.teamcode.Opmode;

import android.widget.TabHost;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Config.Robot10662Hardware;

///////////////////////////////////////////////////////////////////// CLASS ////////////////////////
@TeleOp(name="MainOpmode (Experimental)", group="Robot")
//@Disabled
public class ExperimentalOpmode extends OpMode{
    //Instantiate the Hardware class
    Robot10662Hardware robot = new Robot10662Hardware();

    //Held Motors
    public boolean FLHeld = false;
    public boolean FRHeld = false;
    public boolean BLHeld = false;
    public boolean BRHeld = false;
    public boolean AHeld = false;

    //Driving Mode
    private boolean FCD = true;

    //BRo make comprimise
    private double axial;
    private double lateral;
    private double yaw;

    //Debug booleans
    public boolean armDebug = false;
    private boolean driveDebug = false;

    @Override /////////////////////////////////////////////////////// INIT /////////////////////////
    public void init() { //Runs ONCE when driver hits INIT <<
        //Use 'init' methods from Hardware class to Map hardware to match robot's config
        robot.init(hardwareMap);

        //Signals ready
        telemetry.addData("~>", "Robot Ready.  Press Play.  Or else...");
        telemetry.update();
        telemetry.clearAll();
    }

    @Override /////////////////////////////////////////////////////// INIT LOOP ////////////////////
    public void init_loop() { //Runs REPEATEDLY when driver hits INIT (Before play) <<
    }

    @Override /////////////////////////////////////////////////////// START ////////////////////////
    public void start() { //Runs ONCE when driver hits PLAY <<
    }

    @Override /////////////////////////////////////////////////////// LOOP /////////////////////////
    public void loop() { //Runs REPEATEDLY when driver hits PLAY <<

        mainController();
        armController();






        telemetry.update();
    }

    @Override /////////////////////////////////////////////////////// STOP /////////////////////////
    public void stop() { //Runs ONCE when driver hits STOP <<
    }

    private void mainController() {
        //Controls
        double xCoordinate = gamepad1.left_stick_x;
        double yCoordinate = -gamepad1.left_stick_y;
        double zCoordinate = gamepad1.right_stick_x;
        double throttle1 = gamepad1.right_trigger;
        boolean holdButton = gamepad1.right_bumper;
        boolean drivingButton = gamepad1.y;

        if(drivingButton && !driveDebug) {
            if(FCD) {
                FCD = false;
            } else {
                FCD = true;
            }
            driveDebug = true;
        } else if (!drivingButton && driveDebug) {
            driveDebug = false;
        }

        double gamepadRadians = Math.atan2(xCoordinate, yCoordinate);
        double gamepadHypot = Range.clip(Math.hypot(xCoordinate, yCoordinate), 0, 1);
        double robotRadians = robot.getAngle() * (robot.pi/180);
        double targetRadians = gamepadRadians + robotRadians;
        double xControl = Math.sin(targetRadians)*gamepadHypot;
        double yControl = Math.cos(targetRadians)*gamepadHypot;

        /*
        telemetry.addData("Gamepad Radians", gamepadRadians);
        telemetry.addData("Gamepad Hypotonuse", gamepadHypot);
        telemetry.addData("Robot Radians", robotRadians);
        telemetry.addData("Target Radians", targetRadians);
        telemetry.addData("Y Axis", yControl);
        telemetry.addData("X Axis", xControl);*/

        if (FCD) {
            axial = (yControl / 2.5) * ((throttle1 * 1.5) + 1);
            lateral = (xControl / 2.5) * ((throttle1 * 1.5) + 1);
            yaw = (zCoordinate / 2.5) * ((throttle1 * 1.5) + 1);
        } else {
            axial = (yCoordinate / 2.5) * ((throttle1 * 1.5) + 1);
            lateral = (xCoordinate / 2.5) * ((throttle1 * 1.5) + 1);
            yaw = (zCoordinate / 2.5) * ((throttle1 * 1.5) + 1);
        }

        double frontLeftPower = axial + lateral + yaw;
        double frontRightPower = axial - lateral - yaw;
        double backLeftPower = axial - lateral + yaw;
        double backRightPower = axial + lateral - yaw;


        //Setting power to motors
        if (!holdButton || frontLeftPower != 0) { //FL
            if (robot.FrontLeftDrive.getMode() == DcMotor.RunMode.RUN_TO_POSITION) {
                robot.FrontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
            robot.FrontLeftDrive.setPower(frontLeftPower);
        } else if (frontLeftPower == 0 && holdButton) {
            if (robot.FrontLeftDrive.getMode() == DcMotor.RunMode.RUN_USING_ENCODER) {
                robot.FrontLeftDrive.setTargetPosition(robot.FrontLeftDrive.getCurrentPosition());
                robot.FrontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.FrontLeftDrive.setPower(0.5);
            }
        }

        if (!holdButton || frontRightPower != 0) { //FR
            if (robot.FrontRightDrive.getMode() == DcMotor.RunMode.RUN_TO_POSITION) {
                robot.FrontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
            robot.FrontRightDrive.setPower(frontRightPower);
        } else if (frontRightPower == 0 && holdButton) {
            if (robot.FrontRightDrive.getMode() == DcMotor.RunMode.RUN_USING_ENCODER) {
                robot.FrontRightDrive.setTargetPosition(robot.FrontRightDrive.getCurrentPosition());
                robot.FrontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.FrontRightDrive.setPower(0.5);
            }
        }

        if (!holdButton || backLeftPower != 0) { //BL
            if (robot.BackLeftDrive.getMode() == DcMotor.RunMode.RUN_TO_POSITION) {
                robot.BackLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
            robot.BackLeftDrive.setPower(backLeftPower);
        } else if (backLeftPower == 0 && holdButton) {
            if (robot.BackLeftDrive.getMode() == DcMotor.RunMode.RUN_USING_ENCODER) {
                robot.BackLeftDrive.setTargetPosition(robot.BackLeftDrive.getCurrentPosition());
                robot.BackLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.BackLeftDrive.setPower(0.5);
            }
        }

        if (!holdButton || backRightPower != 0) { //BR
            if (robot.BackRightDrive.getMode() == DcMotor.RunMode.RUN_TO_POSITION) {
                robot.BackRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
            robot.BackRightDrive.setPower(backRightPower);
        } else if (backRightPower == 0 && holdButton) {
            if (robot.BackRightDrive.getMode() == DcMotor.RunMode.RUN_USING_ENCODER) {
                robot.BackRightDrive.setTargetPosition(robot.BackRightDrive.getCurrentPosition());
                robot.BackRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.BackRightDrive.setPower(0.5);
            }
        }
    }

    private void armController() {
        //Controls
        double armControl = -gamepad2.left_stick_y;
        double throttle2 = gamepad2.right_trigger;
        boolean grabButton = gamepad2.x;
        boolean dropButton = gamepad2.y;
        boolean armTouch = robot.armTouch.getState();

        int currentPos = robot.Arm0.getCurrentPosition();
        double armPower;

        if(armDebug && armControl <= 0) {
            armDebug = false;
        }

        //telemetry.addData("ArmDebug",armDebug);

        if (currentPos >= robot.armPositions[0]+300 && currentPos <= robot.armPositions[3]-300) {
            armPower = (armControl / 2.5) * ((throttle2 * 1.5) + 1);
        } else {
            armPower = (armControl / 2.5);
        }

        if(armPower !=0 && !armDebug) {
            if (robot.Arm0.getMode() != DcMotor.RunMode.RUN_USING_ENCODER) {
                robot.Arm0.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.Arm0.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }

            if (!armTouch) {
                if(armPower < 0) {
                    robot.Arm0.setPower(0);
                    robot.Arm1.setPower(0);
                } else {
                    robot.Arm0.setPower(armPower);
                    robot.Arm1.setPower(armPower);
                }
            } else if (currentPos >= robot.armPositions[3]) {
                if(armPower > 0) {
                    armDebug = true;
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

        } else {
            if (robot.Arm0.getMode() != DcMotor.RunMode.RUN_TO_POSITION) {
                if(currentPos >= robot.armPositions[3]-25) {
                    robot.Arm0.setTargetPosition(robot.armPositions[3]);
                    robot.Arm1.setTargetPosition(robot.armPositions[3]);
                } else {
                    robot.Arm0.setTargetPosition(robot.Arm0.getCurrentPosition() + 10);
                    robot.Arm1.setTargetPosition(robot.Arm1.getCurrentPosition() + 10);
                }
                robot.Arm0.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.Arm0.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.Arm0.setPower(0.5);
                robot.Arm1.setPower(0.5);
            }
        }

        if(grabButton) {
            robot.Claw0.setPosition(robot.Claw0Close);
            robot.Claw1.setPosition(robot.Claw1Close);
        }
        if(dropButton) {
            robot.Claw0.setPosition(robot.Claw0Open);
            robot.Claw1.setPosition(robot.Claw1Open);
        }
    }
}