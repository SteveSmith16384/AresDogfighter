package com.scs.aresdogfighter;

import com.jme3.collision.MotionAllowedListener;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.Joystick;
import com.jme3.input.JoystickAxis;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

public class MyCamera implements AnalogListener, ActionListener {

    private static String[] mappings = new String[]{
            "FLYCAM_Left",
            "FLYCAM_Right",
            "FLYCAM_Up",
            "FLYCAM_Down",

            "FLYCAM_StrafeLeft",
            "FLYCAM_StrafeRight",
            "FLYCAM_Forward",
            "FLYCAM_Backward",

            "FLYCAM_ZoomIn",
            "FLYCAM_ZoomOut",
            "FLYCAM_RotateDrag",

            "FLYCAM_Rise",
            "FLYCAM_Lower",
            
            "FLYCAM_InvertY"
        };

    protected Camera cam;
    protected Vector3f initialUpVec;
    protected float rotationSpeed = 1f;
    protected float moveSpeed = 24f; //3f;
    protected float zoomSpeed = 10f;//1f;
    protected MotionAllowedListener motionAllowed = null;
    protected boolean enabled = true;
    protected boolean dragToRotate = false;
    protected boolean canRotate = false;
    protected boolean invertY = false;
    protected InputManager inputManager;
    
    /**
     * Creates a new FlyByCamera to control the given Camera object.
     * @param cam
     */
    public MyCamera(Camera cam){
        this.cam = cam;
        initialUpVec = cam.getUp().clone();
    }

    /**
     * Sets the up vector that should be used for the camera.
     * @param upVec
     */
    public void setUpVector(Vector3f upVec) {
       initialUpVec.set(upVec);
    }

    public void setMotionAllowedListener(MotionAllowedListener listener){
        this.motionAllowed = listener;
    }

    /**
     * Sets the move speed. The speed is given in world units per second.
     * @param moveSpeed
     */
    public void setMoveSpeed(float moveSpeed){
        this.moveSpeed = moveSpeed;
    }
    
    /**
     * Gets the move speed. The speed is given in world units per second.
     * @return moveSpeed
     */
    public float getMoveSpeed(){
        return moveSpeed;
    }

    /**
     * Sets the rotation speed.
     * @param rotationSpeed
     */
    public void setRotationSpeed(float rotationSpeed){
        this.rotationSpeed = rotationSpeed;
    }
    
    /**
     * Gets the move speed. The speed is given in world units per second.
     * @return rotationSpeed
     */
    public float getRotationSpeed(){
        return rotationSpeed;
    }
    
    /**
     * Sets the zoom speed.
     * @param zoomSpeed 
     */
    public void setZoomSpeed(float zoomSpeed) {
        this.zoomSpeed = zoomSpeed;
    }
    
    /**
     * Gets the zoom speed.  The speed is a multiplier to increase/decrease
     * the zoom rate.
     * @return zoomSpeed
     */
    public float getZoomSpeed() {
        return zoomSpeed;
    }

    /**
     * @param enable If false, the camera will ignore input.
     */
    public void setEnabled(boolean enable){
        if (enabled && !enable){
            if (inputManager!= null && (!dragToRotate || (dragToRotate && canRotate))){
                inputManager.setCursorVisible(true);
            }
        }
        enabled = enable;
    }

    /**
     * @return If enabled
     * @see FlyByCamera#setEnabled(boolean)
     */
    public boolean isEnabled(){
        return enabled;
    }

    /**
     * @return If drag to rotate feature is enabled.
     *
     * @see FlyByCamera#setDragToRotate(boolean) 
     */
    public boolean isDragToRotate() {
        return dragToRotate;
    }

    /**
     * Set if drag to rotate mode is enabled.
     * 
     * When true, the user must hold the mouse button
     * and drag over the screen to rotate the camera, and the cursor is
     * visible until dragged. Otherwise, the cursor is invisible at all times
     * and holding the mouse button is not needed to rotate the camera.
     * This feature is disabled by default.
     * 
     * @param dragToRotate True if drag to rotate mode is enabled.
     */
    public void setDragToRotate(boolean dragToRotate) {
        this.dragToRotate = dragToRotate;
        if (inputManager != null) {
            inputManager.setCursorVisible(dragToRotate);
        }
    }

