 /**********************************************************************
  * Scene3-Scene class which represents Boxex with the 
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

public class Scene3 extends Shape3D {

	public Scene3() {

		// TODO Auto-generated method stub
		// ArrayList<Shape3D> objectss = new ArrayList<Shape3D>();
		
		// Matrix to pass to the setRotate method.
		// Creates shapes of different pyramids and box types.
		Matrix3f matrix = new Matrix3f(0, -1, 0, 1, 0, 0, 0, 0, 1);
		
		Pyramid pyramid = new Pyramid();
		pyramid.setSize(0.2f, 0.2f, 0.2f);
		pyramid.setLocation(0, 0, 0);
		pyramid.setColor(0, 0, 1);
		pyramid.setRotate(130, 0.0f, 0.0f, 1.0f);
		objectss.add(pyramid);

		pyramid = new Pyramid();
		pyramid.setSize(0.1f, 0.2f, 0.1f);
		pyramid.setLocation(2.5f, -1.9f, 1.5f);
		pyramid.setColor(1, 0, 1);
		pyramid.setRotate(matrix);
		objectss.add(pyramid);

		Box box = new Box();
		box.setColor(1, 1, 0);
		box.setSize(0.2f, 0.2f, 0.1f);
		box.setLocation(-1.5f, 1.9f, 0.5f);
		box.setRotate(matrix);
		objectss.add(box);

	}
}