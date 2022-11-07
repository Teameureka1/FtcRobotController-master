/**
 * Welcome to the MainOpMode program
 *
 * This programs purpose is to make the robot follow the drivers commands like a puppy dog
 * and try to be a pleasent experience as well. This program is inteded to be used with two controllers.
 * Controller A controls all of the robots wheel based movement.
 * Controller B controls all of the robots arm and claw movement.
 */

//#$#$#$#$#$#$#$#$#$#$#$#$#$> IMPORTS <#$##$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#
package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.Config.Robot10662Hardware;

//#$#$#$#$#$#$#$#$#$#$#$#$#$> MAIN <#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#
@TeleOp(name="Mr Tester", group="Robot")
//@Disabled
public class OdometryTesting extends OpMode{
    //Instantiate the Hardware class
    Robot10662Hardware robot = new Robot10662Hardware();

    double posX;
    double posZ;
    double posY;

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
        double FL = robot.FrontLeftDrive.getCurrentPosition();
        double FR = robot.FrontRightDrive.getCurrentPosition();
        double BL = robot.BackLeftDrive.getCurrentPosition();
        double BR = robot.BackRightDrive.getCurrentPosition();

        posX = ((((FL + (-FR)) - (posZ)) + (-((BL + (-BR)) + (posZ))))/4) / robot.ticksPerInch;
        posZ = (-(-((FL + BL) - (posX)) + ((FR+BR) + (posX)))/4) / robot.ticksPerInch;
        posY = ((((FL + BL) - (posZ + posX)) + ((FR + BR) + (posZ + posX)))/4) / robot.ticksPerInch;

        telemetry.addData("VERTICLE ", posY);
        telemetry.addData("HORIZONTAL ", posX);
        telemetry.addData("YAW ", posZ);
        telemetry.update();

    }

    @Override //>>>>>>>>>>>>>> STOP <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    public void stop() { //Runs ONCE when driver hits STOP <<
    }

    public double getAngle() {
        return robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
    }
}