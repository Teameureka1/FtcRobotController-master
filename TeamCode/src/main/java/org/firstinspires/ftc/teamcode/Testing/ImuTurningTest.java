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
@Autonomous(name="Autonomous IMU Turning Test", group = "Robot")
public class ImuTurningTest extends LinearOpMode {
    ///////////////////////////////////////////////////////////////////// CONFIGURATION ////////////
    private final double movementSpeed = 0.5; //Global speed for the robot when completing actions


    ///////////////////////////////////////////////////////////////////// SETUP ////////////////////
    //Defining the config files
    Robot10662Hardware robot = new Robot10662Hardware(); //Config Class that sets up the robot

    ///////////////////////////////////////////////////////////////// OP MODE //////////////////////
    @Override
    public void runOpMode() {
        //Setting motors ect using above config class
        robot.init(hardwareMap);

        //Waiting for the user to press play
        telemetry.addData(">>","LET IT RIP! Smack that play button!");
        telemetry.update();
        waitForStart();
        ///////////////////////////////////////////////////////////// RUNNING //////////////////////

        moveZ(-45, false);
        moveZ(0, true);

        //Prevent the program from ending and deleting telemetry values
        //TEMPORARY
        while (opModeIsActive()) {}

    }

    //Z Movement
    private void moveZ(double z, boolean toAbs) {
        //Getting Values
        double robotCurrentZ = robot.getAngle(); //Getting robots current position (will be reset later in the code to its current)
        double targetZ = toAbs?z:-(robotCurrentZ + z); //Target z angle for the end result
        String turnDirection = (targetZ<robotCurrentZ)?"Left":"Right"; //Checks if the robot needs to turn left or right
        telemetry.addData("Checkpoint1","");
        telemetry.update();
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
            telemetry.addData("Completed", (turnDirection.equals("Left"))?(robotCurrentZ >= targetZ):(robotCurrentZ <= targetZ));
            telemetry.addData("Angles", "Current Ang: "+ robotCurrentZ + " Target Ang: " + targetZ);
            telemetry.update();
            robotCurrentZ = robot.getAngle();
        }
        robot.FrontLeftDrive.setPower(0);
        robot.FrontRightDrive.setPower(0);
        robot.BackLeftDrive.setPower(0);
        robot.BackRightDrive.setPower(0);
    }
}