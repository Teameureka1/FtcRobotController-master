/////////////////////////////////////////////////////////////////////////////////////////////IMPORTS
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

////////////////////////////////////////////////////////////////////////////CLASS-AUTONOMOUS-MANAGER
@Autonomous(name="Robot: Auto Drive By Encoder", group="Robot")
//@Disabled //Uncomment to disable <<
public class AutonomousManager extends LinearOpMode {
    //Instantiate other classes
    Robot10662Hardware robot = new Robot10662Hardware();

    //Variables
    private String teamSelection = "Red";
    private String sideSelection = "Left";

    public double robotPositionX = 0;
    public double robotPositionY = 0;
    public double robotPositionZ = 0;

    private double robotPositionOffsetX = 0;
    private double robotPositionOffsetY = 0;
    private double robotPositionOffsetZ = 0;

    private double robotRawPositionX = 0;
    private double robotRawPositionY = 0;
    private double robotRawPositionZ = 0;

    private String parkingSpot = null;

    private double parkingPositionX = 0;
    private double parkingPositionY = 0;
    private double parkingPositionZ = 0;

    //Tensor Flow Init
    private static final String TFOD_MODEL_ASSET = "PowerPlay.tflite";

    private static final String[] LABELS = {
            "Position 1",
            "Position 2",
            "Position 3"
    };

    private static final String VUFORIA_KEY =
            "AS3eJNb/////AAABmZW+oXMs1UvbvQspLl6ESTk42fukxk9mH7yBB42bMCBv5PcjTcoDufjeE9Nv84UtCar6bi20b7RncpSLLfnB4uEk4WJel/+UcryTmrrCSrg0sTMuGjnTToDLFwM2iIyYFCg0CWaF5basGzUGIAISiNQou5wYYgFYMUd9pPoseL1HJ32JQ4054XhEvDRdo+tywvhM9NTHRaUK2e5lnSOXQW+TznS85crV/McgshAla0uuVndPYtXWDwNok7bwdST0ajAnTMUSVlEZXWlPQmsSE4H+wVO5j8DQro5JfPok/7jkk+n5hTdY+3ZqKa10jQZ37kU9yOCtV6ToOL0uNeef7oiMx8XXOUR83y3+0oKwXdw0";

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    //Waypoint Positions
    double[] waypointBotPositionsX = {

    };

    double[] waypointBotPositionsY = {

    };

    double[] waypointBotTurnPositions = {

    };





    /////////////////////////////////////////////////////////////////////////////////////////OP-MODE
    @Override
    public void runOpMode() {
        //Use 'init' methods from Hardware class to Map hardware to match robot's config
        robot.init(hardwareMap);
        robot.setAutonomosMode();

        //Selecting position
        pickTeam();
        pickSide();
        setInitPos();

        //Object detection
        activateTFOD();

        //Converting waypoints
        convertPositions();

        //Waiting for the go signal
        startupTelemetry();
        waitForStart();

        ////////////////////////////////////////////////////////////////////////////////////////MAIN

        starter();

    }
    ////////////////////////////////////////////////////////////////////////////////AUTONOMOUS-MODES
    private void starter() {
        grab();
        armHeight(5);
        detectObjects();
        armHeight(0);
        runToCoordinate(waypointBotPositionsX[0],waypointBotPositionsY[0],0);
        runToCoordinate(waypointBotPositionsX[1],waypointBotPositionsY[1],0 );
    }


    ///////////////////////////////////////////////////////////////////////////////////////PICK-TEAM
    public void pickTeam() {
        while (opModeInInit()) {
            //Title
            telemetry.addData(">>", "Select Team");
            //Selection Output
            if (teamSelection.equals("Red")) {
                telemetry.addData("   *", "Red Team");
                telemetry.addData("    ", "Blue Team");
            } else {
                telemetry.addData("    ", "Red Team");
                telemetry.addData("   *", "Blue Team");
            }

            //Selector
            if (gamepad1.a || gamepad2.a) { //Select
                while (gamepad1.a || gamepad2.a) {} //Waits for button to be unselected
                break;
            } else if (gamepad1.dpad_up || gamepad2.dpad_up) { //Select Red
                teamSelection = "Red";
            } else if (gamepad1.dpad_down || gamepad2.dpad_down) { //Select Blue
                teamSelection = "Blue";
            }

            //Updating Telemetry
            telemetry.update();
            telemetry.clearAll();
        }
        telemetry.clearAll(); //Clearing input
    }

