/* !CAVEMAN AUTONOMOUS LEVEL 0
    Ever wondered why this is a level one caveman script, well the only thing it dose is drive
    forward. Yup, for those dyer situations like the first scrimmage where we had super big bugs with
    autonomous to the point that it did not even work! This script simply parks the bot one square
    in-front of it.
 */

///////////////////////////////////////////////////////////////////// IMPORT ///////////////////////
package org.firstinspires.ftc.teamcode.Testing;

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
@Autonomous(name="Imu Drive Correction", group = "Robot")
public class ImuDriveCorrection extends LinearOpMode {
    ///////////////////////////////////////////////////////////////////// CONFIGURATION ////////////
    private final double movementSpeed = 0.60; //Global speed for the robots movment seped during actions
    private final double slowMovementSpeed = 0.5; //Global slow speed for the robots movement speed durring actions
    private final double normalSpeedDistance = 20; //If under inches will activate slow speed

    ///////////////////////////////////////////////////////////////////// SETUP ////////////////////
    //Defining the config files
    Robot10662Hardware robot = new Robot10662Hardware(); //Creating hardwareclass

    ///////////////////////////////////////////////////////////////// OP MODE //////////////////////
    @Override
    public void runOpMode() {
        //Setting motors ect
        robot.init(hardwareMap);

        //Waiting for the user
        telemetry.addData(">>","LET IT RIP! Smack that play button!");
        telemetry.update();
        waitForStart();
        ///////////////////////////////////////////////////////////// RUNNING //////////////////////

        moveXY(100,0);

    }

    ///////////////////////////////////////////////////////////////////// MOVEMENT /////////////////
    private void moveXY(double y, double x) {
        //Saving starting angle
        double startingAngle = robot.getAngle(); //Used later to say going in a straight line

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

        //Define ing base power
        double rawFlSpeed;
        double rawFrSpeed;
        double rawBlSpeed;
        double rawBrSpeed;
        //Setting base power
        if(Math.abs(y) + Math.abs(x) >= normalSpeedDistance) {
            rawFlSpeed = movementSpeed;
            rawFrSpeed = movementSpeed;
            rawBlSpeed = movementSpeed;
            rawBrSpeed = movementSpeed;
        } else {
            rawFlSpeed = slowMovementSpeed;
            rawFrSpeed = slowMovementSpeed;
            rawBlSpeed = slowMovementSpeed;
            rawBrSpeed = slowMovementSpeed;
        }

        while (robot.FrontLeftDrive.isBusy() && robot.FrontRightDrive.isBusy() && robot.BackLeftDrive.isBusy() && robot.BackRightDrive.isBusy() && opModeIsActive()) {
            //Finding distance off center
            double distanceOffCenter = (robot.getAngle() - startingAngle) * y+x!=0?y+x/Math.abs(y+x):1;

            //Defining heading offset for each motor
            double flSpeed = rawFlSpeed + 0 + (distanceOffCenter/30);
            double frSpeed = rawFrSpeed - 0 - (distanceOffCenter/30);
            double blSpeed = rawBlSpeed - 0 + (distanceOffCenter/30);
            double brSpeed = rawBrSpeed + 0 - (distanceOffCenter/30);

            //Using above to correct speed
            robot.FrontLeftDrive.setPower(flSpeed);
            robot.FrontRightDrive.setPower(frSpeed);
            robot.BackLeftDrive.setPower(blSpeed);
            robot.BackRightDrive.setPower(brSpeed);

            //Telemetry
            telemetry.addData("Current Action:","moveXY");
            telemetry.addData("Input", "Y:" + y + " X:" + x);
            telemetry.addData("Starting Heading", startingAngle);
            telemetry.addData("Current Heading", robot.getAngle());
            telemetry.addData("Heading Offset", distanceOffCenter);
            telemetry.addData("","");
            telemetry.addData("FL", "TargetPos:" + frontLeftPos + " CurrentPos:" + robot.FrontLeftDrive.getCurrentPosition() + " CurrentPower:" + robot.FrontLeftDrive.getPower() + " Heading Offset" + flSpeed);
            telemetry.addData("FR", "TargetPos:" + frontRightPos + " CurrentPos:" + robot.FrontRightDrive.getCurrentPosition() + " CurrentPower:" + robot.FrontRightDrive.getPower() + " Heading Offset" + frSpeed);
            telemetry.addData("BL", "TargetPos:" + backLeftPos + " CurrentPos:" + robot.BackLeftDrive.getCurrentPosition() + " CurrentPower:" + robot.BackLeftDrive.getPower() + " Heading Offset" + blSpeed);
            telemetry.addData("BR", "TargetPos:" + backRightPos + " CurrentPos:" + robot.BackRightDrive.getCurrentPosition() + " CurrentPower:" + robot.BackRightDrive.getPower() + " Heading Offset" + brSpeed);
            telemetry.update();
        }

        //Setting speed equal to all to prevent the robot tweaking at an angle
        robot.FrontLeftDrive.setPower(rawFlSpeed);
        robot.BackRightDrive.setPower(rawBrSpeed);
        robot.BackLeftDrive.setPower(rawBlSpeed);
        robot.FrontRightDrive.setPower(rawFrSpeed);

        //waitTime(pauseBetweenActions);
    }

}