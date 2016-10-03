package com.scs.aresdogfighter.jmetests;

import java.nio.FloatBuffer;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.shape.Line;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.BufferUtils;

public class HUDTest extends SimpleApplication {

	public static void main(String[] args){
		HUDTest app = new HUDTest();
		app.showSettings = false;
		app.start();
	}


	public void simpleInitApp() {
		assetManager.registerLocator("assets/", FileLocator.class); // default

		//----- health bar ------------------
		//Material mat_hb = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
		Material mat_hb = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		//mat_hb.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
		Texture t = assetManager.loadTexture("Textures/healthbar.png");
		//t.setWrap(WrapMode.Repeat);
		//mat_hb.setTexture("DiffuseMap", t);
		mat_hb.setTexture("ColorMap", t);

		Geometry rect_hb = new Geometry("rect_hb", new Quad(300, 40));
		rect_hb.move(50, 200, 0);
		rect_hb.setMaterial(mat_hb);
		rect_hb.getMesh().scaleTextureCoordinates(new Vector2f(0.5f, 1f));
		guiNode.attachChild(rect_hb);
		
		
		// ------------------------------------
		
		// Create red transparent material
		Material mat = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", new ColorRGBA(1, 0, 0, 0.5f)); // 0.5f is the alpha value
		mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

		// Create rectangle of size 10x10
		Geometry mouseRect = new Geometry("MouseRect", new Quad(10, 10));
		//mouseRect.move(100, 100, 0);
		mouseRect.setMaterial(mat);
		guiNode.attachChild(mouseRect);

		Geometry rect = new Geometry("rect", new Quad(100, 100));
		rect.move(10, 10, 0);
		rect.setMaterial(mat);
		guiNode.attachChild(rect);

		Geometry rect2 = new Geometry("rect2", new Quad(200, 200));
		rect2.move(300, 300, 0);
		//rect2.setMaterial(mat);
		
		//----------------------------------------------------
		float level = 0.5f;

		Material mat2 = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat2.setBoolean("VertexColor", true);
		mat2.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
		mat2.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		rect2.setMaterial(mat2);

		Mesh mesh = rect2.getMesh();
		FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(16 * 4);
		mesh.setBuffer(Type.Color, 4, colorBuffer);
		float lowColor = 1;
		float highColor = 1 - (level - 0.5f);
		float blackAlpha = 0.6f;

		colorBuffer.position(0);
		colorBuffer.put(1).put(0).put(0).put(1);
		colorBuffer.put(1).put(0).put(0).put(1);
		colorBuffer.put(1).put(lowColor).put(0).put(1);
		colorBuffer.put(1).put(lowColor).put(0).put(1);
		colorBuffer.put(1).put(1).put(0).put(1);
		colorBuffer.put(1).put(1).put(0).put(1);
		colorBuffer.put(highColor).put(1).put(0).put(1);
		colorBuffer.put(highColor).put(1).put(0).put(1);
		colorBuffer.put(0).put(0).put(0).put(blackAlpha);
		colorBuffer.put(0).put(0).put(0).put(blackAlpha);
		colorBuffer.put(0).put(0).put(0).put(blackAlpha);
		colorBuffer.put(0).put(0).put(0).put(blackAlpha);
		colorBuffer.put(0).put(0).put(0).put(blackAlpha);
		colorBuffer.put(0).put(0).put(0).put(blackAlpha);
		colorBuffer.put(0).put(0).put(0).put(blackAlpha);
		colorBuffer.put(0).put(0).put(0).put(blackAlpha);
		mesh.getBuffer(Type.Color).updateData(colorBuffer);

		//guiNode.attachChild(rect2);

		//--------------------------------------------------------

		Material linemat = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		linemat.setColor("Color", new ColorRGBA(1, 1, 1, 0.5f)); // 0.5f is the alpha value
		linemat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

		Line linegeom = new Line(new Vector3f(0, 0, 0), new Vector3f(200, 200, 0));
		linegeom.setLineWidth(2);
		Geometry line = new Geometry("line", linegeom);
		//line.move(400, 400, 0);
		mat.setColor("Color", ColorRGBA.Blue);
		line.setMaterial(mat);
		line.setCullHint(CullHint.Never);
		guiNode.attachChild(line);

		BitmapText bt = new BitmapText(guiFont);
		bt.setSize(guiFont.getCharSet().getRenderedSize());      // font size
		bt.setColor(ColorRGBA.White);                             // font color
		bt.setText("Test\nLine2");
		bt.setLocalTranslation(110, 110, 0);
		guiNode.attachChild(bt);

		guiNode.updateGeometricState();

		inputManager.setCursorVisible(true);

		//System.out.println("size:" + cam.getWidth() + "," + cam.getHeight());
	}


	public void simpleUpdate(float tpf) {
		inputManager.setCursorVisible(true);

		// Move the rectangle to the cursor position
		Vector2f cursor = inputManager.getCursorPosition();
		guiNode.getChild("MouseRect").setLocalTranslation(cursor.x, cursor.y, 0);
		//System.out.println("pos:" + cursor.x + "," + cursor.y);
	}

}
