/***************************************************************************
 * Scene_Box. Java - Scene class which represents Bosex with the 
 *						rotations performed.
 * 					- SetColor, SetLocation, setSize and SetRotate are 
 * 						used to the position and translate
 * 					- Shape transformation is done in the shape when 
 * 						made a call to the setRotate(Matrix ) method or
 * 						setRotate(f,f,f,f) method
 * 					- Scene class extends Shape3D method, which is called 
 * 						by makeScene Method of the Scene_Manager Class
 * 					
 * *************************************************************************/
import java.util.ArrayList;

public class Scene_box extends Shape3D {
	// Scene_box constructor 
	// passed in are values to generate the object Box
	// Shapes are all added to array list objectss
	public Scene_box() {
		// TODO Auto-generated method stub
		// Box Object is being created  at the origin location
		Box box = new Box();
		box.setLocation(0, 0, 0);
		box.setSize(0.5f, 0.5f, 0.5f);
		box.setRotate(90, 1.0f, 0.0f, 0.0f);
		box.setColor(1, 0, 1);
		objectss.add(box);
		
		// Box shapes with different rotation being performed
		box = new Box();
		box.setLocation(0.9f, 1.5f, 0.7f);
		box.setSize(0.2f, 0.4f, 0.2f);
		box.setRotate(120, 0.0f, 0.0f, 1.0f);
		objectss.add(box);

		box = new Box();
		box.setLocation(2.5f, 1.5f, 1);
		box.setSize(0.2f, 0.4f, 0.2f);
		box.setColor(1, 1, 0);
		box.setRotate(180, 1f, 0f, 0f);
		objectss.add(box);

		box = new Box();
		box.setLocation(-2.5f, -1.5f, 1);
		box.setSize(0.1f, 0.3f, 0.2f);
		box.setRotate(180, 1f, 0f, 0f);
		box.setColor(0, 1, 1);
		objectss.add(box);

	}
}
