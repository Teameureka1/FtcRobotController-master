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

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.Config.Robot10662Hardware;

import java.util.List;

///////////////////////////////////////////////////////////////////// CLASS ////////////////////////
@Autonomous(name="Autonomous :: Level 3", group = "Robot")
public class AutonomousLevel3 extends LinearOpMode {
    ///////////////////////////////////////////////////////////////////// CONFIGURATION ////////////
    private final double movementSpeed = 0.70; //Global speed for the robots movment seped during actions
    private final double slowMovementSpeed = 0.5; //Global slow speed for the robots movement speed durring actions
    private final double normalSpeedDistance = 20; //If under inches will activate slow speed
    private final double armMovementSPeed = 1; //Global speed for the robots arm movemeny speed durring actions
    private final double pauseBetweenActions = 0.2; //Amount of seconds the robot will pause for some actions
    private final double tfodTimeout = 1; //Seconds until tfod will time out
    private final double waitForArmTimeout = 3; //Seconds until arm will time out

    private final boolean sideSelectorEnabled = false; //If disabled robot will default to the left side of the field
    //TODO: DONT FORGET TO ENAbLE

    ///////////////////////////////////////////////////////////////////// SETUP ////////////////////
    //Defining the config files
    Robot10662Hardware robot = new Robot10662Hardware(); //Creating hardwareclass

    //Timers
    ElapsedTime runtime = new ElapsedTime(); //Used to keep track of time

    //Variables
    private String side = "Left"; //Robots position on field, later modified, defualts to left
    private int parkingPos = 0; //Position for robot to park in signaled by cone, later changed

    private static final String TFOD_MODEL_ASSET = "CustomSleeve.tflite"; //Tflite file used for object detection

    private static final String[] LABELS = { //Possibile parking positions
            "Pos No",
            "Pos 1",
            "Pos 2",
            "Pos 3",
    };

    private static final String VUFORIA_KEY = //Validation key for vulforia
            "AS3eJNb/////AAABmZW+oXMs1UvbvQspLl6ESTk42fukxk9mH7yBB42bMCBv5PcjTcoDufjeE9Nv84UtCar6bi20b7RncpSLLfnB4uEk4WJel/+UcryTmrrCSrg0sTMuGjnTToDLFwM2iIyYFCg0CWaF5basGzUGIAISiNQou5wYYgFYMUd9pPoseL1HJ32JQ4054XhEvDRdo+tywvhM9NTHRaUK2e5lnSOXQW+TznS85crV/McgshAla0uuVndPYtXWDwNok7bwdST0ajAnTMUSVlEZXWlPQmsSE4H+wVO5j8DQro5JfPok/7jkk+n5hTdY+3ZqKa10jQZ37kU9yOCtV6ToOL0uNeef7oiMx8XXOUR83y3+0oKwXdw0";
    private VuforiaLocalizer vuforia; //Initializing vulforia

    private TFObjectDetector tfod; //Initializing tfod

