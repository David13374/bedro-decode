package org.firstinspires.ftc.teamcode.src.utils.enums

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior
import com.qualcomm.robotcore.hardware.DcMotorSimple

enum class MotorEnum(
    private val hardwareName: String,
    private val runMode: DcMotor.RunMode = DcMotor.RunMode.RUN_WITHOUT_ENCODER,
    private val direction: DcMotorSimple.Direction = DcMotorSimple.Direction.FORWARD,
    private val zeroPowerBehavior: ZeroPowerBehavior = ZeroPowerBehavior.BRAKE
) {
    LEFT_FRONT("leftFront", direction = DcMotorSimple.Direction.REVERSE),
    RIGHT_FRONT("rightFront"),
    LEFT_BACK("leftBack"),
    RIGHT_BACK("rightBack", direction = DcMotorSimple.Direction.REVERSE),
    INTAKE("intake"),
    FLY_WHEEL("outtakeMotor");

    fun getName() = hardwareName
    fun getRunMode() = runMode
    fun getDir() = direction
    fun getZPB() = zeroPowerBehavior
}