    /**
     * Registers the FlyByCamera to receive input events from the provided
     * Dispatcher.
     * @param inputManager
     */
    public void registerWithInput(InputManager inputManager){
        this.inputManager = inputManager;
        
        // both mouse and button - rotation of cam
        inputManager.addMapping("FLYCAM_Left", new KeyTrigger(KeyInput.KEY_LEFT));

        inputManager.addMapping("FLYCAM_Right", new KeyTrigger(KeyInput.KEY_RIGHT));

        inputManager.addMapping("FLYCAM_Up", new KeyTrigger(KeyInput.KEY_UP));

        inputManager.addMapping("FLYCAM_Down", new KeyTrigger(KeyInput.KEY_DOWN));

        // mouse only - zoom in/out with wheel, and rotate drag
        inputManager.addMapping("FLYCAM_ZoomIn", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addMapping("FLYCAM_ZoomOut", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        inputManager.addMapping("FLYCAM_RotateDrag", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));

        // keyboard only WASD for movement and WZ for rise/lower height
        inputManager.addMapping("FLYCAM_StrafeLeft", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("FLYCAM_StrafeRight", new KeyTrigger(KeyInput.KEY_D));
        //inputManager.addMapping("FLYCAM_Forward", new KeyTrigger(KeyInput.KEY_W));
        //inputManager.addMapping("FLYCAM_Backward", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("FLYCAM_Rise", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("FLYCAM_Lower", new KeyTrigger(KeyInput.KEY_S));

        inputManager.addListener(this, mappings);
        inputManager.setCursorVisible(dragToRotate || !isEnabled());

        Joystick[] joysticks = inputManager.getJoysticks();
        if (joysticks != null && joysticks.length > 0){
            for (Joystick j : joysticks) {
                mapJoystick(j);
            }
        }
    }

    protected void mapJoystick( Joystick joystick ) {
        
        // Map it differently if there are Z axis
        if( joystick.getAxis( JoystickAxis.Z_ROTATION ) != null && joystick.getAxis( JoystickAxis.Z_AXIS ) != null ) {
 
            // Make the left stick move
            joystick.getXAxis().assignAxis( "FLYCAM_StrafeRight", "FLYCAM_StrafeLeft" );
            joystick.getYAxis().assignAxis( "FLYCAM_Backward", "FLYCAM_Forward" );
            
            // And the right stick control the camera                       
            joystick.getAxis( JoystickAxis.Z_ROTATION ).assignAxis( "FLYCAM_Down", "FLYCAM_Up" );
            joystick.getAxis( JoystickAxis.Z_AXIS ).assignAxis(  "FLYCAM_Right", "FLYCAM_Left" );
 
            // And let the dpad be up and down           
            joystick.getPovYAxis().assignAxis("FLYCAM_Rise", "FLYCAM_Lower");
 
            if( joystick.getButton( "Button 8" ) != null ) { 
                // Let the stanard select button be the y invert toggle
                joystick.getButton( "Button 8" ).assignButton( "FLYCAM_InvertY" );
            }                
            
        } else {             
            joystick.getPovXAxis().assignAxis("FLYCAM_StrafeRight", "FLYCAM_StrafeLeft");
            joystick.getPovYAxis().assignAxis("FLYCAM_Forward", "FLYCAM_Backward");
            joystick.getXAxis().assignAxis("FLYCAM_Right", "FLYCAM_Left");
            joystick.getYAxis().assignAxis("FLYCAM_Down", "FLYCAM_Up");
        }                
    }

    /**
     * Registers the FlyByCamera to receive input events from the provided
     * Dispatcher.
     * @param inputManager
     */
    public void unregisterInput(){
    
        if (inputManager == null) {
            return;
        }
    
        for (String s : mappings) {
            if (inputManager.hasMapping(s)) {
                inputManager.deleteMapping( s );
            }
        }

        inputManager.removeListener(this);
        inputManager.setCursorVisible(!dragToRotate);

        Joystick[] joysticks = inputManager.getJoysticks();
        if (joysticks != null && joysticks.length > 0){
            Joystick joystick = joysticks[0];
            
            // No way to unassing axis
        }
    }

    protected void rotateCamera(float value, Vector3f axis){
        if (dragToRotate){
            if (canRotate){
//                value = -value;
            }else{
                return;
            }
        }

        Matrix3f mat = new Matrix3f();
        mat.fromAngleNormalAxis(rotationSpeed * value, axis);

        Vector3f up = cam.getUp();
        Vector3f left = cam.getLeft();
        Vector3f dir = cam.getDirection();

        mat.mult(up, up);
        mat.mult(left, left);
        mat.mult(dir, dir);

        Quaternion q = new Quaternion();
        q.fromAxes(left, up, dir);
        q.normalizeLocal();

        cam.setAxes(q);
    }

    protected void zoomCamera(float value){
        // derive fovY value
        float h = cam.getFrustumTop();
        float w = cam.getFrustumRight();
        float aspect = w / h;

        float near = cam.getFrustumNear();

        float fovY = FastMath.atan(h / near)
                  / (FastMath.DEG_TO_RAD * .5f);
        float newFovY = fovY + value * 0.1f * zoomSpeed;
        if (newFovY > 0f) {
            // Don't let the FOV go zero or negative.
            fovY = newFovY;
        }

        h = FastMath.tan( fovY * FastMath.DEG_TO_RAD * .5f) * near;
        w = h * aspect;

        cam.setFrustumTop(h);
        cam.setFrustumBottom(-h);
        cam.setFrustumLeft(-w);
        cam.setFrustumRight(w);
    }

    protected void riseCamera(float value){
        Vector3f vel = new Vector3f(0, value * moveSpeed, 0);
        Vector3f pos = cam.getLocation().clone();

        if (motionAllowed != null)
            motionAllowed.checkMotionAllowed(pos, vel);
        else
            pos.addLocal(vel);

        cam.setLocation(pos);
    }

    protected void moveCamera(float value, boolean sideways){
        Vector3f vel = new Vector3f();
        Vector3f pos = cam.getLocation().clone();

        if (sideways){
            cam.getLeft(vel);
        }else{
            cam.getDirection(vel);
        }
        vel.multLocal(value * moveSpeed);

        if (motionAllowed != null)
            motionAllowed.checkMotionAllowed(pos, vel);
        else
            pos.addLocal(vel);

        cam.setLocation(pos);
    }

    public void onAnalog(String name, float value, float tpf) {
        if (!enabled)
            return;

        if (name.equals("FLYCAM_Left")){
            rotateCamera(value, initialUpVec);
        }else if (name.equals("FLYCAM_Right")){
            rotateCamera(-value, initialUpVec);
        }else if (name.equals("FLYCAM_Up")){
            rotateCamera(-value * (invertY ? -1 : 1), cam.getLeft());
        }else if (name.equals("FLYCAM_Down")){
            rotateCamera(value * (invertY ? -1 : 1), cam.getLeft());
        }else if (name.equals("FLYCAM_Forward")){
            moveCamera(value, false);
        }else if (name.equals("FLYCAM_Backward")){
            moveCamera(-value, false);
        }else if (name.equals("FLYCAM_StrafeLeft")){
            moveCamera(value, true);
        }else if (name.equals("FLYCAM_StrafeRight")){
            moveCamera(-value, true);
        }else if (name.equals("FLYCAM_Rise")){
            riseCamera(value);
        }else if (name.equals("FLYCAM_Lower")){
            riseCamera(-value);
        }else if (name.equals("FLYCAM_ZoomIn")){
            zoomCamera(value);
        }else if (name.equals("FLYCAM_ZoomOut")){
            zoomCamera(-value);
        }
    }

    public void onAction(String name, boolean value, float tpf) {
        if (!enabled)
            return;

        if (name.equals("FLYCAM_RotateDrag") && dragToRotate){
            canRotate = value;
            inputManager.setCursorVisible(!value);
        } else if (name.equals("FLYCAM_InvertY")) {
            // Toggle on the up.
            if( !value ) {  
                invertY = !invertY;
            }
        }        
    }

}
