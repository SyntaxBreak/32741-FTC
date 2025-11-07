package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Mecanum Drive", group = "Linear Opmode")
public class MainControl extends LinearOpMode {

    private DcMotor motor1; // back left
    private DcMotor motor2; // front left
    private DcMotor motor3; // front right
    private DcMotor motor4; // back right

    @Override
    public void runOpMode() {
        // motors mapped on DS, double check if reversed or incorrect
        motor1 = hardwareMap.get(DcMotor.class, "motor1");
        motor2 = hardwareMap.get(DcMotor.class, "motor2");
        motor3 = hardwareMap.get(DcMotor.class, "motor3");
        motor4 = hardwareMap.get(DcMotor.class, "motor4");

        // mecanum drive / reverse test
        motor1.setDirection(DcMotorSimple.Direction.FORWARD);
        motor2.setDirection(DcMotorSimple.Direction.FORWARD);
        motor3.setDirection(DcMotorSimple.Direction.REVERSE);
        motor4.setDirection(DcMotorSimple.Direction.REVERSE);

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

            double maxPower = Math.max(Math.abs(frontLeftPower),
                    Math.max(Math.abs(backLeftPower),
                            Math.max(Math.abs(frontRightPower),
                                    Math.abs(backRightPower))));

            if (maxPower > 1.0) {
                frontLeftPower /= maxPower;
                backLeftPower /= maxPower;
                frontRightPower /= maxPower;
                backRightPower /= maxPower;
            }

            // Apply power to motors
            motor2.setPower(frontLeftPower);
            motor1.setPower(backLeftPower);
            motor3.setPower(frontRightPower);
            motor4.setPower(backRightPower);

            // Telemetry
            telemetry.addData("Drive", drive);
            telemetry.addData("Strafe", strafe);
            telemetry.addData("Rotate", rotate);
            telemetry.addData("FL Power", frontLeftPower);
            telemetry.addData("BL Power", backLeftPower);
            telemetry.addData("FR Power", frontRightPower);
            telemetry.addData("BR Power", backRightPower);
            telemetry.update();
        }
    }
}