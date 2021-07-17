package com.example.study_application;

import android.content.Context;
import android.content.ContextWrapper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReadAndWrite extends AppCompatActivity {

    private final ContextWrapper CONTEXTWRAPPER;
    private final Context CONTEXT;
    private String[][] textNameData;
    private String[] DataString;

    // allows context to be used inside the class
    public ReadAndWrite(Context mContext) {
        this.CONTEXT = mContext;
        CONTEXTWRAPPER = new ContextWrapper(CONTEXT);
    }

    public String[][] readTaskNameData(String file, Boolean TextOrBody) {
        //reads file data and splits it into separate items with each new line in the file data
        String fileData = readFile(file);
        DataString = fileData.split("\n");

        //separates each item into parts because of the commas when reading the file
        String StringArray = Arrays.toString(DataString);
        String[] stringArrays = StringArray.split(",");

        //checks which file has been put through the method
        if (!TextOrBody) {
            //creates new array as it is only used here and does for loop to to add the parts back together to form groups
            String[][] textBodyData = new String[stringArrays.length][];
            for (int i = 1; i < DataString.length; i++) {
                String[] values = DataString[i].split(" ");

                String IdSpecifications = values[0];
                String TaskSpecification = values[1];

                String[] valueSpecificationData = {IdSpecifications, TaskSpecification};

                textBodyData[i] = valueSpecificationData;
            }
            return textBodyData;
        } else {
            //does the same thing from the top code only difference is that its for the other file
            textNameData = new String[stringArrays.length][];
            for (int i = 1; i < DataString.length; i++) {
                String[] values = DataString[i].split(" ");

                String IdName = values[0];
                String taskName = values[1];
                String taskCompletion = values[2];
                String timeRequired = values[3];

                String[] valueNameData = {IdName, taskName, taskCompletion, timeRequired};

                textNameData[i] = valueNameData;
            }
            return textNameData;
        }
    }

    //code already explained
    public String readFile(String file) {
        //stating the variable used to return
        String text = "";
        try {
            FileInputStream fis = CONTEXTWRAPPER.openFileInput(file);
            //checks and sets the size of the byte
            int size = fis.available();
            byte[] buffer = new byte[size];
            //reads the file even though it says its ignored
            fis.read(buffer);
            fis.close();
            text = new String(buffer);
            //if there was an error it would run this code instead of an error message.
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(CONTEXT, "Error reading file", Toast.LENGTH_SHORT).show();
        }
        return text;
    }

    //
    public void write(String file, String textData) {
        try {
            FileOutputStream fos = CONTEXTWRAPPER.openFileOutput(file, MODE_APPEND);
            fos.write(textData.getBytes());
            fos.close();
            Toast.makeText(CONTEXT, "saving file successful", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(CONTEXT, "Error saving file", Toast.LENGTH_SHORT).show();
        }
    }

    public void replaceLines(String oldFileLine, String newFileLine, String fileName) {
        String[] fileDataLines = readFile(fileName).split("\n");

        //opens the file to be able to write inside.
        try {
            FileOutputStream fos = CONTEXTWRAPPER.openFileOutput(fileName, MODE_PRIVATE);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> fileContent = Arrays.asList(fileDataLines);
        //finds specific line inside txt file then replaces the file and puts back
        // into the list which then the list is pasted back inside the txt file
        for (int i = 0; i < fileContent.size(); i++) {
            fileContent.set(i, fileContent.get(i) + "\n");
        }

        // checks through the updated file content to find lines that are empty
        for (int i = 0; i < fileContent.size(); i++) {
            if (fileContent.get(i).equals(oldFileLine + "\n")) {
                if (newFileLine.equals("")) {
                    fileContent.set(i, newFileLine);
                } else {
                    fileContent.set(i, newFileLine + "\n");
                }
                break;
            }
        }

        //writes the file content back inside
        for (int i = 0; i < fileContent.size(); i++) {
            write(fileName, fileContent.get(i));
        }
    }

    public String findBiggestId() {
        String biggestId;
        readTaskNameData("TaskNames.txt", true);
        //sets the length of the array to be one less than the actual amount as one value is null
        Integer[] ids = new Integer[textNameData.length - 1];

        // setting the values for the array
        for (int i = 1; i < DataString.length; i++) {
            int value = Integer.parseInt(textNameData[i][0]);
            ids[i - 1] = value;
        }

        Arrays.sort(ids, Collections.reverseOrder());

        // this code checks if there is no tasks left and if theres none than it will return the value 1
        int errorCheck = textNameData.length - 1;
        if (errorCheck == 0) {
            biggestId = "1";
        } else {
            biggestId = String.valueOf(ids[0] + 1);
        }
        // returns biggest id
        return biggestId;
    }
}
