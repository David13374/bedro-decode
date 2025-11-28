package org.firstinspires.ftc.teamcode.src.opmodes.auto

import com.bylazar.configurables.annotations.Configurable
import com.pedropathing.follower.Follower
import com.pedropathing.geometry.BezierLine
import com.pedropathing.geometry.Pose
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.pedroPathing.Constants

@Configurable
@Autonomous(name = "Red Spectator side")
class RedSpectatorSide : LinearOpMode() {

    private lateinit var follower: Follower

    override fun runOpMode() {
        follower = Constants.createFollower(hardwareMap)
        follower.setStartingPose(startPose)
        val path = follower.pathBuilder()
            .addPath(BezierLine(startPose, endPose))
            .setLinearHeadingInterpolation(BlueSpectatorSide.startPose.heading, BlueSpectatorSide.endPose.heading)
            .build()

        waitForStart()
        follower.followPath(path)
        while(opModeIsActive()) {
            telemetry.addData("Heading", follower.heading)
            telemetry.addData("Heading", follower.headingError)
            follower.update()
            telemetry.update()
        }
    }

    companion object {
        @JvmField val startPose = Pose(88.0, 8.0, Math.toRadians(90.0))
        @JvmField val endPose = Pose(85.66, 39.02, Math.toRadians(90.0))
    }
}