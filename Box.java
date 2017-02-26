
/**
 * Box.java - a class implementation representing a Box object



 *           in OpenGL
 * Oct 16, 2013
 * rdb - derived from Box.cpp
 * 
 * 10/28/14 rdb - revised to explicitly draw faces
 *              - drawPrimitives -> drawObject( GL2 )
 *              - uses glsl
 * 11/10/14 rdb - existing rebuilds glsl buffers on every redraw.
 *                should and canonly do it once.
 * 02/23/16 Nithin - Modified by adding color buffers inorder to
 * 					represent different coors for the different
 * 					faces. 
 * 				- The center of the pyramid should be the origin.
 * 				- COLOR BUFFER - is created for the each set of the vertice
 * 					and their face and the information is sent to the 
 * 					GPU via uniform color variable.
 * 
 **************************************************************************/


import static org.lwjgl.opengl.GL11.*;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.*;

import org.lwjgl.system.MemoryUtil;

public class Box extends Shape3D {
	// --------- instance variables -----------------
	final private int coordSize = 3;

	// vertex coordinates
	private float[] verts = {

			// 3-element vertex coordinates;
			// 3 letter codes are cube corners [lr][bt][nf] left/right
			// bottom/top near/far
			// right face 2 triangles: rbn, rbf, rtf and rbn, rtf, rtn
			0.5f, -0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f,
			0.5f,
			// top face: ltn, rtn, rtf and ltn, rtf, ltf
			-0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f,
			-0.5f,
			// back face: rbf, lbf, ltf and rbf, ltf, rtf
			0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f,
			0.5f, -0.5f,
			// left face: lbf, lbn, ltn and lbf, ltn, ltf -- corrected
			-0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f,
			0.5f, -0.5f,
			// bottom face: lbf, rbf, rbn and lbf, rbn, lbn
			-0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, -0.5f,
			-0.5f, 0.5f,
			// front face 2 triangles: lbn, rbn, rtn and lbn, rtn, ltn
			-0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f,
			0.5f,

	};

	/***************************************************************************
	 * Color Buffer - created array of colors to represent the colors for 
	 * 				  each of the faces.
	 * 				- Color buffer takes color values and passed to the setColor
	 * 					method of the Shape3D 
	 *************************************************************************/

	// color coordinates
	// Color values for each faces line by line starting from front face
	private float[] color_buffer = { 
			
			0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1,
			// top face: ltn, rtn, rtf and ltn, rtf, ltf
			1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1,
			// back face: rbf, lbf, ltf and rbf, ltf, rtf
			-1, 0, 0.5f, -1, 0, 0.5f, -1, 0, 0.5f, -1, 0, 0.5f, -1, 0, 0.5f, -1, 0, 0.5f,
			// left face: lbf, lbn, ltn and lbf, ltn, ltf -- corrected
			1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0,
			// bottom face: lbf, rbf, rbn and lbf, rbn, lbn
			0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0,
			// front face 2 triangles: lbn, rbn, rtn and lbn, rtn, ltn
			1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, };

	// ------------- constructor -----------------------
	/**
	 * Construct the data for this box object.
	 */
	public Box() {
		colorFaces = false;		 // Bolean set it to false
		setCoordData(verts, 3);	 // pass information to the coordBuffer
		setNormalData(verts, 3); // Normals are same as vertices!
		setColorData(color_buffer, 3); // ColorBuff 

	}

}
