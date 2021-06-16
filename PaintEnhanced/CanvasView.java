
//package Paint;

import javax.swing.*;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.geom.*;
import javax.vecmath.*;

class CanvasView extends JPanel {
	private DrawingModel dmodel;
	// keep track of mouse position
	private Point2d M = new Point2d(0,0);
	// check if click and drag
	private boolean dragged = false;

	CanvasView(DrawingModel model) {
		this.dmodel = model;

		// setup the event to go to the controller
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				dmodel.addShape(new Shape());
				dragged = false;
				dmodel.reset_select();
				repaint();
			}

			public void mouseReleased(MouseEvent e) {
				if (dragged)
					dmodel.increment_stroke();
			}

			public void mouseClicked(MouseEvent e) {
				// hit test
				M = new Point2d(e.getX(), e.getY());
				for (int i = 0; i < dmodel.nshapes(); ++i) {
					if (dmodel.getShape(i) != null) {
						if (dmodel.getShape(i).hittest(M.x, M.y)) {
							System.out.println("hit shape");
							dmodel.clear_select();
							dmodel.select_shape(i);
						}
					}
				}
			}
		});
		this.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (dmodel.get_select() == -1) {
					dmodel.getShape(dmodel.nshapes() - 1).addPoint(e.getX(), e.getY());
					dragged = true;
				} else {
					M = new Point2d(e.getX(), e.getY());
				}
				repaint();
			}
		});

		this.dmodel.addView(new IView() {
			public void updateView() {
				System.out.println("View: update view");
				repaint();
			}
		});
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g; // cast to get 2D drawing methods
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // antialiasing look nicer
				RenderingHints.VALUE_ANTIALIAS_ON);

		for (int i = 0; i < dmodel.nshapes(); ++i) {
			if (dmodel.getShape(i) != null) {
				dmodel.getShape(i).draw(g2);
			}
		}
	}
}