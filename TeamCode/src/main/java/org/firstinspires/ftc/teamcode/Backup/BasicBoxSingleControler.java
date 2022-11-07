/* Name: Hex Bot Main
 * Desc: Controls the manualy
 * Team: 10662
 * Developed By: Chester, Kyler, Dillon, Joe, Talon(the great).
 * Date Updated: 09/19/22
 */

//#$#$#$#$#$#$#$#$#$#$#$#$#$> IMPORTS <#$##$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#
package org.firstinspires.ftc.teamcode.Backup;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

//#$#$#$#$#$#$#$#$#$#$#$#$#$> MAIN <#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#
@TeleOp(name="Basic Box: Single Controller", group="Robot")
@Disabled
public class BasicBoxSingleControler extends OpMode{

    //Declares Op Members
    public DcMotor  leftDrive   = null;
    public DcMotor  rightDrive  = null;
    public DcMotor  armHeight   = null;

    public Servo    grabber     = null;

    //Public variables
    public static final double grabberOpen = 0.4 ;
    public static final double grabberClose = 0.1 ;

    @Override //>>>>>>>>>>>>>> INT <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    public void init() { //Runs ONCE when driver hits INIT <<
        //Sets motors
        leftDrive  = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");
        armHeight    = hardwareMap.get(DcMotor.class, "arm_height");

        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);

        //Set servos
        grabber = hardwareMap.get(Servo.class, "grabber");

        //Setting defualt position
        grabber.setPosition(grabberOpen);

        //Signals ready
        telemetry.addData(">", "Robot Ready.  Press Play.");
    }

    @Override //>>>>>>>>>>>>>> INIT LOOP <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    public void init_loop() { //Runs REPEATEDLY when driver hits INIT (Before play) <<
    }

    @Override //>>>>>>>>>>>>>> START <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    public void start() { //Runs ONCE when driver hits PLAY <<
    }

    @Override //>>>>>>>>>>>>>> LOOP <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    public void loop() { //Runs REPEATEDLY when driver hits PLAY <<
        //Keybindings
        double left = (-gamepad1.left_stick_y + gamepad1.left_stick_x);
        double right = (-gamepad1.left_stick_y - gamepad1.left_stick_x);

        double arm = gamepad1.right_stick_y;

        //Movement
        leftDrive.setPower(left);
        rightDrive.setPower(right);

        //Arm Movement
        armHeight.setPower(arm);

        //Grabber Movement
        if (gamepad1.left_bumper)
            grabber.setPosition(grabberClose);
        else if (gamepad1.right_bumper)
            grabber.setPosition(grabberOpen);

        //Displays debug variables
        telemetry.addData("left",  "%.2f", left);
        telemetry.addData("right", "%.2f", right);
        telemetry.addData("arm", "%.2f", arm);
        telemetry.addData("open", "%.2f", grabberOpen);
        telemetry.addData("close", "%.2f", grabberClose);
    }

    @Override //>>>>>>>>>>>>>> STOP <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    public void stop() { //Runs ONCE when driver hits STOP <<
    }
}