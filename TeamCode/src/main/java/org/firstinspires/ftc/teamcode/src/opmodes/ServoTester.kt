package org.firstinspires.ftc.teamcode.src.opmodes

import com.bylazar.configurables.annotations.Configurable
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.ServoImplEx

@Configurable
@TeleOp(name = "ServoTester")
class ServoTester : LinearOpMode() {
    private lateinit var servo: ServoImplEx

    override fun runOpMode() {
        servo = hardwareMap.get(ServoImplEx::class.java, "servotest")

        waitForStart()
        while (opModeIsActive()) {
            servo.setPosition(servoPos)

            telemetry.addData("Servo Pos: ", servo.getPosition())
            telemetry.update()
        }
    }

    companion object {
        var servoPos: Double = 0.1
    }
}