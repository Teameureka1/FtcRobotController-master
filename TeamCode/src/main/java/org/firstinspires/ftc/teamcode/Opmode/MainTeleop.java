///////////////////////////////////////////////////////////////////// IMPORT ///////////////////////
package org.firstinspires.ftc.teamcode.Opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Config.Robot10662Hardware;

///////////////////////////////////////////////////////////////////// CLASS ////////////////////////
@TeleOp(name="MainTeleop", group="Robot")
//@Disabled
public class MainTeleop extends OpMode{
    //Instantiate the Hardware class
    Robot10662Hardware robot = new Robot10662Hardware();

    //Held Motors Variables
    public boolean FLHeld = false;
    public boolean FRHeld = false;
    public boolean BLHeld = false;
    public boolean BRHeld = false;
    public boolean AHeld = false;

    //Mode Variables
    private boolean FieldCentricDriving = true;
    private boolean DebugMode = false;

    //Bebounce booleans
    private boolean driveModeDebounce = false;
    private boolean debugModeDebounce = false;

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

        //region ////////////// GLOBAL CONTROLS //////////////////////////////////////////////////
        //Controls
        boolean debugModeButton = gamepad1.dpad_left || gamepad2.dpad_left;

        //Toggle Debug Mode
        if(debugModeButton && !debugModeDebounce) {
            if(DebugMode) {
                DebugMode = false;
            } else {
                DebugMode = true;
            }
            debugModeDebounce = true;
        } else if (!debugModeButton && debugModeButton) {
            debugModeDebounce = false;
        }
        //endregion

        //region ////////////// MAIN CONTROLS ////////////////////////////////////////////////////
        //Controls
        double xCoordinate = gamepad1.left_stick_x;
        double yCoordinate = -gamepad1.left_stick_y;
        double zCoordinate = gamepad1.right_stick_x;
        double throttle1 = gamepad1.right_trigger;
        boolean holdButton = gamepad1.left_bumper;
        boolean drivingButton = gamepad1.y;
        boolean imuResetButton = gamepad1.dpad_up;

        //FCD Mode Switcher
        if(drivingButton && !driveModeDebounce) {
            if(FieldCentricDriving) {
                FieldCentricDriving = false;
            } else {
                FieldCentricDriving = true;
            }
            driveModeDebounce = true;
        } else if (!drivingButton && driveModeDebounce) {
            driveModeDebounce = false;
        }

        //IMU Position Reset
        if (imuResetButton) {
            robot.setAngleZero();
        }

        //Using a formula to get the robots current heading and convert the joystick heading to
        // Allow the robot to always head forward when joystick pushed forward.
        double gamepadRadians = Math.atan2(xCoordinate, yCoordinate);
        double gamepadHypot = Range.clip(Math.hypot(xCoordinate, yCoordinate), 0, 1);
        double robotRadians = (robot.getAngle() * (Math.PI/180));
        double targetRadians = gamepadRadians + robotRadians;
        double xControl = Math.sin(targetRadians)*gamepadHypot;
        double yControl = Math.cos(targetRadians)*gamepadHypot;

        //Defining Variables
        double axial;
        double lateral;
        double yaw;

        if (FieldCentricDriving) { //When enabled use virtual joystick
            axial = (yControl / 2.5) * ((throttle1 * 1.5) + 1);
            lateral = (xControl / 2.5) * ((throttle1 * 1.5) + 1);
        } else { //Normal driving
            axial = (yCoordinate / 2.5) * ((throttle1 * 1.5) + 1);
            lateral = (xCoordinate / 2.5) * ((throttle1 * 1.5) + 1);
        }
        yaw = (zCoordinate / 2.5) * ((throttle1 * 1.5) + 1);

        //Converting above to power for each motor
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
        } else if (frontLeftPower == 0 && holdButton) { //Stop and hold mode
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
        //endregion
	
	    //region ////////////// ARM CONTROLLER ///////////////////////////////////////////////
	    //Controls
	    double yArmControl = -gamepad2.left_stick_y;


        double armPower = yArmControl;

        robot.Arm.setPower(armPower);

	    //endregion

        //region ////////////// TELEMETRY ////////////////////////////////////////////////////
        // General telemetry ////////////////////
        telemetry.addData(">",robot.robotName + " : " + robot.team + (DebugMode?" : DEBUG MODE ENABLED":""));
        //                      Look cool     Robot's name            Team name          If debug mode is enabled say

        // Main telemetry ///////////////////////
        //TODO: Finnish telemetry.
        if (!DebugMode) { //Only displays if not in debug mode
            telemetry.addData("Don't mind me", "Easily readable telemetry not added yet, please use debug mode, dpad_left");
        }

        // Debug telemetry //////////////////////
        if (DebugMode) { //Only displays if in debug mode
            telemetry.addData("WARNING", "DEBUG MODE HAS EXTRA FUNCTIONS THAT CAN POTENTIALLY BREAK THE ROBOT, PLEASE PROCEED WITH CAUTION.");

            telemetry.addData("","");
            telemetry.addData("Driver 1", "------------------------");
            telemetry.addData("Gamepad",  "Radian-" + gamepadRadians + " X-" + xCoordinate + " Y-" + yCoordinate);
            telemetry.addData("Throttle", gamepadHypot + " + " + throttle1 + " --> " + (gamepadHypot / 2.5) * ((throttle1 * 1.5) + 1));
            telemetry.addData("FcdEnabled", FieldCentricDriving);
            if(FieldCentricDriving) {
                    telemetry.addData("FcdConversions", "TargetRadians-"+ targetRadians +" X-" + xControl + " Y-" + yControl);
                    telemetry.addData("IMUOffset", robot.imuAngleOffset);
            }

            telemetry.addData("","");
            telemetry.addData("Driver 2", "------------------------");
            telemetry.addData("ArmControllerPower", yArmControl);
            telemetry.addData("ArmTargetPower", yArmControl);
            telemetry.addData("ArmCurrentPower", robot.Arm.getPower());
            telemetry.addData("ArmCurrentPosition", robot.Arm.getCurrentPosition());
        }

        // Updating telemetry ///////////////////
        telemetry.update();
	    telemetry.clearAll();
        //endregion
    }

    @Override /////////////////////////////////////////////////////// STOP /////////////////////////
    public void stop() { //Runs ONCE when driver hits STOP <<
    }
}
