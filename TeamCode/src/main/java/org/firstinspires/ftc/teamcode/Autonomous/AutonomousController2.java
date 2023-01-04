///////////////////////////////////////////////////////////////////// IMPORT ///////////////////////
package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.Config.Robot10662Hardware;


import java.util.List;

///////////////////////////////////////////////////////////////////// CLASS ////////////////////////
@Autonomous(name="Autonomous (Experimental)", group = "Robot")
public class AutonomousController2 extends LinearOpMode {
    //Defining robot config file
    Robot10662Hardware robot = new Robot10662Hardware();

    //Timers
    ElapsedTime runtime = new ElapsedTime();

    //Variables
    private String startingSide = "Left"; //Set to left by default if not modified by user
    private int posToPark = 0; //Zero is represented as a "null" valueee

    private static final String[] LABLES = { //All parking positions
            "Pos Null",
            "Pos 1",
            "Pos 2",
            "Pos 3",
    };

    //TF related variables
    private static final String TFOD_MODEL_ASSET = "CustomSleeve.tflite"; //Signal sleeve file
    private static final String VUFORIA_KEY = //Vuforia key
            "AS3eJNb/////AAABmZW+oXMs1UvbvQspLl6ESTk42fukxk9mH7yBB42bMCBv5PcjTcoDufjeE9Nv84UtCar6bi20b7RncpSLLfnB4uEk4WJel/+UcryTmrrCSrg0sTMuGjnTToDLFwM2iIyYFCg0CWaF5basGzUGIAISiNQou5wYYgFYMUd9pPoseL1HJ32JQ4054XhEvDRdo+tywvhM9NTHRaUK2e5lnSOXQW+TznS85crV/McgshAla0uuVndPYtXWDwNok7bwdST0ajAnTMUSVlEZXWlPQmsSE4H+wVO5j8DQro5JfPok/7jkk+n5hTdY+3ZqKa10jQZ37kU9yOCtV6ToOL0uNeef7oiMx8XXOUR83y3+0oKwXdw0";
    private VuforiaLocalizer vuforia; //Store instance of Vuforia
    private TFObjectDetector tfod; //Store instance of TensorFlow

    ///////////////////////////////////////////////////////////////////// RUN OPMODE ///////////////
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
                            startingSide = "Left";
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
                            startingSide = "Right";
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

        telemetry.addData("INIT ", " Vulforia + TFod"); //Telemtrykjzdsv
        telemetry.update();

