/**
 * Welcome to the MainOpMode program
 *
 * This programs purpose is to make the robot follow the drivers commands like a puppy dog
 * and try to be a pleasent experience as well. This program is inteded to be used with two controllers.
 * Controller A controls all of the robots wheel based movement.
 * Controller B controls all of the robots arm and claw movement.
 */

//#$#$#$#$#$#$#$#$#$#$#$#$#$> IMPORTS <#$##$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

//#$#$#$#$#$#$#$#$#$#$#$#$#$> MAIN <#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#
@TeleOp(name="Mr Tester", group="Robot")
//@Disabled
public class Testing extends OpMode{
    //Instantiate the Hardware class
    Robot10662Hardware robot = new Robot10662Hardware();

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
        //===========GAMEPAD1=====================
        //Defining variables
        double axial;
        double lateral;
        double yaw;

        double driveTurn;
        double gamepadXCord;
        double gamepadYCord;
        double gamepadHypot;
        double gamepadDegree;
        double robotDegree;
        double movementDegree;
        double gamepadXControl;
        double gamepadYControl;


        //Epic math I bearly understand but its suppoesd to add driver centric controls
        driveTurn = -gamepad1.right_stick_x;

        gamepadXCord = gamepad1.left_stick_x;
        gamepadYCord = -gamepad1.left_stick_y;
        gamepadHypot = Range.clip(Math.hypot(gamepadXCord, gamepadYCord), 0, 1);

        gamepadDegree = Math.atan2(gamepadYCord, gamepadXCord);
        robotDegree = getAngle();
        movementDegree = gamepadDegree - robotDegree;

        gamepadXControl = Math.cos(Math.toRadians(movementDegree)) * gamepadHypot;
        gamepadYControl = Math.sin(Math.toRadians(movementDegree)) * gamepadHypot;


        //Doubles speed when right bumper 1 is pressed
        if (gamepad1.right_bumper) { //Dividing by two to slow it down
            axial = (gamepadYControl * Math.abs(gamepadYControl));
            lateral = (gamepadXControl * Math.abs(gamepadXControl));
            yaw = (driveTurn);
        } else {
            axial = (gamepadYControl * Math.abs(gamepadYControl)) / 2;
            lateral = (gamepadXControl * Math.abs(gamepadXControl)) / 2;
            yaw = (driveTurn) / 2;
        }

        //Simple algebra to tell moters their speed for movement and strafe
        double frontLeftPower = axial + lateral + yaw;
        double frontRightPower = axial - lateral - yaw;
        double backLeftPower = axial - lateral + yaw;
        double backRightPower = axial + lateral - yaw;




        //===========TELEMETRY=====================
        //Heading
        telemetry.addData("Test", "%.2f :%.2f", gamepadXControl, gamepadYControl);
        //Motors
        telemetry.addData("FL Power",  "%.2f", frontLeftPower);
        telemetry.addData("BL Power",  "%.2f", frontRightPower);
        telemetry.addData("FR Power",  "%.2f", backLeftPower);
        telemetry.addData("BR Power",  "%.2f", backRightPower);
    }

    @Override //>>>>>>>>>>>>>> STOP <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    public void stop() { //Runs ONCE when driver hits STOP <<
    }

    public double getAngle() {
        return robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
    }
}