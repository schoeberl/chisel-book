
// This should be placed in sub folder mypack, by the extraction script currently looks just into this folder
// 这个应该放在mypack的文件夹下一级，但是提取脚本的过程，看起来它像是在这个文件夹

//- start packet
//- 开始包裹
package mypack

import chisel3._

class Abc extends Module {
  val io = IO(new Bundle{})
}
//- end
//- 结束






