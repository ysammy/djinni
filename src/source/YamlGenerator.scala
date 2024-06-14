/**
  * Copyright 2014 Dropbox, Inc.
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *    http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  * 
  * This file has been modified by Snap, Inc.
  */

package djinni

import djinni.ast._
import djinni.ast.Record.DerivingType.DerivingType
import djinni.generatorTools._
import djinni.meta._
import djinni.writer.IndentWriter
import java.util.{Map => JMap}
import scala.collection.JavaConversions._
import scala.collection.mutable

class YamlGenerator(spec: Spec) extends Generator(spec) {

  val cppMarshal = new CppMarshal(spec)
  val objcMarshal = new ObjcMarshal(spec)
  val objcppMarshal = new ObjcppMarshal(spec)
  val javaMarshal = new JavaMarshal(spec)
  val jniMarshal = new JNIMarshal(spec)
  val wasmMarshal = new WasmGenerator(spec)
  val composerMarshal = new ComposerGenerator(spec)
  val tsMarshal = new TsGenerator(spec, false)
  val swiftMarshal = new SwiftMarshal(spec)
  val swiftxxMarshal = new SwiftxxMarshal(spec)

  case class QuotedString(str: String) // For anything that migt require escaping

  private def writeYamlFile(name: String, origin: String, f: IndentWriter => Unit): Unit = {
    createFile(spec.yamlOutFolder.get, name, out => new IndentWriter(out, "  "), w => {
      w.wl("# AUTOGENERATED FILE - DO NOT MODIFY!")
      w.wl("# This file was generated by Djinni from " + origin)
      f(w)
    })
  }

  private def writeYamlFile(tds: Seq[InternTypeDecl]): Unit = {
    val origins = tds.map(_.origin).distinct.sorted.mkString(", ")
    writeYamlFile(spec.yamlOutFile.get, origins, w => {
      // Writing with SnakeYAML creates loads of cluttering and unnecessary tags, so write manually.
      // We're not doing anything complicated anyway and it's good to have human readable output.
      for(td <- tds) {
        w.wl("---")
        write(w, td)
      }
    })
  }

  private def writeYamlFile(ident: String, origin: String, td: InternTypeDecl): Unit =
    writeYamlFile(spec.yamlPrefix + ident + ".yaml", origin, w => {
      write(w, td)
  })

  private def write(w: IndentWriter, td: TypeDecl) {
    write(w, preamble(td))
    w.wl("cpp:").nested { write(w, cpp(td)) }
    w.wl("objc:").nested { write(w, objc(td)) }
    w.wl("objcpp:").nested { write(w, objcpp(td)) }
    w.wl("java:").nested { write(w, java(td)) }
    w.wl("jni:").nested { write(w, jni(td)) }
    if (spec.wasmOutFolder.isDefined) {
      w.wl("wasm:").nested { write(w, wasm(td)) }
    }
    if (spec.composerOutFolder.isDefined) {
      w.wl("composer:").nested { write(w, composer(td)) }
    }
    if (spec.wasmOutFolder.isDefined || spec.composerOutFolder.isDefined) {
      w.wl("ts:").nested {write(w, ts(td)) }
    }
    if (spec.swiftOutFolder.isDefined) {
      w.wl("swift:").nested {write(w, swift(td))}
    }
  }

  private def write(w: IndentWriter, m: Map[String, Any]) {
    for((k, v) <- m) {
      w.w(k + ": ")
      v match {
        case s: String => write(w, s)
        case s: QuotedString => write(w, s)
        case m: Map[_, _] => w.wl.nested { write(w, m.asInstanceOf[Map[String, Any]]) }
        case s: Seq[_] => write(w, s)
        case b: Boolean => write(w, b)
        case _ => throw new AssertionError("unexpected map value")
      }
    }
  }

  private def write(w: IndentWriter, s: Seq[Any]) {
    // The only arrays we have are small enough to use flow notation
    w.wl(s.mkString("[", ",", "]"))
  }

  private def write(w: IndentWriter, b: Boolean) {
    w.wl(if(b) "true" else "false")
  }

  private def write(w: IndentWriter, s: String) {
    if(s.isEmpty) w.wl(q("")) else w.wl(s)
  }

  private def write(w: IndentWriter, s: QuotedString) {
    if(s.str.isEmpty) w.wl(q("")) else w.wl("'" + s.str.replaceAllLiterally("'", "''") + "'")
  }

  private def preamble(td: TypeDecl) = Map[String, Any](
    "name" -> (spec.yamlPrefix + td.ident.name),
    "typedef" -> QuotedString(typeDef(td)),
    "params" -> td.params.collect { case p: TypeParam => p.ident.name },
    "prefix" -> spec.yamlPrefix
  )

