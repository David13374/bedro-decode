package org.firstinspires.ftc.teamcode.src.core

import com.arcrobotics.ftclib.gamepad.GamepadEx
import org.firstinspires.ftc.teamcode.src.utils.enums.InputEnum

object InputHandler {

    /* Outtake */
    fun handleInput(gamepad: GamepadEx) {
        if (gamepad.wasJustPressed(InputEnum.WAIT.getButton()))
            Outtake.changeState(
                Outtake.OuttakeState.WAITING,
            )

        if (gamepad.wasJustPressed(InputEnum.READY.getButton()))
            Outtake.changeState(
                Outtake.OuttakeState.READY,
            )

        if (gamepad.wasJustPressed(InputEnum.SHOOT.getButton()))
            Outtake.changeState(
                Outtake.OuttakeState.SHOOT,
            )

        if (gamepad.wasJustPressed(InputEnum.SORT_LEFT.getButton()))
            Outtake.updateLeftServo()

        if (gamepad.wasJustPressed(InputEnum.SORT_RIGHT.getButton()))
            Outtake.updateRightServo()

        if (gamepad.wasJustPressed(InputEnum.SORT_BOTH.getButton())) {
            Outtake.updateLeftServo()
            Outtake.updateRightServo()
        }


        /* Intake */
        if (gamepad.wasJustPressed(InputEnum.IN.getButton()))
            Intake.changeState(Intake.IntakeState.IN)
        if (gamepad.wasJustPressed(InputEnum.OUT.getButton()))
            Intake.changeState(Intake.IntakeState.OUT)

        /* Drive related */

        //if(gamepad.wasJustPressed(InputEnum.HEADING.getButton()))
        //    Drive.isPidEnabled = !Drive.isPidEnabled
    }
}