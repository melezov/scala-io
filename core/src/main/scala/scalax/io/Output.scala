/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2009-2010, Jesse Eichar             **
**  __\ \/ /__/ __ |/ /__/ __ |    http://scala-lang.org/               **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */

package scalax.io

import resource._
import scala.collection.Traversable
import java.io.{File => JFile, OutputStream}

/**
 * A trait for objects that can have data written to them. For example an
 * OutputStream and File can be an Output object (or be converted to one).
 *
 * <p>
 * Note: Each invocation of a method will typically open a new stream or
 * channel.  That behaviour can be overridden by the implementation but
 * it is the default behaviour.
 * </p>
 *
 * @author Jesse Eichar
 * @since 1.0
 *
 * @see ReadBytes
 * @see Input
 */
trait Output {

  protected def underlyingOutput : OutputResource[OutputStream]

  /**
   * Write data to the underlying object.  In the case of writing ints and bytes it is often
   * recommended to write arrays of data since normally the underlying object can write arrays
   * of bytes or integers most efficiently.
   * <p>
   * Since Characters require a codec to write to an OutputStream characters cannot be written with this method
   * unless a OutputWriterFunction.CharFunction object is provided as the writer.
   * </p>
   * @see #writeChars for more on writing characters
   *
   *
   * @param data
   *          The data to write to underlying object
   * @param writer
   *          The strategy used to write the data to the underlying object.  Many standard data-types are implicitly
   *          resolved and do not need to be supplied
   */
  def write[T](data: TraversableOnce[T])(implicit writer:OutputConverter[T]) : Unit = {
    underlyingOutput.foreach {writer(_,data)}
  }
  /**
  * Writes a string.
  *
  * @param string
  *          the data to write
  * @param codec
  *          the codec of the string to be written. The string will
  *          be converted to the encoding of {@link sourceCodec}
  *          Default is sourceCodec
  */
  def write(string: String)(implicit codec: Codec): Unit = {
      underlyingOutput.writer writeString string
  }

  /*
   * Writes Characters to the underlying object.
   *
   * @param characters the characters to write
   * @param codec the codec to use for encoding the characters
   */
  def writeChars(characters: TraversableOnce[Char])(implicit codec: Codec) : Unit = {
    write(characters)(OutputConverter.charsToOutputFunction)
  }

  /**
  * Write several strings.
  *
  * @param strings
  *          The data to write
  * @param separator
  *          A string to add between each string.
  *          It is not added to the before the first string
  *          or after the last.
  * @param codec
  *          The codec of the strings to be written. The strings will
  *          be converted to the encoding of {@link sourceCodec}
  */
  def writeStrings(strings: Traversable[String], separator:String = "")(implicit codec: Codec): Unit = {
      underlyingOutput.writer.writeStrings(strings,separator)
  }
}
