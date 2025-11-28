@file:Suppress("MemberVisibilityCanBePrivate")

package org.firstinspires.ftc.teamcode.src.core

import com.bylazar.configurables.annotations.Configurable
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.src.utils.HardwareInit
import org.firstinspires.ftc.teamcode.src.utils.enums.MotorEnum

@Configurable
object Intake {
    enum class IntakeState {
        IN,
        OUT,
        OFF
    }

    lateinit var intake: DcMotorEx
    var currentState: IntakeState = IntakeState.OFF

    fun init() {
        intake = HardwareInit.getMotor(MotorEnum.INTAKE)!!
        intake.power = 0.0
    }

    fun changeState(newState: IntakeState) {
        currentState = if (newState == currentState) IntakeState.OFF
        else newState

        when (currentState) {
            IntakeState.IN -> setIntakePower(inPower)
            IntakeState.OUT -> setIntakePower(outPower)
            IntakeState.OFF -> setIntakePower(stopPower)
        }
    }

    fun setIntakePower(power: Double) {
        intake.power = power
    }

    var inPower: Double = 0.7
    var outPower: Double = -0.5
    var stopPower: Double = 0.0
}