    ///////////////////////////////////////////////////////////////////////////////////////PICK-SIDE
    public void pickSide() {
        while (opModeInInit()) {
            //Title
            telemetry.addData(">>", "Select Side");
            //Selection Output
            if (teamSelection.equals("Left")) {
                telemetry.addData("   *", "Left Side");
                telemetry.addData("    ", "Right Side");
            } else {
                telemetry.addData("    ", "Left Side");
                telemetry.addData("   *", "Right Side");
            }

            //Selector
            if (gamepad1.a || gamepad2.a) { //Select
                while (gamepad1.a || gamepad2.a) {} //Waits for button to be unselected
                break;
            } else if (gamepad1.dpad_up || gamepad2.dpad_up) { //Select Left
                teamSelection = "Left";
            } else if (gamepad1.dpad_down || gamepad2.dpad_down) { //Select Right
                teamSelection = "Right";
            }

            //Updating Telemetry
            telemetry.update();
            telemetry.clearAll();
        }
        telemetry.clearAll(); //Clearing input
    }

    ////////////////////////////////////////////////////////////////////////////////////SET-INIT-POS
    public void setInitPos() {
        if (teamSelection.equals("Red")) { //Red Team
            if (sideSelection.equals("Left")) { //Red Left
                robotPositionOffsetX = robot.startPosX;
                robotPositionOffsetY = robot.startPosY;
            } else { //Red Right
                robotPositionOffsetX = robot.startPosX;
                robotPositionOffsetY = -robot.startPosY;
            }
        } else { //Blue Team
            if (sideSelection.equals("Left")) { //Blue Left
                robotPositionOffsetX = -robot.startPosX;
                robotPositionOffsetY = -robot.startPosY;
            } else { //Blue Right
                robotPositionOffsetX = -robot.startPosX;
                robotPositionOffsetY = robot.startPosY;
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////STARTUP-TELEMETRY
    public void startupTelemetry() {
        telemetry.addData(">>", "Welcome back operator. Robot is ready, press play when ready.");
        telemetry.addData("    ", "Current Team: " + teamSelection + " Current Side: " + sideSelection);
        telemetry.update();
    }

    public void runningTelemetry() {
        //Disclaimer
        telemetry.addData("DISCLAIMER: ", "SOME VALUES ARE NOT UPDATED IN REAL TIME, THEY TAKE A LITTLE BIT OF TIME TO UPDATE");

        //Position
        telemetry.addData("POSITION: ", "X:" + robotPositionX + ", Y:" + robotPositionY + ", Z:" + robotPositionZ);

        //Parking Position
        telemetry.addData("PARK AREA: ", parkingSpot + "  X:" + parkingPositionX + ", Y:" + parkingPositionY + ", Z:" + parkingPositionZ);

        //Updating
        telemetry.update();
    }

    /////////////////////////////////////////////////////////////////////////////////////////BACKEND
    public void backend() {
        //This code runs every loop in the autonomous

        //Updating position
        updatePosition();

        //Telemetry
        runningTelemetry();
    }

    /////////////////////////////////////////////////////////////////////////////////UPDATE-POSITION
    public void updatePosition() {
        //Getting position of all the motors
        double FL = robot.FrontLeftDrive.getCurrentPosition();
        double FR = robot.FrontRightDrive.getCurrentPosition();
        double BL = robot.BackLeftDrive.getCurrentPosition();
        double BR = robot.BackRightDrive.getCurrentPosition();

        //Epik formula that I came up with at 1 am
        robotRawPositionX = ((((FL + (-FR)) - (robotRawPositionZ)) + (-((BL + (-BR)) + (robotRawPositionZ))))/4) / robot.ticksPerInch;
        robotRawPositionZ = (-(-((FL + BL) - (robotRawPositionZ)) + ((FR+BR) + (robotRawPositionZ)))/4) / robot.ticksPerInch;
        robotRawPositionY = ((((FL + BL) - (robotRawPositionZ + robotRawPositionZ)) + ((FR + BR) + (robotRawPositionZ + robotRawPositionZ)))/4) / robot.ticksPerInch;

        //Setting Position
        robotPositionX = robotRawPositionX + robotPositionOffsetX;
        robotPositionY = robotRawPositionY + robotPositionOffsetY;
        robotPositionZ = robotRawPositionZ + robotPositionOffsetZ;
    }

    ////////////////////////////////////////////////////////////////////////////////////INIT-VUFORIA
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    ///////////////////////////////////////////////////////////////////////////////////////INIT-TFOD
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.75f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 300;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);

        // Use loadModelFromAsset() if the TF Model is built in as an asset by Android Studio
        // Use loadModelFromFile() if you have downloaded a custom team model to the Robot Controller's FLASH.
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
        // tfod.loadModelFromFile(TFOD_MODEL_FILE, LABELS);
    }

    ///////////////////////////////////////////////////////////////////////////////////ACTIVATE-TFOD
    private void activateTFOD() {
        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(1.0, 16.0/9.0);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////DETECT-OBJECTS
    private void detectObjects() {
        while(parkingSpot == null) {
            if (tfod != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    //telemetry.addData("# Objects Detected", updatedRecognitions.size());

                    // step through the list of recognitions and display image position/size information for each one
                    // Note: "Image number" refers to the randomized image orientation/number
                    for (Recognition recognition : updatedRecognitions) {
                        double col = (recognition.getLeft() + recognition.getRight()) / 2 ;
                        double row = (recognition.getTop()  + recognition.getBottom()) / 2 ;
                        double width  = Math.abs(recognition.getRight() - recognition.getLeft()) ;
                        double height = Math.abs(recognition.getTop()  - recognition.getBottom()) ;

                    /*
                    telemetry.addData(""," ");
                    telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100 );
                    telemetry.addData("- Position (Row/Col)","%.0f / %.0f", row, col);
                    telemetry.addData("- Size (Width/Height)","%.0f / %.0f", width, height);
                    */

                        //Telling code the parking position
                        parkingSpot = recognition.getLabel();

                        //Super long if statement that basically tells the bot the coordinates to park
                        if (recognition.getLabel().equals("Position 1")) {
                            if (teamSelection.equals("Red")) {
                                parkingPositionY = robot.parkingPosBaseY;
                                if (sideSelection.equals("Right")) {
                                    parkingPositionX = -robot.parkingPos3BaseX;
                                } else {
                                    parkingPositionX = robot.parkingPos1BaseX;
                                }
                            } else {
                                parkingPositionY = -robot.parkingPosBaseY;
                                if (sideSelection.equals("Left")) {
                                    parkingPositionX = -robot.parkingPos3BaseX;
                                } else {
                                    parkingPositionX = robot.parkingPos1BaseX;
                                }
                            }
                        } else if (recognition.getLabel().equals("Position 2")) {
                            if (teamSelection.equals("Red")) {
                                parkingPositionY = robot.parkingPosBaseY;
                                if (sideSelection.equals("Right")) {
                                    parkingPositionX = -robot.parkingPos2BaseX;
                                } else {
                                    parkingPositionX = robot.parkingPos2BaseX;
                                }
                            } else {
                                parkingPositionY = -robot.parkingPosBaseY;
                                if (sideSelection.equals("Left")) {
                                    parkingPositionX = -robot.parkingPos2BaseX;
                                } else {
                                    parkingPositionX = robot.parkingPos2BaseX;
                                }
                            }
                        } else if (recognition.getLabel().equals("Position 3")) {
                            if (teamSelection.equals("Red")) {
                                parkingPositionY = robot.parkingPosBaseY;
                                if (sideSelection.equals("Right")) {
                                    parkingPositionX = -robot.parkingPos1BaseX;
                                } else {
                                    parkingPositionX = robot.parkingPos3BaseX;
                                }
                            } else {
                                parkingPositionY = -robot.parkingPosBaseY;
                                if (sideSelection.equals("Left")) {
                                    parkingPositionX = -robot.parkingPos1BaseX;
                                } else {
                                    parkingPositionX = robot.parkingPos3BaseX;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void runToCoordinate(double X, double Y, double Z) {
        updatePosition();

        double targetX = (X - robotPositionX) * robot.ticksPerInch;
        double targetY = (Y - robotPositionY) * robot.ticksPerInch;
        double targetZ = (Z - robotPositionZ) * robot.ticksPerInch;

        double flPos = targetY + targetX + targetZ;
        double frPos = targetY - targetX - targetZ;
        double blPos = targetY - targetX + targetZ;
        double brPos = targetY + targetX - targetZ;

        robot.FrontLeftDrive.setTargetPosition((int)flPos);
        robot.FrontRightDrive.setTargetPosition((int)frPos);
        robot.BackLeftDrive.setTargetPosition((int)blPos);
        robot.BackRightDrive.setTargetPosition((int)brPos);

        if (targetX + targetY + targetZ > 2000) {
            robot.FrontLeftDrive.setPower(0.6);
            robot.FrontRightDrive.setPower(0.6);
            robot.BackLeftDrive.setPower(0.6);
            robot.BackRightDrive.setPower(0.6);
        } else {
            robot.FrontLeftDrive.setPower(0.4);
            robot.FrontRightDrive.setPower(0.4);
            robot.BackLeftDrive.setPower(0.4);
            robot.BackRightDrive.setPower(0.4);
        }

        while (robot.FrontLeftDrive.isBusy() && robot.FrontRightDrive.isBusy()  && robot.BackLeftDrive.isBusy()  & robot.BackRightDrive.isBusy()  ) {
            updatePosition();
        }

        robot.FrontLeftDrive.setTargetPosition(robot.FrontLeftDrive.getCurrentPosition());
        robot.FrontRightDrive.setTargetPosition(robot.FrontLeftDrive.getCurrentPosition());
        robot.BackLeftDrive.setTargetPosition(robot.BackLeftDrive.getCurrentPosition());
        robot.BackRightDrive.setTargetPosition(robot.BackRightDrive.getCurrentPosition());

        robot.FrontLeftDrive.setPower(0.5);
        robot.FrontRightDrive.setPower(0.5);
        robot.BackLeftDrive.setPower(0.5);
        robot.BackRightDrive.setPower(0.5);
    }

    public void grab() {
        robot.Claw0.setPosition(robot.Claw0Close);
        robot.Claw1.setPosition(robot.Claw1Close);
    }

    public void drop() {
        robot.Claw0.setPosition(robot.Claw0Open);
        robot.Claw1.setPosition(robot.Claw1Open);
    }

    private void armHeight(int i) {
        robot.Arm0.setTargetPosition(robot.armHeight[i]);
        robot.Arm1.setTargetPosition(robot.armHeight[i]);

        if (robot.armHeight[i] + robot.Arm0.getCurrentPosition() > 2000) {
            robot.Arm0.setPower(0.6);
            robot.Arm1.setPower(0.6);
        } else {
            robot.Arm0.setPower(0.4);
            robot.Arm1.setPower(0.4);
        }

        while (robot.Arm0.isBusy() || robot.Arm1.isBusy()) {
            updatePosition();
        }

        robot.Arm0.setTargetPosition(robot.Arm0.getCurrentPosition());
        robot.Arm1.setTargetPosition(robot.Arm1.getCurrentPosition());

        robot.Arm0.setPower(0.5);
        robot.Arm1.setPower(0.5);
    }

    private void convertPositions() {
        for (int i = 0; i <= robot.waypointBotPositionBaseX.length; i++) {
            if (teamSelection.equals("Red")) { //Red Team
                if (sideSelection.equals("Left")) { //Red Left
                    waypointBotPositionsX[i] = robot.waypointBotPositionBaseX[i];
                    waypointBotPositionsY[i] = robot.waypointBotPositionBaseY[i];
                } else { //Red Right
                    waypointBotPositionsX[i] = robot.waypointBotPositionBaseX[i];
                    waypointBotPositionsY[i] = -robot.waypointBotPositionBaseY[i];
                }
            } else { //Blue Team
                if (sideSelection.equals("Left")) { //Blue Left
                    waypointBotPositionsX[i] = -robot.waypointBotPositionBaseX[i];
                    waypointBotPositionsY[i] = -robot.waypointBotPositionBaseY[i];
                } else { //Blue Right
                    waypointBotPositionsX[i] = -robot.waypointBotPositionBaseX[i];
                    waypointBotPositionsY[i] = robot.waypointBotPositionBaseY[i];
                }
            }
        }
    }

}
