package org.firstinspires.ftc.teamcode.Autonomous;

public class V01 implements Runnable {
    //Parent Clas
    AutonomousMain parentMain = new AutonomousMain();

    public void run() {
        parentMain.runToCoordinate(parentMain.parkPos012X, parentMain.parkPos012Y, 0);
    }
}
