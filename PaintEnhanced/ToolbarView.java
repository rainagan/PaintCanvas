
//package Paint;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

class ToolbarView extends JPanel implements IView {
	private JButton del_button;
	private JButton red_button;
	private JButton blue_button;
	private JPanel scl_panel;
	private JPanel rot_panel;
	private JSlider scl_slider;
	private JSlider rot_slider;
	private JLabel scl_label = new JLabel("1.0");
	private JLabel rot_label = new JLabel("0");
	private JLabel scl_text = new JLabel("Scale");
	private JLabel rot_text = new JLabel("Rotate");
	// the model that this view is showing
	private DrawingModel dmodel;

	ToolbarView(DrawingModel model) {
		// create delete button
		del_button = new JButton("Delete");
		del_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (dmodel.get_select() != -1) {
					dmodel.removeShape(dmodel.get_select());
					dmodel.decrement_stroke();
					dmodel.reset_select();
				}
			}
		});
		
		// create color red button
		red_button = new JButton("Red");
		red_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (dmodel.get_select() != -1) {
					dmodel.set_red_color(dmodel.get_select());
				}
			}
		});
		
		blue_button = new JButton("Blue");
		blue_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (dmodel.get_select() != -1) {
					dmodel.set_blue_color(dmodel.get_select());
				}
			}
		});
		
		// create scale panel, which contains label, slider, and value_label
		scl_panel = new JPanel();
		scl_panel.add(scl_text);
		scl_slider = new JSlider(5, 20, 10);
		scl_panel.add(scl_slider);
		scl_panel.add(scl_label);
		scl_slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (dmodel.get_select() != -1) {
					JSlider s = (JSlider) e.getSource();
					double value = (double) s.getValue() / 10;
					dmodel.set_scale(dmodel.get_select(), value);
					String s_value = Double.toString(value);
					scl_label.setText(s_value);
				}
			}
		});

		// create rotate panel, which contains label, slider, and value_label
		rot_panel = new JPanel();
		rot_panel.add(rot_text);
		rot_slider = new JSlider(-180, 180, 0);
		rot_panel.add(rot_slider);
		rot_panel.add(rot_label);
		rot_slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (dmodel.get_select() != -1) {
					JSlider s = (JSlider) e.getSource();
					int value = s.getValue();
					dmodel.set_rotate(dmodel.get_select(), value);
					String s_value = Integer.toString(value);
					rot_label.setText(s_value);
				}
			}
		});

		// set the model
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(del_button);
		this.add(scl_panel);
		this.add(rot_panel);
		this.add(red_button);
		this.add(blue_button);
		this.dmodel = model;

		this.dmodel.addView(new IView() {
			public void updateView() {
				if (dmodel.get_select() != -1) {
					enable_toolbar(true);
					System.out.println("View: update view");
					scl_label.setText(Double.toString(dmodel.get_scl_value()));
					scl_slider.setValue((int) dmodel.get_rscl_value());
					System.out.println("Toolbar view: update scl_label");

					rot_slider.setValue(dmodel.get_rot_value());
					rot_label.setText(Integer.toString(dmodel.get_rot_value()));
					System.out.println("Toolbar view: update rot_label");
				} else {
					enable_toolbar(false);
				}
			}
		});
	}
	
	public void updateView() {
		System.out.println("View: update view");
		scl_label.setText(Double.toString(dmodel.get_scl_value()));
		// scl_slider.setValue((int) dmodel.get_scl_value());
		System.out.println("Toolbar view: update scl_label");

		rot_label.setText(Integer.toString(dmodel.get_rot_value()));
		System.out.println("Toolbar view: update rot_label");
	}
	
	public void enable_toolbar(boolean enable) {
		del_button.setEnabled(enable);
		red_button.setEnabled(enable);
		blue_button.setEnabled(enable);
		scl_slider.setEnabled(enable);
		rot_slider.setEnabled(enable);
		scl_label.setEnabled(enable);
		rot_label.setEnabled(enable);
		scl_text.setEnabled(enable);
		rot_text.setEnabled(enable);
	}
}
