/* Copyright (c) 2022 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * This file works in conjunction with the External Hardware Class sample called: ConceptExternalHardwareClass.java
 * Please read the explanations in that Sample about how to use this class definition.
 *
 * This file defines a Java Class that performs all the setup and configuration for a sample robot's hardware (motors and sensors).
 *
 * This one file/class can be used by ALL of your OpModes without having to cut & paste the code each time.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with *exactly the same name*.

 * an OpMode.

 *
 */

public class Robot10662Hardware {

    // Define Motor and Servo objects  (Make them private so they can't be accessed externally)
    public DcMotor FrontLeftDrive   = null;
    public DcMotor FrontRightDrive  = null;
    public DcMotor BackLeftDrive    = null;
    public DcMotor BackRightDrive   = null;
    public DcMotor Arm0             = null;
    public DcMotor Arm1             = null;
    public Servo   Claw0            = null;
    public Servo   Claw1            = null;

    // Define Drive constants.  Make them public so they CAN be used by the calling OpMode
    public static final double Claw0Open       = 0.25 ;
    public static final double Claw1Open       = 0.7 ;
    public static final double Claw0Close      = 0.05 ;
    public static final double Claw1Close      = 0.9 ;

    /* local OpMode members */
    HardwareMap hwMap = null;

    // Define a constructor that allows the OpMode to pass a reference to itself.
    public Robot10662Hardware() {
    }


    /**
     * Initialize all the robot's hardware.
     * This method must be called ONCE when the OpMode is initialized.
     *
     * All of the hardware devices are accessed via the hardware map, and initialized.
     */
    public void init(HardwareMap ahwMap) {
        hwMap = ahwMap;

        // Define and Initialize Motors (note: need to use reference to actual OpMode).
        FrontLeftDrive  = hwMap.get(DcMotor.class, "FL");
        FrontRightDrive = hwMap.get(DcMotor.class, "FR");
        BackLeftDrive   = hwMap.get(DcMotor.class, "BL");
        BackRightDrive  = hwMap.get(DcMotor.class, "BR");
        Arm0    = hwMap.get(DcMotor.class, "Arm0");
        Arm1    = hwMap.get(DcMotor.class, "Arm1");

        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // Pushing the left stick forward MUST make robot go forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
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

        // Define and initialize ALL installed servos.
        Claw0 = hwMap.get(Servo.class, "Claw0");
        Claw1 = hwMap.get(Servo.class, "Claw1");

        Claw0.setPosition(Claw0Open);
        Claw1.setPosition(Claw1Open);
    }
}
