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

        int teamSelection = 0;
        int sideSelection = 0;





        //Display telemetry Waiting for driver to be ready
        telemetry.addData("~>", "Robot Ready.  Press Play.  Or else...");
        telemetry.update();
        waitForStart();

        //>>>>>>>>>>>>>> AUTO Code  runs once "Start" is activated <<<<<<<<<<<<<<<<<<<<<<<<<<




    }
}