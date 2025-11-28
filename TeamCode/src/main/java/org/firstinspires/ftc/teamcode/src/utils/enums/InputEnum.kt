package org.firstinspires.ftc.teamcode.src.utils.enums

import com.arcrobotics.ftclib.gamepad.GamepadKeys

enum class InputEnum(private val button: GamepadKeys.Button) {
    READY(GamepadKeys.Button.A),
    SHOOT(GamepadKeys.Button.X),
    WAIT(GamepadKeys.Button.Y),
    IN(GamepadKeys.Button.LEFT_BUMPER),
    OUT(GamepadKeys.Button.RIGHT_BUMPER),
    //HEADING(GamepadKeys.Button.DPAD_DOWN),
    SORT_LEFT(GamepadKeys.Button.DPAD_LEFT),
    SORT_RIGHT(GamepadKeys.Button.DPAD_RIGHT),
    SORT_BOTH(GamepadKeys.Button.DPAD_DOWN)
    ;
    fun getButton() = button
}