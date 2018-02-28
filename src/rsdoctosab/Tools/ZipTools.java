package rsdoctosab.Tools;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author Nikos Siatras
 */
public class ZipTools
{
    
    private static final int BUFFER_SIZE = 4096;
    
    public static void UnZipFile(String zipFilePath, String destDirectory) throws IOException
    {
        System.out.print("Unzipping " + zipFilePath + "... ");
        File destDir = new File(destDirectory);
        if (!destDir.exists())
        {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null)
        {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory())
            {
                // if the entry is a file, extracts it
                ExtractFile(zipIn, filePath);
            }
            else
            {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
        
        System.out.println("Finished!");
    }
    
    public static void ExtractFiles(String zipFilePath, String destDirectory, String fileExtension) throws IOException
    {
        
        File destDir = new File(destDirectory);
        if (!destDir.exists())
        {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null)
        {
            String filePath = destDirectory + File.separator + entry.getName();
            
            if (entry.getName().endsWith(fileExtension))
            {
                // if the entry is a file, extracts it
                File zipFile = new File(zipFilePath);
                String destinationPath = zipFile.getParent() + File.separator + zipFile.getName().substring(0, zipFile.getName().length() - 4) + fileExtension;
                
                ExtractFile(zipIn, destinationPath);
            }
            
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
        
        System.out.println("Finished!");
    }
    
    public static void ExtractFile(ZipInputStream zipIn, String filePath) throws IOException
    {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1)
        {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
}
