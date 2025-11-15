package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Mecanum Drive", group = "Linear Opmode")
public class MainControl extends LinearOpMode {
    private DcMotor BLWheel; // back left
    private DcMotor FLWheel; // front left
    private DcMotor FRWheel; // front right
    private DcMotor BRWheel; // back right

    @Override
    public void runOpMode() {
        // motors mapped on DS, double check if reversed or incorrect
        BLWheel = hardwareMap.get(DcMotor.class, "BLWheel");
        FLWheel = hardwareMap.get(DcMotor.class, "FLWheel");
        FRWheel = hardwareMap.get(DcMotor.class, "FRWheel");
        BRWheel = hardwareMap.get(DcMotor.class, "BRWheel");
        LFeeder = hardwareMap.get(DcMotor.class, "LFeeder");
        RFeeder = hardwareMap.get(DcMotor.class, "RFeeder");
        
        // mecanum drive / reverse test
        BLWheel.setDirection(DcMotorSimple.Direction.FORWARD);
        FLWheel.setDirection(DcMotorSimple.Direction.FORWARD);
        FRWheel.setDirection(DcMotorSimple.Direction.REVERSE);
        BRWheel.setDirection(DcMotorSimple.Direction.REVERSE);
        LFeeder.setDirection(DcMotorSimple.Direction.FORWARD);
        RFeeder.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData("Status", "Initialized - Waiting for start");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            double drive = -gamepad1.left_stick_y;    // forward/backward
            double strafe = gamepad1.left_stick_x;     // left/right
            double rotate = gamepad1.right_stick_x;    // rotation

            // mecanum drive calc
            double frontLeftPower = drive + strafe + rotate;
            double backLeftPower = drive - strafe + rotate;
            double frontRightPower = drive - strafe - rotate;
            double backRightPower = drive + strafe - rotate;
            double maxPower = Math.max(
                Math.abs(frontLeftPower),
                Math.max(Math.abs(backLeftPower),
                    Math.max(Math.abs(frontRightPower),
                        Math.abs(backRightPower)
                    )
                )
            );

            if (maxPower > 1.0) {
                frontLeftPower /= maxPower;
                backLeftPower /= maxPower;
                frontRightPower /= maxPower;
                backRightPower /= maxPower;
            }

            LFeeder.setPower(gamepad1.left_bumper);
            RFeeder.setPower(gamepad1.left_bumper);

            // Apply power to motors
            FLWheel.setPower(frontLeftPower);
            BLWheel.setPower(backLeftPower);
            FRWheel.setPower(frontRightPower);
            BRWheel.setPower(backRightPower);

            // Telemetry
            telemetry.addData("Drive", drive);
            telemetry.addData("Strafe", strafe);
            telemetry.addData("Rotate", rotate);
            telemetry.addData("FL Wheel Power", FLWheel.getPower());
            telemetry.addData("BL Wheel Power", BLWheel.getPower());
            telemetry.addData("FR Wheel Power", FRWheel.getPower());
            telemetry.addData("BR Wheel Power", BRWheel.getPower());
            telemetry.addData("L Feeder Power", LFeeder.getPower());
            telemetry.addData("R Feeder Power", RFeeder.getPower());
            telemetry.update();
        }
    }
}