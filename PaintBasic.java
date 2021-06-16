/* use source code from /cs349-code/java/6-graphics:
ClosestPoint.java, Transform2.java
/cs349-code/java/6-graphics/shapemodel: 
Shape.java, ShapeDemo.java
/cs349-code/java/7-mvc/hellomvc1:
Controller.java, Model.java, View.java */

//package Paint;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.*;

public class PaintBasic {

	public static void main(String[] args){
		JFrame frame = new JFrame("PaintBasic.java");
		frame.setLayout(new BorderLayout());

		// create DrawingModel and initialize it
		DrawingModel dmodel = new DrawingModel();
		
		// create views, tell them about DrawingModel and Controller
		ToolbarView tbview = new ToolbarView(dmodel);
		StatusbarView sbview = new StatusbarView(dmodel);
		CanvasView cview = new CanvasView(dmodel);
		
		// create toolbar panel
		JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		toolbarPanel.setPreferredSize(new Dimension(frame.getWidth(), 60));
		// create statusbar panel
		JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		statusPanel.setPreferredSize(new Dimension(frame.getWidth(), 30));
		// create canvas panel
		JPanel canvasPanel = new JPanel(new BorderLayout());
		canvasPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		canvasPanel.setPreferredSize(new Dimension(frame.getWidth(), 500));
		canvasPanel.add(cview);
		
		// add view (view is a JPanel)
		frame.getContentPane().add(canvasPanel, BorderLayout.CENTER);
		frame.getContentPane().add(toolbarPanel, BorderLayout.NORTH);
		frame.getContentPane().add(statusPanel, BorderLayout.SOUTH);

		toolbarPanel.add(tbview);
		statusPanel.add(sbview);
		
		frame.setResizable(true);
		frame.setPreferredSize(new Dimension(900, 600));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
