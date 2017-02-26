import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

/**
 * SceneManager - create Scenes, manage scene displays, setup view and
 * projection matrices, handle interaction.
 * 
 * @author rdb 02/07/17
 */
public class SceneManager {
	// ------------------- class variables ---------------------------
	// ----- I've chosen to make the shaderProgram identifier a static "public"
	// variable. This could become a problem for more complex programs
	// that use multiple shader programs.
	static int shaderProgram = -1; //
	// ------------------- instance variables ---------------------------
	private long window; // GLFW window id

	// storage for shapes created
	ArrayList<Shape3D> objects = new ArrayList<Shape3D>();
	// Array for creating storing the scene
	ArrayList<Shape3D> scene_array = new ArrayList<Shape3D>();

	// We need to reference callback instances.
	private GLFWKeyCallback keyCallback;

	// -------------- View and Scene transformation parameters
	private Matrix4f projMatrix = new Matrix4f();
	private Matrix4f projXsceneMatrix = new Matrix4f();

	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f sceneMatrix = new Matrix4f();
	// initial scene parameters will be used to build sceneMatrix
	private float radiansX = 0;
	private float radiansY = 0;
	private float radiansZ = 0;
	private float deltaRotate = 0.1f;
	private float sceneScale = 0.5f;

	private boolean usePerspective = false;
	// Scene class instance variables
	Scene scene = new Scene();
	Scene_box scene_box = new Scene_box();
	Scene3 scene3 = new Scene3();

	// buffers
	private FloatBuffer projXsceneBuf;
	public int period_key_increment = 0;
	public int comma_key_increment = 0;
	public int index = 0;
	public float light_intensity;
	public float light_initial = 0;
	public int ka1 = 4;
	public int kd1 = 4;

	// ------------------- Constructor ------------------------------
	/**
	 * Constructor manages everything.
	 * 
	 * @param windowId
	 *            long window identifier
	 * @param shaderProgram
	 *            int identifier for the shader program
	 */
	public SceneManager(long windowId, int shader) {

		window = windowId; // save it.
		shaderProgram = shader; // save it

		projMatrix.scale(sceneScale); // initial matrix is uniform scale by 1/2
		projXsceneBuf = MemoryUtil.memAllocFloat(16);

		ka1 = glGetUniformLocation(shader, "ka");
		kd1 = glGetUniformLocation(shader, "kd");

		setupKeyHandler();

		makeScene();
		setupView();
		setupProjection();
		updateScene();

		renderLoop();

	}

	// ----------------------- finalize -------------------------------
	/**
	 * finalize - needs to free the non-java-GC memory allocated by lwjgl
	 * MemoryUtil class
	 */
	public void finalize() {
		MemoryUtil.memFree(projXsceneBuf);
	}

	// ------------------ makeScene --------------------------
	/**
	 * Create the objects that make up the scene. Call to the scenes by adding
	 * it to the scene class Scene are instantiated and are invoked during key
	 * press
	 */
	public void makeScene() {

		scene_array.add(new Scene()); // Scene1
		scene_array.add(new Scene_box()); // Scene 2
		scene_array.add(new Scene3()); // Scene 3
		scene_array.add(new Scene4()); // Scene 4

	}

	// -------------------------- renderLoop ----------------------------
	/**
	 * Loop until user closes the window or kills the program.
	 */
	private void renderLoop() {
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glEnable(GL_DEPTH_TEST);
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while (glfwWindowShouldClose(window) == false) {
			// clear the framebuffer
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			// redraw the frame
			redraw();

			glfwSwapBuffers(window); // swap the color buffers

			// Wait for window events. The key callback above will only be
			// invoked during this call.
			// lwjgl demos use glfwPollEvents(), which uses nearly 2X
			// the cpu time for simple demos as glfwWaitEvents.
			glfwWaitEvents();
		}
	}

	// --------------------- setupView ----------------------
	/**
	 * Set up default View specification using lookAt
	 */
	private void setupView() {
		Vector3f eye = new Vector3f(0, 0, 1);
		Vector3f center = new Vector3f(0, 0, 0);
		Vector3f up = new Vector3f(0, 1, 0);
		viewMatrix.lookAt(eye, center, up);
	}