        initVuforia();
        initTfod();


        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(1.0, 16.0/9.0);
        }

        //Waiting for the user
        telemetry.addData(">>","LET IT RIP! Smack that play button!");
        telemetry.update();
        waitForStart();
        ///////////////////////////////////////////////////////////// RUNNING //////////////////////

        newRobotPosition(5,0,0,false,92304,false);




    }

    ///////////////////////////////////////////////////////////////////// MOVE TO POSITION /////////
    private void newRobotPosition(double y, double x, double z, boolean addRotation, int armY, boolean endClawGrip) {
        //Converting above to positions for every motor (FORMAT: *motor*TargetPosition)
        //  Wheel | PerWheelConversion | ConvertingToTicks  |  AddedCurrentPosition
        int fltp = (int)((y + x + z) * robot.ticksPerInch) + robot.FrontLeftDrive.getCurrentPosition();
        int frtp = (int)((y - x - z) * robot.ticksPerInch) + robot.FrontRightDrive.getCurrentPosition();
        int bltp = (int)((y - x + z) * robot.ticksPerInch) + robot.BackLeftDrive.getCurrentPosition();
        int brtp = (int)((y + x - z) * robot.ticksPerInch) + robot.BackRightDrive.getCurrentPosition();
        int atp  = (int)(armY * robot.ticksPerInch);

        //Resting encoders
        robot.FrontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.FrontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.BackLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.BackRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //Setting positions to motors
        robot.FrontLeftDrive.setTargetPosition(fltp);
        robot.FrontRightDrive.setTargetPosition(frtp);
        robot.BackLeftDrive.setTargetPosition(bltp);
        robot.BackRightDrive.setTargetPosition(brtp);
        robot.Arm.setTargetPosition(atp);

        //Setting motor mode
        robot.FrontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.FrontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.BackLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.BackRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.Arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Loop while any motors are active
        while (robot.FrontLeftDrive.isBusy() || robot.FrontRightDrive.isBusy() ||
                robot.BackLeftDrive.isBusy() || robot.BackRightDrive.isBusy() ||
                robot.Arm.isBusy()) {
            //Main drive train
            double distance = distance(robotX(),x,robotY(),y); //Distance to target
            double threshold = 200; //Maximum distance for adjustable speed
            double minimum = 20; //Minimum distance for adjustable speed
            double speed;
            if(distance > threshold) { //Max speed
                speed = 1;
            } else if (distance < minimum) { //Min speed
                speed = 0.2;
            } else { //In between
                speed = distance/threshold;
            }

            //Setting speed to motors
            robot.FrontLeftDrive.setPower(speed);
            robot.FrontRightDrive.setPower(speed);
            robot.BackLeftDrive.setPower(speed);
            robot.BackRightDrive.setPower(speed);

            //Arm
            double distance2 = distance(robot.Arm.getCurrentPosition(), apt, 0, 0); //Distance to target
            double threshold2 = 200; //Maximum distance for adjustable speed
            double minimum2 = 20; //Minimum distance for adjustable speed
            double speed2;
            if(distance > threshold) { //Max speed
                speed2 = 1;
            } else if (distance < minimum) { //Min speed
                speed2 = 0.2;
            } else { //In between
                speed2 = distance/threshold;
            }
            robot.Arm.setPower(speed2);
        }


    }

    private double robotX() {
        double FL = robot.FrontLeftDrive.getCurrentPosition();
        double FR = robot.FrontRightDrive.getCurrentPosition();
        double BL = robot.BackLeftDrive.getCurrentPosition();
        double BR = robot.BackRightDrive.getCurrentPosition();


        double posX = ((FL + (-FR)) + (-((BL + (-BR))))/4) / robot.ticksPerInch;
        double posY = ((((FL + BL) + posX) + ((FR + BR) + posX))/4) / robot.ticksPerInch;
        double posZ = (-(-((FL + BL) - (posX)) + ((FR+BR) + (posX)))/4) / robot.ticksPerInch;

        return posX;
    }

    private double robotY() {
        double FL = robot.FrontLeftDrive.getCurrentPosition();
        double FR = robot.FrontRightDrive.getCurrentPosition();
        double BL = robot.BackLeftDrive.getCurrentPosition();
        double BR = robot.BackRightDrive.getCurrentPosition();


        double posX = ((FL + (-FR)) + (-((BL + (-BR))))/4) / robot.ticksPerInch;
        double posY = ((((FL + BL) + posX) + ((FR + BR) + posX))/4) / robot.ticksPerInch;
        double posZ = (-(-((FL + BL) - (posX)) + ((FR+BR) + (posX)))/4) / robot.ticksPerInch;

        return posY;
    }

    private double robotZ() {
        double FL = robot.FrontLeftDrive.getCurrentPosition();
        double FR = robot.FrontRightDrive.getCurrentPosition();
        double BL = robot.BackLeftDrive.getCurrentPosition();
        double BR = robot.BackRightDrive.getCurrentPosition();


        double posX = ((FL + (-FR)) + (-((BL + (-BR))))/4) / robot.ticksPerInch;
        double posY = ((((FL + BL) + posX) + ((FR + BR) + posX))/4) / robot.ticksPerInch;
        double posZ = (-(-((FL + BL) - (posX)) + ((FR+BR) + (posX)))/4) / robot.ticksPerInch;

        return posZ;
    }

    private double distance(double x1, double x2, double y1, double y2) {
        return Math.sqrt((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1));
    }

    ///////////////////////////////////////////////////////////////////// TENSORFLOW RELATED ///////
    private void scanObjects() {
        if (opModeIsActive()) { //Scanning cone
            while (opModeIsActive() && posToPark == 0) {
                if (tfod != null) {
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        for (Recognition recognition : updatedRecognitions) {

                            if (recognition.getLabel().equals("Pos 1")) {
                                posToPark = 1;
                            } else if (recognition.getLabel().equals("Pos 2")) {
                                posToPark = 2;
                            } else if (recognition.getLabel().equals("Pos 3")) {
                                posToPark = 3;
                            }

                            telemetry.addData("Parking Position",posToPark);
                            telemetry.update();
                        }
                    }
                }
            }
        }
    }

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
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABLES);
        //tfod.loadModelFromFile(TFOD_MODEL_FILE, LABELS);
    }
}

