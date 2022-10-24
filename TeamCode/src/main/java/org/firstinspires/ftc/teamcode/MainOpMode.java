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
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

//#$#$#$#$#$#$#$#$#$#$#$#$#$> MAIN <#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#
@TeleOp(name="Main :: Op Mode", group="Robot")
//@Disabled
public class MainOpMode extends OpMode{
    private ElapsedTime runtime = new ElapsedTime();
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

        //Doubles speed when right bumper 1 is pressed
        if (gamepad1.right_bumper) { //Dividing by two to slow it down
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
        robot.FrontLeftDrive.setPower(leftFrontPower);
        robot.FrontRightDrive.setPower(rightFrontPower);
        robot.BackLeftDrive.setPower(leftBackPower);
        robot.BackRightDrive.setPower(rightBackPower);

        //===========GAMEPAD2=====================
        //Defining variables
        double armPower;

        //Assigning power to arm
        armPower = -gamepad2.left_stick_y;

        if(gamepad2.right_bumper) {  //Doubles speed when pressed
            robot.Arm0.setPower(armPower);
            robot.Arm1.setPower(armPower);
        } else {
            robot.Arm0.setPower(armPower / 2);
            robot.Arm1.setPower(armPower / 2);
        }

        //Open and close claw
        if (gamepad2.x) {
            robot.Claw0.setPosition(robot.Claw0Open);
            robot.Claw1.setPosition(robot.Claw1Open);
        } else if (gamepad2.a) {
            robot.Claw0.setPosition(robot.Claw0Close);
            robot.Claw1.setPosition(robot.Claw1Close);
        }

        //CHESTER PLEASE READ we commented out the telementry because we needed to test the arm without errors
        //P.S - If we didn't already tell you, the claw does work.

        //region ===========TELEMETRY=====================
        //Motors
        telemetry.addData("FL Power",  "%.2f :%7d", robot.FrontLeftDrive.getPower(), robot.FrontLeftDrive.getCurrentPosition());
        telemetry.addData("BL Power",  "%.2f :%7d", robot.BackLeftDrive.getPower(), robot.BackLeftDrive.getCurrentPosition());
        telemetry.addData("FR Power",  "%.2f :%7d", robot.FrontRightDrive.getPower(), robot.FrontRightDrive.getCurrentPosition());
        telemetry.addData("BR Power",  "%.2f :%7d", robot.BackRightDrive.getPower(), robot.BackRightDrive.getCurrentPosition());
        //Claw
        telemetry.addData("LeftClaw Position", "%.2f", robot.Claw0.getPosition());
        telemetry.addData("RightClaw Position", "%.2f", robot.Claw1.getPosition());


        telemetry.addData("Arm1", "%.2f", robot.Arm0.getPower());
        telemetry.addData("Arm2", "%.2f", robot.Arm1.getPower());

        //endregion

    }

    @Override //>>>>>>>>>>>>>> STOP <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    public void stop() { //Runs ONCE when driver hits STOP <<
    }
}