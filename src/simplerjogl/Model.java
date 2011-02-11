
package simplerjogl;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;

import com.sun.opengl.util.gl2.*;

/**
 * A container class for model descriptions
 * 
 * @author <a href="mailto:seth@battis.net">Seth Battis</a>
 * @version 2011-02-08
 */
public abstract class Model extends SimplerJOGLObject
{
	/**
	 * GLU utility calls are supported within this model
	 */
	/* FIXME this should really be static */
	protected GLU glu;

	/**
	 * GLUT utility toolbox calls are supported within this model
	 */
	 /* FIXME this should really be static */
	protected GLUT glut;

	/**
	 * A number of default materials of generic colors are supported within
	 * this model
	 */
	/* FIXME these should be static and defined in Material */
	protected Material red, orange, yellow, green, cyan, blue, purple, magenta, white, black;

	/**
	 * Default constructor
	 * 
	 * @param gl
	 *            OpenGL drawing context for the model
	 */
	public Model (GL2 gl)
	{
		super(gl);
		
		/* FIXME this is a memory-hog maneuver -- these should be static */
		glu = new GLU ();
		glut = new GLUT ();

		red = new Material (gl);
		red.setDiffuse (1, 0, 0, 1);

		orange = new Material (gl);
		orange.setDiffuse (1, 0.5f, 0, 1);

		yellow = new Material (gl);
		yellow.setDiffuse (1, 1, 0, 1);

		green = new Material (gl);
		green.setDiffuse (0, 1, 0, 1);

		cyan = new Material (gl);
		cyan.setDiffuse (0, 1, 1, 1);

		blue = new Material (gl);
		blue.setDiffuse (0, 0, 1, 1);

		purple = new Material (gl);
		purple.setDiffuse (1, 0, 0.5f, 1);

		magenta = new Material (gl);
		magenta.setDiffuse (1, 0, 1, 1);

		white = new Material (gl);
		white.setDiffuse (1, 1, 1, 1);

		black = new Material (gl);
		black.setDiffuse (0, 0, 0, 1);
	}

	/**
	 * Copy constructor
	 * 
	 * @param other
	 *            - another initialized model
	 */
	public Model (Model other)
	{
		super(other);
		
		/* FIXME these should be static */
		this.glu = other.glu;
		this.glut = other.glut;

		/*
		 * TODO Is this really the most efficient way to generate these
		 * materials?
		 */
		this.red = new Material (other.red);
		this.orange = new Material (other.orange);
		this.yellow = new Material (other.yellow);
		this.green = new Material (other.green);
		this.cyan = new Material (other.cyan);
		this.blue = new Material (other.blue);
		this.purple = new Material (other.purple);
		this.magenta = new Material (other.magenta);
		this.white = new Material (other.white);
		this.black = new Material (other.black);
	}

	/**
	 * Any object that extends Model must implement a draw() method that
	 * will make the OpenGL calls necessary to draw the model. The draw()
	 * method is typically called within the JOGLRenderer display() method,
	 * called whenever the OpenGL drawing context is refreshed.
	 */
	public abstract void draw ();
}
