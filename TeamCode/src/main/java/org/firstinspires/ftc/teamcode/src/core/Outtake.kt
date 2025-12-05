@file:Suppress("SpellCheckingInspection", "MemberVisibilityCanBePrivate")

package org.firstinspires.ftc.teamcode.src.core

import com.bylazar.configurables.annotations.Configurable
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.ServoImplEx
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D
import org.firstinspires.ftc.teamcode.src.utils.HardwareInit
import org.firstinspires.ftc.teamcode.src.utils.enums.MotorEnum
import org.firstinspires.ftc.teamcode.src.utils.enums.ServoEnum

@Configurable
object Outtake {
    lateinit var outtakeServo: ServoImplEx
    lateinit var outtakeMotor: DcMotorEx
    lateinit var sortServoLeft: ServoImplEx
    lateinit var sortServoRight: ServoImplEx

    var timer: ElapsedTime = ElapsedTime(ElapsedTime.Resolution.MILLISECONDS)

    enum class OuttakeState {
        READY,
        SHOOT,
        WAITING
    }

    enum class SortServoState {
        CLOSED,
        OPENED
    }

    fun init() {
        outtakeServo = HardwareInit.getServo(ServoEnum.OUTTAKE_SERVO)!!
        outtakeMotor = HardwareInit.getMotor(MotorEnum.FLY_WHEEL)!!
        outtakeServo.position = WaitingServoPos

        sortServoLeft = HardwareInit.getServo(ServoEnum.SORT_LEFT_SERVO)!!
        sortServoRight = HardwareInit.getServo(ServoEnum.SORT_RIGHT_SERVO)!!

        outtakeMotor.direction = DcMotorSimple.Direction.REVERSE
        currentState = OuttakeState.WAITING
        timer.reset()

    }

    var currentState: OuttakeState = OuttakeState.WAITING
    fun changeState(nextState: OuttakeState? = null) {
        if(nextState != null) {
            currentState = nextState
            timer.reset()
        }
        /*Intake.setIntakePower(if(
            currentLeftServoState == SortServoState.OPENED
            || currentRightServoState == SortServoState.OPENED)
            Intake.outPower
        else Intake.stopPower
        )*/
        when (currentState) {
            OuttakeState.READY -> {
                if(timer.time() >= readyTime) {
                    setServoPosition(ReadyServoPos)
                    setMotorPower(0.0)
                }

                if(nextState != null) {
                    setLeftServoPos(sortServoLeftClose)
                    setRightServoPos(sortServoRightClose)
                    currentRightServoState = SortServoState.OPENED
                    currentLeftServoState = SortServoState.OPENED
                }
            }

            OuttakeState.WAITING -> {
                outtakeServo.position = WaitingServoPos
                setMotorPower(0.0)
            }

            OuttakeState.SHOOT -> {
                if (timer.milliseconds() >= motorAccelTime && timer.time() <= motorStopTime + motorAccelTime)
                    outtakeServo.position = ShootServoPos
                if(timer.time() <= motorStopTime + motorAccelTime)
                    setMotorPower(CloseMotorPower)
                else {
                    setMotorPower(0.0)
                    setServoPosition(WaitingServoPos)
                }
            }
        }
    }

    fun update() {
        changeState()
    }

    fun setServoPosition(pos: Double) {
        outtakeServo.position = pos
    }

    fun updateLeftServo() {
        when(currentLeftServoState) {
            SortServoState.CLOSED -> {
                setLeftServoPos(sortServoLeftClose)
                Intake.changeState(Intake.IntakeState.OFF, true)
                currentLeftServoState = SortServoState.OPENED
            }
            SortServoState.OPENED -> {
                setLeftServoPos(sortServoLeftOpen)
                Intake.changeState(Intake.IntakeState.IN, true)
                currentLeftServoState = SortServoState.CLOSED
            }
        }
    }

    fun updateRightServo() {
        when(currentRightServoState) {
            SortServoState.CLOSED -> {
                setRightServoPos(sortServoRightClose)
                Intake.changeState(Intake.IntakeState.OFF, true)
                currentRightServoState = SortServoState.OPENED
            }
            SortServoState.OPENED -> {
                setRightServoPos(sortServoRightOpen)
                Intake.changeState(Intake.IntakeState.IN, true)
                currentRightServoState = SortServoState.CLOSED
            }
        }
    }

    fun setLeftServoPos(pos: Double) {
        sortServoLeft.position = pos
    }

    fun setRightServoPos(pos: Double) {
        sortServoRight.position = pos
    }

    fun isClose(currentPos: Pose2D): Boolean {
        return currentPos.getY(DistanceUnit.MM) > delimPos.getY(DistanceUnit.MM)
    }

    fun setMotorPower(power: Double) {
        outtakeMotor.power = power
    }

    var ReadyServoPos: Double = 0.4
    var ShootServoPos: Double = 0.6
    var WaitingServoPos: Double = 0.0
    var CloseMotorPower: Double = 1.0
    var FarMotorPower: Double = 1.0

    var delimX: Double = 300.0
    var delimY: Double = 300.0

    var delimPos: Pose2D = Pose2D(DistanceUnit.MM, delimX, delimY, AngleUnit.RADIANS, 0.0)

    @JvmField var motorAccelTime: Double = 800.0

    @JvmField var sortServoLeftOpen: Double = 0.13

    @JvmField var sortServoLeftClose: Double = 0.35

    @JvmField var sortServoRightOpen: Double = 0.95
    @JvmField var sortServoRightClose: Double = 0.72

    @JvmField var readyTime: Double = 300.0
    @JvmField var motorStopTime: Double = 600.0

    private var currentLeftServoState: SortServoState = SortServoState.CLOSED
    private var currentRightServoState: SortServoState = SortServoState.CLOSED
}