package org.firstinspires.ftc.teamcode.src.utils

import org.firstinspires.ftc.teamcode.src.utils.enums.Side
import org.firstinspires.ftc.teamcode.src.utils.enums.Teams

object InitConfig {
    var currentTeam: Teams = Teams.BLUE
    var currentSide: Side = Side.SCORE

    fun changeTeam(newTeam: Teams? = null) {
        if(newTeam != null) {
            currentTeam = newTeam
            return
        }
        currentTeam = if (currentTeam == Teams.BLUE) Teams.RED else Teams.BLUE
    }

    fun changeSide(newSide: Side? = null) {
        if(newSide != null) {
            currentSide = newSide
            return
        }
        currentSide = if (currentSide == Side.SCORE) Side.SPECTATOR else Side.SCORE
    }
}