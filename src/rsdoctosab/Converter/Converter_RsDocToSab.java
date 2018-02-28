package rsdoctosab.Converter;

import java.io.File;
import java.nio.file.Files;
import javax.swing.JOptionPane;
import rsdoctosab.Tools.FileHelper;
import rsdoctosab.Tools.ZipTools;

/**
 *
 * @author Nikos Siatras
 */
public class Converter_RsDocToSab
{

    public Converter_RsDocToSab()
    {

    }

    public void Convert(File rsDocFile)
    {
        // Check if file is rsdoc
        if (!rsDocFile.getPath().toLowerCase().endsWith(".rsdoc"))
        {
            JOptionPane.showMessageDialog(null, "The file" + rsDocFile.getName() + " is not .rsdoc!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get rsDoc dir path
        String rsDocDirPath = rsDocFile.getParent();

        //////////////////////////////////////////////////////////////////////////////////
        // Step 1
        // Get .rsdoc name without .rsdoc extension
        //////////////////////////////////////////////////////////////////////////////////
        String rsDocName = rsDocFile.getName();
        rsDocName = rsDocName.substring(0, rsDocName.length() - 6);

        //////////////////////////////////////////////////////////////////////////////////
        // Step 2
        // Copy .rsdoc as .zip
        //////////////////////////////////////////////////////////////////////////////////
        File zipFile = new File(rsDocDirPath + File.separator + rsDocName + "_temp.zip");
        try
        {
            FileHelper.CopyFile(rsDocFile, zipFile);
        }
        catch (Exception ex)
        {
            // Cannot copy file
            JOptionPane.showMessageDialog(null, "You don't have write permission for path:\n" + rsDocDirPath, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //////////////////////////////////////////////////////////////////////////////////
        // Step 3.
        // Extract .sat files from zip
        //////////////////////////////////////////////////////////////////////////////////
        try
        {
            ZipTools.ExtractFiles(zipFile.getPath(), rsDocDirPath, ".sab");
        }
        catch (Exception ex)
        {
            // Cannot copy file
            JOptionPane.showMessageDialog(null, "You don't have write permission for path:\n" + rsDocDirPath, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //////////////////////////////////////////////////////////////////////////////////
        // Step 4.
        // Delete .zip temp file
        //////////////////////////////////////////////////////////////////////////////////
        try
        {
            Files.delete(zipFile.toPath());
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(null, "You don't have delete permission for path:\n" + rsDocDirPath, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
}