	// --------------------- setupProjection ----------------------
	/**
	 * Define the projection parameters; this is needed because defaults are
	 * very unfriendly, particularly the near/far clipping parameters.
	 */
	private void setupProjection() {
		// -------- ortho parameters
		float left = -1;
		float right = 1;
		float bottom = -1;
		float top = 1;
		float nearZ = 0.1f;
		float farZ = 10;
		// -------- perspective parameters
		float fovy = 90;
		float aspect = 1;

		projMatrix.identity();
		if (usePerspective)
			projMatrix.perspective(fovy, aspect, nearZ, farZ);
		else
			projMatrix.ortho(left, right, bottom, top, nearZ, farZ);
	}

	// ------------------ updateTransforms --------------------------
	/**
	 * We have a constant viewing and projection specification. Can define it
	 * once and send the spec to the shader.
	 */
	void updateTransforms() {
		// ----- compute the composite
		projXsceneMatrix.set(projMatrix);
		projXsceneMatrix.mul(viewMatrix);
		projXsceneMatrix.mul(sceneMatrix); // multiply by scene

		// get stores this matrix into buffer parameter and returns buffer
		projXsceneBuf = projXsceneMatrix.get(projXsceneBuf);

		// --- now push the composite into a uniform var in vertex shader
		// this id does not need to be global since we never change
		// projection or viewing specs in this program.
		int unif_pXv = glGetUniformLocation(shaderProgram, "projXview");

		glUniformMatrix4fv(unif_pXv, false, projXsceneBuf);
	}

	// ------------------ updateScene --------------------------
	/**
	 * We have a constant viewing and projection specification. Can define it
	 * once and send the spec to the shader.
	 */
	void updateScene() {
		sceneMatrix.identity();
		sceneMatrix.rotateX(radiansX).rotateY(radiansY).rotateZ(radiansZ);
		updateTransforms();
	}

	// ------------------------ redraw() ----------------------------
	// Redraw - loops throught the objects through the indexes.
	void redraw() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		// iterate through indexes
		for (Shape3D obj : scene_array.get(index).objectss)
			obj.redraw();
		// Uniform location in order to send to the shader code to alter the
		// light
		// factor
		glUniform1f(ka1, light_intensity);
		glUniform1f(kd1, light_intensity);

