package pl.edu.agh.gratex.gui;


import java.util.Iterator;

import pl.edu.agh.gratex.graph.Edge;
import pl.edu.agh.gratex.graph.Graph;
import pl.edu.agh.gratex.graph.LabelE;
import pl.edu.agh.gratex.graph.LabelV;
import pl.edu.agh.gratex.graph.Vertex;
import pl.edu.agh.gratex.model.PropertyModel;


public class Parser
{

	public static String parse(Graph gr)
	{
		String result = "\\usetikzlibrary{shapes.geometric}\n\\begin{tikzpicture}\n[every node/.style={inner sep=0pt}]\n";

		Iterator<Vertex> itv = gr.vertices.listIterator();
		Vertex vtemp = null;
		String line = null;
		while (itv.hasNext())
		{
			vtemp = itv.next();
			line = "\\node (" + vtemp.getNumber() + ") ";
			if (vtemp.type == 1)
			{
				line += "[circle"; //ksztalt
			}
			else
			{
				line += "[regular polygon, regular polygon sides=" + (vtemp.type + 1);
			}
			line += ", minimum size=" + (1.25 * vtemp.radius) + "pt"; //wielkosc
			if (vtemp.vertexColor != null)
				line += ", fill=" + PropertyModel.COLORS.get(vtemp.vertexColor);
			line += ", line width=" + 0.625 * vtemp.lineWidth + "pt";
			if (vtemp.lineType != PropertyModel.NONE)
			{
				line += ", draw";
				if (vtemp.lineColor != null)
					line += "=" + PropertyModel.COLORS.get(vtemp.lineColor);
				if (vtemp.lineType == PropertyModel.DASHED)
					line += ", dashed";
				else if (vtemp.lineType == PropertyModel.DOTTED)
					line += ", dotted";
				else if (vtemp.lineType == PropertyModel.DOUBLE)
					line += ", double";
			}
			line += "] at (" + 0.625 * vtemp.posX + "pt, " + 0.625 * (-vtemp.posY) + "pt) ";
			if (vtemp.labelInside)
			{
				if (vtemp.fontColor != null)
					line += "{\\textcolor{" + PropertyModel.COLORS.get(vtemp.fontColor) + "}{" + vtemp.getNumber() + "}};\n";
				else
					line += "{" + vtemp.getNumber() + "}";
			}
			else
				line += " {};\n";
			result += line;
		}
		vtemp = null;

		Iterator<Edge> etv = gr.edges.listIterator();
		line = "";
		Edge etemp = null;
		while (etv.hasNext())
		{
			etemp = etv.next();
			line = "\\draw [line width=" + 0.625 * etemp.lineWidth;
			if (etemp.directed) {
				line += ", ->";
				if(etemp.arrowType == PropertyModel.FILLED ) line += ", >=latex";
			}
			if (etemp.lineType == PropertyModel.DASHED)
				line += ", dashed";
			else if (etemp.lineType == PropertyModel.DOTTED)
				line += ", dotted";
			else if (etemp.lineType == PropertyModel.DOUBLE)
				line += ", double";
			if (etemp.lineColor != null)
				line += ", color=" + PropertyModel.COLORS.get(etemp.lineColor);
			//petla
			if (etemp.vertexA == etemp.vertexB)
			{
				line += ", loop ";
				switch (etemp.relativeEdgeAngle)
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
				line += "] (" + etemp.vertexA.getNumber() + ") to (" + etemp.vertexB.getNumber() + ");\n";
			}
			else
			{
				line += "]";
				line += " (" + etemp.vertexA.getNumber() + ") to ";
				if (etemp.relativeEdgeAngle > 0)
					line += " [in=" + etemp.inAngle + ", out=" + etemp.outAngle + "]";

				line += " (" + etemp.vertexB.getNumber() + ");\n";
			}
			result += line;
		}
		etemp = null;

		itv = gr.vertices.listIterator();
		vtemp = null;
		LabelV lvtemp = null;
		line = null;
		while (itv.hasNext())
		{
			vtemp = itv.next();
			if (vtemp.label != null)
			{
				lvtemp = vtemp.label;
				line = "\\node at (" + 0.625 * lvtemp.posX + "pt, " + 0.625 * (-lvtemp.posY) + "pt) ";
				if (lvtemp.fontColor != null)
					line += "{\\textcolor{" + PropertyModel.COLORS.get(lvtemp.fontColor) + "}{" + lvtemp.text + "}};\n";
				else
					line += "{" + lvtemp.text + "}";
				result += line;
			}
		}
		lvtemp = null;
		vtemp = null;

		etv = gr.edges.listIterator();
		line = null;
		etemp = null;
		LabelE letemp = null;
		while (etv.hasNext())
		{
			etemp = etv.next();
			if (etemp.label != null)
			{
				letemp = etemp.label;
				line = "\\node at (" + 0.625 * letemp.posX + "pt, " + 0.625 * (-letemp.posY) + "pt) ";
				if (letemp.angle > 0)
					line += "[rotate=" + letemp.angle + "] ";
				if (letemp.fontColor != null)
					line += "{\\textcolor{" + PropertyModel.COLORS.get(letemp.fontColor) + "}{" + letemp.text + "}};\n";
				else
					line += "{" + letemp.text + "}";
				result += line;
			}
		}

		result += "\\end{tikzpicture}";
		return result;
	}
}
