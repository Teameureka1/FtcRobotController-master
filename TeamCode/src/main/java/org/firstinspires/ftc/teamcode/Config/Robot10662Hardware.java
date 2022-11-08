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
package org.firstinspires.ftc.teamcode.Config;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

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
    public DigitalChannel armTouch;
    public BNO055IMU imu         = null;


    //Imu config
    Orientation angles;
    Acceleration gravity;

    //Other
    public final double pi = 3.14159265358979323846;

    //Motor Related
    public final double ticksPerInch = 535 / (pi*4);
    public final int[] armPositions = {0,2000,3200,4400};

    //Servo Related
    public final double Claw0Open       = 0.6;
    public final double Claw1Open       = 0.7;
    public final double Claw0Close      = 0.8;
    public final double Claw1Close      = 0.9;

    //Autonomous Related
    //  Positions
    public final double startPosX = 40;
    public final double startPosY = 65;

    public final double parkingPos1BaseX = 12;
    public final double parkingPos2BaseX = 36;
    public final double parkingPos3BaseX = 60;


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
        //FrontLeft
        FrontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        FrontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FrontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //FrontRight
        FrontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        FrontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FrontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //BackRight
        BackRightDrive.setDirection(DcMotor.Direction.REVERSE);
        BackRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BackRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //BackLeft
        BackLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        BackLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BackLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //ArmMotors
        Arm0.setDirection((DcMotorSimple.Direction.FORWARD));
        Arm0.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Arm1.setDirection((DcMotorSimple.Direction.FORWARD));
        Arm1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //Define and initialize Servos.
        Claw0 = hwMap.get(Servo.class, "Claw0");
        Claw1 = hwMap.get(Servo.class, "Claw1");

        //Opening Claw at the Start to Prevent Problems
        Claw0.setPosition(Claw0Open);
        Claw1.setPosition(Claw1Open);

        //Define Sensors
        //Imu
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit            = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit            = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile  = "BNO055IMUCalibration.json";
        parameters.loggingEnabled       = true;
        parameters.loggingTag           = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu = hwMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);

        //Touch Sensor
        armTouch = hwMap.get(DigitalChannel.class, "ArmTouch");
        //Setting Touch Sensor Mode
        armTouch.setMode(DigitalChannel.Mode.INPUT);

        //Lowering arm until it is bottomed out and then setting the position.
        Arm0.setMode(DcMotor.RunMode.RUN_USING_ENCODER); //Temporarily sets mode
        Arm1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        while (armTouch.getState() == true) {//Lowering
            Arm0.setPower(-0.4);
            Arm1.setPower(-0.4);
        } //Stopping the motors
        //Setting the position.
        Arm0.setPower(0);
        Arm0.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Arm1.setPower(0);
        Arm1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

    }
}
