package pl.edu.agh.gratex.view;


import pl.edu.agh.gratex.model.graph.Graph;

import java.io.*;

public class FileManager {
    public static boolean saveFile(File target, Graph content) {
        boolean result = true;
        ObjectOutputStream output = null;

        try {
            output = new ObjectOutputStream(new FileOutputStream(target));
            output.writeObject(content);
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
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
