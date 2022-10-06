/* Name: Hex Bot Main
 * Desc: Controls the manualy
 * Team: 10662
 * Developed By: Chester, Kyler, Dillon, Joe, Talon(the great).
 * Date Updated: 09/19/22
 */

//#$#$#$#$#$#$#$#$#$#$#$#$#$> IMPORTS <#$##$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

//#$#$#$#$#$#$#$#$#$#$#$#$#$> MAIN <#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#
@TeleOp(name="Main :: Op Mode", group="Robot")
//@Disabled
public class MainOpMode extends OpMode{

    //Declares Op Members
    public DcMotor  BL   = null;
    public DcMotor  FL = null;
    public DcMotor  BR   = null;
    public DcMotor  FR   = null;
    public Servo    CLAW1 = null;
    public Servo    CLAW2 = null;

    //Public Moter Offset
    public double BLoffset = 0;
    public double FLoffset = 0;
    public double BRoffset = 0;
    public double FRoffset = 0.0147;

    //Public Servo Positions
    public double CLAW1open = 0.25;
    public double CLAW2open = 0.7;

    public double CLAW1close = 0.05;
    public double CLAW2close = 0.9;

    @Override //>>>>>>>>>>>>>> INT <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    public void init() { //Runs ONCE when driver hits INIT <<
        //Sets motors
        BL  = hardwareMap.get(DcMotor.class, "BL");
        FL  = hardwareMap.get(DcMotor.class, "FL");
        BR  = hardwareMap.get(DcMotor.class, "BR");
        FR  = hardwareMap.get(DcMotor.class, "FR");

        BL.setDirection(DcMotor.Direction.FORWARD);
        FL.setDirection(DcMotor.Direction.REVERSE);
        BR.setDirection(DcMotor.Direction.REVERSE);
        FR.setDirection(DcMotor.Direction.REVERSE);

        //Set servos
        CLAW1 = hardwareMap.get(Servo.class, "claw1");
        CLAW2 = hardwareMap.get(Servo.class, "claw2");

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

        //Doubles speed when right bumper 1 is pressed
        if (gamepad1.right_bumper) {
            axial = (-gamepad1.left_stick_y);
            lateral = (gamepad1.left_stick_x);
            yaw = (gamepad1.right_stick_x);
        } else {
            axial = (-gamepad1.left_stick_y) / 2;
            lateral = (gamepad1.left_stick_x) / 2;
            yaw = (gamepad1.right_stick_x) / 2;
        }

        //Simple algebra to tell moters their speed for movement and strafe
        double leftFrontPower = axial + lateral + yaw;
        double rightFrontPower = axial - lateral - yaw;
        double leftBackPower = axial - lateral + yaw;
        double rightBackPower = axial + lateral - yaw;

        //Setting power to moters
        FL.setPower(leftFrontPower + FLoffset);
        FR.setPower(rightFrontPower + FRoffset);
        BL.setPower(leftBackPower + BLoffset);
        BR.setPower(rightBackPower + BRoffset);

        //===========GAMEPAD2=====================
        //Open and close claw
        if (gamepad2.x) {
            CLAW1.setPosition(CLAW1open);
            CLAW2.setPosition(CLAW2open);
        } else if (gamepad2.a) {
            CLAW1.setPosition(CLAW1close);
            CLAW2.setPosition(CLAW2close);
        }


        //===========TELEMETRY=====================
        //Motors
        telemetry.addData("FL ",  "%.2f", leftFrontPower);
        telemetry.addData("BL ",  "%.2f", leftBackPower);
        telemetry.addData("FR",  "%.2f", rightFrontPower);
        telemetry.addData("BR",  "%.2f", rightBackPower);
        //Claw
        telemetry.addData("LeftClaw", "%.2f", CLAW1.getPosition());
        telemetry.addData("RightClaw", "%.2f", CLAW2.getPosition());
    }

    @Override //>>>>>>>>>>>>>> STOP <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    public void stop() { //Runs ONCE when driver hits STOP <<
    }
}