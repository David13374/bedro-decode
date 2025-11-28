@file:Suppress("UNUSED", "MemberVisibilityCanBePrivate", "PropertyName", "LocalVariableName",
    "SpellCheckingInspection", "FunctionName"
)

package org.firstinspires.ftc.teamcode.src.core

import com.arcrobotics.ftclib.gamepad.GamepadKeys
import com.bylazar.configurables.annotations.Configurable
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D
import org.firstinspires.ftc.teamcode.src.utils.HardwareInit
import org.firstinspires.ftc.teamcode.src.utils.InitConfig
import org.firstinspires.ftc.teamcode.src.utils.enums.MotorEnum
import org.firstinspires.ftc.teamcode.src.utils.enums.Side
import org.firstinspires.ftc.teamcode.src.utils.enums.Teams
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

@Configurable
object Drive {
    var driverCentric: Boolean = true
    var x: Double = 0.0
    var y: Double = 0.0
    var turn: Double = 0.0
    var robotDegree: Double = 0.0
    var gamepadDegrees: Double = 0.0
    var theta: Double = 0.0
    var sin: Double = 0.0
    var cos: Double = 0.0
    var max: Double = 0.0
    var power: Double = 0.0
    var front_left_motor: Double = 0.0
    var front_right_motor: Double = 0.0
    var back_left_motor: Double = 0.0
    var back_right_motor: Double = 0.0
    var gyro_offset: Double = 0.0
    var coef: Double = 1.0

    var headingButton: GamepadKeys.Button = GamepadKeys.Button.DPAD_DOWN

    fun init() {
        HeadingPID.init()

        leftFront = HardwareInit.getMotor(MotorEnum.LEFT_FRONT)!!
        rightFront = HardwareInit.getMotor(MotorEnum.RIGHT_FRONT)!!
        leftBack = HardwareInit.getMotor(MotorEnum.LEFT_BACK)!!
        rightBack = HardwareInit.getMotor(MotorEnum.RIGHT_BACK)!!
        odo = HardwareInit.odo
    }

    fun start() {
        if (InitConfig.currentTeam == Teams.BLUE) {
            if (InitConfig.currentSide == Side.SPECTATOR) {
                odo.position = Pose2D(DistanceUnit.MM, 55.88059701492537, 7.439724454649827, AngleUnit.DEGREES, -90.0)
            } else {
                odo.position = Pose2D(DistanceUnit.MM, 48.11021814006889, 135.40298507462688, AngleUnit.DEGREES, 90.0)
            }

            HeadingPID.target = -40.0
        } else {
            if (InitConfig.currentSide == Side.SPECTATOR) {
                odo.position = Pose2D(DistanceUnit.MM, 88.94603903559127, 7.605051664753153, AngleUnit.DEGREES, 90.0)
            } else {
                odo.position = Pose2D(DistanceUnit.MM, 95.88978185993112, 135.40298507462688, AngleUnit.DEGREES, -90.0)
            }

            HeadingPID.target = 40.0
        }
    }

    fun getHeading(unit: AngleUnit?): Double {
        return odo.position.getHeading(unit)
    }

    fun resetOdoIMU() {
        odo.recalibrateIMU()
    }

    val position: Pose2D
        get() = odo.position

    fun Move(gamepad: Gamepad) {

        coef = if (gamepad.dpad_up) {
            0.65
        } else 1.0

        if (isPidEnabled) {
            //val radian = atan2(odo.getPosX(DistanceUnit.MM), odo.getPosY(DistanceUnit.MM))
            //val degrees = radian * 180 / PI
            //HeadingPID.target = degrees
            HeadingPID.update(getHeading(AngleUnit.DEGREES))
        }

        x = gamepad.left_stick_x.toDouble()
        y = -gamepad.left_stick_y.toDouble()
        turn = (max(
            -gamepad.left_trigger.toDouble(),
            gamepad.right_trigger.toDouble()
        ) + min(
            -gamepad.left_trigger.toDouble(),
            gamepad.right_trigger.toDouble()
        ))
        if (turn < -1) turn = -1.0

        robotDegree = getHeading(AngleUnit.RADIANS) - gyro_offset
        gamepadDegrees = atan2(y, x)
        power = hypot(x, y)
        if (power > 1) power = 1.0

        theta = if (driverCentric) {
            gamepadDegrees - robotDegree
        } else gamepadDegrees

        sin = sin(theta - Math.PI / 4)
        cos = cos(theta - Math.PI / 4)
        max = max(abs(sin), abs(cos))


        front_left_motor = power * cos / max + turn
        front_right_motor = power * sin / max - turn
        back_left_motor = power * sin / max + turn
        back_right_motor = power * cos / max - turn

        if ((power + abs(turn)) > 1 && turn > 0) {
            front_left_motor /= power + turn
            front_right_motor /= power + turn
            back_left_motor /= power + turn
            back_right_motor /= power + turn
        }

        if ((power + abs(turn)) > 1 && turn < 0) {
            front_left_motor /= power - turn
            front_right_motor /= power - turn
            back_left_motor /= power - turn
            back_right_motor /= power - turn
        }
        if (gamepad.left_stick_button) {
            reset_gyro()
        }

        odo.update()
        setMotorPowers(
            coef * front_left_motor,
            coef * back_left_motor,
            coef * front_right_motor,
            coef * back_right_motor
        )
    }

    fun reset_gyro() {
        gyro_offset = getHeading(AngleUnit.RADIANS)
    }

    fun setRobotCentric() {
        driverCentric = false
    }

    fun setHeadingTarget(target: Double) {
        HeadingPID.target = target
    }

    fun setMotorPowers(
        frontLeftPower: Double,
        backLeftPower: Double,
        frontRightPower: Double,
        backRightPower: Double
    ) {
        leftFront.power = frontLeftPower
        leftBack.power = backLeftPower
        rightFront.power = frontRightPower
        rightBack.power = backRightPower
    }

    var isPidEnabled: Boolean = false
    lateinit var odo: GoBildaPinpointDriver
    lateinit var leftFront: DcMotorEx
    lateinit var leftBack: DcMotorEx
    lateinit var rightFront: DcMotorEx
    lateinit var rightBack: DcMotorEx

    val team: Teams
        get() = InitConfig.currentTeam
}