		glFlush();

	}
	/*
	 * SceneMaker class to assign the lighting variables to the unif value
	 * factors. altering the intensity to produce different light effects
	 */

	// --------------------- setupKeyHandler ----------------------
	/**
	 * void setupKeyHandler
	 */
	private void setupKeyHandler() {
		// Setup a key callback. It is called every time a key is pressed,
		// repeated or released.
		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long keyWindow, int key, int scancode, int action, int mods) {
				keyHandler(keyWindow, key, scancode, action, mods);
			}
		});
	}

	// --------------------- keyHandler ---------------------------
	/**
	 * Make this a full-fledged method called from the invoke method of the
	 * anonymous class created in setupKeyHandler.
	 * 
	 * @param long
	 *            window window Id
	 * @param int
	 *            key key code
	 * @param int
	 *            code "scancode" is low-level non-standard internal code
	 * @param int
	 *            action GLFW_PRESS or GLFW_RELEASE
	 * @param int
	 *            mods bits in int encode modifier keys pressed GLFW_MOD_ALT |
	 *            GLFW_MOD_SHIFT | GLFW_MOD_CONTROL | GLFW_MOD_SUPER (cmd on
	 *            mac)
	 */
	private void keyHandler(long window, int key, int code, int action, int mods) {
		switch (key) {
		// ------------ Perspective/Parallel projection toggle -----------------

		case GLFW_KEY_P:
			if (action == GLFW_RELEASE) // use release so user can change mind
				usePerspective = !usePerspective;
			setupProjection();
			updateTransforms();
			break;
		// ------------ Polygon Line draw mode ---------------------------
		case GLFW_KEY_L:
			if (action == GLFW_RELEASE) // use release so user can change mind
				glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
			break;
		// ------------ Polygon fill draw mode ---------------------------
		case GLFW_KEY_F:
			if (action == GLFW_RELEASE) // use release so user can change mind
				glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
			break;
		// ------------ Exit program -------------------
		// Either q or Esc keys quit
		case GLFW_KEY_Q:
		case GLFW_KEY_ESCAPE:
			// this is another exit key
			if (action == GLFW_RELEASE) // use release so user can change mind
				glfwSetWindowShouldClose(window, true);
			break;
		// ----------- any other keys must be rotation keys or invalid
		case GLFW_KEY_COMMA:
			// Key , or < pressed to go to the next scene
			if (action == GLFW_RELEASE) { // use release so user can change mind
				index--;
				if (index < 0)
					index = 3;
			}
			if (index == 0) {
				glfwSetWindowTitle(window, "Pyramid Object");
			}
			if (index == 1) {
				glfwSetWindowTitle(window, "Box with  rotation ");
			}

			if (index == 2) {
				glfwSetWindowTitle(window, "Pyramid and Box which satisfies rotation around x, y, z axes ");
			}

			if (index == 3) {
				glfwSetWindowTitle(window, "Scene 4 - Pyramid and Box ");
			}

			break;
		case GLFW_KEY_PERIOD:
			// keyword . or > is pressed to go to the previous scene in circular
			// fashion

			if (action == GLFW_RELEASE) { // use release so user can change mind
				index++;
				if (index > 3)
					index = 0;

			}
			if (index == 0) {
				glfwSetWindowTitle(window, "Pyramid Object");
			}
			if (index == 1) {
				glfwSetWindowTitle(window, "Box Object rotation representation  ");
			}

			if (index == 2) {
				glfwSetWindowTitle(window, "Pyramid and Box which satisfies rotation around x, y, z axes");
			}

			if (index == 3) {
				glfwSetWindowTitle(window, " Scene 4 - Pyramid and Box");
			}

			break;

		default:
			rotationKeyHandler(key, action, mods);

		}
	}

	// --------------------- rotationKeyHandler ------------------------------
	/***
	 * Handle key events that specify rotations.
	 * 
	 * @param int
	 *            key key code
	 * @param int
	 *            action GLFW_PRESS or GLFW_RELEASE
	 * @param int
	 *            mods bits in int encode modifier keys pressed GLFW_MOD_ALT |
	 *            GLFW_MOD_SHIFT | GLFW_MOD_CONTROL | GLFW_MOD_SUPER (cmd on
	 *            mac)
	 ***/
	private void rotationKeyHandler(int key, int action, int mods) {
		switch (key) {
		// ------------ Rotations about X axis -------------------
		// Use x, X and UP DOWN keys
		case GLFW_KEY_X:
			if (action == GLFW.GLFW_REPEAT || action == GLFW.GLFW_PRESS) {
				if ((GLFW_MOD_SHIFT & mods) == 0) // it's lower case
					radiansX += deltaRotate;
				else
					radiansX -= deltaRotate;
				updateScene();
			}
			break;
		case GLFW_KEY_UP:
			if (action == GLFW.GLFW_REPEAT || action == GLFW.GLFW_PRESS) {
				radiansX += deltaRotate;
				updateScene();
			}
			break;
		case GLFW_KEY_DOWN:
			if (action == GLFW.GLFW_REPEAT || action == GLFW.GLFW_PRESS) {
				radiansX -= deltaRotate;
				updateScene();
			}
			break;

		case GLFW_KEY_1:
			if (action == GLFW.GLFW_REPEAT || action == GLFW.GLFW_PRESS) {
				if (light_initial == 0) {
					light_intensity = 0.9f;
				} else {
					light_intensity = 0.0f;
				}
				updateScene();
			}
			break;
		// ------------ Rotations about Y axis -------------------
		// Use y, Y and RIGHT, LEFT keys
		case GLFW_KEY_Y:
			if (action == GLFW.GLFW_REPEAT || action == GLFW.GLFW_PRESS) {
				if ((GLFW_MOD_SHIFT & mods) == 0) // it's lower case
					radiansY += deltaRotate;
				else
					radiansY -= deltaRotate;
				updateScene();
			}
			break;
		case GLFW_KEY_RIGHT:
			if (action == GLFW.GLFW_REPEAT || action == GLFW.GLFW_PRESS) {
				radiansY += deltaRotate;
				updateScene();
			}
			break;
		case GLFW_KEY_LEFT:
			if (action == GLFW.GLFW_REPEAT || action == GLFW.GLFW_PRESS) {
				radiansY -= deltaRotate;
				updateScene();
			}
			break;
		// ------------ Rotations about Z axis -------------------
		// Only have z and Z keys
		case GLFW_KEY_Z:
			if (action == GLFW.GLFW_REPEAT || action == GLFW.GLFW_PRESS) {
				if ((GLFW_MOD_SHIFT & mods) == 0) // it's lower case
					radiansZ += deltaRotate;
				else
					radiansZ -= deltaRotate;
				updateScene();
			}
			break;
		}
	}
}
