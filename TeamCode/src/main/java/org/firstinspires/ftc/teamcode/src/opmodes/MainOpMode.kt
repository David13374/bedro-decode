@file:Suppress("UNUSED", "MemberVisibilityCanBePrivate")

package org.firstinspires.ftc.teamcode.src.opmodes

import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.gamepad.GamepadKeys
import com.bylazar.configurables.annotations.Configurable
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.teamcode.src.core.Drive
import org.firstinspires.ftc.teamcode.src.core.HeadingPID
import org.firstinspires.ftc.teamcode.src.core.InputHandler
import org.firstinspires.ftc.teamcode.src.core.Intake
import org.firstinspires.ftc.teamcode.src.core.Outtake
import org.firstinspires.ftc.teamcode.src.utils.HardwareInit
import org.firstinspires.ftc.teamcode.src.utils.InitConfig
import org.firstinspires.ftc.teamcode.src.utils.InitConfig.changeSide
import org.firstinspires.ftc.teamcode.src.utils.InitConfig.changeTeam

@Configurable
@TeleOp(name = "first meet")
class MainOpMode : LinearOpMode() {
    lateinit var gamepadEx1: GamepadEx
    var readyButton: GamepadKeys.Button = GamepadKeys.Button.A
    var shootButton: GamepadKeys.Button = GamepadKeys.Button.X
    var waitButton: GamepadKeys.Button = GamepadKeys.Button.Y

    var inButton: GamepadKeys.Button = GamepadKeys.Button.LEFT_BUMPER
    var outButton: GamepadKeys.Button = GamepadKeys.Button.RIGHT_BUMPER

    override fun runOpMode() {

        HardwareInit.setHardwareMap(hardwareMap)
        HardwareInit.registerAll()

        Intake.init()
        Outtake.init()
        Drive.init()

        gamepadEx1 = GamepadEx(gamepad1)

        Drive.resetOdoIMU()

        while (opModeInInit()) {
            gamepadEx1.readButtons()

            if (gamepadEx1.wasJustPressed(GamepadKeys.Button.A)) {
                changeTeam()
            }
            if (gamepadEx1.wasJustPressed(GamepadKeys.Button.B)) {
                changeSide()
            }
            telemetry.addData("Current Team", InitConfig.currentTeam)
            telemetry.addData("Current Side", InitConfig.currentSide)
            telemetry.update()
        }

        Drive.start()

        waitForStart()
        while (opModeIsActive()) {
            gamepadEx1.readButtons()
            Drive.Move(gamepad1)

            InputHandler.handleInput(gamepadEx1)
            Outtake.changeState()

            /* Telemetry */

            telemetry.addData("Heading", Drive.getHeading(AngleUnit.DEGREES))
            telemetry.addData("Position", Drive.position)
            telemetry.addData("Current Team", InitConfig.currentTeam)
            telemetry.addData("Current Side", InitConfig.currentSide)
            telemetry.addData("Target heading", HeadingPID.target)

            telemetry.update()
        }
    }
}