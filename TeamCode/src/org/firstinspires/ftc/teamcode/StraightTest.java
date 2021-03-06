package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import system.config.StandAlone;
import system.robot.BaseAutonomous;
import system.robot.MainRobot;
import system.robot.roadrunner_util.HALTrajectory;

@StandAlone
@Autonomous(name = "Straight Test")
public class StraightTest extends BaseAutonomous {
    public @MainRobot RoadrunnerCalibBot robot;

    public static double DISTANCE = 60; // in

    @Override
    public void main() {
        HALTrajectory trajectory = robot.drive.trajectoryBuilder(new Pose2d())
                .forward(DISTANCE)
                .build();

        robot.drive.followTrajectory(trajectory);

        Pose2d poseEstimate = robot.drive.getPoseEstimate();
        robot.telemetry.addData("finalX", poseEstimate.getX());
        robot.telemetry.addData("finalY", poseEstimate.getY());
        robot.telemetry.addData("finalHeading", poseEstimate.getHeading());
        robot.telemetry.update();

        waitUntil(()->robot.isStopRequested());
    }
}