  private def typeDef(td: TypeDecl) = {
    def ext(e: Ext): String = (if(e.cpp) " +c" else "") + (if(e.objc) " +o" else "") + (if(e.java) " +j" else "") + (if(e.js) " +w" else "")
    def deriving(r: Record) = {
      if(r.derivingTypes.isEmpty) {
        ""
      } else {
        r.derivingTypes.collect {
          case Record.DerivingType.Eq => "eq"
          case Record.DerivingType.Ord => "ord"
          case Record.DerivingType.AndroidParcelable => "parcelable"
          case Record.DerivingType.NSCopying => "nscopying"
          case Record.DerivingType.Req => "req"
        }.mkString(" deriving(", ", ", ")")
      }
    }
    td.body match {
      case i: Interface => "interface" + ext(i.ext)
      case r: Record => "record" + ext(r.ext) + deriving(r)
      case p: ProtobufMessage => "protobuf"
      case Enum(_, false) => "enum"
      case Enum(_, true) => "flags"
    }
  }

  private def cpp(td: TypeDecl) = Map[String, Any](
    "typename" -> QuotedString(cppMarshal.fqTypename(td.ident, td.body)),
    "header" -> QuotedString(cppMarshal.include(td.ident)),
    "byValue" -> cppMarshal.byValue(td)
  )

  private def objc(td: TypeDecl) = {
    val map = Map[String, Any](
      "typename" -> QuotedString(objcMarshal.fqTypename(td.ident, td.body)),
      "header" -> QuotedString(objcMarshal.include(td.ident)),
      "boxed" -> QuotedString(objcMarshal.boxedTypename(td)),
      "pointer" -> objcMarshal.isPointer(td),
      // "generic" -> false,
      "hash" -> QuotedString("%s.hash"))
    td.body match {
      case Interface(_,_,_) =>
        if (spec.objcGenProtocol)
          map + ("protocol" -> spec.objcGenProtocol)
        else
          map
      case _ => map
    }
  }

  private def objcpp(td: TypeDecl) = Map[String, Any](
    "translator" -> QuotedString(objcppMarshal.helperName(mexpr(td))),
    "header" -> QuotedString(objcppMarshal.include(meta(td)))
  )

  private def java(td: TypeDecl) = Map[String, Any](
    "typename" -> QuotedString(javaMarshal.fqTypename(td.ident, td.body)),
    "boxed" -> QuotedString(javaMarshal.fqTypename(td.ident, td.body)),
    "reference" -> javaMarshal.isReference(td),
    // "generic" -> false,
    "generic" -> true,
    "hash" -> QuotedString("%s.hashCode()"),
    "writeToParcel" -> QuotedString("%s.writeToParcel(out, flags)"),
    "readFromParcel" -> QuotedString("new %s(in)")
  )

  private def jni(td: TypeDecl) = Map[String, Any](
    "translator" -> QuotedString(jniMarshal.helperName(mexpr(td))),
    "header" -> QuotedString(jniMarshal.include(td.ident)),
    "typename" -> jniMarshal.fqParamType(mexpr(td)),
    "typeSignature" -> QuotedString(jniMarshal.fqTypename(td.ident, td.body))
  )

  private def wasm(td: TypeDecl) = Map[String, Any](
    "translator" -> QuotedString(wasmMarshal.helperName(mexpr(td))),
    "header" -> QuotedString(wasmMarshal.include(td.ident)),
    "typename" -> wasmMarshal.wasmType(mexpr(td))
  )

  private def composer(td: TypeDecl) = Map[String, Any](
    "translator" -> QuotedString(composerMarshal.helperName(mexpr(td))),
    "header" -> QuotedString(composerMarshal.include(td.ident))
  )

  private def ts(td: TypeDecl) = Map[String, Any](
    "typename" -> tsMarshal.toTsType(mexpr(td), /*addNullability*/ false),
    "module" -> QuotedString("./" + spec.tsModule)
    //, "generic" -> false
  )

  private def swift(td: TypeDecl) = Map[String, Any](
    "typename" -> QuotedString(swiftMarshal.fqTypename(td.ident, td.body)),
    "module" -> QuotedString(spec.swiftModule)
  )

  // TODO: there has to be a way to do all this without the MExpr/Meta conversions?
  private def mexpr(td: TypeDecl) = MExpr(meta(td), List())

  private def meta(td: TypeDecl) = {
    td match {
      case p: ProtobufTypeDecl => MProtobuf(p.ident.name, 0, p.body.asInstanceOf[ProtobufMessage])
      case _ =>
        val defType = td.body match {
          case i: Interface => DInterface
          case r: Record => DRecord
          case e: Enum => DEnum
          case p: ProtobufMessage => throw new AssertionError("unreachable")
        }
        MDef(td.ident, 0, defType, td.body)
    }
  }

