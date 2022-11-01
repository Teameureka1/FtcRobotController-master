package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Robot10662Hardware;

//#$#$#$#$#$#$#$#$#$#$#$#$#$> MAIN <#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#
@Autonomous(name="Main :: Autonomous", group="Robot")
//@Disabled
public class AutonomousSelector extends LinearOpMode {

    //Instantiate the Hardware class
    Robot10662Hardware robot = new Robot10662Hardware();

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



        //Display telemetry Waiting for driver to be ready
        telemetry.addData("~>", "Robot Ready.  Press Play.  Or else...");
        telemetry.update();
        waitForStart();

        //>>>>>>>>>>>>>> AUTO Code  runs once "Start" is activated <<<<<<<<<<<<<<<<<<<<<<<<<<




    }
}