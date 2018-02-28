package rsdoctosab.Tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 *
 * @author Nikos Siatras
 */
public class FileHelper
{

    static
    {

    }

    public static void CopyFile(File sourceFile, File destinationFile) throws IOException
    {
        FileInputStream inputStream = new FileInputStream(sourceFile);
        FileOutputStream outputStream = new FileOutputStream(destinationFile);
        FileChannel inChannel = inputStream.getChannel();
        FileChannel outChannel = outputStream.getChannel();
        try
        {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        }
        finally
        {
            inChannel.close();
            outChannel.close();
            inputStream.close();
            outputStream.close();
        }
    }
}
