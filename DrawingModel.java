//package Paint;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.vecmath.*;
import javax.swing.border.BevelBorder;

interface IView {
	public void updateView();
}

public class DrawingModel {
	// array of views
	private ArrayList<IView> views = new ArrayList<IView>();
	// arrays of shapes that has been drawn
	private ArrayList<Shape> shapes = new ArrayList<Shape>();
	// scale and rotate value
	private double scl_value = 1.0;
	private int rot_value=0;
	// number of strokes that has been drawn
	private int strokes=0;
	// index of selected stroke in the arraylist of shape
	private int select=-1;
	
	// toolbar 
	public double get_scl_value() {
		return scl_value;
	}
	public double get_rscl_value() {
		return scl_value*10;
	}
	public int get_rot_value() {
		return rot_value;
	}
	
	// statusbar
	public int get_strokes() {
		return strokes;
	}
	public void increment_stroke() {
		++strokes;
		notifyObserver();
	}
	public void decrement_stroke() {
		--strokes;
		notifyObserver();
	}
	
	// canvas
	public void clearShapes() {
		shapes = new ArrayList<Shape>();
		System.out.println("clear arrayList of shapes");
	}
	public void addShape(Shape s) {
		if (shapes == null)
			clearShapes();
			shapes.add(s);
	}
	public void removeShape(int i) {
		shapes.remove(i);
		notifyObserver();
	}
	public Shape getShape(int i) {
		return shapes.get(i);
	}
	public int nshapes() {
		return shapes.size();
	}
	// once a shape is selected, update select, rot_value and scl_value
	public void select_shape(int i) {
		shapes.get(i).select = true;
		select = i;
		rot_value = shapes.get(select).rotate;
		scl_value = shapes.get(select).scale;
		notifyObserver();
	}
	public void clear_select() {
		for (int i=0; i<nshapes(); i++) {
			shapes.get(i).select = false;
			select = -1;
			notifyObserver();
		}
	}
	public int get_select() {
		return select;
	}
	public void reset_select() {
		select = -1;
		clear_select();
		notifyObserver();
	}
	// set shape at index i to have scale = value
	public void set_scale(int i, double value) {
		shapes.get(i).scale = (float) value;
		scl_value = value;
		notifyObserver();
	}
	// set shape at index i to have rotate = value
	public void set_rotate(int i, int value) {
		shapes.get(i).rotate = value;
		rot_value = value;
		notifyObserver();
	}
	
	// view
	public void addView(IView view) {
		this.views.add(view);
		view.updateView();
	}
	public void removeView(IView view) {
		this.views.remove(view);
	}
	
	private void notifyObserver() {
		System.out.println("DrawingModel: notify view");
		for (IView view : this.views) {
			view.updateView();
		}
	}
}