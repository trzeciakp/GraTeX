package pl.edu.agh.gratex.gui;


import java.util.Iterator;

import pl.edu.agh.gratex.graph.Edge;
import pl.edu.agh.gratex.graph.Graph;
import pl.edu.agh.gratex.graph.LabelE;
import pl.edu.agh.gratex.graph.LabelV;
import pl.edu.agh.gratex.graph.Vertex;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.properties.LineType;


public class Parser
{

	public static String parse(Graph gr)
	{
		String result = "\\usetikzlibrary{shapes.geometric}\n\\begin{tikzpicture}\n[every node/.style={inner sep=0pt}]\n";

		Iterator<Vertex> itv = gr.getVertices().listIterator();
		Vertex vtemp = null;
		String line = null;
		while (itv.hasNext())
		{
			vtemp = itv.next();
			line = "\\node (" + vtemp.getNumber() + ") ";
			if (vtemp.getType() == 1)
			{
				line += "[circle"; //ksztalt
			}
			else
			{
				line += "[regular polygon, regular polygon sides=" + (vtemp.getType() + 1);
			}
			line += ", minimum size=" + (1.25 * vtemp.getRadius()) + "pt"; //wielkosc
			if (vtemp.getVertexColor() != null)
				line += ", fill=" + PropertyModel.COLORS.get(vtemp.getVertexColor());
			line += ", line width=" + 0.625 * vtemp.getLineWidth() + "pt";
			if (vtemp.getLineType() != LineType.NONE)
			{
				line += ", draw";
				if (vtemp.getLineColor() != null)
					line += "=" + PropertyModel.COLORS.get(vtemp.getLineColor());
				if (vtemp.getLineType() == LineType.DASHED)
					line += ", dashed";
				else if (vtemp.getLineType() == LineType.DOTTED)
					line += ", dotted";
				else if (vtemp.getLineType() == LineType.DOUBLE)
					line += ", double";
			}
			line += "] at (" + 0.625 * vtemp.getPosX() + "pt, " + 0.625 * (-vtemp.getPosY()) + "pt) ";
			if (vtemp.isLabelInside())
			{
				if (vtemp.getFontColor() != null)
					line += "{\\textcolor{" + PropertyModel.COLORS.get(vtemp.getFontColor()) + "}{" + vtemp.getNumber() + "}};\n";
				else
					line += "{" + vtemp.getNumber() + "}";
			}
			else
				line += " {};\n";
			result += line;
		}
		vtemp = null;

		Iterator<Edge> etv = gr.getEdges().listIterator();
		line = "";
		Edge etemp = null;
		while (etv.hasNext())
		{
			etemp = etv.next();
			line = "\\draw [line width=" + 0.625 * etemp.getLineWidth();
			if (etemp.isDirected()) {
				line += ", ->";
				if(etemp.getArrowType() == PropertyModel.FILLED ) line += ", >=latex";
			}
			if (etemp.getLineType() == LineType.DASHED)
				line += ", dashed";
			else if (etemp.getLineType() == LineType.DOTTED)
				line += ", dotted";
			else if (etemp.getLineType() == LineType.DOUBLE)
				line += ", double";
			if (etemp.getLineColor() != null)
				line += ", color=" + PropertyModel.COLORS.get(etemp.getLineColor());
			//petla
			if (etemp.getVertexA() == etemp.getVertexB())
			{
				line += ", loop ";
				switch (etemp.getRelativeEdgeAngle())
				{
					case 0:
						line += "right";
						break;
					case 90:
						line += "above";
						break;
					case 180:
						line += "left";
						break;
					case 270:
						line += "below";
						break;
				}
				line += "] (" + etemp.getVertexA().getNumber() + ") to (" + etemp.getVertexB().getNumber() + ");\n";
			}
			else
			{
				line += "]";
				line += " (" + etemp.getVertexA().getNumber() + ") to ";
				if (etemp.getRelativeEdgeAngle() > 0)
					line += " [in=" + etemp.getInAngle() + ", out=" + etemp.getOutAngle() + "]";

				line += " (" + etemp.getVertexB().getNumber() + ");\n";
			}
			result += line;
		}
		etemp = null;

		itv = gr.getVertices().listIterator();
		vtemp = null;
		LabelV lvtemp = null;
		line = null;
		while (itv.hasNext())
		{
			vtemp = itv.next();
			if (vtemp.getLabel() != null)
			{
				lvtemp = vtemp.getLabel();
				line = "\\node at (" + 0.625 * lvtemp.getPosX() + "pt, " + 0.625 * (-lvtemp.getPosY()) + "pt) ";
				if (lvtemp.getFontColor() != null)
					line += "{\\textcolor{" + PropertyModel.COLORS.get(lvtemp.getFontColor()) + "}{" + lvtemp.getText() + "}};\n";
				else
					line += "{" + lvtemp.getText() + "}";
				result += line;
			}
		}
		lvtemp = null;
		vtemp = null;

		etv = gr.getEdges().listIterator();
		line = null;
		etemp = null;
		LabelE letemp = null;
		while (etv.hasNext())
		{
			etemp = etv.next();
			if (etemp.getLabel() != null)
			{
				letemp = etemp.getLabel();
				line = "\\node at (" + 0.625 * letemp.getPosX() + "pt, " + 0.625 * (-letemp.getPosY()) + "pt) ";
				if (letemp.getAngle() > 0)
					line += "[rotate=" + letemp.getAngle() + "] ";
				if (letemp.getFontColor() != null)
					line += "{\\textcolor{" + PropertyModel.COLORS.get(letemp.getFontColor()) + "}{" + letemp.getText() + "}};\n";
				else
					line += "{" + letemp.getText() + "}";
				result += line;
			}
		}

		result += "\\end{tikzpicture}";
		return result;
	}
}
