//- start file_reader
//- 开始文件读取
import chisel3._
import scala.io.Source

class FileReader extends Module {
  val io = IO(new Bundle {
    val address = Input(UInt(8.W))
    val data = Output(UInt(8.W))
  })

  val array = new Array[Int](256)
  var idx = 0

  // read the data into a Scala array
  // 读取数据变成scala数组
  val source = Source.fromFile("data.txt")
  for (line <- source.getLines()) {
    array(idx) = line.toInt
    idx += 1
  }

  // convert the Scala integer array into the Chisel type Vec
  // 把scala整型数组变成chisel的矢量类型
  val table = VecInit(array.map(_.U(8.W)))

  // use the table
  // 使用表格
  io.data := table(io.address)
}
//- end
// 结束
