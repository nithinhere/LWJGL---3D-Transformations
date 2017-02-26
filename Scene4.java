 /**********************************************************************
  * Scene4 -Scene class which represents Boxex with the 
 *						rotations performed.
 * 					- SetColor, SetLocation, setSize and SetRotate are 
 * 						used to the position and translate
 * 					- Shape transformation is done in the shape when 
 * 						made a call to the setRotate(Matrix ) method or
 * 						setRotate(f,f,f,f) method
 * 					- Scene class extends Shape3D method, which is called 
 * 						by makeScene Method of the Scene_Manager Class
 * **********************************************************************/
import org.joml.Matrix3f;

public class Scene4 extends Shape3D {
	// Constructor data performs different pyramids and scenes
	// Scenemanager makescene ivokes Scene classes
	public Scene4() {

		// TODO Auto-generated method stub
		// ArrayList<Shape3D> objectss = new ArrayList<Shape3D>();
		// Matrix to pass the rotation parameters
		Matrix3f matrix = new Matrix3f(1, -1, 0, 1, 0, 0, 1, 1, 1);
		
		// Different pyramid shapes ivoke SetRotate method
		Pyramid pyramid = new Pyramid();
		pyramid.setSize(0.2f, 0.2f, 0.2f);
		pyramid.setLocation(1, 0, 1);
		pyramid.setRotate(90, 0.0f, 0.0f, 1.0f);
		objectss.add(pyramid);
		
		// Pyramid shape invoke setRotate method using matrix
		pyramid = new Pyramid();
		pyramid.setSize(0.2f, 0.2f, 0.2f);
		pyramid.setLocation(0,0,1.5f);
		pyramid.setColor(-1, -0.2f, -1);
		pyramid.setRotate(matrix);
		objectss.add(pyramid);
		
		// Box method takes matrix as argument
		Box box = new Box();
		box.setColor(1, 1, 1);
		box.setSize(0.2f, 0.2f, 0.2f);
		box.setLocation(1.8f, -1.4f, 1.0f);
		box.setRotate(matrix);
		objectss.add(box);

	}
}