package com.scs.aresdogfighter.gui;

import java.util.ArrayList;
import java.util.List;

import ssmith.lang.TimeFunctions;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;

public class TextArea extends BitmapText {

	private static final long DURATION = 5;

	private volatile List<String> lines;
	private int max;
	private volatile long next_removal = DURATION;

	//private String curr_line;

	public TextArea(String name, BitmapFont guiFont, int _max, String text, boolean autoclear) {
		super(guiFont, false);

		this.setName(this.getName() + ": " + name);
		this.setSize(guiFont.getCharSet().getRenderedSize());
		this.setColor(ColorRGBA.LightGray);
		this.setText(text);

		lines = new ArrayList<String>(max*2);
		max = _max;

		if (autoclear) {
			Runnable cleardown_thread = new Runnable()
			{
				@Override
				public void run()
				{
					while (true) {
						TimeFunctions.sleep(2000);
						next_removal--;
						if (next_removal <= 0) {
							synchronized (lines) {
								if (lines.size() > 0) {
									lines.remove(0);
								}
							}
							next_removal = DURATION;
						}
					}
				}
			};
			Thread t = new Thread(cleardown_thread, "cleardown_thread");
			t.setDaemon(true);
			t.start();
		}
	}


	public void addLine(String s) {
		synchronized (lines) {
			lines.add(s);
			if (max > 0) {
				while (lines.size() > max) {
					lines.remove(0);
				}
			}
		}
		this.setText();
	}


	private void setText() {
		StringBuffer str = new StringBuffer();
		synchronized (lines) {
			for (int i=0 ; i<lines.size() ; i++) {
				str.append(lines.get(i)+ "\n");
			}
		}
		super.setText(str.toString());

	}

}
