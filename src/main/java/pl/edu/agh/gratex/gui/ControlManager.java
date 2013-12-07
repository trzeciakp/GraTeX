package pl.edu.agh.gratex.gui;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import pl.edu.agh.gratex.editor.AddOperation;
import pl.edu.agh.gratex.editor.CopyPasteOperation;
import pl.edu.agh.gratex.editor.DragOperation;
import pl.edu.agh.gratex.editor.OperationList;
import pl.edu.agh.gratex.editor.PropertyChangeOperation;
import pl.edu.agh.gratex.editor.RemoveOperation;
import pl.edu.agh.gratex.editor.TemplateChangeOperation;
import pl.edu.agh.gratex.graph.Edge;
import pl.edu.agh.gratex.graph.Graph;
import pl.edu.agh.gratex.graph.GraphElement;
import pl.edu.agh.gratex.graph.LabelE;
import pl.edu.agh.gratex.graph.LabelV;
import pl.edu.agh.gratex.graph.Vertex;
import pl.edu.agh.gratex.model.EdgePropertyModel;
import pl.edu.agh.gratex.model.LabelEdgePropertyModel;
import pl.edu.agh.gratex.model.LabelVertexPropertyModel;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.VertexPropertyModel;


public class ControlManager
{
	private static MainWindow				mainWindow;

	public static File						currentFile;

	public static int						mode							= 1;
	public static int						tool							= 1;
	public static final int					VERTEX_MODE						= 1;
	public static final int					EDGE_MODE						= 2;
	public static final int					LABEL_V_MODE					= 3;
	public static final int					LABEL_E_MODE					= 4;
	public static final int					ADD_TOOL						= 1;
	public static final int					REMOVE_TOOL						= 2;
	public static final int					SELECT_TOOL						= 3;

	public static Graph						graph							= new Graph();
	public static Edge						currentlyAddedEdge				= null;
	public static LinkedList<GraphElement>	selection						= new LinkedList<GraphElement>();
	public static int						selectionID						= 0;
	public static OperationList				operations						= null;
	public static GraphElement				currentlyMovedElement			= null;
	public static DragOperation				currentDragOperation			= null;
	public static boolean					changingEdgeAngle;
	public static PropertyChangeOperation	currentPropertyChangeOperation	= null;
	public static CopyPasteOperation		currentCopyPasteOperation		= null;
	public static Vertex					edgeDragDummy					= null;
	public static boolean					changeMade						= false;

	public static boolean					shiftDown;
	public static boolean					mousePressed;
	public static int						mousePressX;
	public static int						mousePressY;
	public static int						mouseX;
	public static int						mouseY;

	public static void passWindowHandle(MainWindow _mainWindow)
	{
		mainWindow = _mainWindow;
	}

	public static void applyChange()
	{
		selection.clear();
		mainWindow.panel_propertyEditor.setMode(ControlManager.mode);
		updatePropertyChangeOperationStatus(false);
		finishMovingElement();
		currentlyMovedElement = null;
		currentDragOperation = null;
		currentlyAddedEdge = null;
		mainWindow.updateFunctions();
	}

	public static void changeMode(int _mode)
	{
		mode = _mode;
		applyChange();
	}

	public static void changeTool(int _tool)
	{
		tool = _tool;
		applyChange();
	}

	public static void publishInfo(String entry)
	{
		mainWindow.label_info.setText(entry);
	}

