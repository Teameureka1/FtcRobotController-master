//#$#$#$#$#$#$#$#$#>> IMPORTS <<#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$
package org.firstinspires.ftc.teamcode.Config;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

//#$#$#$#$#$#$#$#$#>> DEFINING-OBJECTS + PUBLIC-VARIABLES <<#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$
public class Robot10662Hardware {
    //Some stuff to make cool.
    public final String robotName = "Stevo V2";
    public final String team = "#10662 Lazer Sharks In Space"; //Intentionally "mis-spelt" because that is our team name.

    //Defining motors and servos for later
    public DcMotor FrontLeftDrive   = null;
    public DcMotor FrontRightDrive  = null;
    public DcMotor BackLeftDrive    = null;
    public DcMotor BackRightDrive   = null;
    public DcMotor Arm              = null;
    public BNO055IMU imu         = null;


    //Imu config
    Orientation angles;
    Acceleration gravity;
    private double imuAngleOffset = 0;

    //Motor Relate
    public final double ticksPerInch = 535 / (Math.PI*4);
    public final double ticksPerCM = 535 / (Math.PI* 10.16);
    public final int coneStackBase = 150;

    //Local opMember
    HardwareMap hwMap = null;

    //Define a constructor that allows the OpMode to pass a reference to itself.
    public Robot10662Hardware() {
    }

    //#$#$#$#$#$#$#>> MAPPING-OBJECTS + CONFIG <<#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#
    public void init(HardwareMap ahwMap) {
        hwMap = ahwMap; //Importing current opmode's hardware

        //Define and Initialize Motors (note: need to use reference to actual OpMode).
        FrontLeftDrive  = hwMap.get(DcMotor.class, "FL");
        FrontRightDrive = hwMap.get(DcMotor.class, "FR");
        BackLeftDrive   = hwMap.get(DcMotor.class, "BL");
        BackRightDrive  = hwMap.get(DcMotor.class, "BR");
        Arm             = hwMap.get(DcMotor.class, "Arm");

        //Setting Motor Directions + Mode + Resting Encoders if necessary
        //FrontLeft
        FrontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        FrontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FrontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //FrontRight
        FrontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        FrontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FrontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //BackRight
        BackRightDrive.setDirection(DcMotor.Direction.FORWARD);
        BackRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BackRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //BackLeft
        BackLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        BackLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BackLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //Arm
        Arm.setDirection(DcMotor.Direction.FORWARD);
        Arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); //TODO: Replace with better function
        Arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

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
    }

    public double getAngle() {
        return (imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle) + imuAngleOffset;
    }

    public void setAngleZero() {
        imuAngleOffset = getAngle();
    }
}
