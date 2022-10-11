/* Name: Hex Bot Main
 * Desc: Controls the manually
 * Team: 10662
 * Developed By: Chester, Kyler, Dillon, Joe, Talon(the great).
 * Date Updated: 09/19/22
 */

//#$#$#$#$#$#$#$#$#$#$#$#$#$> IMPORTS <#$##$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

//#$#$#$#$#$#$#$#$#$#$#$#$#$> MAIN <#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#
@Autonomous(name="Main :: Auto Mode Test", group="Robot")
//@Disabled
public class TestAutoMode extends LinearOpMode {
    //Runtime
    private ElapsedTime runtime = new ElapsedTime();

    //Declares Op Members
    public DcMotor  BL   = null;
    public DcMotor  FL = null;
    public DcMotor  BR   = null;
    public DcMotor  FR   = null;
    public DcMotor ARM1 = null;
    public DcMotor ARM2 = null;
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

    @Override
        public void runOpMode() { //Runs once started
        //>>>>>>>>>>>>>> INT <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            //Sets motors
            BL  = hardwareMap.get(DcMotor.class, "BL");
            FL  = hardwareMap.get(DcMotor.class, "FL");
            BR  = hardwareMap.get(DcMotor.class, "BR");
            FR  = hardwareMap.get(DcMotor.class, "FR");
            ARM1 = hardwareMap.get(DcMotor.class, "ARM1");
            ARM2 = hardwareMap.get(DcMotor.class, "ARM2");

            BL.setDirection(DcMotor.Direction.FORWARD);
            FL.setDirection(DcMotor.Direction.REVERSE);
            BR.setDirection(DcMotor.Direction.REVERSE);
            FR.setDirection(DcMotor.Direction.REVERSE);

            ARM1.setDirection(DcMotorSimple.Direction.REVERSE);
            ARM1.setDirection(DcMotorSimple.Direction.REVERSE);

            //Set servos
            CLAW1 = hardwareMap.get(Servo.class, "claw1");
            CLAW2 = hardwareMap.get(Servo.class, "claw2");

            //Opening claw to try and prevent the claw from getting jammed in some way... You get the point
            CLAW1.setPosition(CLAW1open);
            CLAW2.setPosition(CLAW2open);

            //Waiting for driver to be ready
            telemetry.addData("~>", "Robot Ready.  Press Play.  Or else...");
            telemetry.update();
            waitForStart();

        //>>>>>>>>>>>>>> AUTO <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


        //Step 1
        moveSeconds(3.4,0,0.3,0);


    }
    //#$#$#$#$#$#$#$#$#$#$#$#$#$> MOVE <#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$
    public void moveSeconds(double seconds, double axial, double lateral, double yaw) {
        //Setting motor speeds with formula
        FL.setPower(axial + lateral + yaw);
        FR.setPower(axial - lateral - yaw);
        BL.setPower(axial - lateral + yaw);
        BR.setPower(axial + lateral - yaw);
        //Repeatedly will run for however many seconds
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < seconds)) {
            telemetry.addData("move", "%4.1f S Elapsed", runtime.seconds());
            telemetry.update();
        }
    }
}