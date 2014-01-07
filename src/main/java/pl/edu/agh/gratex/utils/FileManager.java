package pl.edu.agh.gratex.utils;


import pl.edu.agh.gratex.controller.ParseController;
import pl.edu.agh.gratex.model.graph.Graph;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private File currentFile;
    private List<String> savedContent;
    private ParseController parseController;

    public FileManager(ParseController parseController) {
        this.parseController = parseController;
        currentFile = null;
        savedContent = new ArrayList<>();
    }

    public boolean saveFile(File fileToSave, Graph graph) {
        boolean result = true;
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(fileToSave);
            List<String> content = parseController.parseGraphToLatexCode(graph);
            for (String line : content) {
                printWriter.println(line);
            }
            currentFile = fileToSave;
            savedContent = content;
        } catch (IOException e) {
            //TODO
            result = false;
            e.printStackTrace();
        } finally {
            if(printWriter != null) {
                printWriter.close();
            }
        }
        return result;
    }

    public boolean hasContentChanged(Graph graph) {
        List<String> content = parseController.parseGraphToLatexCode(graph);
        if(content == null || content.size() == 0) {
            return false;
        }
        if(currentFile == null) {
            return true;
        }
        return !content.equals(savedContent);
    }

    public Graph openFile(File fileToOpen) {
        BufferedReader bufferedReader = null;
        Graph result = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(fileToOpen));
            String line;
            List<String> content = new ArrayList<>();
            while((line = bufferedReader.readLine()) != null) {
                content.add(line);
            }
            result = parseController.parseLatexCodeToGraph(content);
            currentFile = fileToOpen;
            savedContent = content;
        } catch (IOException e) {
            //TODO
            e.printStackTrace();
        } finally {
            if(bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public File getCurrentFile() {
        return currentFile;
    }
}
