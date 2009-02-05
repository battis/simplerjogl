
package simplerjogl;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import simplerjogl.shell.*;
import com.sun.opengl.util.*;
import java.awt.event.*;

/**
 * A GLEventListener class -- that is, a class that is capable of drawing
 * to a GL context.
 * 
 * @author Seth Battis
 * @version 2008-11-13
 */
public class Renderer implements GLEventListener, KeyListener, MouseListener, MouseMotionListener, ShellListener
{
	/**
	 * The current GL drawing context
	 */
	protected GL gl;
	/**
	 * GLU utilities instance associated with this GL drawing context
	 */
	protected GLU glu = new GLU ();
	/**
	 * GLUT utilities instance associated with this GL drawing context
	 */
	protected GLUT glut = new GLUT ();
	/**
	 * A reference to a potential terminal pane
	 */
	protected Shell shell;
	/**
	 * Are we showing the x, y and z axes?
	 */
	private boolean axes, hashes;
	private double axisLength;
	private Material xAxis, yAxis, zAxis, defaultMaterial;

	protected void updateGL (GLAutoDrawable glDrawable)
	{
		/* the specific GL canvas in which we are drawing */
		if (glDrawable.getGL () != gl)
		{
			gl = glDrawable.getGL ();
		}
	}

	/**
	 * Called by the GL canvas each time a new frame is required. If other
	 * GLEventListeners exist, they will also be notified and the GL
	 * drawing context will swap buffers if necessary
	 * 
	 * @param glDrawable
	 *            the GL drawing context
	 */
	public void display (GLAutoDrawable glDrawable)
	{
		updateGL (glDrawable);
		/* clear the screen and reset our transformation matrix */
		gl.glClear (GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity ();
		defaultMaterial.use ();
		display ();
		if (axes)
		{
			gl.glPushMatrix ();
			{
				/* X-axis */
				gl.glLineWidth (3);
				gl.glBegin (GL.GL_LINES);
				{
					xAxis.use ();
					gl.glVertex3d (-axisLength, 0, 0);
					gl.glVertex3d (axisLength, 0, 0);
					yAxis.use ();
					gl.glVertex3d (0, -axisLength, 0);
					gl.glVertex3d (0, axisLength, 0);
					zAxis.use ();
					gl.glVertex3d (0, 0, -axisLength);
					gl.glVertex3d (0, 0, axisLength);
				}
				gl.glEnd ();
				xAxis.use ();
				gl.glPushMatrix ();
				{
					gl.glTranslated (axisLength, 0, 0);
					gl.glRotated (90, 0, 1, 0);
					glut.glutSolidCone (0.0625, 0.25, 10, 1);
				}
				gl.glPopMatrix ();
				yAxis.use ();
				gl.glPushMatrix ();
				{
					gl.glTranslated (0, axisLength, 0);
					gl.glRotated (90, -1, 0, 0);
					glut.glutSolidCone (0.0625, 0.25, 10, 1);
				}
				gl.glPopMatrix ();
				zAxis.use ();
				gl.glPushMatrix ();
				{
					gl.glTranslated (0, 0, axisLength);
					glut.glutSolidCone (0.0625, 0.25, 10, 1);
				}
				gl.glPopMatrix ();
				if (hashes)
				{
					xAxis.use ();
					for (int i = ((int) axisLength) * -1; i < axisLength; i++ )
					{
						gl.glPushMatrix ();
						{
							gl.glTranslated (i, 0, 0);
							gl.glRotated (90, 0, 1, 0);
							glut.glutSolidCylinder (0.075, 0.0312, 10, 1);
						}
						gl.glPopMatrix ();
					}
					yAxis.use ();
					for (int i = ((int) axisLength) * -1; i < axisLength; i++ )
					{
						gl.glPushMatrix ();
						{
							gl.glTranslated (0, i, 0);
							gl.glRotated (90, 1, 0, 0);
							glut.glutSolidCylinder (0.075, 0.0312, 10, 1);
						}
						gl.glPopMatrix ();
					}
					zAxis.use ();
					for (int i = ((int) axisLength) * -1; i < axisLength; i++ )
					{
						gl.glPushMatrix ();
						{
							gl.glTranslated (0, 0, i);
							glut.glutSolidCylinder (0.075, 0.0312, 10, 1);
						}
						gl.glPopMatrix ();
					}
				}
			}
			gl.glPopMatrix ();
		}
		gl.glFlush ();
	}

	/**
	 * Called by the GL canvas each time a new frame is required. If other
	 * GLEventListeners exist, they will also be notified and the GL
	 * drawing context will swap buffers if necessary
	 */
	public void display ()
	{}

	/**
	 * Called when the display mode has been changed -- this is included
	 * for consistency with standard OpenGL, although it is a feature
	 * currently unimplemented in JOGL, so...
	 */
	public void displayChanged (GLAutoDrawable glDrawable, boolean modeChanged, boolean deviceChanged)
	{}

	/**
	 * Called by the canvas immediately after the OpenGL drawing context is
	 * initialized for the first time. Used for one-time initialization
	 * (such as setting up lights and display lists)
	 * 
	 * @param glDrawable
	 *            the GL drawing context
	 */
	public void init (GLAutoDrawable glDrawable)
	{
		updateGL (glDrawable);
		/* enable smooth shading so that our round objects appear round */
		gl.glShadeModel (GL.GL_SMOOTH);
		/* make sure that any vectors we define are normal vectors */
		gl.glEnable (GL.GL_NORMALIZE);
		/* make our background black */
		gl.glClearColor (0, 0, 0, 0);
		/* turn on depth testing so that our objects will overlap properly */
		gl.glEnable (GL.GL_DEPTH_TEST);
		/* create a dummy material to set color/material values by default */
		defaultMaterial = new Material (gl);
		axes = false;
		hashes = true;
		axisLength = 1;
		xAxis = new Material (gl);
		xAxis.setDiffuse (1, 0, 0, 1);
		yAxis = new Material (gl);
		yAxis.setDiffuse (1, 1, 0, 1);
		zAxis = new Material (gl);
		zAxis.setDiffuse (0, 0, 1, 1);
		init ();
	}

	/**
	 * Called by the canvas immediately after the OpenGL drawing context is
	 * initialized for the first time. Used for one-time initialization
	 * (such as setting up lights and display lists)
	 */
	public void init ()
	{}

	/**
	 * Enable the display of the X, Y and Z axes in space X is red, Y is
	 * yellow, Z is blue, arrows point positive TODO still a bit of a hack,
	 * this depends on the gluLookAt() call from from the display() method,
	 * as well as all of the _other_ transformations in display() being
	 * isolated within glPushMatrix() and glPopMatrix().
	 */
	public void enableAxes ()
	{
		axes = true;
	}

	/**
	 * Enable hash marks along the axes at unit intervals
	 */
	public void enableHashes ()
	{
		hashes = true;
	}

	/**
	 * Disable the display of the X, Y and Z axes in space
	 */
	public void disableAxes ()
	{
		axes = false;
	}

	/**
	 * Disable hash marks along the axes
	 */
	public void disableHashes ()
	{
		hashes = false;
	}

	/**
	 * @param length
	 *            of the axes from the origin (must be > 0.5)
	 * @return old length of the axes
	 */
	public double setAxisLength (double length)
	{
		double oldLength = axisLength;
		axisLength = Math.max (length, 0.5);
		return oldLength;
	}

	/**
	 * Called by the GL canvas during the first repaint after the canvas
	 * has been resized. This is an opportunity to update the viewport and
	 * view volumes of the canvas to match the new size of the canvas. For
	 * convenience, the canvas has already called the GL.glViewport()
	 * method before this method is called, so we may not have to do
	 * anything!
	 * 
	 * @param glDrawable
	 *            the GL object in which we are drawing
	 * @param x
	 *            the x-coordinate of the viewport rectangle
	 * @param y
	 *            the y-coordinate of the viewport rectangle
	 * @param width
	 *            the new width of the canvas
	 * @param height
	 *            the new height of the canvas
	 */
	public void reshape (GLAutoDrawable glDrawable, int x, int y, int width, int height)
	{
		updateGL (glDrawable);
		/* avoid divide by zero errors on weird heights! */
		if (height <= 0)
		{
			height = 1;
		}
		/* calculate our new aspect ratio */
		float h = (float) width / (float) height;
		/* fill the canvas with our viewport */
		gl.glViewport (0, 0, width, height);
		/* continue to use 3-D projection to calculate our view volume */
		gl.glMatrixMode (GL.GL_PROJECTION);
		gl.glLoadIdentity ();
		/*
		 * recalculate our view volume to be a 45 degree angle with a
		 * minimum depth of field of 0.1 units in front of the eye and
		 * maximum depth of field of 100 units in front of the eye
		 */
		glu.gluPerspective (45, h, 0.1f, 100);
		/* we'd like to our transformations to be applied to our model */
		gl.glMatrixMode (GL.GL_MODELVIEW);
		gl.glLoadIdentity ();
	}

	public void keyPressed (KeyEvent e)
	{}

	public void keyReleased (KeyEvent e)
	{}

	public void keyTyped (KeyEvent e)
	{}

	public void mouseExited (MouseEvent e)
	{}

	public void mouseEntered (MouseEvent e)
	{}

	public void mouseReleased (MouseEvent e)
	{}

	public void mousePressed (MouseEvent e)
	{}

	public void mouseClicked (MouseEvent e)
	{}

	public void mouseMoved (MouseEvent e)
	{}

	public void mouseDragged (MouseEvent e)
	{}

	public void registerShell (Shell s)
	{
		shell = s;
	}

	public void commandComplete (ShellEvent e)
	{}
}