package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@Autonomous(name="Autonomous: Super Simple Park", group="Robot")
public class SimpleAutonomous extends LinearOpMode {
    Robot10662Hardware robot = new Robot10662Hardware();

    ElapsedTime runtime = new ElapsedTime();

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

    private int park = 0;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        initVuforia();
        initTfod();

        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(1.0, 16.0/9.0);
        }

        telemetry.addData(">", "Press Play to start");
        telemetry.update();
        waitForStart();

        grab();
        armHeight(5);

        if (opModeIsActive()) {
            while (park == 0 && opModeIsActive()) {
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
                            telemetry.addData("- Size (Width/Height)","%.0f / %.0f", width, height);*/

                            if (recognition.getLabel().equals("Position 1")) {
                                park = 1;
                            } else if (recognition.getLabel().equals("Position 2")) {
                                park = 2;
                            } else if (recognition.getLabel().equals("Position 3")) {
                                park = 3;
                            }
                        }
                        //telemetry.update();
                    }
                }
            }
        }

        armHeight(0);
        moveInches(0,56);
        moveInches(-12,0);
        drop();

        if (park == 1) {
            moveInches(-12,0);
        } else if (park == 2) {
            moveInches(12,0);
        } else {
            moveInches(36,0);
        }


    }

    private void moveInches(double x, double y) {
        int targetX = (int)x * (int)robot.ticksPerInch;
        int targetY= (int)y * (int)robot.ticksPerInch;

        int flTarget = targetY + targetX;
        int frTarget = targetY - targetX;
        int blTarget = targetY - targetX;
        int brTarget = targetY + targetX;

        robot.FrontLeftDrive.setTargetPosition(flTarget);
        robot.FrontLeftDrive.setTargetPosition(frTarget);
        robot.FrontLeftDrive.setTargetPosition(blTarget);
        robot.FrontLeftDrive.setTargetPosition(brTarget);

        robot.FrontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.FrontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.BackRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.BackLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        if (targetX + targetY > 2000) {
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
        }

        robot.FrontLeftDrive.setTargetPosition(robot.FrontLeftDrive.getCurrentPosition());
        robot.FrontRightDrive.setTargetPosition(robot.FrontLeftDrive.getCurrentPosition());
        robot.BackLeftDrive.setTargetPosition(robot.BackLeftDrive.getCurrentPosition());
        robot.BackRightDrive.setTargetPosition(robot.BackRightDrive.getCurrentPosition());

        robot.FrontLeftDrive.setPower(0.5);
        robot.FrontRightDrive.setPower(0.5);
        robot.BackLeftDrive.setPower(0.5);
        robot.BackRightDrive.setPower(0.5);

        runtime.reset();
        while (runtime.seconds() < 0.2) {}
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

        robot.Arm0.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.Arm1.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        if (robot.armHeight[i] + robot.Arm0.getCurrentPosition() > 2000) {
            robot.Arm0.setPower(0.6);
            robot.Arm1.setPower(0.6);
        } else {
            robot.Arm0.setPower(0.4);
            robot.Arm1.setPower(0.4);
        }

        while (robot.Arm0.isBusy() || robot.Arm1.isBusy()) {
        }

        robot.Arm0.setTargetPosition(robot.Arm0.getCurrentPosition());
        robot.Arm1.setTargetPosition(robot.Arm1.getCurrentPosition());

        robot.Arm0.setPower(0.5);
        robot.Arm1.setPower(0.5);
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
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
        // tfod.loadModelFromFile(TFOD_MODEL_FILE, LABELS);
    }
}
