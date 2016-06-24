package model;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Observable;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CreationHelper;

/**
 * Logic model performs logic and notifies the GUI.
 * 
 * @author Daniel Yao
 * @year 2016
 */
public class LogicModel extends Observable
{
   /** The workbook that is read. */
   XSSFWorkbook wb;
   /** The current excel spreadsheet. */
   XSSFSheet sheet;

   protected ArrayList<Day> dayList;

   public LogicModel()
   {
      dayList = new ArrayList<Day>();
   }

   /**
    * Read the provided excel file.
    * 
    * @param filename The name of the file.
    */
   public void readExcelFile(String filename)
   {
      try
      {
         readXLFile(filename);
      } catch (IOException | InvalidFormatException e)
      {
         e.printStackTrace();
      }
   }

   private void readXLFile(String filename) throws FileNotFoundException, IOException, InvalidFormatException
   {

      XSSFWorkbook wb = new XSSFWorkbook(new File(filename));
      XSSFSheet sheet = wb.getSheetAt(0);

      XSSFRow xlrow;
      XSSFCell cell;

      int numRows; // Num of rows
      numRows = sheet.getPhysicalNumberOfRows();

      int numCols = 0; // Num of columns
      int tmp = 0;

      // This ensures that we get the data properly even if it doesn't start
      // from first few rows
      for (int i = 0; i < 10 || i < numRows; i++)
      {
         xlrow = sheet.getRow(i);
         if (xlrow != null)
         {
            tmp = sheet.getRow(i).getPhysicalNumberOfCells();
            if (tmp > numCols)
               numCols = tmp;
         }
      }
      
      //Initialize available days from first row of sheet
      initializeDayList(sheet.getRow(0), numCols);

      // For every row in the sheet starting from row 2
      for (int row = 1; row < numRows; row++)
      {
         xlrow = sheet.getRow(row);

         if (xlrow != null)
         {
            // For every column in the row
            for (int col = 0; col < numCols; col++)
            {
               cell = xlrow.getCell(col);
               if (cell != null)
               {
                  // Code goes here
                  //System.out.println("Row: " + row + " Col: " + col);
                  // System.out.printf("Row: %3d Col: %3d\n -Value: %s\n", row,
                  // col, cell.toString());
               }
            }
         }
      }
      wb.close();
   }

   private void parseFile()
   {

   }

   /**
    * Initialize all the days that are offered.
    * 
    * @param xlRow The row with the days.
    * @param numCols The total number of columns
    */
   private void initializeDayList(XSSFRow xlRow, int numCols)
   {
      /** First date starts with column 9 */
      int index = 9;
      int col = 0;
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
      XSSFCell cell;
      
      cell = xlRow.getCell(index);
      String cellDate = cell.toString();
      System.out.println("." + cellDate.substring(cellDate.indexOf(" "), cellDate.length()).trim() + ".");
   }

   /**
    * NOT IMPLEMENTED YET Write output to the specified file.
    * 
    * @param outputFile The name of the output file.
    */
   public void writeExcelFile(String outputFile)
   {
      try
      {
         writeXLFile("testOutput.xlsx");
      } catch (IOException e)
      {
         e.printStackTrace();
      }
   }

   private void writeXLFile(String outputFile) throws IOException
   {
      XSSFWorkbook wb = new XSSFWorkbook();
      CreationHelper createHelper = wb.getCreationHelper();
      XSSFSheet sheet = wb.createSheet("Sheet_1");

      // Create a row and put some cells in it. Rows are 0 based.
      XSSFRow row = sheet.createRow(0);

      // Or do it on one line.
      row.createCell(0).setCellValue(1.2);
      row.createCell(1).setCellValue(createHelper.createRichTextString("This is a string"));
      row.createCell(2).setCellValue(true);

      // Write the output to a file
      FileOutputStream fileOut = new FileOutputStream(outputFile);
      wb.write(fileOut);
      fileOut.close();
      wb.close();
   }

}
