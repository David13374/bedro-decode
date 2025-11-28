package org.firstinspires.ftc.teamcode.src.utils.enums

import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.src.core.Outtake

enum class ServoEnum(
    private val hardwareName: String,
    private val direction: Servo.Direction = Servo.Direction.FORWARD,
    private val initPos: Double? = null
) {
    OUTTAKE_SERVO("outtakeServo", initPos = Outtake.WaitingServoPos),
    SORT_RIGHT_SERVO("sortServoRight", initPos = Outtake.sortServoRightOpen),
    SORT_LEFT_SERVO("sortServoLeft", initPos = Outtake.sortServoLeftOpen);

    fun getName() = hardwareName
    fun getDir() = direction
    fun getInitPos() = initPos
}