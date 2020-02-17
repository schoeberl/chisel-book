#!/usr/bin/env amm

// Convert Markdown to HTML

import $ivy.`org.jbibtex:jbibtex:1.0.17`, org.jbibtex._


import $ivy.`org.planet42::laika-core:0.12.0`
import $ivy.`org.planet42::laika-io:0.12.0`
import laika.api._
import laika.format._
import laika.io.implicits._

import scala.io.Source
import java.io._

val transformer = Transformer
  .from(Markdown)
  .to(HTML)
  .build

var file = "course_www.md"
// file = "../../../Dropbox/trunksync/notes/StudentProjects.markdown"

val in = Source.fromFile(file).getLines.mkString("\n")
val res = transformer.transform(in).toString
val content = res.substring(6, res.length-1) // Something strange is part of the output (Right(...))


val intro = """
<!DOCTYPE html>
<html>
  <head>
    <meta content="text/html; charset=utf-8" http-equiv="content-type">
    <title>DTU 02139: Digital Electronics 2</title></head>
    <style type="text/css">
      body { font-family:Helvetica,Arial,sans-serif;
             color:#303030;background-color:#ffffff;
             width:auto;
             margin-left:5em; margin-right:5em; }
      a { color:#306090; text-decoration:none; }
      a:hover { color:#4080c0; text-decoration:none; }

      h1.headbox { font-size:300%; margin-bottom:0px; }
      p.headbox { font-size:150%; margin-top:0px; }
    </style>
  </head>
  <body>
"""

val outro = """
  </body>
</html>
"""

val page = intro + content + outro
println(page)