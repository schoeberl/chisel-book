
import chisel3._

//- start usepacket2
//- 开始使用第二个包裹
class AbcUser2 extends Module {
  val io = IO(new Bundle{})

  val abc = new mypack.Abc()

}
//- end
//- 结束

//- start usepacket3
//- 开始使用第三个包裹
import mypack.Abc

class AbcUser3 extends Module {
  val io = IO(new Bundle{})

  val abc = new Abc()

}
//- end
//- 结束

//- start usepacket
//- 开始使用包裹
import mypack._

class AbcUser extends Module {
  val io = IO(new Bundle{})

  val abc = new Abc()

}
//- end
//- 结束