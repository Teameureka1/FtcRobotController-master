/* Welcome to Team 1662's Autonomous (Dillon, Chester, Kyler, Talon, Emily, Joe, Hunter, and Kaiden)

   Despite being a buggy mess, this autonomous is structured in a semi understandable and reason
   able way. First it asks the user for the input of where the robot is positions then will run a
   class for that position simultaneously so this class can do the backend work while the other
   class is driving the robot though the course.
 */

package org.firstinspires.ftc.teamcode.Autonomous;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Robot10662Hardware;

//#$#$#$#$#$#$#$#$#$#$#$#$#$> MAIN <#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#
@Autonomous(name="Main :: Autonomous", group="Robot")
//@Disabled
public class AutonomousMain extends LinearOpMode {
    //Running
    public boolean running = true;

    //Timer
    private ElapsedTime gameClock = new ElapsedTime();

    //Instantiate the Hardware class
    Robot10662Hardware robot = new Robot10662Hardware();

    //Identifying all other classes
    Thread pos00 = new Thread(new Position00());

    //Current Robot Position
    public double robotPosX = 0;
    public double robotPosY = 0;

    //Offset Robot Position
    public double robotPosXOffset = 0;
    public double robotPosYOffset = 0;

    //Starting Positions
    public final double start00PosX = 35.5;
    public final double start00PosY = 0;

    public final double start01PosX = 35.5;
    public final double start01PosY = 0;

    public final double start10PosX = 35.5;
    public final double start10PosY = 0;

    public final double start11PosX = 35.5;
    public final double start11PosY = 0;

    //Parking Positions
    public final double parkPos001X = 12;
    public final double parkPos001Y = 36;
    public final double parkPos002X = 36;
    public final double parkPos002Y = 36;
    public final double parkPos003X = 60;
    public final double parkPos003Y = 36;

    public final double parkPos011X = -60;
    public final double parkPos011Y = 36;
    public final double parkPos012X = -30;
    public final double parkPos012Y = 36;
    public final double parkPos013X = -12;
    public final double parkPos013Y = 36;

    public final double parkPos101X = -60;
    public final double parkPos101Y = -36;
    public final double parkPos102X = -32;
    public final double parkPos102Y = -36;
    public final double parkPos103X = -12;
    public final double parkPos103Y = -36;

    public final double parkPos111X = 12;
    public final double parkPos111Y = -36;
    public final double parkPos112X = 36;
    public final double parkPos112Y = -36;
    public final double parkPos113X = 60;
    public final double parkPos113Y = -36;


    @SuppressLint("SuspiciousIndentation")
    @Override
    public void runOpMode() { //Runs once started
        //>>>>>>>>>>>>>> INT <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        //Use 'init' methods from Hardware class to Map hardware to match robot's config
        robot.init(hardwareMap);

        //Modes
        int teamSelection = 0;
        int sideSelection = 0;

        //SELECTING TEAM
        while (true) {
            //Title
            telemetry.addData(">>", "Select Team");
            //Selection Output
            if (teamSelection == 0) {
                telemetry.addData("   *", "Red Team");
                telemetry.addData("    ", "Blue Team");
            } else {
                telemetry.addData("    ", "Red Team");
                telemetry.addData("   *", "Blue Team");
            }

            //Selector
            if (gamepad1.a) { //Select
            while (gamepad1.a) {} //Waits for button to be unselected
                break;
            } else if (gamepad1.dpad_up) { //Select 0
                teamSelection = 0;
            } else if (gamepad1.dpad_down) { //Select 1
                teamSelection = 1;
            }

            //Updating Telemetry
            telemetry.update();
            telemetry.clearAll();
        }
        telemetry.clearAll(); //Clearing

        //SELECTING SIDE
        while (true) {
            //Title
            telemetry.addData("SELECTED TEAM :: ", teamSelection + "(0=Red 1=Blue)");
            telemetry.addData(">>", "Select Side");
            //Selection Output
            if (sideSelection == 0) {
                telemetry.addData("   *", "Left Side");
                telemetry.addData("    ", "Right Side");
            } else {
                telemetry.addData("    ", "Left Side");
                telemetry.addData("   *", "Right Side");
            }

            //Selector
            if (gamepad1.a) { //Select
                while (gamepad1.a) {} //Waits for the button to be unseleted
                break;
            } else if (gamepad1.dpad_up) { //Select 0
                sideSelection = 0;
            } else if (gamepad1.dpad_down) { //Select 1
                sideSelection = 1;
            }

            //Updating Telemetry
            telemetry.update();
            telemetry.clearAll();
        }
        telemetry.clearAll(); //Clearing

        //Setting Position
        if (teamSelection == 0) { //Red Team
            if (sideSelection == 0) { //Left Side
                robotPosXOffset = start00PosX;
                robotPosYOffset = start00PosY;
            } else { //Right Side
                robotPosXOffset = start01PosX;
                robotPosYOffset = start01PosY;
            }
        } else { //Blue Team
            if (sideSelection == 0) {
                robotPosXOffset = start10PosX;
                robotPosYOffset = start10PosY;
            } else { //Right Side
                robotPosXOffset = start11PosX;
                robotPosYOffset = start11PosY;
            }
        }

        //!!INSERT TESNSOR FLOW CAMERA CODE HERE


        //Display telemetry Waiting for driver to be ready
        telemetry.addData("CONFIG :: ", "Team:" + teamSelection + " Side:" + sideSelection);
        telemetry.addData("~>", "Robot Ready.  Press Play.  Or else...");
        telemetry.update();
        waitForStart();



        //>>>>>>>>>>>>>> AUTO Code runs once "Start" is activated <<<<<<<<<<<<<<<<<<<<<<<<<<

        //Running Corresponding Autonomous to the robots position
        if (teamSelection == 0) { //Red Team
            if (sideSelection == 0) { //Left Side
                //Starting Class 00
                pos00.start();
            } else { //Right Side

            }
        } else { //Blue Team
            if (sideSelection == 0) { //Left Side

            } else { //Right Side

            }
        }

        //Backend work while running
        while (running) {
            updatePosition();

        }
    }

    private void updatePosition() {
        //Insert code here
    }
}