	public static void reportError(String message)
	{
		JOptionPane.showMessageDialog(mainWindow, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public static void selectAll()
	{
		if (tool == SELECT_TOOL)
		{
			selection.clear();
			if (mode == VERTEX_MODE)
			{
				selection.addAll(ControlManager.graph.getVertices());
			}
			else if (mode == EDGE_MODE)
			{
				selection.addAll(ControlManager.graph.getEdges());
			}
			else if (mode == LABEL_V_MODE)
			{
				selection.addAll(ControlManager.graph.getLabelsV());
			}
			else
			{
				selection.addAll(ControlManager.graph.getLabelsE());
			}
			updatePropertyChangeOperationStatus(true);
			mainWindow.updateWorkspace();
		}
	}

	public static void addToSelection(LinkedList<GraphElement> elements, boolean controlDown)
	{
		if (!controlDown)
		{
			selection.clear();
			selection.addAll(elements);
		}
		else
		{
			Iterator<GraphElement> it = elements.listIterator();
			GraphElement temp;
			while (it.hasNext())
			{
				temp = it.next();
				if (selection.contains(temp))
				{
					selection.remove(temp);
				}
				else
				{
					selection.add(temp);
				}
			}
		}

		updatePropertyChangeOperationStatus(true);
	}

	public static void addToSelection(GraphElement element, boolean controlDown)
	{
		if (element != null)
		{
			if (controlDown)
			{
				if (selection.contains(element))
				{
					selection.remove(element);
				}
				else
				{
					selection.add(element);
				}
			}
			else
			{
				selection.clear();
				selection.add(element);
			}
		}
		else
		{
			if (!controlDown)
			{
				selection.clear();
			}
		}

		updatePropertyChangeOperationStatus(true);
	}

	public static void deleteSelection()
	{
		if (selection.size() > 0)
		{
			operations.addNewOperation(new RemoveOperation(selection));
			publishInfo(operations.redo());
			mainWindow.updateFunctions();
		}
		selection.clear();
		updatePropertyChangeOperationStatus(false);
	}

	public static void updatePropertyChangeOperationStatus(boolean newSelection)
	{
		mainWindow.menuBar.updateFunctions();
		mainWindow.panel_buttonContainer.updateFunctions();

		if (selection.size() > 0)
		{
			if (newSelection)
			{
				selectionID++;
				mainWindow.panel_propertyEditor.setEnabled(true);
			}
			currentPropertyChangeOperation = new PropertyChangeOperation(selection, selectionID);
			mainWindow.panel_propertyEditor.setModel(currentPropertyChangeOperation.initialModel);
		}
		else
		{
			currentPropertyChangeOperation = null;
			mainWindow.panel_propertyEditor.setEnabled(false);
			if (mode == 1)
			{
				mainWindow.panel_propertyEditor.setModel(new VertexPropertyModel());
			}
			else if (mode == 2)
			{
				mainWindow.panel_propertyEditor.setModel(new EdgePropertyModel());
			}
			else if (mode == 3)
			{
				mainWindow.panel_propertyEditor.setModel(new LabelVertexPropertyModel());
			}
			else
			{
				mainWindow.panel_propertyEditor.setModel(new LabelEdgePropertyModel());
			}
		}
	}

	public static void updateSelectedItemsModel(PropertyModel pm)
	{
		if (currentPropertyChangeOperation != null)
		{
			if (!operations.mergePropertyChangeOperations(pm, selectionID))
			{
				currentPropertyChangeOperation.setEndModel(pm);
				operations.addNewOperation(currentPropertyChangeOperation);
				publishInfo(operations.redo());
			}
			mainWindow.updateWorkspace();
			updatePropertyChangeOperationStatus(false);
		}
	}

	public static void newGraphFile()
	{
		if (checkForUnsavedProgress())
		{
			graph = new Graph();
			operations = new OperationList();
			applyChange();
			changeMade = false;
			currentFile = null;
			editGraphTemplate();
		}
	}

	public static void openGraphFile()
	{
		if (checkForUnsavedProgress())
		{
			OpenFileDialog chooser = null;
			if (currentFile != null)
			{
				chooser = new OpenFileDialog(currentFile);
			}
			else
			{
				chooser = new OpenFileDialog();
			}
			chooser.showDialog(mainWindow);
		}
	}

	public static boolean saveGraphFile(boolean saveAs)
	{
		if (!saveAs && currentFile != null)
		{
			if (FileManager.saveFile(currentFile, graph))
			{
				publishInfo("Graph saved successfully");
				return true;
			}
			else
			{
				publishInfo("Saving graph failed!");
				reportError("Saving graph failed.\nCheck whether the target file is write-protected.");
				return false;
			}
		}
		else
		{
			SaveFileDialog chooser = null;
			if (currentFile != null)
			{
				chooser = new SaveFileDialog(currentFile);
			}
			else
			{
				chooser = new SaveFileDialog();
			}
			return chooser.showDialog(mainWindow);
		}
	}

	public static void undo()
	{
		publishInfo(operations.undo());
		mainWindow.updateWorkspace();
		selection.clear();
		updatePropertyChangeOperationStatus(false);
		changeMade = true;
	}

	public static void redo()
	{
		publishInfo(operations.redo());
		mainWindow.updateWorkspace();
		selection.clear();
		changeMade = true;
	}

	public static void toggleGrid()
	{
		if (graph.gridOn)
		{
			graph.gridOn = false;
		}
		else
		{
			GridDialog gd = new GridDialog(mainWindow, graph.gridResolutionX, graph.gridResolutionY);
			int[] result = gd.showDialog();
			if (result != null)
			{
				graph.gridOn = true;
				graph.gridResolutionX = result[0];
				graph.gridResolutionY = result[1];
				graph.adjustVerticesToGrid();
			}
		}
		mainWindow.updateFunctions();
	}

	public static void setNumeration()
	{
		selection.clear();
		updatePropertyChangeOperationStatus(false);
		NumerationDialog nd = new NumerationDialog(mainWindow, graph.digitalNumeration, graph.startNumber, graph.maxNumber);
		int[] result = nd.showDialog();

		if (result != null)
		{
			graph.digitalNumeration = (result[0] == 0);
			graph.startNumber = result[1];
		}

		mainWindow.updateFunctions();
	}

	public static void parseToTeX()
	{
		new LatexCodeDialog(mainWindow, Parser.parse(graph));
	}

	public static void copyToClipboard()
	{
		currentCopyPasteOperation = new CopyPasteOperation(selection);
		mainWindow.menuBar.updateFunctions();
		mainWindow.panel_buttonContainer.updateFunctions();
		publishInfo("Supgraph copied to the clipboard");
	}

	public static void pasteFromClipboard()
	{
		if (!currentCopyPasteOperation.pasting)
		{
			currentCopyPasteOperation.startPasting();
			publishInfo("Choose location for copied subgraph");
			changeMode(VERTEX_MODE);
			changeTool(SELECT_TOOL);
		}
	}

	public static void editGraphTemplate()
	{
		GraphsTemplateDialog gdd = new GraphsTemplateDialog(mainWindow);
		Graph templateGraph = gdd.displayDialog();

		if (templateGraph != null)
		{
			operations.addNewOperation(new TemplateChangeOperation(graph, templateGraph));
			publishInfo(operations.redo());
		}
	}

	public static void processActionButtonClicking(int option)
	{
		switch (option)
		{
			case 0:
				newGraphFile();
				break;
			case 1:
                openGraphFile();
				break;
			case 2:
				saveGraphFile(false);
				break;
			case 3:
				editGraphTemplate();
				break;
			case 4:
				copyToClipboard();
				break;
			case 5:
				pasteFromClipboard();
				break;
			case 6:
				undo();
				break;
			case 7:
				redo();
				break;
			case 8:
				toggleGrid();
				break;
			case 9:
				setNumeration();
				break;
			case 10:
				parseToTeX();
				break;
			default:
				break;
		}
	}

	public static void cancelCurrentOperation()
	{
		if (currentCopyPasteOperation != null)
		{
			currentCopyPasteOperation = currentCopyPasteOperation.getCopy();
		}
		currentlyAddedEdge = null;
	}
	
	public static void showAboutDialog()
	{
		new AboutDialog(mainWindow);
	}

	public static void processMouseClicking(MouseEvent e)
	{
		boolean createdLabel = false;

		int x = e.getX();
		int y = e.getY();
		changeMade = true;

		if (tool != SELECT_TOOL)
		{
			selection.clear();
			updatePropertyChangeOperationStatus(false);
		}

		boolean consumed = false;
		if (currentCopyPasteOperation != null)
		{
			if (currentCopyPasteOperation.pasting)
			{
				consumed = true;
				if (currentCopyPasteOperation.fitsIntoPosition())
				{
					operations.addNewOperation(currentCopyPasteOperation);
					publishInfo(operations.redo());
					selection.addAll(currentCopyPasteOperation.vertices);
					updatePropertyChangeOperationStatus(true);
					currentCopyPasteOperation = currentCopyPasteOperation.getCopy();
					mainWindow.menuBar.updateFunctions();
					mainWindow.panel_buttonContainer.updateFunctions();
				}
				else
				{
					publishInfo("Can't paste this subgraph here - vertices collide with existing ones");
				}
			}
		}

		if (!consumed)
		{
			if (mode == VERTEX_MODE)
			{
				if (tool == ADD_TOOL)
				{
					Vertex vertex = new Vertex();
					vertex.setModel(graph.getVertexDefaultModel());
					vertex.setPosX(x);
					vertex.setPosY(y);
					if (graph.gridOn)
					{
						vertex.adjustToGrid();
					}

					if (vertex.fitsIntoPage())
					{

						if (!graph.vertexCollision(vertex))
						{
							vertex.updateNumber(ControlManager.graph.getNextFreeNumber());
							operations.addNewOperation(new AddOperation(vertex));
							publishInfo(operations.redo());
							selection.add(vertex);
						}
						else
						{
							publishInfo("Can't create a vertex here - too close to another vertex");
						}
					}
					else
					{
						publishInfo("Can't create a vertex here - too close to page edge");
					}
				}

				else if (tool == REMOVE_TOOL)
				{
					Vertex temp = graph.getVertexFromPosition(x, y);
					if (temp != null)
					{
						operations.addNewOperation(new RemoveOperation(temp));
						publishInfo(operations.redo());
					}
					else
					{
						publishInfo("Nothing to remove");
					}
				}

				else if (tool == SELECT_TOOL)
				{
					if (!mousePressed)
					{
						addToSelection(graph.getVertexFromPosition(x, y), e.isControlDown());
					}
				}
			}

			else if (mode == EDGE_MODE)
			{
				if (tool == ADD_TOOL)
				{
					Vertex temp = graph.getVertexFromPosition(x, y);
					if (temp == null)
					{
						if (currentlyAddedEdge == null)
						{
							publishInfo("Choose a starting vertex for the edge (click)");
						}
						else
						{
							currentlyAddedEdge = null;
							publishInfo("Adding edge cancelled");
						}
					}
					else
					{
						if (currentlyAddedEdge == null)
						{
							currentlyAddedEdge = new Edge();
							currentlyAddedEdge.setModel(graph.getEdgeDefaultModel());
							currentlyAddedEdge.setVertexA(temp);
							publishInfo("Now choose the target vertex (click)");
						}
						else
						{
							currentlyAddedEdge.setVertexB(temp);
							currentlyAddedEdge.setDirected(e.isShiftDown());
							operations.addNewOperation(new AddOperation(currentlyAddedEdge));
							publishInfo(operations.redo());
							selection.add(currentlyAddedEdge);
							currentlyAddedEdge = null;
						}
					}
				}

				else if (tool == REMOVE_TOOL)
				{
					Edge temp = graph.getEdgeFromPosition(x, y);
					if (temp != null)
					{
						operations.addNewOperation(new RemoveOperation(temp));
						publishInfo(operations.redo());
					}
					else
					{
						publishInfo("Nothing to remove");
					}
				}

				else if (tool == SELECT_TOOL)
				{
					if (!mousePressed)
					{
						addToSelection(graph.getEdgeFromPosition(x, y), e.isControlDown());
					}
				}
			}

			else if (mode == LABEL_V_MODE)
			{
				if (tool == ADD_TOOL)
				{
					Vertex temp = graph.getVertexFromPosition(x, y);
					if (temp != null)
					{
						if (temp.getLabel() == null)
						{
							LabelV labelV = new LabelV(temp);
							labelV.setModel(graph.getLabelVDefaultModel());
							operations.addNewOperation(new AddOperation(labelV));
							publishInfo(operations.redo());
							selection.add(labelV);
							createdLabel = true;
						}
						else
						{
							publishInfo("This vertex already has a label");
						}
					}
					else
					{
						publishInfo("Choose a vertex to attach label to");
					}
				}

				else if (tool == REMOVE_TOOL)
				{
					LabelV temp = graph.getLabelVFromPosition(x, y);
					if (temp != null)
					{
						operations.addNewOperation(new RemoveOperation(temp));
						publishInfo(operations.redo());
					}
					else
					{
						publishInfo("Nothing to remove");
					}
				}

				else if (tool == SELECT_TOOL)
				{
					if (!mousePressed)
					{
						addToSelection(graph.getLabelVFromPosition(x, y), e.isControlDown());
					}
				}
			}

			else
			{
				if (tool == ADD_TOOL)
				{
					Edge temp = graph.getEdgeFromPosition(x, y);
					if (temp != null)
					{
						if (temp.getLabel() == null)
						{
							LabelE labelE = new LabelE(temp);
							labelE.setModel(graph.getLabelEDefaultModel());
							labelE.setHorizontalPlacement(e.isShiftDown());
							operations.addNewOperation(new AddOperation(labelE));
							publishInfo(operations.redo());
							selection.add(labelE);
							createdLabel = true;
						}
						else
						{
							publishInfo("This edge already has a label");
						}
					}
					else
					{
						publishInfo("Choose an edge to attach label to");
					}
				}

				else if (tool == REMOVE_TOOL)
				{
					LabelE temp = graph.getLabelEFromPosition(x, y);
					if (temp != null)
					{
						operations.addNewOperation(new RemoveOperation(temp));
						publishInfo(operations.redo());
					}
					else
					{
						publishInfo("Nothing to remove");
					}
				}

				else if (tool == SELECT_TOOL)
				{
					if (!mousePressed)
					{
						addToSelection(graph.getLabelEFromPosition(x, y), e.isControlDown());
					}
				}
			}
			updatePropertyChangeOperationStatus(true);
			if (createdLabel)
			{
				mainWindow.panel_propertyEditor.giveFocusToTextField();
			}
		}
	}

	public static void processMousePressing(MouseEvent e)
	{
		int x = e.getX();
		int y = e.getY();
		changeMade = true;
		mousePressed = true;
		mousePressX = x;
		mousePressY = y;

		if (tool != REMOVE_TOOL)
		{
			if (mode == VERTEX_MODE)
			{
				if (graph.getVertexFromPosition(x, y) != null)
				{
					if (selection.contains(graph.getVertexFromPosition(x, y)))
					{
						currentlyMovedElement = graph.getVertexFromPosition(x, y);
						currentDragOperation = new DragOperation(currentlyMovedElement);
						currentDragOperation.setStartPos(((Vertex) currentlyMovedElement).getPosX(), ((Vertex) currentlyMovedElement).getPosY());
					}
				}
			}
			else if (mode == EDGE_MODE)
			{
				if (graph.getEdgeFromPosition(x, y) != null)
				{
					if (selection.contains(graph.getEdgeFromPosition(x, y)))
					{
						Edge edge = (Edge) graph.getEdgeFromPosition(x, y);
						currentlyMovedElement = edge;
						currentDragOperation = new DragOperation(currentlyMovedElement);
						if (edge.getVertexA() == edge.getVertexB())
						{
							Point c = new Point(x, y);
							Point v = new Point(edge.getVertexA().getPosX(), edge.getVertexA().getPosY());
							if (c.distance(v) > (edge.getVertexA().getRadius() + edge.getVertexA().getLineWidth() / 2) * 1.5)
							{
								changingEdgeAngle = true;
								currentDragOperation.setEdgeStartState(edge, true);
							}
							else
							{
								edgeDragDummy = new Vertex();
								edgeDragDummy.setPosX(x);
								edgeDragDummy.setPosY(y);
								edgeDragDummy.setRadius(2);
								if (c.distance(edge.getInPoint()) < c.distance(edge.getOutPoint()))
								{
									currentDragOperation.setEdgeStartState(edge, false);
									edge.setVertexB(edgeDragDummy);
								}
								else
								{
									currentDragOperation.setEdgeStartState(edge, true);
									edge.setVertexA(edgeDragDummy);
								}

							}
						}
						else
						{
							Point c = new Point(x, y);
							Point va = new Point(edge.getVertexA().getPosX(), edge.getVertexA().getPosY());
							Point vb = new Point(edge.getVertexB().getPosX(), edge.getVertexB().getPosY());
							if (c.distance(va) / c.distance(vb) < 2 && c.distance(va) / c.distance(vb) > 0.5)
							{
								changingEdgeAngle = true;
								currentDragOperation.setEdgeStartState(edge, true);
							}
							else
							{
								edgeDragDummy = new Vertex();
								edgeDragDummy.setPosX(x);
								edgeDragDummy.setPosY(y);
								edgeDragDummy.setRadius(2);
								if (c.distance(va) < c.distance(vb))
								{
									currentDragOperation.setEdgeStartState(edge, true);
									edge.setVertexA(edgeDragDummy);
								}
								else
								{
									currentDragOperation.setEdgeStartState(edge, false);
									edge.setVertexB(edgeDragDummy);
								}
							}
						}
					}
				}
			}
			else if (mode == LABEL_V_MODE)
			{
				if (graph.getLabelVFromPosition(x, y) != null)
				{
					if (selection.contains(graph.getLabelVFromPosition(x, y)))
					{
						currentlyMovedElement = graph.getLabelVFromPosition(x, y);
						currentDragOperation = new DragOperation(currentlyMovedElement);
						currentDragOperation.setStartAngle(((LabelV) currentlyMovedElement).getPosition());
					}
				}
			}
			else if (mode == LABEL_E_MODE)
			{
				if (graph.getLabelEFromPosition(x, y) != null)
				{
					if (selection.contains(graph.getLabelEFromPosition(x, y)))
					{
						currentlyMovedElement = graph.getLabelEFromPosition(x, y);
						currentDragOperation = new DragOperation(currentlyMovedElement);
						currentDragOperation.setLabelEStartState((LabelE) currentlyMovedElement);
					}
				}
			}
		}
	}

	public static void finishMovingElement()
	{
		if (currentlyMovedElement != null)
		{
			if (currentlyMovedElement instanceof Vertex)
			{
				currentDragOperation.setEndPos(((Vertex) currentlyMovedElement).getPosX(), ((Vertex) currentlyMovedElement).getPosY());
				if (currentDragOperation.changeMade())
				{
					operations.addNewOperation(currentDragOperation);
					publishInfo(operations.redo());
				}
			}
			if (currentlyMovedElement instanceof Edge)
			{
				Edge edge = (Edge) currentlyMovedElement;
				if (changingEdgeAngle)
				{
					currentDragOperation.setEdgeEndState(edge);
					if (currentDragOperation.changeMade())
					{
						operations.addNewOperation(currentDragOperation);
						publishInfo(operations.redo());
					}
					changingEdgeAngle = false;
				}
				else
				{
					if (currentDragOperation.draggingA())
					{
						if (edge.getVertexA() != currentDragOperation.getDisjointedVertex())
						{
							if (edge.getVertexA() != edgeDragDummy)
							{
								currentDragOperation.setEdgeEndState(edge);
								operations.addNewOperation(currentDragOperation);
								publishInfo(operations.redo());
							}
							else
							{
								currentDragOperation.restoreEdgeStartState();
							}
						}
					}
					else
					{
						if (edge.getVertexB() != currentDragOperation.getDisjointedVertex())
						{
							if (edge.getVertexB() != edgeDragDummy)
							{
								currentDragOperation.setEdgeEndState(edge);
								operations.addNewOperation(currentDragOperation);
								publishInfo(operations.redo());
							}
							else
							{
								currentDragOperation.restoreEdgeStartState();
							}
						}
					}
				}
			}
			else if (currentlyMovedElement instanceof LabelV)
			{
				currentDragOperation.setEndAngle(((LabelV) currentlyMovedElement).getPosition());
				if (currentDragOperation.changeMade())
				{
					operations.addNewOperation(currentDragOperation);
					publishInfo(operations.redo());
				}
			}
			else if (currentlyMovedElement instanceof LabelE)
			{
				currentDragOperation.setLabelEEndState((LabelE) currentlyMovedElement);
				if (currentDragOperation.changeMade())
				{
					operations.addNewOperation(currentDragOperation);
					publishInfo(operations.redo());
				}
			}

			updatePropertyChangeOperationStatus(true);
		}
	}

	public static void processMouseReleasing(MouseEvent e)
	{
		int x = e.getX();
		int y = e.getY();
		changeMade = true;
		mousePressed = false;
		finishMovingElement();

		if (tool > ADD_TOOL && currentlyMovedElement == null)
		{
			int x1 = Math.min(mousePressX, x);
			int width = Math.abs(x - mousePressX);
			int y1 = Math.min(mousePressY, y);
			int height = Math.abs(y - mousePressY);

			if (width + height > 2)
			{
				if (tool == REMOVE_TOOL)
				{
					selection.clear();
					selection.addAll(graph.getIntersectingElements(new Rectangle(x1, y1, width, height)));
					deleteSelection();
				}

				else if (tool == SELECT_TOOL)
				{
					addToSelection(graph.getIntersectingElements(new Rectangle(x1, y1, width, height)), e.isControlDown());
				}
			}
		}

		currentlyMovedElement = null;
		currentDragOperation = null;
	}

	public static void processMouseMoving(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();

		if (currentlyAddedEdge != null)
		{
			currentlyAddedEdge.setDirected(e.isShiftDown());
		}
		if (currentlyMovedElement != null)
		{
			if (currentlyMovedElement instanceof Edge)
			{
				((Edge) currentlyMovedElement).setDirected(e.isShiftDown());
			}
		}
	}

	public static void processShiftPressing(boolean pressed)
	{
		shiftDown = pressed;
		if (ControlManager.currentlyAddedEdge != null)
		{
			ControlManager.currentlyAddedEdge.setDirected(pressed);
		}
		if (currentlyMovedElement != null)
		{
			if (currentlyMovedElement instanceof Edge)
			{
				((Edge) currentlyMovedElement).setDirected(pressed);
			}
			if (currentlyMovedElement instanceof LabelE)
			{
				((LabelE) currentlyMovedElement).setHorizontalPlacement(!pressed);
			}
		}
		mainWindow.updateWorkspace();
	}

	public static void processMouseDragging(MouseEvent e)
	{
		int x = e.getX();
		int y = e.getY();
		changeMade = true;
		mouseX = x;
		mouseY = y;

		if (currentlyMovedElement != null)
		{
			if (mode == VERTEX_MODE)
			{
				Vertex vertex = (Vertex) currentlyMovedElement;
				graph.getVertices().remove(vertex);
				int oldPosX = vertex.getPosX();
				int oldPosY = vertex.getPosY();

				vertex.setPosX(x);
				vertex.setPosY(y);

				if (graph.gridOn)
				{
					vertex.adjustToGrid();
				}

				if (graph.vertexCollision(vertex) || !vertex.fitsIntoPage())
				{
					vertex.setPosX(oldPosX);
					vertex.setPosY(oldPosY);
				}
				graph.getVertices().add(vertex);
			}
			else if (mode == EDGE_MODE)
			{
				if (changingEdgeAngle)
				{
					Edge edge = (Edge) currentlyMovedElement;
					if (edge.getVertexA() == edge.getVertexB())
					{
						double angle = (Math.toDegrees(Math.atan2(mouseX - edge.getVertexB().getPosX(), mouseY - edge.getVertexB().getPosY())) + 270) % 360;
						edge.setRelativeEdgeAngle(((int) Math.floor((angle + 45) / 90) % 4) * 90);
					}
					else
					{
						double angle = 0.0;
						if (Point.distance(x, y, edge.getVertexA().getPosX(), edge.getVertexA().getPosY()) < Point.distance(x, y, edge.getVertexB().getPosX(), edge.getVertexB().getPosY()))
						{
							angle = Math.toDegrees(Math.atan2(x - edge.getVertexA().getPosX(), y - edge.getVertexA().getPosY())) + 270;
							angle -= Math.toDegrees(Math.atan2(edge.getVertexB().getPosX() - edge.getVertexA().getPosX(), edge.getVertexB().getPosY() - edge.getVertexA().getPosY())) + 270;
						}
						else
						{
							angle = Math.toDegrees(Math.atan2(x - edge.getVertexB().getPosX(), y - edge.getVertexB().getPosY())) + 270;
							angle = -angle + Math.toDegrees(Math.atan2(edge.getVertexA().getPosX() - edge.getVertexB().getPosX(), edge.getVertexA().getPosY() - edge.getVertexB().getPosY()))
									+ 270;
						}

						angle = (angle + 362.5) % 360;
						int intAngle = ((((int) Math.floor(angle) / 5) * 5) + 720) % 360;
						if (intAngle > 60)
						{
							if (intAngle < 180)
							{
								intAngle = 60;
							}
							else if (intAngle < 300)
							{
								intAngle = 300;
							}

						}
						edge.setRelativeEdgeAngle(intAngle);
					}
				}
				else
				{
					Vertex vertex;
					if ((vertex = graph.getVertexFromPosition(x, y)) != null)
					{
						Edge edge = (Edge) currentlyMovedElement;
						if (currentDragOperation.draggingA())
						{
							edge.setVertexA(vertex);
						}
						else
						{
							edge.setVertexB(vertex);
						}

						if (edge.getVertexA() == edge.getVertexB())
						{
							double angle = (Math.toDegrees(Math.atan2(mouseX - edge.getVertexB().getPosX(), mouseY - edge.getVertexB().getPosY())) + 270) % 360;
							edge.setRelativeEdgeAngle(((int) Math.floor((angle + 45) / 90) % 4) * 90);
						}
						else
						{
							double angle = 0.0;
							if (Point.distance(x, y, edge.getVertexA().getPosX(), edge.getVertexA().getPosY()) < Point.distance(x, y, edge.getVertexB().getPosX(),
                                    edge.getVertexB().getPosY()))
							{
								angle = Math.toDegrees(Math.atan2(x - edge.getVertexA().getPosX(), y - edge.getVertexA().getPosY())) + 270;
								angle -= Math.toDegrees(Math.atan2(edge.getVertexB().getPosX() - edge.getVertexA().getPosX(), edge.getVertexB().getPosY() - edge.getVertexA().getPosY())) + 270;
							}
							else
							{
								angle = Math.toDegrees(Math.atan2(x - edge.getVertexB().getPosX(), y - edge.getVertexB().getPosY())) + 270;
								angle = -angle
										+ Math.toDegrees(Math.atan2(edge.getVertexA().getPosX() - edge.getVertexB().getPosX(), edge.getVertexA().getPosY() - edge.getVertexB().getPosY()))
										+ 270;
							}

							angle = (angle + 362.5) % 360;
							int intAngle = ((((int) Math.floor(angle) / 5) * 5) + 720) % 360;
							if (intAngle > 60)
							{
								if (intAngle < 180)
								{
									intAngle = 60;
								}
								else if (intAngle < 300)
								{
									intAngle = 300;
								}

							}
							edge.setRelativeEdgeAngle(intAngle);
						}
					}
					else
					{
						edgeDragDummy.setPosX(x);
						edgeDragDummy.setPosY(y);
						((Edge) currentlyMovedElement).setRelativeEdgeAngle(0);
						if (currentDragOperation.draggingA())
						{
							((Edge) currentlyMovedElement).setVertexA(edgeDragDummy);
						}
						else
						{
							((Edge) currentlyMovedElement).setVertexB(edgeDragDummy);
						}
					}
				}
			}
			else if (mode == LABEL_V_MODE)
			{
				Vertex vertex = ((LabelV) currentlyMovedElement).getOwner();
				Point2D p1 = new Point(vertex.getPosX(), vertex.getPosY());
				Point2D p2 = new Point(x, y);
				double angle = Math.toDegrees(Math.asin((x - vertex.getPosX()) / p1.distance(p2)));
				if (y < vertex.getPosY())
				{
					if (x < vertex.getPosX())
					{
						angle += 360;
					}
				}
				else
				{
					angle = 180 - angle;
				}
				((LabelV) currentlyMovedElement).setPosition(((int) Math.abs(Math.ceil((angle - 22.5) / 45))) % 8);
			}
			else if (mode == LABEL_E_MODE)
			{
				int bias = 0;
				Edge edge = ((LabelE) currentlyMovedElement).getOwner();
				LabelE labelE = (LabelE) currentlyMovedElement;
				if (edge.getVertexA() == edge.getVertexB())
				{
					double angle = -Math.toDegrees(Math.atan2(x - edge.getArcMiddle().x, y - edge.getArcMiddle().y)) + 270 + edge.getRelativeEdgeAngle();
					labelE.setPosition((int) Math.round((angle % 360) / 3.6));
				}

				else
				{
					if (edge.getRelativeEdgeAngle() == 0)
					{
						Point p1 = edge.getInPoint();
						Point p2 = edge.getOutPoint();

						if (p2.x < p1.x)
						{
							Point temp = p2;
							p2 = p1;
							p1 = temp;
						}

						Point c = new Point(x, y);
						double p1p2 = p1.distance(p2);
						double p1c = p1.distance(c);
						double p2c = p2.distance(c);
						double mc = Line2D.ptLineDist((double) p1.x, (double) p1.y, (double) p2.x, (double) p2.y, (double) x, (double) y);

						labelE.setTopPlacement(((p2.x - p1.x) * (c.y - p1.y) - (p2.y - p1.y) * (c.x - p1.x) < 0));

						if (p1c * p1c > p2c * p2c + p1p2 * p1p2)
						{
							bias = 100;
						}
						else if (p2c * p2c > p1c * p1c + p1p2 * p1p2)
						{
							bias = 0;
						}
						else
						{
							bias = (int) Math.round(100 * Math.sqrt(p1c * p1c - mc * mc) / p1p2);
						}
						labelE.setPosition(bias);
					}
					else
					{
						double startAngle = (Math.toDegrees(Math.atan2(edge.getOutPoint().x - edge.getArcMiddle().x, edge.getOutPoint().y - edge.getArcMiddle().y)) + 270) % 360;
						double endAngle = (Math.toDegrees(Math.atan2(edge.getInPoint().x - edge.getArcMiddle().x, edge.getInPoint().y - edge.getArcMiddle().y)) + 270) % 360;
						double mouseAngle = (Math.toDegrees(Math.atan2(x - edge.getArcMiddle().x, y - edge.getArcMiddle().y)) + 270) % 360;

						int position = 0;
						double alpha = (startAngle - mouseAngle + 360) % 360;
						if (alpha > 180)
						{
							alpha -= 360;
						}
						double beta = (startAngle - endAngle + 360) % 360;
						if (beta > 180)
						{
							beta -= 360;
						}

						position = (int) Math.round(100 * (alpha / beta));

						if (position > -1 && position < 101)
						{
							labelE.setTopPlacement((edge.getArcMiddle().distance(new Point(x, y)) > edge.getArcRadius()));
							labelE.setPosition(position);
						}
					}
				}
			}
		}
	}

	public static void paintCurrentlyAddedElement(Graphics2D g)
	{
		if (tool == ADD_TOOL)
		{
			if (mode == VERTEX_MODE)
			{
				Vertex vertex = new Vertex();
				vertex.setModel(graph.getVertexDefaultModel());
				vertex.updateNumber(graph.getNextFreeNumber());
				vertex.setPosX(mouseX);
				vertex.setPosY(mouseY);
				if (!graph.vertexCollision(vertex) && vertex.fitsIntoPage())
				{
					vertex.draw(g, true);
				}
			}
			else if (mode == EDGE_MODE)
			{
				if (currentlyAddedEdge != null)
				{
					Vertex vertex = ControlManager.graph.getVertexFromPosition(mouseX, mouseY);
					if (vertex == null)
					{
						vertex = new Vertex();
						vertex.setPosX(mouseX);
						vertex.setPosY(mouseY);
						vertex.setRadius(2);
						currentlyAddedEdge.setRelativeEdgeAngle(0);
					}
					currentlyAddedEdge.setVertexB(vertex);
					if (ControlManager.graph.getVertexFromPosition(mouseX, mouseY) != null)
					{
						if (currentlyAddedEdge.getVertexA() != currentlyAddedEdge.getVertexB())
						{
							double angle = Math.toDegrees(Math.atan2(mouseX - currentlyAddedEdge.getVertexB().getPosX(), mouseY
									- currentlyAddedEdge.getVertexB().getPosY())) + 270;
							angle = -angle
									+ Math.toDegrees(Math.atan2(currentlyAddedEdge.getVertexA().getPosX() - currentlyAddedEdge.getVertexB().getPosX(),
											currentlyAddedEdge.getVertexA().getPosY() - currentlyAddedEdge.getVertexB().getPosY())) + 270;
							angle = (angle + 362.5) % 360;
							int intAngle = ((((int) Math.floor(angle) / 5) * 5) + 720) % 360;
							if (intAngle > 60)
							{
								if (intAngle < 180)
								{
									intAngle = 60;
								}
								else if (intAngle < 300)
								{
									intAngle = 300;
								}

							}
							currentlyAddedEdge.setRelativeEdgeAngle(intAngle);
						}
						else
						{
							double angle = (Math.toDegrees(Math.atan2(mouseX - currentlyAddedEdge.getVertexB().getPosX(), mouseY
									- currentlyAddedEdge.getVertexB().getPosY())) + 270) % 360;
							currentlyAddedEdge.setRelativeEdgeAngle(((int) Math.floor((angle + 45) / 90) % 4) * 90);
						}
					}
					currentlyAddedEdge.draw(g, true);
				}
			}
			else if (mode == LABEL_V_MODE)
			{
				Vertex temp = graph.getVertexFromPosition(mouseX, mouseY);
				if (temp != null)
				{
					if (temp.getLabel() == null)
					{
						LabelV labelV = new LabelV(temp);
						labelV.setModel(graph.getLabelVDefaultModel());

						Point2D p1 = new Point(temp.getPosX(), temp.getPosY());
						Point2D p2 = new Point(mouseX, mouseY);
						double angle = Math.toDegrees(Math.asin((mouseX - temp.getPosX()) / p1.distance(p2)));
						if (mouseY < temp.getPosY())
						{
							if (mouseX < temp.getPosX())
							{
								angle += 360;
							}
						}
						else
						{
							angle = 180 - angle;
						}
						labelV.setPosition(((int) Math.abs(Math.ceil((angle - 22.5) / 45))) % 8);

						graph.getLabelVDefaultModel().position = labelV.getPosition();
						labelV.draw(g, true);
					}
				}
			}
			else
			{
				Edge temp = graph.getEdgeFromPosition(mouseX, mouseY);
				if (temp != null)
				{
					if (temp.getLabel() == null)
					{
						LabelE labelE = new LabelE(temp);
						labelE.setModel(graph.getLabelEDefaultModel());
						labelE.setHorizontalPlacement(shiftDown);

						int bias = 0;
						int x = mouseX;
						int y = mouseY;

						if (temp.getVertexA() == temp.getVertexB())
						{
							double angle = -Math.toDegrees(Math.atan2(x - temp.getArcMiddle().x, y - temp.getArcMiddle().y)) + 270 + temp.getRelativeEdgeAngle();
							labelE.setPosition((int) Math.round((angle % 360) / 3.6));
						}

						else
						{
							if (temp.getRelativeEdgeAngle() == 0)
							{
								Point p1 = temp.getInPoint();
								Point p2 = temp.getOutPoint();

								if (p2.x < p1.x)
								{
									Point tempP = p2;
									p2 = p1;
									p1 = tempP;
								}

								Point c = new Point(x, y);
								double p1p2 = p1.distance(p2);
								double p1c = p1.distance(c);
								double p2c = p2.distance(c);
								double mc = Line2D.ptLineDist((double) p1.x, (double) p1.y, (double) p2.x, (double) p2.y, (double) x, (double) y);

								labelE.setTopPlacement(((p2.x - p1.x) * (c.y - p1.y) - (p2.y - p1.y) * (c.x - p1.x) < 0));

								if (p1c * p1c > p2c * p2c + p1p2 * p1p2)
								{
									bias = 100;
								}
								else if (p2c * p2c > p1c * p1c + p1p2 * p1p2)
								{
									bias = 0;
								}
								else
								{
									bias = (int) Math.round(100 * Math.sqrt(p1c * p1c - mc * mc) / p1p2);
								}
								labelE.setPosition(bias);
							}
							else
							{
								double startAngle = (Math.toDegrees(Math
										.atan2(temp.getOutPoint().x - temp.getArcMiddle().x, temp.getOutPoint().y - temp.getArcMiddle().y)) + 270) % 360;
								double endAngle = (Math.toDegrees(Math.atan2(temp.getInPoint().x - temp.getArcMiddle().x, temp.getInPoint().y - temp.getArcMiddle().y)) + 270) % 360;
								double mouseAngle = (Math.toDegrees(Math.atan2(x - temp.getArcMiddle().x, y - temp.getArcMiddle().y)) + 270) % 360;

								int position = 0;
								double alpha = (startAngle - mouseAngle + 360) % 360;
								if (alpha > 180)
								{
									alpha -= 360;
								}
								double beta = (startAngle - endAngle + 360) % 360;
								if (beta > 180)
								{
									beta -= 360;
								}

								position = (int) Math.round(100 * (alpha / beta));

								if (position > -1 && position < 101)
								{
									labelE.setTopPlacement((temp.getArcMiddle().distance(new Point(x, y)) > temp.getArcRadius()));
									labelE.setPosition(position);
								}
							}
						}

						graph.getLabelEDefaultModel().topPlacement = 0;
						if (labelE.isTopPlacement())
						{
							graph.getLabelEDefaultModel().topPlacement = 1;
						}
						graph.getLabelEDefaultModel().position = labelE.getPosition();
						labelE.draw(g, true);
					}
				}
			}
		}
	}

	public static void paintGrid(Graphics2D g)
	{
		if (graph.gridOn)
		{
			g.setColor(new Color(200, 200, 200));
			int i = 0;
			while ((i += graph.gridResolutionX) < graph.pageWidth)
			{
				g.drawLine(i, 0, i, graph.pageHeight);
			}
			i = 0;
			while ((i += graph.gridResolutionY) < graph.pageHeight)
			{
				g.drawLine(0, i, graph.pageWidth, i);
			}
		}
	}

	public static void paintSelectionArea(Graphics2D g)
	{
		if (tool > ADD_TOOL && currentlyMovedElement == null)
		{
			if (mousePressed && (mouseX != mousePressX || mouseY != mousePressY))
			{
				int x = Math.min(mousePressX, mouseX);
				int width = Math.abs(mouseX - mousePressX);
				int y = Math.min(mousePressY, mouseY);
				int height = Math.abs(mouseY - mousePressY);

				g.setColor(new Color(140, 210, 255, 128));
				g.fillRect(x, y, width, height);

				g.setColor(new Color(0, 0, 255, 128));
				g.drawRect(x, y, width, height);
			}
		}
	}

	public static void paintCopiedSubgraph(Graphics2D g)
	{
		if (currentCopyPasteOperation != null)
		{
			if (currentCopyPasteOperation.pasting)
			{
				currentCopyPasteOperation.targetX = mouseX;
				currentCopyPasteOperation.targetY = mouseY;
				currentCopyPasteOperation.calculatePosition();
				if (currentCopyPasteOperation.fitsIntoPosition())
				{
					currentCopyPasteOperation.drawDummySubgraph(g);
				}
			}
		}
	}

	public static boolean checkForUnsavedProgress()
	{
		if (changeMade)
		{
			Object[] options = { "Save", "Don't save", "Cancel" };
			int option = JOptionPane.showOptionDialog(mainWindow, "There have been changes since last save.\n"
					+ "Would you like to save your graph now?", "Unsaved progress", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
					null, options, options[0]);

			if (option == JOptionPane.CANCEL_OPTION)
			{
				return false;
			}
			else if (option == JOptionPane.NO_OPTION)
			{
				return true;
			}
			else
			{
				return saveGraphFile(true);
			}
		}
		else
		{
			return true;
		}
	}

	public static void exitApplication()
	{
		if (checkForUnsavedProgress())
		{
			System.exit(0);
		}
	}
}
