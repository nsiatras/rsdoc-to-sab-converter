package rsdoctosab.FileDrop;

import java.awt.datatransfer.DataFlavor;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;

public class FileDrop
{

    private transient javax.swing.border.Border normalBorder;
    private transient java.awt.dnd.DropTargetListener dropListener;

    private static Boolean supportsDnD;

    private static java.awt.Color defaultBorderColor = new java.awt.Color(0f, 0f, 1f, 0.25f);

    public FileDrop(
            final java.awt.Component c,
            final Listener listener)
    {
        this(null,
                c,
                javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, defaultBorderColor), // Drag border
                true,
                listener);
    }

    public FileDrop(
            final java.awt.Component c,
            final boolean recursive,
            final Listener listener)
    {
        this(null, // Logging stream
                c, // Drop target
                javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, defaultBorderColor), // Drag border
                recursive, // Recursive
                listener);
    }   // end constructor

    public FileDrop(
            final java.io.PrintStream out,
            final java.awt.Component c,
            final Listener listener)
    {
        this(out, // Logging stream
                c, // Drop target
                javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, defaultBorderColor),
                false, // Recursive
                listener);
    }   // end constructor

    public FileDrop(
            final java.io.PrintStream out,
            final java.awt.Component c,
            final boolean recursive,
            final Listener listener)
    {
        this(out, // Logging stream
                c, // Drop target
                javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, defaultBorderColor), // Drag border
                recursive, // Recursive
                listener);
    }   // end constructor

    public FileDrop(
            final java.awt.Component c,
            final javax.swing.border.Border dragBorder,
            final Listener listener)
    {
        this(
                null, // Logging stream
                c, // Drop target
                dragBorder, // Drag border
                false, // Recursive
                listener);
    }   // end constructor

    public FileDrop(
            final java.awt.Component c,
            final javax.swing.border.Border dragBorder,
            final boolean recursive,
            final Listener listener)
    {
        this(
                null,
                c,
                dragBorder,
                recursive,
                listener);
    }   // end constructor

    public FileDrop(
            final java.io.PrintStream out,
            final java.awt.Component c,
            final javax.swing.border.Border dragBorder,
            final Listener listener)
    {
        this(
                out, // Logging stream
                c, // Drop target
                dragBorder, // Drag border
                false, // Recursive
                listener);
    }   // end constructor

    public FileDrop(
            final java.io.PrintStream out,
            final java.awt.Component c,
            final javax.swing.border.Border dragBorder,
            final boolean recursive,
            final Listener listener)
    {

        if (supportsDnD())
        {   // Make a drop listener
            dropListener = new java.awt.dnd.DropTargetListener()
            {
                public void dragEnter(java.awt.dnd.DropTargetDragEvent evt)
                {
                    log(out, "FileDrop: dragEnter event.");

                    // Is this an acceptable drag event?
                    if (isDragOk(out, evt))
                    {
                        // If it's a Swing component, set its border
                        if (c instanceof javax.swing.JComponent)
                        {
                            javax.swing.JComponent jc = (javax.swing.JComponent) c;
                            normalBorder = jc.getBorder();
                            log(out, "FileDrop: normal border saved.");
                            jc.setBorder(dragBorder);
                            log(out, "FileDrop: drag border set.");
                        }   // end if: JComponent   

                        // Acknowledge that it's okay to enter
                        //evt.acceptDrag( java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE );
                        evt.acceptDrag(java.awt.dnd.DnDConstants.ACTION_COPY);
                        log(out, "FileDrop: event accepted.");
                    }   // end if: drag ok
                    else
                    {   // Reject the drag event
                        evt.rejectDrag();
                        log(out, "FileDrop: event rejected.");
                    }   // end else: drag not ok
                }   // end dragEnter

                public void dragOver(java.awt.dnd.DropTargetDragEvent evt)
                {   // This is called continually as long as the mouse is
                    // over the drag target.
                }   // end dragOver

                public void drop(java.awt.dnd.DropTargetDropEvent evt)
                {
                    log(out, "FileDrop: drop event.");
                    try
                    {   // Get whatever was dropped
                        java.awt.datatransfer.Transferable tr = evt.getTransferable();

                        // Is it a file list?
                        if (tr.isDataFlavorSupported(java.awt.datatransfer.DataFlavor.javaFileListFlavor))
                        {
                            // Say we'll take it.
                            //evt.acceptDrop ( java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE );
                            evt.acceptDrop(java.awt.dnd.DnDConstants.ACTION_COPY);
                            log(out, "FileDrop: file list accepted.");

                            // Get a useful list
                            java.util.List fileList = (java.util.List) tr.getTransferData(java.awt.datatransfer.DataFlavor.javaFileListFlavor);
                            java.util.Iterator iterator = fileList.iterator();

                            // Convert list to array
                            java.io.File[] filesTemp = new java.io.File[fileList.size()];
                            fileList.toArray(filesTemp);
                            final java.io.File[] files = filesTemp;

                            // Alert listener to drop.
                            if (listener != null)
                            {
                                listener.filesDropped(files);
                            }

                            // Mark that drop is completed.
                            evt.getDropTargetContext().dropComplete(true);
                            log(out, "FileDrop: drop complete.");
                        }   // end if: file list
                        else // this section will check for a reader flavor.
                        {
                            // Thanks, Nathan!
                            // BEGIN 2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.
                            DataFlavor[] flavors = tr.getTransferDataFlavors();
                            boolean handled = false;
                            for (int zz = 0; zz < flavors.length; zz++)
                            {
                                if (flavors[zz].isRepresentationClassReader())
                                {
                                    // Say we'll take it.
                                    //evt.acceptDrop ( java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE );
                                    evt.acceptDrop(java.awt.dnd.DnDConstants.ACTION_COPY);
                                    log(out, "FileDrop: reader accepted.");

                                    Reader reader = flavors[zz].getReaderForText(tr);

                                    BufferedReader br = new BufferedReader(reader);

                                    if (listener != null)
                                    {
                                        listener.filesDropped(createFileArray(br, out));
                                    }

                                    // Mark that drop is completed.
                                    evt.getDropTargetContext().dropComplete(true);
                                    log(out, "FileDrop: drop complete.");
                                    handled = true;
                                    break;
                                }
                            }
                            if (!handled)
                            {
                                log(out, "FileDrop: not a file list or reader - abort.");
                                evt.rejectDrop();
                            }
                            // END 2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.
                        }   // end else: not a file list
                    }   // end try
                    catch (java.io.IOException io)
                    {
                        log(out, "FileDrop: IOException - abort:");
                        io.printStackTrace(out);
                        evt.rejectDrop();
                    }   // end catch IOException
                    catch (java.awt.datatransfer.UnsupportedFlavorException ufe)
                    {
                        log(out, "FileDrop: UnsupportedFlavorException - abort:");
                        ufe.printStackTrace(out);
                        evt.rejectDrop();
                    }   // end catch: UnsupportedFlavorException
                    finally
                    {
                        // If it's a Swing component, reset its border
                        if (c instanceof javax.swing.JComponent)
                        {
                            javax.swing.JComponent jc = (javax.swing.JComponent) c;
                            jc.setBorder(normalBorder);
                            log(out, "FileDrop: normal border restored.");
                        }   // end if: JComponent
                    }   // end finally
                }   // end drop

                public void dragExit(java.awt.dnd.DropTargetEvent evt)
                {
                    log(out, "FileDrop: dragExit event.");
                    // If it's a Swing component, reset its border
                    if (c instanceof javax.swing.JComponent)
                    {
                        javax.swing.JComponent jc = (javax.swing.JComponent) c;
                        jc.setBorder(normalBorder);
                        log(out, "FileDrop: normal border restored.");
                    }   // end if: JComponent
                }   // end dragExit

                public void dropActionChanged(java.awt.dnd.DropTargetDragEvent evt)
                {
                    log(out, "FileDrop: dropActionChanged event.");
                    // Is this an acceptable drag event?
                    if (isDragOk(out, evt))
                    {   //evt.acceptDrag( java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE );
                        evt.acceptDrag(java.awt.dnd.DnDConstants.ACTION_COPY);
                        log(out, "FileDrop: event accepted.");
                    }   // end if: drag ok
                    else
                    {
                        evt.rejectDrag();
                        log(out, "FileDrop: event rejected.");
                    }   // end else: drag not ok
                }   // end dropActionChanged
            }; // end DropTargetListener

            // Make the component (and possibly children) drop targets
            makeDropTarget(out, c, recursive);
        }   // end if: supports dnd
        else
        {
            log(out, "FileDrop: Drag and drop is not supported with this JVM");
        }   // end else: does not support DnD
    }   // end constructor

    private static boolean supportsDnD()
    {   // Static Boolean
        if (supportsDnD == null)
        {
            boolean support = false;
            try
            {
                Class arbitraryDndClass = Class.forName("java.awt.dnd.DnDConstants");
                support = true;
            }   // end try
            catch (Exception e)
            {
                support = false;
            }   // end catch
            supportsDnD = new Boolean(support);
        }   // end if: first time through
        return supportsDnD.booleanValue();
    }   // end supportsDnD

    // BEGIN 2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.
    private static String ZERO_CHAR_STRING = "" + (char) 0;

    private static File[] createFileArray(BufferedReader bReader, PrintStream out)
    {
        try
        {
            java.util.List list = new java.util.ArrayList();
            java.lang.String line = null;
            while ((line = bReader.readLine()) != null)
            {
                try
                {
                    // kde seems to append a 0 char to the end of the reader
                    if (ZERO_CHAR_STRING.equals(line))
                    {
                        continue;
                    }

                    java.io.File file = new java.io.File(new java.net.URI(line));
                    list.add(file);
                }
                catch (Exception ex)
                {
                    log(out, "Error with " + line + ": " + ex.getMessage());
                }
            }

            return (java.io.File[]) list.toArray(new File[list.size()]);
        }
        catch (IOException ex)
        {
            log(out, "FileDrop: IOException");
        }
        return new File[0];
    }

    private void makeDropTarget(final java.io.PrintStream out, final java.awt.Component c, boolean recursive)
    {
        // Make drop target
        final java.awt.dnd.DropTarget dt = new java.awt.dnd.DropTarget();
        try
        {
            dt.addDropTargetListener(dropListener);
        }   // end try
        catch (java.util.TooManyListenersException e)
        {
            e.printStackTrace();
            log(out, "FileDrop: Drop will not work due to previous error. Do you have another listener attached?");
        }   // end catch

        // Listen for hierarchy changes and remove the drop target when the parent gets cleared out.
        c.addHierarchyListener(new java.awt.event.HierarchyListener()
        {
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt)
            {
                log(out, "FileDrop: Hierarchy changed.");
                java.awt.Component parent = c.getParent();
                if (parent == null)
                {
                    c.setDropTarget(null);
                    log(out, "FileDrop: Drop target cleared from component.");
                }   // end if: null parent
                else
                {
                    new java.awt.dnd.DropTarget(c, dropListener);
                    log(out, "FileDrop: Drop target added to component.");
                }   // end else: parent not null
            }   // end hierarchyChanged
        }); // end hierarchy listener
        if (c.getParent() != null)
        {
            new java.awt.dnd.DropTarget(c, dropListener);
        }

        if (recursive && (c instanceof java.awt.Container))
        {
            // Get the container
            java.awt.Container cont = (java.awt.Container) c;

            // Get it's components
            java.awt.Component[] comps = cont.getComponents();

            // Set it's components as listeners also
            for (int i = 0; i < comps.length; i++)
            {
                makeDropTarget(out, comps[i], recursive);
            }
        }   // end if: recursively set components as listener
    }   // end dropListener

    /**
     * Determine if the dragged data is a file list.
     */
    private boolean isDragOk(final java.io.PrintStream out, final java.awt.dnd.DropTargetDragEvent evt)
    {
        boolean ok = false;

        // Get data flavors being dragged
        java.awt.datatransfer.DataFlavor[] flavors = evt.getCurrentDataFlavors();

        // See if any of the flavors are a file list
        int i = 0;
        while (!ok && i < flavors.length)
        {
            // BEGIN 2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.
            // Is the flavor a file list?
            final DataFlavor curFlavor = flavors[i];
            if (curFlavor.equals(java.awt.datatransfer.DataFlavor.javaFileListFlavor)
                    || curFlavor.isRepresentationClassReader())
            {
                ok = true;
            }
            // END 2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.
            i++;
        }   // end while: through flavors

        // If logging is enabled, show data flavors
        if (out != null)
        {
            if (flavors.length == 0)
            {
                log(out, "FileDrop: no data flavors.");
            }
            for (i = 0; i < flavors.length; i++)
            {
                log(out, flavors[i].toString());
            }
        }   // end if: logging enabled

        return ok;
    }   // end isDragOk

    /**
     * Outputs <tt>message</tt> to <tt>out</tt> if it's not null.
     */
    private static void log(java.io.PrintStream out, String message)
    {   // Log message if requested
        if (out != null)
        {
            out.println(message);
        }
    }   // end log

    public static boolean remove(java.awt.Component c)
    {
        return remove(null, c, true);
    }   // end remove

    public static boolean remove(java.io.PrintStream out, java.awt.Component c, boolean recursive)
    {   // Make sure we support dnd.
        if (supportsDnD())
        {
            log(out, "FileDrop: Removing drag-and-drop hooks.");
            c.setDropTarget(null);
            if (recursive && (c instanceof java.awt.Container))
            {
                java.awt.Component[] comps = ((java.awt.Container) c).getComponents();
                for (int i = 0; i < comps.length; i++)
                {
                    remove(out, comps[i], recursive);
                }
                return true;
            }   // end if: recursive
            else
            {
                return false;
            }
        }   // end if: supports DnD
        else
        {
            return false;
        }
    }   // end remove

    public static interface Listener
    {

        public abstract void filesDropped(java.io.File[] files);

    }   // end inner-interface Listener

    public static class Event extends java.util.EventObject
    {

        private java.io.File[] files;

        public Event(java.io.File[] files, Object source)
        {
            super(source);
            this.files = files;
        }   // end constructor

        public java.io.File[] getFiles()
        {
            return files;
        }   // end getFiles

    }   // end inner class Event

    public static class TransferableObject implements java.awt.datatransfer.Transferable
    {

        public final static String MIME_TYPE = "application/x-net.iharder.dnd.TransferableObject";

        /**
         * The default {@link java.awt.datatransfer.DataFlavor} for
         * {@link TransferableObject} has the representation class
         * <tt>net.iharder.dnd.TransferableObject.class</tt>
         * and the MIME type
         * <tt>application/x-net.iharder.dnd.TransferableObject</tt>.
         *
         * @since 1.1
         */
        public final static java.awt.datatransfer.DataFlavor DATA_FLAVOR
                = new java.awt.datatransfer.DataFlavor(FileDrop.TransferableObject.class, MIME_TYPE);

        private Fetcher fetcher;
        private Object data;

        private java.awt.datatransfer.DataFlavor customFlavor;

        /**
         * Creates a new {@link TransferableObject} that wraps <var>data</var>.
         * Along with the {@link #DATA_FLAVOR} associated with this class, this
         * creates a custom data flavor with a representation class determined
         * from <code>data.getClass()</code> and the MIME type
         * <tt>application/x-net.iharder.dnd.TransferableObject</tt>.
         *
         * @param data The data to transfer
         * @since 1.1
         */
        public TransferableObject(Object data)
        {
            this.data = data;
            this.customFlavor = new java.awt.datatransfer.DataFlavor(data.getClass(), MIME_TYPE);
        }   // end constructor

        /**
         * Creates a new {@link TransferableObject} that will return the object
         * that is returned by <var>fetcher</var>. No custom data flavor is set
         * other than the default {@link #DATA_FLAVOR}.
         *
         * @see Fetcher
         * @param fetcher The {@link Fetcher} that will return the data object
         * @since 1.1
         */
        public TransferableObject(Fetcher fetcher)
        {
            this.fetcher = fetcher;
        }   // end constructor

        /**
         * Creates a new {@link TransferableObject} that will return the object
         * that is returned by <var>fetcher</var>. Along with the
         * {@link #DATA_FLAVOR} associated with this class, this creates a
         * custom data flavor with a representation class <var>dataClass</var>
         * and the MIME type
         * <tt>application/x-net.iharder.dnd.TransferableObject</tt>.
         *
         * @see Fetcher
         * @param dataClass The {@link java.lang.Class} to use in the custom
         * data flavor
         * @param fetcher The {@link Fetcher} that will return the data object
         * @since 1.1
         */
        public TransferableObject(Class dataClass, Fetcher fetcher)
        {
            this.fetcher = fetcher;
            this.customFlavor = new java.awt.datatransfer.DataFlavor(dataClass, MIME_TYPE);
        }   // end constructor

        public java.awt.datatransfer.DataFlavor getCustomDataFlavor()
        {
            return customFlavor;
        }   // end getCustomDataFlavor

        public java.awt.datatransfer.DataFlavor[] getTransferDataFlavors()
        {
            if (customFlavor != null)
            {
                return new java.awt.datatransfer.DataFlavor[]
                {
                    customFlavor,
                    DATA_FLAVOR,
                    java.awt.datatransfer.DataFlavor.stringFlavor
                };  // end flavors array
            }
            else
            {
                return new java.awt.datatransfer.DataFlavor[]
                {
                    DATA_FLAVOR,
                    java.awt.datatransfer.DataFlavor.stringFlavor
                };  // end flavors array
            }
        }   // end getTransferDataFlavors

        /**
         * Returns the data encapsulated in this {@link TransferableObject}. If
         * the {@link Fetcher} constructor was used, then this is when the
         * {@link Fetcher#getObject getObject()} method will be called. If the
         * requested data flavor is not supported, then the
         * {@link Fetcher#getObject getObject()} method will not be called.
         *
         * @param flavor The data flavor for the data to return
         * @return The dropped data
         * @since 1.1
         */
        public Object getTransferData(java.awt.datatransfer.DataFlavor flavor)
                throws java.awt.datatransfer.UnsupportedFlavorException, java.io.IOException
        {
            // Native object
            if (flavor.equals(DATA_FLAVOR))
            {
                return fetcher == null ? data : fetcher.getObject();
            }

            // String
            if (flavor.equals(java.awt.datatransfer.DataFlavor.stringFlavor))
            {
                return fetcher == null ? data.toString() : fetcher.getObject().toString();
            }

            // We can't do anything else
            throw new java.awt.datatransfer.UnsupportedFlavorException(flavor);
        }   // end getTransferData

        /**
         * Returns <tt>true</tt> if <var>flavor</var> is one of the supported
         * flavors. Flavors are supported using the <code>equals(...)</code>
         * method.
         *
         * @param flavor The data flavor to check
         * @return Whether or not the flavor is supported
         * @since 1.1
         */
        public boolean isDataFlavorSupported(java.awt.datatransfer.DataFlavor flavor)
        {
            // Native object
            if (flavor.equals(DATA_FLAVOR))
            {
                return true;
            }

            // String
            if (flavor.equals(java.awt.datatransfer.DataFlavor.stringFlavor))
            {
                return true;
            }

            // We can't do anything else
            return false;
        }   // end isDataFlavorSupported


        /* ********  I N N E R   I N T E R F A C E   F E T C H E R  ******** */
        /**
         * Instead of passing your data directly to the
         * {@link TransferableObject} constructor, you may want to know exactly
         * when your data was received in case you need to remove it from its
         * source (or do anyting else to it). When the
         * {@link #getTransferData getTransferData(...)} method is called on the
         * {@link TransferableObject}, the {@link Fetcher}'s
         * {@link #getObject getObject()} method will be called.
         *
         * @author Robert Harder
         * @copyright 2001
         * @version 1.1
         * @since 1.1
         */
        public static interface Fetcher
        {

            /**
             * Return the object being encapsulated in the
             * {@link TransferableObject}.
             *
             * @return The dropped object
             * @since 1.1
             */
            public abstract Object getObject();
        }   // end inner interface Fetcher

    }   // end class TransferableObject

}   // end class FileDrop
