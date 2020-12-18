/*
 *  Program #4
 *  DNA Converter DNA -> RNA -> Protein
 *  CS108-1
 *  March 1st, 2020
 *  Rogelio Schevenin Jr.
 */

import javax.management.RuntimeErrorException;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.File;

public class DNAConverter {
    //main used for testing within IDE
    public static void main(String args[]) {
        DNA2RNA("DNA", "RNA");
        RNA2Protein("RNA", "Protein", "RNA2ProteinTable");
    }
    /**
     * @param DNAFile
     * @param RNAFile
     * @throws NoSuchElementException
     * This method shall read DNA.txt
     * and transcribe this into RNA.txt
     * @name DNA2RNA
     */
    public static void DNA2RNA(String DNAFile, String RNAFile) {
        File dnaFile = new File(DNAFile);
        File rnaFile = new File(RNAFile);
        Scanner dna = new Scanner(System.in);
        PrintWriter rna = new PrintWriter(System.out);

        //Ensures files exist
        try {
            dna = new Scanner(dnaFile);
            rna = new PrintWriter(rnaFile);
        } catch (FileNotFoundException e) {}

        //Translates DNA into RNA for every line in DNA file
        while (dna.hasNextLine()) {
            String lineToChange = new String(dna.nextLine());
            for (int i = 0; i < lineToChange.length(); i++) {
                switch (lineToChange.charAt(i)) {
                    case 'T':
                        rna.write('A');
                        break;
                    case 'A':
                        rna.write('U');
                        break;
                    case 'C':
                        rna.write('G');
                        break;
                    case 'G':
                        rna.write('C');
                        break;
                    default:
                        throw new NoSuchElementException("Not a DNA character");
                }
            }
            rna.println();
        }

        //Flush and close io streams
        rna.flush();
        dna.close();
        rna.close();
    }
    /**
     * @param RNAFile
     * @param ProteinFile
     * @param RNA2ProteinTable
     * @throws NoSuchElementException
     * This method shall read RNA.txt and translate this
     * into Protein.txt with help from RNA2Protein.txt
     * @name RNA2Protein
     */
    public static void RNA2Protein(String RNAFile, String ProteinFile, String RNA2ProteinTable) {
        File rnaFile = new File(RNAFile);
        File proteinFile = new File(ProteinFile);
        File tableFile = new File(RNA2ProteinTable);
        Scanner rna = new Scanner(System.in);
        Scanner table = new Scanner(System.in);
        PrintWriter protein = new PrintWriter(System.out);
        String rnaLine = new String();
        String tableLine = new String();

        //Make sure files exist and counts lines inside table file
        try {
            table = new Scanner(tableFile);
            rna = new Scanner(rnaFile);
            protein = new PrintWriter(proteinFile);

        } catch (FileNotFoundException e) {
            System.out.println(e);
            return;
        }
        int lines = 0;
        try {
            while (table.hasNextLine()) {
                lines++;
                table.nextLine();
            }
        } catch (RuntimeException e) {}
        //Reads table file and makes 2D array out of it
        try {
            table = new Scanner(tableFile);
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
        String[][] TABLEgroups = new String[lines][2];
        for (int x = 0; x < lines; x++) {
            tableLine = table.nextLine();
            TABLEgroups[x][0] = tableLine.substring(0, 3);
            TABLEgroups[x][1] = tableLine.substring(4);
        }
        System.out.println("Table Groups: " + Arrays.deepToString(TABLEgroups));

        int j = 1;

        //Translates RNA into Protein for every line in RNA file
        while (rna.hasNextLine()) {
            rnaLine = new String(rna.nextLine());
            String[] RNAgroups = new String[(rnaLine.length()/3)];
            int m = 1;
            //Reads RNA and groups them into an array
            if (rnaLine.length() % 3 != 0) {
                throw new RuntimeException("Invalid RNA length");
            }
            for (int x = 0; x < rnaLine.length(); x += 3) {
                RNAgroups[x/3] = rnaLine.substring(x, x+3);
            }
            System.out.println("RNA Groups on line " + j + " of RNA file: " + rnaLine);
            System.out.println();
            //Compares RNA groups table groups
            for (int i = 0; i < RNAgroups.length; i++) {
                String currGroup = new String(RNAgroups[i]);
                System.out.println("("+m+") Scanning for " + currGroup + " in translation table:");
                for (int k = 0; k < TABLEgroups.length; k++) {
                    if (currGroup.equals(TABLEgroups[k][0])) {
                        System.out.println("MATCH FOUND: " + RNAgroups[i] + " == " + TABLEgroups[k][0]);
                        System.out.println("Translating " + RNAgroups[i] + " into " + TABLEgroups[k][1]);
                        System.out.println();
                        protein.write(TABLEgroups[k][1]);
                        break;
                    } else {
                        System.out.println("" + RNAgroups[i] + " =/= " + TABLEgroups[k][0]);
                    }
                }
                m++;
            }
            protein.println();
            System.out.println("Done translating RNA groups on line " + j);
            System.out.println("-----------------------------------------");
            j++;
        }

        //flush and close io streams
        protein.flush();
        rna.close();
        table.close();

        //Outputs final protein
        Scanner proteinScnr = new Scanner(System.in);
        String proteinLine = new String();
        try {
            proteinScnr = new Scanner(proteinFile);
        } catch (FileNotFoundException e) {}
        System.out.println("Translated into protein: " + proteinScnr.nextLine());
        proteinScnr.close();
    }
}