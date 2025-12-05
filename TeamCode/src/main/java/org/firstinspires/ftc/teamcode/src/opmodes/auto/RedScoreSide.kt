package org.firstinspires.ftc.teamcode.src.opmodes.auto

import com.pedropathing.follower.Follower
import com.pedropathing.geometry.BezierLine
import com.pedropathing.geometry.Pose
import com.pedropathing.paths.PathChain
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.teamcode.pedroPathing.Constants
import org.firstinspires.ftc.teamcode.src.core.Outtake
import org.firstinspires.ftc.teamcode.src.utils.HardwareInit
import org.firstinspires.ftc.teamcode.src.utils.InitConfig
import org.firstinspires.ftc.teamcode.src.utils.enums.MotorEnum
import org.firstinspires.ftc.teamcode.src.utils.enums.ServoEnum
import org.firstinspires.ftc.teamcode.src.utils.enums.Side
import org.firstinspires.ftc.teamcode.src.utils.enums.Teams

@Autonomous(name = "Red Score Side")
class RedScoreSide : OpMode() {
    private val startPos = Pose(118.0,130.0, Math.toRadians(35.0))
    private val forwardPos = Pose(107.5, 121.79, Math.toRadians(35.0))
    private val parkPos = Pose(121.0, 96.6, Math.toRadians(-90.0))

    private lateinit var forwardTraj: PathChain
    private lateinit var parkTraj: PathChain

    private lateinit var follower: Follower

    private var currentState = 0
    private var started = false

    override fun loop() {
        follower.update()
        updateAutonomous()
    }

    override fun init() {
        currentState = 0
        follower = Constants.createFollower(hardwareMap)
        follower.setStartingPose(startPos)
        HardwareInit.setHardwareMap(hardwareMap)
        HardwareInit.registerMotor(MotorEnum.FLY_WHEEL)
        HardwareInit.registerServo(ServoEnum.OUTTAKE_SERVO)
        HardwareInit.registerServo(ServoEnum.SORT_RIGHT_SERVO)
        HardwareInit.registerServo(ServoEnum.SORT_LEFT_SERVO)

        Outtake.init()
        Outtake.changeState(Outtake.OuttakeState.READY)
        Outtake.setServoPosition(Outtake.ReadyServoPos)

        InitConfig.changeSide(Side.SCORE)
        InitConfig.changeTeam(Teams.RED)

        buildPaths()
    }

    fun updateAutonomous() {
        when(currentState) {
            0 -> {
                follower.followPath(forwardTraj)
                currentState = 1
            }
            1 -> {
                if(!follower.isBusy) {
                    if(!started) {
                        Outtake.changeState(Outtake.OuttakeState.SHOOT)
                        started = true
                    }
                    Outtake.update()
                    if(Outtake.timer.time() > Outtake.motorStopTime + Outtake.motorAccelTime) {
                        follower.followPath(parkTraj)
                        Outtake.update()
                        currentState = 2
                    }
                }
            }
        }
    }

    fun buildPaths() {
        forwardTraj = follower.pathBuilder()
            .addPath(BezierLine(startPos, forwardPos))
            .setLinearHeadingInterpolation(startPos.heading, forwardPos.heading)
            .build()

        parkTraj = follower.pathBuilder()
            .addPath(BezierLine(forwardPos, parkPos))
            .setLinearHeadingInterpolation(forwardPos.heading, parkPos.heading)
            .build()
    }
}