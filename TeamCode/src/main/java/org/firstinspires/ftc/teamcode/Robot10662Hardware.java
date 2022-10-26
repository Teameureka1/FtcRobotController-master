/**
 * Welcome to the super awesome Hardware Configuration for team 10662's comptetion robot!
 *
 * This file will set up all of the robots motors, servos, ect, with one simple class that can
 * be called from any script for easy syncing and updates.
 *
 * The following program is structured into three secotions:
 * #IMPORTS
 * #DEFINING-OBJECTS + PUBLIC-VARIABLES
 * #MAPPING-OBJECTS + CONFIG
 */

//#$#$#$#$#$#$#$#$#>> IMPORTS <<#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$
package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

//#$#$#$#$#$#$#$#$#>> DEFINING-OBJECTS + PUBLIC-VARIABLES <<#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$
public class Robot10662Hardware {
    //Defining motors and servos for later
    public DcMotor FrontLeftDrive   = null;
    public DcMotor FrontRightDrive  = null;
    public DcMotor BackLeftDrive    = null;
    public DcMotor BackRightDrive   = null;
    public DcMotor Arm0             = null;
    public DcMotor Arm1             = null;
    public Servo   Claw0            = null;
    public Servo   Claw1            = null;
    public BNO055IMU imu         = null;

    //Defining public constant variables
    public static final double Claw0Open       = 0.7;
    public static final double Claw1Open       = 0.65;
    public static final double Claw0Close      = 0.9;
    public static final double Claw1Close      = 0.85;

    //Local opMember
    HardwareMap hwMap = null;

    //Define a constructor that allows the OpMode to pass a reference to itself.
    public Robot10662Hardware() {
    }

    //#$#$#$#$#$#$#>> MAPPING-OBJECTS + CONFIG <<#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#
    public void init(HardwareMap ahwMap) {
        hwMap = ahwMap;

        //Define and Initialize Motors (note: need to use reference to actual OpMode).
        FrontLeftDrive  = hwMap.get(DcMotor.class, "FL");
        FrontRightDrive = hwMap.get(DcMotor.class, "FR");
        BackLeftDrive   = hwMap.get(DcMotor.class, "BL");
        BackRightDrive  = hwMap.get(DcMotor.class, "BR");
        Arm0    = hwMap.get(DcMotor.class, "Arm0");
        Arm1    = hwMap.get(DcMotor.class, "Arm1");

        //Setting Motor Directions + Mode
        FrontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        FrontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FrontLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        FrontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        FrontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FrontRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        BackRightDrive.setDirection(DcMotor.Direction.REVERSE);
        BackRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BackRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        BackLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        BackLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BackLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        Arm0.setDirection((DcMotorSimple.Direction.REVERSE));

        //Define and initialize Servos.
        Claw0 = hwMap.get(Servo.class, "Claw0");
        Claw1 = hwMap.get(Servo.class, "Claw1");

        //Opening Claw at the Start to Prevent Problems
        Claw0.setPosition(Claw0Open);
        Claw1.setPosition(Claw1Open);

        //Define Sensors
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit            = BNO055IMU.AngleUnit.DEGREES;
        imu = hwMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
    }

    public void toggleMotorHold(String motor, boolean hold) {
        if (hold == true) { //Toggle hold ON
            if (motor.equals("FL")) { //Front Left Motor
                FrontLeftDrive.setTargetPosition(FrontLeftDrive.getCurrentPosition());
                FrontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                FrontLeftDrive.setPower(0);
            } else if (motor.equals("FR")) { //Front Right Motor
                FrontRightDrive.setTargetPosition(FrontRightDrive.getCurrentPosition());
                FrontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                FrontRightDrive.setPower(0);
            } else if (motor.equals("BL")) { //Back Left Motor
                BackLeftDrive.setTargetPosition(BackLeftDrive.getCurrentPosition());
                BackLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                BackLeftDrive.setPower(0);
            } else if (motor.equals("BR")) { //Back Right Motor
                BackRightDrive.setTargetPosition(BackRightDrive.getCurrentPosition());
                BackRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                BackRightDrive.setPower(0);
            } else if (motor.equals("A0")) { //Arm 0 Motor
                Arm0.setTargetPosition(Arm0.getCurrentPosition());
                Arm0.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                Arm0.setPower(0);
            } else if (motor.equals("A1")) { //Arm 1 Motor
                Arm1.setTargetPosition(Arm1.getCurrentPosition());
                Arm1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                Arm1.setPower(0);
            }
        } else { //Toggle hold OFF
            if (motor.equals("FL")) { //Front Left Motor
                FrontLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            } else if (motor.equals("FR")) { //Front Right Motor
                FrontRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            } else if (motor.equals("BL")) { //Back Left Motor
                BackLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            } else if (motor.equals("BR")) { //Back Right Motor
                BackRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            } else if (motor.equals("A0")) { //Arm 0 Motor
                Arm0.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            } else if (motor.equals("A1")) { //Arm 1 Motor
                Arm1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }
        }

    }

}
