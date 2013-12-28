package pl.edu.agh.gratex.utils;


import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.controller.ParseController;
import pl.edu.agh.gratex.model.graph.Graph;
import sun.plugin2.gluegen.runtime.BufferFactory;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class FileManager {
    private File currentFile;
    private List<String> savedContent;

    public boolean saveFile(File fileToSave, Graph graph, ParseController controller) {
        boolean result = true;
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(fileToSave);
            List<String> content = controller.parseGraphToLatexCode(graph);
            for (String line : content) {
                printWriter.println(line);
            }
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
    // TODO GeneralControler nie bedzie juz potrzebny jak bedzie implementacja parsera wstecznego, wtedy wywalic
    public static boolean contentChanged(GeneralController gc, Graph content, File currentFile) {
        try {
            if (currentFile == null) {
                ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(bOut);
                out.writeObject(content);
                out.close();
                byte[] currentBytes = bOut.toByteArray();

                bOut = new ByteArrayOutputStream();
                out = new ObjectOutputStream(bOut);
                out.writeObject(new Graph(gc));
                out.close();
                byte[] newGraphBytes = bOut.toByteArray();

                return !Arrays.equals(currentBytes, newGraphBytes);
            } else {
                ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(bOut);
                out.writeObject(content);
                out.close();
                byte[] currentBytes = bOut.toByteArray();


                byte[] fileBytes = Files.readAllBytes(currentFile.toPath());

                return !Arrays.equals(currentBytes, fileBytes);
            }
        } catch (Exception e) {
            return true;
        }
    }

    public static boolean saveFile(Graph content, File target) {
        boolean result = true;
        ObjectOutputStream output = null;

        try {
            output = new ObjectOutputStream(new FileOutputStream(target));
            output.writeObject(content);
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (Exception e) {
            }
        }
        return result;
    }

    public static Graph openFile(File source) {
        Graph result = null;
        ObjectInputStream input = null;

        try {
            input = new ObjectInputStream(new FileInputStream(source));
            result = (Graph) input.readObject();
        } catch (Exception e) {
            result = null;
        } finally {
            try {
                input.close();
            } catch (Exception e) {
            }
        }
        return result;
    }
}
