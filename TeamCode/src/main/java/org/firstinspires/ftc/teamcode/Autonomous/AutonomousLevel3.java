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
    private final double movementSpeed = 0.5;
    private final double pauseBetweenActions = 0.2;


    ///////////////////////////////////////////////////////////////////// SETUP ////////////////////
    //Defining the config files
    Robot10662Hardware robot = new Robot10662Hardware();

    //Timers
    ElapsedTime runtime = new ElapsedTime();

    //Variables
    private String side = "Left";
    private int parkingPos = 0;

    private static final String TFOD_MODEL_ASSET = "CustomSleeve.tflite";

    private static final String[] LABELS = {
            "Pos No",
            "Pos 1",
            "Pos 2",
            "Pos 3",
    };

    private static final String VUFORIA_KEY =
            "AS3eJNb/////AAABmZW+oXMs1UvbvQspLl6ESTk42fukxk9mH7yBB42bMCBv5PcjTcoDufjeE9Nv84UtCar6bi20b7RncpSLLfnB4uEk4WJel/+UcryTmrrCSrg0sTMuGjnTToDLFwM2iIyYFCg0CWaF5basGzUGIAISiNQou5wYYgFYMUd9pPoseL1HJ32JQ4054XhEvDRdo+tywvhM9NTHRaUK2e5lnSOXQW+TznS85crV/McgshAla0uuVndPYtXWDwNok7bwdST0ajAnTMUSVlEZXWlPQmsSE4H+wVO5j8DQro5JfPok/7jkk+n5hTdY+3ZqKa10jQZ37kU9yOCtV6ToOL0uNeef7oiMx8XXOUR83y3+0oKwXdw0";

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

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

        grab();
        scanObjects();
        moveXY(24,0);
        if(side.equals("Left")) {
            moveZ(90, true);
            moveXY(0,-30);
        } else {
            moveZ(-90,true);
        }


        //Wait forever for debug purposes
        while (opModeIsActive()) {}

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
        robot.FrontLeftDrive.setPower(movementSpeed);
        robot.FrontRightDrive.setPower(movementSpeed);
        robot.BackLeftDrive.setPower(movementSpeed);
        robot.BackRightDrive.setPower(movementSpeed);

        //Waiting until finished
        while (robot.FrontLeftDrive.isBusy() && robot.FrontRightDrive.isBusy() && robot.BackLeftDrive.isBusy() && robot.BackRightDrive.isBusy()) {}

        //Adds a little buffer before the next action
        waitTime(pauseBetweenActions);
    }

    //Z Movement
    private void moveZ(double z, boolean toAbs) {
        //Getting Values
        double robotCurrentZ = robot.getAngle(); //Getting robots current position (will be reset later in the code to its current)
        double targetZ = toAbs?-z:-(robotCurrentZ + z); //Target z angle for the end result
        String turnDirection = (targetZ<robotCurrentZ)?"Left":"Right"; //Checks if the robot needs to turn left or right

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
        while ((turnDirection.equals("Left"))?(robotCurrentZ >= targetZ):(robotCurrentZ <= targetZ)) {
            //Setting angle
            robotCurrentZ = robot.getAngle();
        }

        //Stopping Bot
        robot.FrontLeftDrive.setPower(0);
        robot.FrontRightDrive.setPower(0);
        robot.BackLeftDrive.setPower(0);
        robot.BackRightDrive.setPower(0);
    }

    //Arm Y Movement
    private void armMoveY(double y) {
        //Getting target position
        int targetY = (int)(y * robot.ticksPerInch) + robot.Arm.getCurrentPosition();

        //Setting target position
        robot.Arm.setTargetPosition(targetY);

        //Setting mode
        robot.Arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Setting power
        robot.Arm.setPower(movementSpeed);

        //Waiting until finished
        while (robot.Arm.isBusy()) {}
    }

    //Claw Grab
    private void grab() {
        robot.Claw0.setPosition(robot.clawClose[0]);
        robot.Claw1.setPosition(robot.clawClose[0]);
    }

    //Claw Drop
    private void drop() {
        robot.Claw0.setPosition(robot.clawOpen[0]);
        robot.Claw1.setPosition(robot.clawOpen[1]);
    }

    //Scan Objects
    private void scanObjects() {
        if (opModeIsActive()) { //Scanning cone
            while (opModeIsActive() && parkingPos == 0) {
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
        while (runtime.seconds() <= time) {} //Waits until inputted time is met
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