///////////////////////////////////////////////////////////////////// IMPORT ///////////////////////

//#$#$#$#$#$#$#$#$#$#$#$#$#$> IMPORTS <#$##$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#
package org.firstinspires.ftc.teamcode.Testing;

import android.widget.TabHost;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Config.Robot10662Hardware;

///////////////////////////////////////////////////////////////////// CLASS ////////////////////////
@TeleOp(name="FCD Testing", group="Robot")
//@Disabled
public class FCDTesting extends OpMode{
    //Instantiate the Hardware class
    Robot10662Hardware robot = new Robot10662Hardware();

    //Held Motors
    public boolean FLHeld = false;
    public boolean FRHeld = false;
    public boolean BLHeld = false;
    public boolean BRHeld = false;
    public boolean AHeld = false;

    //Debug booleans
    public boolean armDebug = false;

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
        double xCoordinate = gamepad1.left_stick_x;
        double yCoordinate = gamepad1.left_stick_y;
        double zControl = gamepad1.right_stick_x;

        double gamepadRadians = Math.atan2(xCoordinate, -yCoordinate);
        double gamepadHypot = Range.clip(Math.hypot(xCoordinate, yCoordinate), 0, 1);
        double robotRadians = robot.getAngle() * (robot.pi/180);
        double xControl = Math.sin(gamepadRadians - robotRadians) * gamepadHypot;
        double yControl = Math.cos(gamepadRadians - robotRadians) * gamepadHypot;

        telemetry.addData("Target",xControl + " " + yControl);
        telemetry.update();
    }

    @Override /////////////////////////////////////////////////////// STOP /////////////////////////
    public void stop() { //Runs ONCE when driver hits STOP <<
    }


}