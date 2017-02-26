 /**********************************************************************
 * Scene_Box and pyramid -Scene class which represents Boxex with the 
 *						rotations performed.
 * 					- SetColor, SetLocation, setSize and SetRotate are 
 * 						used to the position and translate
 * 					- Shape transformation is done in the shape when 
 * 						made a call to the setRotate(Matrix ) method or
 * 						setRotate(f,f,f,f) method
 * 					- Scene class extends Shape3D method, which is called 
 * 						by makeScene Method of the Scene_Manager Class
 * **********************************************************************/
import java.util.ArrayList;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class Scene extends Shape3D {

	// passed in are values to generate the object Box
	// Shapes are all added to array list objectss
	public Scene() {

		// TODO Auto-generated method stub
		// Matrix defined inorder to pass it to the setRotate function in 
		// Shape3D class
		Matrix3f matrix = new Matrix3f(0, -1, 0, 1, -1, 0, 1, 1, 1); // Matrix for transformation
		Matrix3f matrix1 = new Matrix3f(1, 0, 0, 0, 1, 0, 0, 0, 1);
		
		// Pyramid shapes - Pyramid 1 at origin All faces coloured
		Pyramid pyramid = new Pyramid();
		pyramid.setSize(0.2f, 0.2f, 0.2f);
		pyramid.setLocation(0, 0, 0);
		pyramid.setColor(0, 1, 1);
		pyramid.setRotate(90, 0.0f, 0.0f, 1.0f);
		objectss.add(pyramid);
		
		// Pyramid Shapes - Pyramid 2 via Matrix method
		pyramid = new Pyramid();
		pyramid.setSize(0.1f, -0.2f, 0.1f);
		pyramid.setLocation(2.5f, 1.5f, 1.2f);
		pyramid.setColor(1, 0, 1);
		pyramid.setRotate(matrix);
		objectss.add(pyramid);
		
	}
}
