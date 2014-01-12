package pl.edu.agh.gratex.utils;


import pl.edu.agh.gratex.controller.ParseController;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.parser.ParserException;

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

    public boolean saveFile(File fileToSave, Graph graph) throws IOException {
        boolean result = true;
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(fileToSave);
            List<String> content = parseController.parseGraphToLatexCode(graph);
            content.addAll(parseController.parseTemplatesToLatexCode());
            for (String line : content) {
                printWriter.println(line);
            }
            currentFile = fileToSave;
            savedContent = content;
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
        List<String> templates = parseController.parseTemplatesToLatexCode();
        content.addAll(templates);
        return !content.equals(savedContent);
    }

    public Graph openFile(File fileToOpen) throws IOException, ParserException {
        BufferedReader bufferedReader = null;
        Graph result = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(fileToOpen));
            String line;
            List<String> content = new ArrayList<>();
            while((line = bufferedReader.readLine()) != null) {
                content.add(line);
            }
            result = parseController.parseLatexCodeWithTemplatesToGraph(content);
            currentFile = fileToOpen;
            savedContent = content;
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