    ///////////////////////////////////////////////////////////////// OP MODE //////////////////////
    @Override
    public void runOpMode() {
        //Setting motors ect
        robot.init(hardwareMap);


        //Asking driver what side the robot is on
        if(sideSelectorEnabled) { //Only runs if enabled
            int tmp = 0; //Temporary value
            if (opModeInInit() || opModeIsActive()) { //Runs forever, but makes sure that the robot is still running
                while (opModeIsActive() || opModeInInit()) {
                    //Break if robot quit
                    if (!(opModeIsActive() || opModeInInit())) {
                        break;
                    }

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

                    //Debug values for selecting
                    if (gamepad1.dpad_up || gamepad2.dpad_up) {
                        tmp = 0;
                    }

                    if (gamepad1.dpad_down || gamepad2.dpad_down) {
                        tmp = 1;
                    }

                    //Updating telemetry/info
                    telemetry.update();
                }
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

        grab(); //Grabs preloaded Cone
        scanObjects(); //Scans signal cone
        moveXYandArmY(61,0,robot.armPositions[2]); //Pushing signal sleeve out of the way and raising
        waitTime(0.2); //Small wait to prevent the bot from being knocked off of track
        moveZ(-2,true);
        moveXYandArmY(-10,0,robot.armPositions[3]); //Backup up and raising arm to the high junction
        moveZ(40, true); //Turning towards junction
        moveXY(19,0); //Aproching junction
        moveArmY(robot.armPositions[3]-600); //Lowers arm
        drop(); //Drops cone
        moveXY(-15.5,0); //Backs away from junction

        for(int i = 1; i <= 2; i++) {
            moveArmY(robot.coneStackBase*(5-(i-1)));
            moveZ(-81,true);
            moveXY(26,0);
            grab();
            moveArmY(robot.armPositions[1]);
            waitForArm();
            moveXYandArmY(-24,0, robot.armPositions[3]);
            moveZ(38,true);
            moveXY(18,0);
            moveArmY(robot.armPositions[3]-600);
            drop(); //Drops cone
            moveXY(-15.5,0);
        }

        moveArmY(0);
        moveZ(2,true);
        waitForArm();



    }

    ///////////////////////////////////////////////////////////////////// MOVEMENT /////////////////
    //XY Movement
    private void moveXY(double y, double x) {
        //Getting robot target position
        int frontLeftPos = (int)((y + x) * robot.ticksPerInch) + robot.FrontLeftDrive.getCurrentPosition();
        int frontRightPos = (int)((y - x) * robot.ticksPerInch) + robot.FrontRightDrive.getCurrentPosition();
        int backLeftPos = (int)((y - x) * robot.ticksPerInch) + robot.BackLeftDrive.getCurrentPosition();
        int backRightPos = (int)((y + x) * robot.ticksPerInch) + robot.BackRightDrive.getCurrentPosition();

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
        if(Math.abs(y) + Math.abs(x) >= normalSpeedDistance) {
            robot.FrontLeftDrive.setPower(movementSpeed);
            robot.FrontRightDrive.setPower(movementSpeed);
            robot.BackLeftDrive.setPower(movementSpeed);
            robot.BackRightDrive.setPower(movementSpeed);
        } else {
            robot.FrontLeftDrive.setPower(slowMovementSpeed);
            robot.FrontRightDrive.setPower(slowMovementSpeed);
            robot.BackLeftDrive.setPower(slowMovementSpeed);
            robot.BackRightDrive.setPower(slowMovementSpeed);
        }

        waitForArm();
    }

    //Z Movement
    private void moveZ(double z, boolean toAbs) {
        //Getting Values
        double robotCurrentZ = robot.getAngle(); //Getting robots current position (will be reset later in the code to its current)
        double targetZ = toAbs?-z:-(robotCurrentZ + z); //Target z angle for the end result
        String turnDirection = (targetZ<robotCurrentZ)?"Left":"Right"; //Checks if the robot needs to turn left or right

        //Adding offset based on amount needed to turn
        double imuTurnOffset = (10);
        targetZ = turnDirection.equals("Left")?targetZ+imuTurnOffset:targetZ-imuTurnOffset;


        //Setting mode
        robot.FrontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.FrontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.BackLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.BackRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //Turning Direction indicated
        if (turnDirection.equals("Left")) {
            robot.FrontLeftDrive.setPower(movementSpeed);
            robot.FrontRightDrive.setPower(-movementSpeed);
            robot.BackLeftDrive.setPower(movementSpeed);
            robot.BackRightDrive.setPower(-movementSpeed);
        } else {
            robot.FrontLeftDrive.setPower(-movementSpeed);
            robot.FrontRightDrive.setPower(movementSpeed);
            robot.BackLeftDrive.setPower(-movementSpeed);
            robot.BackRightDrive.setPower(movementSpeed);
        }

        //Robot needs to turn left
        //  robotCurrentZ >= targetZ
        //Robot needs to turn right
        //  robotCurrentZ <= targetZ
        while ((turnDirection.equals("Left"))?(robotCurrentZ >= targetZ):(robotCurrentZ <= targetZ) && opModeIsActive()) {
            //Setting angle
            robotCurrentZ = robot.getAngle();
        }

        //Stop n Hold
        robot.FrontLeftDrive.setTargetPosition(robot.FrontLeftDrive.getCurrentPosition());
        robot.FrontRightDrive.setTargetPosition(robot.FrontRightDrive.getCurrentPosition());
        robot.BackLeftDrive.setTargetPosition(robot.BackLeftDrive.getCurrentPosition());
        robot.BackRightDrive.setTargetPosition(robot.BackRightDrive.getCurrentPosition());

        //Setting mode
        robot.FrontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.FrontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.BackLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.BackRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Setting power
        robot.FrontLeftDrive.setPower(movementSpeed);
        robot.FrontRightDrive.setPower(movementSpeed);
        robot.BackLeftDrive.setPower(movementSpeed);
        robot.BackRightDrive.setPower(movementSpeed);

        //Short wait to let the robot fully stop
        waitTime(pauseBetweenActions);
    }

    //Arm Y Movement
    private void moveArmY(int y) {
        //Getting target position
        int targetY = (y);

        //Setting target position
        robot.Arm.setTargetPosition(targetY);

        //Setting mode
        robot.Arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Setting power
        robot.Arm.setPower(armMovementSPeed);

        //Short wait to let the robot fully stop
        waitTime(pauseBetweenActions);
    }

    private void moveXYandArmY(double y, double x, int armY) {
        //Getting robot target position
        int frontLeftPos = (int)((y + x) * robot.ticksPerInch) + robot.FrontLeftDrive.getCurrentPosition();
        int frontRightPos = (int)((y - x) * robot.ticksPerInch) + robot.FrontRightDrive.getCurrentPosition();
        int backLeftPos = (int)((y - x) * robot.ticksPerInch) + robot.BackLeftDrive.getCurrentPosition();

        int backRightPos = (int)((y + x) * robot.ticksPerInch) + robot.BackRightDrive.getCurrentPosition();

        //Arm target position
        int targetY = (armY);

        //Setting target position to motors
        robot.FrontLeftDrive.setTargetPosition(frontLeftPos);
        robot.FrontRightDrive.setTargetPosition(frontRightPos);
        robot.BackLeftDrive.setTargetPosition(backLeftPos);
        robot.BackRightDrive.setTargetPosition(backRightPos);

        //Setting arm target position
        robot.Arm.setTargetPosition(targetY);


        //Setting mode0
        robot.FrontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.FrontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.BackLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.BackRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Setting arm mode
        robot.Arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Setting power
        if(Math.abs(y) + Math.abs(x) >= normalSpeedDistance) {
            robot.FrontLeftDrive.setPower(movementSpeed);
            robot.FrontRightDrive.setPower(movementSpeed);
            robot.BackLeftDrive.setPower(movementSpeed);
            robot.BackRightDrive.setPower(movementSpeed);
        } else {
            robot.FrontLeftDrive.setPower(slowMovementSpeed);
            robot.FrontRightDrive.setPower(slowMovementSpeed);
            robot.BackLeftDrive.setPower(slowMovementSpeed);
            robot.BackRightDrive.setPower(slowMovementSpeed);
        }

        //Setting arm speed
        robot.Arm.setPower(armMovementSPeed);

        //Waiting until finished
        while (robot.FrontLeftDrive.isBusy() && robot.FrontRightDrive.isBusy() && robot.BackLeftDrive.isBusy() && robot.BackRightDrive.isBusy() && opModeIsActive()) {}

        //Short wait to let the robot fully stop
        waitTime(pauseBetweenActions);
    }

    //Wait for Arm
    private void waitForArm() {
        runtime.reset();
        while (robot.Arm.isBusy() && runtime.seconds() <= waitForArmTimeout) {}
    }

    //Claw Grab
    private void grab() {
        robot.Claw0.setPosition(robot.clawClose[0]);
        robot.Claw1.setPosition(robot.clawClose[0]);

        //Waiting to allow claw to close before next action
        waitTime(0.2);
    }

    //Claw Drop
    private void drop() {
        robot.Claw0.setPosition(robot.clawOpen[0]);
        robot.Claw1.setPosition(robot.clawOpen[1]);

        //Waiting to allow claw to open before next action
        waitTime(0.2);
    }

    //Scan Objects
    private void scanObjects() {
        //Reseting timer for later
        runtime.reset();
        if (opModeIsActive()) { //Scanning cone
            while (opModeIsActive() && parkingPos == 0) {
                if(runtime.seconds() >= tfodTimeout || !opModeIsActive()) { //Timeout reached or program quit
                    parkingPos = 2; //Setting parking pos to 2 and quiting
                    break;
                }
                if (tfod != null) {
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        for (Recognition recognition : updatedRecognitions) {

                            if (recognition.getLabel().equals("Pos 1")) {
                                parkingPos = 1;
                            } else if (recognition.getLabel().equals("Pos 2")) {
                                parkingPos = 2;
                            } else if (recognition.getLabel().equals("Pos 3")) {
                                parkingPos = 3;
                            }

                            telemetry.addData("Parking Position",parkingPos);
                            telemetry.update();
                        }
                    }
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////// OTHER ////////////////////
    private void waitTime(double time) {
        runtime.reset(); //Resets timer
        while (runtime.seconds() <= time && opModeIsActive()) {} //Waits until inputted time is met
    }

    ///////////////////////////////////////////////////////////////////// TFOD /////////////////////
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
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
        //tfod.loadModelFromFile(TFOD_MODEL_FILE, LABELS);
    }

}