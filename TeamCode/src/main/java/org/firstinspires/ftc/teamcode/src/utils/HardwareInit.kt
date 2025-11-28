@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package org.firstinspires.ftc.teamcode.src.utils

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver
import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.ServoImplEx
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.src.utils.enums.MotorEnum
import org.firstinspires.ftc.teamcode.src.utils.enums.ServoEnum

object HardwareInit {
    var motorMap = HashMap<MotorEnum, DcMotorEx>()
    var servoMap = HashMap<ServoEnum, ServoImplEx>()
    lateinit var odo: GoBildaPinpointDriver

    lateinit var hmap: HardwareMap

    fun setHardwareMap(hardwareMap: HardwareMap) { hmap = hardwareMap }

    private fun registerMotors(motor: MotorEnum): DcMotorEx  {

        val name = motor.getName()
        val runMode = motor.getRunMode()
        val ZPB = motor.getZPB()
        val direction = motor.getDir()

        val newMotor = hmap.get(DcMotorEx::class.java, name)
        newMotor.mode = runMode
        newMotor.zeroPowerBehavior = ZPB
        newMotor.direction = direction

        motorMap[motor] = newMotor

        return newMotor
    }

    private fun registerServos(servo: ServoEnum): ServoImplEx {

        val name = servo.getName()
        val direction = servo.getDir()

        val newServo = hmap.get(ServoImplEx::class.java, name)
        newServo.direction = direction

        servoMap[servo] = newServo

        servo.getInitPos().let { pos ->
            pos ?: return@let
            newServo.position = pos
        }

        return newServo
    }

    private fun registerOdoComp() {
        odo = hmap.get(GoBildaPinpointDriver::class.java, "odo")

        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
        odo.setOffsets(1.0, 1.0, DistanceUnit.MM)

        odo.setEncoderDirections(
            GoBildaPinpointDriver.EncoderDirection.FORWARD,
            GoBildaPinpointDriver.EncoderDirection.FORWARD
        )

        odo.resetPosAndIMU()
    }

    fun registerServo(servo: ServoEnum): ServoImplEx {
        val name = servo.getName()
        val direction = servo.getDir()

        val newServo = hmap.get(ServoImplEx::class.java, name)
        newServo.direction = direction

        servoMap[servo] = newServo

        servo.getInitPos().let { pos ->
            pos ?: return@let
            newServo.position = pos
        }

        return newServo
    }

    fun registerMotor(motor: MotorEnum): DcMotorEx {
        val name = motor.getName()
        val runMode = motor.getRunMode()
        val ZPB = motor.getZPB()
        val direction = motor.getDir()

        val newMotor = hmap.get(DcMotorEx::class.java, name)
        newMotor.mode = runMode
        newMotor.zeroPowerBehavior = ZPB
        newMotor.direction = direction

        motorMap[motor] = newMotor

        return newMotor
    }

    fun setMotorMode(
        name: MotorEnum,
        runMode: RunMode? = null,
        direction: DcMotorSimple.Direction? = null,
        zeroPowerBehavior: ZeroPowerBehavior? = null
    ) {

        val motor = getMotor(name) ?: return

        runMode.let { motor.mode = runMode }
        direction.let { motor.direction = direction }
        zeroPowerBehavior.let { motor.zeroPowerBehavior = zeroPowerBehavior }

        motorMap[name] = motor
    }

    fun setServoMode(
        name: ServoEnum,
        direction: Servo.Direction? = null
    ) {
        val servo = getServo(name) ?: return

        direction.let {servo.direction = direction}

        servoMap[name] = servo
    }

    fun getMotor(name: MotorEnum): DcMotorEx? {
        return motorMap[name]
    }

    fun getServo(name: ServoEnum): ServoImplEx? {
        return servoMap[name]
    }

    fun registerAll() {
        for(entry in MotorEnum.entries) {
            registerMotors(entry)
        }

        for(entry in ServoEnum.entries) {
            registerServos(entry)
        }

        registerOdoComp()
    }
}