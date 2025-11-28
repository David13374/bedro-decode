package org.firstinspires.ftc.teamcode.src.opmodes;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Configurable
@TeleOp(name = "MotorTest")
public class MotorTest extends LinearOpMode {

    DcMotorEx leftF, leftB, rightF, rightB;
    public static double leftFp, leftBp, rightFp, rightBp;
    @Override
    public void runOpMode() {
        leftF = hardwareMap.get(DcMotorEx.class, "leftFront");
        leftB = hardwareMap.get(DcMotorEx.class, "leftBack");
        rightF = hardwareMap.get(DcMotorEx.class, "rightFront");
        rightB = hardwareMap.get(DcMotorEx.class, "rightBack");

        waitForStart();

        leftBp = leftFp = rightBp = rightFp = 0;

        while(opModeIsActive()) {
            leftB.setPower(leftBp);
            leftF.setPower(leftFp);
            rightB.setPower(rightBp);
            rightF.setPower(rightFp);
        }
    }
}
