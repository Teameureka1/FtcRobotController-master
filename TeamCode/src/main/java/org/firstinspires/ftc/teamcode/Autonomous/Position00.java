/* Part of the autonomous main code
   Please don't break.
 */
package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.util.ElapsedTime;

//#$#$#$#$#$#$#$#$#$#$#$#$#$> MAIN <#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$
public class Position00 implements Runnable {
    //Referencing back to the orignal class
    AutonomousMain parentClass = new AutonomousMain();

    //Timer
    private ElapsedTime gameClock = new ElapsedTime();
    private ElapsedTime runtime = new ElapsedTime();

    public void run() {
        //Resting gameTimer
        gameClock.reset();

        //Loop
        while (parentClass.running) {

            //Park
            if (gameClock.seconds() >= 25) {
                park();
                parentClass.running = false;
            }
        }

    }

    public void park() {
        //Insert partking code here
    }
}
