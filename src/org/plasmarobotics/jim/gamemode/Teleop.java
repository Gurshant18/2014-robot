/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.plasmarobotics.jim.gamemode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.plasmarobotics.jim.Aimbot;
import org.plasmarobotics.jim.Logger;
import org.plasmarobotics.jim.controls.ControlPack;
import org.plasmarobotics.jim.controls.PlasmaGamepad;
import org.plasmarobotics.jim.controls.PlasmaJoystick;
import org.plasmarobotics.jim.mechanisms.Drive;
import org.plasmarobotics.jim.mechanisms.MechanismPack;
import org.plasmarobotics.jim.mechanisms.Pickup;
import org.plasmarobotics.jim.mechanisms.Shoot;
import org.plasmarobotics.jim.sensors.SensorPack;

/**
 *
 * @author Jim
 */
public class Teleop{
    private PlasmaJoystick leftJoystick,
            rightJoystick;
    
    private PlasmaGamepad gamePad;
    
    private Drive drive;
    private Shoot shooter;
    private Pickup pickup;
//    private Catch catcher;
    private Aimbot aimbot;
    
    public Teleop(ControlPack controls, MechanismPack mechanisms, SensorPack sensors){
    
        //controls
        this.leftJoystick = controls.getLeftJoystick();
        this.rightJoystick = controls.getRightJoystick();
        this.gamePad = controls.getGamepad();
        
        //mechanisms
        this.drive = mechanisms.getDrive();
        this.shooter = mechanisms.getShooter();
        this.pickup = mechanisms.getPickup();
//        this.catcher = mechanisms.getCatcher();
        
        this.aimbot = new Aimbot();
          
        
    }
    
    /**
     * used to get the robot ready for teleop
     */
    public void teleopInit(){
        drive.setupTeleop();
        shooter.setupTeleop();
        pickup.setupTeleop();
    }
    /**
     * This gets called periodically during teleop
     */
    boolean aimbotNeedReset = false;
    public void run(){
        
        
        if(leftJoystick.getSix().isPressed())
            Logger.raisePriority();
        
        if(leftJoystick.getSeven().isPressed())
            Logger.lowerPriority();
        
        
        if(ControlPack.getInstance().getAimbotButton().get()){
            aimbot.aim(0);
            aimbotNeedReset = true;
            Logger.log("aimbotting", this, 5);
        } else {
            
            if(aimbotNeedReset){
                Logger.log("Aimbot stopped", this, 5);
                aimbot.logGyroAngles();
                aimbot.reset(); // set AimBot state to 0
                aimbotNeedReset = false;
            }
            
          
            //Logger.log("angle: " + SensorPack.getInstance().getGyro().getAbsoluteAngle(), this, 3);
            drive.updateTeleop(); // back to tank (AimBot took over
            shooter.updateTeleop();
            pickup.updateTeleop();
            aimbot.updateTeleop();
//            catcher.updateTeleop();
            SmartDashboard.putNumber("RANGE: ", SensorPack.getInstance().getRangeFinder().getDistance());
            SmartDashboard.putNumber("voltage", SensorPack.getInstance().getRangeFinder().getVoltage());
            SmartDashboard.putNumber("Gyro angle:", SensorPack.getInstance().getGyro().getModdedAngle());
            SmartDashboard.putNumber("encoder", SensorPack.getInstance().getRightEncoder().get());
            
        }
        
    }
    
    
    
}