  override def generate(idl: Seq[TypeDecl]) {
    val internOnly = idl.collect { case itd: InternTypeDecl => itd }.sortWith(_.ident.name < _.ident.name)
    if(spec.yamlOutFile.isDefined) {
      writeYamlFile(internOnly)
    } else {
      for(td <- internOnly) {
        writeYamlFile(td.ident, td.origin, td)
      }
    }
  }

  override def generateEnum(origin: String, ident: Ident, doc: Doc, e: Enum) {
    // unused
  }

  override def generateInterface(origin: String, ident: Ident, doc: Doc, typeParams: Seq[TypeParam], i: Interface) {
    // unused
  }

  override def generateRecord(origin: String, ident: Ident, doc: Doc, params: Seq[TypeParam], r: Record) {
    // unused
  }
}

object YamlGenerator {
  def metaFromYaml(td: ExternTypeDecl) = MExtern(
    td.ident.name.stripPrefix(td.properties("prefix").toString), // Make sure the generator uses this type with its original name for all intents and purposes
    td.params.size,
    defType(td),
    td.body,
    MExtern.Cpp(
      nested(td, "cpp")("typename").toString,
      nested(td, "cpp")("header").toString,
      nested(td, "cpp")("byValue").asInstanceOf[Boolean],
      getOptionalField(td, "cpp", "moveOnly", false)),
    MExtern.Objc(
      nested(td, "objc")("typename").toString,
      nested(td, "objc")("header").toString,
      nested(td, "objc")("boxed").toString,
      nested(td, "objc")("pointer").asInstanceOf[Boolean],
      getOptionalField(td, "objc", "generic", false),
      nested(td, "objc")("hash").toString,
      getOptionalField(td, "objc", "protocol", false)),
    MExtern.Objcpp(
      nested(td, "objcpp")("translator").toString,
      nested(td, "objcpp")("header").toString),
    MExtern.Java(
      nested(td, "java")("typename").toString,
      nested(td, "java")("boxed").toString,
      nested(td, "java")("reference").asInstanceOf[Boolean],
      nested(td, "java")("generic").asInstanceOf[Boolean],
      nested(td, "java")("hash").toString,
      getOptionalField(td, "java", "writeToParcel", "%s.writeToParcel(out, flags)"),
      getOptionalField(td, "java", "readFromParcel", "new %s(in)")),
    MExtern.Jni(
      nested(td, "jni")("translator").toString,
      nested(td, "jni")("header").toString,
      nested(td, "jni")("typename").toString,
      nested(td, "jni")("typeSignature").toString),
    MExtern.Wasm(
      getOptionalField(td, "wasm", "typename"),
      getOptionalField(td, "wasm", "translator"),
      getOptionalField(td, "wasm", "header")),
    MExtern.Composer(
      getOptionalField(td, "composer", "translator"),
      getOptionalField(td, "composer", "header")),
    MExtern.Ts(
      getOptionalField(td, "ts", "typename"),
      getOptionalField(td, "ts", "module"),
      getOptionalField(td, "ts", "generic", false)),
    MExtern.Swift(
      getOptionalField(td, "swift", "typename"),
      getOptionalField(td, "swift", "module"),
      getOptionalField(td, "swift", "translator"),
      getOptionalField(td, "swift", "generic", false)),
    MExtern.Swiftxx(
      getOptionalField(td, "swiftxx", "translator"),
      getOptionalField(td, "swiftxx", "header"))
  )

  private def nested(td: ExternTypeDecl, key: String) = {
    td.properties.get(key).collect { case m: JMap[_, _] => m.collect { case (k: String, v: Any) => (k, v) } } getOrElse(Map[String, Any]())
  }

  private def getOptionalField[T](td: ExternTypeDecl, key: String, subKey: String, defVal: T) = {
    if (nested(td, key) contains subKey)
      nested(td, key)(subKey).asInstanceOf[T]
    else defVal
  }

  private def getOptionalField(td: ExternTypeDecl, key: String, subKey: String) = {
    try {
      nested(td, key)(subKey).toString
    } catch {
      case e: java.util.NoSuchElementException => {
        // println(s"Warning: in ${td.origin}, missing field $key/$subKey")
        "[unspecified]"
      }
    }
  }

  private def defType(td: ExternTypeDecl) = td.body match {
    case i: Interface => DInterface
    case r: Record => DRecord
    case e: Enum => DEnum
    case p: ProtobufMessage => throw new AssertionError("unreachable")
  }
}
