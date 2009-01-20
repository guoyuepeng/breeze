// script to generate counter classes of the appropriate kinds

/*
 Copyright 2009 David Hall, Daniel Ramage
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at 
 
 http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License. 
*/

object GenCounter {
  val valueTypes = List("Int","Double","Float","Short","Long");
  val keyTypes =  None :: valueTypes.map(Some[String]);

  def main(args: Array[String]) {
    import java.io.{File, FileWriter};
    if (args.length != 1) {
      println("please give path of output directory")
      exit(-1)
    }

    for(k <-keyTypes;
      v<- valueTypes) {
      val dir = new File(args(0) + File.separator + mkPath(k,v));
      dir.mkdirs();
      val f = new File(dir + File.separator + mkName(k,v).replaceAll("\\[T\\]","") + ".scala");
      f.createNewFile();
      val out = new FileWriter(f);
      println(mkName(k,v));
      out.write(mkCounter(k,v));
      out.close();
    }
  }

  def mkPath(key : Option[String], value:String) = {
    val dir = "scalanlp/counters" + (key match {
      case Some(k) => "/" + k.toLowerCase + "s/";
      case None => "/"
    })
    dir
  }
  def mkFastMap(key : Option[String], value:String) = {
    val prefix = key match {
      case Some(k) => k + "2"+ value
      case None => "Object2" + value
    }

    prefix + "OpenHashMap" + (key match {
      case Some(k) => ""
      case None => "[T]"
    })
  }
  def mkName(key : Option[String], value:String) = {
    val prefix = key match {
      case Some(k) => k + "2"+ value
      case None => value
    }

    prefix + "Counter" + (key match {
      case Some(k) => ""
      case None => "[T]"
    })
  }

