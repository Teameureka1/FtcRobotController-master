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
import com.qualcomm.robotcore.util.ElapsedTime;

//#$#$#$#$#$#$#$#$#$#$#$#$#$> MAIN <#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#
@Autonomous(name="Main :: Auto Mode Test", group="Robot")
//@Disabled
public class TestAutoMode extends LinearOpMode {
    //Runtime
    private ElapsedTime runtime = new ElapsedTime();

    //Instantiate the Hardware class
    Robot10662Hardware robot = new Robot10662Hardware();

    @Override
        public void runOpMode() { //Runs once started
        //>>>>>>>>>>>>>> INT <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        //Use 'init' methods from Hardware class to Map hardware to match robot's config
            robot.init(hardwareMap);

            //Display telemetry Waiting for driver to be ready
            telemetry.addData("~>", "Robot Ready.  Press Play.  Or else...");
            telemetry.update();
            waitForStart();

        //>>>>>>>>>>>>>> AUTO Code  runs once "Start" is activated <<<<<<<<<<<<<<<<<<<<<<<<<<





    }
    //#$#$#$#$#$#$#$#$#$#$#$#$#$> MOVE <#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$
    public void moveSeconds(double seconds, double axial, double lateral, double yaw) {
        //Setting motor speeds with formula
        robot.FrontLeftDrive.setPower(axial + lateral + yaw);
        robot.BackLeftDrive.setPower(axial - lateral + yaw);
        robot.FrontRightDrive.setPower(axial - lateral - yaw);
        robot.BackRightDrive.setPower(axial + lateral - yaw);
        //Repeatedly will run for however many seconds
        runtime.reset();

        while (opModeIsActive() && (runtime.seconds() < seconds)) {
            telemetry.addData("move", "%4.1f S Elapsed", runtime.seconds());
            telemetry.update();
        }
    }
    public void testMove(double axial, double lateral, double yaw) {
    }
}