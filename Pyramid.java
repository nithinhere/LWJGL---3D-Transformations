/**************************************************************************
 * Pyramid.java - a class implementation representing a Pyramid object 
 * 				- The center of the pyramid should be the origin.
 * 				- COLOR BUFFER - is created for the each set of the vertice
 * 					and their face and the information is sent to the 
 * 					GPU via uniform color variable.
 * 
 **************************************************************************/
public class Pyramid extends Shape3D {
	// --------- instance variables -----------------
	final private int coordSize = 3;

	// vertex coordinates
	private float[] verts = {
			// Front Face of the triangle
			0, -1, -1, 		0, -1, 1, 		0, 1, 1,
			// Right Face
			0,1, 1, 		0, 1, -1,		0, -1, -1,
			// Left Face
			0, -1, -1, 		0, -1, 1,       2, 0, 0, 
			// Bottom Face
			0, -1, 1, 		0, 1, 1,		2, 0, 0,
			// Back Face
			0, 1, 1, 		0, 1, -1, 		2, 0, 0,	
			0, 1, -1, 		0, -1, -1, 		2, 0, 0				

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
			
			0, 1, 1, 		0, 1, 1, 		0, 1, 1,
			0, 0, 1, 		0, 0, 1,		0, 0, 1,
			1, 0, 1, 		1, 0, 1,        1, 0, 1, 
			1, 1, 0, 		1, 1, 0,		1, 1, 0, 		
			0, 1, 1, 		0, 1, 1, 		0, 1, 1,	
			1, 0, 0, 		1, 0, 0, 		1, 0, 0
	};
	
	/**
	 * Construct the data for this box object.
	 */
	public Pyramid() {
		colorFaces = false;		 // Bolean set it to false
		setCoordData(verts, 3);	 // pass information to the coordBuffer
		setNormalData(verts, 3); // Normals are same as vertices!
		setColorData(color_buffer, 3); // ColorBuff
	}

}
