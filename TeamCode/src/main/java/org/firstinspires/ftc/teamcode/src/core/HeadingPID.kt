@file:Suppress("SpellCheckingInspection")

package org.firstinspires.ftc.teamcode.src.core

import com.arcrobotics.ftclib.controller.PIDController
import com.bylazar.configurables.annotations.Configurable

@Configurable
object HeadingPID {
    private lateinit var controller: PIDController
    private const val DIFFERENCE: Double = 5.0

    fun init() {
        controller = PIDController(p, i, d)
    }

    fun update(heading: Double) {
        if(!(heading <= target + DIFFERENCE && heading >= target - DIFFERENCE)) {
            controller.setPID(p, i, d)
            val pid = controller.calculate(heading, target)

            Drive.setMotorPowers(LF_COEF * pid, LB_COEF * pid, RF_COEF * pid, RB_COEF * pid)
        }
    }

    var p: Double = 0.05
    var i: Double = 0.00005
    var d: Double = 0.00005
    var target: Double = 2.35
    private const val LB_COEF: Double = -1.0
    private const val LF_COEF: Double = -1.0
    private const val RB_COEF: Double = 1.0
    private const val RF_COEF: Double = 1.0
}