  def mkCounter(key : Option[String], value : String) = {
    val V = value;
    val T = key.getOrElse("T");
    val sizeType = value match {
      case "Byte" | "Char" | "Short" => "Int"
      case x => x
    }

    val isGeneric = key.isEmpty;
    val G = if(isGeneric) "[T]" else "";

    val PACKAGE = key match {
      case None => "scalanlp.counters";
      case Some(k) => "scalanlp.counters." + k.toLowerCase + "s";
    }

    val superClass = if(isGeneric) {
      "Map[" + T + ","+V+"]"
    } else {
      V + "Counter["+T+"]";
    }

    val hasCounterParent  = !isGeneric

    val ov = if(hasCounterParent) "override " else "";

    val DOUBLE_COUNTER = mkName(key,"Double");
    val COUNTER = mkName(key,value);
<x>// THIS IS AN AUTO-GENERATED FILE. DO NOT MODIFY.    
// generated by GenCounter on {new java.util.Date().toString()}
package {PACKAGE};

import scala.collection.mutable.Map;
import scala.collection.mutable.HashMap;

/**
 * Count objects of type {T} with type {V}.
 * This trait is a wrapper around Scala's Map trait
 * and can work with any scala Map. 
 *
 * @author dlwh
 */
@serializable 
trait {COUNTER} extends {superClass} {{
{if(!hasCounterParent) {
  <x>
  private var pTotal: {sizeType} = 0;

  /**
   * Return the sum of all values in the map.
   */
  def total() = pTotal;

  final protected def updateTotal(delta : {sizeType}) {{
    pTotal += delta;
  }}

  override def clear() {{
    pTotal = 0;
    super.clear();
  }}
</x>.text
} else {""}}

  abstract override def update(k : {T}, v : {V}) = {{
{if(!hasCounterParent) { <x>    updateTotal(v - this(k))</x>.text } else {""}}
    super.update(k,v);
  }}

  // this isn't necessary, except that the jcl MapWrapper overrides put to call Java's put directly.
  override def put(k : {T}, v : {V}) :Option[{V}] = {{ val old = get(k); update(k,v); old}}

  abstract override def -=(key : {T}) = {{
{if(!hasCounterParent) {
  <x>
    updateTotal(-this(key))
</x>.text
} else {""}}
    super.-=(key);
  }}

  /**
   * Increments the count by the given parameter.
   */
   {ov} def incrementCount(t : {T}, v : {V}) = {{
     update(t,(this(t) + v).asInstanceOf[{V}]);
   }}


  override def ++=(kv: Iterable[({T},{V})]) = kv.foreach(+=);

  /**
   * Increments the count associated with {T} by {V}.
   * Note that this is different from the default Map behavior.
  */
  override def +=(kv: ({T},{V})) = incrementCount(kv._1,kv._2);

  override def default(k : {T}) : {V} = 0;

  override def apply(k : {T}) : {V} = super.apply(k);

  // TODO: clone doesn't seem to work. I think this is a JCL bug.
  override def clone(): {COUNTER}  = super.clone().asInstanceOf[{COUNTER}]

  /**
   * Return the {T} with the largest count
   */
  {ov} def argmax() : {T} = (elements reduceLeft ((p1:({T},{V}),p2:({T},{V})) => if (p1._2 &gt; p2._2) p1 else p2))._1

  /**
   * Return the {T} with the smallest count
   */
  {ov} def argmin() : {T} = (elements reduceLeft ((p1:({T},{V}),p2:({T},{V})) => if (p1._2 &lt; p2._2) p1 else p2))._1

  /**
   * Return the largest count
   */
  {ov} def max : {V} = values reduceLeft ((p1:{V},p2:{V}) => if (p1 > p2) p1 else p2)
  /**
   * Return the smallest count
   */
  {ov} def min : {V} = values reduceLeft ((p1:{V},p2:{V}) => if (p1 &lt; p2) p1 else p2)

  // TODO: decide is this is the interface we want?
  /**
   * compares two objects by their counts
   */ 
  {ov} def comparator(a : {T}, b :{T}) = apply(a) compare apply(b);

  /**
   * Return a new {DOUBLE_COUNTER} with each {V} divided by the total;
   */
  {ov} def normalized() : {DOUBLE_COUNTER} = {{
    val normalized = {DOUBLE_COUNTER}();
    val total : Double = this.total
    if(total != 0.0)
      for (pair &lt;- elements) {{
        normalized(pair._1) = pair._2 / total;
      }}
    normalized
  }}

  /**
   * Return the sum of the squares of the values
   */
  {ov} def l2norm() : Double = {{
    var norm = 0.0
    for (val v &lt;- values) {{
      norm += (v * v)
    }}
    return Math.sqrt(norm)
  }}

  /**
   * Return a List the top k elements, along with their counts
   */
  {ov} def topK(k : Int) = Counters.topK[({T},{V})](k,(x,y) => if(x._2 &lt; y._2) -1 else if (x._2 == y._2) 0 else 1)(this);

  /**
   * Return \sum_(t) C1(t) * C2(t). 
   */
  def dot(that : {COUNTER}) : Double = {{
    var total = 0.0
    for (val (k,v) &lt;- that.elements) {{
      total += get(k).asInstanceOf[Double] * v
    }}
    return total
  }}

  def +=(that : {COUNTER}) {{
    for(val (k,v) &lt;- that.elements) {{
      update(k,(this(k) + v).asInstanceOf[{V}]);
    }}
  }}

  def -=(that : {COUNTER}) {{
    for(val (k,v) &lt;- that.elements) {{
      update(k,(this(k) - v).asInstanceOf[{V}]);
    }}
  }}

  {ov} def *=(scale : {V}) {{
    transform {{ (k,v) => (v * scale).asInstanceOf[{V}]}}
  }}

  {ov} def /=(scale : {V}) {{
    transform {{ (k,v) => (v / scale).asInstanceOf[{V}]}}
  }}
}}


object {COUNTER.replaceAll("\\[T\\]","")} {{
  import it.unimi.dsi.fastutil.objects._
  import it.unimi.dsi.fastutil.ints._
  import it.unimi.dsi.fastutil.shorts._
  import it.unimi.dsi.fastutil.longs._
  import it.unimi.dsi.fastutil.floats._
  import it.unimi.dsi.fastutil.doubles._
{if(isGeneric) {<x>
  import scalanlp.counters.ints._
  import scalanlp.counters.shorts._
  import scalanlp.counters.longs._
  import scalanlp.counters.floats._
  import scalanlp.counters.doubles._
</x>.text} 
else ""
}

  import scala.collection.jcl.MapWrapper;
  @serializable
  @SerialVersionUID(1L)
  class FastMapCounter{G} extends MapWrapper[{T},{V}] with {COUNTER} {{
    private val under = new {mkFastMap(key,value)};
    def underlying() = under.asInstanceOf[java.util.Map[{T},{V}]];
    {// fastutil says getInt for Object2IntMap and just get for Int2IntMap
    if(isGeneric) <x>override def apply(x : {T}) = under.get{V}(x);</x>.text
    else <x>override def apply(x : {T}) = under.get(x);</x>.text
    }
    override def update(x : {T}, v : {V}) {{
      val oldV = this(x);
      updateTotal(v-oldV);
      under.put(x,v);
    }}
  }}

  def apply{G}() = new FastMapCounter{G}();

  {
    if(!hasCounterParent) 
      <x>
  private def runtimeClass[T](x : Any) = x.asInstanceOf[AnyRef].getClass

  private val INT = runtimeClass(3);
  private val LNG = runtimeClass(3l);
  private val FLT = runtimeClass(3.0f);
  private val SHR = runtimeClass(3.asInstanceOf[Short]);
  private val DBL = runtimeClass(3.0);

  def apply[T](x : T) : {V}Counter[T] = {{
    runtimeClass(x) match {{
      case INT => Int2{V}Counter().asInstanceOf[{V}Counter[T]];
      case DBL => Double2{V}Counter().asInstanceOf[{V}Counter[T]];
      case FLT => Float2{V}Counter().asInstanceOf[{V}Counter[T]];
      case SHR => Short2{V}Counter().asInstanceOf[{V}Counter[T]];
      case LNG => Long2{V}Counter().asInstanceOf[{V}Counter[T]];
      case _ => {V}Counter().asInstanceOf[{V}Counter[T]];
    }}
  }}
      </x>.text
    else ""  
  }
}}

</x>.text
    
  }

}


GenCounter.main(args);
