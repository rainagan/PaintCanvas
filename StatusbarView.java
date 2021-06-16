
//package Paint;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.*;

class StatusbarView extends JPanel implements IView {

	private DrawingModel dmodel;
	// labels for statusbar
	private JLabel number = new JLabel("0");
	private JLabel stroke = new JLabel(" Strokes");
	private JLabel selection1 = new JLabel("");
	private JLabel selection2 = new JLabel("");
	private JLabel selection3 = new JLabel("");
	private JLabel selection4 = new JLabel("");
	private JLabel nPoints = new JLabel("");
	private JLabel scale = new JLabel("");
	private JLabel rotation = new JLabel("");
	private String status = "Strokes";

	StatusbarView(DrawingModel model) {
		this.add(number);
		this.add(stroke);
		this.add(selection1);
		this.add(nPoints);
		this.add(selection2);
		this.add(scale);
		this.add(selection3);
		this.add(rotation);
		this.add(selection4);
		
		// set the model
		this.dmodel = model;
		// setup the event to go to the controller

		this.dmodel.addView(new IView() {
			public void updateView() {
				number.setText(Integer.toString(dmodel.get_strokes()));
				stroke.setText(status);
				if (dmodel.get_select() != -1) {
					selection1.setText(", Selection (");
					nPoints.setText(Integer.toString(dmodel.getShape(dmodel.get_select()).npoints()));
					selection2.setText(" points, scale: ");
					scale.setText(Double.toString(dmodel.get_scl_value()));
					selection3.setText(", rotation ");
					rotation.setText(Integer.toString(dmodel.get_rot_value()));
					selection4.setText(")");
					
				} else {
					selection1.setText("");
					selection2.setText("");
					selection3.setText("");
					selection4.setText("");
					nPoints.setText("");
					scale.setText("");
					rotation.setText("");
				}
				System.out.println("statusbar view: update stroke");
			}
		});
	}

	public void updateView() {
		System.out.println("View: update view");

	}
}
