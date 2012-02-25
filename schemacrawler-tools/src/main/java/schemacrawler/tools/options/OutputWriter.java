/* 
 *
 * SchemaCrawler
 * http://sourceforge.net/projects/schemacrawler
 * Copyright (c) 2000-2012, Sualeh Fatehi.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 */
package schemacrawler.tools.options;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import schemacrawler.schemacrawler.SchemaCrawlerException;

public final class OutputWriter
  extends Writer
{

  private static final Logger LOGGER = Logger.getLogger(OutputWriter.class
    .getName());

  private final Writer writer;
  private boolean isClosed;
  private boolean isFileOutput;

  public OutputWriter(final OutputOptions outputOptions)
    throws SchemaCrawlerException
  {
    writer = openOutputWriter(outputOptions);
  }

  @Override
  public Writer append(final char c)
    throws IOException
  {
    checkOpen();
    return writer.append(c);
  }

  @Override
  public Writer append(final CharSequence csq)
    throws IOException
  {
    checkOpen();
    return writer.append(csq);
  }

  @Override
  public Writer append(final CharSequence csq, final int start, final int end)
    throws IOException
  {
    checkOpen();
    return writer.append(csq, start, end);
  }

  @Override
  public void close()
    throws IOException
  {
    checkOpen();

    if (writer != null)
    {
      writer.flush();
    }

    if (isFileOutput)
    {
      if (writer != null)
      {
        writer.close();
        LOGGER.log(Level.INFO, "Closed output writer");
      }
    }
    else
    {
      LOGGER.log(Level.INFO,
                 "Not closing output writer, since output is not to a file");
    }

    isClosed = true;
  }

  @Override
  public void flush()
    throws IOException
  {
    checkOpen();
    writer.flush();
  }

  @Override
  public void write(final char[] cbuf)
    throws IOException
  {
    checkOpen();
    writer.write(cbuf);
  }

  @Override
  public void write(final char[] cbuf, final int off, final int len)
    throws IOException
  {
    checkOpen();
    writer.write(cbuf, off, len);
  }

  @Override
  public void write(final int c)
    throws IOException
  {
    checkOpen();
    writer.write(c);
  }

  @Override
  public void write(final String str)
    throws IOException
  {
    checkOpen();
    writer.write(str);
  }

  @Override
  public void write(final String str, final int off, final int len)
    throws IOException
  {
    checkOpen();
    writer.write(str, off, len);
  }

  @Override
  protected void finalize()
    throws Throwable
  {
    super.finalize();
    if (!isClosed)
    {
      throw new IllegalStateException("Output writer was not closed");
    }
  }

  private void checkOpen()
    throws IllegalAccessError
  {
    if (isClosed)
    {
      throw new IllegalAccessError("Writer has already been closed");
    }
  }

  /**
   * Opens the output writer.
   * 
   * @return Writer
   * @throws SchemaCrawlerException
   *         On an exception
   */
  private Writer openOutputWriter(final OutputOptions outputOptions)
    throws SchemaCrawlerException
  {
    try
    {
      final Writer writer;
      if (outputOptions == null || outputOptions.isConsoleOutput())
      {
        writer = new OutputStreamWriter(System.out);
        LOGGER.log(Level.INFO, "Opened output writer to console");
      }
      else if (outputOptions.getWriter() != null)
      {
        writer = outputOptions.getWriter();
        LOGGER.log(Level.INFO, "Output to provided writer");
      }
      else
      {
        isFileOutput = true;
        final File outputFile = outputOptions.getOutputFile();
        writer = new FileWriter(outputFile, outputOptions.isAppendOutput());
        LOGGER.log(Level.INFO,
                   "Opened output writer to file, "
                       + outputFile.getAbsolutePath());
      }
      return writer;
    }
    catch (final Exception e)
    {
      throw new SchemaCrawlerException("Could not obtain output writer", e);
    }
  }

}
