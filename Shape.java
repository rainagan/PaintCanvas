
//package Paint;

import java.awt.*;
import java.awt.geom.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.lang.Math.*;
import javax.vecmath.*;

//simple shape model class
public class Shape {
	// shape points
	ArrayList<Point2d> points;
	// whether this shape is selected
	boolean select = false;
	// features for creating a bounding box
	double xmin;
	double xmax;
	double ymin;
	double ymax;
	Point2d center;
	// rotation degree
	int rotate;
	AffineTransform M1;

	public void clearPoints() {
		points = new ArrayList<Point2d>();
		pointsChanged = true;
	}

	// add a point to end of shape
	public void addPoint(Point2d p) {
		if (points == null)
			clearPoints();
		points.add(p);
		pointsChanged = true;
	}

	// add a point to end of shape
	public void addPoint(double x, double y) {
		addPoint(new Point2d(x, y));
	}

	public int npoints() {
		return points.size();
	}

	// shape is polyline or polygon
	Boolean isClosed = false;

	public Boolean getIsClosed() {
		return isClosed;
	}

	public void setIsClosed(Boolean isClosed) {
		this.isClosed = isClosed;
	}

	// if polygon is filled or not
	Boolean isFilled = false;

	public Boolean getIsFilled() {
		return isFilled;
	}

	public void setIsFilled(Boolean isFilled) {
		this.isFilled = isFilled;
	}

	// drawing attributes
	Color colour = Color.BLACK;
	float strokeThickness = 2.0f;

	public Color getColour() {
		return colour;
	}

	public void setColour(Color colour) {
		this.colour = colour;
	}

	public float getStrokeThickness() {
		return strokeThickness;
	}

	public void setStrokeThickness(float strokeThickness) {
		this.strokeThickness = strokeThickness;
	}

	public void get_xmin() {
		double min = 100000;
		for (int i = 0; i < points.size(); i++) {
			if (points.get(i).getX() < min) {
				min = points.get(i).getX();
			}
		}
		xmin = min;
	}

	public void get_xmax() {
		double max = -100000;
		for (int i = 0; i < points.size(); i++) {
			if (points.get(i).getX() > max) {
				max = points.get(i).getX();
			}
		}
		xmax = max;
	}

	public void get_ymin() {
		double min = 100000;
		for (int i = 0; i < points.size(); i++) {
			if (points.get(i).getY() < min) {
				min = points.get(i).getY();
			}
		}
		ymin = min;
	}

	public void get_ymax() {
		double max = -100000;
		for (int i = 0; i < points.size(); i++) {
			if (points.get(i).getY() > max) {
				max = points.get(i).getY();
			}
		}
		ymax = max;
	}

	public void set_center() {
		center = new Point2d((xmax + xmin) / 2, (ymax + ymin) / 2);
	}

	// shape's transform
	// quick hack, get and set would be better
	float scale = 1.0f;

	// some optimization to cache points for drawing
	Boolean pointsChanged = false; // dirty bit
	int[] xpoints, ypoints;
	int npoints = 0;

	void cachePointsArray() {
		xpoints = new int[points.size()];
		ypoints = new int[points.size()];
		for (int i = 0; i < points.size(); i++) {
			xpoints[i] = (int) points.get(i).getX();
			ypoints[i] = (int) points.get(i).getY();
		}
		npoints = points.size();
		pointsChanged = false;
	}

	public void draw(Graphics2D g2) {
		// don't draw if points are empty (not shape)
		if (points == null)
			return;
		get_xmin();
		get_ymin();
		get_xmax();
		get_ymax();
		set_center();

		// see if we need to update the cache
		if (pointsChanged)
			cachePointsArray();
		// save the current g2 transform matrix
		AffineTransform M = g2.getTransform();
		// multiply in this shape's transform
		// (uniform scale)
		g2.translate(center.getX(), center.getY());
		g2.rotate(Math.toRadians(rotate));
		g2.scale(scale, scale);
		g2.translate((-1) * center.getX(), (-1) * center.getY());
		// save the modified g2 transform matrix
		M1 = g2.getTransform();

		// call drawing functions
		g2.setColor(colour);
		if (isFilled) {
			g2.fillPolygon(xpoints, ypoints, npoints);
		} else {
			// can adjust stroke size using scale
			g2.setStroke(new BasicStroke(strokeThickness / scale));
			if (isClosed) {
				g2.drawPolygon(xpoints, ypoints, npoints);
			} else {
				g2.drawPolyline(xpoints, ypoints, npoints);
				// if select, highlight this shape
				if (select) {
					setStrokeThickness(6);
					g2.setStroke(new BasicStroke(strokeThickness / scale));
					g2.setColor(Color.YELLOW);
					g2.drawPolyline(xpoints, ypoints, npoints);
					setStrokeThickness(2);
					g2.setStroke(new BasicStroke(strokeThickness / scale));
					g2.setColor(Color.BLACK);
					g2.drawPolyline(xpoints, ypoints, npoints);
				}
			}
		}
		// reset the transform to what it was before we drew the shape
		g2.setTransform(M);
	}

	static Point2d closestPoint(Point2d m, Point2d P0, Point2d P1) {
		Vector2d v = new Vector2d();
		v.sub(P1, P0); // v = P2 - P1

		// early out if line is less than 1 pixel long
		if (v.lengthSquared() < 0.5)
			return P0;

		Vector2d u = new Vector2d();
		u.sub(m, P0); // u = M - P1

		// scalar of vector projection ...
		double s = u.dot(v) / v.dot(v);

		// find point for constrained line segment
		if (s < 0)
			return P0;
		else if (s > 1)
			return P1;
		else {
			Point2d I = P0;
			Vector2d w = new Vector2d();
			w.scale(s, v); // w = s * v
			I.add(w); // I = P1 + w
			return I;
		}
	}

	// let shape handle its own hit testing
	// (x,y) is the point to test against
	// (x,y) needs to be in same coordinate frame as shape, you could add
	// a panel-to-shape transform as an extra parameter to this function
	// (note this isn't good separation of shape Controller from shape Model)
	public boolean hittest(double x, double y) {
		Point2d M = new Point2d(x, y);
		Point2d c;
		try {
			Point2D dst = new Point2D.Double(x,y);
			Point2D src = new Point2D.Double(x,y);
			M1.inverseTransform(src, dst);
			M = new Point2d(dst.getX(), dst.getY());
		} catch (NoninvertibleTransformException ex) {
			M = new Point2d(x, y);
		}
		if (points != null) {
			for (int i = 0; i < points.size() - 1; ++i) {
				c = closestPoint(M, points.get(i), points.get(i + 1));
				if (M.distance(c) <= 5) {
					select = true;
					return true;
				}
			}
		}
		select = false;
		return false;
	}
}