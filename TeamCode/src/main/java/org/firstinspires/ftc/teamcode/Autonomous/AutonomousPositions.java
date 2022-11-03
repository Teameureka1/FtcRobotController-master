package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Robot10662Hardware;

public class AutonomousPositions {
    //Instantiate the Hardware class and parent
    Robot10662Hardware robot = new Robot10662Hardware();
    AutonomousMain parent = new AutonomousMain();

    //Stages
    private boolean startup = false;
    private int stage = 0;
    private boolean done = true;
    private int cones = 5;

    //Timers
    private ElapsedTime runtime = new ElapsedTime();
    private ElapsedTime gameTime = new ElapsedTime();

    public boolean position00() {
        if (!startup) {
            clawToggle(true);
            runToCoordinate(36 ,0 ,0 ,0);
            clawToggle(false);
            runToCoordinate(36, 12, 0 ,0);
            runToCoordinate(36 ,12 , -35, 1);
        }




        if (!robot.FrontLeftDrive.isBusy() && !robot.FrontRightDrive.isBusy()  && !robot.BackLeftDrive.isBusy()  & !robot.BackRightDrive.isBusy()) {
            done = true;
        }





        if (stage == 0 && done) {
            runToCoordinate(65 ,12 , -35, 1);
            done = false;
        } else if (stage == 0 && !done) {
            stage = 1;
        }

        if (stage == 1 && done) {
            clawToggle(true);
            done = false;
        } else if (stage == 1 && !done) {
            stage = 2;
        }

        if (stage == 2 && done) {
            runToCoordinate(60, 12, -35, 1);
            done = false;
        } else if (stage == 2 && !done) {
            stage = 3;
        }

        if (stage == 2 && done) {
            runToCoordinate(60, 12, 35, 1);
            done = false;
        } else if (stage == 2 && !done) {
            stage = 3;
        }

        if (stage == 3 && done) {
            runToCoordinate(36 ,12, 35, 1);
            done = false;
        } else if (stage == 3 && !done) {
            stage = 4;
        }

        if (stage == 4 && done) {
            runToCoordinate(36 ,12, 0, 8);
            done = false;
        } else if (stage == 4 && !done) {
            stage = 5;
        }

        if (stage == 5 && done) {
            runToCoordinate(24 ,7, 0, 8);
            done = false;
        } else if (stage == 5 && !done) {
            stage = 6;
        }

        if (stage == 6 && done) {
            runToCoordinate(24 ,7, 0, 7);
            done = false;
        } else if (stage == 6 && !done) {
            stage = 7;
        }

        if (stage == 7 && done) {
            clawToggle(false);
            cones = cones-1;
            done = false;
        } else if (stage == 7 && !done) {
            stage = 8;
        }

        if (stage == 8 && done) {
            runToCoordinate(36 ,12, 0, 1);
            done = false;
        } else if (stage == 8 && !done) {
            stage = 9;
        }

        if (stage == 9 && done) {
            runToCoordinate(36 ,12, -35, 1);
            done = false;
        } else if (stage == 9 && !done) {
            stage = 0;
        }






        if (gameTime.seconds() >= 25) {
            //Parking and ending
            return true;
        } else {
            return false;
        }
    }


    public void runToCoordinate(double X, double Y, double Z, int armPos) {
        parent.updatePosition();

        double targetX = (X - parent.robotPosX) * robot.ticksPerInch;
        double targetY = (Y - parent.robotPosY) * robot.ticksPerInch;
        double targetZ = (Z - parent.robotPosZ) * robot.ticksPerInch;

        double flPos = targetY + targetX + targetZ;
        double frPos = targetY - targetX - targetZ;
        double blPos = targetY - targetX + targetZ;
        double brPos = targetY + targetX - targetZ;

        robot.FrontLeftDrive.setTargetPosition((int)flPos);
        robot.FrontRightDrive.setTargetPosition((int)frPos);
        robot.BackLeftDrive.setTargetPosition((int)blPos);
        robot.BackRightDrive.setTargetPosition((int)brPos);

        if (armPos == 0) {
            robot.Arm0.setTargetPosition(robot.armPos0);
            robot.Arm1.setTargetPosition(robot.armPos0);
        } else if (armPos == 1) {
            if (cones == 5) {
                robot.Arm0.setTargetPosition(robot.armPos5);
                robot.Arm1.setTargetPosition(robot.armPos5);
            } else if (cones == 4) {
                robot.Arm0.setTargetPosition(robot.armPos4);
                robot.Arm1.setTargetPosition(robot.armPos4);
            } else if (cones == 3) {
                robot.Arm0.setTargetPosition(robot.armPos3);
                robot.Arm1.setTargetPosition(robot.armPos3);
            } else if (cones == 2) {
                robot.Arm0.setTargetPosition(robot.armPos2);
                robot.Arm1.setTargetPosition(robot.armPos2);
            } else if (cones == 1) {
                robot.Arm0.setTargetPosition(robot.armPos1);
                robot.Arm1.setTargetPosition(robot.armPos1);
            } else {
                //Bro Stop
            }

        } else if (armPos == 2) {
            robot.Arm0.setTargetPosition(robot.armPos6);
            robot.Arm1.setTargetPosition(robot.armPos6);
        } else if (armPos == 3) {
            robot.Arm0.setTargetPosition(robot.armPos7);
            robot.Arm1.setTargetPosition(robot.armPos7);
        } else if (armPos == 4) {
            robot.Arm0.setTargetPosition(robot.armPos8);
            robot.Arm1.setTargetPosition(robot.armPos8);
        }

        robot.FrontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.FrontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.BackLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.BackRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.Arm0.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.Arm1.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        if (targetX + targetY + targetZ > 2000) {
            robot.FrontLeftDrive.setPower(0.6);
            robot.FrontRightDrive.setPower(0.6);
            robot.BackLeftDrive.setPower(0.6);
            robot.BackRightDrive.setPower(0.6);
            robot.Arm0.setPower(0.6);
            robot.Arm1.setPower(0.6);
        } else {
            robot.FrontLeftDrive.setPower(0.4);
            robot.FrontRightDrive.setPower(0.4);
            robot.BackLeftDrive.setPower(0.4);
            robot.BackRightDrive.setPower(0.4);
            robot.Arm0.setPower(0.6);
            robot.Arm1.setPower(0.6);
        }

        /*
        while (robot.FrontLeftDrive.isBusy() && robot.FrontRightDrive.isBusy()  && robot.BackLeftDrive.isBusy()  & robot.BackRightDrive.isBusy()  ) {
            parent.updatePosition();
        }

        robot.FrontLeftDrive.setTargetPosition(robot.FrontLeftDrive.getCurrentPosition());
        robot.FrontRightDrive.setTargetPosition(robot.FrontLeftDrive.getCurrentPosition());
        robot.BackLeftDrive.setTargetPosition(robot.BackLeftDrive.getCurrentPosition());
        robot.BackRightDrive.setTargetPosition(robot.BackRightDrive.getCurrentPosition());

        robot.FrontLeftDrive.setPower(0.5);
        robot.FrontRightDrive.setPower(0.5);
        robot.BackLeftDrive.setPower(0.5);
        robot.BackRightDrive.setPower(0.5);
*/
    }

    public void clawToggle(boolean toggle) {
        if (toggle) {
            robot.Claw0.setPosition(robot.Claw0Close);
            robot.Claw1.setPosition(robot.Claw1Close);
        } else {
            robot.Claw0.setPosition(robot.Claw0Open);
            robot.Claw1.setPosition(robot.Claw1Open);
        }
    }

}
