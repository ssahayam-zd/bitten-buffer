package zd.bitten

import weaver.monixcompat.SimpleTaskSuite
import weaver.scalacheck.Checkers
import io.circe.Json
import io.circe.ParsingFailure
import io.circe.Parser
import io.circe.Printer
import io.circe.jawn.parseByteArray
import io.circe.jawn.parseByteBuffer
import io.circe.literal._
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets


// Requirement: Convert a Json payload into bytes and create an EnginePosition.
// The EnginePosition is transfer to the Writer and subsequently returned to be 
// persisted into the JobStore so save our move progress. These bytes would
// then be decoded into an EnginePosition when resuming a move.
object JsonParserSuite extends SimpleTaskSuite with Checkers {

  private val jsonContent = json"""
      {
        "myKey": "myValue"
      }
    """


  pureTest("A JsonParser should parse Json - parseByteArray") {
    val byteBuffer: ByteBuffer = Printer.noSpaces.printToByteBuffer(jsonContent)
    val bytes = byteBuffer.array()

    val resultE:  Either[ParsingFailure, Json] = parseByteArray(bytes)

    expect.eql(Right(jsonContent), resultE)
  }

  pureTest("A JsonParser should parse Json - String") {
    val jsonString: String = jsonContent.noSpaces

    val bytes = jsonString.getBytes(StandardCharsets.UTF_8)

    val resultE:  Either[ParsingFailure, Json] = parseByteArray(bytes)

    expect.eql(Right(jsonContent), resultE)
  } 

  pureTest("Show the diff") {
    val jsonString: String = jsonContent.noSpaces
    val stringBytes = jsonString.getBytes(StandardCharsets.UTF_8)

    val byteBuffer: ByteBuffer = Printer.noSpaces.printToByteBuffer(jsonContent)
    val byteBufferBytes = byteBuffer.array()

    printArray("stringBytes", stringBytes)
    printArray("byteBufferBytes", byteBufferBytes)

    println(s"capacity: ${byteBuffer.capacity()}")
    println(s"limit: ${byteBuffer.limit()}")
    println(s"offset: ${byteBuffer.arrayOffset()}")
    println(s"remaining: ${byteBuffer.remaining()}")

    expect(true)
  }

  private def printArray(banner: String, values: Array[Byte]): Unit = {
    println(s"${"=" * 10}> $banner")

    values.zipWithIndex.foreach { 
      case (b, index) => 
        val charValue = b.asInstanceOf[Char]
        val result = s"[$index]=${charValue} (${b})"
        println(result)
    }
    
  }  

  pureTest("A JsonParser should parse Json - parseByteBuffer") {
    val byteBuffer: ByteBuffer = Printer.noSpaces.printToByteBuffer(jsonContent)

    val resultE:  Either[ParsingFailure, Json] = parseByteBuffer(byteBuffer)

    expect.eql(Right(jsonContent), resultE)
  }

  //https://github.com/circe/circe/blob/e6685317bc9e6e65539bb6194900e0e754fbcb7c/modules/tests/shared/src/main/scala/io/circe/tests/PrinterSuite.scala#L23
  pureTest("A JsonParser should parse Json - parseByteArray - take 2") {
    val byteBuffer: ByteBuffer = Printer.noSpaces.printToByteBuffer(jsonContent)
    val bytes = new Array[Byte](byteBuffer.limit)
    byteBuffer.get(bytes)

    val resultE:  Either[ParsingFailure, Json] = parseByteArray(bytes)

    expect.eql(Right(jsonContent), resultE)
  }  

  pureTest("A JsonParser should parse Json - parseByteArray - take 3") {
    val byteBuffer: ByteBuffer = Printer.noSpaces.printToByteBuffer(jsonContent)
    val bytes = new Array[Byte](byteBuffer.remaining())
    byteBuffer.get(bytes)

    val resultE:  Either[ParsingFailure, Json] = parseByteArray(bytes)

    expect.eql(Right(jsonContent), resultE)
  } 



 // <p> A buffer's <i>capacity</i> is the number of elements it (can) contain.  The
 // capacity of a buffer is never negative and never changes.  </p>
 //
 // <p> A buffer's <i>limit</i> is the index of the first element that should
 // not be read or written.  A buffer's limit is never negative and is never
 // greater than its capacity.  </p>
 //
 // <p> A buffer's <i>position</i> is the index of the next element to be
 // read or written.  A buffer's position is never negative and is never
 // greater than its limit.  </p>  

 // arrayOffset - offset within this buffer's backing array of the first
//  element of the buffer





//TL;DR: When using a ByteBuffer ensure to copy only the data in the buffer 
//and not the full buffer. You can do this with the .get function that takes
//in an appropriately-sized array.
}

